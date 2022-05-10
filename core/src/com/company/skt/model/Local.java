package com.company.skt.model;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * {@code Local} provides all the language-dependand {@code Strings} for the UIs. <br>
 * Use {@link #get(String)} in UI-classes.
 * <p>
 * Note: {@code Local}-class has to be booted via calling {@link #boot(String)} to set the path where the
 * language-files are located before retrieving a {@code String}! <br><br>
 * New languages can be implemented by additions in the {@code switch-case} in {@link #boot(String)} and adding
 * appropriatly named {@code TXT}-files. <br>
 * Localized {@code Strings} have to of the following sheme: <br>
 * "{@code key=localizedString}"
 * </p><br>
 * This class uses a {@link Properties}-object internally to manage the currently set language.
 * */
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
    
    public static String get(String key) {
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