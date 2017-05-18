package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.jinbu.mariobros.Screens.PlayScreen;

import static com.jinbu.mariobros.MarioBros.PPM;

/**
 * Created by 15049051 on 07/05/2017.
 */
public class Mario extends Sprite{
    // the world that mario is going to live in.
    public World world;

    public Body b2body;

    // get the individuel texture of mario standing still
    private TextureRegion marioStand;

    public Mario(World world, PlayScreen screen){
        // make a call to super, which is a sprite class and it can take a textureregion that we can manupulate later.
        super(screen.getAtlas().findRegion("little_mario"));

        this.world = world;
        defineMario();

        // the first param is the texture we want to get.
        marioStand = new TextureRegion(getTexture(), 0, 10, 16, 16);
        //marioStand = new TextureRegion(getTexture(), 0, 0, 0, 0);// todo: temporary to hide the sprite.
        setBounds(0, 0, 16 / PPM, 16 / PPM);
        setRegion(marioStand);
    }

    private void defineMario(){
        defineBody();
        defineFeet();
    }

    private void defineFeet(){
        FixtureDef fdef = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-6 / PPM, -7 / PPM), new Vector2(6 / PPM, -7 / PPM));
        fdef.shape = feet;
        //fdef.friction = 0.5f;
        fdef.isSensor = false;
        b2body.createFixture(fdef);
    }

    private void defineBody(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / PPM, 32 / PPM); // temp
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        // we need to create a circle shape
//        CircleShape shape = new CircleShape();
        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(5 / PPM, 6 / PPM);
//        shape.setRadius(5 / PPM);
        //fdef.density = -2; todo: make it slide less
        fdef.shape = shape2;
        b2body.createFixture(fdef);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }
}
