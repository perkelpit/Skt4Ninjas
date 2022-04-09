package com.company.skt.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.company.skt.Skt;
import com.company.skt.lib.StageScreen;
import com.company.skt.model.Assets;
import com.company.skt.view.*;

import java.io.IOException;

public class Menu extends StageScreen {
    
    Session session;
    
    @Override
    public void initialize() {
        super.initialize();
        Assets.finishLoading();
        addStage(new MenuBackground("menuBackground", true));
        addStage(new MainMenuUI("mainMenuUI", true));
        addStage(new SettingsUI("settingsUI"));

    }
    
    @Override
    public void update(float dt) {
        super.update(dt);
    }
    
    @Override
    public AssetManager getAssets() {
        return null;
    }
    
    public void buttonClicked(String buttonName) {
        switch(buttonName) {
            // *** MAIN MENU-CLICKS ***
            case "HOST":
                DebugWindow.println("Host clicked");
                DebugWindow.setUIFocus(DebugWindow.Focus.Lobby);
                session = new HostSession();
                break;
            case "JOIN":
                DebugWindow.println("Join clicked");
                DebugWindow.setUIFocus(DebugWindow.Focus.Lobby);
                session = new ClientSession();
                break;
            case "ARCHIVE":
                DebugWindow.println("Archive clicked");
                DebugWindow.setUIFocus(DebugWindow.Focus.Archive);
                break;
            case "SETTINGS":
                DebugWindow.println("Settings clicked");
                DebugWindow.setUIFocus(DebugWindow.Focus.Settings);
                setStageActive("mainMenuUI", false);
                setStageActive("settingsUI", true);
                break;
            case "CREDITS":
                DebugWindow.println("Credits clicked");
                break;
            case "EXIT":
                DebugWindow.println("Exit clicked");
                try {Thread.sleep(500);}
                catch(InterruptedException e) {e.printStackTrace();}
                Skt.getDebugWindowPositionUpdater().shutdownNow();
                DebugWindow.disposeDebugWindow();
                Gdx.app.exit();
                break;
            // *** SETTINGS MENU-CLICKS ***
            case "CHANGE_NAME":
                DebugWindow.println("Change name clicked");
                break;
            case "QUIT_SETTINGS":
                DebugWindow.println("Quit settings clicked");
                DebugWindow.setUIFocus(DebugWindow.Focus.Main);
                setStageActive("mainMenuUI", true);
                setStageActive("settingsUI", false);
                break;
            case "QUIT_LOBBY":
                DebugWindow.println("Quit Lobby clicked");
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
                DebugWindow.println("Event: READY_FOR_LOBBY");
                Gdx.app.postRunnable(() -> {
                    addStage(new LobbyUI("lobbyUI", true));
                    setStageActive("mainMenuUI", false);
                    ((LobbyUI)findStage("lobbyUI")).updateUI();
                });
                DebugWindow.update();
                break;
            case "READY_FOR_SUMMARY":
                DebugWindow.println("Event: READY_FOR_SUMMARY");
                // TODO open SummaryUI
                break;
            case "LOBBY_DATA_HAS_CHANGED":
                DebugWindow.println("Event: LOBBY_DATA_HAS_CHANGED");
                // TODO update lobbyUI accordingly + if(host): broadcast changes
                DebugWindow.update();
                if(findStage("lobbyUI") != null) {
                    ((LobbyUI)findStage("lobbyUI")).updateUI();
                }
                break;
            case "CONNECTION_WARNING_PLAYER_1":
                DebugWindow.println("Event: CONNECTION_WARNING_PLAYER_1");
                // TODO p1
                break;
            case "CONNECTION_WARNING_PLAYER_2":
                DebugWindow.println("Event: CONNECTION_WARNING_PLAYER_2");
                // TODO p2
                break;
            case "LEAVE_LOBBY":
                DebugWindow.println("Event: LEAVE_LOBBY");
                DebugWindow.setUIFocus(DebugWindow.Focus.Main);
                setStageActive("mainMenuUI", true);
                setStageActive("lobbyUI", false);
                removeStage("lobbyUI");
                try {
                    session.stopSession();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
