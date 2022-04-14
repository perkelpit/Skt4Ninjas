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
    static private FreeTypeFontParameter fontParametersWelcome;
    static private FreeTypeFontParameter fontParametersButton;
    static private FreeTypeFontParameter fontParametersMessage;
    static private FreeTypeFontParameter fP_pirata_16p_black_bord1white;
    
    public static void boot(String path) {
        Fonts.path = path;
        FontsMap = new HashMap<>();
    
        fontParametersWelcome = new FreeTypeFontParameter();
        fontParametersWelcome.size = 48;
        fontParametersWelcome.color = Color.BLACK;
        fontParametersWelcome.borderWidth = 2;
        fontParametersWelcome.borderColor = Color.WHITE;
        fontParametersWelcome.borderStraight = true;
        fontParametersWelcome.minFilter = Texture.TextureFilter.Linear;
        fontParametersWelcome.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put("PirataOne-Regular_Welcome",
                     new FreeTypeFontGenerator(
                         Gdx.files.internal(path + "PirataOne-Regular.ttf")).generateFont(fontParametersWelcome));
    
        fontParametersButton = new FreeTypeFontParameter();
        fontParametersButton.size = 32;
        fontParametersButton.color = Color.BLACK;
        fontParametersButton.borderWidth = 2;
        fontParametersButton.borderColor = Color.WHITE;
        fontParametersButton.borderStraight = true;
        fontParametersButton.minFilter = Texture.TextureFilter.Linear;
        fontParametersButton.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put("PirataOne-Regular_Button",
                     new FreeTypeFontGenerator(
                         Gdx.files.internal(path + "PirataOne-Regular.ttf")).generateFont(fontParametersButton));
    
        fontParametersMessage = new FreeTypeFontParameter();
        fontParametersMessage.size = 24;
        fontParametersMessage.color = Color.BLACK;
        fontParametersMessage.borderWidth = 2;
        fontParametersMessage.borderColor = Color.WHITE;
        fontParametersMessage.borderStraight = true;
        fontParametersMessage.minFilter = Texture.TextureFilter.Linear;
        fontParametersMessage.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put("PirataOne-Regular_Message",
                     new FreeTypeFontGenerator(
                         Gdx.files.internal(path + "PirataOne-Regular.ttf")).generateFont(fontParametersMessage));
    
        fP_pirata_16p_black_bord1white = new FreeTypeFontParameter();
        fP_pirata_16p_black_bord1white.size = 16;
        fP_pirata_16p_black_bord1white.color = Color.BLACK;
        fP_pirata_16p_black_bord1white.borderWidth = 1;
        fP_pirata_16p_black_bord1white.borderColor = Color.WHITE;
        fP_pirata_16p_black_bord1white.borderStraight = true;
        fP_pirata_16p_black_bord1white.minFilter = Texture.TextureFilter.Linear;
        fP_pirata_16p_black_bord1white.magFilter = Texture.TextureFilter.Linear;
        FontsMap.put(
            "pirata_16p_black_bord1white",
            new FreeTypeFontGenerator(
                Gdx.files.internal(path + "PirataOne-Regular.ttf")).generateFont(fP_pirata_16p_black_bord1white));
    }
    
    public static BitmapFont getFont(String fontName){
        return FontsMap.get(fontName);
    }
    
}
