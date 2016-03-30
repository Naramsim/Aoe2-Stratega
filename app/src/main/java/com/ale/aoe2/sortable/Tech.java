package com.ale.aoe2.sortable;

/**
 * Created by Ale on 09/03/2016.
 */
public class Tech {
    private String type; //noob, normal, pro
    private String tech;

    Tech(String type, String tech) {
        this.type = type;
        this.tech = tech;
    }

    public String getType() {
        return type;
    }

    public String getTech() {
        return tech;
    }

}