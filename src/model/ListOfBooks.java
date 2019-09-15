package model;

import java.util.ArrayList;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class ListOfBooks extends ListOfMedias {
    private ArrayList<Book> bookList;

    public ListOfBooks() {
        super();
        bookList = new ArrayList<>();
    }

    @Override
    public void update (int index, String infoToAdjust, String newInfo) {
        if (infoToAdjust.equals("year")) {
            getBookList().get(index).setYear(parseInt(newInfo));
        }
        else if (infoToAdjust.equals("author")) {
            getBookList().get(index).setAuthor(newInfo);
        }
        else if (infoToAdjust.equals("pages")) {
            getBookList().get(index).setPages(parseInt(newInfo));
        }
        else if (infoToAdjust.equals("genre")) {
            getBookList().get(index).addGenre(newInfo);
        }
        else if (infoToAdjust.equals("is best seller")) {
            getBookList().get(index).setIsBestSeller(parseBoolean(newInfo));
        }
    }

    public void updatePublisher(int index, Publisher publisher) {
        getBookList().get(index).setPublisher(publisher);
    }

    @Override
    public ArrayList<String> getShortForms() {
        ArrayList<String> shortFormList = new ArrayList<>();
        for (Book b : bookList) {
            shortFormList.add(b.shortFormInfo());
        }
        return shortFormList;
    }

    @Override
    public void updateGenre(int index, boolean toAdd, ArrayList<String> newGenres) {
        for (String g: newGenres) {
            if (toAdd) {
                getBookList().get(index).addGenre(g);
            }
            else {
                getBookList().get(index).removeGenre(g);}
        }
    }

    // MODIFIES: this
    // EFFECTS: Add m to this if m is not already in this, otherwise do nothing
    public boolean addToList(Book b) {
        if (!bookList.contains(b)) {
            bookList.add(b);
            return true;
        }
        else return false;
    }

    //MODIFIES: this
    //EFFECTS: remove m from this if m is in this
    public void removeFromList(Book b) {
        if (bookList.contains(b)) {
            bookList.remove(b);
        }
    }

    // EFFECTS: if movie with name is in bookList then return the movie's index
    // otherwise return -1
    public int bookIndexFromName(String name) {
        for (Book b: getBookList()) {
            if (b.getName().equals(name)) {
                return getBookList().indexOf(b);
            }
        }

        return -1;
    }

    //EFFECTS: return this.bookList
    public ArrayList<Book> getBookList() {
        return bookList;
    }

    public Book searchByName(String bookName) {
        for (Book b: bookList) {
            if (b.getName().equals(bookName)) {
                return b;
            }
        }
        return new Book();
    }
}
