import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 14/01/14
 * Time: 18:38
 * To change this template use File | Settings | File Templates.
 */
public class Engine implements ApplicationListener {

    private SpriteBatch spriteBatch;
    private Background background;
    private Ship ship;
    private Ship boss;

    private List<Bullet> bullets;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();

        background = new Background(new Texture(Gdx.files.internal("assets/stars.png")),-5f);
        ship = new Ship(Ship.ShipType.PLAYER,0,0,500f,0.5f);
        boss = new Ship(Ship.ShipType.BOSS,0,Gdx.graphics.getHeight(),500f,0.25f);
        bullets = new ArrayList<Bullet>();
    }

    @Override
    public void resize(int i, int i2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void render() {
        background.update();

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            ship.move(Ship.Direction.U);

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            ship.move(Ship.Direction.L);

        if (Gdx.input.isKeyPressed(Input.Keys.S))
            ship.move(Ship.Direction.D);

        if (Gdx.input.isKeyPressed(Input.Keys.D))
            ship.move(Ship.Direction.R);

        boss.ai();

        Bullet playerBullet = ship.fire();
        if (playerBullet != null)
            bullets.add(playerBullet);

        Bullet enemyBullet = boss.fire();
        if (enemyBullet != null)
            bullets.add(enemyBullet);

        for (Bullet bullet : bullets)
            bullet.update();

        spriteBatch.begin();
        background.render(spriteBatch);
        ship.render(spriteBatch);
        boss.render(spriteBatch);

        for (Bullet bullet : bullets)
            bullet.render(spriteBatch);

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
