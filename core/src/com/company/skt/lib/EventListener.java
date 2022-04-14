package com.company.skt.lib;

import java.util.ArrayList;

public abstract class EventListener {
    
    ArrayList<String> observedEvents;
    
    {
        observedEvents = new ArrayList<>();
    }
    
    public EventListener(String... events) {
        for(String event : events) {
            observedEvents.add(event);
        }
    }
    
    public final void eventCheck(String event) {
    
    }
    
    public abstract String event();
    
}
