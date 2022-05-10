package com.company.skt.view;

import com.company.skt.lib.UpdateStage;

/**
 * ### This UI is a Stub by now ### <br>
 * This UI shows up after every {@link com.company.skt.lib.Game Game} and when a new
 * {@link com.company.skt.lib.GameList GameList} is started or a existing one is loaded
 * from the {@link LobbyUI}. <br>
 * It shows the game values of every game played sofar and the points the players have.
 * A chat window is also present and the clients need to click ready for the host beeing able to proceed.
 * */
public class SummaryUI extends UpdateStage {
    
    public SummaryUI(String name) {
        super(name);
        build();
    }
    
    public SummaryUI(String name, boolean active) {
        super(name, active);
        build();
    }
    
    private void build() {
        // TODO do some fancy G-magic
    }
    
    public void updateUI() {
        // TODO update UI
    }
    
}
