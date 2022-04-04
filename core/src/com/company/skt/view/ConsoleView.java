package com.company.skt.view;

import com.company.skt.model.SessionData;

import javax.swing.*;
import java.awt.*;

public class ConsoleView extends JFrame {
    
    public ConsoleView() {
        Dimension dimension = new Dimension(800, 500);
        setSize(dimension);
        JTextArea textArea = new JTextArea();
        textArea.setSize(dimension);
        textArea.setEditable(false);
        add(textArea);
        
    }
    
    public static void printLobbyData() {
        SessionData data = SessionData.get();
        String dataString = "";
        dataString += "        ### LOBBY ###        " + "\n";
        dataString += "Host: " + SessionData.isHost() + "\n";
        dataString += "-----------------------------" + "\n";
        dataString += "Player0: " + data.getPlayer(0) + "\n";
        dataString += "Player1: " + data.getPlayer(1) + "\n";
        dataString += "Player2: " + data.getPlayer(2) + "\n";
        dataString += "-----------------------------" + "\n";
        dataString += "      Session Settings:      " + "\n";
        for(String key : (String[])data.getSessionCfg().keySet().toArray()) {
            dataString += key + ": " + data.getCfgValue(key) + "\n";
        }
    }
    
}
