package com.company.skt.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ScreenUtils;
import com.company.skt.model.Assets;
import com.company.skt.model.Utils;

public abstract class StageScreen implements Screen, InputProcessor, Initialize_Update {
    
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
    
    public abstract AssetManager getAssets();
    
    public void addStage(UpdateStage stage) {
        layeredStages.addStage(stage);
        if(inputMultiplexer != null) {
            inputMultiplexer.addProcessor(0, stage);
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
        for(UpdateStage stage : layeredStages.getStages()) {
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

    public UpdateStage findStage(String name){
        UpdateStage output = null;
        for (UpdateStage stage : layeredStages.getStages()){
            if(stage.getName().equals(name)){
                output = stage;
            }
        }
        return output;
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
