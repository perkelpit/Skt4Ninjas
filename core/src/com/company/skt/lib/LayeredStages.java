package com.company.skt.lib;


import com.badlogic.gdx.utils.Array;

public class LayeredStages extends UpdateStage {
    
    private Array<UpdateStage> stages;
    
    {
        stages = new Array<>();
    }

    public LayeredStages(String name) {
        super(name, true);
    }

    public Array<UpdateStage> getStages() {
        return stages;
    }
    
    public void addStage(UpdateStage stage) {
        stages.add(stage);
    }
    
    private void alignStages() {
        for(UpdateStage stage: stages) {
            if(stage instanceof AlignedStage) {
                ((AlignedStage)stage).alignCamera();
            }
        }
    }
    
    public void setCameraActor(MovedGroup cameraActor) {
        for(UpdateStage stage: stages) {
            if(stage instanceof AlignedStage) {
                ((AlignedStage)stage).setCameraActor(cameraActor);
            }
        }
    }
    
    @Override
    public void update(float delta) {
        for(UpdateStage stage : stages) {
            if (stage.isActive()) {
                stage.update(delta);
            }
        }
    }
    
    @Override
    public void act(float delta) {
        alignStages();
        for(UpdateStage stage : stages) {
            if (stage.isActive()) {
                stage.act(delta);
            }
        }
    }
    
    @Override
    public void draw() {
        for(UpdateStage stage : stages) {
            if (stage.isActive()) {
                stage.draw();
            }
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        for(UpdateStage stage : stages) {
            stage.dispose();
        }
    }
}
