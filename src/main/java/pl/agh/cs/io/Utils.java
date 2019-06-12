package pl.agh.cs.io;

public class Utils {
    public static Long timeToLong(String input) throws NumberFormatException {
        Long hours;
        Long minutes;
        Long seconds;

        String[] parts = input.split(":");

        if (parts.length < 2 || parts.length > 3) {
            throw new NumberFormatException();
        }

        hours = Long.parseLong(parts[0]);
        if (hours < 0 || hours > 24) {
            throw new NumberFormatException();
        }

        minutes = Long.parseLong(parts[1]);
        if (minutes < 0 || minutes >= 60) {
            throw new NumberFormatException();
        }

        seconds = 0L;
        if (parts.length == 3) {
            seconds = Long.parseLong(parts[2]);
            if (seconds < 0 || seconds >= 60) {
                throw new NumberFormatException();
            }
        }

        Long result = seconds + minutes * 60 + hours * 3600;

        //if more time than it is in one day
        if (result > 24 * 3600 || result <= 0) {
            throw new NumberFormatException();
        }

        return result;
    }
}
