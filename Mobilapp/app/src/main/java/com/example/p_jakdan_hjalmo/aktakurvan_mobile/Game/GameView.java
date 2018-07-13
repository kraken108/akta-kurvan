package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.GameController.Direction;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.GameController.GameState;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.GameModel;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.Sprite;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.Model.SpriteHolder;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.Game.ViewModel.ScreenInfo;
import com.example.p_jakdan_hjalmo.aktakurvan_mobile.R;

import java.util.ArrayList;

public class GameView extends SurfaceView implements
        SurfaceHolder.Callback {

    private SpriteHolder spriteHolder;

    private ArrayList<Sprite> theSprites;
    private int initCounter = 0;
    private final float playerSpeed = 7;
    private final float playerSpeedTurn = playerSpeed * 3 / 4;

    //	private Canvas canvas;

    private SurfaceHolder holder;
    private boolean hasSurface;
    public final int X_RESOLUTION, Y_RESOLUTION;


    private Direction direction;


    private GraphicsThread graphicsThread;
    private Handler handler = new Handler();
    private Context context;
    private int state;
    private long timeStamp;

    private Sprite apple;
    private Boolean appleEaten;
    private final Drawable appleSprite;

    private Sprite player;


    public GameView(Context context, int xRes, int yRes) {
        super(context);
        this.context = context;

        holder = getHolder();
        holder.addCallback(this);
        hasSurface = false;
        spriteHolder = GameModel.getInstance().getSpriteHolder();

        timeStamp = System.currentTimeMillis();

        theSprites = new ArrayList<>();

        X_RESOLUTION = xRes;
        Y_RESOLUTION = yRes;
        ScreenInfo.getInstance().setHeight(yRes);
        ScreenInfo.getInstance().setWidth(xRes);


        player = GameModel.getInstance().getHead();

      //  if (initCounter == 0)
        initGame();

        appleEaten = false;
        appleSprite = context.getResources().getDrawable(R.drawable.apple);
        apple = new Sprite(0,0);
        apple.spawnRandom(X_RESOLUTION-50,Y_RESOLUTION-50);


    }


    public void initGame() {
        //clear at game over
        initCounter++;
        float startX = GameModel.getInstance().getStartX();
        float startY = GameModel.getInstance().getStartY();

        player.setPosition(X_RESOLUTION * startX, Y_RESOLUTION * startY);

        player.setVelocity(0, playerSpeed);
        player.move();

        state = 1;
        setFocusable(true);
        requestFocus();
    }

    public void stateCorrector() {
        if (state <= 0)
            state = 8;

        if (state >= 9)
            state = 1;
    }

    protected void move() {

        stateHandler(state);
        stateCorrector();
        Float floatVal = new Float(0.7);
        //Log.i("STATE: ", direction.toString());
        if (direction == Direction.UP) {
            player.setVelocity(0, playerSpeed * -1);  //UP
            //	player.setImage(upbluesprite);
        }
        if (direction == Direction.RIGHTUP) {
            player.setVelocity(playerSpeed * floatVal, playerSpeed * -1);  //RIGHTUP
            //	player.setImage(uprightbluesprite);
        }
        if (direction == Direction.RIGHT) {
            player.setVelocity(playerSpeed, 0);  //RIGHT
            //	player.setImage(uprightbluesprite);
        }
        if (direction == Direction.RIGHTDOWN) {
            player.setVelocity(playerSpeed * floatVal, playerSpeed * floatVal); //RIGHTDOWN
            //	player.setImage(rightdownbluesprite);
        }
        if (direction == Direction.DOWN) {
            player.setVelocity(0, playerSpeed); //DOWN
            //	player.setImage(downbluesprite);
        }
        if (direction == Direction.LEFTDOWN) {
            player.setVelocity((playerSpeed * floatVal) * -1, playerSpeed * floatVal);  //LEFTDOWN
            //	player.setImage(leftdownbluesprite);
        }
        if (direction == Direction.LEFT) {
            player.setVelocity(playerSpeed * -1, 0); //LEFT
            //	player.setImage(leftbluesprite);
        }
        if (direction == Direction.LEFTUP) {
            player.setVelocity((playerSpeed * floatVal) * -1, (playerSpeed * floatVal) * -1); //LEFTUP

            //	player.setImage(leftupbluesprite);
        }
        player.move();
    }

    public void stateHandler(int state) {

        switch (state) {
            case 1:
                direction = Direction.UP;
                player.setDirection(direction.UP);
                break;
            case 2:
                direction = Direction.RIGHTUP;
                player.setDirection(direction.RIGHTUP);
                break;
            case 3:
                direction = Direction.RIGHT;
                player.setDirection(direction.RIGHT);
                break;
            case 4:
                direction = Direction.RIGHTDOWN;
                player.setDirection(direction.RIGHTDOWN);
                break;
            case 5:
                direction = Direction.DOWN;
                player.setDirection(direction.DOWN);
                break;
            case 6:
                direction = Direction.LEFTDOWN;
                player.setDirection(direction.LEFTDOWN);
                break;
            case 7:
                direction = Direction.LEFT;
                player.setDirection(direction.LEFT);
                break;
            case 8:
                direction = Direction.LEFTUP;
                player.setDirection(direction.LEFTUP);
                break;
        }
    }

    public void spawnApple(){
        if(appleEaten){
            apple.spawnRandom(X_RESOLUTION-50, Y_RESOLUTION-50);
            appleEaten=false;
        }
    }

    public void resume() {
        if (graphicsThread == null) {
            Log.i("BounceSurfaceView", "resume");
            graphicsThread = new GraphicsThread(this, 20); // 20 ms between updates
            if (hasSurface) {
                graphicsThread.start();
            }
        }
    }

    public void pause() {
        if (graphicsThread != null) {
            Log.i("BounceSurfaceView", "pause");
            graphicsThread.requestExitAndWait();
            graphicsThread = null;
        }
    }


    public void collisionDetected() {

        Drawable playerHead = ScreenInfo.getInstance().getPlayerDrawable();
        int width = playerHead.getIntrinsicWidth() / 2;
        int height = playerHead.getIntrinsicHeight() / 2;
        int playerX = Math.round(player.getX());
        int playerY = Math.round(player.getY());
        playerHead.setBounds(playerX - width, playerY - height, playerX + width, playerY + height);


        Rect pRect = playerHead.getBounds();
        ///Outside screen

        if (pRect.right > X_RESOLUTION) {
            GameModel.getInstance().setGameState(GameState.GAMEOVER);
            return;
        }

        //		Log.i("outside screen > X", "outside screen > X");

        if (pRect.left < 0) {
            GameModel.getInstance().setGameState(GameState.GAMEOVER);
            return;
        }

        //		Log.i("outside screen < 0", "outside screen <X");

        if (pRect.bottom > Y_RESOLUTION) {
            GameModel.getInstance().setGameState(GameState.GAMEOVER);
            return;
        }

        //	Log.i("outside screen >Y", "outside screen >Y");

        if (pRect.top < 0) {
            GameModel.getInstance().setGameState(GameState.GAMEOVER);
            return;
        }

        ArrayList<Sprite> sprites = GameModel.getInstance().getSpriteHolder().getSprites();


        Drawable enemyDrawable = ScreenInfo.getInstance().getPlayerDrawable();

        int pTop = pRect.top;
        int pBot = pRect.bottom;
        int pLeft = pRect.left;
        int pRight = pRect.right;

        for (int i = 0; i < sprites.size(); i++) {
            int enemyX = Math.round(sprites.get(i).getX());
            int enemyY = Math.round(sprites.get(i).getY());
            enemyDrawable.setBounds(enemyX - width, enemyY - height, enemyX + width, enemyY + height);

            Rect oRect = enemyDrawable.getBounds();
            if (i < (sprites.size() - 10)) { //cant crash with last 10 elements
                if (pTop > oRect.bottom || pRight < oRect.left || pLeft > oRect.right
                        || pBot < oRect.top) {
                } else {
                    System.out.println("collided with: top:" + oRect.top + " bot: " + oRect.bottom + " left: " + oRect.left + " right: " + oRect.right);
                    System.out.println("crashed with sprite nr: " + i);
                    GameModel.getInstance().setGameState(GameState.GAMEOVER);
                    return;
                }
            }


            /// OM SINGLEPLAYER

            if(!GameModel.getInstance().isMultiplayer()){
                int appleW = appleSprite.getIntrinsicWidth() / 2;
                int appleH = appleSprite.getIntrinsicHeight() / 2;
                int appleX = Math.round(apple.getX());
                int appleY = Math.round(apple.getY());

                appleSprite.setBounds(appleX - appleW, appleY - appleH, appleX + appleW, appleY + appleH);
                Rect appleR = appleSprite.getBounds();

                if (pTop > appleR.bottom || pRight < appleR.left || pLeft > appleR.right
                        || pBot < appleR.top) {

                }else{
                    appleEaten = true;
                }
            }


        }

        int i = 0;
        GameModel.getInstance().setSpritesToAddLock(true);
        for (Sprite s : GameModel.getInstance().getSpritesToAdd()) {
            int xPos = Math.round(s.getX());
            int yPos = Math.round(s.getY());
            enemyDrawable.setBounds(xPos - width, yPos - height, xPos + width, yPos + height);
            Rect oRect = enemyDrawable.getBounds();

            if (pTop > oRect.bottom || pRight < oRect.left || pLeft > oRect.right
                    || pBot < oRect.top) {
            } else {
                System.out.println("collided with: top:" + oRect.top + " bot: " + oRect.bottom + " left: " + oRect.left + " right: " + oRect.right);
                System.out.println("crashed with sprite nr: " + i);
                GameModel.getInstance().setGameState(GameState.GAMEOVER);
                return;
            }

            i++;
        }
        GameModel.getInstance().setSpritesToAddLock(false);





    }


    public void addTail() {


        //int arraySize = GameModel.getInstance().getSpriteHolder().getSprites().size();
        // String sizeString = String.valueOf(arraySize);

        Sprite tailSprite = new Sprite(player.getX(), player.getY());
        GameModel.getInstance().getSpriteHolder().getSprites().add(tailSprite);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int halfX = X_RESOLUTION / 2;
        int x = (int) event.getX();
        int y = (int) event.getY();

//        Rect rightButton = new Rect(rightArrow.getIconBounds());
//        Rect leftButton = new Rect(leftArrow.getIconBounds());


        if (x >= halfX) {
            long now = System.currentTimeMillis();
            if (now > timeStamp + 200) {
                state = state + 1;
                timeStamp = now;
                if (state >= 9 || state <= 0) {
                    stateCorrector();
                    Log.i("Turned Right", "Turned Right");
                }
            }
        }

        if (x < halfX) {
            long now = System.currentTimeMillis();
            if (now > timeStamp + 200) {
                state = state - 1;
                timeStamp = now;
                if (state >= 9 || state <= 0) {
                    stateCorrector();
                    Log.i("Turned Left", "Turned Left");
                }
            }
        }
        return true;
    }

    protected void draw() {

        Canvas canvas = new Canvas();

        canvas = holder.lockCanvas();
        {


            int oneFifthX = X_RESOLUTION / 5;
            int oneFifthY = Y_RESOLUTION / 5;

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);

            if (paint == null)
                Log.i("PAINT ÄR NULL", "PAINT ÄR NULL");

            if (canvas == null) {
                Log.i("Canvas är null", "CANVAS ÄR NULL");
                canvas = new Canvas();
            }


            Rect playScreen = new Rect();
            playScreen.set(0, 0, X_RESOLUTION, Y_RESOLUTION);
            canvas.drawRect(playScreen, paint);


            Drawable sprite = ScreenInfo.getInstance().getPlayerDrawable();
            int width = sprite.getIntrinsicWidth() / 2;
            int height = sprite.getIntrinsicHeight() / 2;

            paint.setColor(Color.BLUE);

            for (Sprite s : GameModel.getInstance().getSpriteHolder().getSprites()) {
                int xPos = Math.round(s.getX());
                int yPos = Math.round(s.getY());
                sprite.setBounds(xPos - width, yPos - height, xPos + width, yPos + height);
                Rect rect = sprite.getBounds();

                if (s != null) {
                    canvas.drawRect(rect, paint);
                    // s.draw(canvas);
                }
            }

            paint.setColor(Color.RED);

            sprite = ScreenInfo.getInstance().getEnemyDrawable();

            GameModel.getInstance().setSpritesToAddLock(true);
            for (Sprite s : GameModel.getInstance().getSpritesToAdd()) {
                int xPos = Math.round(s.getX());
                int yPos = Math.round(s.getY());
                sprite.setBounds(xPos - width, yPos - height, xPos + width, yPos + height);
                Rect rect = sprite.getBounds();

                if (s != null) {
                    canvas.drawRect(rect, paint);
                    // s.draw(canvas);
                }
            }
            GameModel.getInstance().setSpritesToAddLock(false);



            /// OM SINGLEPLAYER

            if(!GameModel.getInstance().isMultiplayer()){
                int appleW = appleSprite.getIntrinsicWidth() / 2;
                int appleH = appleSprite.getIntrinsicHeight() / 2;
                int appleX = Math.round(apple.getX());
                int appleY = Math.round(apple.getY());
                appleSprite.setBounds(appleX - appleW, appleY - appleH, appleX + appleW, appleY + appleH);
                appleSprite.draw(canvas);
            }


        }
        if (canvas != null) {
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (graphicsThread == null) {
            graphicsThread = new GraphicsThread(this, 100);
            graphicsThread.start();
            hasSurface = true;
        } else {
            graphicsThread.start();
            hasSurface = true;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
        pause();
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (graphicsThread != null) {
            graphicsThread.onWindowResize(w, h);
        }
    }


}