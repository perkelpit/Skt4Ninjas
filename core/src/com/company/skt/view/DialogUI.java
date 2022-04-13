package com.company.skt.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
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

import java.util.List;

import static java.util.Arrays.asList;

public class DialogUI extends UpdateStage {
    
    private StageScreen screen;
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
    
    private static float scaleX;
    private static float scaleY;
    
    static {
        scaleX = Utils.getScaleFactorX();
        scaleY = Utils.getScaleFactorY();
    }
    
    {
        screen = Utils.getCurrentScreen();
        table = new Table();
        table.setSize(1200 * scaleX, 800 * scaleY);
        table.setPosition(360 * scaleX, 140 * scaleY, Align.bottomLeft);
        table.align(Align.top);
    }
    
    private DialogUI() {
        super("dialogUI", true);
    }
    
    public static void newOkMessage(UpdateStage callingUI,
                             @Null String title, @Null String message,
                             @Null String okButtonText,
                             UpdateStage nextUI, @Null Runnable okRunnable) {
        
        DialogUI dialog = prepareDialog(callingUI, title, message);
        addButtons(dialog, asList(okButtonText), asList("ok"), asList(nextUI), asList(okRunnable));
        finalizeDialog(dialog, callingUI);
    }
    
    public static String newInputDialog(UpdateStage callingUI,
                                 @Null String title, @Null String message,
                                 @Null String okButtonText,
                                 UpdateStage nextUI, @Null Runnable okRunnable) {
        String input = null;
        DialogUI dialog = prepareDialog(callingUI, title, message);
        dialog.table.row();
        // TODO TextField textField = new TextField()
        addButtons(dialog, asList(okButtonText), asList("ok"), asList(nextUI), asList(okRunnable));
        finalizeDialog(dialog, callingUI);
        return input;
    }
    
    public static void newYesNoQuestion(UpdateStage callingUI,
                                 @Null String title, @Null String message,
                                 @Null String yesButtonText, @Null String noButtonText,
                                 UpdateStage yesUI, UpdateStage noUI,
                                 @Null Runnable yesRunnable, @Null Runnable noRunnable) {
        
        DialogUI dialog = prepareDialog(callingUI, title, message);
        addButtons(dialog, asList(yesButtonText, noButtonText), asList("confirm", "reject"),
                   asList(yesUI, noUI), asList(yesRunnable, noRunnable));
        finalizeDialog(dialog, callingUI);
    }
    
    public static void newYesNoCancelQuestion(UpdateStage callingUI,
                                       @Null String title, @Null String message,
                                       @Null String yesButtonText, @Null String noButtonText,
                                       @Null String cancelButtonText,
                                       UpdateStage yesUI, UpdateStage noUI, UpdateStage cancelUI,
                                       @Null Runnable yesRunnable, @Null Runnable noRunnable,
                                       @Null Runnable cancelRunnable) {
        DialogUI dialog = prepareDialog(callingUI, title, message);
        addButtons(dialog, asList(yesButtonText, noButtonText, cancelButtonText),
                   asList("confirm", "reject", "cancel"), asList(yesUI, noUI, cancelUI),
                   asList(yesRunnable, noRunnable, cancelRunnable));
        finalizeDialog(dialog, callingUI);
    }
    
    public static void newTriggerMessage(UpdateStage callingUI,
                                  @Null String title, @Null String message,
                                  @Null Animation<TextureRegion> waitingAnimation,
                                  @Null String cancelButtonText,
                                  UpdateStage triggeredUI, UpdateStage fallbackUI,
                                  @Null Runnable triggerRunnable, @Null Runnable cancelRunnable,
                                  boolean volatileTrigger, int timeOutMs) {
        // TODO correct constructor
    }
    
    public static void newTriggerCancelMessage(UpdateStage callingUI,
                                        @Null String title, @Null String message,
                                        @Null Animation<TextureRegion> waitingAnimation,
                                        UpdateStage triggeredUI, UpdateStage fallbackUI,
                                        boolean volatileTrigger) {
        // TODO correct constructor
    }
    
    private static DialogUI prepareDialog(UpdateStage callingUI, String title, String message) {
        DialogUI dialog = new DialogUI();
        
        /* ### TITLE LABLE ### */
        if(title != null) {
            dialog.labelStyleTitle = new LabelStyle();
            dialog.labelStyleTitle.font = Fonts.getFont("PirataOne-Regular_Button");
            dialog.titleLable = new Label(title, dialog.labelStyleTitle);
            dialog.table.add(dialog.titleLable);
        }
    
        /* ### MESSAGE LABLE ### */
        if(message != null) {
            dialog.labelStyleMessage = new LabelStyle();
            dialog.labelStyleMessage.font = Fonts.getFont("PirataOne-Regular_Message");
            dialog.messageLable = new Label(message, dialog.labelStyleMessage);
            dialog.table.row();
            dialog.table.add(dialog.messageLable);
        }
        
        return dialog;
    }
    
    private static void addButtons(DialogUI dialog, List<String> buttonTexts,
                                   List<String> stdButtonTextLocalStrings,
                                   List<UpdateStage> nextUIs, List<Runnable> buttonRunnables) {
        
        dialog.buttons = new Array<>();
        dialog.buttonDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        dialog.buttonPressedDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexturePressed.png"));
        dialog.buttonFont = Fonts.getFont("PirataOne-Regular_Button");
        dialog.table.row();
        
        for(int i = 0; i < buttonTexts.size(); i++) {
            dialog.buttons.add(new ImageTextButton(
                (buttonTexts.get(i) != null ?
                 buttonTexts.get(i) : Local.getString(stdButtonTextLocalStrings.get(i))),
                new ImageTextButtonStyle(
                    dialog.buttonDrawable, dialog.buttonPressedDrawable, null, dialog.buttonFont)));
            int finalI = i;
            dialog.buttons.get(i).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    dialog.screen.setStageActive(dialog, false);
                    dialog.screen.removeStage(dialog);
                    dialog.screen.setStageActive(nextUIs.get(finalI), true);
                    if(buttonRunnables.get(finalI) != null) {
                        new Thread(buttonRunnables.get(finalI)).start();
                    }
                }
            });
            dialog.table.add(dialog.buttons.get(i));
        }
    }
    
    
    private static void finalizeDialog(DialogUI dialog, UpdateStage callingUI) {
        dialog.addActor(dialog.table);
        dialog.screen.setStageActive(callingUI, false);
        dialog.screen.addStage(dialog);
        dialog.screen.setStageActive(dialog, true);
    }
    
}
