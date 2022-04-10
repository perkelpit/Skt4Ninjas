package com.company.skt.controller;

import com.company.skt.lib.StageScreen;
import com.company.skt.model.Assets;
import com.company.skt.model.SessionData;
import com.company.skt.view.*;

public class Play extends StageScreen {
    
    private Session session;
    private SessionData sessionData;
    
    public Play(Session session, SessionData sessionData) {
        this.session = session;
        this.sessionData = sessionData;
    }
    
    @Override
    public void initialize() {
        super.initialize();
        // TODO initializing stuff
        // TODO load Assets via Assets.finishLoading() or Assets.update()
    }
    
    @Override
    public void update(float dt) {
        super.update(dt);
        // TODO update stuff or remove method
    }
    
    public void buttonClicked(String buttonName) {
        switch(buttonName) {
            case "DUMMY1_DONT_ANOY_ME_INTELLIJ":
                // TODO fill DUMMY1
                break;
            case "DUMMY2_DONT_ANOY_ME_INTELLIJ":
                // TODO fill DUMMY2
                break;
        }
    }
    
    public void event(String eventName) {
        switch(eventName) {
            case "SESSION_DATA_CHANGED":
                DebugWindow.println("[Play|Event] session data changed");
                if(SessionData.isHost()) {
                    ((HostSession)session).sendStringToAll(SessionData.getDataStringForClient());
                }
                if(findStage("gameUI") != null) {
                    ((GameUI)findStage("playUI")).updateUI();
                }
                if(findStage("summaryUI") != null) {
                    ((SummaryUI)findStage("summaryUI")).updateUI();
                }
                break;
            case "DUMMY1_DONT_ANOY_ME_INTELLIJ":
                // TODO fill DUMMY1
                break;
        }
    }
    
}
