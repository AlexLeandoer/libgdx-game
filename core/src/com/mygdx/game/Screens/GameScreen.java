package com.mygdx.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.mygdx.game.Blob;
import com.mygdx.game.Johny;

import java.util.ArrayList;

public class GameScreen implements Screen {
    Game game;
    Stage stage;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    Johny johny;
    Blob blob, blob2;

    private ArrayList<Rectangle> mapObjectsRects;
    private ArrayList<Rectangle> specialMapObjectsRects;

    Music music = Gdx.audio.newMusic(Gdx.files.internal("data/song.mp3"));

    public GameScreen(Game game) {
        this.game = game;
    }

    public void show() {
        map = new TmxMapLoader().load("level1.tmx");
        final float pixelsPerTile = 16;
        renderer = new OrthogonalTiledMapRenderer(map, 1 / pixelsPerTile);
        camera = new OrthographicCamera();

        stage = new Stage();
        stage.getViewport().setCamera(camera);

        johny = new Johny();
        johny.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        johny.setPosition(14, 10);
        stage.addActor(johny);

        /*
        for(MapObject mapObject : map.getLayers().get("bananas").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) mapObject).getRectangle();
            specialMapObjectsRects.add(rect);
        }
        */

        blob = new Blob();
        blob.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        blob.setPosition(50, 15);
        stage.addActor(blob);

        blob2 = new Blob();
        blob2.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        blob2.setPosition(17, 15);
        stage.addActor(blob2);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Musica
        music.play();

        camera.position.x = johny.getX();
        camera.update();

        renderer.setView(camera);
        renderer.render();

        stage.act(delta);
        stage.draw();

        if (johny.getY()< 0){
            game.setScreen(new GameOverScreen(game));
            music.stop();
        }

        if (blob2.getX()>johny.getX()+1) {
            blob2.isFacingRight=false;
            blob2.xVelocity =  blob2.xVelocity -  blob2.MAX_VELOCITY/3;

        }
    }

    public void dispose() {
    }

    public void hide() {
    }

    public void pause() {
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, 20 * width / height, 20);
    }

    public void resume() {
    }
}
