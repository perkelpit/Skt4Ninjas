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
    static private FreeTypeFontParameter fontParametersWelcome, fontParametersButton;
    
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
    }
    
    public static BitmapFont getFont(String fontName){
        return FontsMap.get(fontName);
    }
    
}
