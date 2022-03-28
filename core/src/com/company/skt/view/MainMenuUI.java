package com.company.skt.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.company.skt.controller.Menu;
import com.company.skt.lib.UpdateStage;
import com.company.skt.lib.Utils;
import com.company.skt.model.Assets;
import com.company.skt.model.Local;
import com.company.skt.model.Settings;

import java.util.Properties;


public class MainMenuUI extends UpdateStage {
    
    Table menuTable;
    Label welcomeMessageLabel;
    FreeTypeFontGenerator fontGenerator;
    FreeTypeFontParameter fontParametersWelcome, fontParametersButtons;
    ImageTextButton hostButton, joinButton, archiveButton, settingsButton, creditsButton, exitButton;
    TextureRegionDrawable buttonDrawable, buttonPressedDrawable;
    BitmapFont welcomeMessageFont, buttonFont;
    LabelStyle labelStyleWelcome, labelStyleButtons;
    Properties appCfg;
    AssetManager aM;
    
    {
        appCfg = Settings.getProperties(Settings.APP);
        aM = Assets.getAssets();
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/PirataOne-Regular.ttf"));
        fontParametersWelcome = new FreeTypeFontParameter();
        fontParametersWelcome.size = 48;
        fontParametersWelcome.color = Color.BLACK;
        fontParametersWelcome.borderWidth = 2;
        fontParametersWelcome.borderColor = Color.WHITE;
        fontParametersWelcome.borderStraight = true;
        fontParametersWelcome.minFilter = TextureFilter.Linear;
        fontParametersWelcome.magFilter = TextureFilter.Linear;
        
        welcomeMessageFont = fontGenerator.generateFont(fontParametersWelcome);
        labelStyleWelcome = new LabelStyle();
        labelStyleWelcome.font = welcomeMessageFont;
    
        fontParametersButtons = new FreeTypeFontParameter();
        fontParametersButtons.size = 32;
        fontParametersButtons.color = Color.BLACK;
        fontParametersButtons.borderWidth = 2;
        fontParametersButtons.borderColor = Color.WHITE;
        fontParametersButtons.borderStraight = true;
        fontParametersButtons.minFilter = TextureFilter.Linear;
        fontParametersButtons.magFilter = TextureFilter.Linear;
        
        buttonFont = fontGenerator.generateFont(fontParametersButtons);
        buttonDrawable = new TextureRegionDrawable(aM.<Texture>get(
            "assets/art/menu/ButtonTexture.png"));
        
        buttonPressedDrawable = new TextureRegionDrawable(aM.<Texture>get(
            "assets/art/menu/ButtonTexturePressed.png"));
        
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
        welcomeMessageLabel = new Label(Local.getString("mm_greet") + " " + Local.getString("player-name"),
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
                ((Menu)Utils.getCurrentScreen()).buttonClicked("HOST");
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
                ((Menu)Utils.getCurrentScreen()).buttonClicked("JOIN");
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
                ((Menu)Utils.getCurrentScreen()).buttonClicked("ARCHIVE");
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
                ((Menu)Utils.getCurrentScreen()).buttonClicked("SETTINGS");
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
                ((Menu)Utils.getCurrentScreen()).buttonClicked("CREDITS");
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
                ((Menu)Utils.getCurrentScreen()).buttonClicked("EXIT");
            }
        });
        menuTable.add(exitButton);
        
        
        addActor(menuTable);
    }
}
