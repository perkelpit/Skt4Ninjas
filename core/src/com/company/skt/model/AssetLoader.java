package com.company.skt.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import javax.swing.*;
import java.io.File;
import java.util.Properties;

class AssetLoader {
    
    AssetManager aM;
    String basePath;
    String refResPngPath;
    String pngPath;
    String audioPath;
    String screenName;
    Array<String> pngList;
    Array<String> soundList;
    Array<String> musicList;
    Properties appCfg;
    
    void boot(String path) {
        this.basePath = path;
        appCfg = Settings.getProperties(Settings.APP);
        pngList = new Array<>();
        soundList = new Array<>();
        musicList = new Array<>();
    }
    
    void setScreen(String screenName) {
        this.screenName = screenName;
        refResPngPath = basePath + screenName + "/" + "art/";
        audioPath = basePath + screenName + "/" + "audio/";
        String currentResStr = appCfg.getProperty("resolution_x") + "x" +
                               appCfg.getProperty("resolution_y");
        if(currentResStr.equals(appCfg.getProperty("ref_res"))) {
            pngPath = refResPngPath;
        } else {
            pngPath = basePath + screenName + "/" + currentResStr + "/";
        }
        if(aM != null) {
            aM.dispose();
        }
        aM = new AssetManager();
        listFiles();
        loadAssets();
    }
    
    private void loadAssets() {
        for(String fileName : pngList) {
            if(fileName.startsWith("atlas_")) {
                aM.load(pngPath + fileName, TextureAtlas.class);
            } else {
                aM.load(pngPath + fileName, Texture.class);
            }
        }
        for(String fileName : soundList) {
            aM.load(audioPath + fileName, Sound.class);
        }
        for(String fileName : musicList) {
            aM.load(audioPath + fileName, Music.class);
        }
    }
    
    void finishLoading() {
        aM.finishLoading();
    }
    
    <T> T get(String fileName) {
        if(fileName.substring(fileName.indexOf('.') + 1).equals("png")) {
            return aM.get(pngPath + fileName);
        }
        if(fileName.substring(fileName.indexOf('.') + 1).equals("wav") ||
           fileName.substring(fileName.indexOf('.') + 1).equals("mp3")) {
            return aM.get(audioPath + fileName);
        }
        return null;
    }
    
    float getProgress() {
        return aM.getProgress();
    }
    
    boolean update(float delta) {
        return aM.update((int)(delta * 1000));
    }
    
    private void listFiles() {
        // *** clear lists ***
        pngList.clear();
        soundList.clear();
        musicList.clear();
        System.out.println("listFiles()"); // DEBUG
        if(!pngPath.equals(refResPngPath)) {
            scaleAndCache();
        }
        for(File file : new File(pngPath).listFiles()) {
            if(file.isFile()) {
                if(file.getName().substring(file.getName().indexOf('.') + 1).equals("png")) {
                    pngList.add(file.getName());
                }
            }
        }
        for(File file : new File(audioPath).listFiles()) {
            if(file.isFile()) {
                if(file.getName().substring(file.getName().indexOf('.') + 1).equals("wav")) {
                    soundList.add(file.getName());
                }
                if(file.getName().substring(file.getName().indexOf('.') + 1).equals("mp3")) {
                    musicList.add(file.getName());
                }
            }
        }
    }
    
    private void scaleAndCache() {
        System.out.println("scaleAndCache()"); // DEBUG
        File cacheFolder = new File(pngPath);
        if(!(cacheFolder.exists())) {
            if(!cacheFolder.mkdir()) ioError();
        }
        // TODO distinguish between scaling with respect to aspect ratio and not doing so?
        // scale without respect to aspect ratio
        String refRes = appCfg.getProperty("ref_res");
        float scaleFactorX = Float.parseFloat(appCfg.getProperty("resolution_x")) /
                             Float.parseFloat(refRes.substring(0, refRes.indexOf('x')));
        float scaleFactorY = Float.parseFloat(appCfg.getProperty("resolution_y")) /
                             Float.parseFloat(refRes.substring(refRes.indexOf('x') + 1));
        Array<String> refPngList = new Array<>();
        for(File file : new File(refResPngPath).listFiles()) {
            if(file.isFile()) {
                if(file.getName().substring(file.getName().indexOf('.') + 1).equals("png")) {
                    refPngList.add(file.getName());
                }
            }
        }
        /* TODO figure out, how to add loading screen
        * timing problem? -> this code is called by Game´s setActiveScreen() */
        for(String pngName : refPngList) {
            Utils.scaleAndCachePng(refResPngPath + pngName, pngPath + pngName,
                                   scaleFactorX, scaleFactorY, Pixmap.Filter.BiLinear);
        }

    }
    
    private void ioError() {
        JOptionPane.showConfirmDialog(null, "FileIO-Error! App closed! \n"
                                         + "Ensure correct permission levels for app folder!",
                                      "Error", JOptionPane.DEFAULT_OPTION);
        Gdx.app.exit();
    }
    
}