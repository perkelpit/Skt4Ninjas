package com.company.skt.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;

public abstract class Fonts {
    
    static String path;
    static HashMap<String, BitmapFont> FontsMap;
    static private FreeTypeFontParameter CV_48p_black_brd2white;
    static private FreeTypeFontParameter CV_32p_black_brd2white;
    static private FreeTypeFontParameter CVc_24p_black_brd2white;
    static private FreeTypeFontParameter CVc_16p_black_brd1white;
    
    public static void boot(String path) {
        Fonts.path = path;
        FontsMap = new HashMap<>();
        
        /* ### FALLBACK / ERROR ### */
        BitmapFont stdFont = new BitmapFont();
        stdFont.setColor(Color.RED);
        FontsMap.put("Arial_15p_Std", stdFont);
    
        /* ### 48P BLACK, BORDER: 2, WHITE ### */
        CV_48p_black_brd2white = new FreeTypeFontParameter();
        CV_48p_black_brd2white.size = 48;
        CV_48p_black_brd2white.color = Color.BLACK;
        CV_48p_black_brd2white.borderWidth = 2;
        CV_48p_black_brd2white.borderColor = Color.WHITE;
        CV_48p_black_brd2white.borderStraight = true;
        CV_48p_black_brd2white.minFilter = Texture.TextureFilter.Linear;
        CV_48p_black_brd2white.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put("CV_48p_black_brd2white",
                     new FreeTypeFontGenerator(
                         Gdx.files.internal(path + "coolvetica_rg.ttf")).generateFont(CV_48p_black_brd2white));
    
        /* ### 32P BLACK, BORDER: 2, WHITE ### */
        CV_32p_black_brd2white = new FreeTypeFontParameter();
        CV_32p_black_brd2white.size = 32;
        CV_32p_black_brd2white.color = Color.BLACK;
        CV_32p_black_brd2white.borderWidth = 2;
        CV_32p_black_brd2white.borderColor = Color.WHITE;
        CV_32p_black_brd2white.borderStraight = true;
        CV_32p_black_brd2white.minFilter = Texture.TextureFilter.Linear;
        CV_32p_black_brd2white.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put("CV_32p_black_brd2white",
                     new FreeTypeFontGenerator(
                         Gdx.files.internal(path + "coolvetica_rg.ttf")).generateFont(CV_32p_black_brd2white));
    
        /* ### 24P BLACK, BORDER: 2, WHITE ### */
        CVc_24p_black_brd2white = new FreeTypeFontParameter();
        CVc_24p_black_brd2white.size = 24;
        CVc_24p_black_brd2white.color = Color.BLACK;
        CVc_24p_black_brd2white.borderWidth = 2;
        CVc_24p_black_brd2white.borderColor = Color.WHITE;
        CVc_24p_black_brd2white.borderStraight = true;
        CVc_24p_black_brd2white.minFilter = Texture.TextureFilter.Linear;
        CVc_24p_black_brd2white.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put("CVc_24p_black_brd2white",
                     new FreeTypeFontGenerator(
                         Gdx.files.internal(path + "coolvetica_condensed_rg.ttf")).generateFont(
                         CVc_24p_black_brd2white));
    
        /* ### 16P BLACK, BORDER: 1, WHITE ### */
        CVc_16p_black_brd1white = new FreeTypeFontParameter();
        CVc_16p_black_brd1white.size = 16;
        CVc_16p_black_brd1white.color = Color.BLACK;
        CVc_16p_black_brd1white.borderWidth = 1;
        CVc_16p_black_brd1white.borderColor = Color.WHITE;
        CVc_16p_black_brd1white.borderStraight = true;
        CVc_16p_black_brd1white.minFilter = Texture.TextureFilter.Linear;
        CVc_16p_black_brd1white.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put(
            "CVc_16p_black_brd1white",
            new FreeTypeFontGenerator(
                Gdx.files.internal(path + "coolvetica_condensed_rg.ttf")).generateFont(CVc_16p_black_brd1white));
    }
    
    public static BitmapFont get(String usage){
        String fontName;
        String mainCase;
        String subCase;
        if (usage.contains("#")) {
            mainCase = usage.substring(0, usage.indexOf("#"));
            subCase = usage.substring(usage.indexOf("#") + 1);
        } else {
            mainCase = usage;
            subCase = "";
        }
        switch(mainCase) {
            case "button":
                fontName = "CV_32p_black_brd2white";
                break;
            case "bigTitle":
                fontName = "CV_48p_black_brd2white";
                break;
            case "medTitle":
                fontName = "CV_32p_black_brd2white";
                break;
            case "medLable":
                fontName = "CV_32p_black_brd2white";
                break;
            case "textField":
                fontName = "CVc_24p_black_brd2white";
                break;
            case "message":
                fontName = "CVc_24p_black_brd2white";
                break;
            default:
                fontName = "Arial_15p_Std";
                break;
        }
        return FontsMap.get(fontName);
    }
    
}
