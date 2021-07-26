//1. Создать три потока, каждый из которых выводит определенную букву (A, B и C) 5 раз (порядок – ABСABСABС).
// Используйте wait/notify/notifyAll.
//2. На серверной стороне сетевого чата реализовать управление потоками через ExecutorService.


public class MainApp {



    public static void main(String[] args) {

        Counter counter = new Counter(100); // счетчик управляет доступом и количеством повторений

        ThreadA A = new ThreadA(counter);
        ThreadB B = new ThreadB(counter);
        ThreadC C = new ThreadC(counter);

        A.start();
        B.start();
        C.start();



    }


}
