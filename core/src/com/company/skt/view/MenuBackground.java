package com.company.skt.view;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.company.skt.lib.TextureActor;
import com.company.skt.lib.UpdateStage;
import com.company.skt.lib.Utils;
import com.company.skt.model.Assets;
import com.company.skt.model.Settings;

import java.util.Properties;

public class MenuBackground extends UpdateStage {
    
    @Override
    public void initialize() {
        super.initialize();
        Properties appCfg = Settings.getProperties(Settings.APP);
        addActor(new TextureActor(
            "Background", Assets.getAssets().<Texture>get("assets/Menu/art/Background.png")));
    }
}
