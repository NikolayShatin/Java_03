import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Car implements Runnable {
    private static int CAR_NUMBER; // переименуем в CAR_NUMBER, чтобы не было путанницы с переменной из MainClass

    static {
        CAR_NUMBER = 0;
    }

    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CAR_NUMBER++;
        this.name = "Участник #" + CAR_NUMBER;
    }


    //CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public void run() {
        try {
            //lock.lock();

            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            //
            MainClass.countDownLatch.countDown();
            MainClass.countDownLatch.await();
            // System.out.println(countDownLatch.getCount());
            // countDownLatch.await();


        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        try {
            MainClass.lock.lock();
            System.out.println("Финишировал " + name);
        } finally {
            MainClass.countDownLatch2.countDown();
            MainClass.lock.unlock();
            try {
                MainClass.countDownLatch2.await();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
