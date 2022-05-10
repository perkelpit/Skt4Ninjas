package com.company.skt.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.company.skt.controller.Utils;
import com.company.skt.lib.UpdateStage;
import com.company.skt.model.*;

public class SettingsUI extends UpdateStage {

    // *** Ressources ***
    private TextureRegionDrawable buttonDrawable, buttonPressedDrawable;
    private Float scaleX, scaleY;
    private BitmapFont buttonFont;

    // *** tabTable ***
    private Table tabTable;

    private ImageTextButton gameTabButton, videoTabButton, audioTabButton, quitSettingsButton;

    // *** gameTable ***
    private Table gameTable;

    private Label changeNameLabel, numberOfGamesLabel, lostFactorLabel, timeLimitLabel;
    public TextField changeNameTextField;
    private ImageTextButton changeNameButton;
    public SelectBox<String> amountGamesSelectBox, timeLimitSelectBox, lostFactorSelectBox;
    public CheckBox junkCheckbox;

    // *** videoTable ***

    private Table videoTable;

    // *** audioTable ***

    private Table audioTable;

    {
        tabTable = new Table();
        gameTable = new Table();
        videoTable = new Table();
        videoTable.setVisible(false);
        audioTable = new Table();
        audioTable.setVisible(false);

        
        buttonDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        buttonPressedDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexturePressed.png"));
        buttonFont = Fonts.get("button");

        scaleX = Utils.getScaleFactorX();
        scaleY = Utils.getScaleFactorY();
    }

    public SettingsUI(String name) {
        super(name);
    }
    
    public SettingsUI(String name, boolean active) {
        super(name, active);
    }

    @Override
    public void initialize() {
        super.initialize();
        
        // ### TAB MENU ###
        tabTable.setSize(300 * scaleX, 900 * scaleY);
        tabTable.setPosition(110 * scaleX, 90 * scaleY, Align.bottomLeft);
        tabTable.align(Align.top);

        // *** GAME TAB BUTTON ***
        gameTabButton = new ImageTextButton(
            Local.get("sm_tab_game"), new ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, null, Fonts.get("button")
        ));
        gameTabButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y){
               Utils.getCurrentScreen().click("TAB_GAME");
           }
        });
        tabTable.add(gameTabButton);
        tabTable.row();

        // *** VIDEO TAB BUTTON ***
       videoTabButton = new ImageTextButton(
           Local.get("sm_tab_video"), new ImageTextButtonStyle(
               buttonDrawable, buttonPressedDrawable, null, Fonts.get("button")
        ));
        videoTabButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                Utils.getCurrentScreen().click("TAB_VIDEO");
            }
        });
        tabTable.add(videoTabButton);
        tabTable.row();

        // *** AUDIO TAB BUTTON ***
        audioTabButton = new ImageTextButton(
            Local.get("sm_tab_audio"), new ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, null, Fonts.get("button")
        ));
        audioTabButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                Utils.getCurrentScreen().click("TAB_AUDIO");
            }
        });
        tabTable.add(audioTabButton);
        tabTable.row();

        // *** QUIT SETTINGS TAB BUTTON ***
        quitSettingsButton = new ImageTextButton(
            Local.get("sm_quit"), new ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, null, Fonts.get("button")
        ));
        quitSettingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                Utils.getCurrentScreen().click("QUIT_SETTINGS");
            }
        });
        tabTable.add(quitSettingsButton);
        tabTable.row();

        addActor(tabTable);


        // #### GAME TAB ####
        gameTable.setSize(1300 * scaleX, 900 * scaleY);
        gameTable.setPosition(530 * scaleX, 90 * scaleY, Align.bottomLeft);
        gameTable.align(Align.top);

        // *** CHANGE NAME ***
        changeNameLabel = new Label(
                Local.get("sm_game_label_change_name"),
                new Label.LabelStyle(Fonts.get("button"), null));
        gameTable.add(changeNameLabel);

        changeNameTextField = new TextField(Settings.getProperties(Settings.APP).getProperty("player_name"),
                Styles.newTextFieldStyle());
        gameTable.add(changeNameTextField);

        changeNameButton = new ImageTextButton(
            Local.get("sm_game_button_change_name"), new ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, null, Fonts.get("button")
        ));
        changeNameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                Utils.getCurrentScreen().click("CHANGE_NAME");
            }
        });
        gameTable.add(changeNameButton);
        gameTable.row();

        // *** NUMBER OF GAMES ***
        numberOfGamesLabel = new Label(Local.get("lb_no-of-games") + ": ",
                new Label.LabelStyle(Fonts.get("medLable"), null));
        gameTable.add(numberOfGamesLabel).padRight(10 * scaleX).align(Align.left);

        amountGamesSelectBox = new SelectBox<>(new SelectBox.SelectBoxStyle(buttonFont, Color.WHITE,
                null, new ScrollPane.ScrollPaneStyle(), new List.ListStyle(buttonFont, Color.ORANGE,
                Color.WHITE, new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png")))));
        amountGamesSelectBox.setItems("1", "3", "6", "9", "18", "36");
        amountGamesSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Utils.getCurrentScreen().buttonClicked("GAME_SETTINGS_CLICKED#AMOUNT_GAMES");
            }
        });
        gameTable.add(amountGamesSelectBox).align(Align.left);
        gameTable.row();

        // *** LOSTFACTOR ***
        lostFactorLabel = new Label(Local.get("lb_lost_factor") + ": ",
                new Label.LabelStyle(Fonts.get("medLable"), null));
        gameTable.add(lostFactorLabel).padRight(10 * scaleX).align(Align.left);

        lostFactorSelectBox = new SelectBox<>(new SelectBox.SelectBoxStyle(buttonFont, Color.WHITE,
                null, new ScrollPane.ScrollPaneStyle(), new List.ListStyle(buttonFont, Color.ORANGE,
                Color.WHITE, new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png")))));
        lostFactorSelectBox.setItems("-1", "-2", "-3", "-4");
        lostFactorSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Utils.getCurrentScreen().buttonClicked("GAME_SETTINGS_CLICKED#LOST_FACTOR");
            }
        });
        gameTable.add(lostFactorSelectBox).align(Align.left);
        gameTable.row();

        // *** TIMELIMIT ***
        timeLimitLabel = new Label(Local.get("time-limit") + ": ",
                new Label.LabelStyle(Fonts.get("medLable"), null));
        gameTable.add(timeLimitLabel).align(Align.left);

        timeLimitSelectBox = new SelectBox<>(new SelectBox.SelectBoxStyle(buttonFont, Color.WHITE,
                null, new ScrollPane.ScrollPaneStyle(), new List.ListStyle(buttonFont, Color.ORANGE,
                Color.WHITE, new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png")))));
        timeLimitSelectBox.setItems(
                Local.get("lb_tl_opt_no-tl"),
                "30" + Local.get("abr_second"),
                "1" + Local.get("abr_minute"),
                "2" + Local.get("abr_minute"),
                "3" + Local.get("abr_minute"),
                "5" + Local.get("abr_minute"),
                "10" + Local.get("abr_minute"));
        timeLimitSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Utils.getCurrentScreen().buttonClicked("GAME_SETTINGS_CLICKED#TIME_LIMIT");
            }
        });
        gameTable.add(timeLimitSelectBox).align(Align.left);
        gameTable.row();

        // *** RAMSCH ***
        junkCheckbox = new CheckBox(Local.get("lb_include_junk"),
                new CheckBox.CheckBoxStyle(
                        new TextureRegionDrawable(Assets.<Texture>get("CheckTexture.png")),
                        new TextureRegionDrawable(Assets.<Texture>get("CheckTextureActive.png")),
                        Fonts.get("medLable"),
                        null));
        junkCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Utils.getCurrentScreen().buttonClicked("GAME_SETTINGS_CLICKED#RAMSCH");
            }
        });
        gameTable.add(junkCheckbox).align(Align.left);




        addActor(gameTable);
    }
}
