package com.company.skt.model;

import java.io.*;
import java.util.Properties;

public abstract class Settings {
    
    public static final int APP = 0,
        GAME = 1,
        STD_APP = 10,
        STD_GAME = 11;
    private static Properties appCfg,
        altAppCfg,
        gameCfg;
    private static String appCfgPathStr,
        stdAppCfgPathStr,
        gameCfgPathStr,
        stdGameCfgPathStr,
        cfgFileExtension;
    
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
        return !(altAppCfg == null);
    }
    
    public static void setProperty(int cfg, String key, String value) {
        switch (cfg) {
            case APP:
                if (altAppCfg == null) {
                    altAppCfg = getProperties(APP);
                }
                altAppCfg.setProperty(key, value);
                break;
            
            case GAME:// TODO alt game config
                gameCfg.setProperty(key, value);
                writeTo(gameCfg, gameCfgPathStr);
                break;
            
            default: break;
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
                altAppCfg = loadProperties(STD_APP);
                break;
            case GAME:
                gameCfg = loadProperties(STD_GAME);
                break;
        }
    }
    
    public static void acceptAltAppCfg() {
        appCfg = altAppCfg;// TODO change to accept alt config
        altAppCfg = null;
        writeTo(appCfg, appCfgPathStr);
    }
    
    public static void rejectAltAppCfg() {
        altAppCfg = null;
    }
    
    public static Properties getAltAppCfg() {
        return altAppCfg;
    }
    
}