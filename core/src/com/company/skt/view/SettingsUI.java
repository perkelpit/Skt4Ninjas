package com.company.skt.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.company.skt.controller.Menu;
import com.company.skt.lib.UpdateStage;
import com.company.skt.model.Utils;
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
    
    {
        tabTable = new Table();
        videoTable = new Table();
        audioTable = new Table();
        gameTable = new Table();
        
        buttonTabDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        buttonTabPressedDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexturePressed.png"));
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
