package com.company.skt.controller;

import com.badlogic.gdx.utils.Array;
import com.company.skt.model.Player;
import com.company.skt.model.SessionData;
import com.company.skt.model.Settings;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class ClientSession extends Session {
    
    private SessionData sessionData;
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
                    ((Menu)Utils.getCurrentScreen()).event("READY_FOR_LOBBY");
                }
                if (in.startsWith("SUMMARY")) {
                    ((Menu)Utils.getCurrentScreen()).event("READY_FOR_SUMMARY");
                }
                if (in.startsWith("CFG#")) {
                    parseAndChangeSessionCfg(in.substring(in.indexOf('#') + 1));
                    sendString("PLAYER#" + thisPlayer.getName());
                }
                if (in.startsWith("END")) {
                    // TODO Message to User
                    ((Menu)Utils.getCurrentScreen()).event("LEAVE_LOBBY");
                    try {stopSession();}
                    catch(IOException e) {e.printStackTrace();}
                }
            }
        }
    }
    
    private void parseAndChangeSessionCfg(String in) {
        String cfgStr = in;
        Array<String> keys = new Array<>();
        Array<String> values = new Array<>();
        while(cfgStr.length() > 0) {
            keys.add(cfgStr.substring(0, cfgStr.indexOf('=')));
            values.add(cfgStr.substring(cfgStr.indexOf('=') + 1, cfgStr.indexOf(';')));
            cfgStr = cfgStr.substring(cfgStr.indexOf(';'));
            cfgStr = cfgStr.replace(";", "");
        }
        sessionData.setCfgValues(keys.toArray(), values.toArray());
    }
    
    ClientSession() {
        lock = new Object();
        sessionData = SessionData.get();
        thisPlayer = new Player(fetchPlayerName());
        fetchServerIpAndLogin();
        if(connected) {
            start();
        }
    }
    
    private String fetchPlayerName() {
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
        return playerName;
    }
    
    private void fetchServerIpAndLogin() {
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
    
    void sendString(String msg) {
        out.println(msg);
    }
    
    void stopSession() throws IOException {
        if(connected) {
            logOut();
        }
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
