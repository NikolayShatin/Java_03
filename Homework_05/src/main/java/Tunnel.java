import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
        Semaphore semaphore = new Semaphore(MainClass.getCarsCount()/2); // ограничим доступ к туннелю через семафор

    // проверка, удалить.
    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }
    @Override
    public void go(Car c) {

        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                semaphore.acquire(); // остановим гонщика перед въездом в туннель, если внутри больше половины
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {

                System.out.println(c.getName() + " закончил этап: " + description);
                semaphore.release();// освободим место в туннеле

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}