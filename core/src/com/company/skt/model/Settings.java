package com.company.skt.model;

import java.io.*;
import java.util.Properties;

/**
 * {@code Settings} provides {@code APP} and {@code GAME} settings and their standard variants. <br>
 * The class has to be booted by calling {@link #boot(String)} setting the cfg-path before use.
 * <p>
 * Note: After changing a property via {@link #setProperty(int, String, String)} it&acute;s necessary to call
 * {@link #acceptTempAppCfg()} resp. {@link #acceptTempGameCfg()} to make those changes permanent. </p><br>
 * */

public abstract class Settings {
    
    public static final int APP = 0;
    public static final int GAME = 1;
    public static final int STD_APP = 10;
    public static final int STD_GAME = 11;
    
    private static Properties appCfg;
    private static Properties tempAppCfg;
    private static Properties gameCfg;
    private static Properties tempGameCfg;
    
    private static String appCfgPathStr;
    private static String stdAppCfgPathStr;
    private static String gameCfgPathStr;
    private static String stdGameCfgPathStr;
    private static String cfgFileExtension;
    
    public static void boot(String path) {
        appCfg = null;
        gameCfg = null;
        cfgFileExtension = ".txt";
        
        appCfgPathStr = path + "app" + cfgFileExtension;
        stdAppCfgPathStr = path + "std_app" + cfgFileExtension;
        gameCfgPathStr = path + "game" + cfgFileExtension;
        stdGameCfgPathStr = path + "std_game" + cfgFileExtension;
        
        appCfg = getProperties(APP);
        gameCfg = getProperties(GAME);
    }
    
    public static boolean isAltered() {
        return !(tempAppCfg == null);
    }
    
    public static void setProperty(int cfg, String key, String value) {
        switch (cfg) {
            case APP:
                if(tempAppCfg == null) {
                    tempAppCfg = getProperties(APP);
                }
                tempAppCfg.setProperty(key, value);
                break;
            case GAME:
                if(tempGameCfg == null) {
                    tempGameCfg = getProperties(GAME);
                }
                tempGameCfg.setProperty(key, value);
                break;
        }
    }
    
    public static Properties getProperties(int cfg) {
        switch (cfg) {
            case APP: {
                if (appCfg == null) {
                    appCfg = loadProperties(APP);
                    if (appCfg == null) {
                        appCfg = loadProperties(STD_APP);
                    }
                }
                return appCfg;
            }
            case GAME: {
                if (gameCfg == null) {
                    gameCfg = loadProperties(GAME);
                    if (gameCfg == null) {
                        gameCfg = loadProperties(STD_GAME);
                    }
                }
                return gameCfg;
            }
            default:
                return null;
        }
    }
    
    private static Properties loadProperties(int cfg) {
        Properties properties;
        switch (cfg) {
            case APP :
                properties = loadFrom(appCfgPathStr);
                break;
            case GAME :
                properties = loadFrom(gameCfgPathStr);
                break;
            case STD_APP :
                properties = loadFrom(stdAppCfgPathStr);
                break;
            case STD_GAME :
                properties = loadFrom(stdGameCfgPathStr);
                break;
            default :
                properties = null;
                break;
        }
        return properties;
    }
    
    private static Properties loadFrom(String path) {
        Properties p = new Properties();
        if (new File(path).exists()) {
            try (InputStream is = new FileInputStream(path)) {
                p.load(is);
            } catch (IOException e) {e.printStackTrace();
            }
            return p;
        } else {
            return null;
        }
    }
    
    private static void writeTo(Properties cfg, String path) {
        try (OutputStream out = new FileOutputStream(path)) {
            cfg.store(out, null);
        } catch (IOException e) {e.printStackTrace();
        }
    }
    
    public static void setToDefault(int cfg) {
        switch (cfg) {
            case APP:
                tempAppCfg = loadProperties(STD_APP);
                break;
            case GAME:
                gameCfg = loadProperties(STD_GAME);
                break;
        }
    }
    
    public static void acceptTempAppCfg() {
        appCfg = tempAppCfg;
        tempAppCfg = null;
        writeTo(appCfg, appCfgPathStr);
    }
    
    public static void acceptTempGameCfg() {
        gameCfg = tempGameCfg;
        tempGameCfg = null;
        writeTo(gameCfg, gameCfgPathStr);
    }
    
    public static void rejectTempAppCfg() {
        tempAppCfg = null;
    }
    
    public static void rejectTempGameCfg() {
        tempGameCfg = null;
    }
    
    public static Properties getTempAppCfg() {
        return tempAppCfg;
    }
    
    public static Properties getTempGameCfg() {
        return tempGameCfg;
    }
}