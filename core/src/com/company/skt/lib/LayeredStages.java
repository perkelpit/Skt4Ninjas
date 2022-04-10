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

    /**
     * disposes and removes Stage by name from stagesArray
     */
    public void removeStage(UpdateStage stage) {
        stages.removeValue(stage, true);
        stage.dispose();
    }

    private void alignStages() {
        UpdateStage stage = null;
        for (int i = 0; i < stages.size; i++) {
            stage = stages.get(i);
            if (stage instanceof AlignedStage) {
                ((AlignedStage)stage).alignCamera();
            }
        }
    }

    public void setCameraActor(MovedGroup cameraActor) {
        UpdateStage stage = null;
        for (int i = 0; i < stages.size; i++) {
            stage = stages.get(i);
            if (stage instanceof AlignedStage) {
                ((AlignedStage)stage).setCameraActor(cameraActor);
            }
        }
    }

    @Override
    public void update(float delta) {
        UpdateStage stage = null;
        for (int i = 0; i < stages.size; i++) {
            stage = stages.get(i);
            if (stage.isActive()) {
                stage.update(delta);
            }
        }
    }

    @Override
    public void act(float delta) {
        alignStages();
        UpdateStage stage = null;
        for (int i = 0; i < stages.size; i++) {
            stage = stages.get(i);
            if (stage.isActive()) {
                stage.act(delta);
            }
        }
    }

    @Override
    public void draw() {
        UpdateStage stage = null;
        for (int i = 0; i < stages.size; i++) {
            stage = stages.get(i);
            if (stage.isActive()) {
                stage.draw();
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        UpdateStage stage = null;
        for (int i = 0; i < stages.size; i++) {
            stage = stages.get(i);
            stage.dispose();

        }
    }
}
