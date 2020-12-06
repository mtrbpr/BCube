/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torabipour.graph.algorithm;

import com.torabipour.graph.Graph;
import com.torabipour.graph.Node;
import com.torabipour.graph.TraverseDirection;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.wicket.util.collections.ConcurrentHashSet;

/**
 *
 * @author Mohammad TRB
 */
public class BFS<N extends Node> implements Serializable {

    protected TraverseDirection dir;
    protected volatile Queue<N> nextToVisit;
    protected volatile boolean flag;
    protected volatile Set<Object> visited;
    protected Consumer<N> consumer;
    protected Predicate<N> checker;
    protected Predicate<N> stopCondition;
    protected volatile Map<Object, N> predecessor;
    protected volatile Map<Integer, Set<N>> levels;
    protected volatile Map<Object, Set<Object>> adjList;
    protected volatile Map<Object, N> idMap;

    public BFS(TraverseDirection dir, N root, Consumer<N> consumer, Predicate<N> checker, Predicate<N> stopCondition) {

        this.dir = dir;
        this.flag = false;
        this.checker = checker;
        this.consumer = consumer;
        this.stopCondition = stopCondition;
        this.predecessor = new ConcurrentHashMap<>();
        this.levels = new ConcurrentHashMap<>();
        this.adjList = new ConcurrentHashMap<>();
        this.idMap = new ConcurrentHashMap<>();
        idMap.put(root.getId(), root);
        levels.put(0, Stream.of(root).collect(Collectors.toSet()));
        logic(root, consumer, checker, stopCondition);
    }

    protected void logic(N root, Consumer<N> consumer, Predicate<N> checker, Predicate<N> stopCondition) {
        nextToVisit = Stream.of(root).collect(Collectors.toCollection(LinkedList::new));
        visited = new ConcurrentHashSet<>();

        while (!nextToVisit.isEmpty()) {
            N current = nextToVisit.poll();
            if (current == null) {
                return;
            }

            if (checker.test(current)) {
                flag = true;
            }

            if (visited.contains(current.getId())) {
                continue;
            }

            consumer.accept(current);

            if (stopCondition.test(current)) {
                break;
            }

            visited.add(current.getId());

            Map<TraverseDirection, Set<N>> adj = adj(current);

            adj.keySet().stream().forEach(dir -> {
                adj.get(dir).stream().filter(x -> !visited.contains(x.getId())).forEach(x -> updateQueueAndDepth(current, x, dir));
            });
        }
    }

    protected Map<TraverseDirection, Set<N>> adj(N node) {
        Map<TraverseDirection, Set<N>> result = new ConcurrentHashMap<>();
        switch (dir) {
            case ANY:
                result.put(TraverseDirection.ANY, node.getAdjacent());
                break;
            case DOWN:
                result.put(TraverseDirection.DOWN, node.getChild());
                break;
            case UP:
                result.put(TraverseDirection.UP, node.getParent());
                break;
            case BIDIRECTION:
                result.put(TraverseDirection.UP, node.getParent());
                result.put(TraverseDirection.DOWN, node.getChild());
                break;
            default:
                result.put(TraverseDirection.ANY, node.getAdjacent());
                break;
        }
        result.values().stream().flatMap(n -> n.stream()).forEach(x -> idMap.put(x.getId(), x));
        adjList.put(node.getId(), result.values().stream().flatMap(n -> n.stream()).map(x -> x.getId()).collect(Collectors.toSet()));
        return result;
    }

    public boolean flagTriggered() {
        return flag;
    }

    private void updateQueueAndDepth(N pred, N des, TraverseDirection dir) {
        des.setDepth(pred.getDepth() + dir.getDepthIncrement());
        nextToVisit.add(des);
        predecessor.put(des.getId(), pred);
        addToLevel(des.getDepth(), des);
    }

    protected void addToLevel(int level, N node) {

        if (levels.containsKey(level)) {
            levels.get(level).add(node);
        } else {
            levels.put(level, Stream.of(node).collect(Collectors.toCollection(ConcurrentHashSet::new)));
        }
    }

    public Map<Integer, Set<N>> getLevels() {
        return levels;
    }

    public Map<Object, N> getPredecessor() {
        return predecessor;
    }

    public Map<Object, Set<Object>> getAdjList() {
        return adjList;
    }

    public N loadById(Object id) {
        return idMap.get(id);
    }

    public Set<N> getNodes() {
        return new HashSet<>(idMap.values());
    }
}
