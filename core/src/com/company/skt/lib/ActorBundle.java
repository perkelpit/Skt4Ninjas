package com.company.skt.lib;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class ActorBundle<T extends Actor> extends Actor implements Initialize_Update, Named {
  
  private boolean initialized;
  Array<T> actors;
  
  {
    actors = new Array<>();
  }
  
  public ActorBundle(String name) {
    setName(name);
  }
  
  public void add(T actor) {
    actors.add(actor);
  }
  
  public void remove(T actor, boolean indentity) {
    actors.removeValue(actor, indentity);
  }
  
  public Array<T> getActors() {
    return actors;
  }
  
  public T findActor(String name) {
    // TODO Change to std-for-loop -> can cause GdxRuntimeException: #iterator() cannot be used nested
    for(T actor : actors) {
      if(actor.getName().equals(name)) {
        return actor;
      }
    }
    return null;
  }
  
  public void initialize(){
    // TODO Change to std-for-loop -> can cause GdxRuntimeException: #iterator() cannot be used nested
    for(T actor : actors) {
      if(actor instanceof Initialize_Update)
        ((Initialize_Update)actor).initialize();
    }
    initialized = true;
  }
  
  public boolean isInitialized() {
    return initialized;
  }
  
  @Override
  public void update(float delta) {
    Initialize_Update.super.update(delta);
    // TODO Change to std-for-loop -> can cause GdxRuntimeException: #iterator() cannot be used nested
    for(T actor : actors) {
      if(actor instanceof Initialize_Update)
        ((Initialize_Update)actor).update(delta);
    }
  }
  
  @Override
  public void act(float delta) {
    super.act(delta);
    // TODO Change to std-for-loop -> can cause GdxRuntimeException: #iterator() cannot be used nested
    for(T actor : actors) {
      actor.act(delta);
    }
  }
  
  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    if (isVisible()) {
      // TODO Change to std-for-loop -> can cause GdxRuntimeException: #iterator() cannot be used nested
      for(T actor : actors) {
        if (actor.isVisible()) {
          actor.draw(batch, parentAlpha);
        }
      }
    }
  }
  
}
