package com.company.skt.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.company.skt.controller.Menu;
import com.company.skt.lib.UpdateStage;
import com.company.skt.lib.Utils;
import com.company.skt.model.Assets;
import com.company.skt.model.Fonts;
import com.company.skt.model.Local;

public class SettingsUI extends UpdateStage {
    
    Table tabTable;
    Table videoTable;
    Table audioTable;
    Table gameTable;
    SelectBox<String> resolution;
    ImageTextButton gameTabButton, videoTabButton, audioTabButton;
    TextureRegionDrawable buttonTabDrawable, buttonTabPressedDrawable;
    AssetManager aM;

    
    {
        tabTable = new Table();
        videoTable = new Table();
        audioTable = new Table();
        gameTable = new Table();
    
        aM = Assets.getAssets();
        
        buttonTabDrawable = new TextureRegionDrawable(aM.<Texture>get(
            "assets/art/menu/ButtonTexture.png"));
        buttonTabPressedDrawable = new TextureRegionDrawable(aM.<Texture>get(
        "assets/art/menu/ButtonTexturePressed.png"));
    }
    
    @Override
    public void initialize() {
        super.initialize();


        // *** BUTTON ***
        gameTabButton = new ImageTextButton(
            Local.getString("sm_tabgame"), new ImageTextButtonStyle(
            buttonTabDrawable, buttonTabPressedDrawable, null, Fonts.getFont("PirataOne-Regular_Button")
        ));
        gameTabButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y){
               ((Menu) Utils.getCurrentScreen()).buttonClicked("TAB_GAME");
           }
        });
    }
}
