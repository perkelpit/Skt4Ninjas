package com.company.skt.lib;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

/** Abstract class inheriting from libGDX´s {@link Game}-class mainly to automize the setting of the
 * {@link com.badlogic.gdx.InputProcessor InputProcessor} to a {@link InputMultiplexer}.*/
public abstract class ScreenController extends Game {
  
  private static ScreenController screenController;
  
  
  @Override
  public void create() {
    Gdx.input.setInputProcessor(new InputMultiplexer());
  }
  
  public ScreenController() {
    screenController = this;
  }
  
  public static ScreenController getController() {
    return screenController;
  }
  
  public static void setActiveScreen(StageScreen screen) {
    screenController.setScreen(screen);
  }
  
}
