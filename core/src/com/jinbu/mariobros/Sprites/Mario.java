package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.jinbu.mariobros.Levels.Level1;
import com.jinbu.mariobros.Screens.PlayScreen;

import static com.badlogic.gdx.math.MathUtils.round;
import static com.jinbu.mariobros.MarioBros.PPM;

/**
 * Created by 15049051 on 07/05/2017.
 */
public class Mario extends Sprite{

    private final int NOT_IN_AIR                    = 0;
    private final float JUMP_VELOCITY               = 3.7f;

    private final float MAX_RUN_VELOCITY_TO_RIGHT   = 2f;
    private final float MAX_RUN_VELOCITY_TO_LEFT    = -2f;

    private final float MAX_WALK_VELOCITY_TO_RIGHT  = 0.8f;
    private final float MAX_WALK_VELOCITY_TO_LEFT   = -0.8f;

    private final float MOVEMENT_SPEED_RIGHT        = 0.1f;
    private final float MOVEMENT_SPEED_LEFT         = -0.1f;

    private float yAxesBefore;
    private float yAxesAfter;
    private float xAxesBefore;
    private float xAxesAfter;
    private STATE state;
    private STATE previousState;

    public enum STATE{
        JUMPING,
        FALLING,
        STANDING,
        WALKING,
        CROUCHING,
        DEAD
    }

    // the world that mario is going to live in.
    private World world;

    public Body b2body; // todo: temporary public. change it to private

    // get the individuel texture of mario standing still
    private TextureRegion marioStand;
    private TextureRegion marioJump;

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
        defineState();
    }

    private void defineState(){
        state = STATE.STANDING;

        xAxesBefore = b2body.getPosition().x;
        xAxesAfter = b2body.getPosition().x;

        yAxesBefore = b2body.getPosition().y;
        yAxesAfter = b2body.getPosition().y;
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
        marioJump = new TextureRegion(getTexture(), 82, 10, 16, 16 );

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
        if(state == STATE.DEAD){
            return;
        }

        updateState();
        updateTexture();
        setPosition(b2body.getPosition().x - getWidth() / 1.8f, b2body.getPosition().y - getHeight() / 2);
    }

    private void updateTexture(){
        switch(state){
            case JUMPING:
                setRegion(marioJump);
                break;
            case STANDING:
                setRegion(marioStand);
                break;
        }
    }

    public static float round2(float number, int digit) {
        int pow = 10;
        for (int i = 1; i < digit; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }

    private void updateState(){
        xAxesBefore = xAxesAfter;
        xAxesAfter = b2body.getPosition().x;

        yAxesBefore = yAxesAfter;
        yAxesAfter = b2body.getPosition().y;

        if(xAxesBefore < xAxesAfter){
            //System.out.println("Walking to right");
        }

        if(xAxesBefore > xAxesAfter){
            //System.out.println("wWalking to left");
        }

        if(round2(yAxesBefore, 2) != round2(yAxesAfter, 2)){
            if(yAxesBefore < yAxesAfter){
                System.out.println("Jumping, ybefore = " + yAxesBefore + " and yafter = " + yAxesAfter);
                state = STATE.JUMPING;
            }

            if(yAxesBefore > yAxesAfter){
                System.out.println("Falling, ybefore = " + yAxesBefore + " and yafter = " + yAxesAfter);
                state = STATE.FALLING;
            }
        }
        else if(round2(yAxesBefore, 3) == round2(yAxesAfter, 3)){
            state = STATE.STANDING;
        }
        else if(yAxesAfter < 0){
            state = STATE.DEAD;
        }
    }

    public STATE getState(){
        return state;
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
