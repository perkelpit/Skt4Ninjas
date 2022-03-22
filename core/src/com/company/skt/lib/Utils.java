package com.company.skt.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public abstract class Utils {
  private static HashMap<String, HashMap<String, float[]>> vMaps;
  private static StageScreen currentScreen;
  
  static {
    vMaps = new HashMap<>();
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
  
  
  public static AssetManager getCurrentAssets() {
    return currentScreen.getAssets();
  }
  
  public static StageScreen getCurrentScreen() {
    return currentScreen;
  }
  
  public static void setCurrentScreen(StageScreen screen) {
    currentScreen = screen;
  }
  
}
