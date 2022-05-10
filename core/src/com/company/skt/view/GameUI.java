package com.company.skt.view;

import com.company.skt.lib.UpdateStage;

/** The UI where the actual game, a round of Skat, takes place. <br>
 * A stub by now.*/
public class GameUI extends UpdateStage {
    
    public GameUI(String name) {
        super(name);
        build();
    }
    
    public GameUI(String name, boolean active) {
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
