package com.company.skt.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import com.company.skt.controller.Utils;
import com.company.skt.lib.AnimationActor;
import com.company.skt.lib.StageScreen;
import com.company.skt.lib.TaskCompleteException;
import com.company.skt.lib.UpdateStage;
import com.company.skt.model.Assets;
import com.company.skt.model.Fonts;
import com.company.skt.model.Local;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

public class DialogUI extends UpdateStage {
    
    private StageScreen screen;
    private Table table;
    private Label titleLable;
    private Label messageLable;
    private LabelStyle labelStyleTitle;
    private LabelStyle labelStyleMessage;
    private Array<ImageTextButton> buttons;
    private BitmapFont buttonFont;
    private TextureRegionDrawable buttonDrawable;
    private TextureRegionDrawable buttonPressedDrawable;
    
    private static ScheduledExecutorService triggerChecker;
    private static volatile String input;
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
        super("dialogUI");
    }
    
    public static void newOkMessage(UpdateStage callingUI,
                             @Null String title, @Null String message,
                             @Null String okButtonText,
                             UpdateStage okUI, @Null Runnable okRunnable) {
        
        DialogUI dialog = prepareDialog(title, message);
        addButtons(dialog, asList(okButtonText), asList("ok"), asList(okUI), asList(okRunnable));
        finalizeDialog(dialog, callingUI);
    }
    
    
    public static void newYesNoQuestion(UpdateStage callingUI,
                                 @Null String title, @Null String message,
                                 @Null String yesButtonText, @Null String noButtonText,
                                 UpdateStage yesUI, UpdateStage noUI,
                                 @Null Runnable yesRunnable, @Null Runnable noRunnable) {
        
        DialogUI dialog = prepareDialog(title, message);
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
        
        DialogUI dialog = prepareDialog(title, message);
        addButtons(dialog, asList(yesButtonText, noButtonText, cancelButtonText),
                   asList("confirm", "reject", "cancel"), asList(yesUI, noUI, cancelUI),
                   asList(yesRunnable, noRunnable, cancelRunnable));
        finalizeDialog(dialog, callingUI);
    }
    
    public static void newInputDialog(UpdateStage callingUI, String eventSubcategory, @Null String defaultInput,
                                      @Null String title, @Null String message,
                                      @Null String okButtonText, @Null String cancelButtonText,
                                      UpdateStage okUI, UpdateStage cancelUI,
                                      @Null Predicate<String> predicate, @Null Runnable cancelRunnable) {
        
        setInputString("");
        DialogUI dialog = prepareDialog(title, message);
        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.font = Fonts.getFont("pirata_16p_black_bord1white");
        textFieldStyle.fontColor = Color.WHITE;
        TextField inputField = new TextField(defaultInput, textFieldStyle);
        inputField.setWidth(800 * scaleX);
        inputField.setDisabled(false);
        inputField.setTextFieldListener((textField, c) -> {
            if(c == '\r' || c == '\n' ) {
                boolean pass = true;
                if(predicate != null) {
                    pass = predicate.evaluate(textField.getText());
                }
                if(pass) {
                    setInputString(textField.getText());
                    dialog.screen.event("DIALOG_INPUT_READY#" + eventSubcategory);
                    dialog.screen.setStageActive(dialog, false);
                    dialog.screen.removeStage(dialog);
                    dialog.screen.setStageActive(okUI, true);
                }
            }
        });
        dialog.table.row();
        dialog.table.add(inputField);
        dialog.table.row();
        dialog.buttons = new Array<>();
        dialog.buttonDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        dialog.buttonPressedDrawable = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexturePressed.png"));
        dialog.buttonFont = Fonts.getFont("PirataOne-Regular_Button");
        dialog.buttons.add(new ImageTextButton(
            (okButtonText != null ?
             okButtonText : Local.getString("ok")),
            new ImageTextButtonStyle(
                dialog.buttonDrawable, dialog.buttonPressedDrawable, null, dialog.buttonFont)));
        dialog.buttons.get(0).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean pass = true;
                if(predicate != null) {
                    pass = predicate.evaluate(inputField.getText());
                }
                if(pass) {
                    setInputString(inputField.getText());
                    dialog.screen.event("DIALOG_INPUT_READY#" + eventSubcategory);
                    dialog.screen.setStageActive(dialog, false);
                    dialog.screen.removeStage(dialog);
                    dialog.screen.setStageActive(okUI, true);
                }
            }
        });
        dialog.table.add(dialog.buttons.get(0));
        dialog.buttons.add(new ImageTextButton(
            (cancelButtonText != null ?
             cancelButtonText : Local.getString("cancel")),
            new ImageTextButtonStyle(
                dialog.buttonDrawable, dialog.buttonPressedDrawable, null, dialog.buttonFont)));
        dialog.buttons.get(1).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.screen.setStageActive(dialog, false);
                dialog.screen.removeStage(dialog);
                dialog.screen.setStageActive(cancelUI, true);
                if(cancelRunnable != null) {
                    new Thread(cancelRunnable).start();
                }
            }
        });
        dialog.table.add(dialog.buttons.get(1));
        finalizeDialog(dialog, callingUI);
    }
    
    public static void newTriggerMessage(UpdateStage callingUI,
                                  @Null String title, @Null String message,
                                  @Null Animation<TextureRegion> waitingAnimation,
                                  @Null String cancelButtonText,
                                  UpdateStage triggeredUI, UpdateStage fallbackUI,
                                  @Null final Runnable triggerRunnable, @Null final Runnable timeoutRunnable,
                                  boolean volatileTrigger, final int timeoutMs) {
    
        triggerChecker = Executors.newSingleThreadScheduledExecutor();
        DialogUI dialog = prepareDialog(title, message);
        if(waitingAnimation != null) {
            dialog.table.row();
            dialog.table.add(new AnimationActor("waitingAnimation", waitingAnimation));
        }
        addButtons(dialog, asList(cancelButtonText), asList("cancel"), asList(fallbackUI),
                   asList(() -> {
                       dialog.screen.setStageActive(dialog, false);
                       dialog.screen.removeStage(dialog);
                       dialog.screen.setStageActive(fallbackUI, true);
                   }));
        finalizeDialog(dialog, callingUI);
        final long startTime = TimeUtils.millis();
        triggerChecker.scheduleAtFixedRate(() -> {
            if(volatileTrigger) {
                Gdx.app.postRunnable(() -> {
                    dialog.screen.setStageActive(dialog, false);
                    dialog.screen.removeStage(dialog);
                    dialog.screen.setStageActive(triggeredUI, true);
                    if(triggerRunnable != null) {
                        new Thread(triggerRunnable).start();
                    }
                });
                triggerChecker.shutdownNow();
                triggerChecker = null;
                throw new TaskCompleteException();
            }
            if(TimeUtils.timeSinceMillis(startTime) > timeoutMs) {
                Gdx.app.postRunnable(() -> {
                    dialog.screen.setStageActive(dialog, false);
                    dialog.screen.removeStage(dialog);
                    dialog.screen.setStageActive(fallbackUI, true);
                    if(timeoutRunnable != null) {
                        new Thread(timeoutRunnable).start();
                    }
                });
                triggerChecker.shutdownNow();
                triggerChecker = null;
                throw new TaskCompleteException();
            }
        }, 2000, 10, TimeUnit.MILLISECONDS);
    }
    
    
    private static DialogUI prepareDialog(String title, String message) {
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
    
    private static synchronized void setInputString(String input) {
        DialogUI.input = input;
    }
    
    public static synchronized String getInputString() {
        return input;
    }
    
}
