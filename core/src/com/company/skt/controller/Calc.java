package com.company.skt.controller;

import com.company.skt.model.Card;
import com.company.skt.model.Game;
import com.company.skt.model.Hand;

public abstract class Calc {
    
    public static int getGameValue(Game game) {
        return calcValue(
            game.playerMap.get(game.getPlaying()).startHand, game.getGameType(),
            game.modifier[Game.HAND], game.modifier[Game.OVERT],
            game.modifier[Game.SCHNEIDER], game.modifier[Game.SCHWARZ],
            game.modifier[Game.CALLED_SCHNEIDER], game.modifier[Game.CALLED_SCHWARZ],
            game.won, Integer.parseInt(game.storedGameCfg.getProperty("lost_factor"))
                        );
    }
    
    public static int getBidValue(
        Hand hand, int gameType,
        boolean bHand, boolean bOvert,
        boolean bSchneider, boolean bSchwarz
                                 ) {
        return calcValue(
            hand, gameType, bHand, bOvert,
            bSchneider, bSchwarz,
            null, null, null, null
                        );
    }
    
    private static int calcValue (
        Hand hand, int gameType,
        boolean bHand, boolean bOvert,
        boolean bSchneider, boolean bSchwarz,
        Boolean calledSchneider, Boolean calledSchwarz,
        Boolean won, Integer lostFactor
                                 ) {
        
        int value = 0;
        if (gameType == Game.NULL) {
            if (bHand || bOvert) {
                if (bHand && !bOvert) {
                    value = 35;
                }
                if (!bHand && bOvert) {
                    value = 46;
                }
                if (bHand && bOvert) {
                    value = 59;
                }
            } else {
                value = 23;
            }
        } else {
            int factor = 2;
            boolean[] jacks = new boolean[4];
            for (Card card: hand) {
                if(card.getCardID() == Card.JACK) {
                    jacks[card.getSuitID()] = true;
                }
            }
            if (jacks[0]) {
                if (jacks[1]) {
                    ++factor;
                    if (jacks[2]) {
                        ++factor;
                        if (jacks[3]) {
                            ++factor;
                        }
                    }
                }
            } else /* if (!jacks[0]) */{
                if (!jacks[1]) {
                    ++factor;
                    if (!jacks[2]) {
                        ++factor;
                        if (!jacks[3]) {
                            ++factor;
                        }
                    }
                }
            }
            if(bHand) {
                ++factor;
                if(bSchneider) {
                    ++factor;
                    if(bSchwarz) {
                        ++factor;
                        if(bOvert) {
                            ++factor;
                            // TODO Welchen der 4 oder alle als check?
                            if(calledSchneider != null && calledSchwarz != null) {
                                if (calledSchneider) {
                                    ++factor;
                                    if (calledSchwarz) {
                                        ++factor;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            /* TODO Grundwert bei Grand Overt irgendwie anders (36?)
             * und automatisch. mit Schn./Schw.-Ansage */
            value = gameType * factor;
        }
        // TODO Welchen der 4 oder alle als check?
        if(calledSchneider != null && calledSchwarz != null) {
            if(!won) {
                value = value * lostFactor;
            }
        }
        return value;
    }
    
}