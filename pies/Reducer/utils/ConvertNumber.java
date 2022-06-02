package pies.Reducer.utils;

public class ConvertNumber {
    public static String Clean(String string) {
        String s = string;

        if (string.length() > 5) s = string.substring(0, 5);
        else if (string.length() == 4) s = s + "0";
        else if (string.length() == 3) s = s + "00";

        return s;
    }
}
