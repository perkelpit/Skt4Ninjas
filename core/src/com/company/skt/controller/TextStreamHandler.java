package com.company.skt.controller;

import java.io.BufferedReader;
import java.io.IOException;

abstract class TextStreamHandler extends StreamHandler<String> {
  protected BufferedReader br;

  public TextStreamHandler() {
  }
  
  public TextStreamHandler(BufferedReader br, int delay){
    super(br, delay);
    this.br = br;
  }
  
  protected String read() {
    String s = null;
    try {
      if (br.ready()) {
        s = br.readLine();
      }
    } catch (IOException e) {e.printStackTrace();}
    return s;
  }
  
}