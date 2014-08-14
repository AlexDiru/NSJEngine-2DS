import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 14/01/14
 * Time: 18:38
 * To change this template use File | Settings | File Templates.
 */
public class NSJEngine implements ApplicationListener {

    private SpriteBatch spriteBatch;
    private NSJMap map;
    private Texture projectile;
    private Texture crosshair;
    private NSJPlayer player;
    private NSJCharacter enemy;
    private PerspectiveCamera camera;

    private boolean mouseFlag = false;
    private boolean leftBracketFlag = false;
    private boolean rightBracketFlag = false;
    private boolean minusFlag = false;
    private boolean plusFlag = false;
    private boolean spaceFlag = false;
    private boolean apoFlag = false;
    private boolean hashFlag = false;

    private int x = 0;
    private int y = 0;

    private int moveDir = -1;
    private float distanceMoved = 0f;
    private static final float moveDistance = 32f;

    private Random random = new Random();

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();

        projectile = new Texture("assets/projectile.png");
        crosshair = new Texture("assets/crosshair.png");
        player =  new NSJPlayer(new Texture("assets/player.png"),new Texture("assets/player.png"));

        map = new NSJMap(player);

        enemy = new NSJAI(NSJAI.Test,200,200);

        map.addEntity(1,enemy);
        map.addEntity(1,player);


        NSJGUI.load();
    }

    @Override
    public void resize(int i, int i2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            if (player.canMoveTo(map, player.getX()-1, player.getY()))
                player.increaseX(-1);
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            if (player.canMoveTo(map, player.getX()+1, player.getY()))
                player.increaseX(1);
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            if (player.canMoveTo(map, player.getX(), player.getY()+1))
                player.increaseY(1);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            if (player.canMoveTo(map, player.getX(), player.getY()-1))
                player.increaseY(-1);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT_BRACKET) && leftBracketFlag) {
            ((NSJAI)enemy).iterateAITypeDown();
            leftBracketFlag = false;
        } else if (!Gdx.input.isKeyPressed(Input.Keys.LEFT_BRACKET))
            leftBracketFlag = true;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT_BRACKET) && rightBracketFlag) {
            ((NSJAI)enemy).iterateAITypeUp();
            rightBracketFlag = false;
        } else if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT_BRACKET))
            rightBracketFlag = true;

        if (Gdx.input.isKeyPressed(Input.Keys.MINUS) && minusFlag) {
            player.increaseZ(-1);
            minusFlag = false;
            NSJDebug.write(NSJDebug.NOTIF, "Decreasing Player Z by 1");
        } else if (!Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            minusFlag = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && spaceFlag) {
            player.jump();
            spaceFlag = false;
        } else if (!Gdx.input.isKeyPressed(Input.Keys.SPACE))
            spaceFlag =true;


        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS) && plusFlag) {
            player.increaseZ(1);
            plusFlag = false;
            NSJDebug.write(NSJDebug.NOTIF, "Increasing Player Z by 1");
        } else if (!Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
            plusFlag = true;
        }


        if (Gdx.input.isKeyPressed(Input.Keys.APOSTROPHE)) {
            map.increaseEntityZ(-1);
            NSJDebug.write(NSJDebug.NOTIF,"s");
        }



        if (Gdx.input.isKeyPressed(Input.Keys.SYM))
            map.increaseEntityZ(1);


        //Shoot
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && mouseFlag) {
            mouseFlag = false;
            int destX = (int)(Gdx.input.getX() + player.getX() - (Gdx.graphics.getWidth() >> 1));
            int destY = (int)((Gdx.graphics.getHeight() - Gdx.input.getY()) + player.getY() - (Gdx.graphics.getHeight() >> 1));

            //Adjust destX and destY for crosshair centre offsetaaaaaaaaa
            destX -= crosshair.getWidth() >> 1;
            destY -= crosshair.getHeight() >> 1;

            NSJPair<Float, Float> speeds = NSJProjectile.computeSpeeds(player.getX(), player.getY(), destX, destY, 200f);
            map.addEntity(1, new NSJProjectile(NSJProjectile.Fireball, player.getX(), player.getY(), destX, destY, player));

        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        mouseFlag = true;


        //Projectile Test
        /*if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            float signX = random.nextInt(2) == 1 ? 1 : -1;
            float signY = random.nextInt(2) == 1 ? 1 : -1;
            map.addEntity(3,new NSJProjectile(projectile, random.nextInt(640),random.nextInt(480),16,16,signX * (random.nextInt(5) + 3),signY * (random.nextInt(5) + 3),(float)random.nextInt(300)/100,(float)random.nextInt(300)/100, true));
        }*/
        map.update();

        //Render
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        spriteBatch.begin();

        map.render(spriteBatch, (int)player.getX() - Gdx.graphics.getWidth()/2 + player.getWidth()/2, (int)player.getY() - Gdx.graphics.getHeight()/2 + player.getHeight()/2);

        //Crosshair
        spriteBatch.draw(crosshair, Gdx.input.getX()-8, 480-Gdx.input.getY()-8);


        NSJGUI.render(spriteBatch, player.getHealth(),0,5);

        spriteBatch.end();
    }

    @Override
    public void pause() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resume() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dispose() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
