package com.jinbu.mariobros.Levels;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.jinbu.mariobros.Sprites.*;

import static com.jinbu.mariobros.MarioBros.PPM;

/**
 * Created by 15049051 on 14/05/2017.
 */
public class Level1{
    public static final int MARIO_SPAWN_X = 32;
    public static final int MARIO_SPAWN_Y = 32;

    public Level1(TiledMap map, World world) {
        // todo: for now we are initialising the map in the playscreen class. this code will probably move here.

        // Load the pipes
        for (MapObject object : map.getLayers().get(Pipe.LAYER_NAME).getObjects().getByType(Pipe.TYPE)){
            new Pipe(world, map, object);
        }

        // Load the coins
        for (MapObject object : map.getLayers().get(Coin.LAYER_NAME).getObjects().getByType(Coin.TYPE)){
            new Coin(world, map, object);
        }

        // Load the bricks
        for (MapObject object : map.getLayers().get(Brick.LAYER_NAME).getObjects().getByType(Brick.TYPE)){
            new Brick(world, map, object);
        }

        // Load the ground
        for (MapObject object : map.getLayers().get(Ground.LAYER_NAME).getObjects().getByType(Ground.TYPE)){
            new Ground(world, map, object);
        }

       // Load the stairs
        for (MapObject object : map.getLayers().get(Stair.LAYER_NAME).getObjects().getByType(Stair.TYPE)){
            new Stair(world, map, object);
        }
    }
}
