package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import static com.jinbu.mariobros.MarioBros.PPM;

/**
 * Created by 15049051 on 14/05/2017.
 */
public abstract class InteractiveRectangleObject extends InteractiveTileObject {

    protected Fixture fixture;
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected MapObject object;

    public InteractiveRectangleObject(World world, TiledMap map, MapObject object, float friction){
        this.world          = world;
        this.map            = map;
        this.bounds         = ((RectangleMapObject)object).getRectangle();

        this.object = object; // todo: temp

        BodyDef bdef        = new BodyDef();
        FixtureDef fdef     = new FixtureDef();
        PolygonShape shape  = new PolygonShape();

        fdef.friction       = friction;
        fdef.restitution    = 0;
        bdef.type           = BodyDef.BodyType.StaticBody;

        float xScaled       = (bounds.getX() + bounds.getWidth() / 2) / PPM;
        float yScaled       = (bounds.getY() + bounds.getHeight() / 2) / PPM;

        bdef.position.set(xScaled, yScaled);
        shape.setAsBox(bounds.getWidth() / 2 / PPM, bounds.getHeight() / 2 / PPM);

        fdef.shape          = shape;
        body                = world.createBody(bdef);

        fixture = body.createFixture(fdef);
    }

    public void setCategoryFIlter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * PPM / 16), (int) (body.getPosition().y * PPM / 16));
    }
}