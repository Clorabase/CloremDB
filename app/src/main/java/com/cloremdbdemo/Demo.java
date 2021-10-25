package com.cloremdbdemo;

public class Demo {
    //A variable which will act as primary key for our database, it is our responsibility to keep it unique
    private String id;
    private String demoDescription;

    public Demo(String id, String demoDescription) {
        this.id = id;
        this.demoDescription = demoDescription;
    }

    public String getDemoDescription() {
        return demoDescription;
    }

    public void setDemoDescription(String demoDescription) {
        this.demoDescription = demoDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
