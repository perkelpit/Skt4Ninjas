package com.company.skt.model;

import java.io.Serializable;

public class Player implements Serializable {
    
    private static final long serialVersionUID = 684898298341945719L;
    private String name;
    public Tally tally; // TODO what was that for ???
    public boolean isReady;
    
    public Player(String name) {
        this.name = name;
        isReady = false;
    }
    
    @Override
    public String toString() {
        return name + " | ready: " + isReady;
    }
}