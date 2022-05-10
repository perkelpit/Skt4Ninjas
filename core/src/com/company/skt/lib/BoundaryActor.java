package com.company.skt.lib;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;

/** Inherits from {@link BaseActor} and is used by {@link BaseGroup} to provide overlapping and bouncing
 * funtionalities. <br>
 * at it´s core it creates a {@code standard rectangle} around the {@link BaseGroup} calculated from the sizes
 * of it´s childs. This reactangle is in every case used first to check if there may be/is an overlap to save
 * performance. A more precise {@link Polygon} may be added via {@link #setCustomBoundary(float[], float, float)}
 * to enhance the overlap check. */
public class BoundaryActor extends BaseActor {
    
    private Polygon boundary;
    private boolean standardRectAngle;
    private float refWidth;
    private float refHeight;
    private float offsetX;
    private float offsetY;
    
    {
        standardRectAngle = true;
        boundary = new Polygon();
    }
    
    public BoundaryActor(String name, BaseGroup parent) {
        super(name);
        boundary.setVertices(new float[]{
            0, 0, parent.getWidth(), 0,parent.getWidth(), parent.getHeight(), 0, parent.getHeight()});
    }
    
    @Override
    public void initialize() {
        super.initialize();
        if(standardRectAngle) {
            float[] vertices = new float[]{0, 0,                                             // bottom left
                                           getParent().getWidth(), 0,                        // bottom right
                                           getParent().getWidth(), getParent().getHeight(),  // top right
                                           0, getParent().getHeight()};                      // top left
            boundary.setVertices(vertices);
            boundary.setOrigin(getParent().getWidth() / 2, getParent().getHeight() / 2);
        } else {
            if(getParent().getWidth() != refWidth) {
                offsetX = (getParent().getWidth() - refWidth) / 2f;
            }
            if(getParent().getHeight() != refHeight) {
                offsetY = (getParent().getHeight() - refHeight) / 2f;
            }
            boundary.setOrigin(refWidth/2, refHeight/2);
        }
        updateBoundaryPosition();
        updateBoundaryRotation();
        updateBoundaryScale();
    }
    
    @Override
    public void update(float delta) {
        super.update(delta);
    }
    
    public void updateBoundaryPosition() {
        boundary.setPosition(getParent().getX() + offsetX, getParent().getY() + offsetY);
    }
    
    public void updateBoundaryRotation() {
        boundary.setRotation(getParent().getRotation());
    }
    
    public void updateBoundaryScale() {
        boundary.setScale(getParent().getScaleX(), getParent().getScaleY());
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
    }
    
    public void setCustomBoundary(float[] vertices, float refWidth, float refHeight) {
        standardRectAngle = false;
        this.refWidth = refWidth;
        this.refHeight = refHeight;
        boundary.setVertices(vertices);
        boundary.setPosition(getParent().getX(), getParent().getY());
        boundary.setRotation(getParent().getRotation());
        boundary.setScale(getParent().getScaleX(), getParent().getScaleY());
    }
    
    public Polygon getBoundary() {
        return boundary;
    }
    
}
