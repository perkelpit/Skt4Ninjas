package com.company.skt.controller;

import com.badlogic.gdx.Gdx;
import com.company.skt.lib.Player;
import com.company.skt.lib.StageScreen;
import com.company.skt.model.Local;
import com.company.skt.model.SessionData;
import com.company.skt.model.Settings;
import com.company.skt.view.DebugWindow;
import com.company.skt.view.DialogUI;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
    private boolean connected, rejected;
    
    private class ClientStringStreamHandler extends StringStreamHandler {
    
        private boolean isInitialSessionData;
    
        ClientStringStreamHandler(BufferedReader br, int delay) {
            super(br, delay);
            isInitialSessionData = true;
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
                    Utils.getCurrentScreen().event("READY_FOR_LOBBY");
                }
                if (in.startsWith("SUMMARY")) {
                    Utils.getCurrentScreen().event("READY_FOR_SUMMARY");
                }
                if (in.startsWith("SDATA>")) {
                    processSessionDataString(in.substring(in.indexOf('>') + 1));
                    if(isInitialSessionData) {
                        sendString("SDATA_REC");
                        isInitialSessionData = false;
                    }
                }
                if (in.startsWith("QRY_PLAYER")) {
                    sendString("PLAYER>" + thisPlayer.getName());
                }
                if (in.startsWith("KICK")) {
                    sessionLeft(true);
                }
                if (in.startsWith("END") && !rejected) {
                    sessionLeft(false);
                }
            }
        }
    }
    
    private void sessionLeft(boolean kicked) {
        StageScreen screen = Utils.getCurrentScreen();
        Gdx.app.postRunnable(() -> {
            DialogUI.newOkMessage(screen.findStage("lobbyUI"), Local.getString("lb_msg_left"),
                                  kicked ? Local.getString("lb_msg_kicked") : Local.getString("lb_msg_closed"),
                                  null, screen.findStage("mainMenuUI"),
                                  () -> {
                                      Gdx.app.postRunnable(() -> {
                                          screen.removeStage(screen.findStage("lobbyUI"));
                                      });
                                  });
        });
        try {stopSession();} catch(IOException e) {e.printStackTrace();}
        DebugWindow.setUIFocus(DebugWindow.Focus.Main);
    }
    
    private void processSessionDataString(String in) {
        DebugWindow.println("[ClientSession] processing sessionDataString");
        String invalidStr = "[ClientSession] invalid sessionDataString. should start with ";
        String dataStr = in;
        if(!dataStr.startsWith("HdlNr{")) {
            DebugWindow.println(invalidStr + "\"HdlNr{\": " + dataStr);
        } else {
            SessionData.setOwnPlayerNumber(Integer.parseInt(dataStr.substring(6,7)));
            dataStr = dataStr.substring(8);
        }
        if(!dataStr.startsWith("CFG{")) {
            DebugWindow.println(invalidStr + "\"CFG{\": " + dataStr);
        } else {
            dataStr = processCfgSubstring(dataStr);
        }
        if(!dataStr.startsWith("PLAYERS{")) {
            DebugWindow.println(invalidStr + "\"PLAYERS{\"): " + dataStr);
        } else {
            dataStr = dataStr.substring(8);
            for(int i = 0; i < 3; i++) {
                if(!dataStr.startsWith(i + "{")) {
                    DebugWindow.println(invalidStr + "\"" + i + "{\"): " + dataStr);
                } else {
                    dataStr = processPlayerSubstring(dataStr);
                }
            }
        }
        DebugWindow.println("[ClientSession] sessionDataString successfully processed");
    }
    
    private String processCfgSubstring(String dataStr){
        String cfgStr = dataStr.substring(0, dataStr.indexOf('}') + 1);
        cfgStr = cfgStr.substring(4);
        ArrayList<String> keyList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        while(cfgStr.charAt(0) != '}') {
            keyList.add(cfgStr.substring(0, cfgStr.indexOf('=')));
            valueList.add(cfgStr.substring(cfgStr.indexOf('=') + 1, cfgStr.indexOf(';')));
            cfgStr = cfgStr.substring(cfgStr.indexOf(';'));
            cfgStr = cfgStr.replaceFirst(";", "");
        }
        String[] keys = new String[keyList.size()];
        String[] values = new String[valueList.size()];
        keyList.toArray(keys);
        valueList.toArray(values);
        sessionData.setCfgValues(keys, values);
        return dataStr.substring(dataStr.indexOf('}') + 1);
    }
    
    private String processPlayerSubstring(String dataStr) {
        int playerNumber = Integer.parseInt(dataStr.substring(0, 1));
        String playerStr = dataStr.substring(2);
        if(playerStr.startsWith("null")) {
            sessionData.setPlayer(null, playerNumber);
        } else {
            Player player = sessionData.getPlayer(playerNumber);
            String playerName = playerStr.substring(0, playerStr.indexOf(";"));
            if(player == null) {
                player = new Player(playerName);
            } else {
                player.setName(playerName);
            }
            playerStr = playerStr.substring(playerStr.indexOf(";") + 1);
            player.setReady(Boolean.parseBoolean(playerStr.substring(0, playerStr.indexOf(";"))));
            playerStr = playerStr.substring(playerStr.indexOf(";") + 1);
            player.setConnectivity(Integer.parseInt(playerStr.substring(0, playerStr.indexOf(";"))));
            playerStr = playerStr.substring(playerStr.indexOf(";") + 1);
            sessionData.setPlayer(player, playerNumber);
            dataStr = playerStr;
        }
        return dataStr.length() > 1 ?
               dataStr.substring(dataStr.indexOf('}') + 1) : "";
    }
    
    ClientSession(String serverIP) {
        DebugWindow.println("[ClientSession] got IP: " + serverIP);
        this.serverIP = serverIP;
        startSession();
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
                null);
        }
        return playerName;
    }
    
    private boolean connect() {
        try {
            socket = new Socket(serverIP, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new ClientStringStreamHandler(new BufferedReader(new InputStreamReader(socket.getInputStream())), 100);
            in.startStreamHandler();
            Utils.getCurrentScreen().event("CLIENT_SERVER_FOUND");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Utils.getCurrentScreen().event("CLIENT_NO_SERVER_FOUND");
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
        DebugWindow.println("[ClientSession] starting...");
        sessionData = SessionData.get();
        thisPlayer = new Player(fetchPlayerName());
        DebugWindow.println("[ClientSession] trying to connect...");
        connected = connect();
        if(!connected) {
            DebugWindow.println("[ClientSession] connecting failed. stopping session.");
            try {
                stopSession();
            } catch (IOException e) {e.printStackTrace();}
        } else {
            DebugWindow.println("[ClientSession] connection ok.");
        }
    }
    
    void stopSession() throws IOException {
        if(connected) {
            logOut();
        }
        in.stopStreamHandler();
        out.close();
        socket.close();
    }
    
    
}
