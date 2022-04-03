package com.company.skt.view;

import com.company.skt.model.SessionData;

public class ConsoleView {
    
    public static void printLobbyData() {
        SessionData data = SessionData.get();
        System.out.println("        ### LOBBY ###        ");
        System.out.println("Host: " + SessionData.isHost());
        System.out.println("-----------------------------");
        System.out.println("Player0: " + data.getPlayer(0));
        System.out.println("Player1: " + data.getPlayer(1));
        System.out.println("Player2: " + data.getPlayer(2));
        System.out.println("-----------------------------");
        System.out.println("      Session Settings:      ");
        for(String key : (String[])data.getSessionCfg().keySet().toArray()) {
            System.out.println(key + ": " + data.getCfgValue(key));
        }
    }
    
}
