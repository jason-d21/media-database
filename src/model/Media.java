package model;

import java.util.ArrayList;

public abstract class Media {

    protected String name;
    protected int year;
    protected Genres genre;

    public Media(String name, int year, ArrayList<String> genre) {
        this.name = name;
        this.year = year;
        this.genre = new Genres(genre);
    }

    public Media() {
        this.name = "";
        this.year = 0;
        this.genre = new Genres();
    }

    public boolean isBlankMedia() {
        return this.name.equals("") &&
                this.year == 0 &&
                this.genre.getGenre().isEmpty();
    }

    public abstract boolean isBlank();

    public boolean isSameMedia(Media m) {
        return this.name.equals(m.getName()) &&
                this.year == m.getYear() &&
                this.genre.equals(m.getGenre());
    }

    // MODIFIES: this
    // EFFECTS: set this.year to newYear
    public void setYear (int newYear) {
        this.year = newYear;
    }

    // MODIFIES: this
    // EFFECTS: set this.genre to newGenre
    public void setGenre(ArrayList<String> newGenre) {
        this.genre.setGenre(newGenre);
    }

    // EFFECTs: returns true if this is of the genre, false otherwise
    public boolean isOfGenre(String genre) {
        return this.genre.containsGenre(genre);
    }

    public abstract String shortFormInfo();

    // EFFECTS: returns name of this
    public String getName() {
        return name;
    }

    // EFFECTS: returns year of this
    public int getYear() {
        return year;
    }

    // EFFECTS: returns genre of this
    public Genres getGenre() {
        return genre;
    }

    // MODIFIES: this
    // EFFECTS: if genre is in this.genre, remove it from this.genre,
    // otherwise do nothing
    public void removeGenre(String genre) {
        this.genre.removeGenre(genre);
    }

    // MODIFIES: this
    // EFFECTS: addToList genre to this.genre if not already in list
    public void addGenre(String genre) {
        this.genre.addGenre(genre);
    }


    // MODIFIES: this
    // EFFECTS: set this.name to newName
    public void setName(String newName) {
        this.name = newName;
    }
}
