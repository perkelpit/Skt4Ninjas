package com.company.skt.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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

    private Table topLeftTable, topRightTable;
    private Label player0Name, player1Name, player2Name;
    private TextField chatField;
    private TextArea chatArea;
    private ImageTextButton readyButton, quitButton;
    private Properties appCfg;
    private BitmapFont buttonFont;
    private TextureRegionDrawable buttonDrawable, buttonPressedDrawable;
    private SelectBox<String> numberOfGamesSelectBox, timeLimitSelectBox;
    private CheckBox junkCheckbox;
    private Float scaleX, scaleY;


    {
        appCfg = Settings.getProperties(Settings.APP);

        buttonFont = Fonts.getFont("PirataOne-Regular_Button");
        buttonDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        buttonPressedDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexturePressed.png"));

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

        // *** TOP LEFT TABLE ***
        topLeftTable = new Table();
        topLeftTable.setWidth((Float.parseFloat(appCfg.getProperty("resolution_x")) / 2f) * scaleX);
        topLeftTable.setHeight((Float.parseFloat(appCfg.getProperty("resolution_y")) / 2f) * scaleY);
        topLeftTable.setPosition(0,
                (Float.parseFloat(appCfg.getProperty("resolution_y"))) * scaleY,
                Align.bottomLeft);

        // *** PLAYER LABELS ***
        player0Name = new Label(Local.getString("lb_player_null"),
                new Label.LabelStyle(Fonts.getFont("PirataOne-Regular_Button"), null));
        topLeftTable.add(player0Name);
        topLeftTable.row();

        player1Name = new Label(Local.getString("lb_player_null"),
                new Label.LabelStyle(Fonts.getFont("PirataOne-Regular_Button"), null));
        topLeftTable.add(player1Name);
        topLeftTable.row();

        player2Name = new Label(Local.getString("lb_player_null"),
                new Label.LabelStyle(Fonts.getFont("PirataOne-Regular_Button"), null));
        topLeftTable.add(player2Name);
        topLeftTable.row();

        // *** QUIT BUTTON ***
        quitButton = new ImageTextButton(
                Local.getString("quit"), new ImageTextButton.ImageTextButtonStyle(
                buttonDrawable, buttonPressedDrawable, null, Fonts.getFont("PirataOne-Regular_Button")
        ));
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Menu) Utils.getCurrentScreen()).buttonClicked("QUIT_LOBBY");
            }
        });

        topLeftTable.add(quitButton);
        topLeftTable.row();

        addActor(topLeftTable);

        // *** TOP RIGHT TABLE ***
        topRightTable = new Table();
        topRightTable.setWidth((Float.parseFloat(appCfg.getProperty("resolution_x")) / 2f) * scaleX);
        topRightTable.setHeight((Float.parseFloat(appCfg.getProperty("resolution_y")) / 2f) * scaleY);
        topRightTable.setPosition(
                Float.parseFloat(appCfg.getProperty("resolution_x")) * scaleX,
                (Float.parseFloat(appCfg.getProperty("resolution_y"))) * scaleY,
                Align.bottomLeft);

        numberOfGamesSelectBox = new SelectBox<String>(new SelectBox.SelectBoxStyle(buttonFont, Color.WHITE,
                null, new ScrollPane.ScrollPaneStyle(), new List.ListStyle(buttonFont, Color.ORANGE,
                Color.WHITE, new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png")))));
        numberOfGamesSelectBox.setItems("1", "3", "6", "9", "18", "36");
        topRightTable.add(numberOfGamesSelectBox);
        topRightTable.row();

        timeLimitSelectBox = new SelectBox<String>(new SelectBox.SelectBoxStyle(buttonFont, Color.WHITE,
                null, new ScrollPane.ScrollPaneStyle(), new List.ListStyle(buttonFont, Color.ORANGE,
                Color.WHITE, new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png")))));
        timeLimitSelectBox.setItems("no limit", "30sec", "1 min.", "2 min.", "3 min.", "5 min.", "10 min.");
        topRightTable.add(timeLimitSelectBox);
        topRightTable.row();

        /* //TODO CHECKBOXSTYLECONSTRUCTOR SUCKT
        junkCheckbox = new CheckBox(Local.getString("lb_include_junk"), new CheckBox.CheckBoxStyle());
        topRightTable.add(junkCheckbox);
        topRightTable.row();
        */
        addActor(topRightTable);
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public void updateUI(){
        SessionData data = SessionData.get();
        // DEBUG NullPointer in "player0Name.setText(SessionData.get().getPlayer(0).getName());" ?!?!?
        DebugWindow.println("updateUI() called");
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

    }

}
