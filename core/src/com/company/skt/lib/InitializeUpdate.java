package com.company.skt.lib;

/** An {@code interface} for different libGDX-derived classes to provide class-independant calling of those two
 * methods. <br>
 * {@link #update(float)} is called EVERY FRAME BEFORE libGDX´s own {@code act(float)} methods and <br>
 * {@link #initialize()} is called ONCE after creation of the object at the beginning of {@link #update(float)}
 * and never again afterwards.*/
public interface InitializeUpdate {
  
  void initialize();
  
  boolean isInitialized();
  
  default void update(float delta) {
        if(!isInitialized()) {
          initialize();
        }
  }
}