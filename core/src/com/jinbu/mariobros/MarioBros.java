package com.jinbu.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jinbu.mariobros.Screens.PlayScreen;
import com.jinbu.mariobros.Tools.DesktopController;
import com.jinbu.mariobros.Tools.InputHandler;
import com.jinbu.mariobros.Tools.AndroindController;

public class MarioBros extends Game {
	// virtual width and height for the game. These units will be used to have a responsive game world
	// Otherwise we would have to work with pixels and that will differ from each device.
	// Apparently its not important what these numbers are as long as if you work consistent with them.
	// 400 / 16 = 25
	// 280 / 16 = 13.
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;
	// Spritebatch is very memory intensive. So only have 1 spritebatch per game. Make it public in order to make it accessible
	// to other classes or pass it around.
	public SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();


		// By passing this game class to the screen, the screen class can set another screen by himself.
		setScreen(new PlayScreen(this, getController()));
	}

	private InputHandler getController(){
		InputHandler controller;
		switch(Gdx.app.getType()) {
			case Android:
				controller = new AndroindController();
				break;
			case Desktop:
			default:
				controller = new DesktopController();
				break;
		}
		return controller;
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

/*
lisft of todo's:
TODO: KEEP SPEED AFTER JUMPING
TODO: ADD PROPER JUMPING FOR ANDROID
TODO: FIX WEIRD VERTICAL VELOCITY BUG WHEN FRICTION TO THE FEET IS ADDED
TODO: MAKE THE BODY FALL EASIER BETWEEN BLOCKS.
TODO: ADD FRICTION IN THE AIR
TODO: MAKE THE FONT SHARPER.
TODO: JUMP HIGHER WHEN HOLDING THE JUMP BUTTON AND LOWER WHEN PRESSING LESS
TODO: CHANGE THE STATIC Y LOCATION OF THE INVISIBLE WALL
TODO:
 */
