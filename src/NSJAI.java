import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 27/06/14
 * Time: 13:08
 * To change this template use File | Settings | File Templates.
 */
public class NSJAI extends NSJCharacter {
    private static final int FireMode_Standard = 0;
    private static final int FireMode_Jitter = 1;

    private static final int AIType_Doom = 0;
    private static final int AIType_Doom2 = 1;
    private static final int MAX_AI_TYPES = 2;

    private static final float MoveDirectionTimer_MAX = 1.5f;

    private static final Random random = new Random();

    public static final int Test = 0;
    private static final Texture TestTexture = new Texture("assets/enemy.png");
    private static final Texture TestPainTexture = new Texture("assets/enemypain.png");
    private static final int TestFireMode = FireMode_Standard;
    private static final float TestFireRate = 1f;



    private int fireMode;
    private float fireRateTimer = 0;
    private float fireRate;
    private int jitterAmount = 0;
    private int aiType;
    private float speed;


    private boolean moveNorth = false, moveSouth = false, moveEast = false, moveWest = false;
    private float moveDirectionTimer = 0;

    public NSJAI(int id, float x, float y) {
        if (id == Test) {
            construct(TestTexture, x, y);
            painTexture = TestPainTexture;
            fireMode = TestFireMode;
            fireRate = TestFireRate;
        }

        aiType = AIType_Doom;
        speed = 100;
    }

    public void iterateAITypeUp() {
        if (aiType < MAX_AI_TYPES - 1)
            aiType++;
        else
            aiType = 0;

        NSJDebug.write(NSJDebug.NOTIF, "AI Type switched to " + aiType);
    }

    public void iterateAITypeDown() {
        if (aiType > 0)
            aiType--;
        else
            aiType = MAX_AI_TYPES - 1;

        NSJDebug.write(NSJDebug.NOTIF, "AI Type switched to " + aiType);
    }

    private static NSJPair<Float, Float> applyJitter(float x, float y, int jitter) {
        return new NSJPair<Float, Float>(x + (random.nextInt(jitter) - (jitter >> 1)),y + (random.nextInt(jitter) - (jitter >> 1) ));
    }

    public void updateAI(NSJMap map, NSJEntity target) {

        if (!canSee(target, map))
            return;

        if (aiType == AIType_Doom)
            behaveAsDoom(map, target);
        else if (aiType == AIType_Doom2)
            behaveAsDoom2(map, target);



        if (fireRateTimer > 0) {
            fireRateTimer -= Gdx.graphics.getDeltaTime();
            return;
        }

        if (fireMode == FireMode_Standard)
            map.addEntity(1, new NSJProjectile(NSJProjectile.Fireball, x,y, target.getX(), target.getY(), this));

        if (fireMode == FireMode_Jitter) {
            NSJPair<Float, Float> jitteredCoords = applyJitter(target.getX(),target.getY(), jitterAmount);
            map.addEntity(1, new NSJProjectile(NSJProjectile.Fireball,x,y,target.getX() + (random.nextInt(3) - 1), target.getY() + (random.nextInt(3) - 1), this));
        }

        fireRateTimer += fireRate;
    }

    private boolean canSee(NSJEntity target, NSJMap map) {
        return true;
    }

    private static final int NE = 0, NW = 1, N = 2, E = 3, S = 4, SW = 5, SE = 6, W = 7;

    private int getRandomCardinalOrdinal() {
        return random.nextInt(8);
    }

    private int getRandomDirection(int d1, int d2, int d3) {
        int r = random.nextInt(3);

        if (r == 0)
            return d1;
        else if (r == 1)
            return d2;
        else
            return d3;
    }

    private void behaveAsDoom2(NSJMap map, NSJEntity target) {
        //Move towards target with a cardinal/ordinal direction


        float timerSpeed = speed * Gdx.graphics.getDeltaTime();

        if (moveDirectionTimer <= 0) {

            moveNorth = false;
            moveSouth = false;
            moveEast = false;
            moveWest = false;

            int newMoveDirection = 0;

            if (random.nextInt(2) == 1) {
                if (target.getX() < x)
                    newMoveDirection = getRandomDirection(NE,E,SE);
                else if (target.getX() > x)
                    newMoveDirection = getRandomDirection(NW,W,SW);
            } else {
                if (target.getY() < y)
                    newMoveDirection = getRandomDirection(SE, S, SW);
                else if (target.getY() > y)
                    newMoveDirection = getRandomDirection(NE, N, NW);
            }

            if (newMoveDirection == NE) {
                moveNorth = true;
                moveEast = true;
            } else if (newMoveDirection == N) {
                moveNorth = true;
            } else if (newMoveDirection == NW) {
                moveNorth = true;
                moveWest = true;
            } else if (newMoveDirection == E) {
                moveEast = true;
            } else if (newMoveDirection == SE) {
                moveSouth = true;
                moveEast = true;
            } else if (newMoveDirection == SW) {
                moveSouth = true;
                moveWest = true;
            } else if (newMoveDirection == S) {
                moveSouth = true;
            } else if (newMoveDirection == W) {
                moveWest = true;
            }

            moveDirectionTimer = MoveDirectionTimer_MAX;
        }

        boolean failedMove = false;

        if (moveEast)
            if (canMoveTo(map, x - timerSpeed, y))
                x -= timerSpeed;
            else
                failedMove = true;

        if (moveWest)
            if (canMoveTo(map, x + timerSpeed, y))
                x += timerSpeed;
            else
                failedMove = true;


        if (moveNorth)
            if (canMoveTo(map, x, y + timerSpeed))
                y += timerSpeed;
            else
                failedMove = true;

        if (moveSouth)
            if (canMoveTo(map, x, y - timerSpeed))
                y -= timerSpeed;
            else
                failedMove = true;

        if (failedMove) {
            moveNorth = false;
            moveSouth = false;
            moveEast = false;
            moveWest = false;

            int newMoveDirection = getRandomCardinalOrdinal();

            if (newMoveDirection == NE) {
                moveNorth = true;
                moveEast = true;
            } else if (newMoveDirection == N) {
                moveNorth = true;
            } else if (newMoveDirection == NW) {
                moveNorth = true;
                moveWest = true;
            } else if (newMoveDirection == E) {
                moveEast = true;
            } else if (newMoveDirection == SE) {
                moveSouth = true;
                moveEast = true;
            } else if (newMoveDirection == SW) {
                moveSouth = true;
                moveWest = true;
            } else if (newMoveDirection == S) {
                moveSouth = true;
            } else if (newMoveDirection == W) {
                moveWest = true;
            }

            moveDirectionTimer = MoveDirectionTimer_MAX;
        }


        moveDirectionTimer -= Gdx.graphics.getDeltaTime();


    }

    //http://doom.wikia.com/wiki/Monster_behavior
    private void behaveAsDoom(NSJMap map, NSJEntity target) {
        //Move towards target with a cardinal/ordinal direction


        float timerSpeed = speed * Gdx.graphics.getDeltaTime();

        if (moveDirectionTimer <= 0) {
            moveNorth = false;
            moveSouth = false;
            moveEast = false;
            moveWest = false;

            if (target.getX() < x)
                moveEast = true;
            else if (target.getX() > x)
                moveWest = true;

            if (target.getY() < y)
                moveSouth = true;
            else if (target.getY() > y)
                moveNorth = true;

            moveDirectionTimer = MoveDirectionTimer_MAX;
        }

        boolean failedMove = false;

        if (moveEast)
            if (canMoveTo(map, x - timerSpeed, y))
                x -= timerSpeed;
            else
                failedMove = true;

        if (moveWest)
            if (canMoveTo(map, x + timerSpeed, y))
                x += timerSpeed;
            else
                failedMove = true;


        if (moveNorth)
            if (canMoveTo(map, x, y + timerSpeed))
                y += timerSpeed;
            else
                failedMove = true;

        if (moveSouth)
            if (canMoveTo(map, x, y - timerSpeed))
                y -= timerSpeed;
            else
                failedMove = true;

        if (failedMove) {
            moveNorth = false;
            moveSouth = false;
            moveEast = false;
            moveWest = false;
            //Random direction
            if (random.nextInt(2) == 1)
                moveWest = true;
            else
                moveEast = true;

            if (random.nextInt(2) == 1)
                moveNorth = true;
            else
                moveSouth = true;

            moveDirectionTimer = MoveDirectionTimer_MAX;
        }


        moveDirectionTimer -= Gdx.graphics.getDeltaTime();


    }
}
