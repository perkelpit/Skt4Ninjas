package com.company.skt.controller;

import com.badlogic.gdx.assets.AssetManager;
import com.company.skt.lib.StageScreen;
import com.company.skt.lib.UpdateStage;
import com.company.skt.view.MainMenuUI;
import com.company.skt.view.MenuBackground;

public class Menu extends StageScreen {
    
    @Override
    public void initialize() {
        super.initialize();
        addStage(new MenuBackground());
        addStage(new MainMenuUI());
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
                break;
            case "CREDITS":
                System.out.println("Credits clicked");
                break;
            case "EXIT":
                System.exit(0);
                break;
            default :
                System.out.println("buttonName " + buttonName +" in " + this.getClass().getSimpleName() +  " not found");
        }
    }
}
