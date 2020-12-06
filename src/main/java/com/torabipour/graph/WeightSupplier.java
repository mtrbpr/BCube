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
public interface WeightSupplier<T> {
    public T getWeight(Node src, Node des);
}
