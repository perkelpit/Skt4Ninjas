package com.company.skt.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.company.skt.controller.Menu;
import com.company.skt.lib.UpdateStage;
import com.company.skt.model.*;

public class SettingsUI extends UpdateStage {
    
    Table tabTable, videoTable, audioTable, gameTable;
    ImageTextButton
            gameTabButton, videoTabButton, audioTabButton, quitSettingsButton,
            changeNameButton;
    TextureRegionDrawable buttonTabDrawable, buttonTabPressedDrawable;
    Label changeNameLabel;
    TextField changeNameTextField;


    Float scaleX, scaleY;
    
    {
        tabTable = new Table();
        gameTable = new Table();
        videoTable = new Table();
        videoTable.setVisible(false);
        audioTable = new Table();
        audioTable.setVisible(false);

        
        buttonTabDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        buttonTabPressedDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexturePressed.png"));

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


        // *** TAB MENU ***
        tabTable.setSize(300 * scaleX, 900 * scaleY);
        tabTable.setPosition(110 * scaleX, 90 * scaleY, Align.bottomLeft);
        tabTable.align(Align.top);

        // *** GAME TAB BUTTON ***
        gameTabButton = new ImageTextButton(
            Local.getString("sm_tab_game"), new ImageTextButtonStyle(
            buttonTabDrawable, buttonTabPressedDrawable, null, Fonts.getFont("PirataOne-Regular_Button")
        ));
        gameTabButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y){
               ((Menu) Utils.getCurrentScreen()).buttonClicked("TAB_GAME");
           }
        });
        tabTable.add(gameTabButton);
        tabTable.row();

        // *** VIDEO TAB BUTTON ***
       videoTabButton = new ImageTextButton(
                Local.getString("sm_tab_video"), new ImageTextButtonStyle(
                buttonTabDrawable, buttonTabPressedDrawable, null, Fonts.getFont("PirataOne-Regular_Button")
        ));
        videoTabButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Menu) Utils.getCurrentScreen()).buttonClicked("TAB_VIDEO");
            }
        });
        tabTable.add(videoTabButton);
        tabTable.row();

        // *** AUDIO TAB BUTTON ***
        audioTabButton = new ImageTextButton(
                Local.getString("sm_tab_audio"), new ImageTextButtonStyle(
                buttonTabDrawable, buttonTabPressedDrawable, null, Fonts.getFont("PirataOne-Regular_Button")
        ));
        audioTabButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Menu) Utils.getCurrentScreen()).buttonClicked("TAB_AUDIO");
            }
        });
        tabTable.add(audioTabButton);
        tabTable.row();

        // *** QUIT SETTINGS TAB BUTTON ***
        quitSettingsButton = new ImageTextButton(
                Local.getString("sm_quit"), new ImageTextButtonStyle(
                buttonTabDrawable, buttonTabPressedDrawable, null, Fonts.getFont("PirataOne-Regular_Button")
        ));
        quitSettingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Menu) Utils.getCurrentScreen()).buttonClicked("QUIT_SETTINGS");
            }
        });
        tabTable.add(quitSettingsButton);
        tabTable.row();

        addActor(tabTable);

        // *** GAME TAB ***
        gameTable.setSize(1300 * scaleX, 900 * scaleY);
        gameTable.setPosition(530 * scaleX, 90 * scaleY, Align.bottomLeft);
        gameTable.align(Align.top);

        // *** CHANGE NAME ***
        changeNameLabel = new Label(
                Local.getString("sm_game_label_change_name"),
                new Label.LabelStyle(Fonts.getFont("PirataOne-Regular_Button"), null));
        gameTable.add(changeNameLabel);

        changeNameTextField = new TextField(Settings.getProperties(Settings.APP).getProperty("player_name"),
                new TextField.TextFieldStyle(Fonts.getFont("PirataOne-Regular_Button"), Color.BLACK,
                        null, null, null));
        gameTable.add(changeNameTextField);

        changeNameButton = new ImageTextButton(
                Local.getString("sm_game_button_change_name"), new ImageTextButtonStyle(
                buttonTabDrawable, buttonTabPressedDrawable, null, Fonts.getFont("PirataOne-Regular_Button")
        ));
        changeNameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Menu) Utils.getCurrentScreen()).buttonClicked("CHANGE_NAME");
            }
        });
        gameTable.add(changeNameButton);

        addActor(gameTable);
    }
}
