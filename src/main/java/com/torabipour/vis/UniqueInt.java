/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torabipour.vis;

/**
 *
 * @author Mohammad TRB
 */
public class UniqueInt {
    private int num;
    private static UniqueInt instance;
    
    private UniqueInt(){
        num = 1;
    }
    
    public int next(){
        return ++num;
    }
    
    public static UniqueInt getInstance(){
        if(instance == null){
            instance = new UniqueInt();
        }
        return instance;
    }
}
