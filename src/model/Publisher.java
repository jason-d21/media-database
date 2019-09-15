package model;

import java.util.*;

public class Publisher extends Distributor {
    private Map<String, Book> books;

    public Publisher(String name) {
        super(name);
        books = new HashMap<>();
    }

    public void addBook(Book b) {
        if (!books.containsKey(b.getName())) {
            books.put(b.getName(), b);
            b.setPublisher(this);
        }
    }

    public void removeBook(Book b) {
        if (books.containsKey(b.getName())) {
            books.remove(b.getName());
            b.removePublisher(this);
        }
    }

    public Map<String, Book> getBooks() {return books;}

    @Override
    public List<String> getMediaNames() {
        List<String> keys = new ArrayList<>();
        for (String key: books.keySet()) {
            keys.add(key);
        }
        return keys;
    }
}
