package pl.agh.cs.io;

public class TimeGuard {

    public static void main(String args[]) {
        Rules rules = new Rules();
        TimeCounterController timeCounterController = new TimeCounterController();
        WindowsListenerRunner.run(snapshot -> {
            rules.accept(snapshot);
            timeCounterController.accept(snapshot.getForegroundWindow(), rules.getRulesCopy());
        });
    }
}
