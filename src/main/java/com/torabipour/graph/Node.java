/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torabipour.graph;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Mohammad TRB
 */
public abstract class Node<Identifier, Data> implements Serializable{

    protected Identifier id;

    protected Data data;

    public Node(Identifier id, Data data) {
        this.id = id;
        this.data = data;
    }
    
    private int depth;
    
    protected Set<Node> cachedParent;
    protected Set<Node> cachedChildren;
    protected Set<Node> cachedAdj;    

    public abstract Set<Node> getParent();

    public abstract Set<Node> getChild();
    
    public abstract Set<Node> getAdjacent();

    public int getDepth() {
        return depth;
    }
    
    public void setDepth(int depth){
        this.depth = depth;
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
