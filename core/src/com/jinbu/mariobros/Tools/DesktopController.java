package com.jinbu.mariobros.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by 15049051 on 14/05/2017.
 */
public class DesktopController extends InputHandler {
    @Override
    public boolean leftIsPressed() {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            return true;
        }
        return false;
    }

    @Override
    public boolean rightIsPressed() {
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            return true;
        }
        return false;
    }

    @Override
    public boolean upIsPressed() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            return true;
        }
        return false;
    }
}
