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
        aL.setScreen(screen.getClass().getSimpleName());
    }
    
    public static void boot(String path) {
        aL.boot(path);
    }
    
    public static void finishLoading() {
        aL.finishLoading();
    }
    
    public static synchronized <T> T get(String fileName) {
        return aL.get(fileName);
    }
    
    public static float getProgress() {
        return aL.getProgress();
    }
    
    public static boolean update(float delta) {
        return aL.update(delta);
    }
    
}
