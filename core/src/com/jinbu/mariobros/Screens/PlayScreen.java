package com.jinbu.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jinbu.mariobros.MarioBros;
import com.jinbu.mariobros.Scenes.Hud;

/**
 * Created by 15049051 on 29/04/2017.
 */
public class PlayScreen implements Screen {
    private MarioBros game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    // load our map into the game
    private TmxMapLoader maploader;

    // reference to the map itself
    private TiledMap map;

    // This is what renders our map to the screen
    private OrthogonalTiledMapRenderer renderer;

    public PlayScreen(MarioBros game){
        this.game = game;
        // Set screen color to black
        Gdx.gl.glClearColor(0, 0, 0, 1);

        gameCam = new OrthographicCamera();
//        gamePort = new StretchViewport(800, 480, gameCam); stretches to the screen size
//        gamePort = new ScreenViewport(gameCam); // shows more when the screen is bigger
        gamePort = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, gameCam); // add bars to the screen to keep the ratio.
        hud = new Hud(game.batch);

        maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
    }

    public void update(float dt){
        // check if any input is happening
        handleInput(dt);

        // update cam
        gameCam.update();

        renderer.setView(gameCam);
    }

    private void handleInput(float dt){
        if(Gdx.input.isTouched()){
            gameCam.position.x += 100 * dt; // temp
        }
    }

    @Override
    public void show() {
        System.out.println("show");
    }

    @Override
    public void render(float delta) {
        update(delta);
        // Clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        // render only what the camera can see rather than everything
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {
        System.out.println("pause");
        System.out.println("Making big changes");
    }

    @Override
    public void resume() {
        System.out.println("resume");
    }

    @Override
    public void hide() {
        System.out.println("hide");
    }

    @Override
    public void dispose() {
        System.out.println("dispose");
    }
}
