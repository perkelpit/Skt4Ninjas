package com.company.skt.lib;

import com.company.skt.lib.Hand;
import com.company.skt.lib.TrickStack;

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