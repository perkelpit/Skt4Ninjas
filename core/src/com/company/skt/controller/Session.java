package com.company.skt.controller;

import java.io.IOException;

/**
 * {@code Session} is the abstract super-class for {@link HostSession} and {@link ClientSession}, modelled as abstract
 * class rather than as interface to leave the possibilty to implement standard methods though not needed by now.
 * For now it only ensures that a {@code Session} can be started or stopped without knowing which one it is.
 * */
public abstract class Session {
  
  abstract void startSession() throws IOException;
  abstract void stopSession() throws IOException;

}
