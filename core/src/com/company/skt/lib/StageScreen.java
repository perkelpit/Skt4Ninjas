package com.company.skt.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.company.skt.controller.Utils;
import com.company.skt.model.Assets;

public abstract class StageScreen implements Screen, InputProcessor, Initialize_Update, EventClickHandler {
    
    private LayeredStages layeredStages;
    private InputMultiplexer inputMultiplexer;
    private boolean initialized;
    protected boolean stop;
    
    public StageScreen() {
        layeredStages = new LayeredStages("layeredStages");
    }
    
    public void initialize(){
        initialized = true;
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public void update(float delta) {
        Initialize_Update.super.update(delta);
    }

    /** disposes and removes Stage by name from stagesArray */
    public void removeStage(String stageName){
        boolean found = false;
        UpdateStage stage;
        for (int i = 0; i < layeredStages.getStages().size; i++) {
            stage = layeredStages.getStages().get(i);
            if (stageName.equals(stage.getName())){
                layeredStages.removeStage(stage);
                found = true;
            }
        }
        if (!found){
            System.out.println("removeStage() No such stage found: " + stageName);
        }
    }
    
    public void removeStage(UpdateStage stage){
        layeredStages.removeStage(stage);
    }
    
    public void addStage(UpdateStage stage) {
        layeredStages.addStage(stage);
        if(stage.isActive()) {
            if(inputMultiplexer != null) {
                inputMultiplexer.addProcessor(0, stage);
            }
        }
    }
    
    @Override
    public void show() {
        inputMultiplexer = (InputMultiplexer)Gdx.input.getInputProcessor();
        inputMultiplexer.addProcessor(this);
        for(int i = layeredStages.getStages().size - 1; i >= 0; i-- ) {
            inputMultiplexer.addProcessor(layeredStages.getStages().get(i));
        }
        Utils.setCurrentScreen(this);
        Assets.setCurrentScreen(this);
    }
    
    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(this);
        UpdateStage stage;
        for (int i = 0; i < layeredStages.getStages().size; i++) {
            stage = layeredStages.getStages().get(i);
            inputMultiplexer.removeProcessor(stage);
        }
        inputMultiplexer = null;
        Utils.setCurrentScreen(null);
        Assets.setCurrentScreen(null);
        dispose();
    }
    
    public void render(float delta) {
        layeredStages.update(delta);
        update(delta);
        
        if(!stop) {
            layeredStages.act(delta);
        }
        
        ScreenUtils.clear(0, 0, 0, 1, false);
        
        layeredStages.draw();
    }

    public void setStageActive(String name, boolean active) {
        UpdateStage stage = findStage(name);
        setStageActive(stage, active);
    }
    
    public void setStageActive(UpdateStage stage, boolean active) {
        if(stage != null) {
            if(active && !stage.isActive()) {
                stage.setActive(true);
                inputMultiplexer.addProcessor(0, stage);
            }
            if(!active && stage.isActive()) {
                stage.setActive(false);
                inputMultiplexer.removeProcessor(stage);
            }
        }
    }
    
    public UpdateStage findStage(String name){
        UpdateStage stage;
        for (int i = 0; i < layeredStages.getStages().size; i++) {
            stage = layeredStages.getStages().get(i);
            if(stage.getName().equals(name)){
                return stage;
            }
        }
        return null;
    }
    
    @Override
    public void buttonClicked(String click) {
    
    }
    
    @Override
    public void event(String event) {
    
    }

    @Override
    public void dispose() {
        layeredStages.dispose();
    }
    
    
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }
    
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
    
    @Override
    public void resize(int width, int height) {
    
    }
    
    @Override
    public void pause() {
    
    }
    
    @Override
    public void resume() {
    
    }
    
}
