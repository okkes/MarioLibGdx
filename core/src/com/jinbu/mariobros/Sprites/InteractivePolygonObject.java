package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import static com.jinbu.mariobros.MarioBros.PPM;

/**
 * Created by 15049051 on 14/05/2017.
 */
public abstract class InteractivePolygonObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected float vertices[];
    protected Body body;

    public InteractivePolygonObject(World world, TiledMap map, MapObject object){
        this.map = map;
        this.world = world;
        this.vertices = ((PolygonMapObject) object).getPolygon().getTransformedVertices();

        BodyDef bdef        = new BodyDef();
        FixtureDef fdef     = new FixtureDef();
        ChainShape shape    = new ChainShape();

        for (int x = 0; x < vertices.length; x++) {
            vertices[x] /= PPM;
        }

        shape.createChain(vertices);
        bdef.position.set(0, 0);

        // add body the world
        body = world.createBody(bdef);
        fdef.shape = shape;

        //add fixture to the body
        body.createFixture(fdef);
    }
}
