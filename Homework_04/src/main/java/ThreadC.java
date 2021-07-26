public class ThreadC extends Thread{

    Counter counter3;

    public ThreadC(Counter counter3) { // для того, чтобы вставить ссылку на общий для всех потоков счетчик
        this.counter3 = counter3;
    }

    @Override
    public void run() {

        try {
            synchronized (counter3) {
                for (int i = 0; i < counter3.getRepeats(); i++) {
                    while (counter3.getCounter() != 3) { // пока счетчик не равен 3, паркуем поток
                        try {
                            counter3.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print("C");
                    counter3.setCounter(1);
                    counter3.notifyAll();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
