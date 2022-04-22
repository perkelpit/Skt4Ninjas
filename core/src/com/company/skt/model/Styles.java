package com.company.skt.model;

import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public abstract class Styles {
    
    private static String path;
    
    public static void boot(String basePath) {
        Styles.path = basePath + "styles/";
    }
    
    public static void get(String widgetName) {
        String category;
        String subCategory = "";
        if (widgetName.contains("#")) {
            category = widgetName.substring(0, widgetName.indexOf("#"));
            subCategory = widgetName.substring(widgetName.indexOf("#") + 1);
        } else {
            category = widgetName;
        }
        switch(category) {
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
