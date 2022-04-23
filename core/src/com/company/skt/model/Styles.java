package com.company.skt.model;

import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public abstract class Styles {
    
    private static String path;
    
    public static void boot(String basePath) {
        Styles.path = basePath + "styles/";
    }
    
    public static void get(String widgetName) {
        String mainCase;
        String subCase;
        if (widgetName.contains("#")) {
            mainCase = widgetName.substring(0, widgetName.indexOf("#"));
            subCase = widgetName.substring(widgetName.indexOf("#") + 1);
        } else {
            mainCase = widgetName;
            subCase = "";
        }
        switch(mainCase) {
            case "Label":
                
                break;
        }
    }
    
    private LabelStyle newLabelStyle() {
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = Fonts.get("PirataOne-Regular_Welcome");
        return labelStyle;
    }
    
}
