/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 16/07/14
 * Time: 23:52
 * To change this template use File | Settings | File Templates.
 */
public class NSJDebug {

    public static final int NOTIF = 0;


    public static void write(int priority, String msg) {
        System.out.println("[" + priority + "] " + msg);
    }
}
