package com.jinbu.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jinbu.mariobros.Screens.PlayScreen;

public class MarioBros extends Game {
	// virtual width and height for the game. These units will be used to have a responsive game world
	// Otherwise we would have to work with pixels and that will differ from each device.
	// Apparently its not important what these numbers are as long as if you work consistent with them.
	// 400 / 16 = 25
	// 280 / 16 = 13.
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;

	// Spritebatch is very memory intensive. So only have 1 spritebatch per game. Make it public in order to make it accessible
	// to other classes or pass it around.
	public SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		// By passing this game class to the screen, the screen class can set another screen by himself.
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		// Delegate the render method to the screen that is active at the time.
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		// img.dispose();
	}
}
