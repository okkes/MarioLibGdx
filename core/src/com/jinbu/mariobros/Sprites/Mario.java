package com.jinbu.mariobros.Sprites;

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

    //TODO: TEMPORARY CODE
    public enum States{FALLING, JUMPING, STANDING, RUNNING};
    public States CurrentState;
    public States PreviousState;
    private Animation<TextureRegion> MarioRun;
    private Animation<TextureRegion> MarioJump;
    private boolean runningRight;
    private float stateTimer; // This will use to keep track of the amount of time we are in any given stateNew like a jump stateNew or run stateNew.
    //TODO: END OF TEMP CODE

    // TODO: TEMP CODE
    // TODO: END TEMP CODE
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

    private STATE state;
    private DIRECTION direction;

    // get the individuel texture of mario standing still
    private TextureRegion marioStand;
    private TextureRegion marioJump;
    private TextureRegion marioFall;
    private Animation<TextureRegion> marioRun;

    // the world that mario is going to live in.
    private World world;

    // The physical body of mario that will be used in the physics world.
    private Body b2body; // todo: temporary public. change it to private

    private boolean onPlatform;

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
        shape.setAsBox(5 / PPM, 5 / PPM);

        FixtureDef fdef             = new FixtureDef();
        fdef.shape                  = shape;
        fdef.filter.categoryBits    = MARIO_BIT;
        fdef.filter.maskBits        = DEFAULT_BIT | COIN_BIT | BRICK_BIT;

        b2body.createFixture(fdef).setUserData(this);

        // Attach the head fixture to the body
        EdgeShape headShape = new EdgeShape();
        headShape.set(new Vector2(-4 / PPM, 5 / PPM), new Vector2(4 / PPM, 5 / PPM));

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
        feetShape.set(new Vector2(-5.5f / PPM, -7 / PPM), new Vector2(5.5f/ PPM, -7 / PPM));

        FixtureDef fdef             = new FixtureDef();
        fdef.shape                  = feetShape;
        fdef.filter.categoryBits    = MARIO_FEET_BIT;
        fdef.filter.maskBits        = DEFAULT_BIT | COIN_BIT | BRICK_BIT;
        fdef.friction               = 0.5f;
        fdef.isSensor               = false;
        b2body.createFixture(fdef).setUserData(this);
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
        onPlatform  = true;
    }

    public float getPositionX(){
        return b2body.getPosition().x;
    }

    public void update(float dt){
        updateState();
        updateTexture();
        setPosition(b2body.getPosition().x - getWidth() / 1.7f, b2body.getPosition().y - getHeight() / 2);
        //todo sout
        System.out.println("state: " + state );
    }

    private float calculateStateTimer(STATE oldState){
        // The action changed. Reset the timer.
        if(oldState!= state){
            return 0;
        }
        // Increase the stateTimer according to the speed of the body
        else{
            return stateTimer + (abs(b2body.getLinearVelocity().x) / 100 * 5f);
        }
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

        if(velocityY < 0){
            onPlatform = false;
        }else if(onPlatform && velocityX == 0){
            state = STATE.STANDING;
        }else if(onPlatform && velocityX != 0){
            state = STATE.WALKING;
        }

        // update jump state
        if(velocityY > 0 ){
            state = STATE.JUMPING;
        }else if(state == STATE.JUMPING && velocityY < 0){
            state = STATE.JUMP_FALLING;
        }else if((state == STATE.WALKING || state == STATE.STANDING) && velocityY < 0){
            state = STATE.WALK_FALLING;
        }

        // update movement animation
        if(state == STATE.WALKING || state == STATE.STANDING) {
            if(velocityX > 0) {
                direction   = DIRECTION.RIGHT;
                state       = STATE.WALKING;
            }else if (velocityX < 0) {
                direction   = DIRECTION.LEFT;
                state       = STATE.WALKING;
            }
        }

        if(state == STATE.WALK_FALLING || state == STATE.JUMPING || state == STATE.JUMP_FALLING){
            if(!noFriction){
                if(b2body.getLinearVelocity().x > 0.5f){
                    for(Fixture fixture : b2body.getFixtureList()) {
                        //fixture.setFriction(0);
                    }
                }
            }
        }else if(noFriction){
            for(Fixture fixture : b2body.getFixtureList()){
                if(fixture.getFilterData().categoryBits == MARIO_FEET_BIT){
                    //fixture.setFriction(0.5f);
                }else{
                    //fixture.setFriction(0.2f);
                }
            }
        }

        // update state timer
        stateTimer = calculateStateTimer(oldState);
    }

    private boolean noFriction = false;

    private float startLocationY = 0;
    private boolean jumpInterrupted = false;
    private boolean jumpButtonReady = false;
    private boolean jumpButtonHolding = false;
    public void jump(boolean jumpIsPressed) {
        if(state == STATE.DEAD)return;

        boolean marioOnAPlatform    = state == STATE.STANDING || state == STATE.WALKING || onPlatform;
        if(!jumpButtonReady) {
            jumpButtonReady = marioOnAPlatform && !jumpIsPressed;
        }else if(jumpButtonReady && state == STATE.WALK_FALLING){
            jumpButtonReady = false;
        }

        if(!jumpIsPressed){
            jumpButtonHolding = false;
            return;
        }

        boolean jump = false;
        float currentLocationY = b2body.getPosition().y;

        boolean jumpingFromPlatform = jumpButtonReady && jumpIsPressed;
        if(jumpingFromPlatform){
            jump                = true;
            startLocationY      = currentLocationY;
            jumpButtonReady     = false;
            jumpButtonHolding   = true;
            jumpInterrupted     = false;
        }else {
            boolean risingInAir         = state == STATE.JUMPING;
            boolean withinMaxJumpHeight = currentLocationY - startLocationY < 0.5f;
            boolean withinMaxJumpSpeed  = b2body.getLinearVelocity().y <= 2f; // todo: soon probably redundant
            jump    = risingInAir && withinMaxJumpHeight && withinMaxJumpSpeed && !jumpInterrupted && jumpButtonHolding;
        }

        if(jump) {
            onPlatform = false;
            b2body.applyLinearImpulse(new Vector2(0, 2.1f - b2body.getLinearVelocity().y), b2body.getWorldCenter(), true);
        }
    }

    private Vector2 calculateJumpVelocity(){
        return null;
    }

    public void moveToRight(boolean isRunning) {
        if(state == STATE.DEAD)return;

        boolean move = false;
        //todo: the jump limit is wrong. it should be consistent in the air and not faster nor slower. in our case it gets slower.
        // Check if the run button is pressed
        // Check if the player is not in the air
        // Increase the speed limit
        if(isRunning && b2body.getLinearVelocity().x <= MAX_RUN_VELOCITY_TO_RIGHT){ // && state != STATE.JUMPING
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
        if(state == STATE.DEAD)return;

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

    @Override
    public void collisionOccured(Object object, int filterBit) {
        System.out.println("filterbid: " + filterBit);
        switch(filterBit){
            case MARIO_FEET_BIT | DEFAULT_BIT:
            case MARIO_FEET_BIT | BRICK_BIT:
            case MARIO_FEET_BIT | COIN_BIT:
                onPlatform = true;
                break;
            case MARIO_HEAD_BIT | DEFAULT_BIT:
            case MARIO_HEAD_BIT | BRICK_BIT:
            case MARIO_HEAD_BIT | COIN_BIT:
                jumpInterrupted = true;
                break;
        }
    }

}
