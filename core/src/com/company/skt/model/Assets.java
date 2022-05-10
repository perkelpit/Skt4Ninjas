package com.company.skt.model;

import com.badlogic.gdx.assets.AssetManager;
import com.company.skt.lib.StageScreen;

/**
 * {@code Assets} is a wrapper class for the {@link AssetLoader AssetLoader} class with the purpose
 * of providing assets for the currently set {@link com.company.skt.lib.StageScreen StageScreen} while not holding
 * a static resource in libGDX. Before usage,
 * {@link #boot(String)} has to be called defining a base path in whoms subfolders, exactly named like the
 * corresponding {@code StageScreen}-subclasses, the assets are placed.
 * <p>
 * Note: Though the assets to load into RAM are automatically listed by file extension (only {@code PNG, WAV} and
 * {@code MP3} are supported), {@link #finishLoading()} or {@link #update(float)} according to the use specified by
 * {@link AssetLoader} has to be called to actually load those assets before retrieving them.
 * </p>
 * */
public abstract class Assets {
    
    private static AssetLoader aL;
    
    static {
        aL = new AssetLoader();
    }
    
    public static AssetManager getAssetManager() {
        return aL.aM;
    }
    
    public static void setCurrentScreen(StageScreen screen) {
        if(screen != null) {
            aL.setScreen(screen.getClass().getSimpleName());
        }
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
