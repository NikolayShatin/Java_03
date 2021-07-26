public class Counter {

    private int counter = 1;
    private int repeats;


    public Counter(int repeats) {
        this.repeats = repeats;
    }


    public int getRepeats() {
        return repeats;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
