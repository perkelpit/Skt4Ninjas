package com.company.skt.lib;

import java.util.ArrayList;

/** Holds all the tricks earned by a {@link Player} in a single {@link Game}.
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
