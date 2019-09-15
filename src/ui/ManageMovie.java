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
import model.Company;
import model.ListOfMovies;
import model.Movie;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static ui.UpdateNotification.getNotification;

public class ManageMovie extends Observable{
    private static final String MOVIE_FIELD_NAMES = "year | box office | genre | rt rating | company";
    private static final String MOVIE = "movie";

    private static ListOfMovies movieList;
    private static List<Company> companyList;
    private static HandleInfo handler;
    private Scanner scanner = new Scanner(System.in);


    public ManageMovie() {
        this.movieList = new ListOfMovies();
        this.companyList = new ArrayList<>();
        this.handler = new HandleInfo();
        addObserver(getNotification());
    }

    public ListOfMovies getMovieList() {
        return movieList;
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    protected void addMovieToDatabase(String name, int year, double boxOffice, ArrayList<String> genres, int rTRating, String companyString) {
        Company company = new Company(companyString);

        company = getRightCompany(company);
        Movie newMovie = new Movie(name, year, boxOffice, genres, rTRating, company);
        boolean changed = movieList.addToList(newMovie);
        if (changed) {
            setChanged();
            notifyObservers("movie added");
            //System.out.println(newMovie.getName() + " added to database");
            AlertBox.display("Success!", newMovie.getName() + " added to database");
        }
    }

    protected void adjustMovieList(String name, String infoToAdjust, String newInfo, ArrayList<String> genresAdd, ArrayList<String> genresRemove) {
        int index = movieList.movieIndexFromName(name);
        String output = "";
        try {
            if (index == -1) {
                throw new NotFoundException();
            }

            if (infoToAdjust.equals("genres")) {
                boolean changed = this.handler.adjustGenre(genresAdd,true, index, name, movieList);   // add new genres
                if (changed) {
                    setChanged();
                    notifyObservers("movie changed");
                    output = output + "specified genres added" + "\n";
                }
                else output = output + "no genres added" + "\n";
                changed = this.handler.adjustGenre(genresRemove, false, index, name, movieList);  // remove existing genres
                if (changed) {
                    setChanged();
                    notifyObservers("movie changed");
                    output = output + "specified genres removed" + "\n";
                }
                else output = output + "no genres removed" + "\n";
                AlertBox.display("Report", output);
            }

            else {
                if (infoToAdjust.equals("company")) {
                    movieList.updateCompany(index, getRightCompany(new Company(newInfo)));
                    output = output + infoToAdjust + " for '" + name + "' adjusted" + "\n";
                    setChanged();
                    notifyObservers("movie changed");
                    AlertBox.display("Success","Adjust Successful");
                } else {
                    try {
                        movieList.update(index, infoToAdjust, newInfo);
                        output = output + infoToAdjust + " for '" + name + "' adjusted" + "\n";
                        setChanged();
                        notifyObservers("movie changed");
                        AlertBox.display("Success","Adjust Successful");
                    } catch (NumberFormatException e) {
                        AlertBox.display("Error", "Adjust Unsuccessful. " + infoToAdjust + " must be a number");
                    }
                }
            }
        }
        catch (NotFoundException e) {
            AlertBox.display("Error", "Movie not found");
        }
    }

    protected static String lookUpMovie(String movieName, String infoToSearch) {

        return handler.getMovieInfo(movieList.searchByName(movieName), infoToSearch);

    }

    public void loadCompanyList() {
        for (Movie m: movieList.getMovieList()) {
            if (!companyList.contains(m.getCompany()) && !m.getCompany().getMediaNames().isEmpty()) {
                companyList.add(m.getCompany());
            }
        }
    }

    public String showCompanyData() {
        loadCompanyList();
        String companyData = "";
        //System.out.println("Movie Companies:");
        companyData = companyData + "Movie Companies:" + "\n";
        for (Company company: companyList) {
            if (!company.getName().equals("")) {
                //System.out.println(company.getName() + " - " + company.getMediaNames());
                companyData = companyData + company.getName() + " - " + company.getMediaNames() + "\n";
            }
        }

        return companyData;
    }

    public static Company getRightCompany(Company company) {
        Company rightCompany;
        if (!companyList.contains(company)) {
            rightCompany = company;
            if (!company.getName().equals("")) {
                companyList.add(rightCompany);
            }
        }
        else {
            rightCompany = companyList.get(companyList.indexOf(company));
        }

        return rightCompany;
    }

    public void loadMovie(ArrayList<String> partsOfLine) {
        String name;
        int year;
        ArrayList<String> genre = new ArrayList<>();
        double boxOffice;
        int rTRating;
        Company company = new Company("");

        if (partsOfLine.get(1).equals("unknown")) {name = "";}
        else {name = partsOfLine.get(1);}

        if (partsOfLine.get(2).equals("unknown")) {year = 0;}
        else {year = parseInt(partsOfLine.get(2));}

        if (partsOfLine.get(3).equals("unknown")) {boxOffice = 0;}
        else {boxOffice = parseDouble(partsOfLine.get(3));}
        for (int i = 4; i < (partsOfLine.size() - 2); i++) {
            if (!partsOfLine.get(i).equals("unknown")) {genre.add(partsOfLine.get(i));}
        }

        if (partsOfLine.get(partsOfLine.size() - 2).equals("unknown")) {rTRating = 0;}
        else {rTRating = parseInt(partsOfLine.get(partsOfLine.size() - 2));}

        if (partsOfLine.get(partsOfLine.size() - 1).equals("unknown")) {company.setName("");}
        else {company.setName(partsOfLine.get(partsOfLine.size() - 1));}


        company = getRightCompany(company);
        Movie movie = new Movie(name, year, boxOffice, genre, rTRating, company);
        movieList.addToList(movie);
    }

    public void saveMovies(String save) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(save,"UTF-8");

        for (Movie m: movieList.getMovieList()) {
            writer.print("Movie  ");
            if (m.getName().equals("")) {writer.print("unknown" + "  ");}
            else {writer.print(m.getName() + "  ");}

            if (m.getYear() == 0) {writer.print("unknown" + "  ");}
            else {writer.print(m.getYear() + "  ");}

            if (m.getBoxOffice() == 0) {writer.print("unknown" + "  ");}
            else {writer.print(m.getBoxOffice() + "  ");}

            if (m.getGenre().getGenre().isEmpty()) {writer.print("unknown" + "  ");}
            else {
                for (int i = 0; i < m.getGenre().getGenre().size(); i++) {
                    writer.print(m.getGenre().getGenre().get(i) + "  ");
                }
            }

            if (m.getRTRating() == 0) {writer.print("unknown" + "  ");}
            else {writer.print(m.getRTRating() + "  ");}

            if (m.getCompany().getName().equals("")) {writer.println("unknown");}
            else {writer.println(m.getCompany());}
        }

        writer.close();
    }

    public String showShortForms() {
        return this.handler.printShortForms("Movies", movieList.getShortForms());
    }

    public String showData() {
        //System.out.println("Movies:");
        String output = "Movies:" + "\n";
        for (Movie m : movieList.getMovieList()) {
            String name, year, boxOffice, genre, rTRating, company;
            if (m.getName().equals("")) {
                name = "Name: unknown; ";
            } else {
                name = "Name: " + m.getName() + "; ";
            }
            if (m.getYear() == 0) {
                year = "Year: unknown; ";
            } else {
                year = "Year: " + m.getYear() + "; ";
            }
            if (m.getBoxOffice() == 0) {
                boxOffice = "Box Office: unknown; ";
            } else {
                String currency = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(m.getBoxOffice());
                boxOffice = "Box Office: " + currency + "; ";
            }
            if (m.getGenre().getGenre().isEmpty()) {
                genre = "Genres: unknown; ";
            } else {
                genre = "Genres: " + m.getGenre().getGenre() + "; ";
            }
            if (m.getRTRating() == 0) {
                rTRating = "RT Rating: unknown; ";
            } else {
                rTRating = "RT Rating: " + m.getRTRating() + "%; ";
            }
            if (m.getCompany().getName().equals("")) {
                company = "Company: unknown";
            } else {
                company = "Company: " + m.getCompany().getName();
            }
            output = output + name + year + boxOffice + genre + rTRating + company + "\n";
//            System.out.print(name);
//            System.out.print(year);
//            System.out.print(boxOffice);
//            System.out.print(genre);
//            System.out.print(rTRating);
//            System.out.println(company);
        }
        return output;
    }

    public void addToList(Movie m) {
        movieList.addToList(m);
    }

    public static void displayLookUpWindow() {
        Stage window = new Stage();
        window.setResizable(false);

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Look Up Movie");
        window.setMinWidth(400);

        Label instruction1 = new Label();
        instruction1.setText("Name of Movie to Look Up:");
        TextField txtName = new TextField("<enter name of movie here>");
        Label instruction2 = new Label();
        instruction2.setText("Information to Look For:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("year", "box office", "genre", "rt rating", "company");
        //choiceBox.getItems().addAll("author", "year", "pages", "genre", "is best seller", "publisher");

        choiceBox.setValue("year");

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
            AlertBox.display("Error", "Please enter a movie name");
            return "";
        }

        else {
            String infoToSearch = choiceBox.getValue();
            return lookUpMovie(name, infoToSearch);
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

        Label lblName = new Label("Enter name of movie");
        TextField txtName = new TextField();
        Label lblYear = new Label("Enter movie relase year");
        TextField txtYear = new TextField();

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


        Label lblBoxOffice = new Label("Enter movie box office");
        TextField txtBoxOffice = new TextField();
        Label lblRTRating = new Label("Enter Rotten Tomatoes rating of movie");
        TextField txtRTRating = new TextField();
        Label lblCompany = new Label("Enter distribution company");
        TextField txtCompany = new TextField();

        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> {
            String name, year, boxOffice, rTRating, company;
            ArrayList<String> genres = new ArrayList<>();
            name = txtName.getText();
            year = txtYear.getText();
            boxOffice = txtBoxOffice.getText();
            rTRating = txtRTRating.getText();
            company = txtCompany.getText();

            if (cbNumOfGenres.getValue() >= 1) {
                genres.add(txtGenre1.getText());
            }
            if (cbNumOfGenres.getValue() >= 2) {
                genres.add(txtGenre2.getText());
            }
            if (cbNumOfGenres.getValue() == 3) {
                genres.add(txtGenre3.getText());
            }

            checkValidAdd(name, year, boxOffice, genres, rTRating, company);
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
        layout3.getChildren().addAll(lblName, txtName, lblYear, txtYear, cbNumOfGenres,
                lblGenre1, txtGenre1, lblGenre2, txtGenre2, lblGenre3, txtGenre3,
                lblBoxOffice, txtBoxOffice, lblRTRating, txtRTRating, lblCompany, txtCompany, btnAdd);
        layout3.setAlignment(Pos.CENTER);
        layout3.setPadding(new Insets(20, 20, 20, 20));

        scene = new Scene(layout3);

        //Display window and wait for it to be closed before returning
        window.setScene(scene);
        window.showAndWait();
    }

    private void checkValidAdd(String name, String year, String boxOffice, ArrayList<String> genres, String rTRating, String company) {
        try {
            addMovieToDatabase(name, parseInt(year), parseDouble(boxOffice), genres, parseInt(rTRating), company);
        }
        catch (NumberFormatException e) {
            AlertBox.display("Invalid Entry", "year and Rotten Tomatoes ratings should be integers" + "\n" + "box office should be a number");
        }
    }

    public void displayAdjustDatabaseWindow() {
        Stage window = new Stage();
        window.setResizable(false);

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Adjust Movie");
        window.setMinWidth(250);

        Label lblName = new Label("Enter name of movie to adjust");
        TextField txtName = new TextField();

        Label lblChoices = new Label("Select type of information to adjust");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("year", "genres", "box office", "Rotten Tomatoes rating", "company");

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
            adjustMovieList(name, infoToAdjust, newInfo, genresAdd, genresRemove);
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
