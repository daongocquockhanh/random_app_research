package com.bosch.random_app.controller;

import com.bosch.random_app.model.AppModel;

public class MainController {
    private AppModel appModel;
    public void setAppModel(AppModel appModel){
        this.appModel = appModel;
    }
    public String handleRandomStringRequest() {
        return appModel.getRandomOption();
    }
}
