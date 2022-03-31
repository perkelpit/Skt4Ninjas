package com.company.skt.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import javax.swing.*;
import java.io.File;
import java.util.Properties;

class AssetLoader {
    
    AssetManager aM;
    String basePath;
    String screenName;
    Array<String> pngList;
    Array<String> soundList;
    Array<String> musicList;
    Properties appCfg;
    
    void boot(String path) {
        this.basePath = path;
        appCfg = Settings.getProperties(Settings.APP);
    }
    
    void setScreen(String screenName) {
        this.screenName = screenName;
        if(aM != null) {
            aM.dispose();
        }
        aM = new AssetManager();
        loadAssets();
    }
    
    private void loadAssets() {
        listFiles();
        for(String fileName : pngList.toArray()) {
            if(fileName.startsWith("atlas_")) {
                aM.load(basePath + screenName + "/" + fileName, TextureAtlas.class);
            } else {
                aM.load(basePath + screenName + "/" + fileName, Texture.class);
            }
        }
        for(String fileName : soundList.toArray()) {
            aM.load(basePath + screenName + "/" + fileName, Sound.class);
        }
        for(String fileName : musicList.toArray()) {
            aM.load(basePath + screenName + "/" + fileName, Music.class);
        }
    }
    
    void finishLoading() {
        aM.finishLoading();
    }
    
    float getProgress() {
        return aM.getProgress();
    }
    
    boolean update(float delta) {
        return aM.update((int)(delta * 1000));
    }
    
    private void rescaleAndCache() {
    
    }
    
    private void listFiles() {
        // *** clear lists ***
        pngList.clear();
        soundList.clear();
        musicList.clear();
        
        // *** scan directories in sub-folder of current screen and prepare directory-list ***
        Array<File> dirs = new Array<>();
        for(File folder : new File(basePath + screenName + "/").listFiles()) {
            if(folder.isDirectory()) {
                dirs.add(folder);
            }
        }
        // if (not reference resolution)
        if(!((appCfg.getProperty("resolution_x") + "x" +
              appCfg.getProperty("resolution_y")).equals(appCfg.getProperty("ref_res")))) {
            for(File folder : dirs.toArray()) {
                // delete and exclude all folders not maching "art", "sound" or reference resolution
                if(!(
                    folder.getName().equals("art") ||
                    folder.getName().equals("sound") ||
                    folder.getName().equals(
                         appCfg.getProperty("resolution_x") + "x" +
                         appCfg.getProperty("resolution_y"))
                )) {
                    if(!folder.delete()) {
                        ioError();
                    }
                    dirs.removeValue(folder, true);
                // exclude reference resolution art folder
                } else {
                    if(folder.getName().equals("art")) {
                        dirs.removeValue(folder, true);
                    }
                }
                rescaleAndCache();
            }
        // else (reference resolution)
        } else {
            //  delete and exclude all folders not maching "art" or "sound"
            for(File folder : dirs.toArray()) {
                if(!(folder.getName().equals("art") || folder.getName().equals("sound"))) {
                    if(!folder.delete()) {
                        ioError();
                    }
                    dirs.removeValue(folder, true);
                }
            }
        }
        for(File folder : dirs.toArray()) {
            for(File file : folder.listFiles()) {
                if(file.isFile()) {
                    switch(file.getName().substring(file.getName().indexOf('.'))) {
                        case "png":
                            pngList.add(file.getName());
                            break;
                        case "wav":
                            soundList.add(file.getName());
                            break;
                        case "mp3":
                            musicList.add(file.getName());
                            break;
                    }
                }
            }
        }
    }
    
    private void ioError() {
        JOptionPane.showConfirmDialog(null, "FileIO-Error! App closed! \n"
                                         + "Ensure correct permission levels for app folder!",
                                      "Error", JOptionPane.DEFAULT_OPTION);
        Gdx.app.exit();
    }
    
}