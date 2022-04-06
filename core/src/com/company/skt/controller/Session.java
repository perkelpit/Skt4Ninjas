package com.company.skt.controller;

import java.io.IOException;

/* Methodes:
 *  public abstract void run()
 * */

public abstract class Session {
  
  abstract void startSession() throws IOException;
  abstract void stopSession() throws IOException;

}
