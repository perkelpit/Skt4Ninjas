package com.company.skt.model;

import com.company.skt.controller.Calc;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Game {
    
    public static final int GRAND = 24;
    public static final int CLUBS = 12;
    public static final int SPADES = 11;
    public static final int HEARTS = 10;
    public static final int DIAMONDS = 9;
    public static final int NULL = 0;
    public static final int JUNK = 1;
    public static final int HAND = 0;
    public static final int SCHNEIDER = 1;
    public static final int SCHWARZ = 2;
    public static final int OVERT = 3;
    public static final int CALLED_SCHNEIDER = 4;
    public static final int CALLED_SCHWARZ = 5;
    
    public HashMap<Player, PlayerData> playerMap;
    Properties gameCfg;
    public Properties storedGameCfg;
    public Boolean won;
    public boolean[] modifier;
    int gameType;
    int result;
    
    public Game(Player player0, Player player1, Player player2) {
        playerMap.put(player0, new PlayerData());
        playerMap.get(player0).order = 0;
        playerMap.put(player1, new PlayerData());
        playerMap.get(player1).order = 1;
        playerMap.put(player2, new PlayerData());
        playerMap.get(player2).order = 2;
        
        gameCfg = Settings.getProperties(Settings.GAME);
        
        won = null;
        modifier = new boolean[6];
        for (int i = 0; i < 6; i++) {
            modifier[i] = false;
        }
        gameType = -1;
        result = -1;
        storedGameCfg = new Properties();
        // TODO funtioniert das wirklich? Echter deep clone?
        storedGameCfg.putAll(gameCfg);
    }
    
    public void setGameType(int gameType) {
        this.gameType = gameType;
    }
    
    public int getGameType() {
        return gameType;
    }
    
    public void setModifier(int modType, boolean b) {
        this.modifier[modType] = b;
    }
    
    public void setPlaying(Player playing) {
        playerMap.get(playing).plays = true;
    }
    
    public Player getPlaying() {
        Player p = null;
        for(Map.Entry<Player, PlayerData> entry : playerMap.entrySet()) {
            if (entry.getValue().plays) {
                p = entry.getKey();
            }
        }
        return p;
    }
    
    public TrickStack getTrickStack(Player player) {
        return playerMap.get(player).trickStack;
    }
    
    public Player[] getPlayers() {
        return playerMap.keySet().toArray(new Player[3]);
    }
    
    public Properties getStoredCfg() {
        return storedGameCfg;
    }
    
    private boolean isFinished() {
        boolean dataIsComplete = true;
        if (gameType == -1) {
            dataIsComplete = false;
        } else {
            if (gameType != JUNK) {
                boolean onePlays = false;
                for(Map.Entry<Player, PlayerData> entry : playerMap.entrySet()) {
                    if(entry.getValue().plays) {
                        onePlays = true;
                        break; // TODO correct?
                    }
                }
                if (!onePlays) {
                    dataIsComplete = false;
                }
            }
            for(Map.Entry<Player, PlayerData> entry : playerMap.entrySet()) {
                if(!entry.getValue().trickStack.isClosed()
                   || entry.getValue().startHand.isEmpty()
                   || !entry.getValue().currentHand.isEmpty()
                   || entry.getValue().order == -1) {
                    dataIsComplete = false;
                    break; // TODO correct?
                }
            }
            if (won == null) {
                dataIsComplete = false;
            }
        }
        return dataIsComplete;
    }
    
    public int getResult() {
        if (result == -1 && isFinished()) {
            result = Calc.getGameValue(this);
        }
        return result;
    }
    
}
