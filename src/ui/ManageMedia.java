package ui;

import Exceptions.InvalidEntryException;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;


public class ManageMedia extends Application {
    private static ManageMovie manageMovie = new ManageMovie();
    private static ManageBook manageBook = new ManageBook();
    private Scanner scanner = new Scanner(System.in);
    private HandleInfo handler = new HandleInfo();

    private Stage window;
    private Scene sceneMenu, sceneDatabase, sceneUpdate, sceneLookup;
    private Scene sceneAddToDatabase, sceneAdjustDatabase;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setResizable(false);

        // main menu labels and buttons
        Label lblWelcomeMessage = new Label("Welcome to Media World!");
        lblWelcomeMessage.setFont(Font.font("Cambria" , FontWeight.BOLD, 30));
        lblWelcomeMessage.setTextFill(Color.DARKGOLDENROD);

        Button btnGoToDatabase = new Button("Show Database");
        btnGoToDatabase.setOnAction(e -> window.setScene(sceneDatabase));

        Button btnGoToUpdate = new Button("Update Database");
        btnGoToUpdate.setOnAction(e -> window.setScene(sceneUpdate));

        Button btnGoToLookUp = new Button ("Look Up Database");
        btnGoToLookUp.setOnAction(e -> window.setScene(sceneLookup));


        // main menu layout
        VBox layoutMenu = new VBox(20);
        layoutMenu.setPrefWidth(200);
        btnGoToDatabase.setMinWidth(layoutMenu.getPrefWidth());
        btnGoToUpdate.setMinWidth(layoutMenu.getPrefWidth());
        btnGoToLookUp.setMinWidth(layoutMenu.getPrefWidth());
        layoutMenu.getChildren().addAll(lblWelcomeMessage, btnGoToDatabase, btnGoToUpdate, btnGoToLookUp);
        layoutMenu.setPadding(new Insets(20, 20, 20, 20));
        layoutMenu.setAlignment(Pos.CENTER);
        layoutMenu.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        sceneMenu = new Scene(layoutMenu, 400, 250);

        // database buttons
        Button btnByDist = new Button("By Distributor");
        btnByDist.setOnAction(e -> AlertBox.display("Database by Distributor", showDistDatabase()));

        Button btnShortForm = new Button("Short Form");
        btnShortForm.setOnAction(e -> AlertBox.display("Database in Short Form", showShortFormDatabase()));

        Button btnFullDatabase = new Button("Full Database");
        btnFullDatabase.setOnAction(e -> AlertBox.display("Full Database", showFullDatabase()));

        Button btnBackToMenuFromDatabase = new Button("Back To Main Menu");
        btnBackToMenuFromDatabase.setOnAction(e -> window.setScene(sceneMenu));


        // database layout
        VBox layoutDatabase = new VBox(20);
        layoutDatabase.setPrefWidth(200);
        btnByDist.setMinWidth(layoutDatabase.getPrefWidth());
        btnShortForm.setMinWidth(layoutDatabase.getPrefWidth());
        btnFullDatabase.setMinWidth(layoutDatabase.getPrefWidth());
        btnBackToMenuFromDatabase.setMinWidth(layoutDatabase.getPrefWidth());
        layoutDatabase.getChildren().addAll(btnByDist, btnShortForm, btnFullDatabase, btnBackToMenuFromDatabase);
        layoutDatabase.setPadding(new Insets(20, 20, 20, 20));
        layoutDatabase.setAlignment(Pos.CENTER);
        layoutDatabase.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        sceneDatabase = new Scene(layoutDatabase, 300, 300);

        // update buttons
        Button btnAddToDatabase = new Button("Add to Database");
        btnAddToDatabase.setOnAction(e -> window.setScene(sceneAddToDatabase));

        Button btnAdjustDatabase = new Button("Adjust Database");
        btnAdjustDatabase.setOnAction(e -> window.setScene(sceneAdjustDatabase));

        Button btnBackToMenuFromUpdate = new Button("Back To Main Menu");
        btnBackToMenuFromUpdate.setOnAction(e -> window.setScene(sceneMenu));


        // update layout
        VBox layoutUpdate = new VBox(20);
        layoutUpdate.setPrefWidth(200);
        btnAddToDatabase.setMinWidth(layoutUpdate.getPrefWidth());
        btnAdjustDatabase.setMinWidth(layoutUpdate.getPrefWidth());
        btnBackToMenuFromUpdate.setMinWidth(layoutUpdate.getPrefWidth());
        layoutUpdate.getChildren().addAll(btnAddToDatabase, btnAdjustDatabase, btnBackToMenuFromUpdate);
        layoutUpdate.setPadding(new Insets(20, 20, 20, 20));
        layoutUpdate.setAlignment(Pos.CENTER);
        layoutUpdate.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        sceneUpdate = new Scene(layoutUpdate, 300, 200);


        // lookup buttons
        Button btnLookUpBook = new Button("Look Up Book");
        btnLookUpBook.setOnAction(e -> manageBook.displayLookUpWindow());

        Button btnLookUpMovie = new Button("Look Up Movie");
        btnLookUpMovie.setOnAction(e -> manageMovie.displayLookUpWindow());

        Button btnBackToMenuFromLookup = new Button("Back To Main Menu");
        btnBackToMenuFromLookup.setOnAction(e -> window.setScene(sceneMenu));


        // lookup layout
        VBox layoutLookup = new VBox(20);
        layoutLookup.setPrefWidth(200);
        btnLookUpBook.setMinWidth(layoutLookup.getPrefWidth());
        btnLookUpMovie.setMinWidth(layoutLookup.getPrefWidth());
        btnBackToMenuFromLookup.setMinWidth(layoutLookup.getPrefWidth());
        layoutLookup.getChildren().addAll(btnLookUpBook, btnLookUpMovie, btnBackToMenuFromLookup);
        layoutLookup.setPadding(new Insets(20, 20, 20, 20));
        layoutLookup.setAlignment(Pos.CENTER);
        layoutLookup.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        sceneLookup = new Scene(layoutLookup, 300, 200);


        // addToDatabase buttons
        Button btnAddBook = new Button("Add Book");
        btnAddBook.setOnAction(e -> manageBook.displayAddToDatabaseWindow());

        Button btnAddMovie = new Button("Add Movie");
        btnAddMovie.setOnAction(e -> manageMovie.displayAddToDatabaseWindow());

        Button btnBackToUpdateFromAdd = new Button("Back");
        btnBackToUpdateFromAdd.setOnAction(e -> window.setScene(sceneUpdate));


        // addToDatabase layout
        VBox layoutAddToDatabase = new VBox(20);
        layoutAddToDatabase.setPrefWidth(200);
        btnAddBook.setMinWidth(layoutAddToDatabase.getPrefWidth());
        btnAddMovie.setMinWidth(layoutAddToDatabase.getPrefWidth());
        btnBackToUpdateFromAdd.setMinWidth(layoutAddToDatabase.getPrefWidth());
        layoutAddToDatabase.getChildren().addAll(btnAddBook, btnAddMovie, btnBackToUpdateFromAdd);
        layoutAddToDatabase.setPadding(new Insets(20, 20, 20, 20));
        layoutAddToDatabase.setAlignment(Pos.CENTER);
        layoutAddToDatabase.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        sceneAddToDatabase = new Scene(layoutAddToDatabase, 300, 200);


        // adjustDatabase buttons
        Button btnAdjustBook = new Button("Adjust Book");
        btnAdjustBook.setOnAction(e -> manageBook.displayAdjustDatabaseWindow());

        Button btnAdjustMovie = new Button("Adjust Movie");
        btnAdjustMovie.setOnAction(e -> manageMovie.displayAdjustDatabaseWindow());

        Button btnBackToUpdateFromAdjust = new Button("Back");
        btnBackToUpdateFromAdjust.setOnAction(e -> window.setScene(sceneUpdate));


        // adjustDatabase layout
        VBox layoutAdjustDatabase = new VBox(20);
        layoutAdjustDatabase.setPrefWidth(200);
        btnAdjustBook.setMinWidth(layoutAdjustDatabase.getPrefWidth());
        btnAdjustMovie.setMinWidth(layoutAdjustDatabase.getPrefWidth());
        btnBackToUpdateFromAdjust.setMinWidth(layoutAdjustDatabase.getPrefWidth());
        layoutAdjustDatabase.getChildren().addAll(btnAdjustBook, btnAdjustMovie, btnBackToUpdateFromAdjust);
        layoutAdjustDatabase.setPadding(new Insets(20, 20, 20, 20));
        layoutAdjustDatabase.setAlignment(Pos.CENTER);
        layoutAdjustDatabase.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        sceneAdjustDatabase = new Scene(layoutAdjustDatabase, 300, 200);


        // display menu first
        window.setScene(sceneMenu);
        primaryStage.setTitle("Media World");
        window.show();
    }


    public ManageMedia() {
//        String loadFileName = "load";
//        String saveFileName = "save";
//        try {
//            load(loadFileName);
//            System.out.println("Data from '" + loadFileName + "' loaded into database");
//        }
//        catch (IOException e) {
//            System.out.println("Load Unsuccessful");
//        }
//        finally {
//            String choice = "";
//            System.out.println();
//            System.out.println("Welcome to Movie World!");
//            while (true) {
//                try {
//                    System.out.println();
//                    System.out.println("Main Menu: Would you like to 'see database', 'update database', or 'look up' media information?");
//                    choice = scanner.nextLine();
//                    if (choice.equals("update database")) {
//                        updateDatabase();
//                    } else if (choice.equals("look up")) {
//                        lookUpInfo();
//                    } else if (choice.equals(("see database"))) {
//                        showDatabase();
//                    } else if (choice.equals("exit")) {
//                        try {
//                            save(saveFileName);
//                            System.out.println("Data saved to '" + saveFileName + "'");
//                        }
//                        catch (IOException e) {
//                            System.out.println("Save Unsuccessful. '" + saveFileName + "' is not a valid file");
//                        }
//                        finally {
//                            getNotification().getStats();
//                            break;
//                        }
//                    } else {
//                        throw new InvalidEntryException();
//                    }
//                } catch (InvalidEntryException e) {
//                    this.handler.invalidEntryMessage();
//                }
//            }
//        }
    }


    private void lookUpInfo() {
//        boolean stillLookUp = true;
//        String answerHolder = "";
//        while (stillLookUp) {
//            try {
//                System.out.println("Look up 'Book' or 'Movie'?");
//                answerHolder = scanner.nextLine();
//                if (answerHolder.equals("Movie")) {
//                    manageMovie.lookUpMovie();
//                } else if (answerHolder.equals("Book")) {
//                    manageBook.lookUpBook();
//                } else {
//                    throw new InvalidEntryException();
//                }
//                answerHolder = "";
//                stillLookUp = this.handler.stillStayOrDone(answerHolder, "look up more");
//            }
//            catch (InvalidEntryException e) {
//                this.handler.invalidEntryMessage();
//            }
//        }
    }



    private void updateDatabase() {
        boolean stillUpdate = true;
        String choice;
        while (stillUpdate) {
            try {
                System.out.println("Would you like to 'add to database', 'adjust database', or 'back' to main menu?");
                choice = scanner.nextLine();
                if (choice.equals("add to database")) {
                    addToDatabase();
                } else if (choice.equals("adjust database")) {
                    adjustDatabase();
                } else if (choice.equals("back")) {
                    stillUpdate = false;
                } else {
                    throw new InvalidEntryException();
                }
            }
            catch (InvalidEntryException e) {
                this.handler.invalidEntryMessage();
            }

        }

    }

    private void adjustDatabase() {
//        boolean stillAdjust = true;
//        while (stillAdjust) {
//            try {
//                String answerHolder = "";
//                System.out.println("adjust 'Book' or 'Movie'?");
//                answerHolder = scanner.nextLine();
//                if (answerHolder.equals("Movie")) {
//                    manageMovie.adjustMovieList();
//
//                } else if (answerHolder.equals("Book")) {
//                    manageBook.adjustBookList();
//                }
//                else {
//                    throw new InvalidEntryException();
//                }
//                answerHolder = "";
//                stillAdjust = this.handler.stillStayOrDone(answerHolder, "adjust more");
//            }
//            catch (InvalidEntryException e) {
//                this.handler.invalidEntryMessage();
//
//            }
//        }
    }


    private void addToDatabase() {
//        boolean stillAdd = true;
//        while (stillAdd) {
//            try {
//                String answerHolder = "";
//                System.out.println("Add 'Book' or 'Movie'?");
//                answerHolder = scanner.nextLine();
//                if (answerHolder.equals("Movie")) {
//                    manageMovie.addMovieToDatabase();
//                    stillAdd = false;
//                } else if (answerHolder.equals("Book")) {
//                    manageBook.addBookToDatabase();
//                    stillAdd = false;
//                } else {
//                    throw new InvalidEntryException();
//                }
//            }
//            catch (InvalidEntryException e) {
//                this.handler.invalidEntryMessage();
//            }
//        }
    }



    private String showDistDatabase() {
        return manageMovie.showCompanyData() + "\n" + manageBook.showPublisherData();
    }

    private String showShortFormDatabase() {
        return manageMovie.showShortForms() + "\n" + manageBook.showShortForms();
    }

    private String showFullDatabase() {
        return manageMovie.showData() + "\n" + manageBook.showData();
    }

    private void showDatabase() {
        String shortForm = "";
        String distOnly = "";

        System.out.println("Distributor based?");
        distOnly = scanner.nextLine();

        if (distOnly.equals("yes") || distOnly.equals("Yes")) {
            manageMovie.showCompanyData();
            System.out.println();
            manageBook.showPublisherData();
        }

        else {
            System.out.println("Short form?");
            shortForm = scanner.nextLine();

            if (shortForm.equals("yes") || shortForm.equals("Yes")) {
                manageMovie.showShortForms();
                System.out.println();
                manageBook.showShortForms();
            }
            else {
                manageMovie.showData();
                System.out.println();
                manageBook.showData();
            }
        }

    }


    public static void main(String[] args)  {
        String loadFileName = "load";
        String saveFileName = "save";
        try {
            load(loadFileName);
            System.out.println("Data from '" + loadFileName + "' loaded into database");
        }
        catch (IOException e) {
            System.out.println("Load Unsuccessful");
        }
        //new ReadWebPage("https://en.wikipedia.org/wiki/List_of_years_in_film");
        launch(args);

        //new ManageMedia();
    }

    private static ArrayList<String> splitOnSpace(String line){
        String[] splits = line.split("  ");
        return new ArrayList<>(Arrays.asList(splits));
    }

    public static void load(String load) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(load));
        for (String line : lines){
            ArrayList<String> partsOfLine = splitOnSpace(line);
            if (partsOfLine.get(0).equals("Movie")) {
                manageMovie.loadMovie(partsOfLine);
            }

            else if (partsOfLine.get(0).equals("Book")) {
                manageBook.loadBook(partsOfLine);
            }
        }
        manageMovie.loadCompanyList();
        manageBook.loadPublisherList();
    }

    public void save(String save) throws IOException {
        manageMovie.saveMovies(save);
        manageBook.saveBooks(save);
    }

    public ListOfMovies getMovieList() {
        return manageMovie.getMovieList();
    }


    public void addToList(Movie m) {
        manageMovie.addToList(m);
    }

    public ManageMedia (String test) {

    }
}
