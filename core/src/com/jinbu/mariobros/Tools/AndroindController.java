package com.jinbu.mariobros.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by 15049051 on 14/05/2017.
 */
public class AndroindController extends InputHandler{
    @Override
    public boolean leftIsPressed() {
        boolean leftScreenAreaClicked = Gdx.input.getX() >= 0 && Gdx.input.getX() <= 200;
        boolean screenIsPressed = Gdx.input.isTouched();
        if(leftScreenAreaClicked && screenIsPressed){
            return true;
        }
        return false;
    }

    @Override
    public boolean rightIsPressed() {
        boolean leftScreenAreaClicked = Gdx.input.getX() >= 250 && Gdx.input.getX() <= 450;
        boolean screenIsPressed = Gdx.input.isTouched();
        if(leftScreenAreaClicked && screenIsPressed){
            return true;
        }
        return false;
    }

    @Override
    public boolean upIsPressed() {
        if(Gdx.input.getX() >= 500 && Gdx.input.isTouched()){
            return true;
        }
        return false;
    }
}
