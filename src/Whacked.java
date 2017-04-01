/**s
 * Created by Andromeda on 4/1/17.
 */

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Whacked {

    static Semaphore semaphore = new Semaphore(4);

    static class Mole extends Thread {

        int id;

        Mole(int id) {
            this.id = id;
        }

        public void run() {
            try {
                System.out.println("Mole " + id + " attempting to acquire lock...");
                semaphore.acquire();
                System.out.println("Mole " + id + " acquired lock!");

                try {
                    System.out.println("Mole " + id + " is doing work...");
                    TimeUnit.SECONDS.sleep(5);
                } finally {
                    System.out.println("Mole " + id + " releasing lock...");
                    semaphore.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main (String[] args) {
        for (int i = 0; i < 10; i++) {
            new Mole(i).start();
        }
    }

}
