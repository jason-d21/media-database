package ui;

import Exceptions.InvalidEntryException;
import model.Book;
import model.Distributor;
import model.ListOfMedias;
import model.Movie;

import java.text.NumberFormat;
import java.util.*;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class HandleInfo {
    Scanner scanner = new Scanner(System.in);

    public void invalidEntryMessage() {
        System.out.println("Invalid entry. Please try again");
    }
    public void numberErrorMessage(String infoToAdjust) {
        System.out.println("Adjust Unsuccessful. " + infoToAdjust + " must be a number.");
    }
    public boolean stillStayOrDone(String answerHolder, String doMore) {
        boolean stillStay = true;
        String done = "done";
        while (!answerHolder.equals(doMore) && !answerHolder.equals(done)) {
            try {
                System.out.println("'" + doMore + "'" + " or '" + done + "'?");
                answerHolder = scanner.nextLine();
                if (answerHolder.equals(done)) {
                    stillStay = false;
                } else if (!answerHolder.equals(doMore)) {
                    throw new InvalidEntryException();
                }
            } catch (InvalidEntryException e) {
                invalidEntryMessage();
            }
        }
        return stillStay;
    }

    public String printShortForms(String type, List<String> shortFormList) {
        //System.out.println(type + " (omitted if lack name or year):");
        String output = type + " (omitted if lack name or year):" + "\n";
        for (String s: shortFormList) {
            //System.out.println(s + "; ");
            output = output + s + "; " + "\n";
        }
        return output + "\n";
    }

    public String changeStringIfKnown(String answerHolder, String name) {
        if (!answerHolder.equals("unknown")) {
            name = answerHolder;
        }
        return name;
    }

    public String getInfoToSearch(String fields, String type) {
        String infoToSearch;
        System.out.println("Enter wanted information: " + fields);
        infoToSearch = scanner.nextLine();
        while (notValidField(infoToSearch, type)) {
            invalidEntryMessage();
            System.out.println("Enter wanted information: " + fields);
            infoToSearch = scanner.nextLine();
        }
        return infoToSearch;
    }

    public boolean notValidField(String fieldString, String type) {
        if (type.equals("book")) {
            return !(fieldString.equals("author") ||
                    fieldString.equals("year") ||
                    fieldString.equals("pages") ||
                    fieldString.equals("genre") ||
                    fieldString.equals("is best seller") ||
                    fieldString.equals("publisher"));
        }
        else {
            return !(fieldString.equals("year") ||
                    fieldString.equals("box office") ||
                    fieldString.equals("genre") ||
                    fieldString.equals("rt rating") ||
                    fieldString.equals("company"));
        }
    }

    public double changeDoubleIfKnown(double boxOffice, String s) {
        String answerHolder;
        boolean tryNum = true;
        while (tryNum) {
            requestEnterOrUnknown(s);
            answerHolder = scanner.nextLine();
            if (!answerHolder.equals("unknown")) {
                try {
                    boxOffice = parseDouble(answerHolder);
                    tryNum = false;
                } catch (NumberFormatException e) {
                    numberFormatMessage();
                }
            } else {
                tryNum = false;
            }
        }
        return boxOffice;
    }

    public void numberFormatMessage() {
        System.out.println("Please enter a valid number");
    }

    public void requestEnterOrUnknown(String s) {
        System.out.println("Please enter " + s + " or 'unknown'");
    }

    public void changeDistributorIfKnown(String answerHolder, Distributor distributor) {
        if (!answerHolder.equals("unknown")) {
            distributor.setName(answerHolder);
        }
    }

    public boolean changeBoolIfKnown(String answerHolder, boolean isBestSeller) {
        if (!answerHolder.equals("unknown")) {
            isBestSeller = parseBoolean(answerHolder);
        }
        return isBestSeller;
    }

    public ArrayList<String> addGenreUntilUnknown(String type) {
        String answerHolder;
        ArrayList<String> genre = new ArrayList<>();
        requestEnterOrUnknown("one genre for the " + type);
        answerHolder = scanner.nextLine();
        while (!answerHolder.equals("unknown")) {
            genre.add(answerHolder);
            requestEnterOrUnknown("one more genre for the " + type);
            answerHolder = scanner.nextLine();
        }
        return genre;
    }

    public String getMovieInfo(Movie m, String infoToSearch) {
        if (m.isBlank()) {
            AlertBox.display("Error", "Movie not found in database");
            return "";
        }
        else {
            if (infoToSearch.equals("year")) {
                if (m.getYear() == 0) return "unknown";
                return String.valueOf(m.getYear());
            }
            else if (infoToSearch.equals("box office")){
                if (m.getBoxOffice() == 0) return "unknown";
                String currency = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(m.getBoxOffice());
                return currency;
            }
            else if (infoToSearch.equals("genre")) {
                if (m.getGenre().getGenre().isEmpty()) return "unknown";
                return String.valueOf(m.getGenre().getGenre());
            }
            else if (infoToSearch.equals("rt rating")) {
                if (m.getRTRating() == 0) return "unknown";
                return String.valueOf(m.getRTRating());
            }
            else {
                if (m.getCompany().getName().equals("")) return "unknown";
                return String.valueOf(m.getCompany().getName());
            }
        }
    }

    public int changeIntIfKnown(int year, String s) {
        String answerHolder;
        boolean tryNum = true;
        while (tryNum) {
            this.requestEnterOrUnknown(s);
            answerHolder = scanner.nextLine();
            if (!answerHolder.equals("unknown")) {
                try {
                    year = parseInt(answerHolder);
                    tryNum = false;
                } catch (NumberFormatException e) {
                    this.numberFormatMessage();
                }
            } else {
                tryNum = false;
            }
        }
        return year;
    }

    public boolean adjustGenre(ArrayList<String> newGenres, boolean isAdd, int index, String name, ListOfMedias mediaList) {
        mediaList.updateGenre(index, isAdd, newGenres);

        if (!newGenres.isEmpty()) {
            return true;
        }
        else {return false;}
    }

    public String getBookInfo(Book b, String infoToSearch) {
        if (b.isBlank()) {
            AlertBox.display("Error", "Movie not found in database");
            return "";}
        else {
            if (infoToSearch.equals("author")) {
                if (b.getAuthor().equals("")) return "unknown";
                return b.getAuthor();
            }
            else if (infoToSearch.equals("year")) {
                if (b.getYear() == 0) return "unknown";
                return String.valueOf(b.getYear());
            }
            else if (infoToSearch.equals("pages")) {
                if (b.getPages() == 0) return "unknown";
                return String.valueOf(b.getPages());
            }
            else if (infoToSearch.equals("genre")) {
                if (b.getGenre().getGenre().isEmpty()) return "unknown";
                return String.valueOf(b.getGenre().getGenre());
            }
            else if (infoToSearch.equals("is best seller"))
                return String.valueOf(b.getIsBestSeller());
            else {
                if (b.getPublisher().getName().equals("")) return "unknown";
                return String.valueOf(b.getPublisher().getName());
            }
        }
    }


}
