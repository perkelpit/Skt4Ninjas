package com.company.skt.controller;

import com.company.skt.model.Player;
import com.company.skt.model.SessionData;
import com.company.skt.model.Settings;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

public class HostSession extends Session {
    
    private final int PORT = 2152;
    private Thread clientSearch;
    private volatile boolean stop;
    private boolean clientSearchOngoing;
    public boolean lobby;
    public boolean summary;
    public boolean game;
    SessionData sessionData;
    Properties appCfg;
    Properties sessionCfg;
    ServerSocket serverSocket;
    ClientHandler handlerCPlayer1;
    ClientHandler handlerCPlayer2;
    
    HostSession() {
        lobby = false;
        appCfg = Settings.getProperties(Settings.APP);
        lobby = true;
        sessionData = SessionData.get(true);
        sessionCfg = sessionData.getSessionCfg();
        sessionData.setPlayer(new Player(appCfg.getProperty("player_name")), 0);
        clientSearchOngoing = false;
        stop = false;
        try {
            serverSocket = new ServerSocket(PORT);
            start();
        } catch (IOException e) {e.printStackTrace();}
    }
    
    void sendStringToAll(String msg) {
        if (handlerCPlayer1 != null) {
            handlerCPlayer1.sendText(msg);
        }
        if (handlerCPlayer2 != null) {
            handlerCPlayer2.sendText(msg);
        }
    }
    
    void stopSession() {
        if(!stop)
            stop = true;
    }
    
    private void clientSearch() {
        clientSearch = new Thread(() -> {
            while (!stop) {
                clientSearchOngoing = true;
                while (handlerCPlayer1 == null || handlerCPlayer2 == null) {
                    try {
                        if (handlerCPlayer1 == null && !stop) {
                            handlerCPlayer1 = new ClientHandler(
                                HostSession.this, serverSocket.accept());
                            fetchPlayerName(handlerCPlayer1);
                        }
                        if (handlerCPlayer1 != null && handlerCPlayer2 == null && !stop) {
                            handlerCPlayer2 = new ClientHandler(
                                HostSession.this, serverSocket.accept());
                            fetchPlayerName(handlerCPlayer2);
                        }
                    } catch (IOException ignored) {}
                }
            }
        });
        clientSearch.start();
        clientSearchOngoing = false; // TODO DEBUG correct? (seems off placed)
    }
    
    private void fetchPlayerName(ClientHandler clientHandler) {
        Thread thread = new Thread(() -> {
            while (sessionData.getPlayer(1) == null && !stop) {
                if(clientHandler.getPlayer() != null) {
                    sessionData.setPlayer(clientHandler.getPlayer(), 1);
                }
                try {
                    Thread.sleep(100);
                } catch(InterruptedException e) {e.printStackTrace();}
            }
        });
        thread.start();
    }
    
    void unreadyAllClients() {
        sessionData.setPlayerReady(1, false);
        sessionData.setPlayerReady(2, false);
    }
    
    void clientReadyToggle(ClientHandler clientHandler) {
        if (clientHandler.equals(handlerCPlayer1)) {
            sessionData.setPlayerReady(1, !(sessionData.getPlayer(1).isReady()));
        }
        if (clientHandler.equals(handlerCPlayer2)) {
            sessionData.setPlayerReady(2, !(sessionData.getPlayer(2).isReady()));
        }
    }
    
    void clientConnectionWarning(ClientHandler clientHandler) {
        if (clientHandler.equals(handlerCPlayer1)) {
            ((Menu)Utils.getCurrentScreen()).event("CONNECTION_WARNING_PLAYER_1");
        }
        if (clientHandler.equals(handlerCPlayer2)) {
            ((Menu)Utils.getCurrentScreen()).event("CONNECTION_WARNING_PLAYER_2");
        }
    }
    
    void clientLost(ClientHandler clientHandler) {
        if (clientHandler.equals(handlerCPlayer1)) {
            handlerCPlayer1 = null;
            sessionData.setPlayer(null, 1);
            if (!clientSearchOngoing)
                clientSearch();
        }
        if (clientHandler.equals(handlerCPlayer2)) {
            handlerCPlayer2 = null;
            sessionData.setPlayer(null, 2);
            if (!clientSearchOngoing)
                clientSearch();
        }
    }
    
    @Override
    public void run() {
        
        clientSearch();
        
        while (!stop) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {e.printStackTrace();}
        }
        
        try {
            if (handlerCPlayer1 != null) {
                handlerCPlayer1.stopClientHandler();
                handlerCPlayer1 = null;
            }
            if (handlerCPlayer2 != null) {
                handlerCPlayer2.stopClientHandler();
                handlerCPlayer2 = null;
            }
            serverSocket.close();
            serverSocket = null;
        } catch (IOException e) {e.printStackTrace();}
        
    }
    
}
