package com.company.skt.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.company.skt.controller.Utils;
import com.company.skt.lib.UpdateStage;
import com.company.skt.model.*;

import java.util.Properties;

public class LobbyUI extends UpdateStage {

    // *** RESOURCES ***
    private TextureRegionDrawable buttonDrawable, buttonPressedDrawable, buttonCheckedDrawable;
    private BitmapFont buttonFont;
    private Properties appCfg;

    // *** TOP LEFT TABLE ***
    private Table topLeftTable;
    private Label player0Name, player1Name, player2Name;
    private ImageTextButton kickPlayer1Button, kickPlayer2Button;
    private Float scaleX, scaleY;

    // *** TOP RIGHT TABLE ***
    private Table topRightTable;
    public SelectBox<String> amountGamesSelectBox, timeLimitSelectBox, lostFactorSelectBox;
    private Label numberOfGamesLabel, timeLimitLabel, lostFactorLabel;
    public CheckBox junkCheckbox;

    // *** BOTTOM LEFT TABLE ***
    private Table bottomLeftTable;
    private TextField chatField;
    private TextArea chatArea;

    // *** BOTTOM RIGHT TABLE ***
    private Table bottomRightTable;
    private ImageTextButton readyStartButton, quitButton;


    {
        appCfg = Settings.getProperties(Settings.APP);

        buttonFont = Fonts.get("button");
        buttonDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        buttonPressedDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexturePressed.png"));
        buttonCheckedDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTextureChecked.png"));

        scaleX = Utils.getScaleFactorX();
        scaleY = Utils.getScaleFactorY();
    }

    public LobbyUI(String name) {
        super(name);
        build();
    }

    public LobbyUI(String name, boolean active) {
        super(name, active);
        build();
    }

    private void build() {

        // ****** TOP LEFT TABLE ******
        topLeftTable = new Table().padTop(50 * scaleY);
        topLeftTable.setWidth((Float.parseFloat(appCfg.getProperty("resolution_x")) / 2f));
        topLeftTable.setHeight((Float.parseFloat(appCfg.getProperty("resolution_y")) / 2f));
        topLeftTable.setPosition(0,
                (Float.parseFloat(appCfg.getProperty("resolution_y"))) - topLeftTable.getHeight(),
                Align.bottomLeft);

        // **** PLAYER ****

        // *** PLAYER0 ***
        player0Name = new Label(Local.getString("lb_player_null"),
                new Label.LabelStyle(Fonts.get("medLable"), null));
        topLeftTable.add(player0Name);
        topLeftTable.row();

        // *** PLAYER1 ***
        player1Name = new Label(Local.getString("lb_player_null"),
                new Label.LabelStyle(Fonts.get("medLable"), null));
        topLeftTable.add(player1Name).align(Align.left);
    
        if(SessionData.isHost()) {
            kickPlayer1Button = new ImageTextButton(
                Local.getString("lb_kick"), new ImageTextButton.ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, null, Fonts.get("button")));
            kickPlayer1Button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y){
                    Utils.getCurrentScreen().buttonClicked("KICK_PLAYER#1");
                }
            });
            topLeftTable.add(kickPlayer1Button);
        }
        topLeftTable.row();

        // *** PLAYER2 ***
        player2Name = new Label(Local.getString("lb_player_null"),
                new Label.LabelStyle(Fonts.get("medLable"), null));
        topLeftTable.add(player2Name).align(Align.left);
        if(SessionData.isHost()) {
            kickPlayer2Button = new ImageTextButton(
                Local.getString("lb_kick"), new ImageTextButton.ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, null, Fonts.get("button")));
            kickPlayer2Button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y){
                    Utils.getCurrentScreen().buttonClicked("KICK_PLAYER#2");
                }
            });
            topLeftTable.add(kickPlayer2Button);
        }
        topLeftTable.row();



        addActor(topLeftTable);

        // ****** TOP RIGHT TABLE ******
        topRightTable = new Table().padTop(50 * scaleY);
        topRightTable.setWidth((Float.parseFloat(appCfg.getProperty("resolution_x")) / 2f));
        topRightTable.setHeight((Float.parseFloat(appCfg.getProperty("resolution_y")) / 2f));
        topRightTable.setPosition(
                Float.parseFloat(appCfg.getProperty("resolution_x")) - topRightTable.getWidth(),
                Float.parseFloat(appCfg.getProperty("resolution_y")) - topRightTable.getHeight(),
                Align.bottomLeft);

        // *** GAME SETTINGS ***
        numberOfGamesLabel = new Label(Local.getString("lb_no-of-games") + ": ",
                new Label.LabelStyle(Fonts.get("medLable"), null));
        topRightTable.add(numberOfGamesLabel).padRight(10 * scaleX).align(Align.left);

        amountGamesSelectBox = new SelectBox<>(new SelectBox.SelectBoxStyle(buttonFont, Color.WHITE,
                null, new ScrollPane.ScrollPaneStyle(), new List.ListStyle(buttonFont, Color.ORANGE,
                Color.WHITE, new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png")))));
        amountGamesSelectBox.setItems("1", "3", "6", "9", "18", "36");
        amountGamesSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                /* // TODO works too good - find better way
                if(amountGamesSelectBox.hit(Gdx.input.getX(), Gdx.input.getY(), true) != null) {

                }*/
                Utils.getCurrentScreen().buttonClicked("LOBBY_SETTINGS_CLICKED#AMOUNT_GAMES");
            }
        });
        if(!SessionData.isHost()) {
            amountGamesSelectBox.setDisabled(true);
        }
        topRightTable.add(amountGamesSelectBox).align(Align.left);
        topRightTable.row();

        lostFactorLabel = new Label(Local.getString("lb_lost_factor") + ": ",
                new Label.LabelStyle(Fonts.get("medLable"), null));
        topRightTable.add(lostFactorLabel).padRight(10 * scaleX).align(Align.left);

        lostFactorSelectBox = new SelectBox<>(new SelectBox.SelectBoxStyle(buttonFont, Color.WHITE,
                null, new ScrollPane.ScrollPaneStyle(), new List.ListStyle(buttonFont, Color.ORANGE,
                Color.WHITE, new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png")))));
        lostFactorSelectBox.setItems("-1", "-2", "-3", "-4");
        lostFactorSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                /* // TODO works too good - find better way
                if(lostFactorSelectBox.hit(Gdx.input.getX(), Gdx.input.getY(), true) != null) {

                }*/
                Utils.getCurrentScreen().buttonClicked("LOBBY_SETTINGS_CLICKED#LOST_FACTOR");
            }
        });
        if(!SessionData.isHost()) {
            lostFactorSelectBox.setDisabled(true);
        }
        topRightTable.add(lostFactorSelectBox).align(Align.left);
        topRightTable.row();

        timeLimitLabel = new Label(Local.getString("time-limit") + ": ",
                new Label.LabelStyle(Fonts.get("medLable"), null));
        topRightTable.add(timeLimitLabel).align(Align.left);

        timeLimitSelectBox = new SelectBox<>(new SelectBox.SelectBoxStyle(buttonFont, Color.WHITE,
                null, new ScrollPane.ScrollPaneStyle(), new List.ListStyle(buttonFont, Color.ORANGE,
                Color.WHITE, new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png")))));
        timeLimitSelectBox.setItems(
                Local.getString("lb_tl_opt_no-tl"),
                "30" + Local.getString("abr_second"),
                "1" + Local.getString("abr_minute"),
                "2" + Local.getString("abr_minute"),
                "3" + Local.getString("abr_minute"),
                "5" + Local.getString("abr_minute"),
                "10" + Local.getString("abr_minute"));
        timeLimitSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                /* // TODO works too good - find better way
                if(timeLimitSelectBox.hit(Gdx.input.getX(), Gdx.input.getY(), true) != null) {

                }*/
                Utils.getCurrentScreen().buttonClicked("LOBBY_SETTINGS_CLICKED#TIME_LIMIT");
            }
        });
        if(!SessionData.isHost()) {
            timeLimitSelectBox.setDisabled(true);
        }
        topRightTable.add(timeLimitSelectBox).align(Align.left);
        topRightTable.row();

        junkCheckbox = new CheckBox(Local.getString("lb_include_junk"),
                new CheckBox.CheckBoxStyle(
                        new TextureRegionDrawable(Assets.<Texture>get("CheckTexture.png")),
                                new TextureRegionDrawable(Assets.<Texture>get("CheckTextureActive.png")),
                                        Fonts.get("medLable"),
                                        null));
        junkCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Utils.getCurrentScreen().buttonClicked("LOBBY_SETTINGS_CLICKED#RAMSCH");
            }
        });
        if(!SessionData.isHost()) {
            junkCheckbox.setDisabled(true);
        }
        topRightTable.add(junkCheckbox).align(Align.left);
        topRightTable.row();
        addActor(topRightTable);

        // ****** BOTTOM LEFT TABLE ******
        bottomLeftTable = new Table();
        bottomLeftTable.setWidth(Float.parseFloat(appCfg.getProperty("resolution_x")) / 2f);
        bottomLeftTable.setHeight(Float.parseFloat(appCfg.getProperty("resolution_y")) / 2f);
        bottomLeftTable.setPosition(0, 0, Align.bottomLeft);

        chatArea = new TextArea(
                "testArea",
                new TextField.TextFieldStyle(
                        Fonts.get("textField"),
                        Color.WHITE,
                        null,
                        null,
                        new TextureRegionDrawable(Assets.<Texture>get("TextfieldTexture.png"))));
        chatArea.setDisabled(false);
        bottomLeftTable.add(chatArea);
        bottomLeftTable.row();

        chatField = new TextField("testField",
                new TextField.TextFieldStyle(
                        Fonts.get("textField"),
                        Color.WHITE,
                        null,
                        null,
                        new TextureRegionDrawable(Assets.<Texture>get("TextfieldTexture.png"))));

        bottomLeftTable.add(chatField);

        addActor(bottomLeftTable);


        // ****** BOTTOM RIGHT TABLE ******
        bottomRightTable = new Table();
        bottomRightTable.setWidth(Float.parseFloat(appCfg.getProperty("resolution_x")) / 2f);
        bottomRightTable.setHeight(Float.parseFloat(appCfg.getProperty("resolution_y")) / 2f);
        bottomRightTable.setPosition(
                Float.parseFloat(appCfg.getProperty("resolution_x")) - bottomRightTable.getWidth(),
                0, Align.bottomLeft);

        // *** READY BUTTON ***
        readyStartButton = new ImageTextButton(
            (SessionData.isHost() ? Local.getString("lb_start") : Local.getString("lb_ready")),
                new ImageTextButton.ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, buttonCheckedDrawable, Fonts.get("button")));
        readyStartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                Utils.getCurrentScreen().buttonClicked("READY");
            }
        });
        if(SessionData.isHost()) {
            readyStartButton.setDisabled(true);
            readyStartButton.setColor(Color.GRAY);
        } else {
            readyStartButton.setColor(Color.RED);
        }
        bottomRightTable.add(readyStartButton);
        bottomRightTable.row();


        // *** QUIT BUTTON ***
        quitButton = new ImageTextButton(
                Local.getString("quit"), new ImageTextButton.ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, null, Fonts.get("button")));
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                Utils.getCurrentScreen().buttonClicked("QUIT_LOBBY");
            }
        });
        bottomRightTable.add(quitButton).padTop(10f * scaleY);
        bottomRightTable.row();

        addActor(bottomRightTable);
    }

    @Override
    public void initialize() {
        super.initialize();
        updateUI();
    }

    public void updateUI() {
        SessionData data = SessionData.get();
        
        // *** READY/START BUTTON ***
        if(SessionData.isHost()) {
            if(data.getPlayer(1) != null && data.getPlayer(2) != null &&
               data.getPlayer(1).isReady() && data.getPlayer(2).isReady()) {
                readyStartButton.setDisabled(false);
                readyStartButton.setColor(Color.WHITE);
                readyStartButton.setTouchable(Touchable.enabled);
            } else {
                readyStartButton.setDisabled(true);
                readyStartButton.setColor(Color.GRAY);
                readyStartButton.setTouchable(Touchable.disabled);
            }
        } else {
            if(data.getPlayer(SessionData.getOwnPlayerNumber()) != null) {
                readyStartButton.setColor(data.getPlayer(SessionData.getOwnPlayerNumber()).isReady() ?
                                          Color.GREEN : Color.RED);
            }
        }
        
        // *** KICK BUTTONS ***
        if(kickPlayer1Button != null) {
            if(data.getPlayer(1) != null) {
                kickPlayer1Button.setDisabled(true);
                kickPlayer1Button.setVisible(true);
                kickPlayer1Button.setTouchable(Touchable.enabled);
            } else {
                kickPlayer1Button.setDisabled(false);
                kickPlayer1Button.setVisible(false);
                kickPlayer1Button.setTouchable(Touchable.disabled);
            }
        }
        if(kickPlayer2Button != null) {
            if(data.getPlayer(2) != null) {
                kickPlayer2Button.setDisabled(true);
                kickPlayer2Button.setVisible(true);
                kickPlayer2Button.setTouchable(Touchable.enabled);
            } else {
                kickPlayer2Button.setDisabled(false);
                kickPlayer2Button.setVisible(false);
                kickPlayer2Button.setTouchable(Touchable.disabled);
            }
        }
        
        // *** PLAYERLABELS ***
        if (data.getPlayer(0) != null) {
            player0Name.setText(data.getPlayer(0).getName());
        }
        else{
            player0Name.setText(Local.getString("lb_player_null"));
        }
        if (data.getPlayer(1) != null) {
            player1Name.setText(data.getPlayer(1).getName());
        }
        else{
            player1Name.setText(Local.getString("lb_player_null"));
        }
        if (data.getPlayer(2) != null) {
            player2Name.setText(data.getPlayer(2).getName());
        }
        else{
            player2Name.setText(Local.getString("lb_player_null"));
        }

        // *** SETTINGS ***
        amountGamesSelectBox.setSelected(data.getCfgValue("amount_games"));
        lostFactorSelectBox.setSelected(data.getCfgValue("lost_factor"));
        junkCheckbox.setChecked(Boolean.parseBoolean(data.getCfgValue("ramsch")));

        switch(data.getCfgValue("time_limit")) {
            case "0":
                timeLimitSelectBox.setSelectedIndex(0);
                break;
            case "30":
                timeLimitSelectBox.setSelectedIndex(1);
                break;
            case "60":
                timeLimitSelectBox.setSelectedIndex(2);
                break;
            case "120":
                timeLimitSelectBox.setSelectedIndex(3);
                break;
            case "180":
                timeLimitSelectBox.setSelectedIndex(4);
                break;
            case "300":
                timeLimitSelectBox.setSelectedIndex(5);
                break;
            case "600":
                timeLimitSelectBox.setSelectedIndex(6);
                break;
        }
    }
}
