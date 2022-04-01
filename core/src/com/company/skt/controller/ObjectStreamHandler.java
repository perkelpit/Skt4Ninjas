package com.company.skt.controller;

import java.io.ObjectInputStream;

abstract class ObjectStreamHandler extends StreamHandler<Object> {
  ObjectInputStream ois;

  public ObjectStreamHandler() {
  }
  
  public ObjectStreamHandler(ObjectInputStream ois, int delay){
    super(ois, delay);
    this.ois = ois;
  }
  
  @Override
  protected Object read() {
    Object o = null;
    try {
      o = ois.readObject();
    } catch (Exception ignored) {}
    return o;
  }

}
