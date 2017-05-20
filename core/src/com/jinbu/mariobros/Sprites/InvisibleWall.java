package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.jinbu.mariobros.MarioBros;

import static com.jinbu.mariobros.MarioBros.PPM;

/**
 * Created by 15049051 on 20/05/2017.
 */
public class InvisibleWall {
    private Body b2body;
    private final float LOCATION_Y          = 1 / PPM;
    private final float WALL_WIDTH          = 2 / PPM;

    public InvisibleWall(World world){
        BodyDef bdef    = new BodyDef();
        bdef.type       = BodyDef.BodyType.StaticBody;
        bdef.position.set(0 - WALL_WIDTH, LOCATION_Y);
        b2body          = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        // multiply the height by two to prevent mario from jumping over the wall via high obstacles.
        shape.setAsBox(WALL_WIDTH, MarioBros.V_HEIGHT / PPM * 2);
        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

    public void update(float location) {
        b2body.setTransform(location - WALL_WIDTH, LOCATION_Y, 0);
    }
}
