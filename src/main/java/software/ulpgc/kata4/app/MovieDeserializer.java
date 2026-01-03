package software.ulpgc.kata4.app;

import software.ulpgc.kata4.model.Movie;

public class MovieDeserializer {
    public static Movie fromTsv(String line) {
        return fromTsv(line.split("\t"));
    }

    public static Movie fromTsv(String[] split) {
        return new Movie(split[2], toInteger(split[5]), toInteger(split[7]));
    }

    public static int toInteger(String s) {
        return s.equals("\\N") ? -1 : Integer.parseInt(s);
    }

}
