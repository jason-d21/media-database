package ui;

import java.util.Observable;
import java.util.Observer;

public class UpdateNotification implements Observer {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static UpdateNotification notification;
    private int movieAddedCount  = 0;
    private int movieChangedCount = 0;
    private int bookAddedCount  = 0;
    private int bookChangedCount = 0;

    private UpdateNotification() {

    }

    public static UpdateNotification getNotification() {
        if (notification == null) {
            notification = new UpdateNotification();
        }
        return notification;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals("movie added")) {
            movieAddedCount++;
        }
        else if (arg.equals("movie changed")) {
            movieChangedCount++;
        }
        else if (arg.equals("book added")) {
            bookAddedCount++;
        }
        else if (arg.equals("book changed")) {
            bookChangedCount++;
        }
        System.out.println();
        System.out.println(ANSI_RED + "Database has been changed!" + ANSI_RESET);
        System.out.println();
    }

    public void getStats() {
        System.out.println();
        System.out.println("Databases Changes:");
        System.out.println(movieAddedCount + " movies added to database");
        System.out.println(movieChangedCount + " changes made to the movie database");
        System.out.println(bookAddedCount + " books added to database");
        System.out.println(bookChangedCount + " changes made to the book database");
    }
}
