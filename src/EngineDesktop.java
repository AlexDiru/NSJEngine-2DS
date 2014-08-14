import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 14/01/14
 * Time: 18:37
 * To change this template use File | Settings | File Templates.
 */
public class EngineDesktop {
        public static void main (String[] args) {
            new LwjglApplication(new NSJEngine(), "poo", 640,480);
        }
    }
