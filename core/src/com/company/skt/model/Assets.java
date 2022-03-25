package com.company.skt.model;

import com.badlogic.gdx.assets.AssetManager;
import com.company.skt.lib.StageScreen;

public abstract class Assets {
    
    private static AssetLoader aL;
    
    static {
        aL = new AssetLoader();
    }
    
    public static AssetManager getAssets() {
        return aL.aM;
    }
    
    public static void setCurrentScreen(StageScreen screen) {
        aL.setAssets(screen.getClass().getSimpleName());
    }
    
}
