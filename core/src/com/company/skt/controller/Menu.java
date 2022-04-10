package com.company.skt.controller;

import com.badlogic.gdx.Gdx;
import com.company.skt.Skt;
import com.company.skt.lib.StageScreen;
import com.company.skt.model.Assets;
import com.company.skt.model.SessionData;
import com.company.skt.view.*;

import java.io.IOException;

public class Menu extends StageScreen {
    
    private Session session;
    private SessionData sessionData;
    
    @Override
    public void initialize() {
        super.initialize();
        Assets.finishLoading();
        addStage(new MenuBackground("menuBackground", true));
        addStage(new MainMenuUI("mainMenuUI", true));
        addStage(new SettingsUI("settingsUI"));
        sessionData = SessionData.get();
    }
    
    @Override
    public void update(float dt) {
        super.update(dt);
    }
    
    public void buttonClicked(String buttonName) {
        switch(buttonName) {
            // *** MAIN MENU-CLICKS ***
            case "HOST":
                DebugWindow.setUIFocus(DebugWindow.Focus.Lobby);
                session = new HostSession();
                break;
            case "JOIN":
                DebugWindow.setUIFocus(DebugWindow.Focus.Lobby);
                session = new ClientSession();
                break;
            case "ARCHIVE":
                DebugWindow.println("[MainMenu] archive clicked");
                DebugWindow.setUIFocus(DebugWindow.Focus.Archive);
                break;
            case "SETTINGS":
                DebugWindow.setUIFocus(DebugWindow.Focus.Settings);
                setStageActive("mainMenuUI", false);
                setStageActive("settingsUI", true);
                break;
            case "CREDITS":
                DebugWindow.println("[MainMenu] credits clicked");
                break;
            case "EXIT":
                if(Skt.isDebug()) {
                    Skt.getDebugWindowPositionUpdater().shutdownNow();
                }
                DebugWindow.disposeDebugWindow();
                Gdx.app.exit();
                break;
            // *** SETTINGS MENU-CLICKS ***
            case "CHANGE_NAME":
                DebugWindow.println("[Settings] change name clicked");
                break;
            case "QUIT_SETTINGS":
                DebugWindow.setUIFocus(DebugWindow.Focus.Main);
                setStageActive("mainMenuUI", true);
                setStageActive("settingsUI", false);
                break;
            case "QUIT_LOBBY":
                DebugWindow.setUIFocus(DebugWindow.Focus.Main);
                try {session.stopSession();} catch (IOException e) {e.printStackTrace();}
                setStageActive("lobbyUI", false);
                removeStage("lobbyUI");
                setStageActive("mainMenuUI", true);
                break;
            default :
                DebugWindow.println("buttonName " + buttonName + " in " +
                                   this.getClass().getSimpleName() +  " not found");
        }
    }

    public void event(String eventName) {
        switch(eventName) {
            case "READY_FOR_LOBBY":
                DebugWindow.println("[Menu|Event] ready for lobby");
                Gdx.app.postRunnable(() -> {
                    addStage(new LobbyUI("lobbyUI", true));
                    setStageActive("mainMenuUI", false);
                    ((LobbyUI)findStage("lobbyUI")).updateUI();
                });
                break;
            case "READY_FOR_SUMMARY":
                DebugWindow.println("[Menu|Event] ready for summary");
                // TODO open SummaryUI
                break;
            case "SESSION_DATA_CHANGED":
                DebugWindow.println("[Menu|Event] session data changed");
                if(SessionData.isHost() && session != null) {
                    System.out.println(SessionData.getDataStringForClient());
                    ((HostSession)session).sendStringToAll(SessionData.getDataStringForClient());
                }
                if(findStage("lobbyUI") != null) {
                    ((LobbyUI)findStage("lobbyUI")).updateUI();
                }
                if(Skt.isDebug()) {
                    DebugWindow.update();
                }
                break;
            case "LEAVE_LOBBY":
                DebugWindow.println("[Menu|Event] leave lobby");
                DebugWindow.setUIFocus(DebugWindow.Focus.Main);
                Gdx.app.postRunnable(() -> {
                    setStageActive("mainMenuUI", true);
                    setStageActive("lobbyUI", false);
                    removeStage("lobbyUI");
                });
                try {
                    if(SessionData.isHost()) {
                    
                    }
                    session.stopSession();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
