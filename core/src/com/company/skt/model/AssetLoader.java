package com.company.skt.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.company.skt.controller.Utils;
import com.company.skt.view.DebugWindow;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

/**
 * {@code AssetLoader} provides resources for the currently set {@link com.company.skt.lib.StageScreen StageScreen}.
 * It uses an {@link AssetManager} as core to do so.
 *
 * @see Assets
 * */
class AssetLoader {
    
    AssetManager aM;
    String basePath;
    String refResPngPath;
    String pngPath;
    String audioPath;
    String screenName;
    String currentResStr;
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
        DebugWindow.println("[AssetLoader] processing resolution settings");
        currentResStr = appCfg.getProperty("resolution_x") + "x" +
                               appCfg.getProperty("resolution_y");
        if(currentResStr.equals(appCfg.getProperty("ref_res"))) {
            pngPath = refResPngPath;
        } else {
            pngPath = refResPngPath + "cache/";
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
        DebugWindow.println("[AssetLoader] loading all assets at once");
        aM.finishLoading();
    }
    
    <T> T get(String fileName) {
        if(fileName.endsWith("png")) {
            return aM.get(pngPath + fileName);
        }
        if(fileName.endsWith("wav") ||
           fileName.endsWith("mp3")) {
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

        if(!pngPath.equals(refResPngPath)) {
            DebugWindow.println("[AssetLoader] not std-resolution -> analyzing assets");
            scaleAndCache();
        } else {
            DebugWindow.println("[AssetLoader] std-resolution detected");
        }
        DebugWindow.println("[AssetLoader] listing asset files");
        for(File file : new File(pngPath).listFiles()) {
            if(file.isFile()) {
                if(file.getName().endsWith("png")) {
                    pngList.add(file.getName());
                }
            }
        }
        for(File file : new File(audioPath).listFiles()) {
            if(file.isFile()) {
                if(file.getName().endsWith("wav")) {
                    soundList.add(file.getName());
                }
                if(file.getName().endsWith("mp3")) {
                    musicList.add(file.getName());
                }
            }
        }
    }
    
    private void scaleAndCache() {
        Array<String> refPngList = new Array<>();
        for(File file : new File(refResPngPath).listFiles()) {
            if(file.isFile()) {
                if(file.getName().endsWith("png")) {
                    refPngList.add(file.getName());
                }
            }
        }
        boolean filesOk = false;
        File cacheFolder = new File(pngPath);
        File cacheCfg = new File(pngPath + "cache.cfg");
        
        // cache folder does not exist : create new folder and cfg, write cache-resolution to cfg
        if(!(cacheFolder.exists())) {
            DebugWindow.println("[AssetLoader] cache folder does not exist");
            if(!cacheFolder.mkdir()) ioError();
            try {
                if(!cacheCfg.createNewFile()) ioError();
            } catch(Exception ignored) {}
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(cacheCfg))) {
                writer.write(currentResStr + "\n");
            } catch(Exception e) {ioError();}
        // cache folder exists
        } else {
            DebugWindow.println("[AssetLoader] cache folder exists");
            // cache folder exists, but cacheCfg does not exist
            if(!(cacheCfg.exists())) {
                DebugWindow.println("[AssetLoader] cache cfg does not exist");
                // create new cacheCfg
                try {
                    if(!cacheCfg.createNewFile()) ioError();
                    DebugWindow.println("cache cfg created @" + cacheCfg.getPath());
                } catch(Exception ignored) {}
                try(BufferedWriter writer = new BufferedWriter(new FileWriter(cacheCfg))) {
                    writer.write(currentResStr + "\n");
                } catch(Exception e) {ioError();}
            // cacheCfg exists
            } else {
                DebugWindow.println("[AssetLoader] cache cfg exists -> reading");
                String cacheResStr = "";
                try(BufferedReader reader = new BufferedReader(new FileReader(cacheCfg))) {
                    cacheResStr = reader.readLine();
                } catch (Exception e) {ioError();}
                if(cacheResStr.equals(currentResStr)) {
                    filesOk = true;
                    DebugWindow.println("[AssetLoader] checking assets files in cache");
                    for(String fileName : refPngList) {
                        if(!(new File(pngPath + fileName).exists())) {
                            filesOk = false;
                        }
                    }
                }
            }
        }
        
        if(!filesOk) {
            DebugWindow.println("[AssetLoader] cached assets need some work");
            DebugWindow.println("[AssetLoader] scaling and caching assets");
            // delete all files in folder
            for(File file : cacheFolder.listFiles()) {
                if(!file.getPath().endsWith("cfg")) {
                    if(!file.delete()) ioError();
                }
            }
            // TODO distinguish between scaling with respect to aspect ratio and not doing so?
            // scale without respect to aspect ratio
            String refRes = appCfg.getProperty("ref_res");
            float scaleFactorX = Float.parseFloat(appCfg.getProperty("resolution_x")) /
                                 Float.parseFloat(refRes.substring(0, refRes.indexOf('x')));
            float scaleFactorY = Float.parseFloat(appCfg.getProperty("resolution_y")) /
                                 Float.parseFloat(refRes.substring(refRes.indexOf('x') + 1));
    
            /* TODO figure out, how to add loading screen
             * timing problem? -> this code is called by Game´s setActiveScreen() */
            for(String pngName : refPngList) {
                Utils.scaleAndCachePng(refResPngPath + pngName, pngPath + pngName,
                                       scaleFactorX, scaleFactorY, Pixmap.Filter.BiLinear);
            }
        } else {
            DebugWindow.println("[AssetLoader] cached assets ok");
        }
    }
    
    private void ioError() {
        JFrame jFrame = new JFrame();
        jFrame.setLocation(Gdx.graphics.getDisplayMode().height/2,
                           Gdx.graphics.getDisplayMode().width/2);
        JOptionPane.showConfirmDialog(jFrame, "FileIO error! App closed! \n" +
              "Ensure correct permission levels for app folder!",
                                      "Error", JOptionPane.DEFAULT_OPTION);
        Gdx.app.exit();
    }
    
}