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


        // *** LEFT TABLE ***
        leftTable = new Table();
        leftTable.setWidth((Float.parseFloat(appCfg.getProperty("resolution_x")) / 2f) * scaleX);
        leftTable.setHeight((Float.parseFloat(appCfg.getProperty("resolution_y")) / 2f) * scaleY);
        leftTable.setPosition((Float.parseFloat(appCfg.getProperty("resolution_x")) / 2f) * scaleX,
                (Float.parseFloat(appCfg.getProperty("resolution_y")) / 2f) * scaleY,
                Align.bottomLeft);

       // *** PLAYER LABELS ***
        player0Name = new Label(Local.getString("lb_player_null"),
                new Label.LabelStyle(Fonts.getFont("PirataOne-Regular_Button"), null));
        leftTable.add(player0Name);
        leftTable.row();

        player1Name = new Label(Local.getString("lb_player_null"),
                new Label.LabelStyle(Fonts.getFont("PirataOne-Regular_Button"), null));
        leftTable.add(player1Name);
        leftTable.row();

        player2Name = new Label(Local.getString("lb_player_null"),
                new Label.LabelStyle(Fonts.getFont("PirataOne-Regular_Button"), null));
        leftTable.add(player2Name);
        leftTable.row();
    
        // *** von Perkel reingepfuschter quitButton ***
        quitButton = new ImageTextButton(
            Local.getString("sm_quit"), new ImageTextButton.ImageTextButtonStyle(
            buttonDrawable, buttonPressedDrawable, null, Fonts.getFont("PirataOne-Regular_Button")
        ));
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Menu) Utils.getCurrentScreen()).buttonClicked("QUIT_LOBBY");
            }
        });
        leftTable.add(quitButton);
        leftTable.row();

        addActor(leftTable);
    }

    public void updateUI(){
        // DEBUG NullPointer in "player0Name.setText(SessionData.get().getPlayer(0).getName());" ?!?!?
        /*
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
         */
    }

}
