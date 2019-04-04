package pl.agh.cs.io;

import java.util.List;

public class TimeGuard {
    public static void main(String args[]){
        WindowsListenerRunner.run(s -> System.out.println(s.getAllWindows()));
    }
}
