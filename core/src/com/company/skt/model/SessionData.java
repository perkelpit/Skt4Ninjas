package com.company.skt.model;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Null;
import com.company.skt.controller.Menu;
import com.company.skt.controller.Play;
import com.company.skt.controller.Utils;
import com.company.skt.lib.EventClickHandler;
import com.company.skt.lib.GameList;
import com.company.skt.lib.Player;
import com.company.skt.lib.StageScreen;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.Properties;

/**
 * {@code SessionData} holds all those values needed for proper management and display of session-related
 * data. Some values and methods are only used by {@link com.company.skt.controller.HostSession HostSession}, some
 * only by {@link com.company.skt.controller.ClientSession ClientSession}.<br>
 * Additionally it provides methods to save and load a SessionData-object.
 * <p>
 * Note: always when a value is changed the event {@code SESSION_DATA_CHANGED} is called on the current
 * {@link StageScreen}. Therefore {@code StageScreen} implements {@link EventClickHandler} and all of it´s
 * subclasses need to handle the event properly.
 * </p>
 * */
public class SessionData implements Serializable {
    
    private static SessionData data;
    private static int ownPlayerNumber;
    
    static {
        ownPlayerNumber = -1;
    }
    
    private Player player0;
    private Player player1;
    private Player player2;
    private Properties sessionCfg;
    private GameList gameList;
    
    private SessionData() {
        sessionCfg = new Properties(Settings.getProperties(Settings.GAME));
        gameList = new GameList(Integer.parseInt(sessionCfg.getProperty("amount_games")));
    }
    
    public synchronized static SessionData get() {
        if(data == null) {
            data = new SessionData();
        }
        return data;
    }
    
    public synchronized static SessionData get(boolean isHost) {
        if(isHost) {
            setOwnPlayerNumber(0);
        }
        return get();
    }
    
    public synchronized static void dispose() {
        data = null;
        ownPlayerNumber = -1;
    }
    
    public synchronized static String getDataStringForClient() {
        StringBuilder cfgString = new StringBuilder("SDATA>");
        /* ### gameCfg ### */
        cfgString.append("CFG{"); // begin gameCfg
        Enumeration<?> keys = data.getSessionCfg().propertyNames();
        while(keys.hasMoreElements()) {
            String keyStr = keys.nextElement().toString();
            cfgString.append(keyStr).append("=").append(data.getCfgValue(keyStr)).append(";");
        }
        cfgString.append("}"); // end gameCfg
        /* ### players ### */
        cfgString.append("PLAYERS{"); // begin players
        for(int i = 0; i < 3; i++) {
            cfgString.append(i + "{"); // begin player(i)
            Player player = data.getPlayer(i);
            if(player != null) {
                cfgString.append(player.getName()).append(";");
                cfgString.append(player.isReady()).append(";");
                cfgString.append(player.getConnectivity()).append(";");
            } else {
                cfgString.append("null");
            }
            cfgString.append("}"); // end player(i)
        }
        cfgString.append("}"); // end players
        return cfgString.toString();
    }
    
    public synchronized void setPlayer(@Null Player player, int playerNumber) {
        if((playerNumber >= 0) && (playerNumber < 3)) {
            switch(playerNumber) {
                case 0:
                    player0 = player;
                    break;
                case 1:
                    player1 = player;
                    break;
                case 2:
                    player2 = player;
                    break;
            }
        }
        changed();
    }
    
    public synchronized Player getPlayer(int playerNumber) {
        switch(playerNumber) {
            case 0:
                return player0;
            case 1:
                return player1;
            case 2:
                return player2;
        }
        return null;
    }
    
    public synchronized void setPlayerReady(int playerNumber, boolean ready) {
        switch(playerNumber) {
            case 0:
                if(data.player0 != null) {
                    data.player0.setReady(ready);
                }
                break;
            case 1:
                if(data.player1 != null) {
                    data.player1.setReady(ready);
                }
                break;
            case 2:
                if(data.player2 != null) {
                    data.player2.setReady(ready);
                }
                break;
        }
        changed();
    }
    
    public synchronized void setCfgValue(String key, String value) {
        data.sessionCfg.setProperty(key, value);
        if(isHost()) {
            Settings.setProperty(Settings.GAME, key, value);
        }
        if(key.equals("amount_games")) {
            gameList.setMaxGames(Integer.parseInt(value));
        }
        changed();
    }
    
    public synchronized void setCfgValues(String[] keys, String[] values) {
        if(keys.length == values.length) {
            for(int i = 0; i < keys.length; i++) {
                data.sessionCfg.setProperty(keys[i], values[i]);
                if(isHost()) {
                    Settings.setProperty(Settings.GAME, keys[i], values[i]);
                }
                if(keys[i].equals("amount_games")) {
                    gameList.setMaxGames(Integer.parseInt(values[i]));
                }
            }
        }
        changed();
    }
    
    public synchronized String getCfgValue(String key) {
        return data.sessionCfg.getProperty(key);
    }
    
    public synchronized Properties getSessionCfg() {
        return sessionCfg;
    }
    
    public synchronized static boolean isHost() {
        return ownPlayerNumber == 0;
    }
    
    public synchronized GameList getGameList() {
        return gameList;
    }
    
    public static void changed() {
        EventClickHandler screen = Utils.getCurrentScreen();
        screen.event("SESSION_DATA_CHANGED");
    }
    
    public static void save(String path) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(data);
        } catch (IOException e) {e.printStackTrace();}
    }
    
    public static SessionData load(String path) {
        SessionData loadedData = null;
        if (new File(path).exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
                loadedData = (SessionData)in.readObject();
            } catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
        }
        return loadedData;
    }
    
    public static void setOwnPlayerNumber(int ownPlayerNumber) {
        SessionData.ownPlayerNumber = ownPlayerNumber;
    }
    
    public static int getOwnPlayerNumber() {
        return ownPlayerNumber;
    }
}
