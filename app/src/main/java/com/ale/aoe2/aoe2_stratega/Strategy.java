package com.ale.aoe2.aoe2_stratega;

import java.io.File;

/**
 * Created by Ale on 29/02/2016.
 */
public class Strategy {
    public String name;
    public String author;
    public String civ;
    public String map;
    public String icon;
    public File strategyFile;

    public String _id;
    public String content;
    public String title_declared;
    public String version;
    public int created;
    public int stars;
    public int views;
    public int downloaded;
    public int intIcon;
    public String soup;
    public String xdab;

    public boolean hasStringIcon;



    public Strategy(String name,String author,String civ,
                    String map,String icon, String _id,String content,
                    String title_declared,String version,
                    int created,int stars,int views,int downloaded) {
        this.name = name.trim();
        this.author = author.trim();
        this.civ = civ.trim();
        this.map = map.trim();
        this.icon = icon.trim();
        this._id =_id;
        this.content = content;
        this.title_declared = title_declared.trim();
        this.version = version.trim();
        this.created = created;
        this.stars = stars;
        this.views = views;
        this.downloaded = downloaded;
        this.hasStringIcon = true;
    }

    public Strategy(String name, String author, String civ, String map, int intIcon, File strategyFile) {
        this.name = name.trim();
        this.author = author.trim();
        this.civ = civ.trim();
        this.map = map.trim();
        this.intIcon = intIcon;
        this.strategyFile = strategyFile;
        this.hasStringIcon = false;
    }
}
