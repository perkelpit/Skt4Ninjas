package com.company.skt.controller;

import com.company.skt.view.DebugWindow;

import java.io.IOException;

public class ExcessHandler extends ClientHandler {

    ExcessHandler(HostSession hostSession) {
        super(hostSession);
    }

    @Override
    public void run() {
        handlerTag = "[ExcessHandler]";
        DebugWindow.println(handlerTag + " started. connecting...");
        connected = connectToClient();
        if (connected) {
            DebugWindow.println("[ExcessHandler] connected. rejecting...");
            sendString("REJECTED");
        }
        DebugWindow.println("[ExcessHandler] rejected. stopping...");
        try {stopClientHandler();} catch(IOException e) {e.printStackTrace();}
        DebugWindow.println("[ExcessHandler] stopped.");
    }

}
