package com.ale.aoe2.stratega;

/**
 * Created by Ale on 09/03/2016.
 */
public class Tip {
    private int exp; //noob, normal, pro
    private String tip;

    Tip(int exp, String tip) {
        this.exp = exp;
        this.tip = tip;
    }

    public int getExp() {
        return exp;
    }

    public String getTip() {
        return tip;
    }

}