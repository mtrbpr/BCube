/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torabipour.vis;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Mohammad TRB
 */
public class VisGraph implements Serializable{

    private Set<VisEdge> edges;
    private Set<VisNode> nodes;

    public VisGraph(Set<VisEdge> edges, Set<VisNode> nodes) {
        this.edges = edges;
        this.nodes = nodes;
    }

    public Set<VisEdge> getEdges() {
        return edges;
    }

    public void setEdges(Set<VisEdge> edges) {
        this.edges = edges;
    }

    public Set<VisNode> getNodes() {
        return nodes;
    }

    public void setNodes(Set<VisNode> nodes) {
        this.nodes = nodes;
    }

   
}
