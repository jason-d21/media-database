package ui;

import Exceptions.InvalidEntryException;
import Exceptions.NotFoundException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.numberOfTrailingZeros;
import static java.lang.Integer.parseInt;
import static ui.UpdateNotification.getNotification;

public class ManageBook extends Observable {
    private static final String BOOK_FIELD_NAMES = "author | year | pages | genre | is best seller | publisher";
    private static final String BOOK = "book";
    private static ListOfBooks bookList;
    private static List<Publisher> publisherList;
    private static HandleInfo handler;
    private Scanner scanner = new Scanner(System.in);


    public ManageBook() {
        this.bookList = new ListOfBooks();
        this.publisherList = new ArrayList<>();
        this.handler = new HandleInfo();
        addObserver(getNotification());
    }

    public ListOfBooks getBookList() {
        return bookList;
    }

    public List<Publisher> getPublisherList() {
        return publisherList;
    }

    protected void addBookToDatabase(String name, String author, int year, int pages, ArrayList<String> genres, boolean isBestSeller, String publisherString) {
        Publisher publisher = new Publisher(publisherString);

        publisher = getRightPublisher(publisher);
        Book newBook = new Book(name, author, year, pages, genres, isBestSeller, publisher);
        boolean changed = bookList.addToList(newBook);
        if (changed) {
            setChanged();
            notifyObservers("book added");
            //System.out.println(newBook.getName() + " added to database");
            AlertBox.display("Success", newBook.getName() + " added to database");
        }
    }

    protected static String lookUpBook(String bookName, String infoToSearch) {
        return handler.getBookInfo(bookList.searchByName(bookName), infoToSearch);
    }

    protected void adjustBookList(String name, String infoToAdjust, String newInfo, ArrayList<String> genresAdd, ArrayList<String> genresRemove) {
        int index = bookList.bookIndexFromName(name);
        String output = "";
        try {
            if (index == -1) {
                throw new NotFoundException();
            }

            if (infoToAdjust.equals("genres")) {
                boolean changed = this.handler.adjustGenre(genresAdd,true, index, name, bookList);   // add new genres
                if (changed) {
                    setChanged();
                    notifyObservers("book changed");
                    output = output + "specified genres added" + "\n";
                }
                else output = output + "no genres added" + "\n";
                changed = this.handler.adjustGenre(genresRemove, false, index, name, bookList);  // remove existing genres
                if (changed) {
                    setChanged();
                    notifyObservers("book changed");
                    output = output + "specified genres removed" + "\n";
                }
                else output = output + "no genres removed" + "\n";
                AlertBox.display("Report", output);
            }

            else {
                if (infoToAdjust.equals("publisher")) {
                    bookList.updatePublisher(index, getRightPublisher(new Publisher(newInfo)));
                    output = output + infoToAdjust + " for '" + name + "' adjusted" + "\n";
                    setChanged();
                    notifyObservers("book changed");
                    AlertBox.display("Success","Adjust Successful");
                } else {
                    try {
                        bookList.update(index, infoToAdjust, newInfo);
                        output = output + infoToAdjust + " for '" + name + "' adjusted" + "\n";
                        setChanged();
                        notifyObservers("book changed");
                        AlertBox.display("Success","Adjust Successful");
                    } catch (NumberFormatException e) {
                        AlertBox.display("Error", "Adjust Unsuccessful. " + infoToAdjust + " must be a number");
                    }
                }
            }
        }
        catch (NotFoundException e) {
            AlertBox.display("Error", "Book not found");
        }

    }

    public void loadPublisherList() {
        for (Book b: bookList.getBookList()) {
            if (!publisherList.contains(b.getPublisher()) && !b.getPublisher().getMediaNames().isEmpty()) {
                publisherList.add(b.getPublisher());
            }
        }
    }

    public String showPublisherData() {
        loadPublisherList();
        String publisherData = "";
        //System.out.println("Book Publishers:");
        publisherData = publisherData + "Book Publishers:" + "\n";
        for (Publisher publisher: publisherList) {
            if (!publisher.getName().equals("")) {
                //System.out.println(publisher.getName() + " - " + publisher.getMediaNames());
                publisherData = publisherData + publisher.getName() + " - " + publisher.getMediaNames() + "\n";
            }
        }
        return publisherData;
    }

    public Publisher getRightPublisher(Publisher publisher) {
        Publisher rightPublisher;
        if (!publisherList.contains(publisher)) {
            rightPublisher = publisher;
            if (!publisher.getName().equals("")) {
                publisherList.add(rightPublisher);
            }
        }
        else {
            rightPublisher = publisherList.get(publisherList.indexOf(publisher));
        }

        return rightPublisher;
    }

    public void loadBook(ArrayList<String> partsOfLine) {
        String name;
        String author;
        int year;
        int pages;
        ArrayList<String> genre = new ArrayList<>();
        boolean isBestSeller;
        Publisher publisher = new Publisher("");

        if (partsOfLine.get(1).equals("unknown")) {name = "";}
        else {name = partsOfLine.get(1);}

        if (partsOfLine.get(2).equals("unknown")) {author = "";}
        else {author = partsOfLine.get(2);}

        if (partsOfLine.get(3).equals("unknown")) {year = 0;}
        else {year = parseInt(partsOfLine.get(3));}

        if (partsOfLine.get(4).equals("unknown")) {pages = 0;}
        else {pages = parseInt(partsOfLine.get(4));}
        for (int i = 5; i < (partsOfLine.size() - 2); i++) {
            if (!partsOfLine.get(i).equals("unknown")) {genre.add(partsOfLine.get(i));}
        }

        if (partsOfLine.get(partsOfLine.size() - 2).equals("Yes")) {isBestSeller = true;}
        else {isBestSeller = false;}

        if (partsOfLine.get(partsOfLine.size() - 1).equals("unknown")) {publisher.setName("");}
        else {publisher.setName(partsOfLine.get(partsOfLine.size() - 1));}


        publisher = getRightPublisher(publisher);
        Book book = new Book(name, author, year, pages, genre, isBestSeller, publisher);
        bookList.addToList(book);
    }

    public void saveBooks(String save) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(save,"UTF-8");

        for (Book b: bookList.getBookList()) {
            writer.print("Book  ");
            if (b.getName().equals("")) {writer.print("unknown" + "  ");}
            else {writer.print(b.getName() + "  ");}

            if (b.getAuthor().equals("")) {writer.print("unknown" + "  ");}
            else {writer.print(b.getAuthor() + "  ");}

            if (b.getYear() == 0) {writer.print("unknown" + "  ");}
            else {writer.print(b.getYear() + "  ");}

            if (b.getPages() == 0) {writer.print("unknown" + "  ");}
            else {writer.print(b.getPages() + "  ");}

            if (b.getGenre().getGenre().isEmpty()) {writer.print("unknown" + "  ");}
            else {
                for (int i = 0; i < b.getGenre().getGenre().size(); i++) {
                    writer.print(b.getGenre().getGenre().get(i) + "  ");
                }
            }

            if (!b.getIsBestSeller()) {writer.print("No" + "  ");}
            else {writer.print("Yes" + "  ");}

            if (b.getPublisher().getName().equals("")) {writer.println("unknown");}
            else {writer.println(b.getPublisher());}
        }

        writer.close();
    }

    public String showShortForms() {
        return this.handler.printShortForms("Books", bookList.getShortForms());
    }

    public String showData() {
        //System.out.println("Books:");
        String output = "Books:" + "\n";
        for (Book b : bookList.getBookList()) {
            String name, author, year, pages, genre, isBestSeller, publisher;
            if (b.getName().equals("")) {
                name = "Name: unknown; ";
            } else {
                name = "Name: " + b.getName() + "; ";
            }
            if (b.getAuthor().equals("")) {
                author = "Author: unknown; ";
            } else {
                author = "Author: " + b.getAuthor() + "; ";
            }
            if (b.getYear() == 0) {
                year = "Year: unknown; ";
            } else {
                year = "Year: " + b.getYear() + "; ";
            }
            if (b.getPages() == 0) {
                pages = "# of pages: unknown; ";
            } else {
                pages = "# of pages: " + b.getPages() + "; ";
            }
            if (b.getGenre().getGenre().isEmpty()) {
                genre = "Genres: unknown; ";
            } else {
                genre = "Genres: " + b.getGenre().getGenre() + "; ";
            }
            if (!b.getIsBestSeller()) {
                isBestSeller = "Best Seller?: No; ";
            } else {
                isBestSeller = "Best Seller?: Yes; ";
            }
            if (b.getPublisher().getName().equals("")) {
                publisher = "Publisher: unknown";
            } else {
                publisher = "Publisher: " + b.getPublisher().getName();
            }
//            System.out.print(name);
//            System.out.print(author);
//            System.out.print(year);
//            System.out.print(pages);
//            System.out.print(genre);
//            System.out.print(isBestSeller);
//            System.out.println(publisher);
            output = output + name + author + year + pages + genre + isBestSeller + publisher + "\n";
        }
        return output;
    }

    public static void displayLookUpWindow() {
        Stage window = new Stage();
        window.setResizable(false);

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Look Up Book");
        window.setMinWidth(400);

        Label instruction1 = new Label();
        instruction1.setText("Name of Book to Look Up:");
        TextField txtName = new TextField("<enter name of book here>");
        Label instruction2 = new Label();
        instruction2.setText("Information to Look For:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        //choiceBox.getItems().addAll("year", "box office", "genre", "rt rating", "company");
        choiceBox.getItems().addAll("author", "year", "pages", "genre", "is best seller", "publisher");

        choiceBox.setValue("author");

        Label output = new Label();

        Button btnLookup = new Button("Look Up");
        btnLookup.setOnAction(e -> output.setText(checkAndGetLookUp(txtName.getText(), choiceBox)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(instruction1, txtName,instruction2, choiceBox, btnLookup, output);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20, 20, 20, 20));

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    private static String checkAndGetLookUp(String name, ChoiceBox<String> choiceBox) {
        if (name == null | name.equals("")) {
            AlertBox.display("Error", "Please enter a book name");
            return "";
        }

        else {
            String infoToSearch = choiceBox.getValue();
            return lookUpBook(name, infoToSearch);
        }
    }

    public void displayAddToDatabaseWindow() {
        Stage window = new Stage();
        window.setResizable(false);
        Scene scene;

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add to Database");
        window.setMinWidth(250);

        Label lblName = new Label("Enter name of book");
        TextField txtName = new TextField();
        Label lblAuthor = new Label("Enter name of author");
        TextField txtAuthor = new TextField();
        Label lblYear = new Label("Enter book relase year");
        TextField txtYear = new TextField();
        Label lblPages = new Label("Enter number of pages");
        TextField txtPages = new TextField();

        ComboBox<Integer> cbNumOfGenres = new ComboBox<>();
        cbNumOfGenres.getItems().addAll(0, 1, 2, 3);
        cbNumOfGenres.setPromptText("<Select number of genres to enter>");

        Label lblGenre1 = new Label("Enter the first genre");
        TextField txtGenre1 = new TextField();
        Label lblGenre2 = new Label("Enter the second genre");
        TextField txtGenre2 = new TextField();
        Label lblGenre3 = new Label("Enter the third genre");
        TextField txtGenre3 = new TextField();
        lblGenre1.setVisible(false);
        lblGenre2.setVisible(false);
        lblGenre3.setVisible(false);
        txtGenre1.setVisible(false);
        txtGenre2.setVisible(false);
        txtGenre3.setVisible(false);

        Label lblIsBestSeller = new Label("Is the book a best seller?");
        ChoiceBox<Boolean> cbIsBestSeller = new ChoiceBox<>();
        cbIsBestSeller.getItems().addAll(true, false);
        cbIsBestSeller.setValue(false);

        Label lblPublisher = new Label("Enter book publisher");
        TextField txtPublisher = new TextField();

        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> {
            String name, author, year, pages, publisher;
            Boolean isBestSeller;
            ArrayList<String> genres = new ArrayList<>();
            name = txtName.getText();
            author = txtAuthor.getText();
            year = txtYear.getText();
            pages = txtPages.getText();
            isBestSeller = cbIsBestSeller.getValue();
            publisher = txtPublisher.getText();

            if (cbNumOfGenres.getValue() >= 1) {
                genres.add(txtGenre1.getText());
            }
            if (cbNumOfGenres.getValue() >= 2) {
                genres.add(txtGenre2.getText());
            }
            if (cbNumOfGenres.getValue() == 3) {
                genres.add(txtGenre3.getText());
            }

            checkValidAdd(name, author, year, pages, genres, isBestSeller, publisher);
        });

        cbNumOfGenres.setOnAction(e -> {
            if (cbNumOfGenres.getValue() == 1) {
                lblGenre1.setVisible(true);
                lblGenre2.setVisible(false);
                lblGenre3.setVisible(false);
                txtGenre1.setVisible(true);
                txtGenre2.setVisible(false);
                txtGenre3.setVisible(false);
            }
            else if (cbNumOfGenres.getValue() == 2) {
                lblGenre1.setVisible(true);
                lblGenre2.setVisible(true);
                lblGenre3.setVisible(false);
                txtGenre1.setVisible(true);
                txtGenre2.setVisible(true);
                txtGenre3.setVisible(false);
            }
            else if (cbNumOfGenres.getValue() == 3) {
                lblGenre1.setVisible(true);
                lblGenre2.setVisible(true);
                lblGenre3.setVisible(true);
                txtGenre1.setVisible(true);
                txtGenre2.setVisible(true);
                txtGenre3.setVisible(true);
            }
            else {
                lblGenre1.setVisible(false);
                lblGenre2.setVisible(false);
                lblGenre3.setVisible(false);
                txtGenre1.setVisible(false);
                txtGenre2.setVisible(false);
                txtGenre3.setVisible(false);
            }
        });


        VBox layout3 = new VBox(10);
        layout3.getChildren().addAll(lblName, txtName, lblAuthor, txtAuthor, lblYear, txtYear, lblPages, txtPages,
                cbNumOfGenres, lblGenre1, txtGenre1, lblGenre2, txtGenre2, lblGenre3, txtGenre3,
                lblIsBestSeller, cbIsBestSeller, lblPublisher, txtPublisher, btnAdd);
        layout3.setAlignment(Pos.CENTER);
        layout3.setPadding(new Insets(20, 20, 20, 20));

        scene = new Scene(layout3);

        //Display window and wait for it to be closed before returning
        window.setScene(scene);
        window.showAndWait();
    }

    private void checkValidAdd(String name, String author, String year, String pages, ArrayList<String> genres, Boolean isBestSeller, String publisher) {
        try {
            addBookToDatabase(name, author, parseInt(year), parseInt(pages), genres, isBestSeller, publisher);
        }
        catch (NumberFormatException e) {
            AlertBox.display("Invalid Entry", "year and pages should be integers");
        }
    }

    public void displayAdjustDatabaseWindow() {
        Stage window = new Stage();
        window.setResizable(false);

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Adjust Book");
        window.setMinWidth(250);

        Label lblName = new Label("Enter name of book to adjust");
        TextField txtName = new TextField();

        Label lblChoices = new Label("Select type of information to adjust");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("author", "year", "pages", "genres", "is best seller?", "publisher");

        Label lbl1 = new Label();
        TextField txt1 = new TextField();
        Label lbl2 = new Label();
        TextField txt2 = new TextField();
        Label lbl3 = new Label();
        TextField txt3 = new TextField();
        Label lbl4 = new Label();
        TextField txt4 = new TextField();

        lbl1.setVisible(false);
        lbl2.setVisible(false);
        lbl3.setVisible(false);
        lbl4.setVisible(false);
        txt1.setVisible(false);
        txt2.setVisible(false);
        txt3.setVisible(false);
        txt4.setVisible(false);

        comboBox.setOnAction(e -> {
            if (comboBox.getValue().equals("genres")) {
                lbl1.setText("Enter first genre to add");
                lbl2.setText("Enter second genre to add");
                lbl3.setText("Enter first genre to remove");
                lbl4.setText("Enter second genre to remove");
                lbl1.setVisible(true);
                lbl2.setVisible(true);
                lbl3.setVisible(true);
                lbl4.setVisible(true);
                lbl1.setVisible(true);
                txt2.setVisible(true);
                txt3.setVisible(true);
                txt4.setVisible(true);
            }
            else {
                lbl1.setText("Enter new " + comboBox.getValue());
                lbl1.setVisible(true);
                lbl2.setVisible(false);
                lbl3.setVisible(false);
                lbl4.setVisible(false);
                txt1.setVisible(true);
                txt2.setVisible(false);
                txt3.setVisible(false);
                txt4.setVisible(false);
            }
        });

        Button btnAdjust = new Button("Adjust");
        btnAdjust.setOnAction(e -> {
            ArrayList<String> genresAdd = new ArrayList<>();
            ArrayList<String> genresRemove = new ArrayList<>();
            String infoToAdjust = comboBox.getValue();
            String newInfo = "";
            String name = txtName.getText();
            if (infoToAdjust.equals("genres")) {
                if (!txt1.getText().equals("")) {
                    genresAdd.add(txt1.getText());
                }
                if (!txt2.getText().equals("")) {
                    genresAdd.add(txt2.getText());
                }
                if (!txt3.getText().equals("")) {
                    genresRemove.add(txt3.getText());
                }
                if (!txt4.getText().equals("")) {
                    genresRemove.add(txt4.getText());
                }
            }
            else {
                newInfo = txt1.getText();
            }
            adjustBookList(name, infoToAdjust, newInfo, genresAdd, genresRemove);
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(lblName, txtName, lblChoices, comboBox, lbl1, txt1, lbl2, txt2, lbl3, txt3, lbl4, txt4, btnAdjust);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20, 20, 20, 20));

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();


    }
}
