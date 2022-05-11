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
import com.company.skt.Skt;
import com.company.skt.controller.Utils;
import com.company.skt.lib.*;
import com.company.skt.model.Assets;
import com.company.skt.model.Fonts;
import com.company.skt.model.Local;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

/**
 * {@code DialogUI} is a sub-framework to dynamically create dialog stages. It roughly leans to Java´s own dialogs.
 * It provides following dialog types: <br>
 * {@link #newOkMessage(UpdateStage, String, String, String, UpdateStage, Runnable) Ok-Message / 1-Button-Dialog} <br>
 * {@link #newYesNoQuestion(UpdateStage, String, String, String, String, UpdateStage, UpdateStage, Runnable, Runnable)
 * Yes-No-Message / 2-Button-Dialog}<br>
 * {@link #newYesNoCancelQuestion(UpdateStage, String, String, String, String, String, UpdateStage, UpdateStage,
 * UpdateStage, Runnable, Runnable, Runnable) Yes-No-CancelMessage / 3-Button-Dialog} <br>
 * {@link #newInputDialog(UpdateStage, String, String, String, String, String, String, UpdateStage, UpdateStage,
 * Predicate, Runnable) Input-Dialog} <br>
 * {@link #newTriggerMessage(UpdateStage, String, String, Animation, String, UpdateStage, UpdateStage, Runnable,
 * Runnable, Runnable, int) Trigger-Message}*/
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

    private static Trigger trigger;
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
    
    /**
     * A 1-Button-Dialog whichs button text defaults to "Ok"(or corresponding Strings in other languages). <br>
     * After hitting the button, the corresponding UI passed as param is activated.
     * A {@link Runnable}, called after hitting the button, may be passed. The rest of the {@code nullable} params
     * should be self-explanatory.
     * */
    public static void newOkMessage(UpdateStage callingUI,
                             @Null String title, @Null String message,
                             @Null String okButtonText,
                             UpdateStage okUI, @Null Runnable okRunnable) {
        
        DialogUI dialog = prepareDialog(title, message);
        addButtons(dialog, asList(okButtonText), asList("ok"), asList(okUI), asList(okRunnable));
        finalizeDialog(dialog, callingUI);
    }
    
    /**
     * A 2-Button-Dialog whichs buttons texts default to "Yes" and "No"(or corresponding Strings in other languages). <br>
     * After hitting a button, the corresponding UI passed as param is activated.
     * A {@link Runnable}, called after hitting a button, may be passed. The rest of the {@code nullable} params
     * should be self-explanatory.
     * */
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
    
    /**
     * A 3-Button-Dialog whichs buttons texts default to "Yes", "No" and "Cancel"
     * (or corresponding Strings in other languages). <br>
     * After hitting a button, the corresponding UI passed as param is activated.
     * A {@link Runnable}, called after hitting a button, may be passed. The rest of the {@code nullable} params
     * should be self-explanatory.
     * */
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
    
    /**
     * A Input-Dialog with two buttons. The button-texts default to "Ok" and "Cancel".
     * (or corresponding Strings in other languages). <br>
     * After hitting a button, the corresponding UI passed as param is activated and the {@code String} in the
     * {@code TextField} is stored in the {@code static String input} to be gathered by another code fragment.
     * Hitting {@code ENTER} within the {@code TextField} is treated like clicking the ok-button. <br>
     * A {@link Runnable}, called after hitting the cancel button, may be passed. <br>
     * A {@link Predicate} to test if the input is valid may be passed. <br>
     * The rest of the {@code nullable} params
     * should be self-explanatory.
     * */
    public static void newInputDialog(UpdateStage callingUI, String eventSubcategory, @Null String defaultInput,
                                      @Null String title, @Null String message,
                                      @Null String okButtonText, @Null String cancelButtonText,
                                      UpdateStage okUI, UpdateStage cancelUI,
                                      @Null Predicate<String> predicate, @Null Runnable cancelRunnable) {
        
        setInputString("");
        DialogUI dialog = prepareDialog(title, message);
        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.font = Fonts.get("textField");
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = new TextureRegionDrawable(
            new TextureRegion(Assets.<Texture>get("TextfieldTexture.png")));
        TextField inputField = new TextField(defaultInput, textFieldStyle);
        inputField.setWidth(800 * scaleX);
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
        dialog.buttonFont = Fonts.get("button");
        dialog.buttons.add(new ImageTextButton(
            (okButtonText != null ?
             okButtonText : Local.get("ok")),
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
             cancelButtonText : Local.get("cancel")),
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
    
    /**
     * A Trigger-Dialog one button. The button-text default to "Cancel".
     * (or corresponding Strings in other languages). <br>
     * After hitting the button, the corresponding UI passed as param is activated.
     * After the {@code private static Trigger trigger} is set to {@code SUCCESS} or {@code FAIL},
     * the corresponding UI passed as param is activated.
     * Hitting {@code ENTER} within the {@code TextField} is treated like clicking the ok-button. <br>
     * A {@link Runnable}, called after hitting the cancel-button(fallbackUI), after the Trigger is set or
     * after the time-out is reached, may be passed. <br>
     * The rest of the {@code nullable} params
     * should be self-explanatory. <br>
     * A time-out-value in milliseconds has to be passed.
     * */
    public static void newTriggerMessage(UpdateStage callingUI,
                                         @Null String title, @Null String message,
                                         @Null Animation<TextureRegion> waitingAnimation,
                                         @Null String cancelButtonText,
                                         UpdateStage triggeredUI, UpdateStage fallbackUI,
                                         @Null final Runnable successRunnable, @Null final Runnable failRunnable,
                                         @Null final Runnable timeoutRunnable, final int timeoutMs) {
    

        DialogUI dialog = prepareDialog(title, message);
        if(waitingAnimation != null) {
            dialog.table.row();
            dialog.table.add(new AnimationActor("waitingAnimation", waitingAnimation));
        }
        addButtons(dialog, asList(cancelButtonText), asList("cancel"), asList(fallbackUI),
                   asList(() -> {
                       dialog.screen.setStageActive(dialog, false);
                       dialog.screen.setStageActive(fallbackUI, true);
                       dialog.screen.removeStage(dialog);
                   }));
        finalizeDialog(dialog, callingUI);
        final long startTime = TimeUtils.millis();
        Skt.getExecutor().scheduleAtFixedRate(() -> {
            if(Skt.isStop()) {
                throw new TaskCompleteException();
            }
            if(getTrigger() == Trigger.SUCCESS) {
                setTrigger(Trigger.WAITING);
                Gdx.app.postRunnable(() -> {
                    dialog.screen.setStageActive(dialog, false);
                    dialog.screen.setStageActive(triggeredUI, true);
                    dialog.screen.removeStage(dialog);
                    if(successRunnable != null) {
                        new Thread(successRunnable).start();
                    }
                });
                throw new TaskCompleteException();
            }
            if(getTrigger() == Trigger.FAIL) {
                Gdx.app.postRunnable(() -> {
                    setTrigger(Trigger.WAITING);
                    dialog.screen.setStageActive(dialog, false);
                    dialog.screen.setStageActive(fallbackUI, true);
                    dialog.screen.removeStage(dialog);
                    if(failRunnable != null) {
                        new Thread(failRunnable).start();
                    }
                });
                throw new TaskCompleteException();
            }
            if(TimeUtils.timeSinceMillis(startTime) > timeoutMs) {
                Gdx.app.postRunnable(() -> {
                    dialog.screen.setStageActive(dialog, false);
                    dialog.screen.setStageActive(fallbackUI, true);
                    dialog.screen.removeStage(dialog);
                    if(timeoutRunnable != null) {
                        new Thread(timeoutRunnable).start();
                    }
                });
                throw new TaskCompleteException();
            }
        }, 1000, 10, TimeUnit.MILLISECONDS);
    }
    
    
    private static DialogUI prepareDialog(String title, String message) {
        DialogUI dialog = new DialogUI();
        
        /* ### TITLE LABLE ### */
        if(title != null) {
            dialog.labelStyleTitle = new LabelStyle();
            dialog.labelStyleTitle.font = Fonts.get("button");
            dialog.titleLable = new Label(title, dialog.labelStyleTitle);
            dialog.table.add(dialog.titleLable);
        }
    
        /* ### MESSAGE LABLE ### */
        if(message != null) {
            dialog.labelStyleMessage = new LabelStyle();
            dialog.labelStyleMessage.font = Fonts.get("medLable");
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
        dialog.buttonFont = Fonts.get("button");
        dialog.table.row();
        
        for(int i = 0; i < buttonTexts.size(); i++) {
            dialog.buttons.add(new ImageTextButton(
                (buttonTexts.get(i) != null ?
                 buttonTexts.get(i) : Local.get(stdButtonTextLocalStrings.get(i))),
                new ImageTextButtonStyle(
                    dialog.buttonDrawable, dialog.buttonPressedDrawable, null, dialog.buttonFont)));
            int finalI = i;
            dialog.buttons.get(i).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    dialog.screen.setStageActive(dialog, false);
                    dialog.screen.setStageActive(nextUIs.get(finalI), true);
                    dialog.screen.removeStage(dialog);
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

    public static synchronized Trigger getTrigger() {
        return trigger;
    }

    public static synchronized void setTrigger(Trigger trigger) {
        DialogUI.trigger = trigger;
    }
}
