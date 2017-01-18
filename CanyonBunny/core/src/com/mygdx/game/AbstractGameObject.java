package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by LucasRezende on 07/11/2016.
 */
public abstract class AbstractGameObject {

    //Atributos base
    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;

    //atributos especiais
    public Vector2 velocity;
    public Vector2 terminalVelocity;
    public Vector2 friction;
    public Vector2 acceleration;
    public Rectangle bounds;

    public AbstractGameObject(){
        position = new Vector2(0,0);
        dimension = new Vector2(1,1);
        origin = new Vector2(0,0);
        scale = new Vector2(1,1);
        rotation = 0;
        velocity = new Vector2();
        terminalVelocity = new Vector2(1, 1);
        friction = new Vector2();
        acceleration = new Vector2();
        bounds = new Rectangle();
    }

    protected void updateMotionX (float delta) {
        if (velocity.x != 0) {
            if (velocity.x > 0) {//Atrito
                velocity.x = Math.max(velocity.x - friction.x * delta, 0);
            } else {
                velocity.x = Math.min(velocity.x + friction.x * delta, 0);
            }
        }
        //aceleraçao
        velocity.x += acceleration.x * delta;
        velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
    }

    protected void updateMotionY (float delta) {
        if (velocity.y != 0) {
            if (velocity.y > 0) {//Atrito
                velocity.y = Math.max(velocity.y - friction.y * delta, 0);
            } else {
                velocity.y = Math.min(velocity.y + friction.y * delta, 0);
            }
        }
       //aceleraçao
        velocity.y += acceleration.y * delta;
        velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
    }

    public void update(float delta){
        updateMotionX(delta);
        updateMotionY(delta);
        // Movimentação
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
    }

    public abstract void render(SpriteBatch batch);
}
