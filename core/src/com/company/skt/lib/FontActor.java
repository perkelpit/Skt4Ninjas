package com.company.skt.lib;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;

/** A {@link BaseActor}-subclass to display a bit of text. */
public class FontActor extends BaseActor {
    private BitmapFont font;
    private String text;
    private GlyphLayout layout;
    private int align;
    private boolean wrap;
    
    // TODO rework the constructors and the ugly redundant calls to createLayout()
    
    public FontActor(String name) {
        super(name);
        text = "";
        font = new BitmapFont();
        align = Align.center;
        wrap = false;
    }
    
    public FontActor(String name, String text, Stage stage) {
        this(name);
        setText(text);
        stage.addActor(this);
    }
    
    public FontActor(String name, BitmapFont font, Stage stage) {
        this(name);
        setFont(font);
        stage.addActor(this);
    }
    
    public FontActor(String name, BitmapFont font, String text, Stage stage) {
        this(name);
        setText(text);
        setFont(font);
        stage.addActor(this);
    }
    
    public FontActor(String name, BitmapFont font, String text) {
        this(name);
        setText(text);
        setFont(font);
    }
    
    public FontActor(String name, BitmapFont font, String text, float x, float y) {
        this(name);
        setText(text);
        setFont(font);
        setPosition(x, y);
    }
    
    public FontActor(String name, BitmapFont font, String text, Stage stage, float x, float y) {
        this(name);
        setText(text);
        setFont(font);
        setPosition(x, y);
        stage.addActor(this);
    }
    
    private void createLayout(BitmapFont font, String text) {
        layout = new GlyphLayout(font, text);
        setWidth(layout.width);
        setHeight(layout.height);
    }
    
    public GlyphLayout getLayout() {
        return layout;
    }
    
    public void setFont(BitmapFont font) {
        font.dispose();
        this.font = font;
        createLayout(font, text);
    }
    
    public void setText(String text) {
        this.text = text;
        createLayout(font, text);
    }
    
    public void setAlign(int align) {
        this.align = align;
        createLayout(font, text);
    }
    
    public void setWrap(boolean wrap) {
        this.wrap = wrap;
        createLayout(font, text);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.draw(batch, text, getX(), getY(), getWidth(), align, wrap);
    }
    
    @Override
    // override if updates needed, even if stopped.
    public void update(float delta) {}
}
