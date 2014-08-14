import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 26/06/14
 * Time: 22:51
 * To change this template use File | Settings | File Templates.
 */
public class NSJGUI {
    private static NSJEntity[] numbers = new NSJEntity[10];

    public static void load() {
        for (int i = 0; i < 10; i++) {
            numbers[i] = new NSJEntity(new Texture("assets/" + i + ".png"),0,0,30,32);
        }
    }

    public static void render(SpriteBatch spriteBatch, int value, int x, int y) {

        char[] valueStr = String.valueOf(value).toCharArray();
        for (int i = 0; i < valueStr.length; i++) {
            int inx = (int)valueStr[i] - 48;
            numbers[inx].setX(x + i * numbers[x].width);
            numbers[inx].setY(y);
            numbers[inx].render(spriteBatch,0,0);
        }

    }
}
