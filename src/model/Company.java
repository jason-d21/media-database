package model;

import java.util.*;

public class Company extends Distributor {
    private Map<String, Movie> movies;

    public Company(String name) {
        super(name);
        this.movies = new HashMap<>();
    }

    public void addMovie(Movie m) {
        if (!movies.containsKey(m.getName())) {
            movies.put(m.getName(), m);
            m.setCompany(this);
        }
    }

    public void removeMovie(Movie m) {
        if (movies.containsKey(m.getName())) {
            movies.remove(m.getName());
            m.removeCompany(this);
        }
    }

    public Map<String, Movie> getMovies() {return movies;}

    @Override
    public List<String> getMediaNames() {
        List<String> keys = new ArrayList<>();
        for (String key: movies.keySet()) {
            keys.add(key);
        }
        return keys;
    }
}
