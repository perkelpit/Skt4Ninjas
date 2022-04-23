package com.company.skt.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Null;

import java.util.HashMap;

public abstract class Fonts {
    
    static String path;
    static FileHandle stdTTF;
    static HashMap<String, BitmapFont> FontsMap;
    
    public static void boot(String path) {
        Fonts.path = path;
        FontsMap = new HashMap<>();
        
        /* ### FALLBACK FONT ### */
        BitmapFont fallbackFont = new BitmapFont();
        fallbackFont.setColor(Color.RED);
        FontsMap.put("Arial_15p_white", fallbackFont);
        
        /* ### STANDARD TTF ###*/
        stdTTF = Gdx.files.internal(path + "coolvetica_rg.ttf");
        
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
    
    
    public static BitmapFont get(int fontSize, Color fontColor) {
        return get(stdTTF, fontSize, fontColor);
    }
    
    public static BitmapFont get(FileHandle ttfFile, int fontSize, Color fontColor) {
        FreeTypeFontParameter fontParams = new FreeTypeFontParameter();
        fontParams.size = fontSize;
        fontParams.color = fontColor;
        fontParams.minFilter = Texture.TextureFilter.Linear;
        fontParams.magFilter = Texture.TextureFilter.Linear;
        return new FreeTypeFontGenerator(ttfFile).generateFont(fontParams);
    }
    
    public static BitmapFont get(@Null FileHandle ttfFile, int fontSize, @Null Color fontColor, float fontGamma,
                                 int borderWidth, boolean borderStraight, float borderGamma, @Null Color borderColor,
                                 int shadowOffsetX, int shadowOffsetY, @Null Color shadowColor,
                                 int spaceX, int spaceY, int padTop, int padBottom, int padLeft, int padRight) {
        FreeTypeFontParameter fontParams = new FreeTypeFontParameter();
        fontParams.size = fontSize;
        fontParams.color = fontColor != null ? fontColor : Color.BLACK;
        fontParams.gamma = fontGamma != 0.0f ? fontGamma : 1.8f;
        fontParams.borderWidth = borderWidth;
        fontParams.borderStraight = borderStraight;
        fontParams.borderGamma = borderGamma != 0.0f ? borderGamma : 1.8f;
        fontParams.borderColor = borderColor != null ? borderColor : Color.BLACK;
        fontParams.shadowOffsetX = shadowOffsetX;
        fontParams.shadowOffsetY = shadowOffsetY;
        fontParams.shadowColor = shadowColor != null ? shadowColor : Color.BLACK;
        fontParams.spaceX = spaceX;
        fontParams.spaceY = spaceY;
        fontParams.padTop = padTop;
        fontParams.padBottom = padBottom;
        fontParams.padLeft = padLeft;
        fontParams.padRight = padRight;
        fontParams.minFilter = Texture.TextureFilter.Linear;
        fontParams.magFilter = Texture.TextureFilter.Linear;
        return new FreeTypeFontGenerator(ttfFile != null ? ttfFile : stdTTF).generateFont(fontParams);
    }
    
}
