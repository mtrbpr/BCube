/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torabipour.graph;

import java.io.Serializable;


/**
 *
 * @author Mohammad TRB
 */
public enum TraverseDirection implements Serializable {
    UP(1),
    DOWN(-1),
    BIDIRECTION(1),
    ANY(1);
    
    private int depthIncrement;

    private TraverseDirection(Integer depthIncrementStrategy) {
        this.depthIncrement = depthIncrementStrategy;
    }

    public int getDepthIncrement() {
        return depthIncrement;
    }
}
