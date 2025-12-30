package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Movie;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

public class RemoteMovieLoader implements MovieLoader {
    private static final String url = "https://datasets.imdbws.com/title.basics.tsv.gz";
    private final Function<String, Movie> deserializer;

    public RemoteMovieLoader(Function<String, Movie> deserializer) {
        this.deserializer = deserializer;
    }

    public List<Movie> loadAll() {
        try {
            return loadFrom(new URL(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Movie> loadFrom(URL url) throws IOException {
        return loadFrom(url.openConnection());
    }

    private List<Movie> loadFrom(URLConnection urlConnection) throws IOException {
        try (InputStream inputStream = unzip(urlConnection.getInputStream())){
            return loadFrom(inputStream);
        }
    }

    private List<Movie> loadFrom(InputStream inputStream) throws IOException {
        return loadFrom(toReader(inputStream));
    }

    private List<Movie> loadFrom(BufferedReader reader) throws IOException {
        List<Movie> movies = new ArrayList<>();
        reader.readLine();
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            movies.add(deserializer.apply(line));
        }
        return movies;
    }

    private BufferedReader toReader(InputStream inputStream) throws IOException {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private GZIPInputStream unzip(InputStream inputStream) throws IOException {
        return new GZIPInputStream(new BufferedInputStream(inputStream));
    }
}
