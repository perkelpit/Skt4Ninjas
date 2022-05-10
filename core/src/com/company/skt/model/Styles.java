package com.company.skt.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Null;

import java.awt.*;

public abstract class Styles {
    
    public static LabelStyle newLabelStyle() {
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = Fonts.get("medLable");
        return labelStyle;
    }
    
    public static LabelStyle newLabelStyle(@Null BitmapFont font, @Null Color fontColor, @Null Drawable background) {
        LabelStyle style = new LabelStyle();
        style.font = font != null ? font : Fonts.get("medLable");
        style.fontColor = fontColor != null ? fontColor : Color.BLACK;
        if(background != null)
            style.background = background;
        return style;
    }
    
    public static ImageTextButtonStyle newImageTextButtonStyle() {
        ImageTextButtonStyle style = new ImageTextButtonStyle();
        style.up = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        style.down = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexturePressed.png"));
        style.font = Fonts.get("button");
        return style;
    }
    
    public static ImageTextButtonStyle newImageTextButtonStyle(@Null Drawable up, @Null Drawable down,
                                                         @Null Drawable focused, @Null Drawable checkedFocus,
                                                         @Null Drawable checked, @Null Drawable checkedDown) {
        ImageTextButtonStyle style = new ImageTextButtonStyle();
        style.up = up != null ? up : new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        style.down = down != null ? down : new TextureRegionDrawable(Assets.<Texture>get("ButtonTexturePressed.png"));
        if(checked != null)
            style.checked = checked;
        if(checkedDown != null)
            style.checkedDown = checkedDown;
        if(focused != null)
            style.focused = focused;
        if(checkedFocus != null)
            style.checkedFocused = checkedFocus;
        style.font = Fonts.get("button");
        return style;
    }
    
    public static ImageTextButtonStyle newImageTextButtonStyle(@Null Drawable up, @Null Drawable down,
                                                         @Null BitmapFont font, @Null Color fontColor) {
        ImageTextButtonStyle style = new ImageTextButtonStyle();
        style.up = up != null ? up : new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        style.down = down != null ? down : new TextureRegionDrawable(Assets.<Texture>get("ButtonTexturePressed.png"));
        style.font = font != null ? font : Fonts.get("button");
        style.fontColor = fontColor != null ? fontColor : Color.BLACK;

        return style;
    }
    
    public static SelectBoxStyle newSelectBoxStyle() {
        SelectBoxStyle style = new SelectBoxStyle();
        style.font = Fonts.get("selectBox");
        style.fontColor = Color.BLACK;
        style.overFontColor = Color.GRAY;
        
        // TODO some standards like scrollknob etc. for this one:
        style.scrollStyle = new ScrollPaneStyle();
        
        style.listStyle = new ListStyle();
        style.listStyle.font = Fonts.get("selectBox");
        style.listStyle.selection = new TextureRegionDrawable(Assets.<Texture>get("ButtonTexture.png"));
        style.listStyle.fontColorSelected = Color.ORANGE;
        style.listStyle.fontColorUnselected = Color.WHITE;
        return style;
    }
    
    public static TextFieldStyle newTextFieldStyle() {
        TextFieldStyle style = new TextFieldStyle();
        style.font = Fonts.get("textField");
        style.fontColor = Color.BLACK;
        style.background = new TextureRegionDrawable((Texture)Assets.get("TextfieldTexture2.png"));
        // style.cursor = new TextureRegionDrawable((Texture)Assets.get("textEditCursor.png"));
        style.focusedBackground = new TextureRegionDrawable((Texture)Assets.get("TextfieldTexture2.png"));
        style.disabledBackground = new TextureRegionDrawable((Texture)Assets.get("TextfieldTexture2.png"));
        style.disabledFontColor = Color.DARK_GRAY;
        style.messageFont = Fonts.get("textField");
        style.messageFontColor = Color.BLACK;
        return style;
    }
    
}
