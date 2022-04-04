package com.company.skt.view;

import com.company.skt.model.SessionData;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

public class TextAreaView extends JFrame {
    
    private static TextAreaView textAreaView;
    private static JTextArea textArea;
    private static String lobbyData;
    
    public static void showTextAreaView() {
        if(textAreaView == null) {
            textAreaView = new TextAreaView();
        }
        textAreaView.setVisible(true);
    }
    
    public static void hideTextAreaView() {
        if(textAreaView != null) {
            textAreaView.setVisible(false);
        }
    }
    
    public static void disposeTextAreaView() {
        if(textAreaView != null) {
            textAreaView.dispose();
        }
    }
    
    public TextAreaView() {
        Dimension dimension = new Dimension(400, 400);
        setSize(dimension);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        getContentPane().setSize(dimension);
        getContentPane().setPreferredSize(dimension);
        getContentPane().setMinimumSize(dimension);
        textArea = new JTextArea();
        textArea.setSize(dimension);
        textArea.setMinimumSize(dimension);
        textArea.setPreferredSize(dimension);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        textArea.setEditable(false);
        textArea.setBackground(new Color(43,49,47));
        textArea.setForeground(new Color(95,201,197));
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        ((DefaultCaret)textArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        add(scrollPane);
        setVisible(false);
        pack();
    }
    
    public static void updateTAVLobbyData() {
        SessionData data = SessionData.get();
        lobbyData = "";
        lobbyData += "        ### LOBBY ###        " + "\n";
        lobbyData += "Host: " + SessionData.isHost() + "\n";
        lobbyData += "-----------------------------" + "\n";
        lobbyData += "Player0: " + data.getPlayer(0) + "\n";
        lobbyData += "Player1: " + data.getPlayer(1) + "\n";
        lobbyData += "Player2: " + data.getPlayer(2) + "\n";
        lobbyData += "-----------------------------" + "\n";
        lobbyData += "      Session Settings:      " + "\n";
        for(String key : data.getSessionCfg().stringPropertyNames()) {
            lobbyData += key + ": " + data.getCfgValue(key) + "\n";
        }
        updateTAV();
    }
    
    private static void updateTAV() {
        textArea.setText(lobbyData);
        textArea.update(textArea.getGraphics());
    }
    
}
