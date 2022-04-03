package com.company.skt.controller;

import com.company.skt.model.Player;
import com.company.skt.model.Settings;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class ClientHandler extends Thread {
  
  private HostSession hostSession;
  private Socket socketT,
                 socketO;
  private Player player;
  private PrintWriter outT;
  private ObjectOutputStream outO;
  private HostTextStreamHandler inT;
  private HostObjectStreamHandler inO;
  private HeartBeat hb;
  private Properties gameCfg;
  private volatile boolean stop;
  
  private class HeartBeat extends Thread {
    private int pingRate;
    private int noPongCount;
    private volatile boolean pong;
    private volatile boolean stop;
    
    HeartBeat(int ms) {
      pingRate = ms;
      noPongCount = 0;
      pong = false;
      stop = false;
    }
    
    public void stopHeartBeat() {
      stop = true;
    }
    
    public void pong() {
      pong = true;
    }
    
    public void run() {
      while (!stop) {
        pong = false;
        outT.println("PING");
        /* TODO Pingrate ans Ende, hier wait() mit max. waiting time check, latency
         * pong() um notify() ergänzen, noPongCount ersetzen etc. */
        try {
          Thread.sleep(pingRate);
        } catch (InterruptedException e) {e.printStackTrace();}
        if (pong) {
          noPongCount = 0;
        } else {
          ++noPongCount;
          if (noPongCount == 10 || noPongCount == 20 ) {
            // TODO proper Connection-Warning in UI
            System.out.println("Connection Warning: " + player.name);
          }
          if (noPongCount > 25) {
            stop = true;
            System.out.println("Stopping Handler: " + player.name);
            try {
              stopClientHandler();
            } catch (IOException e) {e.printStackTrace();}
            // TODO proper Connection-Lost-Msg in UI
          }
        }
      }
    }
    
  }
  
  private class HostTextStreamHandler extends TextStreamHandler {
    private ClientHandler clientHandler;
    
    HostTextStreamHandler(BufferedReader br, int delay, ClientHandler clientHandler) {
      super(br, delay);
      this.clientHandler = clientHandler;
    }

    protected void process(String in) throws IOException {
      if (in != null) { 
        if (in.startsWith("QUIT")) {
          stopClientHandler();
        }
        if (in.startsWith("PONG")) {
          hb.pong();
        }
        if (in.startsWith("SETT_REC")) {
          outT.println("LOGGEDIN");
        }
        if (in.startsWith("LOBBY")) {
          sendObject(hostSession.players);
        }
        if (in.startsWith("GAMELIST")) {
          sendObject(hostSession.gameList);
        }
        if (in.startsWith("RDY_TGL")) {
          hostSession.clientReadyToggle(clientHandler);
        }
      }
    }
    
  }
  
  private class HostObjectStreamHandler extends ObjectStreamHandler {
    
    HostObjectStreamHandler(ObjectInputStream ois, int delay) {
      super(ois, delay);
    }
    
    protected void process(Object o) {
      if (o != null) {
        if(o instanceof Player) {
          player = (Player)o;
          sendObject(gameCfg);
        }
      }
    }
    
  }
  
  
  public ClientHandler(HostSession hostSession, Socket socketT, Socket socketO) {
    this.hostSession = hostSession;
    this.socketT = socketT;
    this.socketO = socketO;
    try {
      outT = new PrintWriter(socketT.getOutputStream(), true);
      outO = new ObjectOutputStream(socketO.getOutputStream());
      hb = new HeartBeat(200);
      hb.start();
      inT = new HostTextStreamHandler(new BufferedReader(
        new InputStreamReader(socketT.getInputStream())), 100, this);
      inO = new HostObjectStreamHandler(new ObjectInputStream(
        socketO.getInputStream()), 100);
    } catch (IOException ioe) {ioe.printStackTrace();}
    player = null;
    stop = false;
    inT.startStreamHandler();
    inO.startStreamHandler();
    gameCfg = Settings.getProperties(Settings.GAME);
    start();
  }
  
  protected void sendObject(Object o) {
    try {
      outO.reset();
      outO.writeObject(o);
    } catch (IOException e) {e.printStackTrace();}
  }
  
  protected void sendText(String msg) {
      outT.println(msg);
  }
  
  public Player getPlayer() {
    return player;
  }
  
  public void stopClientHandler() throws IOException {
    stop = true;
    hb.stopHeartBeat();
    inT.stopStreamHandler();
    inO.stopStreamHandler();
    outT.close();
    outO.close();
    hostSession.clientLost(this);
  }
  
  public void run() {
    while (!socketT.isClosed() && !socketO.isClosed()) {
      while(!stop) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {e.printStackTrace();}
      }
    }  
  }
  
}
