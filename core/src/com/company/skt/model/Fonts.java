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
    
    public static void boot(String path) {
        Fonts.path = path;
        FontsMap = new HashMap<>();
        
        /* ### FALLBACK / ERROR ### */
        BitmapFont stdFont = new BitmapFont();
        FontsMap.put("Arial_15p_white", stdFont);
        
        generateFonts();
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
                fontName = "CV_28p_black_brd2lightGray";
                break;
            case "bigTitle":
                fontName = "CV_48p_black_brd2lightGray";
                break;
            case "medTitle":
                fontName = "CV_28p_black_brd2lightGray";
                break;
            case "medLable":
                fontName = "CV_28p_black_brd2lightGray";
                break;
            case "textField":
                fontName = "CVc_20p_black_brd1lightGray";
                break;
            case "message":
                fontName = "CVc_24p_black_brd2lightGray";
                break;
            default:
                fontName = "Arial_15p_white";
                break;
        }
        return FontsMap.get(fontName);
    }
    
    private static void generateFonts() {
        FreeTypeFontParameter fontParams = new FreeTypeFontParameter();
    
        /* ### 48P BLACK, BORDER: 2, LIGHT_GRAY ### */
        fontParams.size = 48;
        fontParams.color = Color.BLACK;
        fontParams.borderWidth = 2;
        fontParams.borderColor = Color.LIGHT_GRAY;
        fontParams.borderStraight = true;
        fontParams.minFilter = Texture.TextureFilter.Linear;
        fontParams.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put("CV_48p_black_brd2lightGray",
                     new FreeTypeFontGenerator(
                         Gdx.files.internal(path + "coolvetica_rg.ttf")).generateFont(fontParams));
    
        /* ### 28P BLACK, BORDER: 2, LIGHT_GRAY ### */
        fontParams.size = 28;
        fontParams.color = Color.BLACK;
        fontParams.borderWidth = 2;
        fontParams.borderColor = Color.LIGHT_GRAY;
        fontParams.borderStraight = true;
        fontParams.minFilter = Texture.TextureFilter.Linear;
        fontParams.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put("CV_28p_black_brd2lightGray",
                     new FreeTypeFontGenerator(
                         Gdx.files.internal(path + "coolvetica_rg.ttf")).generateFont(fontParams));
    
        /* ### 24P BLACK, BORDER: 2, LIGHT_GRAY ### */
        fontParams.size = 24;
        fontParams.color = Color.BLACK;
        fontParams.borderWidth = 2;
        fontParams.borderColor = Color.LIGHT_GRAY;
        fontParams.borderStraight = true;
        fontParams.minFilter = Texture.TextureFilter.Linear;
        fontParams.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put("CVc_24p_black_brd2lightGray",
                     new FreeTypeFontGenerator(
                         Gdx.files.internal(path + "coolvetica_condensed_rg.ttf")).generateFont(
                         fontParams));
    
        /* ### 20P BLACK, BORDER: 1, LIGHT_GRAY ### */
        fontParams.size = 20;
        fontParams.color = Color.BLACK;
        fontParams.borderWidth = 1;
        fontParams.borderColor = Color.LIGHT_GRAY;
        fontParams.borderStraight = true;
        fontParams.minFilter = Texture.TextureFilter.Linear;
        fontParams.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put(
            "CVc_20p_black_brd1lightGray",
            new FreeTypeFontGenerator(
                Gdx.files.internal(path + "coolvetica_condensed_rg.ttf")).generateFont(fontParams));
    }
    
}
