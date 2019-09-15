package model;

import java.util.ArrayList;

public class Movie extends Media implements Entertainment {
    private double boxOffice;
    private int rTRating;
    private Company company;

    // INVARIANT: rTRating <= 100


    // EFFECTS: construct new Movie with values corresponding to arguments
    public Movie(String name, int year, double boxOffice, ArrayList<String> genre, int rTRating, Company company) {
        super(name, year, genre);
        this.boxOffice = boxOffice;
        this.rTRating = rTRating;
        this.company = company;
        company.addMovie(this);
    }

    // EFFECTS: construct new Movie with blank values
    public Movie() {
        super();
        boxOffice = 0;
        rTRating = 0;
        company = new Company("");
    }

    // EFFECTS: return true if movie is blank (no information), false otherwise
    @Override
    public boolean isBlank() {
        return isBlankMedia() &&
        boxOffice == 0 &&
        rTRating == 0 &&
        company.getName().equals("");
    }

    public boolean isSameMovie(Movie m) {
        return isSameMedia(m) &&
                boxOffice == m.getBoxOffice() &&
                rTRating == m.getRTRating() &&
                company.equals(m.getCompany());
    }



    // EFFECTS: returns boxOffice of this
    public double getBoxOffice() {
        return boxOffice;
    }


    // EFFECTS: returns rTRating of this

    public int getRTRating() {
        return rTRating;
    }

    // EFFECTS: returns company of this
    public Company getCompany() {
        return company;
    }



    // MODIFIES: this
    // EFFECTS: set this.boxOffice to newBoxOffice
    public void setBoxOffice(double newBoxOffice) {
        this.boxOffice = newBoxOffice;
    }


    // MODIFIES: this
    // EFFECTS: set this.rTRating to newRTRating

    public void setRTRating(int newRating) {
        this.rTRating = newRating;
    }

    // MODIFIES: this
    // EFFECTS: set this.company to newCompany
    public void setCompany(Company newCompany) {
        if (!company.equals(newCompany)) {
            removeCompany(this.company);
            this.company = newCompany;
            newCompany.addMovie(this);
        }
    }


    @Override
    public boolean isAcclaimed() {
        return rTRating >= 80;
    }

    @Override
    public String shortFormInfo() {
        if (name.equals("") || year == 0) return "";
        else  return name + " (" + year + ")";
    }

    public void removeCompany(Company company) {
        if (this.company.equals(company)) {
            this.company = new Company("");
            company.removeMovie(this);
        }
    }
}
