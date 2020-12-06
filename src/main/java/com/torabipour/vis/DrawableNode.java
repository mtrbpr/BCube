/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torabipour.vis;

import com.torabipour.graph.Node;
import java.util.List;

/**
 *
 * @author Mohammad TRB
 */
public abstract class DrawableNode<Identifier, Data> extends Node<Identifier, Data> {

    private int intId;
    private String nodeLabel;

    public DrawableNode(Identifier id, Data data, int intId, String nodeLabel) {
        super(id, data);
        this.intId = intId;
        this.nodeLabel = nodeLabel;
    }

    public String getNodeLabel() {
        return nodeLabel;
    }

    public Integer getIntegerId() {
        return intId;
    }
}
