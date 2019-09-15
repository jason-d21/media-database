package model;

import java.util.ArrayList;

public class Genres {
    private ArrayList<String> genre;

    public Genres() {
        genre = new ArrayList<>();
    }

    public Genres(ArrayList<String> genre) {
        this.genre = genre;
    }

    public boolean containsGenre(String genre) {
        for (String g: this.genre) {
            if (g.equals(genre)) {
                return true;
            }
        }

        return false;
    }

    public void removeGenre(String genre) {
        for (int i = 0; i < this.genre.size(); i++) {
            if (this.genre.get(i).equals(genre)) {
                this.genre.remove(i);
            }
        }
    }

    public void addGenre(String genre) {
        if (!this.genre.contains(genre)) {
            this.genre.add(genre);
        }
    }






    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }
}
