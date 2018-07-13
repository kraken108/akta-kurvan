package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.ViewModel;

import android.graphics.drawable.Drawable;

/**
 * Created by Jakob on 2018-01-06.
 */

public class ScreenInfo {
    private static final ScreenInfo ourInstance = new ScreenInfo();

    public static ScreenInfo getInstance() {
        return ourInstance;
    }

    private int width;
    private int height;
    private Drawable enemyDrawable;
    private Drawable playerDrawable;

    private ScreenInfo() {
        width = -1;
        height = -1;
    }

    public Drawable getPlayerDrawable() {
        return playerDrawable;
    }

    public void setPlayerDrawable(Drawable playerDrawable) {
        this.playerDrawable = playerDrawable;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Drawable getEnemyDrawable() {
        return enemyDrawable;
    }

    public void setEnemyDrawable(Drawable enemyDrawable) {
        this.enemyDrawable = enemyDrawable;
    }
}
