package com.company.skt.controller;

import com.company.skt.model.GameList;
import com.company.skt.model.Player;
import com.company.skt.model.Settings;
import com.company.skt.model.Utils;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class ClientSession extends Session {
  
  private Socket socketT;
  private Socket socketO;
  private String playerName;
  private PrintWriter outT;
  private ObjectOutputStream outO;
  private ClientTextStreamHandler inT;
  private ClientObjectStreamHandler inO;
  private Properties appCfg;
  private Player thisPlayer;
  private final int TEXT_PORT = 2152;
  private final int OBJECT_PORT = 2153;
  private String serverIP;
  private volatile String reqObjStr;
  private volatile Object reqObj;
  private final Object lock;
  private boolean connected;
  private boolean initialSettingRecieved;
  private volatile boolean stop;
  
  private class ClientTextStreamHandler extends TextStreamHandler {
  
    ClientTextStreamHandler(BufferedReader br, int delay) {
      super(br, delay);
      initialSettingRecieved = false;
    }
    
    protected void process(String in) {
      if (in != null) {
        // TODO other messages, in switch-case umbauen
        if (in.startsWith("PING")) {
          outT.println("PONG");
        }
        if (in.startsWith("LOGGEDIN")) {
          ((Menu)Utils.getCurrentScreen()).event("LOGGEDIN");
          outT.println("LOBBY");
        }
        if (in.startsWith("SUMMARY")) {
          ((Menu)Utils.getCurrentScreen()).event("SUMMARY");
        }
      }
    }  
  }
  
  private class ClientObjectStreamHandler extends ObjectStreamHandler {
  
    ClientObjectStreamHandler(ObjectInputStream ois, int delay) {
      super(ois, delay);
      reqObj = null;
    }
    
    private void catchRequestedObject(Object o) {
      synchronized (lock) {
        switch(reqObjStr) {
          case "GAMELIST":
            if (o instanceof GameList) {
              reqObj = o;
              reqObjStr = null;
              lock.notify();
            }
            break;
          case "DUMMY": // other object to catch
            // do stuff
            break;
        }
      }
    }
    
    protected void process(Object o) {
      if (o != null) {
        if (reqObjStr != null) {
          System.out.println("Catching Object: " + reqObjStr); // DEBUG
          catchRequestedObject(o);
        }
        if (o instanceof Properties) {
          // TODO deliver this gameCfg somewhere
          // OLD: gameCfg = (Properties)o;
          if (!initialSettingRecieved) {
            outT.println("SETT_REC");
            initialSettingRecieved = true;
          } else {
            ((Menu)Utils.getCurrentScreen()).event("LOBBY_UPDATE_GAMECFG");
            // OLD: lobbyUI.updateGameCfg(gameCfg);
          }
        }
        if (o instanceof Player[]) {
          ((Menu)Utils.getCurrentScreen()).event("LOBBY_UPDATE_PLAYERS");
          // OLD: lobbyUI.updatePlayers((Player[])o);
        }
      }
    }  
  }
  
  public ClientSession() {
    lock = new Object();
    playerName = null;
    reqObjStr = null;
    appCfg = Settings.getProperties(Settings.APP);
    playerName = appCfg.getProperty("player_name");
    while (playerName == null) {
      // TODO better fitting UI-element to enter playerName
      String prompt = "Enter Player Name: ";
      playerName = (String)JOptionPane.showInputDialog(
          null,
          prompt,
          "Player Name Input",
          JOptionPane.QUESTION_MESSAGE,
          null,
          null,
          null
      );
    }
    thisPlayer = new Player(playerName);
    connected = false;
    boolean abort = false;
    String prompt = "Enter Server-IP: ";
    do {
      // TODO better fitting UI-element to enter serverIP
      serverIP = (String)JOptionPane.showInputDialog(
          null,
          prompt,
          "Server-IP Input",
          JOptionPane.QUESTION_MESSAGE,
          null,
          null,
          "localhost"
      );
      if (serverIP != null) {
        try {
          connected = logIn();
        } catch (Exception e) {
          prompt = "ERROR: Enter valid server-IP!";
        }
      } else { // (serverIP == null) -> "Cancel" was selected in Dialog
        abort = true;
        try {
          stopSession();
        } catch (IOException e) {e.printStackTrace();}
        // TODO any action if input of serverIP was aborted?
      }
 
    } while (!connected && !abort);
    start();
  }
  
  private boolean logIn() {
    try {
      socketT = new Socket(serverIP, TEXT_PORT);
      socketO = new Socket(serverIP, OBJECT_PORT);
      outT = new PrintWriter(socketT.getOutputStream(), true);
      outO = new ObjectOutputStream(socketO.getOutputStream());
      inT = new ClientTextStreamHandler(new BufferedReader(new InputStreamReader(socketT.getInputStream())), 100);
      inO = new ClientObjectStreamHandler(new ObjectInputStream(socketO.getInputStream()), 100);
      inT.startStreamHandler();
      inO.startStreamHandler();
      outO.reset();
      outO.writeObject(thisPlayer);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
  
  private void logOut() {
    outT.println("QUIT");
  }
  
  public Object callForObject(String objectStr) {
    synchronized (lock) {
      reqObj = null;
      reqObjStr = objectStr;
      outT.println(objectStr);
      try {
        System.out.println("Waiting for " + reqObjStr);
        lock.wait();
      } catch (InterruptedException e) {e.printStackTrace();}
      System.out.println("Returning req. Obj.: " + reqObj.toString());
      return reqObj;
    }
  }
  
  public void sendText(String msg) {
    outT.println(msg);
  }
  
  public void stopSession() throws IOException {
    logOut();
    stop = true;
    inT.stopStreamHandler();
    inO.stopStreamHandler();
    outT.close();
    outO.close();
    socketT.close();
    socketO.close();
  }
  
  @Override
  public void run() {
    while (!stop) {
      // TODO do something usefull
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {e.printStackTrace();}
    }
  }
  
}
