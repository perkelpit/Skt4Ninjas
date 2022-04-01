package com.company.skt.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.company.skt.lib.StageScreen;
import com.company.skt.model.Assets;
import com.company.skt.view.MainMenuUI;
import com.company.skt.view.MenuBackground;
import com.company.skt.view.SettingsUI;

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
                break;
            case "JOIN":
                System.out.println("Join clicked");
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
            case "HOST_LOBBYUI":
                // TODO open LobbyUI as host
                break;
            case "LOGGEDIN":
                // TODO open LobbyUI as client
                break;
            case "SUMMARY":
                // TODO open SummaryUI
                break;
            case "LOBBY_UPDATE_GAMECFG":
                // TODO update gameCfg-related view in LobbyUI
                break;
            case "LOBBY_UPDATE_PLAYERS":
                // TODO update players in LobbyUI
                break;
            case "LOBBY_SET_PLAYER0_SELF":
                // TODO set player0 in LobbyUI to own name (as host)
                // TODO can be solved differently?
                break;
            case "LOBBY_SET_PLAYER1_HOST":
                // TODO set player1 in LobbyUI to newly connected player´s name (as host)
                // TODO can be solved differently?
                break;
            case "LOBBY_SET_PLAYER2_HOST":
                // TODO set player2 in LobbyUI to newly connected player´s name (as host)
                // TODO can be solved differently?
                break;
            case "LOBBY_REMOVE_PLAYER1_HOST":
                // TODO remove player1 in LobbyUI because connection lost (as host)
                // TODO can be solved differently?
                break;
            case "LOBBY_REMOVE_PLAYER2_HOST":
                // TODO remove player2 in LobbyUI because connection lost (as host)
                // TODO can be solved differently?
                break;
                
        }
    }
}
