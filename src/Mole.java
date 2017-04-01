import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Michael on 4/1/17.
 */

public class Mole {

    public int id;
    public Random r = new Random();
    public ReentrantLock lock;

    public Mole(int id, ReentrantLock lock) {
        this.id = id;
        this.lock = lock;
        go();
    }

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
            //System.out.println("Thread: " + this.id + " is working. My lock is: " + lock.toString());
            int time = r.nextInt((10 - 1)+1);
            TimeUnit.SECONDS.sleep(time+1);
            //System.out.println("Thread: " + this.id + " is done after " + (time+1) + " seconds.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
