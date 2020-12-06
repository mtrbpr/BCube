/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torabipour.vis;

import com.torabipour.graph.Graph;
import com.torabipour.graph.TraverseDirection;
import com.torabipour.graph.WeightSupplier;
import com.torabipour.graph.algorithm.BFS;
import com.torabipour.graph.algorithm.DAGBFS;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author Mohammad TRB
 */
public class GraphGeneratorAbstraction<T extends DrawableNode> {

    private T root;
    private Consumer<T> consumer;
    private Predicate<T> flagChecker;
    private Predicate<T> stopCondition;
    private TraverseDirection dir;
    private WeightSupplier<String> weightSupplier;
    private BFS<T> bfs;
    private Graph<T> graph;
    private PositionSupplier<T> positionSupplier;

    public final static int verticalSpace = 150;
    public final static int horizontalSpace = 250;

    public GraphGeneratorAbstraction(T root, Consumer<T> consumer, Predicate<T> flagChecker, Predicate<T> stopCondition, TraverseDirection dir, WeightSupplier<String> weightSupplier) {
        this.root = root;
        this.consumer = consumer;
        this.flagChecker = flagChecker;
        this.stopCondition = stopCondition;
        this.dir = dir;
        this.weightSupplier = weightSupplier;
    }

    public GraphGeneratorAbstraction(T root, TraverseDirection dir) {
        this.root = root;
        this.dir = dir;
        this.consumer = x -> {
        };
        this.flagChecker = x -> false;
        this.stopCondition = x -> false;
        this.weightSupplier = (src, des) -> "";
    }

    public GraphGeneratorAbstraction(Graph<T> graph, PositionSupplier<T> positionSupplier) {
        this.root = graph.getNodes().iterator().next();
        this.dir = TraverseDirection.ANY;
        this.consumer = x -> {
        };
        this.flagChecker = x -> false;
        this.stopCondition = x -> false;
        this.weightSupplier = (src, des) -> String.valueOf(graph.getWeightSupplier().getWeight(src, des));
        this.graph = graph;
        this.positionSupplier = positionSupplier;
    }

    public VisGraph generateGraph() {

        Set<VisNode> nodes = new HashSet<>();
        Set<VisEdge> edges = new HashSet<>();

        if (graph != null) {
            bfs = graph.getBFS(dir, root, consumer, flagChecker, stopCondition);
        } else if (dir.equals(TraverseDirection.ANY)) {
            bfs = new BFS<T>(dir, root, consumer, flagChecker, stopCondition);
        } else {
            bfs = new DAGBFS<T>(dir, root, consumer, flagChecker, stopCondition);
        }

        Map<Integer, Set<T>> levels = bfs.getLevels();

        Map<Object, Point> position = positionSupplier == null ? determinePositions(levels)
                : graph.getNodes().stream().collect(Collectors.toMap(x -> x.getId(), x -> positionSupplier.determinePosition(x)));

        Map<Object, Set<Object>> adj = graph != null ? graph.getGraph() : bfs.getAdjList();

        Map<Object, Boolean> visited = new HashMap<>();

        adj.keySet().stream().forEach(node -> visited.put(node, false));

        Map<Object, Integer> idMap = new HashMap<>();

        adj.forEach((des, source) -> {
            source.stream().forEach(src -> {
                boolean newEdge = false;

                T desObj = graph != null ? graph.loadById(des) : bfs.loadById(des);
                T srcObj = graph != null ? graph.loadById(src) : bfs.loadById(src);
                if (des != null && !visited.get(des)) {
                    idMap.put(des, desObj.getIntegerId());

                    nodes.add(new VisNode(idMap.get(des), desObj.getNodeLabel(), position.get(des)));
                    visited.put(des, Boolean.TRUE);
                    newEdge = true;
                }
                if (src != null && !visited.get(src)) {
                    idMap.put(src, srcObj.getIntegerId());

                    nodes.add(new VisNode(idMap.get(src), srcObj.getNodeLabel(), position.get(src)));
                    visited.put(src, Boolean.TRUE);
                    newEdge = true;
                }

                edges.add(new VisEdge(idMap.get(des), idMap.get(src), weightSupplier.getWeight(srcObj, desObj)));
            });

        });
        return new VisGraph(edges, nodes);
    }

    private Map<Object, Point> determinePositions(Map<Integer, Set<T>> levels) {

        Map<Object, Point> result = new HashMap<>();
        levels.keySet().forEach((level) -> {
            int i = 0;
            int n = levels.get(level).size();
            int mid = n / 2;

            for (Iterator<T> it = levels.get(level).iterator(); it.hasNext();) {
                T node = it.next();
                result.put(node.getId(), new Point((i - mid) * horizontalSpace, -level * verticalSpace));
                i++;
            }
        });

        return result;
    }

    public Set<T> getNodes() {
        if (bfs == null) {
            generateGraph();
        }
        return bfs.getNodes();
    }

    public BFS getBfs() {
        return bfs;
    }
}
