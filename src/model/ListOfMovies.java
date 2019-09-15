package model;

import java.util.ArrayList;
import java.util.Observable;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class ListOfMovies extends ListOfMedias {
    private ArrayList<Movie> movieList;

    public ListOfMovies() {
        super();
        movieList = new ArrayList<>();
    }

    @Override
    public void update(int index, String infoToAdjust, String newInfo) {
        if (infoToAdjust.equals("year")) {
            getMovieList().get(index).setYear(parseInt(newInfo));
        }
        else if (infoToAdjust.equals("box office")) {
            getMovieList().get(index).setBoxOffice(parseDouble(newInfo));
        }
        else if (infoToAdjust.equals("genre")) {
            getMovieList().get(index).addGenre(newInfo);
        }
        else if (infoToAdjust.equals("rt rating")) {
            getMovieList().get(index).setRTRating(parseInt(newInfo));
        }
    }

    public void updateCompany(int index, Company company) {
        getMovieList().get(index).setCompany(company);
    }

    @Override
    public ArrayList<String> getShortForms() {
        ArrayList<String> shortFormList = new ArrayList<>();
        for (Movie m : movieList) {
            shortFormList.add(m.shortFormInfo());
        }
        return shortFormList;
    }

    @Override
    public void updateGenre(int index, boolean toAdd, ArrayList<String> newGenres) {
        for (String g: newGenres) {
            if (toAdd) {
                getMovieList().get(index).addGenre(g);
            }
            else {
                getMovieList().get(index).removeGenre(g);}
        }
    }

    // MODIFIES: this
    // EFFECTS: Add m to this if m is not already in this, otherwise do nothing
    public boolean addToList(Movie m) {
        if (!movieList.contains(m)) {
            movieList.add(m);
            return true;
        }
        else return false;
    }

    //MODIFIES: this
    //EFFECTS: remove m from this if m is in this
    public void removeMovie(Movie m) {
        if (movieList.contains(m)) {
            movieList.remove(m);
        }
    }

    // EFFECTS: if movie with name is in movieList then return the movie's index
    // otherwise return -1
    public int movieIndexFromName(String name) {
        for (Movie m: getMovieList()) {
            if (m.getName().equals(name)) {
                return getMovieList().indexOf(m);
            }
        }

        return -1;
    }

    //EFFECTS: return this.movieList
    public ArrayList<Movie> getMovieList() {
        return movieList;
    }

    public Movie searchByName(String movieName) {
        for (Movie m: movieList) {
            if (m.getName().equals(movieName)) {
                return m;
            }
        }
        return new Movie();
    }

}
