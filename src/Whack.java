/**
 * Created by Michael on 4/1/17.
 */

public class Whack {

    public static void main (String[] args) {

        System.out.println(Thread.currentThread().getName() + ": RunnableTest");

        for (int i = 0; i < Integer.parseInt(args[0]); i++) {
            new Mole().go();
        }
    }

}
