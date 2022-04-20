package com.company.skt.controller;

import com.company.skt.Skt;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

abstract class StreamHandler<T> implements Runnable {
  protected volatile boolean stop;
  protected int delay;
  protected Closeable c;
  
  StreamHandler(Closeable c, int delay){
    this.c = c;
    this.delay = delay;
  }
  
  protected abstract T read();
  
  protected abstract void process(T in) throws IOException;
  
  void startStreamHandler() {
    stop = false;
    Skt.getExecutor().schedule(this, 0, TimeUnit.MILLISECONDS);
  }
  
  void stopStreamHandler() {
    stop = true;
    try {c.close();} catch (IOException e) {e.printStackTrace();}
  }
  
  protected void delay(int ms) {
    try {Thread.sleep(ms);} catch (InterruptedException e) {e.printStackTrace();}
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