package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

import static com.jinbu.mariobros.MarioBros.*;

/**
 * Created by 15049051 on 14/05/2017.
 */
public class Brick extends InteractiveRectangleObject {
    public final static String LAYER_NAME   = "bricks";
    public final static Class<RectangleMapObject> TYPE = RectangleMapObject.class; // TODO: move to super?, make it more dynamic?

    public Brick (World world, TiledMap map, MapObject object){
        super(world, map, object, 0);
        super.fixture.setUserData(this);
        setcategoryfilter(BRICK_BIT);
    }

//    @Override
//    public void sensorCollision() {
//        setcategoryfilter(DESTROYED_BIT);
//        getCell().setTile(null);
//    }

    @Override
    public void beginContactCollision(Object object, int filterBit) {
        switch(filterBit){
            case BRICK_BIT | MARIO_HEAD_BIT:
                if(((Mario)object).readyToHit) {
                    setcategoryfilter(DESTROYED_BIT);
                    getCell().setTile(null);
                    ((Mario)object).readyToHit = false;
                }
                break;
        }
    }

    private void destroyedByPlayer(){

    }

    @Override
    public void endContactCollision(Contact contact, int filterBit) {

    }

    @Override
    public void preSolveCollision(Contact contact, int filterBit) {

    }

    @Override
    public void postSolveCollision(Contact contact, int filterBit) {

    }
}
