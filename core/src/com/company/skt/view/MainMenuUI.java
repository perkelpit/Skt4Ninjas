package com.company.skt.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.company.skt.controller.Utils;
import com.company.skt.lib.UpdateStage;
import com.company.skt.model.*;

import java.util.Properties;


public class MainMenuUI extends UpdateStage {

    private Table menuTable;
    private Label welcomeMessageLabel;
    private ImageTextButton hostButton, joinButton, archiveButton, settingsButton, creditsButton, exitButton;
    private TextureRegionDrawable buttonDrawable, buttonPressedDrawable;
    private BitmapFont buttonFont;
    private LabelStyle labelStyleWelcome;
    private Properties appCfg;
    
    {
        appCfg = Settings.getProperties(Settings.APP);

        labelStyleWelcome = new LabelStyle();
        labelStyleWelcome.font = Fonts.get("bigTitle");
        
        buttonFont = Fonts.get("button");
        buttonDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        buttonPressedDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexturePressed.png"));
    }

    public MainMenuUI(String name) {
        super(name);
    }
    
    public MainMenuUI(String name, boolean active) {
        super(name, active);
    }

    @Override
    public void initialize() {
        super.initialize();

        // *** TABLE ***
        menuTable = new Table();
        menuTable.setWidth(Float.parseFloat(appCfg.getProperty("resolution_x")));
        menuTable.setHeight(Float.parseFloat(appCfg.getProperty("resolution_y")));
        menuTable.align(Align.center);
        
        // *** LABEL ***
        welcomeMessageLabel = new Label(
                Local.getString("mm_greet") + " " +
                Settings.getProperties(Settings.APP).getProperty("player_name"),
                                        labelStyleWelcome);
        menuTable.add(welcomeMessageLabel);
        menuTable.row();
        
        // *** HOST BUTTON ***
        hostButton = new ImageTextButton(
            Local.getString("mm_host"), new ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, null, buttonFont));
        hostButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Utils.getCurrentScreen().buttonClicked("HOST");
            }
        });
        menuTable.add(hostButton);
        menuTable.row();
        
        // *** JOIN BUTTON ***
        joinButton = new ImageTextButton(
            Local.getString("mm_join"), new ImageTextButtonStyle(
            buttonDrawable, buttonPressedDrawable, null, buttonFont));
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Utils.getCurrentScreen().buttonClicked("JOIN");
            }
        });
        menuTable.add(joinButton);
        menuTable.row();
        
        // *** ARCHIVE BUTTON ***
        archiveButton = new ImageTextButton(
            Local.getString("mm_archive"), new ImageTextButtonStyle(
            buttonDrawable, buttonPressedDrawable, null, buttonFont));
        archiveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Utils.getCurrentScreen().buttonClicked("ARCHIVE");
            }
        });
        menuTable.add(archiveButton);
        menuTable.row();
        
        // *** SETTINGS ***
        settingsButton = new ImageTextButton(
            Local.getString("mm_settings"), new ImageTextButtonStyle(
            buttonDrawable, buttonPressedDrawable, null, buttonFont));
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Utils.getCurrentScreen().buttonClicked("SETTINGS");
            }
        });
        menuTable.add(settingsButton);
        menuTable.row();
    
        // *** CREDITS BUTTON ***
        creditsButton = new ImageTextButton(
            Local.getString("mm_credits"), new ImageTextButtonStyle(
            buttonDrawable, buttonPressedDrawable, null, buttonFont));
        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Utils.getCurrentScreen().buttonClicked("CREDITS");
            }
        });
        menuTable.add(creditsButton);
        menuTable.row();
        
        // *** EXIT BUTTON ***
        exitButton = new ImageTextButton(
            Local.getString("mm_exit"), new ImageTextButtonStyle(
            buttonDrawable, buttonPressedDrawable, null, buttonFont));
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Utils.getCurrentScreen().buttonClicked("EXIT");
            }
        });
        menuTable.add(exitButton);
        
        
        addActor(menuTable);
    }
}
