package com.company.skt.lib;

public interface Initialize_Update {
  
  void initialize();
  
  boolean isInitialized();
  
  default void update(float delta) {
        if(!isInitialized()) {
          initialize();
        }
  }
}