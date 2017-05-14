package com.jinbu.mariobros.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jinbu.mariobros.MarioBros;

/**
 * Created by 15049051 on 29/04/2017.
 */
public class Hud implements Disposable{
    public Stage stage;
    // When our game world moves, we want to hud to stay on its place.
    // In order to do that, we will create a different camera for the hud.
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private Integer score;

    // create widget
    private Label countdownLabel;
    private Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;

    public Hud(SpriteBatch sb){
        worldTimer  = 300;
        timeCount   = 0;
        score       = 0;

        viewport    = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, new OrthographicCamera());
        stage       = new Stage(viewport, sb);

        // Stage is like an empty box. If we want to place widgets like labels in there, it will fall and have no organization.
        // In order to provide some sort of organization, we can place table in the stage. Then we can layout the table to organize the labels in
        // certain position.

        Table table = new Table();
        // By default it will align in the center of the stage, but when we say table.top, it will align on the top of the stage.
        table.top();
        // The table is now the size of the stage.
        table.setFillParent(true);

        countdownLabel  = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel      = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel       = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel      = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel      = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel      = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
