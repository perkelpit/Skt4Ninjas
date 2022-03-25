package com.company.skt.model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

class AssetLoader {
    
    AssetManager aM;
    
    void setAssets(String screenName) {
        if(aM != null) {
            aM.dispose();
        }
        aM = new AssetManager();
        loadAssets(screenName);
        aM.finishLoading();
    }
    
    private void loadAssets(String screenName) {
        switch(screenName) {
            case "Menu":
                aM.load("assets/art/menu/Background.png", Texture.class);
                aM.load("assets/art/menu/ButtonTexture.png", Texture.class);
                aM.load("assets/art/menu/ButtonTexturePressed.png", Texture.class);
                aM.load("assets/art/menu/skat1444x900.png", Texture.class);
                aM.load("assets/art/menu/buttonDebug.png", Texture.class);
                break;
            case "Game":
                break;
        }
    }
    
}