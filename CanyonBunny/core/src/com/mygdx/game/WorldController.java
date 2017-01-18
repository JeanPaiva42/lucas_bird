package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.BunnyHead.JUMP_STATE;


/**
 * Created by LucasRezende on 07/11/2016.
 */
public class WorldController  extends InputAdapter{

    private static final String TAG = WorldController.class.getName();

    public CameraHelper camera;
    public Level level;
    public int lives;
    public int score;
    public  int selectedSprite;
    public Sprite[] testSprites;
    private float timeLeftGameOverDelay;

    //retangulos para detectar colisões
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();

    public WorldController(){
        init();
    }

    private void init(){
        Gdx.input.setInputProcessor(this);
        camera = new CameraHelper();
        lives = Constants.LIVES_START;
      //  initTestObjects();
        timeLeftGameOverDelay = 0;
        initLevel();
    }

    private void initLevel(){
        score = 0;
        level = new Level(Constants.LEVEL_01);
        camera.setTarget(level.bunnyHead);
    }

    public  void update(float delta){
        handleDebugInput(delta);
        if (isGameOver()) {
            timeLeftGameOverDelay -= delta;
            if (timeLeftGameOverDelay < 0) init();
        }else {
            handleInputGame(delta);
        }
        level.update(delta);
        testCollisions();
        camera.update(delta);
        if (!isGameOver() && isPlayerInWater()) {
            lives--;
            if (isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            else
                initLevel();
        }
      //  updateTestObjects(delta);
    }

    private void onCollisionBunnyHeadWithRock(Rock rock){
        BunnyHead bunnyHead = level.bunnyHead;
        float heightDifference = Math.abs(bunnyHead.position.y - ( rock.position.y + rock.bounds.height));
        if (heightDifference > 0.25f) {
            boolean hitLeftEdge = bunnyHead.position.x > ( rock.position.x + rock.bounds.width / 2.0f);
            if (hitLeftEdge) {
                bunnyHead.position.x = rock.position.x + rock.bounds.width;
            } else {
                bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
            }
            return;
           }
        switch (bunnyHead.jumpState) {
            case GROUNDED:
                break;
            case FALLING:
            case JUMP_FALLING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                bunnyHead.jumpState = JUMP_STATE.GROUNDED;
                break;
            case JUMP_RISING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                break;
        }
    }

    private void onCollisionBunnyHeadWithGoldCoin(GoldCoin goldCoin){
        goldCoin.collected = true;
        score += goldCoin.getScore();
        Gdx.app.log(TAG, "Gold coin collected");
    }

    private void onCollisionBunnyHeadWithFeather(Feather feather) {
        feather.collected = true;
        score += feather.getScore();
        level.bunnyHead.setFeatherPowerup(true);
        Gdx.app.log(TAG, "Feather collected");
    }

    private void testCollisions () {
        r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y,
                level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);

        for (Rock rock : level.rocks) {
            r2.set(rock.position.x, rock.position.y,
                    rock.bounds.width, rock.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyHeadWithRock(rock);
            // IMPORTANT: must do all collisions for valid
            // edge testing on rocks.
        }

        for (GoldCoin goldcoin : level.goldcoins) {
            if (goldcoin.collected) continue;
            r2.set(goldcoin.position.x, goldcoin.position.y,
                    goldcoin.bounds.width, goldcoin.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyHeadWithGoldCoin(goldcoin);
            break;
        }

        for (Feather feather : level.feathers) {
            if (feather.collected) continue;
            r2.set(feather.position.x, feather.position.y,
                    feather.bounds.width, feather.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyHeadWithFeather(feather);
            break;
        }
    }

    public boolean isGameOver () {
        return lives < 0;
    }
    public boolean isPlayerInWater () {
        return level.bunnyHead.position.y < -5;
    }

    private void handleInputGame (float deltaTime) {
        if (camera.hasTarget(level.bunnyHead)) {
            // movimento
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
            } else {
                // Execute auto-forward movement on non-desktop platform
                if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
                    level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
                }
            }
            // salto
            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                level.bunnyHead.setJumping(true);
            } else {
                level.bunnyHead.setJumping(false);
            }
        }
    }


    private void initTestObjects() {
        testSprites = new Sprite[5];
       Array<TextureRegion> regions = new Array<TextureRegion>();
        regions.add(Assets.instance.bunny.head);
        regions.add(Assets.instance.rock.edge);
        regions.add(Assets.instance.rock.middle);
        regions.add(Assets.instance.goldCoin.goldCoin);
        regions.add(Assets.instance.levelDecoration.mountainRight);
        regions.add(Assets.instance.levelDecoration.mountainLeft);
        for (int i = 0; i < testSprites.length; i++) {
            Sprite spr = new Sprite(regions.random());
            spr.setSize(1, 1);
            spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
            float randomX = MathUtils.random(-2.0f, 2.0f);
            float randomY = MathUtils.random(-2.0f, 2.0f);
            spr.setPosition(randomX, randomY);
            testSprites[i] = spr;
        }
        selectedSprite = 0;
    }

    private void updateTestObjects(float deltaTime) {
        float rotation = testSprites[selectedSprite].getRotation();
        rotation += 90 * deltaTime;
        rotation %= 360;
        testSprites[selectedSprite].setRotation(rotation);
    }

///////////////////////////////////Classes de debug/////////////////////////////////////


    private void handleDebugInput(float delta) {
     if(Gdx.app.getType() != Application.ApplicationType.Desktop)
         return;

     // controle da camera
        float camMovSpeed = 5 * delta;
        float camMovSpeedAcelerate = 5;
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            camMovSpeed *= camMovSpeedAcelerate;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)){
            camera.setPosition(0,0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            moveCamera(-camMovSpeed,0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            moveCamera(camMovSpeed,0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            moveCamera(0,camMovSpeed);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            moveCamera(0,-camMovSpeed);
        }
        //zoom
        float camZoomSpeed = 1* delta;
        float camZoomSpeedAcelerate = 5;
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            camZoomSpeed *= camZoomSpeedAcelerate;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)){
           camera.setZoom(1);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Z)){
            camera.addZoom(camZoomSpeed);
            Gdx.app.debug(TAG,"ZoomIn");
        }
        if(Gdx.input.isKeyPressed(Input.Keys.X)){
            camera.addZoom(-camZoomSpeed);
            Gdx.app.debug(TAG,"ZoomOut");
        }

    }

    private void  moveCamera(float x , float y){
        x += camera.getPosition().x;
        y += camera.getPosition().y;
        camera.setPosition(x, y);
    }

    public boolean keyUp(int keycode){//reseta o world
        if(keycode == Input.Keys.R){
            init();
            Gdx.app.debug(TAG,"Game world resetado");
        }
        // Toggle camera follow
        else if (keycode == Input.Keys.ENTER) {
            camera.setTarget(camera.hasTarget() ? null: level.bunnyHead);
            Gdx.app.debug(TAG, "Camera follow enabled: " + camera.hasTarget());
        }
            return false;
    }



}
