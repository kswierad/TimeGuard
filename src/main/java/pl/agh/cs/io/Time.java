package pl.agh.cs.io;

public class Time {
    private int fg, bg;

    public Time() {
        this.fg = 0;
        this.bg = 0;
    }

    public void addFgTime() {
        this.fg += 1;
        this.bg += 1;
    }

    public void addBgTime() {
        this.bg += 1;
    }

    public int getBg() {
        return bg;
    }

    public int getFg() {
        return fg;
    }
}
