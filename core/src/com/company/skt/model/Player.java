package com.company.skt.model;

import java.io.Serializable;

public class Player implements Serializable {
    
    private static final long serialVersionUID = 684898298341945719L;
    private String name;
    public Tally tally; // TODO what was that for ???
    public boolean isReady;
    public int connectivity;
    
    public Player(String name) {
        this.name = name;
        isReady = false;
        connectivity = -1;
    }
    
    @Override
    public String toString() {
        return name + " | ready: " + isReady;
    }
}