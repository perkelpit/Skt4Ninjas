package com.company.skt.view;

import com.badlogic.gdx.graphics.Texture;
import com.company.skt.lib.TextureActor;
import com.company.skt.lib.UpdateStage;
import com.company.skt.model.Assets;

public class MenuBackground extends UpdateStage {

    public MenuBackground(String name) {
        super(name);
    }
    
    public MenuBackground(String name, boolean active) {
        super(name, active);
    }

    @Override
    public void initialize() {
        super.initialize();
        addActor(new TextureActor("Background", Assets.<Texture>get("Background.png")));
    }
}
