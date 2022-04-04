package com.company.skt.controller;

import com.company.skt.model.Player;
import com.company.skt.model.Settings;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class ClientSession extends Session {
    
    private Socket socket;
    private PrintWriter out;
    private ClientStringStreamHandler in;
    private Properties appCfg;
    private Player thisPlayer;
    private final int PORT = 2152;
    private String serverIP;
    private final Object lock;
    private boolean connected;
    private boolean initialSettingRecieved;
    private volatile boolean stop;
    
    private class ClientStringStreamHandler extends StringStreamHandler {
        
        ClientStringStreamHandler(BufferedReader br, int delay) {
            super(br, delay);
        }
        
        protected void process(String in) {
            if (in != null) {
                // TODO other messages, in switch-case umbauen
                if (in.startsWith("PING")) {
                    sendString("PONG");
                }
                if (in.startsWith("LOGGEDIN")) {
                    ((Menu)Utils.getCurrentScreen()).event("LOGGEDIN");
                    sendString("LOBBY");
                }
                if (in.startsWith("SUMMARY")) {
                    ((Menu)Utils.getCurrentScreen()).event("SUMMARY");
                }
                if (in.startsWith("CFG#")) {
                    // TODO change SessionCfg
                }
            }
        }
    }
    
    public ClientSession() {
        lock = new Object();
        String playerName = null;
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
            socket = new Socket(serverIP, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new ClientStringStreamHandler(new BufferedReader(new InputStreamReader(socket.getInputStream())), 100);
            in.startStreamHandler();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void logOut() {
        sendString("QUIT");
    }
    
    public void sendString(String msg) {
        out.println(msg);
    }
    
    public void stopSession() throws IOException {
        logOut();
        stop = true;
        in.stopStreamHandler();
        out.close();
        socket.close();
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
