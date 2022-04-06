package com.company.skt.lib;

import java.util.ArrayList;

/* Methodes:
 * */

public class TrickStack extends ArrayList<Trick> {
    private boolean closed;
    
    public TrickStack() {
        closed = false;
    }
    
    public void close() {
        closed = true;
    }
    
    public boolean isClosed() {
        return closed;
    }
    
}
