package com.company.skt.lib;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class UpdateStage extends Stage implements InitializeUpdate, Named {
  
  private boolean initialized;
  private boolean active;
  private String name;

  public UpdateStage(String name){
    this.name = name;
  }
  
  public UpdateStage(String name, boolean active){
    this.name = name;
    this.active = active;
  }

  public void initialize(){
    initialized = true;
  }
  
  public boolean isInitialized() {
    return initialized;
  }

  public void setActive(boolean active){
    this.active = active;
  }

  public boolean isActive() {
    return active;
  }

  @Override
  public void update(float delta) {
    InitializeUpdate.super.update(delta);
    for(Actor actor : getActors()) {
      if(actor instanceof InitializeUpdate) {
        ((InitializeUpdate)actor).update(delta);
      }
    }
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
