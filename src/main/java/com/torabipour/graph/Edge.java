/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torabipour.graph;

/**
 *
 * @author Mohammad TRB
 */
public class Edge<N extends Node> {

    public N from;
    public N to;

    public Edge(N from, N to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public int hashCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(from.getId()));
        sb.append("-");
        sb.append(String.valueOf(to.getId()));
        return sb.toString().hashCode();
    }

}
