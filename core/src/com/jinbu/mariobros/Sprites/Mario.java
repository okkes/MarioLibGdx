package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.jinbu.mariobros.Screens.PlayScreen;

import static com.jinbu.mariobros.MarioBros.*;
import static java.lang.Math.abs;

/**
 * Created by 15049051 on 07/05/2017.
 */
public class Mario extends Sprite implements InteractiveTileObject{


    private float stateTimer; // This will use to keep track of the amount of time we are in any given stateNew like a jump stateNew or run stateNew.
    public enum STATE{
        JUMPING,
        JUMP_FALLING,
        WALK_FALLING,
        STANDING,
        WALKING,
        CROUCHING,
        DEAD
    }

    private enum DIRECTION{
        LEFT,
        RIGHT
    }

    private final float JUMP_VELOCITY               = 3.5f;

    private final float MAX_RUN_VELOCITY_TO_RIGHT   = 2f;
    private final float MAX_WALK_VELOCITY_TO_RIGHT  = 0.7f;
    private final float MOVEMENT_SPEED_RIGHT        = 0.1f;
    private final float MAX_RUN_VELOCITY_TO_LEFT    = -MAX_RUN_VELOCITY_TO_RIGHT;
    private final float MAX_WALK_VELOCITY_TO_LEFT   = -MAX_WALK_VELOCITY_TO_RIGHT;
    private final float MOVEMENT_SPEED_LEFT         = -MOVEMENT_SPEED_RIGHT;

    private final float RUN_ANIMATION_CHANGE_FRAME  = 0.2f;

    private final float SMALL_MARIO_BOUNDS_HEIGHT   = 16 / PPM;
    private final float SMALL_MARIO_BOUNDS_WIDTH    = 16 / PPM;

    private Vector2 walkToLeftVelocity;
    private Vector2 walkToRightVelocity;
    private Vector2 jumpVelocity;

    public STATE state;
    private DIRECTION direction;

    // get the individuel texture of mario standing still
    private TextureRegion marioStand;
    private TextureRegion marioJump;
    private TextureRegion marioFall;
    private Animation<TextureRegion> marioRun;

    // the world that mario is going to live in.
    private World world;

    // The physical body of mario that will be used in the physics world.
    public Body b2body; // todo: temporary public. change it to private

    private int amountOfPlatformsStanding;

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
        defineTexture();
        setStartupStates();
    }

    private void defineMovementVelocities(){
        walkToLeftVelocity  = new Vector2(MOVEMENT_SPEED_LEFT, 0);
        walkToRightVelocity = new Vector2(MOVEMENT_SPEED_RIGHT, 0);
        jumpVelocity        = new Vector2(0, JUMP_VELOCITY);
    }

    private void createBody(){
        // Create the body
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / PPM, 32 / PPM); // temp
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
//        we need to create a circle shape
//        CircleShape shape = new CircleShape();
//        shape.setRadius(5 / PPM);

        // Attach the fixture to the body
        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = new Vector2[6];
        vertices[0] = new Vector2(-5, 5).scl(1 / PPM);
        vertices[1] = new Vector2(5, 5).scl(1 / PPM);
        vertices[2] = new Vector2(-5f, -4).scl(1 / PPM);
        vertices[3] = new Vector2(5f, -4).scl(1 / PPM);
        vertices[4] = new Vector2(-4.75f, -5).scl(1 / PPM);
        vertices[5] = new Vector2(4.75f, -5).scl(1 / PPM);
        shape.set(vertices);

        FixtureDef fdef             = new FixtureDef();
        fdef.shape                  = shape;
        fdef.filter.categoryBits    = MARIO_BIT;
        fdef.filter.maskBits        = DEFAULT_BIT | COIN_BIT | BRICK_BIT;

        b2body.createFixture(fdef).setUserData(this);

        // Attach the head fixture to the body
        EdgeShape headShape = new EdgeShape();
        headShape.set(new Vector2(-3 / PPM, 5 / PPM), new Vector2(3 / PPM, 5 / PPM));

        fdef.shape = headShape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = MARIO_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    /**
     * The feet is created in order to avoid the bouncy collision the body makes when he
     * walks over many bodies close to each other (for example the blocks).
     */
    private void createFeetForBody(){
        EdgeShape feetShape         = new EdgeShape();
        feetShape.set(new Vector2(-4.3f / PPM, -7 / PPM), new Vector2(4.3f/ PPM, -7 / PPM)); //todo: temporary uncomment this. probably no need to make the feet bigger than the body.
//        feetShape.set(new Vector2(-5f / PPM, -7 / PPM), new Vector2(5/ PPM, -7 / PPM));

        FixtureDef fdef             = new FixtureDef();
        fdef.shape                  = feetShape;
        fdef.filter.categoryBits    = MARIO_FEET_BIT;
        fdef.filter.maskBits        = DEFAULT_BIT | COIN_BIT | BRICK_BIT;
        fdef.friction               = 0.5f;
        fdef.isSensor               = true;
        b2body.createFixture(fdef).setUserData(this);
        //todo: test
        fdef.filter.categoryBits = MARIO_BIT;
        fdef.isSensor = false;
        feetShape.set(new Vector2(-5.5f / PPM, -7 / PPM), new Vector2(5.5f / PPM, -7 / PPM));
        b2body.createFixture(fdef).setUserData(this);

//        EdgeShape feetSensor = new EdgeShape();
//        feetSensor.set(new Vector2( -5f / PPM, -7.5f / PPM), new Vector2(5f / PPM, -7.5f / PPM));
//        fdef.shape = feetShape;
//        fdef.filter.categoryBits = MARIO_FEET_BIT;
//        fdef.isSensor = true;
//        b2body.createFixture(fdef).setUserData(this);
    }

    private void defineTexture(){
        marioStand  = new TextureRegion(getTexture(), 0, 11, 16, 16);
        marioJump   = new TextureRegion(getTexture(), 80, 11, 17, 16);
        marioFall   = marioStand;

        // init the run animation
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 4 ; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 11, 16, 16));
        }

        marioRun = new Animation<TextureRegion>(RUN_ANIMATION_CHANGE_FRAME, frames);
        frames.clear();

        // Set the size of the image.
        setBounds(0, 0, SMALL_MARIO_BOUNDS_WIDTH, SMALL_MARIO_BOUNDS_HEIGHT);

        // Set the region so that the playscreen can draw the region.
        setRegion(marioStand);
    }

    private void setStartupStates(){
        state       = STATE.STANDING;
        direction   = DIRECTION.RIGHT;
        stateTimer  = 0;
        amountOfPlatformsStanding = 0;
        readyToHit = false;
    }

    public float getPositionX(){
        return b2body.getPosition().x;
    }

    public void update(float dt){
        updateState();
        updateTexture();
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //todo sout
    }

    private float calculateStateTimer(STATE oldState){
        // The action changed. Reset the timer.
        if(oldState!= state) return 0;

        // Increase the stateTimer according to the speed of the body
        return stateTimer + (abs(b2body.getLinearVelocity().x) / 100 * 5f);
    }

    private void updateTexture(){
        TextureRegion region;

        switch(state){
            case JUMPING:
            case JUMP_FALLING:
                region = marioJump;
                break;
            case STANDING:
                region = marioStand;
                marioFall   = marioRun.getKeyFrame(stateTimer, false);
                break;
            case WALKING:
                region      = marioRun.getKeyFrame(stateTimer, true);
                marioFall   = marioRun.getKeyFrame(stateTimer, false); // todo: check how true works
                break;
            case WALK_FALLING:
                region = marioFall;
                break;
            default:
                region = marioStand; // todo: probably wrong. check it.
        }

        if(direction == DIRECTION.LEFT && !region.isFlipX()){
            region.flip(true, false);
        } else if(direction == DIRECTION.RIGHT && region.isFlipX()){
            region.flip(true, false);
        }

        setRegion(region);
    }

    /**
     * Round the float number to given decimal.
     * @param number the float number that needs to be rounded
     * @param digit the amount of decimal you want after the given float number.
     * @return return the rounded float number.
     */
    public static float round2(float number, int digit) {
        //todo for now its not needed anymore. delete it later perhaps?
        int pow = 10;
        for (int i = 1; i < digit; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }

    private void updateState(){
        // return if player is dead
        if(b2body.getPosition().y < 0){
            state = STATE.DEAD;
            return; // todo: not sure if the return statement is permanent.
        }

        float velocityX = b2body.getLinearVelocity().x;
        float velocityY = b2body.getLinearVelocity().y;
        STATE oldState  = state;

        // update walk state
        if(amountOfPlatformsStanding > 0 && velocityX == 0){
            state = STATE.STANDING;
        }else if(amountOfPlatformsStanding > 0 && velocityX != 0){
            state = STATE.WALKING;
            if(velocityX > 0){
                direction = DIRECTION.RIGHT;
            } else if(velocityX < 0){
                direction = DIRECTION.LEFT;
            }
        }
        // update jump state
        else if(velocityY > 0 ){
            state = STATE.JUMPING;
        }else if(state == STATE.JUMPING && velocityY < 0){
            state = STATE.JUMP_FALLING;
        }else if((state == STATE.WALKING || state == STATE.STANDING) && velocityY < 0){
            state = STATE.WALK_FALLING;
        }else if(state == STATE.JUMPING && velocityY == 0){
            // Mario probably hit his head to something which leads to a 0 velocity in Y direction. Let him fall.
            state = STATE.JUMP_FALLING;
        }

        // update state timer
        stateTimer = calculateStateTimer(oldState);
    }

    public boolean noFriction = false;

    private float locationYBeforeJump = 0;
    private boolean listenToJumpButton = false;
    private boolean oldJumpButtonPressed = false;
    public void jump(boolean jumpButtonPressed) {
        // Listen to the jump button if the jump button
        // was let go in the previous iteration and mario is on a platform.
        if(!jumpButtonPressed){
            oldJumpButtonPressed    = jumpButtonPressed;
            listenToJumpButton      = false;
            return;
        }

        if(!listenToJumpButton){
            listenToJumpButton = amountOfPlatformsStanding > 0 && !oldJumpButtonPressed;
        }

        float currentLocationY = b2body.getPosition().y;

        // Check if Mario is jumping from platform or in process of jumping
        if(amountOfPlatformsStanding > 0){
            locationYBeforeJump = currentLocationY;
        } else if(listenToJumpButton){
            boolean withinMaxJumpHeight = currentLocationY - locationYBeforeJump < 0.55f; // todo + additional increase in height according to it's x velocity
            listenToJumpButton          = state == STATE.JUMPING && withinMaxJumpHeight;
        }

        boolean withinMaxJumpSpeed  = b2body.getLinearVelocity().y <= 2f;
        if(listenToJumpButton && withinMaxJumpSpeed) {
            Vector2 impulse = new Vector2(0, 2f - b2body.getLinearVelocity().y);
            b2body.applyLinearImpulse(impulse, b2body.getWorldCenter(), true);
        }

        oldJumpButtonPressed = jumpButtonPressed;
    }

    public void moveToRight(boolean isRunning) {
        if(state == STATE.DEAD)return;

        float maxVelocityX = calculateMaxVelocityX(isRunning);
        if(maxVelocityX == 0) return;

        if(b2body.getLinearVelocity().x < maxVelocityX){
            b2body.applyLinearImpulse(walkToRightVelocity, b2body.getWorldCenter(), true);
        }
    }

    public float calculateMaxVelocityX(boolean isRunning){
        float speed = abs(b2body.getLinearVelocity().x);
        switch(state){
            case JUMPING:
                if(speed < 0.8) return 0.8f;
                return speed;
            case JUMP_FALLING:
            case WALK_FALLING:
                if(speed < 0.3) return 0.3f;
                return speed;
            case WALKING:
            case STANDING:
                if(isRunning) return MAX_RUN_VELOCITY_TO_RIGHT;
                return MAX_WALK_VELOCITY_TO_RIGHT;
            default:
                return 0;
        }
    }

    public void moveToLeft(boolean isRunning) {
        if(state == STATE.DEAD)return;

        float maxVelocityX = -calculateMaxVelocityX(isRunning);
        if(maxVelocityX == 0) return;

        if(b2body.getLinearVelocity().x > maxVelocityX){
            b2body.applyLinearImpulse(walkToLeftVelocity, b2body.getWorldCenter(), true);
        }
    }

    public boolean readyToHit;

    @Override
    public void beginContactCollision(Object object, int filterBit) {
        switch(filterBit){
            case MARIO_FEET_BIT | DEFAULT_BIT:
            case MARIO_FEET_BIT | BRICK_BIT:
            case MARIO_FEET_BIT | COIN_BIT:
                amountOfPlatformsStanding++;
                readyToHit = true;
                break;
        }
    }

    @Override
    public void endContactCollision(Contact contact, int filterBit) {
        Fixture marioFixture = contact.getFixtureA().getUserData() == this ? contact.getFixtureA() : contact.getFixtureB();
        if(state == STATE.JUMPING || state == STATE.WALK_FALLING) noFriction = true;

        if(noFriction){
            marioFixture.setFriction(0);
            contact.resetFriction();
        }

        if(marioFixture.getFilterData().categoryBits == MARIO_FEET_BIT){
            if(amountOfPlatformsStanding > 0) amountOfPlatformsStanding--;
        }

    }
    private float frictionFreeDT = 0;

    @Override
    public void preSolveCollision(Contact contact, int filterBit) {
        Fixture marioFixture = contact.getFixtureA().getUserData() == this ? contact.getFixtureA() : contact.getFixtureB();

        if(frictionFreeDT >= 0.1f){
            noFriction = false;
            frictionFreeDT = 0;
        }

        if(!noFriction){
            if(marioFixture.getFilterData().categoryBits == MARIO_FEET_BIT){
                marioFixture.setFriction(0.5f);
            }else{
                marioFixture.setFriction(0.2f);
            }
            contact.resetFriction();
        } else if(state == STATE.STANDING || state == STATE.WALKING){
            frictionFreeDT += Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void postSolveCollision(Contact contact, int filterBit) {

    }

}
