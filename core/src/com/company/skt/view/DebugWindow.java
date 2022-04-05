package com.company.skt.view;

import com.company.skt.model.SessionData;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class DebugWindow extends JFrame {

    private static DebugWindow debugWindow;
    private static JTextArea textAreaTop;
    private static JTextArea textAreaBottom;
    private static String lobbyData;

    public static void showTextAreaView() {
        if (debugWindow == null) {
            debugWindow = new DebugWindow();
        }
        debugWindow.setVisible(true);
    }

    public static void hideDebugWindow() {
        if (debugWindow != null) {
            debugWindow.setVisible(false);
        }
    }

    public static void disposeTextAreaView() {
        if (debugWindow != null) {
            debugWindow.dispose();
        }
    }

    public DebugWindow() {
        Dimension dimension = new Dimension(420, 750);
        setSize(dimension);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        getContentPane().setSize(dimension);
        getContentPane().setPreferredSize(dimension);
        getContentPane().setMinimumSize(dimension);
        getContentPane().setLayout(new FlowLayout());
        dimension = new Dimension(400, 400);
        textAreaTop = new JTextArea();
        textAreaTop.setSize(dimension);
        textAreaTop.setMinimumSize(dimension);
        textAreaTop.setPreferredSize(dimension);
        textAreaTop.setEditable(false);
        textAreaTop.setFont(new Font("Arial", Font.PLAIN, 20));
        textAreaTop.setEditable(false);
        textAreaTop.setBackground(new Color(43, 49, 47));
        textAreaTop.setForeground(new Color(95, 201, 197));
        textAreaTop.setWrapStyleWord(true);
        textAreaTop.setLineWrap(true);
        JScrollPane scrollPaneTop = new JScrollPane(textAreaTop);
        scrollPaneTop.setPreferredSize(dimension);
        ((DefaultCaret) textAreaTop.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        add(scrollPaneTop);
        dimension = new Dimension(400, 300);
        textAreaBottom = new JTextArea();
        //textAreaBottom.setSize(dimension);
        textAreaBottom.setMinimumSize(dimension);
        //textAreaBottom.setPreferredSize(dimension);
        textAreaBottom.setEditable(false);
        textAreaBottom.setFont(new Font("Arial", Font.PLAIN, 20));
        textAreaBottom.setEditable(false);
        textAreaBottom.setBackground(new Color(43, 49, 47));
        textAreaBottom.setForeground(new Color(95, 201, 197));
        textAreaBottom.setWrapStyleWord(true);
        textAreaBottom.setLineWrap(true);
        JScrollPane scrollPaneBottom = new JScrollPane(textAreaBottom);
        scrollPaneBottom.setPreferredSize(dimension);
        ((DefaultCaret) textAreaBottom.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        add(scrollPaneBottom);
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
        for (String key : data.getSessionCfg().stringPropertyNames()) {
            lobbyData += key + ": " + data.getCfgValue(key) + "\n";
        }
        updateTopDebugArea();
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

    private static void updateTopDebugArea() {
        if (textAreaTop != null) {
            textAreaTop.setText(lobbyData);
        }
    }
}
