package com.company.skt.controller;

import java.io.Closeable;
import java.io.IOException;

abstract class StreamHandler<T> extends Thread {
  protected volatile boolean stop;
  protected int delay;
  protected Closeable c;
  
  public StreamHandler() {
  }
  
  public StreamHandler(Closeable c, int delay){
    this.c = c;
    this.delay = delay;
  }
  
  protected abstract T read();
  
  protected abstract void process(T in) throws IOException;
  
  public void startStreamHandler() {
    stop = false;
    this.start();
  }
  
  public void stopStreamHandler() {
    stop = true;
    try {
      c.close();
    } catch (IOException e) {e.printStackTrace();}
  }
  
  protected void delay(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {e.printStackTrace();}
  }
  
  public void run() {
    while (!stop) {
      try {
        process(read());
      } catch (IOException e) {e.printStackTrace();}
      delay(delay);
    }
  }
  
}