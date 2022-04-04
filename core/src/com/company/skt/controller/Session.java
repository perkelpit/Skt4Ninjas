package com.company.skt.controller;

import java.io.IOException;

/* Methodes:
 *  public abstract void run()
 * */

public abstract class Session extends Thread {
  
  abstract void stopSession() throws IOException;

}
