package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model;

import android.graphics.drawable.Drawable;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.GameController.Direction;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.GameController.GameState;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.ViewModel.ScreenInfo;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jakob on 2018-01-06.
 */

public class GameModel {
    private static final GameModel ourInstance = new GameModel();

    public static GameModel getInstance() {
        return ourInstance;
    }


    private boolean isMultiplayer;

    private SpriteHolder spriteHolder;
    private Sprite head;
    private boolean isActive;

    private boolean spritesToAddLock;

    private List<Sprite> spritesToAdd;
    private float startX, startY;
    private Direction direction;
    private Integer state;

    private GameState gameState;

    private GameModel() {
        state = 1;
        isMultiplayer = false;
        spriteHolder = new SpriteHolder();
        startX = 0.5F;
        startY = 0.5F;
        direction = Direction.UP;
        gameState = GameState.RUNNING;
        head = new Sprite();
        isActive = true;
        //spritesToAdd = new ArrayList<>();
        spritesToAdd = Collections.synchronizedList(new ArrayList<Sprite>());

        spritesToAddLock = false;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        isMultiplayer = multiplayer;
    }

    public boolean isSpritesToAddLock() {
        return spritesToAddLock;
    }

    public void setSpritesToAddLock(boolean spritesToAddLock) {
        this.spritesToAddLock = spritesToAddLock;
    }

    public void clearModel(){
        state = 1;
        direction = Direction.UP;
        spriteHolder = new SpriteHolder();
        startX = 0.5F;
        startY = 0.5F;
        gameState = GameState.RUNNING;
        head = new Sprite();
        isActive = true;
        spritesToAdd = Collections.synchronizedList(new ArrayList<Sprite>());
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public synchronized List<Sprite> getSpritesToAdd() {
        return spritesToAdd;
    }

    public synchronized SpriteHolder getSpriteHolder() {
        return spriteHolder;
    }

    public boolean isActive() {
        return isActive;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setSpriteHolder(SpriteHolder spriteHolder) {
        this.spriteHolder = spriteHolder;
    }

    public synchronized Sprite getHead() {
        return head;
    }

    public void setHead(Sprite head) {
        this.head = head;
    }


    public void savePositions() {
        int width = ScreenInfo.getInstance().getWidth();
        int height = ScreenInfo.getInstance().getHeight();

        head.setPosition(head.getX()/width,head.getY()/height);
        startX = head.getX()/width;
        startY = head.getY()/height;
        for(Sprite s : spriteHolder.getSprites()){
            s.setPosition(s.getX()/width,s.getY()/height);
        }

        for(Sprite s : spritesToAdd){
            s.setPosition(s.getX()/width,s.getY()/height);
        }


    }

    public void loadPositions(){

        int width = ScreenInfo.getInstance().getWidth();
        int height = ScreenInfo.getInstance().getHeight();

        head.setPosition(head.getX()*width,head.getY()*height);

        for(Sprite s : spriteHolder.getSprites()){
            s.setPosition(s.getX()*width,s.getY()*height);
        }

        for(Sprite s : spritesToAdd){
            s.setPosition(s.getX()*width,s.getY()*height);
        }
    }

}
