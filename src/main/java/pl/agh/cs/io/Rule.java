package pl.agh.cs.io;

public class Rule {
    private String exePath;
    private Time time;

    public Rule(String path){
        this.exePath = path;
        this.time = new Time();
    }

    public void handle(State state) {
        switch (state){
            case FG:
                time.addFgTime();
                break;
            case BG:
                time.addBgTime();
                break;
        }
    }

    public String getExePath() {
        return exePath;
    }

    public Time getTime() {
        return time;
    }
}
