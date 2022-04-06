package com.company.skt.model;

import com.badlogic.gdx.utils.Null;
import com.company.skt.controller.Menu;
import com.company.skt.controller.Utils;
import com.company.skt.lib.GameList;
import com.company.skt.lib.Player;

import java.util.Properties;

public class SessionData {
    
    private static SessionData data;
    private static boolean isHost;
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
    
    // TODO depricate this by getting this information automatically
    public static SessionData get(boolean isHost) {
        SessionData.isHost = isHost;
        return get();
    }
    
    public static void dispose() {
        data = null;
    }
    
    public static String getCfgString() {
        String cfgString = "CFG#";
        for(String key : (String[])data.getSessionCfg().keySet().toArray()) {
            cfgString += key + "=" + data.getCfgValue(key) + ";";
        }
        return cfgString;
    }
    
    public synchronized void setPlayer(@Null Player player, int playerNumber) {
        if((playerNumber >= 0) && (playerNumber < 3)) {
            switch(playerNumber) {
                case 0:
                    if(player0 != player) {
                        player0 = player;
                    } break;
                case 1:
                    if(player1 != player) {
                        player1 = player;
                    } break;
                case 2:
                    if(player2 != player) {
                        player2 = player;
                    } break;
            }
        }
        changed();
    }
    
    public Player getPlayer(int playerNumber) {
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
        if(isHost) {
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
                if(isHost) {
                    Settings.setProperty(Settings.GAME, keys[i], values[i]);
                }
                if(keys[i].equals("amount_games")) {
                    gameList.setMaxGames(Integer.parseInt(values[i]));
                }
            }
        }
        changed();
    }
    
    public String getCfgValue(String key) {
        return data.sessionCfg.getProperty(key);
    }
    
    public Properties getSessionCfg() {
        return sessionCfg;
    }
    
    public static boolean isHost() {
        return isHost;
    }
    
    private static void changed() {
        ((Menu)Utils.getCurrentScreen()).event("LOBBY_DATA_HAS_CHANGED");
    }
    
    public GameList getGameList() {
        return gameList;
    }
}
