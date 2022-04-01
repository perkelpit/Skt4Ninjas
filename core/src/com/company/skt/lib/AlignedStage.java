package com.company.skt.lib;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class AlignedStage extends UpdateStage {
    
    MovedGroup cameraActor;
    Vector2 startPos;
    Vector2 shift;
    private float parallaxFactor;
    private boolean isMain;
    
    {
        startPos = new Vector2();
        shift = new Vector2(0, 0);
        parallaxFactor = 1;
    }

    public AlignedStage(String name) {
        super(name);
    }

    public void setParallaxFactor(float parallaxFactor) {
        this.parallaxFactor = parallaxFactor;
    }
    
    public float getParallaxFactor() {
        return parallaxFactor;
    }
    
    public void setCameraActor(MovedGroup movedGroup) {
        this.cameraActor = movedGroup;
        startPos.set(cameraActor.getX() + cameraActor.getOriginX(), cameraActor.getY() + cameraActor.getOriginY());
    }
    
    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }
    
    public boolean getIsMain() {
        return isMain;
    }
    
    @Override
    public void update(float delta) {
        super.update(delta);
        if(cameraActor != null) {
            shift.set((cameraActor.getX() + cameraActor.getOriginX() - startPos.x) * parallaxFactor,
                      (cameraActor.getY() + cameraActor.getOriginY() - startPos.y) * parallaxFactor);
            alignCamera();
        }
    }
    
    public void alignCamera() {
        Camera camera = getCamera();
        
        // center camera on actor
        camera.position.set(startPos.x + shift.x,
                            startPos.y + shift.y, 0);
        
        camera.update();
    }
    
}
