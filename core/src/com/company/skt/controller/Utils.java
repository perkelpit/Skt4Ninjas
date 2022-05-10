package com.company.skt.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.company.skt.lib.StageScreen;
import com.company.skt.model.Local;
import com.company.skt.model.Settings;

import java.util.HashMap;
import java.util.Properties;

/**
 * {@code Utils} is a currently very messy utility-class holding all those static methods which have not found a
 * better place by now. Some of those methods are even remnants of a previously made "space-game" for learning libGDX'
 * basics. They are left in until it´s certain they aren´t needed for animation purposes. <br>
 * <p>
 * By now only following methods are in use in {@code skt}: <br>
 * - {@code scale}-methods: <br> used to scale textures from reference-resolution to actual resolution. <br>
 * - {@code setCurrentScreen} and {@code getCurrentScreen}: <br> used at many places to get a reference to
 * the current screen <br>
 * - {@code getScaleFactorX and ..Y}: <br> provide/calculate those values, needed at different places
 * </p>
 * */
public abstract class Utils {
    /* TODO split class into:
     * model.Utils(methods only used by skt) and
     * lib.LibUtils(with methods used by lib-classes) */
    private static HashMap<String, HashMap<String, float[]>> vMaps;
    private static StageScreen currentScreen;

    
    static {
        vMaps = new HashMap<>();
    }


    public static void scaleAndCachePng(String scourceFile, String targetFile,
                                  float scaleFactorX, float scaleFactorY, Pixmap.Filter filter) {
        System.out.println("scaleAndCachePng()");
        Pixmap unscaled = new Pixmap(Gdx.files.internal(scourceFile));
        int width = (int)(unscaled.getWidth() * scaleFactorX);
        int height = (int)(unscaled.getHeight() * scaleFactorY);
        Pixmap scaled = new Pixmap(width, height, unscaled.getFormat());
        scaled.setFilter(filter);
        scaled.drawPixmap(unscaled,
                          0, 0, unscaled.getWidth(), unscaled.getHeight(),
                          0, 0, scaled.getWidth(), scaled.getHeight());
        PixmapIO.writePNG(Gdx.files.local(targetFile), scaled);
        unscaled.dispose();
        scaled.dispose();
    }
    
    public static Texture scaleTextureTo(Texture texture, int width, int height, Pixmap.Filter filter) {
        if(!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        Pixmap unscaled = texture.getTextureData().consumePixmap();
        Pixmap scaled = new Pixmap(width, height, unscaled.getFormat());
        scaled.setFilter(filter);
        scaled.drawPixmap(unscaled,
                          0, 0, unscaled.getWidth(), unscaled.getHeight(),
                          0, 0, scaled.getWidth(), scaled.getHeight());
        Texture scaledTexture = new Texture(scaled);
        unscaled.dispose();
        scaled.dispose();
        return scaledTexture;
    }
    
    public static Texture scaleTextureTo(TextureRegion textureRegion, int width, int height, Pixmap.Filter filter) {
        if(!textureRegion.getTexture().getTextureData().isPrepared()){
            textureRegion.getTexture().getTextureData().prepare();
        }
        Pixmap unscaled = textureRegion.getTexture().getTextureData().consumePixmap();
        Pixmap scaled = new Pixmap(width, height, unscaled.getFormat());
        scaled.setFilter(filter);
        scaled.drawPixmap(unscaled,
                          textureRegion.getRegionX(),textureRegion.getRegionY(),
                          textureRegion.getRegionX() + textureRegion.getRegionWidth(),
                          textureRegion.getRegionY() + textureRegion.getRegionHeight(),
                          0, 0, scaled.getWidth(), scaled.getHeight());
        Texture scaledTexture = new Texture(scaled);
        unscaled.dispose();
        scaled.dispose();
        return scaledTexture;
    }
    
    public static float limitDegrees(float degree) {
        float temp = degree;
        if(temp > 360) {
            temp -= 360;
        }
        if(temp < 0) {
            temp += 360;
        }
        return temp;
    }
    
    private static HashMap<String, float[]> readVerticesMap(String path) {
        String text = Gdx.files.internal(path).readString();
        String[] lines = text.split("\r\n");
        String name;
        String[] floatStrings;
        Array<Float> verticesArray = new Array<>();
        HashMap<String, float[]> verticesMap = new HashMap<>();
        for (String line : lines) {
            verticesArray.clear();
            name = line.substring(0, line.indexOf("{"));
            floatStrings = line.substring(line.indexOf("{") + 1, line.indexOf("}")).split(",");
            for(String floatString : floatStrings) {
                floatString = floatString.replace(",","");
                verticesArray.add(Float.parseFloat(floatString));
            }
            float[] vertices = new float[verticesArray.size];
            int i = 0;
            for(Float f : verticesArray) {
                vertices[i] = f;
                i++;
            }
            verticesMap.put(name, vertices);
        }
        return verticesMap;
    }
    
    public static HashMap<String,float[]> getVerticesMap(String path) {
        if(!vMaps.containsKey(path)) {
            vMaps.put(path, readVerticesMap(path));
        }
        return vMaps.get(path);
    }
    
    public static boolean isRotateLeftShorter(float startAngle, float targetAngle) {
        float a = targetAngle - startAngle;
        a += 180;
        while(a < 0){
            a += 360;
        }
        a %= 360;
        a -= 180;
        return (a >= 0);
    }
    
    public static boolean getRandomBoolean() {
        return Math.random() < 0.5;
    }
    
    public static StageScreen getCurrentScreen() {
        return currentScreen;
    }
    
    public static void setCurrentScreen(StageScreen screen) {
        currentScreen = screen;
    }

    public static float getScaleFactorX(){
        Properties appCfg = Settings.getProperties(Settings.APP);
        String refRes = appCfg.getProperty("ref_res");
        return Float.parseFloat(appCfg.getProperty("resolution_x")) /
                Float.parseFloat(refRes.substring(0, refRes.indexOf('x')));
    }

    public static float getScaleFactorY(){
        Properties appCfg = Settings.getProperties(Settings.APP);
        String refRes = appCfg.getProperty("ref_res");
        return Float.parseFloat(appCfg.getProperty("resolution_y")) /
                Float.parseFloat(refRes.substring(refRes.indexOf('x') + 1));
    }

}
