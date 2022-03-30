package com.company.skt.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public abstract class Local {
    
    public static final int ENG = 0;
    public static final int DEU = 1;
    private static int lang;
    private static Properties local;
    private static Properties appCfg;
    private static String localPathStr;
    private static String localFileExtension;
    
    public static void boot(String path) {
        appCfg = Settings.getProperties(Settings.APP);
        lang = Integer.parseInt(appCfg.getProperty("lang"));
        localFileExtension = ".txt";
        String langStr;
        switch(lang) {
            case 1:
                langStr = "deu";
                break;
            case 0:
            default:
                langStr = "eng";
                break;
        }
        localPathStr = path + langStr + localFileExtension;
        loadLocal();
    }
    
    public static void setLanguage(int lang) {
        Local.lang = lang;
    }
    
    public static String getString(String key) {
        String s = local.getProperty(key);
        if (s == null) {
            return key;
        }
        return s;
    }
    
    private static void loadLocal() {
        Properties p = new Properties();
        try (InputStreamReader isr = new InputStreamReader(
            new FileInputStream(localPathStr), StandardCharsets.UTF_8)) {
            p.load(isr);
        } catch (IOException e) {e.printStackTrace();}
        local = p;
    }
}