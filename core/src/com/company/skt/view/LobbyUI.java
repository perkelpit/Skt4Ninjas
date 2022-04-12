package com.company.skt.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.company.skt.controller.Menu;
import com.company.skt.controller.Session;
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
    public SelectBox<String> numberOfGamesSelectBox, timeLimitSelectBox, lostFactorSelectBox;
    private Label numberOfGamesLabel, timeLimitLabel, lostFactorLabel;
    public CheckBox junkCheckbox;

    // *** BOTTOM LEFT TABLE ***
    private Table bottomLeftTable;
    private TextField chatField;
    private TextArea chatArea;

    // *** BOTTOM RIGHT TABLE ***
    private Table bottomRightTable;
    private ImageTextButton readyButton, quitButton;


    {
        appCfg = Settings.getProperties(Settings.APP);

        buttonFont = Fonts.getFont("PirataOne-Regular_Button");
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
                new Label.LabelStyle(Fonts.getFont("PirataOne-Regular_Button"), null));
        topLeftTable.add(player0Name);
        topLeftTable.row();

        // *** PLAYER1 ***
        player1Name = new Label(Local.getString("lb_player_null"),
                new Label.LabelStyle(Fonts.getFont("PirataOne-Regular_Button"), null));
        topLeftTable.add(player1Name).align(Align.left);

        kickPlayer1Button = new ImageTextButton(
                Local.getString("lb_kick"), new ImageTextButton.ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, null, Fonts.getFont("PirataOne-Regular_Button")));
        kickPlayer1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Menu) Utils.getCurrentScreen()).buttonClicked("KICK_PLAYER1");
            }
        });
        topLeftTable.add(kickPlayer1Button);
        topLeftTable.row();

        // *** PLAYER2 ***
        player2Name = new Label(Local.getString("lb_player_null"),
                new Label.LabelStyle(Fonts.getFont("PirataOne-Regular_Button"), null));
        topLeftTable.add(player2Name).align(Align.left);
        kickPlayer2Button = new ImageTextButton(
                Local.getString("lb_kick"), new ImageTextButton.ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, null, Fonts.getFont("PirataOne-Regular_Button")));
        kickPlayer2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Menu) Utils.getCurrentScreen()).buttonClicked("KICK_PLAYER2");
            }
        });
        topLeftTable.add(kickPlayer2Button);
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
                new Label.LabelStyle(Fonts.getFont("PirataOne-Regular_Button"), null));
        topRightTable.add(numberOfGamesLabel).padRight(10 * scaleX).align(Align.left);

        numberOfGamesSelectBox = new SelectBox<>(new SelectBox.SelectBoxStyle(buttonFont, Color.WHITE,
                null, new ScrollPane.ScrollPaneStyle(), new List.ListStyle(buttonFont, Color.ORANGE,
                Color.WHITE, new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png")))));
        numberOfGamesSelectBox.setItems("1", "3", "6", "9", "18", "36");
        numberOfGamesSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((Menu) Utils.getCurrentScreen()).buttonClicked("GAME_SETTINGS_CHANGED");
            }
        });

        topRightTable.add(numberOfGamesSelectBox).align(Align.left);
        topRightTable.row();

        lostFactorLabel = new Label(Local.getString("lb_lost_factor") + ": ",
                new Label.LabelStyle(Fonts.getFont("PirataOne-Regular_Button"), null));
        topRightTable.add(lostFactorLabel).padRight(10 * scaleX).align(Align.left);

        lostFactorSelectBox = new SelectBox<String>(new SelectBox.SelectBoxStyle(buttonFont, Color.WHITE,
                null, new ScrollPane.ScrollPaneStyle(), new List.ListStyle(buttonFont, Color.ORANGE,
                Color.WHITE, new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png")))));
        lostFactorSelectBox.setItems("-1", "-2", "-3", "-4");
        lostFactorSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((Menu) Utils.getCurrentScreen()).buttonClicked("GAME_SETTINGS_CHANGED");
            }
        });
        topRightTable.add(lostFactorSelectBox).align(Align.left);
        topRightTable.row();

        timeLimitLabel = new Label(Local.getString("time-limit") + ": ",
                new Label.LabelStyle(Fonts.getFont("PirataOne-Regular_Button"), null));
        topRightTable.add(timeLimitLabel).align(Align.left);

        timeLimitSelectBox = new SelectBox<String>(new SelectBox.SelectBoxStyle(buttonFont, Color.WHITE,
                null, new ScrollPane.ScrollPaneStyle(), new List.ListStyle(buttonFont, Color.ORANGE,
                Color.WHITE, new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png")))));
        timeLimitSelectBox.setItems(Local.getString("lb_tl_opt_no-tl"), "30sec", "1 min.", "2 min.", "3 min.", "5 min.", "10 min.");
        timeLimitSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((Menu) Utils.getCurrentScreen()).buttonClicked("GAME_SETTINGS_CHANGED");
            }
        });
        topRightTable.add(timeLimitSelectBox).align(Align.left);
        topRightTable.row();

        junkCheckbox = new CheckBox(Local.getString("lb_include_junk"),
                new CheckBox.CheckBoxStyle(
                        new TextureRegionDrawable(Assets.<Texture>get("CheckTexture.png")),
                                new TextureRegionDrawable(Assets.<Texture>get("CheckTextureActive.png")),
                                        Fonts.getFont("PirataOne-Regular_Button"),
                                        null));
        junkCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((Menu) Utils.getCurrentScreen()).buttonClicked("GAME_SETTINGS_CHANGED");
            }
        });
        topRightTable.add(junkCheckbox).align(Align.left);
        topRightTable.row();

        addActor(topRightTable);

        // ****** BOTTOM LEFT TABLE ******
        bottomLeftTable = new Table();
        bottomLeftTable.setWidth(Float.parseFloat(appCfg.getProperty("resolution_x")) / 2f);
        bottomLeftTable.setHeight(Float.parseFloat(appCfg.getProperty("resolution_y")) / 2f);
        bottomLeftTable.setPosition(0, 0, Align.bottomLeft);


        addActor(bottomLeftTable);

        // ****** BOTTOM RIGHT TABLE ******
        bottomRightTable = new Table();
        bottomRightTable.setWidth(Float.parseFloat(appCfg.getProperty("resolution_x")) / 2f);
        bottomRightTable.setHeight(Float.parseFloat(appCfg.getProperty("resolution_y")) / 2f);
        bottomRightTable.setPosition(
                Float.parseFloat(appCfg.getProperty("resolution_x")) - bottomRightTable.getWidth(),
                0, Align.bottomLeft);

        // *** READY BUTTON ***
        readyButton = new ImageTextButton(
                Local.getString("lb_ready"), new ImageTextButton.ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, buttonCheckedDrawable, Fonts.getFont("PirataOne-Regular_Button")));
        readyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Menu) Utils.getCurrentScreen()).buttonClicked("READY");
            }
        });
        bottomRightTable.add(readyButton);
        bottomRightTable.row();


        // *** QUIT BUTTON ***
        quitButton = new ImageTextButton(
                Local.getString("quit"), new ImageTextButton.ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, null, Fonts.getFont("PirataOne-Regular_Button")));
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Menu) Utils.getCurrentScreen()).buttonClicked("QUIT_LOBBY");
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
        numberOfGamesSelectBox.setSelected(data.getCfgValue("amount_games"));
        timeLimitSelectBox.setSelected(data.getCfgValue("time_limit"));
        lostFactorSelectBox.setSelected(data.getCfgValue("lost_factor"));
        junkCheckbox.setChecked(Boolean.parseBoolean(data.getCfgValue("ramsch")));
    }
}
