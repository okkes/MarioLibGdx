package com.jinbu.mariobros.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by 15049051 on 14/05/2017.
 */
public class DesktopController extends InputHandler {
    @Override
    public boolean leftIsPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }

    @Override
    public boolean rightIsPressed() {
         return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    @Override
    public boolean jumpIsPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.X);
    }

    @Override
    public boolean sprintIsPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.Z);
    }
}
