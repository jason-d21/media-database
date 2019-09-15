package model;

import java.util.ArrayList;

public class Book extends Media {
    private String author;
    private int pages;
    private boolean isBestSeller;
    private Publisher publisher;

    // INVARIANT: rTRating <= 100


    // EFFECTS: construct new Movie with values corresponding to arguments
    public Book(String name, String author, int year, int pages, ArrayList<String> genre, boolean isBestSeller, Publisher publisher) {
        super(name, year, genre);
        this.author = author;
        this.pages = pages;
        this.isBestSeller = isBestSeller;
        this.publisher = publisher;
        publisher.addBook(this);
    }

    // EFFECTS: construct new Movie with blank values
    public Book() {
        super();
        author = "";
        pages = 0;
        isBestSeller = false;
        publisher = new Publisher("");
    }

    // EFFECTS: return true if movie is blank (no information), false otherwise
    @Override
    public boolean isBlank() {
        return isBlankMedia() &&
        author.equals("") &&
        pages == 0 &&
        !isBestSeller &&
        publisher.getName().equals("");
    }

    public boolean isSameBook(Book b) {
        return isSameMedia(b) &&
                author.equals(b.getAuthor()) &&
                pages == b.getPages() &&
                isBestSeller == b.getIsBestSeller() &&
                publisher.equals(b.getPublisher());
    }


    // EFFECTS: returns name of this

    public String getAuthor() {
        return author;
    }

    // EFFECTS: returns boxOffice of this
    public int getPages() {
        return pages;
    }

    // EFFECTS: returns rTRating of this

    public boolean getIsBestSeller() {
        return isBestSeller;
    }

    // EFFECTS: returns publisher of this
    public Publisher getPublisher() {
        return publisher;
    }

    // MODIFIES: this
    // EFFECTS: set this.name to newName
    public void setAuthor(String newAuthor) {
        this.author = newAuthor;
    }

    // MODIFIES: this
    // EFFECTS: set this.boxOffice to newBoxOffice
    public void setPages(int newPages) {
        this.pages = newPages;
    }

    // MODIFIES: this
    // EFFECTS: set this.rTRating to newRTRating
    public void setIsBestSeller(boolean newIsBestSeller) {
        this.isBestSeller = newIsBestSeller;
    }

    // MODIFIES: this
    // EFFECTS: set this.publisher to newCompany
    public void setPublisher(Publisher newPublisher) {
        if (!this.publisher.equals(newPublisher)) {
            removePublisher(this.publisher);
            this.publisher = newPublisher;
            newPublisher.addBook(this);
        }
    }


    @Override
    public String shortFormInfo() {
        if (name.equals("") || author.equals("")) return "";
        else  return author + "'s " + name;
    }

    public void removePublisher(Publisher publisher) {
        if (this.publisher.equals(publisher)) {
            this.publisher = new Publisher("");
            publisher.removeBook(this);
        }
    }
}
