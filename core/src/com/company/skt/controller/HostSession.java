package com.company.skt.controller;

import com.company.skt.model.Player;
import com.company.skt.model.SessionData;
import com.company.skt.model.Settings;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

public class HostSession extends Session {
    
    private final int tPORT = 2152;
    private final int oPORT = 2153;
    private Thread clientSearch;
    private volatile boolean stop;
    private boolean clientSearchOngoing;
    public boolean lobby;
    public boolean summary;
    public boolean game;
    SessionData sessionData;
    Properties appCfg;
    Properties sessionCfg;
    ServerSocket sSocketT;
    ServerSocket sSocketO;
    ClientHandler handlerCPlayer1;
    ClientHandler handlerCPlayer2;
    
    public HostSession() {
        lobby = false;
        appCfg = Settings.getProperties(Settings.APP);
        lobby = true;
        sessionData = SessionData.get(true);
        sessionCfg = sessionData.getSessionCfg();
        sessionData.setPlayer(new Player(appCfg.getProperty("player_name")), 0);
        clientSearchOngoing = false;
        stop = false;
        try {
            sSocketT = new ServerSocket(tPORT);
            sSocketO = new ServerSocket(oPORT);
            start();
        } catch (IOException e) {e.printStackTrace();}
    }
    
    public void sendObjectToAll(String objName) {
        Object o;
        switch(objName) {
            case "gameCfg":
                o = sessionCfg; // TODO change when moved to model
                break;
            case "players":
                o = players; // TODO change when moved to model
                break;
            default:
                System.out.println("Illegal object name for broadcasting to all players");
                o = null;
                break;
        }
        if (handlerCPlayer1 != null) {
            handlerCPlayer1.sendObject(o);
        }
        if (handlerCPlayer2 != null) {
            handlerCPlayer2.sendObject(o);
        }
    }
    
    public void sendTextToAll(String msg) {
        if (handlerCPlayer1 != null) {
            handlerCPlayer1.sendText(msg);
        }
        if (handlerCPlayer2 != null) {
            handlerCPlayer2.sendText(msg);
        }
    }
    
    public void stopSession() {
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
                                HostSession.this, sSocketT.accept(), sSocketO.accept());
                            while (sessionData.getPlayer(1) == null && !stop) {
                                sessionData.setPlayer(handlerCPlayer1.getPlayer(), 1);
                            }
                        }
                        if (handlerCPlayer1 != null && handlerCPlayer2 == null && !stop) {
                            handlerCPlayer2 = new ClientHandler(
                                HostSession.this, sSocketT.accept(), sSocketO.accept());
                            while (sessionData.getPlayer(2) == null && !stop) {
                                sessionData.setPlayer(handlerCPlayer2.getPlayer(), 2);
                            }
                            ((Menu)Utils.getCurrentScreen()).event("LOBBY_SET_PLAYER1_HOST");
                        }
                    } catch (IOException ignored) {}
                }
            }
        });
        clientSearch.start();
        clientSearchOngoing = false;
    }
    
    public void unreadyAllClients() {
        sessionData.getPlayer(1).isReady = false;
        sessionData.getPlayer(2).isReady = false;
    }
    
    void clientReadyToggle(ClientHandler clientHandler) {
        if (clientHandler.equals(handlerCPlayer1)) {
            sessionData.getPlayer(1).isReady = !(sessionData.getPlayer(1).isReady);
        }
        if (clientHandler.equals(handlerCPlayer2)) {
            sessionData.getPlayer(2).isReady = !(sessionData.getPlayer(2).isReady);
        }
        ((Menu)Utils.getCurrentScreen()).event("LOBBY_UPDATE_PLAYERS");
        // OLD: lobbyUI.updatePlayers(players);
        sendObjectToAll("players");
        /* ### DEBUG OLD CODE TO REPAIR ### */
        // TODO DEBUG somehow get clientReadyState from model to repair this:
        if(players[1].isReady && players[2].isReady) {
            if(lobby /* DEBUG OLD: && !lobbyUI.getAllClientsReady() */) {
                ((Menu)Utils.getCurrentScreen()).event("LOBBY_ALL_CLIENTS_READY_TOGGLE");
                // OLD: lobbyUI.allClientsReadyToggle();
            }
            if(summary /* DEBUG OLD: && !summaryUI.getAllClientsReady() */) {
                ((Menu)Utils.getCurrentScreen()).event("SUMMARY_ALL_CLIENTS_READY_TOGGLE");
                // OLD: summaryUI.allClientsReadyToggle();
            }
        }
        if(!players[1].isReady || !players[2].isReady) {
            if(lobby /* DEBUG OLD: && lobbyUI.getAllClientsReady() */) {
                ((Menu)Utils.getCurrentScreen()).event("LOBBY_ALL_CLIENTS_READY_TOGGLE");
                // OLD: lobbyUI.allClientsReadyToggle();
            }
            if(summary /* DEBUG OLD: && summaryUI.getAllClientsReady() */) {
                ((Menu)Utils.getCurrentScreen()).event("SUMMARY_ALL_CLIENTS_READY_TOGGLE");
                // OLD: summaryUI.allClientsReadyToggle();
            }
        
        }
    }
    
    void clientLost(ClientHandler ch) {
        if (ch.equals(handlerCPlayer1)) {
            handlerCPlayer1 = null;
            ((Menu)Utils.getCurrentScreen()).event("LOBBY_REMOVE_PLAYER1_HOST");
            // OLD: lobbyUI.removePlayer(1);
            players[1] = null;
            if (!clientSearchOngoing)
                clientSearch();
        }
        if (ch.equals(handlerCPlayer2)) {
            handlerCPlayer2 = null;
            ((Menu)Utils.getCurrentScreen()).event("LOBBY_REMOVE_PLAYER2_HOST");
            // OLD: lobbyUI.removePlayer(2);
            players[2] = null;
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
            sSocketT.close();
            sSocketT = null;
            sSocketO.close();
            sSocketO = null;
        } catch (IOException e) {e.printStackTrace();}
        
    }
    
}
