/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torabipour.graph.algorithm;

import com.torabipour.graph.Node;
import com.torabipour.graph.TraverseDirection;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.wicket.util.collections.ConcurrentHashSet;

/**
 *
 * @author Mohammad TRB optimized BFS for directed acyclic graphs
 */
public class DAGBFS<N extends Node> extends BFS<N> implements Serializable {

    private volatile boolean shouldStop;

    protected volatile Queue<ArrayList<N>> nextToVisit;

    public DAGBFS(TraverseDirection dir, N root, Consumer<N> consumer, Predicate<N> checker, Predicate<N> stopCondition) {
        super(dir, root, consumer, checker, stopCondition);
    }

    @Override
    protected void logic(N root, Consumer<N> consumer, Predicate<N> checker, Predicate<N> stopCondition) {
        nextToVisit = Stream.of(Stream.of(root).collect(Collectors.toCollection(ArrayList::new))).collect(Collectors.toCollection(LinkedList::new));
        visited = new ConcurrentHashSet<>();
        shouldStop = false;
        while (!nextToVisit.isEmpty() && !shouldStop) {
            List<N> currentList = nextToVisit.poll();
            if (currentList == null || currentList.isEmpty()) {
                return;
            }

            currentList.parallelStream().forEach(this::forEachNode);

        }
    }

    private void forEachNode(N current) {
        if (checker.test(current)) {
            flag = true;
        }

        if (visited.contains(current.getId())) {
            return;
        }

        consumer.accept(current);

        if (stopCondition.test(current)) {
            shouldStop = true;
            return;
        }

        visited.add(current.getId());

        Map<TraverseDirection, Set<N>> adj = adj(current);

        adj.keySet().stream().forEach(dir -> {
            ArrayList<N> toAdd = adj.get(dir).stream().filter(x -> !visited.contains(x.getId())).collect(Collectors.toCollection(ArrayList::new));
            toAdd.forEach(x -> {
                predecessor.put(x.getId(), current);
                x.setDepth(current.getDepth() + dir.getDepthIncrement());
                addToLevel(x.getDepth(), x);
            });
            if (!toAdd.isEmpty()) {
                DAGBFS.this.nextToVisit.add(toAdd);
            }
        });

    }
    

}
