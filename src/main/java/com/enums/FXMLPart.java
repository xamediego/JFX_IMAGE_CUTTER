package com.enums;

public enum FXMLPart {

    SNAPSHOTTER {
        public String getPath() {
            return "/FxmlFiles/SnapShotter.fxml";
        }
    },
    TITLEBAR{
        public String getPath(){
            return "/FxmlFiles/TitleBar.fxml";
        }
    };


    ;

    public abstract String getPath();
}
