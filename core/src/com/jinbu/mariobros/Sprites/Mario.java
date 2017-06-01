package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.jinbu.mariobros.Screens.PlayScreen;

import static com.jinbu.mariobros.MarioBros.PPM;
import static java.lang.Math.abs;

/**
 * Created by 15049051 on 07/05/2017.
 */
public class Mario extends Sprite{
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
        FALLING,
        STANDING,
        WALKING,
        CROUCHING,
        DEAD
    }

    private enum DIRECTION{
        LEFT,
        RIGHT
    }

    private final int NOT_IN_AIR                    = 0;
    private final float JUMP_VELOCITY               = 3.7f;

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

    private float positionY;
    private float positionX;

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
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / PPM, 32 / PPM); // temp
        bdef.type = BodyDef.BodyType.DynamicBody;
        //bdef.linearDamping = 1f;//todo: playing with it. intention is to create air resistance/friction.
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

    /**
     * The feet is created in order to avoid the bouncy collision the body makes when he
     * walks over many bodies close to each other (for example the blocks).
     */
    private void createFeetForBody(){
        FixtureDef fdef = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-6 / PPM, -7 / PPM), new Vector2(6 / PPM, -7 / PPM));
        fdef.shape = feet;
        //fdef.friction = 0.5f; //todo: adding friction causes buggy vertical velocity sometimes.
        fdef.isSensor = false;
        b2body.createFixture(fdef);
    }

    private void defineTexture(){
        marioStand = new TextureRegion(getTexture(), 0, 10, 16, 17);
        marioJump = new TextureRegion(getTexture(), 82, 10, 16, 17);
        marioFall = marioStand;

        // init the run animation
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 4 ; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 10, 16, 17));
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
        positionX = 0;
        positionY = 0;
    }

    public float getPositionX(){
        return b2body.getPosition().x;
    }

    public void update(float dt){
        updateState();
        updateTexture();
        setPosition(b2body.getPosition().x - getWidth() / 1.7f, b2body.getPosition().y - getHeight() / 2);
//        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) b = !b;
//        if(b)System.out.println(b2body.getLinearVelocity().x);
    }
//    private boolean b = true;
    private float calculateStateTimer(STATE oldState){
        // reset stateTimer since the state has changed.
        if(oldState != state) return 0;

        return stateTimer + (abs(b2body.getLinearVelocity().x) / 100 * 5f);
    }

    private void updateTexture(){
        TextureRegion region;

        switch(state){
            case JUMPING:
                region = marioJump;
                break;
            case STANDING:
                region = marioStand;
                break;
            case WALKING:
                region      = marioRun.getKeyFrame(stateTimer, true);
                marioFall   = marioRun.getKeyFrame(stateTimer, false);
                break;
            case FALLING:
                region = marioFall;
                break;
            default:
                region = marioStand; // todo: probably wrong. check it.
        }

        if(direction == DIRECTION.LEFT && !region.isFlipX()){
            region.flip(true, false);
//            System.out.println("Direction = " + direction + ", and region is flipped: " + region.isFlipX());
//            System.out.println(b2body.getLinearVelocity().x < 0);
        } else if(direction == DIRECTION.RIGHT && region.isFlipX()){
//            System.out.println("Direction = " + direction + ", and region is flipped: " + region.isFlipX());
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
        int pow = 10;
        for (int i = 1; i < digit; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }

    private void updateState(){
        if(b2body.getPosition().y < 0){
            state = STATE.DEAD;
            return; // todo: not sure if the return statement is permanent.
        }

        float velocityX = b2body.getLinearVelocity().x;
        float velocityY = b2body.getLinearVelocity().y;
        STATE oldState  = state;

        // update jump state
        if(velocityY > 0 ||  (velocityY < 0 && state == STATE.JUMPING)){
            state = STATE.JUMPING;
        }else if(velocityY < 0){
            state = STATE.FALLING;
        }else {
            //todo: Probably temporary. Set the stand state when the actual collision occurs
            state = STATE.STANDING;
        }

        // update movement animation
        if(state != STATE.JUMPING && state != STATE.FALLING) {
            if(velocityX > 0) {
                direction   = DIRECTION.RIGHT;
                state       = STATE.WALKING;
            }else if (velocityX < 0) {
                direction   = DIRECTION.LEFT;
                state       = STATE.WALKING;
            }else{
                state       = STATE.STANDING;
            }
        }

        stateTimer = calculateStateTimer(oldState);
    }

    public void jump() {
        if(state == STATE.DEAD)return;

        if(b2body.getLinearVelocity().y == NOT_IN_AIR){
            // By applying the impulse to the center of the body, the body wont spin/rotate.
            // The last parameter is set to true to wake the body up when applying the impulse. The bodies are sleeping by default.
            b2body.applyLinearImpulse(jumpVelocity, b2body.getWorldCenter(), true);
        }
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
}
