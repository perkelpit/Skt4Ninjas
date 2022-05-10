package com.company.skt.lib;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/** <pre> Extended Actor:
 * - boundary polygon w/ default ractangle-shape
 * - corresponding overlaps() methods
 * - update() via Updatable
 * - possibility to stop act(), but not update()
 * - solid setable (default: false) </pre>
 * */
public class BaseActor extends Actor implements InitializeUpdate, Named {
    
    private boolean initialized;
    private boolean stopped;
    private float opacity;
    
    {
        opacity = 1f;
    }
    
    public BaseActor(String name) {
        setName(name);
    }
    
    public void initialize(){
        initialized = true;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public void update(float delta) {
        InitializeUpdate.super.update(delta);
    }
    
    @Override
    public void act(float delta) {
        if(!stopped) {
            super.act(delta);
        }
    }
    
    public boolean isStopped() {
        return stopped;
    }
    
    public void setStopped(boolean b) {
        stopped = b;
    }
    
    public float getOpacity() {
        return opacity;
    }
    
    public Vector2 getCenter() {
        Vector2 center = new Vector2();
        center.x = getX() + (getWidth() / 2f);
        center.y = getY() + (getHeight() / 2f);
        return center;
    }
    
    public void setOpacity(float opacity) {
        this.opacity = MathUtils.clamp(opacity, 0f, 1f);
        Color color = getColor();
        setColor(color.r, color.g, color.b, opacity);
    }
}
