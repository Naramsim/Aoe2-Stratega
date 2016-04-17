package com.ale.aoe2.sortable;

import android.content.Context;

/**
 * Created by Ale on 24/02/2016.
 */
public class Step {
    private String title;
    private String subtitle;
    private String img;
    private Context context;

    Step(String title, String img, String subtitle, Context context) {
        this.title = title;
        this.subtitle = subtitle;
        this.img = img;
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getImg() {
        int toReturn = context.getResources().getIdentifier(img, "drawable", context.getPackageName());
        return toReturn;
    }
}
