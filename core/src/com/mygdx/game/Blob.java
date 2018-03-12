package com.mygdx.game;



import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Blob extends Image {
    TextureRegion stand,jump;
    Animation walk;

    float time = 0;
    public float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;
    public boolean isFacingRight = true;
    public TiledMapTileLayer layer;

    final float GRAVITY = -1.5f;
    public final float MAX_VELOCITY = 10f;
    final float DAMPING = 0.87f;

    public Blob() {
        final float width = 21;
        final float height = 33;
        this.setSize(1, height / width);

        Texture koalaTexture = new Texture("walkblob.png");
        TextureRegion[][] grid = TextureRegion.split(koalaTexture, (int) width, (int) height);
        stand = grid[0][2];
        jump = grid[0][1];
        walk = new Animation(0.15f,grid[0][3], grid[0][4],grid[0][5],grid[0][6],grid[0][7]);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    public void act(float delta) {
        time = time + delta;

        boolean upTouched = Gdx.input.isTouched() && Gdx.input.getY() < Gdx.graphics.getHeight() / 2;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || upTouched) {
            if (canJump) {
                yVelocity = yVelocity + MAX_VELOCITY * 4;
            }
            canJump = false;
        }

        yVelocity = yVelocity + GRAVITY;

        float x = this.getX();
        float y = this.getY();
        float xChange = xVelocity * delta;
        float yChange = yVelocity * delta;

        if (canMoveTo(x + xChange, y, false) == false) {
            xVelocity = xChange = 0;
        }

        if (canMoveTo(x, y + yChange, yVelocity > 0) == false) {
            canJump = yVelocity < 0;
            yVelocity = yChange = 0;
        }

        this.setPosition(x + xChange, y + yChange);

        xVelocity = xVelocity * DAMPING;
        if (Math.abs(xVelocity) < 0.5f) {
            xVelocity = 0;
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame;


        if (yVelocity != 0) {
            frame = jump;
        } else if (xVelocity != 0) {
            frame = (TextureRegion) walk.getKeyFrame(time);
        } else {
            frame = stand;
        }

            if (isFacingRight) {
                batch.draw(frame, this.getX(), this.getY(), this.getWidth(), this.getHeight());
            } else {
                batch.draw(frame, this.getX() + this.getWidth(), this.getY(), -1 * this.getWidth(), this.getHeight());
            }
        }


    private boolean canMoveTo(float startX, float startY, boolean shouldDestroy) {
        float endX = startX + this.getWidth();
        float endY = startY + this.getHeight();

        int x = (int) startX;
        while (x < endX) {

            int y = (int) startY;
            while (y < endY) {
                if (layer.getCell(x, y) != null) {
                    if (shouldDestroy) {
                        //layer.setCell(x, y, null);
                    }
                    return false;
                }
                y = y + 1;
            }
            x = x + 1;
        }

        return true;
    }
}