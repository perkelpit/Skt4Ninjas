package com.company.skt.controller;

import com.company.skt.lib.Player;
import com.company.skt.lib.TaskCompleteException;
import com.company.skt.model.SessionData;
import com.company.skt.view.DebugWindow;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable {
    
    /* ### [CLIENT HANDLER]: FIELDS */
    
    private String handlerTag;
    private HostSession hostSession;
    private Socket socket;
    private Player player;
    private int playerNumber;
    private PrintWriter out;
    private HostStringStreamHandler in;
    private HeartBeat heartBeat;
    private volatile boolean connected;
    
    
    /* ### [INNER CLASS]: HEARTBEAT ### */
    
    private class HeartBeat {
        private Object lock;
        private int pingRate;
        private int noPongCount;
        private volatile boolean pong;
        private volatile boolean stop;
        
        HeartBeat(int ms) {
            lock = new Object();
            pingRate = ms;
            noPongCount = 0;
        }
        
        public void stop() {
            stop = true;
        }
        
        public void pong() {
            pong = true;
        }
        
        public void start() {
            DebugWindow.println(handlerTag + " starting heartbeat");
            hostSession.getExecutor().scheduleAtFixedRate(() -> {
                if(stop) {
                    DebugWindow.println(handlerTag + " stopping heartbeat");
                    throw new TaskCompleteException();
                }
                pong = false;
                out.println("PING");
                hostSession.getExecutor().schedule(() -> {
                    if (pong) {
                        noPongCount = 0;
                    } else {
                        ++noPongCount;
                        if (noPongCount == 10 || noPongCount == 20 ) {
                            hostSession.clientConnectionWarning(ClientHandler.this);
                        }
                        if (noPongCount > 30) {
                            hostSession.clientLost(ClientHandler.this);
                            try {stopClientHandler();} catch (IOException e) {e.printStackTrace();}
                        }
                    }
                    lock.notify();
                }, pingRate/2, TimeUnit.MILLISECONDS);
                try {lock.wait();} catch(InterruptedException ignored) {}
            }, 0, pingRate/2, TimeUnit.MILLISECONDS);
        }
        
    }
    
    
    /* ### [INNER CLASS]: STREAM HANDLER for Strings ### */
    
    private class HostStringStreamHandler extends StringStreamHandler {
        private ClientHandler clientHandler;
        
        HostStringStreamHandler(BufferedReader br, int delay, ClientHandler clientHandler) {
            super(br, delay);
            this.clientHandler = clientHandler;
        }
        
        @Override
        protected void process(String in) throws IOException {
            if (in != null) {
                if (in.startsWith("QUIT")) {
                    stopClientHandler();
                }
                if (in.startsWith("PONG")) {
                    heartBeat.pong();
                }
                if (in.startsWith("SETT_REC")) {
                    sendText("QRY_PLAYER");
                }
                if (in.startsWith("PLAYER")) {
                    parseAndSetPlayer(in);
                    sendText("LOGGEDIN");
                }
                if (in.startsWith("RDY_TGL")) {
                    hostSession.clientReadyToggle(clientHandler);
                }
            }
        }
        
    }
    
    
    /* ### [CLIENT HANDLER]: CONSTRUCTOR & METHODS */
    
    ClientHandler(HostSession hostSession) {
        this.hostSession = hostSession;
        playerNumber = ClientHandler.this == hostSession.getHandler(1) ? 1 : 2;
        handlerTag = "[ClientHandler(" + playerNumber + ")]";
    }
    
    @Override
    public void run() {
        connected = connectToClient();
        if (!connected) {
            try {stopClientHandler();} catch(IOException e) {e.printStackTrace();}
        }
    }
    
    
    void listenAtPort(){
        try {socket = hostSession.getServerSocket().accept();}
        catch(IOException ignored) {}
    }
    
    private boolean connectToClient() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new HostStringStreamHandler(new BufferedReader(
                new InputStreamReader(socket.getInputStream())), 100, this);
            in.startStreamHandler();
            sendText(SessionData.getCfgString());
            heartBeat = new HeartBeat(200);
            heartBeat.start();
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
        if(heartBeat != null) {
            heartBeat.stop();
            heartBeat = null;
        }
        if(out != null) {
            out.println("END");
            out.close();
            out = null;
        }
        if(in != null) {
            in.stopStreamHandler();
            in = null;
        }
        player = null;
        connected = false;
        socket = null;
    }
    
    boolean hasSocket() {
        return socket != null;
    }
    
    boolean isConnected() {
        return connected;
    }
    
    void parseAndSetPlayer(String playerString) {
        String name = playerString.substring(playerString.indexOf('#') + 1);
        SessionData.get().setPlayer(new Player(name), playerNumber);
    }
    
}
