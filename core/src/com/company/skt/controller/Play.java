package com.company.skt.controller;

import com.company.skt.lib.StageScreen;
import com.company.skt.model.Assets;
import com.company.skt.model.SessionData;
import com.company.skt.view.*;

/**
 * ### Currently a Stub/Sceletton ### <br>
 * {@code Play} is the {@link StageScreen} for all those menus and scenes from the beginning of the actual game session
 * to it´s end: <br>
 * {@link SummaryUI} and {@link GameUI}. <br>
 * It handles all the {@code clicks} and {@code events} happening within those UIs and corresponding
 * controller- and model-classes via {@link #click(String)} and {@link #event(String)}. */
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
    
    public void click(String buttonName) {
        switch(buttonName) {
            case "DUMMY1_DONT_ANNOY_ME_INTELLIJ":
                // TODO fill DUMMY1
                break;
            case "DUMMY2_DONT_ANNOY_ME_INTELLIJ":
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
                    ((GameUI)findStage("gameUI")).updateUI();
                }
                if(findStage("summaryUI") != null) {
                    ((SummaryUI)findStage("summaryUI")).updateUI();
                }
                break;
            case "DUMMY1_DONT_ANNOY_ME_INTELLIJ":
                // TODO fill DUMMY1
                break;
        }
    }
    
}
