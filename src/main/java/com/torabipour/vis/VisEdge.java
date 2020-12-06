/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torabipour.vis;

import java.io.Serializable;

/**
 *
 * @author Mohammad TRB
 */
public class VisEdge implements Serializable{
    private int from;
    private int to;
    private String label;

    public VisEdge(int from, int to, String label) {
        this.from = from;
        this.to = to;
        this.label = label;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
}
