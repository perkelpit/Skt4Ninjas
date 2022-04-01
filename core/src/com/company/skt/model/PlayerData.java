package com.company.skt.model;

class PlayerData {
    Hand startHand,
        currentHand;
    TrickStack trickStack;
    int order;
    boolean plays;
    
    PlayerData() {
        startHand = new Hand();
        currentHand = new Hand();
        trickStack = new TrickStack();
        order = -1;
        plays = false;
    }
}
