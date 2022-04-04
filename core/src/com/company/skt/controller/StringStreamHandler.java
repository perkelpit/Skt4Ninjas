package com.company.skt.controller;

import java.io.BufferedReader;
import java.io.IOException;

abstract class StringStreamHandler extends StreamHandler<String> {
  protected BufferedReader br;
  
  StringStreamHandler(BufferedReader br, int delay){
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