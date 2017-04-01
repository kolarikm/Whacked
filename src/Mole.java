import java.util.concurrent.TimeUnit;
import java.util.Random;

/**
 * Created by Andromeda on 4/1/17.
 */
public class Mole {

    public Random r = new Random();

    public void go() {
        Runnable moleTask = () -> {
            boolean running = true;
            while (running) {
                running = work();
            }
        };
        new Thread(moleTask).start();
    }

    public boolean work() {
        try {
            System.out.println(Thread.currentThread().getName() + " is working");
            int time = r.nextInt((10 - 1)+1);
            TimeUnit.SECONDS.sleep(time+1);
            System.out.println(Thread.currentThread().getName() + " is done after " + (time+1) + " seconds");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
