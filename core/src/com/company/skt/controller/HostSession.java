package com.company.skt.controller;

import com.company.skt.model.Player;
import com.company.skt.model.SessionData;
import com.company.skt.model.Settings;
import com.company.skt.view.DebugWindow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HostSession extends Session {
    
    private final int PORT = 2152;
    private volatile boolean stop;
    private boolean clientSearchOngoing;
    ScheduledExecutorService clientSearchThread;
    ScheduledExecutorService nameFetchThread;
    SessionData sessionData;
    Properties appCfg;
    Properties sessionCfg;
    ServerSocket serverSocket;
    ClientHandler handlerCPlayer1;
    ClientHandler handlerCPlayer2;
    
    HostSession() {
        appCfg = Settings.getProperties(Settings.APP);
        sessionData = SessionData.get(true);
        sessionCfg = sessionData.getSessionCfg();
        sessionData.setPlayer(new Player(appCfg.getProperty("player_name")), 0);
        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(10000); //DEBUG
            start();
        } catch (IOException e) {e.printStackTrace();}
        ((Menu)Utils.getCurrentScreen()).event("READY_FOR_LOBBY");
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
    
    private synchronized void clientSearch() {
        clientSearchThread = Executors.newSingleThreadScheduledExecutor();
        clientSearchThread.scheduleAtFixedRate(() -> {
            clientSearchOngoing = true;
            while (!stop && (handlerCPlayer1 == null || handlerCPlayer2 == null)) {
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
                }
                catch (SocketTimeoutException ste){
                }
                catch (IOException ignored) {}
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
        clientSearchOngoing = false;
    }
    
    private synchronized void fetchPlayerName(ClientHandler clientHandler) {
        nameFetchThread = Executors.newSingleThreadScheduledExecutor();
        nameFetchThread.scheduleAtFixedRate(() -> {
            while (sessionData.getPlayer(1) == null && !stop) {
                DebugWindow.println("loop:fetch player name");  //DEBUG
                if(clientHandler.getPlayer() != null) {
                    sessionData.setPlayer(clientHandler.getPlayer(), 1);
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
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
            DebugWindow.println("loop: hostsession");  //DEBUG
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {e.printStackTrace();}

        }
        
        endSession();
        
    }
    
    private void endSession() {
    
        try {
            if (handlerCPlayer1 != null) {
                DebugWindow.println("Hostsession stopping handler... handlerCPlayer1 != null");
                handlerCPlayer1.stopClientHandler();
                handlerCPlayer1 = null;
            }
            if (handlerCPlayer2 != null) {
                DebugWindow.println("Hostsession stopping handler... handlerCPlayer2 != null");
                handlerCPlayer2.stopClientHandler();
                handlerCPlayer2 = null;
            }
            if(clientSearchThread != null) {
                clientSearchThread.shutdownNow();
            }
            if(nameFetchThread != null) {
                nameFetchThread.shutdownNow();
            }
            serverSocket.close();
            serverSocket = null;
        } catch (IOException e) {e.printStackTrace();}
        System.out.println("Hostsession ended");
    }
    
}
