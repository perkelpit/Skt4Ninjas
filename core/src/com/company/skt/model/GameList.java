package com.company.skt.model;

import java.util.ArrayList;

public class GameList extends ArrayList<Game> {
    
    private static final long serialVersionUID = 170357016241913470L;
    private int maxGames;
    private int played;
    private boolean closed;
    
    public GameList(int maxGames) {
        super(maxGames);
        this.maxGames = maxGames;
    }
    
    public void setMaxGames(int maxGames) {
        this.maxGames = maxGames;
    }
    
    public boolean isClosed() {
        return closed;
    }
    
    public void setClosed(boolean closed) {
        this.closed = closed;
    }
    
    /** only add finished games! */
    @Override
    public boolean add(Game game) {
        if(!closed) {
            played++;
            if(played == maxGames) {
                setClosed(true);
            }
            return super.add(game);
        } else {
            return false;
        }
    }
    
}
