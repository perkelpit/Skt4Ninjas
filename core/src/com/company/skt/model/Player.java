package com.company.skt.model;

import java.io.Serializable;

public class Player implements Serializable {
    
    private static final long serialVersionUID = 684898298341945719L;
    public String name;
    public Tally tally;
    public boolean isReady;
    
    public Player(String name) {
        this.name = name;
        isReady = false;
    }
    
}