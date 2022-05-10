package com.company.skt.lib;

/** Holds all the data connected to a player regarding a single Skat {@link Game}-round*/
public class PlayerData {
    public Hand startHand;
    public Hand currentHand;
    public TrickStack trickStack;
    public int order;
    public boolean plays;
    
    public PlayerData() {
        startHand = new Hand();
        currentHand = new Hand();
        trickStack = new TrickStack();
        order = -1;
        plays = false;
    }
}