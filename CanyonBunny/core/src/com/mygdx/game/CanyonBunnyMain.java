package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.assets.AssetManager;


public class CanyonBunnyMain implements ApplicationListener {

	private static final java.lang.String Tag = CanyonBunnyMain.class.getName();

	private  WorldController worldController;
	private  WorldRenderer   worldRenderer;
	private boolean paused;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.instance.init(new AssetManager());
		//inicializar o render e controller do world
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		paused = false;
	}

	@Override
	public void render () {
		if(!paused) {
			worldController.update(Gdx.graphics.getDeltaTime());
		}
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);//fundo setado em azul hexadecimal transformado em float
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//limpar o buff de imagens
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		Assets.instance.init(new AssetManager());
		paused = false;
	}

	@Override
	public void dispose () {
		worldRenderer.dispose();
		Assets.instance.dispose();
	}
}
