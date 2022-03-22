package com.company.skt.lib;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class UpdateStage extends Stage implements Initialize_Update {
  
  private boolean initialized;
  
  public void initialize(){
  
    initialized = true;
  }
  
  public boolean isInitialized() {
    return initialized;
  }
  
  @Override
  public void update(float delta) {
    Initialize_Update.super.update(delta);
    for(Actor actor : getActors()) {
      if(actor instanceof Initialize_Update) {
        ((Initialize_Update)actor).update(delta);
      }
    }
  }
  
}
