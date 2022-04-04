package com.company.skt.controller;

import com.company.skt.model.Player;
import com.company.skt.model.SessionData;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    
    private HostSession hostSession;
    private Socket socket;
    private Player player;
    private PrintWriter out;
    private HostStringStreamHandler in;
    private HeartBeat heartBeat;
    private volatile boolean stop;
    
    private class HeartBeat extends Thread {
        private int pingRate;
        private int noPongCount;
        private volatile boolean pong;
        private volatile boolean stop;
        
        HeartBeat(int ms) {
            pingRate = ms;
            noPongCount = 0;
        }
        
        public void stopHeartBeat() {
            stop = true;
        }
        
        public void pong() {
            pong = true;
        }
        
        public void run() {
            while (!stop) {
                pong = false;
                out.println("PING");
                /* TODO Pingrate ans Ende, hier wait() mit max. waiting time check, latency
                 * pong() um notify() ergänzen, noPongCount ersetzen etc. */
                try {
                    Thread.sleep(pingRate);
                } catch (InterruptedException e) {e.printStackTrace();}
                if (pong) {
                    noPongCount = 0;
                } else {
                    ++noPongCount;
                    if (noPongCount == 10 || noPongCount == 20 ) {
                        hostSession.clientConnectionWarning(ClientHandler.this);
                    }
                    if (noPongCount > 30) {
                        stop = true;
                        hostSession.clientLost(ClientHandler.this);
                        try {
                            stopClientHandler();
                        } catch (IOException e) {e.printStackTrace();}
                    }
                }
            }
        }
        
    }
    
    private class HostStringStreamHandler extends StringStreamHandler {
        private ClientHandler clientHandler;
        
        HostStringStreamHandler(BufferedReader br, int delay, ClientHandler clientHandler) {
            super(br, delay);
            this.clientHandler = clientHandler;
        }
        
        protected void process(String in) throws IOException {
            if (in != null) {
                if (in.startsWith("QUIT")) {
                    stopClientHandler();
                }
                if (in.startsWith("PONG")) {
                    heartBeat.pong();
                }
                if (in.startsWith("SETT_REC")) {
                    sendText("LOGGEDIN");
                }
                if (in.startsWith("RDY_TGL")) {
                    hostSession.clientReadyToggle(clientHandler);
                }
            }
        }
        
    }
    
    ClientHandler(HostSession hostSession, Socket socket) {
        this.hostSession = hostSession;
        this.socket = socket;
        if (!connectToClient()) {
            try {
                stopClientHandler();
            } catch(IOException e) {e.printStackTrace();}
        }
    }
    
    private boolean connectToClient() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            heartBeat = new HeartBeat(200);
            heartBeat.start();
            in = new HostStringStreamHandler(new BufferedReader(
                new InputStreamReader(socket.getInputStream())), 100, this);
            in.startStreamHandler();
            sendText(SessionData.getCfgString());
            start();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }
    
    void sendText(String msg) {
        out.println(msg);
    }
    
    Player getPlayer() {
        return player;
    }
    
    void stopClientHandler() throws IOException {
        stop = true;
        heartBeat.stopHeartBeat();
        in.stopStreamHandler();
        out.close();
    }
    
    public void run() {
        while (!socket.isClosed()) {
            while(!stop) {
                try {
                    // TODO do something usefull here
                    Thread.sleep(500);
                } catch (InterruptedException e) {e.printStackTrace();}
            }
        }
    }
    
}
