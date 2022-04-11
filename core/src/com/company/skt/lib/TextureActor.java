package com.company.skt.lib;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureActor extends BaseActor {
    
    private TextureRegion texture;
    
    public TextureActor(String name) {
        super(name);
    }
    
    public TextureActor(String name, TextureRegion textureRegion) {
        super(name);
        setTexture(textureRegion);
    }
    
    public TextureActor(String name, Texture texture) {
        super(name);
        setTexture(texture);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor());
        if(isVisible()) {
            batch.draw(texture, getX(), getY(),
                       getOriginX(), getOriginY(), getWidth(), getHeight(),
                       getScaleX(), getScaleY(), getRotation());
        }
    }
    
    public void setTexture(Texture texture) {
        setTexture(new TextureRegion(texture));
    }
    
    public void setTexture(TextureRegion textureRegion) {
        this.texture = textureRegion;
        setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        setOrigin(textureRegion.getRegionWidth() / 2f, textureRegion.getRegionHeight() / 2f);
    }
    
}
