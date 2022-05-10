package com.company.skt.lib;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** A subclass to {@link BaseActor} to show an {@link Animation}*/
public class AnimationActor extends BaseActor {
    
    private Animation<TextureRegion> animation;
    
    private float elapsedTime;
    private boolean animationPaused;
    
    {
        elapsedTime = 0;
    }
    
    public AnimationActor(String name) {
        super(name);
    }
    
    
    public AnimationActor(String name, Animation<TextureRegion> animation) {
        super(name);
        setAnimation(animation);
    }
    
    public AnimationActor(String name, TextureAtlas atlas, String regionName, Animation.PlayMode playMode) {
        super(name);
        setAnimation(atlas, regionName, playMode);
    }
    
    @Override
    public void update(float delta) {
    
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
        if(!isStopped()) {
            if(animation != null && !animationPaused) {
                elapsedTime += delta;
            }
        }
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor());
        if(isVisible()) {
            batch.draw(animation.getKeyFrame(elapsedTime), getX(), getY(),
                       getOriginX(), getOriginY(), getWidth(), getHeight(),
                       getScaleX(), getScaleY(), getRotation());
        }
    }
    
    public void setAnimation(TextureAtlas atlas, String regionName, Animation.PlayMode playMode) {
        setAnimation(new Animation<>(0.066f, atlas.findRegions(regionName), playMode));
    }
    
    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
        setSize(animation.getKeyFrame(0).getRegionWidth(),
                animation.getKeyFrame(0).getRegionHeight());
        setOrigin(animation.getKeyFrame(0).getRegionWidth() / (float)2,
                  animation.getKeyFrame(0).getRegionHeight() / (float)2);
    }
    
    public Animation<TextureRegion> getAnimation() {
        return animation;
    }
    
    public void setAnimationPaused(boolean b) {
        animationPaused = b;
    }
    
    public boolean isAnimationFinished() {
        return animation.isAnimationFinished(elapsedTime);
    }
    
}
