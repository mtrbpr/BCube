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
public class VisNode implements Serializable{
    private int id;
    private String label;
    public int x;
    public int y;

    public VisNode(int id, String label, int x, int y) {
        this.id = id;
        this.label = label;
        this.x = x;
        this.y = y;
    }
    
    public VisNode(int id, String label, Point p) {
        this.id = id;
        this.label = label;
        this.x = p.x;
        this.y = p.y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
