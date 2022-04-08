package com.company.skt.controller;

import com.company.skt.lib.Lock;
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
    private SessionData sessionData;
    private Socket socket;
    private Player player;
    private int playerNumber;
    private PrintWriter out;
    private HostStringStreamHandler in;
    private HeartBeat heartBeat;
    private volatile boolean connected;
    
    
    /* ### [INNER CLASS]: HEARTBEAT ### */
    
    private class HeartBeat {
        private Lock lock;
        private int pingRate;
        private int noPongCount;
        private volatile boolean pong;
        private volatile boolean stop;
        
        HeartBeat(int ms) {
            lock = new Lock();
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
                sendString("PING");

                lock.syncWait((pingRate/4)*3);

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
                if(!in.startsWith("PONG")) {
                    DebugWindow.println(handlerTag + " Msg recieved: " +
                                        (in.length() > 8 ? in.substring(0, 8) + "..." : in));
                }
                if (in.startsWith("QUIT")) {
                    stopClientHandler();
                }
                if (in.startsWith("PONG")) {
                    heartBeat.pong();
                }
                if (in.startsWith("CFG_REC")) {
                    sendString("QRY_PLAYER");
                }
                if (in.startsWith("PLAYER")) {
                    parseAndSetPlayer(in);
                    sendString("LOGGEDIN");
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
    }
    
    @Override
    public void run() {
        sessionData = SessionData.get();
        playerNumber = (ClientHandler.this == hostSession.getHandler(1)) ? 1 : 2;
        handlerTag = "[ClientHandler(" + playerNumber + ")]";
        DebugWindow.println(handlerTag + " initializing connection");
        connected = connectToClient();
        if (!connected) {
            try {stopClientHandler();} catch(IOException e) {e.printStackTrace();}
        } else {
            DebugWindow.println(handlerTag + " connected");
            //sendString("CFG>amount_games=6;time_limit=0;lost_factor=-2;ramsch=true;"); // DEBUG
            //TODO DEBUG why the heck causes the following line the calling thread to freeze?!?(seems so)
            sendString(SessionData.getCfgString());
        }
    }
    
    synchronized void sendString(String msg) {
        if (!msg.startsWith("PING"))
            DebugWindow.println(handlerTag + " Sending Msg: " +
                                (msg.length() > 8 ? msg.substring(0, 8) + "..." : msg));
        out.println(msg);
    }
    
    Player getPlayer() {
        return player;
    }
    
    void listenAtPort(){
        try {socket = hostSession.getServerSocket().accept();}
        catch(IOException ignored) {}
    }
    
    private boolean connectToClient() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            DebugWindow.println(handlerTag + " opened OutputStream");
            in = new HostStringStreamHandler(new BufferedReader(
                new InputStreamReader(socket.getInputStream())), 100, this);
            in.startStreamHandler();
            DebugWindow.println(handlerTag + " opened InputStream");
            heartBeat = new HeartBeat(400);
            heartBeat.start();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
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
        String name = playerString.substring(playerString.indexOf('>') + 1);
        SessionData.get().setPlayer(new Player(name), playerNumber);
    }
    
}
