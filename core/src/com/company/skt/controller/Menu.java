package com.company.skt.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.company.skt.lib.StageScreen;
import com.company.skt.model.Assets;
import com.company.skt.view.*;

public class Menu extends StageScreen {
    
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
                System.out.println("Host clicked");
                new HostSession();
                break;
            case "JOIN":
                System.out.println("Join clicked");
                new ClientSession();
                break;
            case "ARCHIVE":
                System.out.println("Archive clicked");
                break;
            case "SETTINGS":
                System.out.println("Settings clicked");
                setStageActive("mainMenuUI", false);
                setStageActive("settingsUI", true);
                break;
            case "CREDITS":
                System.out.println("Credits clicked");
                break;
            case "EXIT":
                Gdx.app.exit();
                break;
            // *** SETTINGS MENU-CLICKS ***
            case "CHANGE_NAME":
                System.out.println("Change name clicked");
                break;
            case "QUIT_SETTINGS":
                System.out.println("Quit settings clicked");
                setStageActive("mainMenuUI", true);
                setStageActive("settingsUI", false);
                break;
            default :
                System.out.println("buttonName " + buttonName +" in " + this.getClass().getSimpleName() +  " not found");
        }
    }
    
    public void event(String eventName) {
        switch(eventName) {
            case "LOBBY_ENTERED":
                addStage(new LobbyUI("lobbyUI",true ));
                setStageActive("mainMenuUI", false);
                // [TEMP]
                System.out.println("Lobby entered.");
                //ConsoleView.printLobbyData();
                break;
            case "LOGGEDIN":
                addStage(new LobbyUI("lobbyUI", true));
                setStageActive("mainMenuUI", false);
                break;
            case "SUMMARY":
                // TODO open SummaryUI
                break;
            case "LOBBY_DATA_HAS_CHANGED":
                // TODO update lobbyUI accordingly + if(host): broadcast changes
                break;
            case "CONNECTION_WARNING_PLAYER_1":
                // TODO p1
                break;
            case "CONNECTION_WARNING_PLAYER_2":
                // TODO p2
                break;
        }
    }
}
