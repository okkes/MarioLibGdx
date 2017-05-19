package com.jinbu.mariobros.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by 15049051 on 14/05/2017.
 */
public class AndroindController extends InputHandler{
    @Override
    public boolean leftIsPressed() {
        for(int i = 0; i < 2; i++){
            if(Gdx.input.isTouched(i)) {
                boolean leftAreaClicked = Gdx.input.getX(i) >= 0 && Gdx.input.getX(i) <= 200;

                if (leftAreaClicked) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean rightIsPressed() {
        for(int i = 0; i < 2; i++){
            if(Gdx.input.isTouched(i)) {
                boolean rightAreaClicked = Gdx.input.getX(i) >= 250 && Gdx.input.getX(i) <= 450;

                if (rightAreaClicked) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean upIsPressed() {
        //TODO: While holding the jump button, when you walk around, you will start of with jumping before you walk. Fix this bug
        if(Gdx.input.justTouched()){
            for(int i = 0; i < 2; i++){
                if (Gdx.input.isTouched(i)) {
                    if(Gdx.input.getX(i) >= 500){
                        System.out.println("gdx.x on " + i + " = " + Gdx.input.getX(i));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean sprintIsPressed() {
        return false;
        ///todo: add sprint for android
    }
}
