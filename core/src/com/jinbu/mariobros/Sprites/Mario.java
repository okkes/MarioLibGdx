package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.jinbu.mariobros.Screens.PlayScreen;

import static com.jinbu.mariobros.MarioBros.PPM;

/**
 * Created by 15049051 on 07/05/2017.
 */
public class Mario extends Sprite{

    private final int NOT_IN_AIR                    = 0;
    private final float JUMP_VELOCITY               = 4f;

    private final float MAX_RUN_VELOCITY_TO_RIGHT   = 2f;
    private final float MAX_RUN_VELOCITY_TO_LEFT    = -2f;

    private final float MAX_WALK_VELOCITY_TO_RIGHT  = 0.8f;
    private final float MAX_WALK_VELOCITY_TO_LEFT   = -0.8f;

    private final float MOVEMENT_SPEED_RIGHT        = 0.1f;
    private final float MOVEMENT_SPEED_LEFT         = -0.1f;

    // the world that mario is going to live in.
    private World world;

    private Body b2body;

    // get the individuel texture of mario standing still
    private TextureRegion marioStand;

    private Vector2 walkToLeftVelocity;
    private Vector2 walkToRightVelocity;
    private Vector2 jumpVelocity;

    public Mario(World world, PlayScreen screen){
        // make a call to super, which is a sprite class and it can take a textureregion that we can manupulate later.
        super(screen.getAtlas().findRegion("little_mario"));

        this.world  = world;

        defineMario();
    }

    private void defineMario(){
        defineMovementVelocities();
        createBody();
        createFeetForBody();
        addFixtureToBody();
    }

    private void defineMovementVelocities(){
        walkToLeftVelocity  = new Vector2(MOVEMENT_SPEED_LEFT, 0);
        walkToRightVelocity = new Vector2(MOVEMENT_SPEED_RIGHT, 0);
        jumpVelocity        = new Vector2(0, JUMP_VELOCITY);
    }

    public float getPositionX(){
        return b2body.getPosition().x;
    }

    private void addFixtureToBody(){
        // the first param is the texture we want to get.
        marioStand = new TextureRegion(getTexture(), 0, 10, 16, 16);

        setBounds(0, 0, 16 / PPM, 16 / PPM);
        setRegion(marioStand);
    }

    private void createFeetForBody(){
        FixtureDef fdef = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-6 / PPM, -7 / PPM), new Vector2(6 / PPM, -7 / PPM));
        fdef.shape = feet;
        fdef.friction = 0.5f; //todo: adding friction causes buggy vertical velocity sometimes.
        fdef.isSensor = false;
        b2body.createFixture(fdef);
    }

    private void createBody(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / PPM, 32 / PPM); // temp
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

//        we need to create a circle shape
//        CircleShape shape = new CircleShape();
//        shape.setRadius(5 / PPM);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6 / PPM, 6 / PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 1.8f, b2body.getPosition().y - getHeight() / 2);
    }

    public void jump() {
        if(b2body.getLinearVelocity().y == NOT_IN_AIR){
            // By applying the impulse to the center of the body, the body wont spin/rotate.
            // The last parameter is set to true to wake the body up when applying the impulse. The bodies are sleeping by default.
            b2body.applyLinearImpulse(jumpVelocity, b2body.getWorldCenter(), true);
        }
    }

    public void moveToRight(boolean isRunning) {
        boolean move = false;
        if(isRunning && b2body.getLinearVelocity().x <= MAX_RUN_VELOCITY_TO_RIGHT){
            move = true;
        }
        else if (b2body.getLinearVelocity().x <= MAX_WALK_VELOCITY_TO_RIGHT){
            move = true;
        }

        if(move){
            b2body.applyLinearImpulse(walkToRightVelocity, b2body.getWorldCenter(), true);
        }
    }

    public void moveToLeft(boolean isRunning) {
        boolean move = false;
        if(isRunning && b2body.getLinearVelocity().x >= MAX_RUN_VELOCITY_TO_LEFT){
            move = true;
        }
        else if (b2body.getLinearVelocity().x >= MAX_WALK_VELOCITY_TO_LEFT){
            move = true;
        }

        if(move){
            b2body.applyLinearImpulse(walkToLeftVelocity, b2body.getWorldCenter(), true);
        }
    }
}
