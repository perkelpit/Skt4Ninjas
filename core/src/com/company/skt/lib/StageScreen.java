package com.company.skt.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ScreenUtils;

public abstract class StageScreen implements Screen, InputProcessor {
    
    protected LayeredStages stages;
    protected boolean stop;
    
    public StageScreen() {
        stages = new LayeredStages();
        create();
    }
    
    
    public abstract void create();
    
    public abstract void update(float dt);
    
    public abstract AssetManager getAssets();
    
    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = (InputMultiplexer)Gdx.input.getInputProcessor();
        inputMultiplexer.addProcessor(this);
        for(int i = stages.getStages().size - 1; i >= 0; i-- ) {
            inputMultiplexer.addProcessor(stages.getStages().get(i));
        }
        Utils.setCurrentScreen(this);
    }
    
    @Override
    public void hide() {
        InputMultiplexer inputMultiplexer = (InputMultiplexer)Gdx.input.getInputProcessor();
        inputMultiplexer.removeProcessor(this);
        for(UpdateStage stage : stages.getStages()) {
            inputMultiplexer.removeProcessor(stage);
        }
    }
    
    public void render(float delta) {
        stages.update(delta);
        update(delta);
        
        if(!stop) {
            stages.act(delta);
        }
        
        ScreenUtils.clear(1, 0, 0, 1, false);
        
        stages.draw();
    }
    

    @Override
    public void dispose() {
        stages.dispose();
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
