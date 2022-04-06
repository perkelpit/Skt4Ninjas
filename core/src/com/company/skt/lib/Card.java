package com.company.skt.lib;

public class Card {
    
    public static final int CLUBS = 0;
    public static final int SPADES = 1;
    public static final int HEARTS = 2;
    public static final int DIAMONDS = 3;
    
    public static final int JACK = 0;
    public static final int ACE = 1;
    public static final int TEN = 2;
    public static final int KING = 3;
    public static final int QUEEN = 4;
    public static final int NINE = 5;
    public static final int EIGHT = 6;
    public static final int SEVEN = 7;
    
    private final int suitID;
    private final int cardID;
    private final int cardScore;
    private int cardRank;
    private boolean trump;
    private boolean layable;
    private boolean isLed;
    
    Card (int suitID, int cardID){
        this.suitID = suitID;
        this.cardID = cardID;
        switch (cardID) {
            case JACK: cardScore = 2; break;
            case ACE: cardScore = 11; break;
            case TEN: cardScore = 10; break;
            case KING: cardScore = 4; break;
            case QUEEN: cardScore = 3; break;
            default: cardScore = 0;
        }
        setCardRank(Game.GRAND);
        trump = false;
        layable = false;
        isLed = false;
    }
    
    public void setCardRank(int gameType) {
        if(gameType == Game.NULL || gameType == Game.JUNK) {
            switch (cardID) {
                case QUEEN: cardRank = 2; break;
                case KING: cardRank = 1; break;
                case TEN: cardRank = 4; break;
                case ACE: cardRank = 0; break;
                case JACK: cardRank = 3; break;
                default: cardRank = cardID;
            }
        } else {
            cardRank = cardID;
        }
    }
    public int getCardRank() {
        return cardRank;
    }
    
    public void setTrump(int gameType) {
        switch (gameType) {
            case Game.CLUBS:
            case Game.SPADES:
            case Game.HEARTS:
            case Game.DIAMONDS:
                if(cardID == JACK) {
                    trump = true;
                } else {
                    if(suitID == gameType) {
                        trump = true;
                    }
                }
                break;
            case Game.GRAND:
                if(cardID == JACK) {
                    trump = true;
                }
                break;
            case Game.NULL:
            case Game.JUNK:
                break;
        }
    }
    
    public boolean isTrump() {
        return trump;
    }
    
    public void setLayable(boolean b) {
        if (b) layable = true;
        if (!b) layable = false;
    }
    public boolean isLayable() {
        return layable;
    }
    
    public void setLed(boolean b) {
        if (b) isLed = true;
        if (!b) isLed = false;
    }
    public boolean getLed() {
        if (isLed) return true;
        return false;
    }
    
    public int getSuitID() {
        return suitID;
    }
    public int getCardID() {
        return cardID;
    }
    public int getCardScore() {
        return cardScore;
    }
}
