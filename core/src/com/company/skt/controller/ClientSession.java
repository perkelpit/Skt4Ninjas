package com.company.skt.controller;

import com.badlogic.gdx.utils.Array;
import com.company.skt.lib.Player;
import com.company.skt.model.SessionData;
import com.company.skt.model.Settings;
import com.company.skt.view.DebugWindow;

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
    private boolean connected;
    
    private class ClientStringStreamHandler extends StringStreamHandler {
        
        ClientStringStreamHandler(BufferedReader br, int delay) {
            super(br, delay);
        }
        
        @Override
        protected void process(String in) {
            if (in != null) {
                if (!in.startsWith("PING")) {
                    DebugWindow.println("[ClientSession]: Msg recieved: " +
                                        (in.length() > 8 ? in.substring(0, 8) + "..." : in));
                }
                if (in.startsWith("PING")) {
                    sendString("PONG");
                }
                if (in.startsWith("LOGGEDIN")) {
                    ((Menu)Utils.getCurrentScreen()).event("READY_FOR_LOBBY");
                }
                if (in.startsWith("SUMMARY")) {
                    ((Menu)Utils.getCurrentScreen()).event("READY_FOR_SUMMARY");
                }
                if (in.startsWith("CFG>")) {
                    parseAndChangeSessionCfg(in.substring(in.indexOf('>') + 1));
                    sendString("CFG_REC");
                }
                if (in.startsWith("QRY_PLAYER")) {
                    sendString("PLAYER>" + thisPlayer.getName());
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
        DebugWindow.println("[ClientSession] cfg-String parsed and sessionCfg changed");
    }
    
    ClientSession() {
        startSession();
    }
    
    ClientSession(boolean start) {
        if (start) {
           startSession();
        }
    }
    
    private String fetchPlayerName() {
        appCfg = Settings.getProperties(Settings.APP);
        String playerName = appCfg.getProperty("player_name");
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
            /*
            // DEBUG turned off cause Dialog does not show properly
            // TODO better fitting UI-element to enter serverIP
            serverIP = (String)JOptionPane.showInputDialog(
                null,
                prompt,
                "Server-IP Input",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                "localhost");
                */
            serverIP = "localhost";
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
        if(!msg.startsWith("PONG")) {
            DebugWindow.println("[ClientSession]: Sending Msg: " +
                                (msg.length() > 8 ? msg.substring(0, 8) + "..." : msg));
        }
        out.println(msg);
    }
    
    @Override
    void startSession() {
        sessionData = SessionData.get();
        thisPlayer = new Player(fetchPlayerName());
        fetchServerIpAndLogin();
    }
    
    void stopSession() throws IOException {
        if(connected) {
            logOut();
        }
        in.stopStreamHandler();
        out.close();
        socket.close();
        ((Menu)Utils.getCurrentScreen()).event("LEAVE_LOBBY");
    }
    
    
}
