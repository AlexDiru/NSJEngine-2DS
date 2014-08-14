import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 14/01/14
 * Time: 18:57
 * To change this template use File | Settings | File Templates.
 */
public class Ship {

    private int moveCyclesLeft = 0;
    private Direction moveDirection;

    public void ai() {
        if (moveCyclesLeft > 0) {
            move(moveDirection);
            moveCyclesLeft--;
        }else {
            moveDirection = Math.random() < 0.5f ? Direction.L : Direction.R;
            moveCyclesLeft = 15 +(int) (Math.random() *35);
        }
    }

    public enum Direction
    {
        U,D,L,R
    }

    public Texture texture;

    private float x, y;
    private float speed = 160f;


    private float fireSpeed = 0.5f;
    private float currentFire = 0f;
    private float bulletSpeed = 100f;
    private ShipType shipType;

    public Ship(ShipType shipType, float x, float y, float bulletSpeed, float fireSpeed) {
        if (shipType == ShipType.PLAYER)
            texture = new Texture(Gdx.files.internal("assets/ship.png"));
        else if (shipType == ShipType.BOSS) {
            texture = new Texture(Gdx.files.internal("assets/boss.png"));
            y -= texture.getHeight();
        }
        this.x = x;
        this.y = y;
        this.bulletSpeed = bulletSpeed;
        this.fireSpeed = fireSpeed;
        this.shipType = shipType;
    }

    public enum ShipType {
        BOSS, PLAYER
    }

    public void move(Direction direction) {
        float s = speed * Gdx.graphics.getDeltaTime();
        if (direction == Direction.D)
            y -= s;
        else if (direction == Direction.L)
            x -= s;
        else if (direction == Direction.U)
            y += s;
        else if (direction == Direction.R)
            x += s;

        if (x < 0) {
            x = 0;
        } else if (x > Gdx.graphics.getWidth() - texture.getWidth()) {
            x = Gdx.graphics.getWidth() - texture.getWidth();
        }
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, x, y);
    }

    public Bullet fire() {
        currentFire += Gdx.graphics.getDeltaTime();
        if (currentFire > fireSpeed) {
            currentFire -= fireSpeed;
            if (shipType == ShipType.PLAYER)
                return new Bullet(x + (texture.getWidth() >> 1), y + texture.getHeight(), bulletSpeed, 0, 1, shipType);
            else if (shipType == ShipType.BOSS)
                return new Bullet(x + (texture.getWidth() >> 1), y, -bulletSpeed, 0, 1, shipType);
        }

        return null;
    }

}
