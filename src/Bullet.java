import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 09/03/14
 * Time: 19:34
 * To change this template use File | Settings | File Templates.
 */
public class Bullet {
    private float y;
    private float x;
    private final float vertSpeed;
    private final float horSpeed;
    private final int power;
    private Ship.ShipType shipType;

    public Bullet(float originX, float originY, float vertSpeed, float horSpeed, int power, Ship.ShipType shipType) {
        this.x = originX - (Assets.bulletTexture.getWidth() >> 1);
        this.y = originY;
        this.vertSpeed = vertSpeed;
        this.horSpeed = horSpeed;
        this.power = power;
        this.shipType = shipType;
    }

    public void update() {
        x += (horSpeed * Gdx.graphics.getDeltaTime());
        y += (vertSpeed * Gdx.graphics.getDeltaTime());
    }

    public void render(SpriteBatch batch) {
        if (shipType == Ship.ShipType.PLAYER)
            batch.draw(Assets.bulletTexture,x,y);
        else
            batch.draw(Assets.enemyBulletTexture,x,y);
    }
}
