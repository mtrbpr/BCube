/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torabipour.graph;

import com.torabipour.graph.algorithm.BFS;
import com.torabipour.graph.algorithm.GraphBFSProvider;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Mohammad TRB
 */
public class Graph<N extends Node> {

    private Map<Object, Set<Object>> graph;
    private Set<Edge<N>> edges;
    private Map<Object, N> nodes;
    private WeightSupplier<Double> weightSupplier;
    private BFS<N> bfs;

    public Graph() {
        init();
        this.weightSupplier = (a1, a2) -> 1.0;
    }

    public Graph(WeightSupplier<Double> weightSupplier) {
        init();
        this.weightSupplier = weightSupplier;
    }

    private void init() {
        graph = new HashMap<>();
        edges = new HashSet<>();
        nodes = new HashMap<>();
    }

    public void addEdge(N from, N to) {
        nodes.put(to.getId(), to);
        nodes.put(from.getId(), from);
        updateAdj(from.getId(), to.getId());
        edges.add(new Edge(from, to));

    }

    private void updateAdj(Object from, Object to) {
        if (graph.get(from) == null) {
            graph.put(from, Stream.of(to).collect(Collectors.toCollection(HashSet::new)));
        } else {
            graph.get(from).add(to);
        }
    }

    public Set<N> adj(N node) {
        return adj(node.getId()).stream().map(x -> nodes.get(x)).collect(Collectors.toCollection(HashSet::new));
    }

    private Set<Object> adj(Object node) {
        return graph.get(node);
    }

    public Set<Edge<N>> getEdges() {
        return edges;
    }

    public Set<N> getNodes() {
        return new HashSet<>(nodes.values());
    }

    public int getSize() {
        return nodes.keySet().size();
    }

    public BFS<N> getBFS(TraverseDirection dir, N root, Consumer<N> consumer, Predicate<N> checker, Predicate<N> stopCondition) {
        if (this.bfs == null) {
            this.bfs = new GraphBFSProvider<N>(this, root, consumer, checker, stopCondition).getBfs();
        }
        return this.bfs;
    }

    public WeightSupplier<Double> getWeightSupplier() {
        return weightSupplier;
    }
    
    public Boolean areAdj(N src, N des){
        return graph.get(src.getId()).contains(des.getId());
    }

    public Map<Object, Set<Object>> getGraph() {
        return graph;
    }
    
     public N loadById(Object id) {
        return nodes.get(id);
    }

}
