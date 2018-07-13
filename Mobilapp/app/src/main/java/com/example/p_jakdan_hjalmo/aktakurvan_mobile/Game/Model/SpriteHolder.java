package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model;

import java.util.ArrayList;

/**
 * Created by Michael on 2018-01-06.
 */

public class SpriteHolder {

    private ArrayList<Sprite> sprites;

    public SpriteHolder() {
        sprites = new ArrayList<Sprite>();
    }

    public void addSprite(Sprite sprite){
        sprites.add(sprite);
    }

    public ArrayList<Sprite> getSprites() {
        return (ArrayList<Sprite>) sprites;
    }

    public void setSprites(ArrayList<Sprite> sprites) {
        this.sprites = sprites;
    }


    public Sprite getSprite(int index){
        return sprites.get(index);
    }
}
