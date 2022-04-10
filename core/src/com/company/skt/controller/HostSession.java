package com.company.skt.controller;

import com.company.skt.lib.TaskCompleteException;
import com.company.skt.lib.Player;
import com.company.skt.model.SessionData;
import com.company.skt.model.Settings;
import com.company.skt.view.DebugWindow;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.concurrent.*;

public class HostSession extends Session {
    
    private final int PORT = 2152;
    private boolean clientSearchOngoing;
    private ScheduledThreadPoolExecutor executor;
    private SessionData sessionData;
    private Properties appCfg;
    private ServerSocket serverSocket;
    private ClientHandler handlerPlayer1;
    private ClientHandler handlerPlayer2;
    
    HostSession() {
        try {startSession();} catch(IOException e) {e.printStackTrace();}
    }
    
    HostSession(boolean start) {
        if(start) {
            try {startSession();} catch(IOException e) {e.printStackTrace();}
        }
    }

    @Override
    void startSession() throws IOException {
        DebugWindow.println("[HostSession] starting");
        appCfg = Settings.getProperties(Settings.APP);
        sessionData = SessionData.get(true);
        Player self = new Player(appCfg.getProperty("player_name"));
        self.setConnectivity(1);
        self.setReady(true);
        sessionData.setPlayer(self, 0);
        try {
            serverSocket = new ServerSocket(PORT);
        } catch(IOException e) {e.printStackTrace();}
        ((Menu)Utils.getCurrentScreen()).event("READY_FOR_LOBBY");
        Executors.newCachedThreadPool();
            // TODO seems a bad solution: better wrap singleThreadScheduled araund a CachedThreadPool
        executor = new ScheduledThreadPoolExecutor(32);
        handlerPlayer1 = new ClientHandler(HostSession.this);
        handlerPlayer2 = new ClientHandler(HostSession.this);
        clientSearch();
    }
    
    @Override
    void stopSession() {
        executor.shutdownNow();
        try {
            if(handlerPlayer1 != null) {
                DebugWindow.println("[HostSession] stopping handler: handlerPlayer1");
                handlerPlayer1.stopClientHandler();
                handlerPlayer1 = null;
            }
            if(handlerPlayer2 != null) {
                DebugWindow.println("[HostSession] stopping handler: handlerPlayer2");
                handlerPlayer2.stopClientHandler();
                handlerPlayer2 = null;
            }
            DebugWindow.println("[HostSession] closing serverSocket");
            serverSocket.close();
            serverSocket = null;
        } catch(IOException e) {e.printStackTrace();}
        DebugWindow.println("[HostSession] ended");
    }
    
    private synchronized void clientSearch() {
        DebugWindow.println("[HostSession] starting clientsearch");
        clientSearchOngoing = true;
        executor.scheduleAtFixedRate(() -> {
            if(!handlerPlayer1.hasSocket() || !handlerPlayer2.hasSocket()) {
                if(!handlerPlayer1.hasSocket()) {
                    DebugWindow.println("[HostSession] listening @port for player1");
                    handlerPlayer1.listenAtPort();
                    if(handlerPlayer1.hasSocket()) {
                        DebugWindow.println("[HostSession] starting handlerPlayer1");
                        executor.schedule(handlerPlayer1, 0, TimeUnit.MILLISECONDS);
                    }
                }
                if(handlerPlayer1.hasSocket() && !handlerPlayer2.hasSocket()) {
                    DebugWindow.println("[HostSession] listening @port for player2");
                    handlerPlayer2.listenAtPort();
                    if(handlerPlayer2.hasSocket()) {
                        DebugWindow.println("[HostSession] starting handlerPlayer2");
                        executor.schedule(handlerPlayer2, 0, TimeUnit.MILLISECONDS);
                    }
                }
            } else {
                clientSearchOngoing = false;
                DebugWindow.println("[HostSession] clientsearch ended");
                throw new TaskCompleteException();
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }
    
    void unreadyAllClients() {
        DebugWindow.println("[HostSession] setPlayerReady to false for both clients");
        sessionData.setPlayerReady(1, false);
        sessionData.setPlayerReady(2, false);
    }
    
    void clientReadyToggle(ClientHandler clientHandler) {
        if(clientHandler.equals(handlerPlayer1)) {
            DebugWindow.println("[HostSession] setPlayerReady for player1 to " +
                                !(sessionData.getPlayer(1).isReady()));
            sessionData.setPlayerReady(1, !(sessionData.getPlayer(1).isReady()));
        }
        if(clientHandler.equals(handlerPlayer2)) {
            DebugWindow.println("[HostSession] setPlayerReady for player2 to " +
                                !(sessionData.getPlayer(1).isReady()));
            sessionData.setPlayerReady(2, !(sessionData.getPlayer(2).isReady()));
        }
    }
    
    void sendStringToAll(String msg) {
        DebugWindow.println("[HostSession] sending String to all clients: " +
                            (msg.length() > 8 ? msg.substring(0, 8) + "..." : msg));
        if(handlerPlayer1.isConnected()) {
            handlerPlayer1.sendString(msg);
        }
        if(handlerPlayer2.isConnected()) {
            handlerPlayer2.sendString(msg);
        }
    }
    
    ClientHandler getHandler(int playerNumber) {
        if(playerNumber == 1) {
            return handlerPlayer1;
        } else {
            return handlerPlayer2;
        }
    }
    
    ScheduledExecutorService getExecutor() {
        return executor;
    }
    
    ServerSocket getServerSocket() {
        return serverSocket;
    }
}
