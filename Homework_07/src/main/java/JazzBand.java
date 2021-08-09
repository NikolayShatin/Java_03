import java.util.ArrayList;
import java.util.Random;

public class JazzBand {
    private double income; // выручка группы
    private int fanCount; // количество поклонников
    private String bandName; // название группы
    private int daysExists; // сколько дней существует
    private ArrayList<Player> band = new ArrayList<>(); // массив музыкантов
    private int albumRecorded; // количество записанных альбомов
    private int concertsTotal; // количество отыгранных концертов
    private int interviews;// количество данных интервью
    private Random random = new Random();

    public JazzBand(String bandName) {
        this.bandName = bandName;
    }


    @Test (value = 2)
    public double playConcert(int ticketPrice) {
        int isHigh = 0;

        if (!isBandFormed()) {
            System.out.println("Band is not formed");
            return 0;
        }

        for (Player player : band) {
            if (player.isHigh()) {
                isHigh++;
            }
        }
        if (isHigh + 1 > band.size() / 2) {
            System.out.println("The concert was canceled because to much band musicians were not in good condition");
            fanCount -= 100;
            daysExists++;
            return 0;
        }

        int audience = (int) (fanCount * 0.1) + random.nextInt(100);

        income += audience * ticketPrice * 0.5;
        daysExists++;
        concertsTotal++;

        System.out.println("A concert was held. The band " + bandName + " earned $" + (audience * ticketPrice * 0.5));
        System.out.println("The total income is now " + income);
        for (Player player : band) {
            player.setHigh(true);
        }
        return income;
    }

    @Test (value = 3)
    public boolean createNewAlbum() {
        if (!isBandFormed()) {
            System.out.println("Band is not formed");
            return false;
        }
        System.out.println("The band starts recording new album");
        for (Player player : band) {
            if (player.isHigh()) {
                System.out.println("Musician " + player.getName() + " plays on his " + player.getInstrument() + " like a God");
                player.setHigh(false);
            } else {
                System.out.println("Some of the musicians are in a state of creative crisis. " +
                        "The recording of the album is postponed");
                income -= 20_000;
                daysExists += 30;
                System.out.println("The total income is now " + income);
                System.out.println("*********************************");
                return false;
            }
        }


        daysExists += 100;
        income += 50_000;
        fanCount += 1000;
        albumRecorded++;

        System.out.println("New album was created");
        System.out.println("The total income is now " + income);
        System.out.println("The fans count is now " + fanCount);
        System.out.println("*********************************");
        return true;
    }

    @Test (value = 4)
    public boolean goTouring(int concertsCount, int ticketPrice) {
        if (!isBandFormed()) {
            System.out.println("Band is not formed");
            return false;
        }
        System.out.println("The band starts tour consists of " + concertsCount + " concerts");
        for (int i = 0; i < concertsCount; i++) {
            playConcert(ticketPrice);
        }


        System.out.println("The band " + bandName + " has finished touring");
        System.out.println("*********************************");
        return true;
    }

    @Test (value = 5)
    public void giveInterview() {
        for (Player player : band) {
            player.setHigh(true);
        }

        System.out.println("Band " + bandName + " gave an interview to the New York Times magazine");
        fanCount += random.nextInt(2000);

        income -= fanCount * 0.5;
        daysExists++;
        interviews++;

        System.out.println("The fans count is now " + fanCount);
        System.out.println("The total income is now " + income);

        System.out.println("*********************************");

        for (Player player : band) {
            player.setHigh(random.nextBoolean());

        }
    }

    @AfterSuite (value = 9)
    public boolean disBand() {
        if (income >= 1_000_000 * band.size() && income>0) {
            System.out.println("Facebook message: After " + daysExists / 365 + " years of existance, " +
                    "legendary band " + bandName + " decided to disband due to inner contradiction. \n" +
                    "Was recorded " + albumRecorded + " platinum albums, " + interviews + " interviews were given and " + concertsTotal + " concerts were played." +
                    " Thanks to all " + fanCount + " fans!");
            System.out.println("*********************************");
            band.clear();

            return true;
        }
        if (income <= -10_000) {
            System.out.println("Facebook message: After " + daysExists / 365 + " years of existance " +
                    "band " + bandName + " decided to disband due to financial issues");
            System.out.println("*********************************");
            return true;
        }
        return false;
    }

    @BeforeSuite (value = 1)
    private boolean isBandFormed() {
        return band.size() >= 3;
    }

    @Test (value = 6)
    public void addPlayerToBand(Player player) {
        band.add(player);
    }

    @Test (value = 7)
    public void bandInfo() {
        System.out.println("The band " + "'" + bandName + "'" + " consists of " + band.size() + " musicians");
        for (int i = 0; i < band.size(); i++) {
            System.out.println("Musician " + (i + 1) + ": " + band.get(i).getName() + " plays " + band.get(i).getInstrument());
        }

        System.out.println();
    }

    @Test (value = 8)
    public ArrayList<Player> getBand() {
        return band;
    }
}
