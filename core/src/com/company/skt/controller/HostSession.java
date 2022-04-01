package com.company.skt.controller;

import com.company.skt.model.GameList;
import com.company.skt.model.Player;
import com.company.skt.model.Settings;
import com.company.skt.model.Utils;

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
    Player[] players;   // TODO store somewhere in model
    GameList gameList;   // TODO store somewhere in model
    Properties appCfg;
    Properties gameCfg;
    ServerSocket sSocketT;
    ServerSocket sSocketO;
    ClientHandler handlerCPlayer1;
    ClientHandler handlerCPlayer2;
    
    public HostSession() {
        lobby = false;
        appCfg = Settings.getProperties(Settings.APP);
        gameCfg = Settings.getProperties(Settings.GAME);
        ((Menu)Utils.getCurrentScreen()).event("HOST_LOBBYUI");
        // OLD: lobbyUI = new LobbyUI(mainMenu, this);
        lobby = true;
        players = new Player[3];
        players[0] = new Player(appCfg.getProperty("player_name"));
        ((Menu)Utils.getCurrentScreen()).event("LOBBY_SET_PLAYER0_SELF");
        // OLD: lobbyUI.setPlayer(0, players[0].name);
        clientSearchOngoing = false;
        stop = false;
        try {
            sSocketT = new ServerSocket(tPORT);
            sSocketO = new ServerSocket(oPORT);
            start();
        } catch (IOException e) {e.printStackTrace();}
    }
    
    // TODO remove when moved to model
    public GameList getGameList() {
        return gameList;
    }
    
    // TODO remove when moved to model
    public void createGameList() {
        gameList = new GameList(Integer.parseInt(gameCfg.getProperty("amount_games")));
    }
    
    public void sendObjectToAll(String objName) {
        Object o;
        switch(objName) {
            case "gameCfg":
                o = gameCfg; // TODO change when moved to model
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
                            handlerCPlayer1 = new ClientHandler(HostSession.this, sSocketT.accept(), sSocketO.accept());
                            while (players[1] == null && !stop) {
                                players[1] = handlerCPlayer1.getPlayer();
                            }
                            ((Menu)Utils.getCurrentScreen()).event("LOBBY_SET_PLAYER1_HOST");
                            // OLD: lobbyUI.setPlayer(1, players[1].name);
                        }
                        if (handlerCPlayer1 != null && handlerCPlayer2 == null && !stop) {
                            handlerCPlayer2 = new ClientHandler(HostSession.this, sSocketT.accept(), sSocketO.accept());
                            while (players[2] == null && !stop) {
                                players[2] = handlerCPlayer2.getPlayer();
                            }
                            ((Menu)Utils.getCurrentScreen()).event("LOBBY_SET_PLAYER1_HOST");
                            // OLD: lobbyUI.setPlayer(2, players[2].name);
                        }
                    } catch (IOException ignored) {}
                }
            }
        });
        clientSearch.start();
        clientSearchOngoing = false;
    }
    
    public void unreadyAllClients() {
        players[1].isReady = false;
        players[2].isReady = false;
    }
    
    void clientReadyToggle(ClientHandler ch) {
        if (ch.equals(handlerCPlayer1)) {
            players[1].isReady = !(players[1].isReady);
        }
        if (ch.equals(handlerCPlayer2)) {
            players[2].isReady = !(players[2].isReady);
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
