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
    static private FreeTypeFontParameter CV_48p_black_brd2lightGray;
    static private FreeTypeFontParameter CV_28p_black_brd2lightGray;
    static private FreeTypeFontParameter CVc_24p_black_brd2lightGray;
    static private FreeTypeFontParameter CVc_16p_black_brd1lightGray;
    
    public static void boot(String path) {
        Fonts.path = path;
        FontsMap = new HashMap<>();
        
        /* ### FALLBACK / ERROR ### */
        BitmapFont stdFont = new BitmapFont();
        stdFont.setColor(Color.RED);
        FontsMap.put("Arial_15p_Std", stdFont);
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
                fontName = "CVc_24p_black_brd2lightGray";
                break;
            case "message":
                fontName = "CVc_24p_black_brd2lightGray";
                break;
            default:
                fontName = "Arial_15p_Std";
                break;
        }
        return FontsMap.get(fontName);
    }
    
    private static void generateFonts() {
        /* ### 48P BLACK, BORDER: 2, WHITE ### */
        CV_48p_black_brd2lightGray = new FreeTypeFontParameter();
        CV_48p_black_brd2lightGray.size = 48;
        CV_48p_black_brd2lightGray.color = Color.BLACK;
        CV_48p_black_brd2lightGray.borderWidth = 2;
        CV_48p_black_brd2lightGray.borderColor = Color.LIGHT_GRAY;
        CV_48p_black_brd2lightGray.borderStraight = true;
        CV_48p_black_brd2lightGray.minFilter = Texture.TextureFilter.Linear;
        CV_48p_black_brd2lightGray.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put("CV_48p_black_brd2lightGray",
                     new FreeTypeFontGenerator(
                         Gdx.files.internal(path + "coolvetica_rg.ttf")).generateFont(CV_48p_black_brd2lightGray));
    
        /* ### 32P BLACK, BORDER: 2, WHITE ### */
        CV_28p_black_brd2lightGray = new FreeTypeFontParameter();
        CV_28p_black_brd2lightGray.size = 28;
        CV_28p_black_brd2lightGray.color = Color.BLACK;
        CV_28p_black_brd2lightGray.borderWidth = 2;
        CV_28p_black_brd2lightGray.borderColor = Color.LIGHT_GRAY;
        CV_28p_black_brd2lightGray.borderStraight = true;
        CV_28p_black_brd2lightGray.minFilter = Texture.TextureFilter.Linear;
        CV_28p_black_brd2lightGray.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put("CV_28p_black_brd2lightGray",
                     new FreeTypeFontGenerator(
                         Gdx.files.internal(path + "coolvetica_rg.ttf")).generateFont(CV_28p_black_brd2lightGray));
    
        /* ### 24P BLACK, BORDER: 2, WHITE ### */
        CVc_24p_black_brd2lightGray = new FreeTypeFontParameter();
        CVc_24p_black_brd2lightGray.size = 24;
        CVc_24p_black_brd2lightGray.color = Color.BLACK;
        CVc_24p_black_brd2lightGray.borderWidth = 2;
        CVc_24p_black_brd2lightGray.borderColor = Color.LIGHT_GRAY;
        CVc_24p_black_brd2lightGray.borderStraight = true;
        CVc_24p_black_brd2lightGray.minFilter = Texture.TextureFilter.Linear;
        CVc_24p_black_brd2lightGray.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put("CVc_24p_black_brd2lightGray",
                     new FreeTypeFontGenerator(
                         Gdx.files.internal(path + "coolvetica_condensed_rg.ttf")).generateFont(
                         CVc_24p_black_brd2lightGray));
    
        /* ### 16P BLACK, BORDER: 1, WHITE ### */
        CVc_16p_black_brd1lightGray = new FreeTypeFontParameter();
        CVc_16p_black_brd1lightGray.size = 16;
        CVc_16p_black_brd1lightGray.color = Color.BLACK;
        CVc_16p_black_brd1lightGray.borderWidth = 1;
        CVc_16p_black_brd1lightGray.borderColor = Color.LIGHT_GRAY;
        CVc_16p_black_brd1lightGray.borderStraight = true;
        CVc_16p_black_brd1lightGray.minFilter = Texture.TextureFilter.Linear;
        CVc_16p_black_brd1lightGray.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put(
            "CVc_16p_black_brd1lightGray",
            new FreeTypeFontGenerator(
                Gdx.files.internal(path + "coolvetica_condensed_rg.ttf")).generateFont(CVc_16p_black_brd1lightGray));
    }
    
}
