package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by LucasRezende on 30/11/2016.
 */
public class Rock extends AbstractGameObject {

    private TextureRegion regEdge;
    private TextureRegion regMiddle;
    private int length;

    public Rock(){
        init();
    }

    private void init() {
        dimension.set(1,1.5f);
        regEdge = Assets.instance.rock.edge;
        regMiddle = Assets.instance.rock.middle;
        setLength(1);
    }

    public void setLength(int length) {
        this.length = length;
        bounds.set(0, 0, dimension.x * length, dimension.y);
    }

    public void increaseLength(int amount) {
        setLength(length + amount);
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion reg = null;
        float relX = 0;
        float relY = 0;

        //draw left region
        reg = regEdge;
        relX -= dimension.x/4;
            batch.draw(reg.getTexture(),
                    position.x + relX, position.y + relY,
                    origin.x,origin.y,
                    dimension.x/4,dimension.y,
                    scale.x,scale.y,
                    rotation,
                    reg.getRegionX(),reg.getRegionY(),
                    reg.getRegionWidth(),reg.getRegionHeight(),
                    false,false);


        //draw middle region
        relX = 0;
        reg = regMiddle;
        for(int i = 0; i < length; i++){
            batch.draw(reg.getTexture(),
                    position.x + relX, position.y + relY,
                    origin.x,origin.y,
                    dimension.x,dimension.y,
                    scale.x,scale.y,
                    rotation,
                    reg.getRegionX(),reg.getRegionY(),
                    reg.getRegionWidth(),reg.getRegionHeight(),
                    false,false);
            relX += dimension.x;
        }

        //draw right region

        reg = regEdge;
        batch.draw(reg.getTexture(),
                position.x + relX, position.y + relY,
                origin.x + dimension.x /8 ,origin.y,
                dimension.x/4,dimension.y,
                scale.x,scale.y,
                rotation,
                reg.getRegionX(),reg.getRegionY(),
                reg.getRegionWidth(),reg.getRegionHeight(),
                true,false);

    }
}




















