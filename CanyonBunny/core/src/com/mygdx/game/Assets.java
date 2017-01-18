package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import javafx.scene.transform.Scale;


/**
 * Created by LucasRezende on 21/11/2016.
 */
public class Assets implements Disposable, AssetErrorListener {

    public static  final java.lang.String Tag = Assets.class.getName();
    public  static final  Assets instance = new Assets();
    private AssetManager assetManager;
    public  AssetFonts fontes;


    public AssetBunny bunny;
    public AssetRock rock;
    public AssetGoldCoin goldCoin;
    public AssetFeather feather;
    public AssetLevelDecoration levelDecoration;

    private Assets(){};

    public void init(AssetManager assetManager){
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        assetManager.finishLoading();
        Gdx.app.debug(Tag,"# assets carregados: " + assetManager.getAssetNames().size);
        for(String a : assetManager.getAssetNames()) {
            Gdx.app.debug(Tag, "asset: " + a);
        }
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
        for (Texture t : atlas.getTextures()){
            t.setFilter(TextureFilter.Linear,TextureFilter.Linear);
        }
        fontes = new AssetFonts();
        bunny = new AssetBunny(atlas);
        rock = new AssetRock(atlas);
        goldCoin = new AssetGoldCoin(atlas);
        feather = new AssetFeather(atlas);
        levelDecoration = new AssetLevelDecoration(atlas);
    }


    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(Tag, "Asset nao foi carregada ",(Exception)throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fontes.defaultBig.dispose();
        fontes.defaultNormal.dispose();
        fontes.defaultSmall.dispose();
    }

    public class AssetBunny{
       public final  AtlasRegion head;

       public  AssetBunny (TextureAtlas atlas){
           head = atlas.findRegion("bunny_head");
       }
    }

    public class AssetRock{
        public final AtlasRegion edge;
        public final AtlasRegion middle;

        public  AssetRock(TextureAtlas atlas){
            edge = atlas.findRegion("rock_edge");
            middle = atlas.findRegion("rock_middle");
        }
    }

    public class AssetGoldCoin{
        public final  AtlasRegion goldCoin;

        public  AssetGoldCoin (TextureAtlas atlas){
            goldCoin = atlas.findRegion("item_gold_coin");
        }
    }

    public class AssetFeather{
        public final  AtlasRegion feather;

        public  AssetFeather (TextureAtlas atlas){
            feather = atlas.findRegion("item_feather");
        }
    }

    public class AssetLevelDecoration{
        public final  AtlasRegion cloud01;
        public final  AtlasRegion cloud02;
        public final  AtlasRegion cloud03;
        public final  AtlasRegion mountainLeft;
        public final  AtlasRegion mountainRight;
        public final  AtlasRegion waterOverlay;

        public  AssetLevelDecoration (TextureAtlas atlas){
            cloud01 = atlas.findRegion("cloud01");
            cloud02 = atlas.findRegion("cloud02");
            cloud03 = atlas.findRegion("cloud03");
            mountainLeft = atlas.findRegion("mountain_left");
            mountainRight = atlas.findRegion("mountain_right");
            waterOverlay = atlas.findRegion("water_overlay");
        }
    }

    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts () {
            // create three fonts using Libgdx's 15px bitmap font
            defaultSmall = new BitmapFont(Gdx.files.internal(Constants.FONT_SOURCE), true);
            defaultNormal = new BitmapFont(Gdx.files.internal(Constants.FONT_SOURCE), true);
            defaultBig = new BitmapFont(Gdx.files.internal(Constants.FONT_SOURCE), true);
            //    android/assets/

            // set font sizes
            //defaultSmall.setScale(0.75f);
            // defaultNormal.setScale(1.0f);
            // defaultBig.setScale(2.0f);
            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            //defaultSmall.setScale(Scale.scale(0.75,0.75f).);
            defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        }

    }
}

