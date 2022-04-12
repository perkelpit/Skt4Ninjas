package com.company.skt.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.*;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.company.skt.controller.Utils;
import com.company.skt.lib.StageScreen;
import com.company.skt.lib.UpdateStage;
import com.company.skt.model.Assets;
import com.company.skt.model.Fonts;
import com.company.skt.model.Local;

public class DialogUI extends UpdateStage {
    
    public enum DialogType {
        OK_MESSAGE, TRIGGER_MESSAGE, TRIGGER_CANCEL_MESSAGE,
        YES_NO_QUESTION, YES_NO_CANCEL_QUESTION, INPUT_DIALOG
    }
    
    private Table table;
    private Label titleLable;
    private Label messageLable;
    private LabelStyle labelStyleTitle;
    private LabelStyle labelStyleMessage;
    private TextField inputField;
    private Array<ImageTextButton> buttons;
    private BitmapFont buttonFont;
    private TextureRegionDrawable buttonDrawable;
    private TextureRegionDrawable buttonPressedDrawable;
    
    private float scaleX;
    private float scaleY;
    
    {
        table = new Table();
        scaleX = Utils.getScaleFactorX();
        scaleY = Utils.getScaleFactorY();
    }
    
    private DialogUI(UpdateStage callingUI, @Null String title, String message) {
        super("dialogUI", true);
        buttons = new Array<>();
        /* ### TABLE ### */
        table.setSize(1200 * scaleX, 800 * scaleY);
        table.setPosition(360 * scaleX, 140 * scaleY, Align.bottomLeft);
        table.align(Align.top);

        /* ### TITLE LABLE ### */
        if(title != null) {
            labelStyleTitle = new LabelStyle();
            labelStyleTitle.font = Fonts.getFont("PirataOne-Regular_Button");
            titleLable = new Label(title, labelStyleTitle);
            table.add(titleLable);
            table.row();
        }
    
        /* ### MESSAGE LABLE ### */
        labelStyleMessage = new LabelStyle();
        labelStyleMessage.font = Fonts.getFont("PirataOne-Regular_Message");
        messageLable = new Label(message, labelStyleMessage);
        table.add(messageLable);
    }
    
    public static void newOkMessage(UpdateStage callingUI,
                             @Null String title, String message,
                             @Null String okButtonText,
                             UpdateStage nextUI) {
        StageScreen screen = Utils.getCurrentScreen();
        DialogUI dialog = new DialogUI(callingUI, title, message);
        dialog.prepareButtonTexturesAndFont();
        dialog.buttons.add(new ImageTextButton(
            (okButtonText != null ? okButtonText : Local.getString("ok")),
            new ImageTextButtonStyle(
                dialog.buttonDrawable, dialog.buttonPressedDrawable, null, dialog.buttonFont)));
        dialog.buttons.get(0).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.setStageActive(dialog, false);
                screen.removeStage(dialog);
                screen.setStageActive(nextUI, true);
            }
        });
        dialog.table.row();
        dialog.table.add(dialog.buttons.get(0));
        dialog.addActor(dialog.table);
        screen.setStageActive(callingUI, false);
        screen.addStage(dialog);
        screen.setStageActive(dialog, true);
    }
    
    public static String newInputDialog(UpdateStage callingUI,
                                 @Null String title, String message,
                                 @Null String okButtonText,
                                 UpdateStage nextUI) {
        String input = null;
        
        // TODO correct constructor
        return input;
    }
    
    public static void newYesNoQuestion(UpdateStage callingUI,
                                 @Null String title, String message,
                                 @Null String yesButtonText, @Null String noButtonText,
                                 UpdateStage yesUI, UpdateStage noUI) {
        // TODO correct constructor
    }
    
    public static void newYesNoCancelQuestion(UpdateStage callingUI,
                                       @Null String title, String message,
                                       @Null String yesButtonText, @Null String noButtonText,
                                       @Null String cancelButtonText,
                                       UpdateStage yesUI, UpdateStage noUI, UpdateStage cancelUI) {
        // TODO correct constructor
    }
    
    public static void newTriggerMessage(UpdateStage callingUI,
                                  @Null String title, String message,
                                  @Null Animation<TextureRegion> waitingAnimation,
                                  UpdateStage triggeredUI, UpdateStage fallbackUI,
                                  boolean volatileTrigger, int timeOutMs) {
        // TODO correct constructor
    }
    
    public static void newTriggerCancelMessage(UpdateStage callingUI,
                                        @Null String title, String message,
                                        @Null Animation<TextureRegion> waitingAnimation,
                                        UpdateStage triggeredUI, UpdateStage fallbackUI,
                                        boolean volatileTrigger) {
        // TODO correct constructor
    }
    
    private void prepareButtonTexturesAndFont() {
        buttonDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        buttonPressedDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexturePressed.png"));
        buttonFont = Fonts.getFont("PirataOne-Regular_Button");
    }
    
}
