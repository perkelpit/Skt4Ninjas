package com.company.skt.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.company.skt.lib.StageScreen;
import com.company.skt.lib.UpdateStage;
import com.company.skt.model.Assets;
import com.company.skt.view.MainMenuUI;
import com.company.skt.view.MenuBackground;
import com.company.skt.view.SettingsUI;

public class Menu extends StageScreen {
    
    @Override
    public void initialize() {
        super.initialize();
        Assets.finishLoading();
        addStage(new MenuBackground("menuBackground"));
        addStage(new MainMenuUI("mainMenuUI"));
        addStage(new SettingsUI("settingsUI"));
        findStage("settingsUI").setActive(false);
    }
    
    @Override
    public void update(float dt) {
        super.update(dt);
    }
    
    @Override
    public AssetManager getAssets() {
        return null;
    }
    
    public void buttonClicked(String buttonName){
        switch(buttonName) {
            // *** MAINMENU-CLICKS ***
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
                findStage("mainMenuUI").setActive(false);
                findStage("settingsUI").setActive(true);
                break;
            case "CREDITS":
                System.out.println("Credits clicked");
                break;
            case "EXIT":
                // TODO DEBUG This first one causes the "AL lib: alc_cleanup: 1 device not closed"-Error
                // DEBUG The second one leaves the "non-zero-exit value" in place and may cause problems on iOS
                //System.exit(0);
                Gdx.app.exit();
                break;
            // *** SETTINGSMENU-CLICKS ***
            case "CHANGE_NAME":
                System.out.println("Change name clicked");
                break;
            case "QUIT_SETTINGS":
                System.out.println("Quit settings clicked");
                findStage("mainMenuUI").setActive(true);
                findStage("settingsUI").setActive(false);
                break;
            default :
                System.out.println("buttonName " + buttonName +" in " + this.getClass().getSimpleName() +  " not found");
        }
    }
}
