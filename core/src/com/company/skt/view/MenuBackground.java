package com.company.skt.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.company.skt.lib.ActorBundle;
import com.company.skt.lib.TextureActor;
import com.company.skt.lib.UpdateStage;
import com.company.skt.model.Assets;

public class MenuBackground extends UpdateStage {
    
    @Override
    public void initialize() {
        super.initialize();
        addActor(new TextureActor(
            "Background", Assets.getAssets().<Texture>get("assets/art/menu/Background.png")));
    }
}
