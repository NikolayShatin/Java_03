public class ThreadA extends Thread {
    Counter counter1;

    public ThreadA(Counter counter1) { // для того, чтобы вставить ссылку на общий для всех потоков счетчик
        this.counter1 = counter1;
    }

    @Override
    public void run() {

        try {
            synchronized (counter1) {
                for (int i = 0; i < counter1.getRepeats(); i++) {
                    while (counter1.getCounter() != 1) { // пока счетчик не равен 1, паркуем поток
                        try {
                            counter1.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print("A");
                    counter1.setCounter(2);
                    counter1.notifyAll();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
