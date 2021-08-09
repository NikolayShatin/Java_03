public class Player {
    private String name;
    private String instrument;
    private boolean isHigh;

    public Player(String name, String instrument) {
        this.name = name;
        this.instrument = instrument;
        this.isHigh = true;
    }

    public void setHigh(boolean high) {
        isHigh = high;
    }

    public boolean isHigh() {
        return isHigh;
    }

    public String getName() {
        return name;
    }

    public String getInstrument() {
        return instrument;
    }
}
