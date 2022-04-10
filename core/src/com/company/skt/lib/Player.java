package com.company.skt.lib;

public class Player {
    
    public static final int CONNECTION_LOST = -1;
    public static final int CONNECTION_WARNING = 0;
    public static final int CONNECTION_OK = 1;
    private String name;
    public Tally tally; // TODO what was that for ???
    private boolean isReady;
    private int connectivity;
    
    public Player(String name) {
        this.name = name;
        isReady = false;
        connectivity = -1;
    }
    
    @Override
    public String toString() {
        String conStr = "";
        switch(connectivity) {
            case 1:
                conStr = "OK";
                break;
            case 0:
                conStr = "WARNING";
                break;
            case -1:
                conStr = "LOST";
                break;
        }
        return name + " | ready: " + isReady + " | " + conStr;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isReady() {
        return isReady;
    }
    
    public void setReady(boolean ready) {
        isReady = ready;
    }
    
    public int getConnectivity() {
        return connectivity;
    }
    
    public void setConnectivity(int connectivity) {
        this.connectivity = connectivity;
    }
}