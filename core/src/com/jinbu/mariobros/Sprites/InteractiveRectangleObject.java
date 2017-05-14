package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import static com.jinbu.mariobros.MarioBros.PPM;

/**
 * Created by 15049051 on 14/05/2017.
 */
public abstract class InteractiveRectangleObject {

    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    public InteractiveRectangleObject(World world, TiledMap map, MapObject object){
        this.world  = world;
        this.map    = map;
        this.bounds = ((RectangleMapObject)object).getRectangle();

        BodyDef bdef        = new BodyDef();
        FixtureDef fdef     = new FixtureDef();
        PolygonShape shape  = new PolygonShape();

        bdef.type       = BodyDef.BodyType.StaticBody;

        float xScaled   = (bounds.getX() + bounds.getWidth() / 2) / PPM;
        float yScaled   = (bounds.getY() + bounds.getHeight() / 2) / PPM;
        bdef.position.set(xScaled, yScaled);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / PPM, bounds.getHeight() / 2 / PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
    }
}