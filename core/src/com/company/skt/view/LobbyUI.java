package com.company.skt.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.company.skt.controller.Menu;
import com.company.skt.controller.Utils;
import com.company.skt.lib.UpdateStage;
import com.company.skt.model.*;

import java.util.Properties;

public class LobbyUI extends UpdateStage {

    private Table leftTable, rightTable;
    private Label player0Name, player1Name, player2Name;
    private TextField chatField;
    private TextArea chatArea;
    private ImageTextButton readyButton, quitButton;
    private Properties appCfg;
    private BitmapFont buttonFont;
    private TextureRegionDrawable buttonDrawable, buttonPressedDrawable;
    private Float scaleX, scaleY;


    {
        appCfg = Settings.getProperties(Settings.APP);
        rightTable = new Table();

        buttonFont = Fonts.getFont("PirataOne-Regular_Button");
        buttonDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        buttonPressedDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexturePressed.png"));

        scaleX = Utils.getScaleFactorX();
        scaleY = Utils.getScaleFactorY();
    }

    public LobbyUI(String name) {
        super(name);
    }

    public LobbyUI(String name, boolean active) {
        super(name, active);
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public void updateUI(){
        // DEBUG NullPointer in "player0Name.setText(SessionData.get().getPlayer(0).getName());" ?!?!?

        if (SessionData.get().getPlayer(0) != null) {
            player0Name.setText(SessionData.get().getPlayer(0).getName());
        }
        else{
            player0Name.setText(Local.getString("lb_player_null"));
        }
        if (SessionData.get().getPlayer(1) != null) {
            player1Name.setText(SessionData.get().getPlayer(1).getName());
        }
        else{
            player1Name.setText(Local.getString("lb_player_null"));
        }
        if (SessionData.get().getPlayer(2) != null) {
            player2Name.setText(SessionData.get().getPlayer(2).getName());
        }
        else{
            player2Name.setText(Local.getString("lb_player_null"));
        }

    }

}
