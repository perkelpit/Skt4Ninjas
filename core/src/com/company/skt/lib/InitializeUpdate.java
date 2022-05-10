package com.company.skt.lib;

public interface InitializeUpdate {
  
  void initialize();
  
  boolean isInitialized();
  
  default void update(float delta) {
        if(!isInitialized()) {
          initialize();
        }
  }
}