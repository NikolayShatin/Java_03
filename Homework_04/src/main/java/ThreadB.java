public class ThreadB extends Thread {

    Counter counter2;

    public ThreadB(Counter counter2) { // для того, чтобы вставить ссылку на общий для всех потоков счетчик
        this.counter2 = counter2;
    }

    @Override
    public void run() {

        try {
            synchronized (counter2) {
                for (int i = 0; i < counter2.getRepeats(); i++) {
                    while (counter2.getCounter() != 2) { // пока счетчик не равен 2, паркуем поток
                        try {
                            counter2.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print("B");
                    counter2.setCounter(3);
                    counter2.notifyAll();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
