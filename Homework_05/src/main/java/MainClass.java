//Организуем гонки:
//        Все участники должны стартовать одновременно, несмотря на то, что на подготовку у каждого из них уходит разное время.
//        В туннель не может заехать одновременно больше половины участников (условность).
//        Попробуйте всё это синхронизировать.
//        Только после того как все завершат гонку, нужно выдать объявление об окончании.
//        Можете корректировать классы (в т.ч. конструктор машин) и добавлять объекты классов из пакета util.concurrent.
// Добавить победу


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MainClass {
    public static final int CARS_COUNT = 4;

    public static int getCarsCount() {
        return CARS_COUNT;
    }

    //public static Object mon = new Object();

    public static CountDownLatch countDownLatch = new CountDownLatch(CARS_COUNT); // защелка, чтобы все стартовали из одной точки

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {


        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        Car[] cars = new Car[CARS_COUNT];
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));

        for (int i = 0; i < cars.length; i++) { // цикл, создающий в массиве объекты
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }

        for (int i = 0; i < cars.length; i++) { // цикл, запускающий потоки

            new Thread(cars[i]).start(); // в поток кладем объект, реализующий Runnable
            //cars[i].notifyAll(); // вот такой бред
            //countDownLatch.countDown();
        }


        //countDownLatch.await();
        //Thread.currentThread().wait();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}

