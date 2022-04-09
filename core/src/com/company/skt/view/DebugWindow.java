package com.company.skt.view;

import com.company.skt.model.SessionData;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class DebugWindow extends JFrame {

    public enum Focus {
        Main, Settings, Archive, Lobby, Summary, Game
    }
    
    private static DebugWindow debugWindow;
    private static JTextArea textAreaTop;
    private static JTextArea textAreaBottom;
    private static String lobbyData;
    private static String mainData;
    private static String settingsData;
    private static String archiveData;
    private static String summaryData;
    private static String gameData;
    private static Focus focus;

    public static void createDebugWindow() {
        if (debugWindow == null) {
            debugWindow = new DebugWindow();
        }
        debugWindow.setVisible(false);
        update();
    }
    
    public static void showDebugWindow() {
        if (debugWindow != null) {
            debugWindow.setVisible(true);
        }
    }

    public static void hideDebugWindow() {
        if (debugWindow != null) {
            debugWindow.setVisible(false);
        }
    }

    public static void disposeDebugWindow() {
        if (debugWindow != null) {
            debugWindow.dispose();
        }
    }
    
    public static void setPosition(int x, int y) {
        if(debugWindow != null) {
            debugWindow.setLocation(x, y);
        }
    }
    
    public static DebugWindow getWindow() {
        return debugWindow;
    }
    
    public static void setUIFocus(Focus focus) {
        DebugWindow.focus = focus;
        update();
    }

    public DebugWindow() {
        focus = Focus.Main;
        Dimension windowSize = new Dimension(400, 750);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(windowSize);
        setMinimumSize(windowSize);
        setPreferredSize(windowSize);
        setUndecorated(true);
        getContentPane().setSize(windowSize);
        getContentPane().setPreferredSize(windowSize);
        getContentPane().setMinimumSize(windowSize);
        getContentPane().setLayout(new FlowLayout());
        Dimension topAreaSize = new Dimension(windowSize.width - 10, (windowSize.height / 2) - 5);
        textAreaTop = new JTextArea();
        textAreaTop.setSize(topAreaSize);
        textAreaTop.setMinimumSize(topAreaSize);
        textAreaTop.setPreferredSize(topAreaSize);
        textAreaTop.setEditable(false);
        textAreaTop.setFont(new Font("Arial", Font.PLAIN, 20));
        textAreaTop.setEditable(false);
        textAreaTop.setBackground(new Color(43, 49, 47));
        textAreaTop.setForeground(new Color(95, 201, 197));
        textAreaTop.setWrapStyleWord(true);
        textAreaTop.setLineWrap(true);
        JScrollPane scrollPaneTop = new JScrollPane(textAreaTop);
        scrollPaneTop.setPreferredSize(topAreaSize);
        ((DefaultCaret) textAreaTop.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        add(scrollPaneTop);
        Dimension bottomAreaSize = new Dimension(windowSize.width - 10, ((windowSize.height / 2) - 5));
        textAreaBottom = new JTextArea();
        //textAreaBottom.setSize(dimension);
        textAreaBottom.setMinimumSize(bottomAreaSize);
        //textAreaBottom.setPreferredSize(dimension);
        textAreaBottom.setEditable(false);
        textAreaBottom.setFont(new Font("Arial", Font.PLAIN, 20));
        textAreaBottom.setEditable(false);
        textAreaBottom.setBackground(new Color(43, 49, 47));
        textAreaBottom.setForeground(new Color(95, 201, 197));
        textAreaBottom.setWrapStyleWord(true);
        textAreaBottom.setLineWrap(true);
        JScrollPane scrollPaneBottom = new JScrollPane(textAreaBottom);
        scrollPaneBottom.setPreferredSize(bottomAreaSize);
        ((DefaultCaret) textAreaBottom.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        add(scrollPaneBottom);
        setVisible(false);
        pack();
    }

    public static void println(String string) {
        if (textAreaBottom != null) {
            textAreaBottom.append(string + "\n");
            try {
                textAreaBottom.setCaretPosition(textAreaBottom.getLineStartOffset(textAreaBottom.getLineCount() - 1));
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void update() {
        updateData();
        if (textAreaTop != null) {
            switch(focus) {
                case Lobby:
                    textAreaTop.setText(lobbyData);
                    break;
                case Main:
                    textAreaTop.setText(mainData);
                    break;
                case Settings:
                    textAreaTop.setText(settingsData);
                    break;
                case Archive:
                    textAreaTop.setText(archiveData);
                    break;
                case Summary:
                    textAreaTop.setText(summaryData);
                    break;
                case Game:
                    textAreaTop.setText(gameData);
                    break;
            }
        }
    }

    private static void updateData() {
        StringBuilder stringBuilder;
        switch(focus) {
            case Lobby:
                SessionData data = SessionData.get();
                lobbyData = "";
                stringBuilder = new StringBuilder(lobbyData);
                stringBuilder.append("        ### LOBBY ###        " + "\n");
                stringBuilder.append("Host: " + SessionData.isHost() + "\n");
                stringBuilder.append("-----------------------------" + "\n");
                stringBuilder.append("Player0: " + data.getPlayer(0) + "\n");
                stringBuilder.append("Player1: " + data.getPlayer(1) + "\n");
                stringBuilder.append("Player2: " + data.getPlayer(2) + "\n");
                stringBuilder.append("-----------------------------" + "\n");
                stringBuilder.append("      Session Settings:      " + "\n");
                for (String key : data.getSessionCfg().stringPropertyNames()) {
                    stringBuilder.append(key + ": " + data.getCfgValue(key) + "\n");
                }
                lobbyData = stringBuilder.toString();
                break;
            case Main:
                mainData = "";
                stringBuilder = new StringBuilder(mainData);
                stringBuilder.append("      ### MAIN MENU ###     " + "\n");
                mainData = stringBuilder.toString();
                break;
            case Summary:
                summaryData = "";
                stringBuilder = new StringBuilder(summaryData);
                stringBuilder.append("      ### SUMMARY ###     " + "\n");
                summaryData = stringBuilder.toString();
                break;
            case Settings:
                settingsData = "";
                stringBuilder = new StringBuilder(settingsData);
                stringBuilder.append("      ### SETTINGS ###     " + "\n");
                settingsData = stringBuilder.toString();
                break;
            case Game:
                gameData = "";
                stringBuilder = new StringBuilder(gameData);
                stringBuilder.append("         ### GAME ###        " + "\n");
                gameData = stringBuilder.toString();
                break;
            case Archive:
                archiveData = "";
                stringBuilder = new StringBuilder(archiveData);
                stringBuilder.append("        ### ARCHIVE ###       " + "\n");
                archiveData = stringBuilder.toString();
                break;
        }
    }
    
}
