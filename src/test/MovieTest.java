package test;

import model.Company;
import model.Entertainment;
import model.Media;
import model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;


public class MovieTest {
    String name = "Inception";
    int year = 2010;
    double boxOffice = 825532764;
    ArrayList<String> genres = new ArrayList<>();
    int rTRating = 86;
    Company company = new Company("WB");
    Movie testMovie;
    Movie blankMovie;
    Entertainment testEnt;
    Media testMedia;

    @BeforeEach
    public void runBefore() {
        testMovie = new Movie(name, year, boxOffice, genres, rTRating, company);
        blankMovie = new Movie();
        testEnt = new Movie(name, year, boxOffice, genres, rTRating, company);
        testMedia = new Movie(name, year, boxOffice, genres, rTRating, company);
    }

    @Test
    public void testConstructorMovieWithParameters() {
        assertEquals(testMovie.getName(), name);
        assertEquals(testMovie.getYear(), year);
        assertEquals(testMovie.getBoxOffice(), boxOffice);
        assertEquals(testMovie.getGenre().getGenre(), genres);
        assertEquals(testMovie.getRTRating(), rTRating);
        assertEquals(testMovie.getCompany(), company);
    }

    public void testConstructorMovieNoParameters() {

        assertEquals(blankMovie.getName(), "");
        assertEquals(blankMovie.getYear(), 0);
        assertEquals(blankMovie.getBoxOffice(), 0);
        assertTrue(blankMovie.getGenre().getGenre().isEmpty());
        assertEquals(blankMovie.getRTRating(), 0);
        assertEquals(blankMovie.getCompany(), "");
    }

    @Test
    public void testAddGenreAddOne() {
        assertEquals(testMovie.getGenre().getGenre().size(), 0);
        testMovie.addGenre("Action");
        assertEquals(testMovie.getGenre().getGenre().size(), 1);
    }

    @Test
    public void testAddGenreAddTwo() {
        assertEquals(testMovie.getGenre().getGenre().size(), 0);
        testMovie.addGenre("Action");
        assertEquals(testMovie.getGenre().getGenre().size(), 1);
        testMovie.addGenre("Adventure");
        assertEquals(testMovie.getGenre().getGenre().size(), 2);
    }

    @Test
    public void testAddGenreAlreadyThere() {
        assertEquals(testMovie.getGenre().getGenre().size(), 0);
        testMovie.addGenre("Action");
        assertEquals(testMovie.getGenre().getGenre().size(), 1);
        testMovie.addGenre("Adventure");
        assertEquals(testMovie.getGenre().getGenre().size(), 2);
        testMovie.addGenre("Action");
        assertEquals(testMovie.getGenre().getGenre().size(), 2);
    }

    @Test
    public void testRemoveGenreNoGenre() {
        assertEquals(testMovie.getGenre().getGenre().size(), 0);
        testMovie.removeGenre("Thriller");
        assertEquals(testMovie.getGenre().getGenre().size(), 0);
    }

    @Test
    public void testRemoveGenreRemoveOne() {
        testMovie.addGenre("Action");
        testMovie.addGenre("Adventure");
        testMovie.addGenre("Sci-fi");
        assertEquals(testMovie.getGenre().getGenre().size(), 3);
        testMovie.removeGenre("Adventure");
        assertEquals(testMovie.getGenre().getGenre().size(), 2);
    }

    @Test
    public void testRemoveGenreRemoveTwo() {
        testMovie.addGenre("Action");
        testMovie.addGenre("Adventure");
        testMovie.addGenre("Sci-fi");
        assertEquals(testMovie.getGenre().getGenre().size(), 3);
        testMovie.removeGenre("Adventure");
        assertEquals(testMovie.getGenre().getGenre().size(), 2);
        testMovie.removeGenre("Sci-fi");
        assertEquals(testMovie.getGenre().getGenre().size(), 1);
    }

    @Test
    public void testRemoveGenreNotInGenre() {
        testMovie.addGenre("Action");
        testMovie.addGenre("Adventure");
        testMovie.addGenre("Sci-fi");
        assertEquals(testMovie.getGenre().getGenre().size(), 3);
        testMovie.removeGenre("Thriller");
        assertEquals(testMovie.getGenre().getGenre().size(), 3);
    }

    @Test
    public void testIsGenreNoGenre() {
        assertEquals(testMovie.getGenre().getGenre().size(), 0);
        boolean isGenre = testMovie.isOfGenre("Action");
        assertFalse(isGenre);
    }

    @Test
    public void testIsGenreOneGenreTrue() {
        assertEquals(testMovie.getGenre().getGenre().size(), 0);
        testMovie.addGenre("Action");
        assertEquals(testMovie.getGenre().getGenre().size(), 1);
        boolean isGenre = testMovie.isOfGenre("Action");
        assertTrue(isGenre);
    }

    @Test
    public void testIsGenreOneGenreFalse() {
        assertEquals(testMovie.getGenre().getGenre().size(), 0);
        testMovie.addGenre("Action");
        assertEquals(testMovie.getGenre().getGenre().size(), 1);
        boolean isGenre = testMovie.isOfGenre("Adventure");
        assertFalse(isGenre);
    }

    @Test
    public void testIsGenreTwoGenresTrue() {
        assertEquals(testMovie.getGenre().getGenre().size(), 0);
        testMovie.addGenre("Action");
        assertEquals(testMovie.getGenre().getGenre().size(), 1);
        testMovie.addGenre("Adventure");
        assertEquals(testMovie.getGenre().getGenre().size(), 2);
        boolean isGenre = testMovie.isOfGenre("Action");
        assertTrue(isGenre);
    }

    @Test
    public void testIsGenreTwoGenresFalse() {
        assertEquals(testMovie.getGenre().getGenre().size(), 0);
        testMovie.addGenre("Action");
        assertEquals(testMovie.getGenre().getGenre().size(), 1);
        testMovie.addGenre("Adventure");
        assertEquals(testMovie.getGenre().getGenre().size(), 2);
        boolean isGenre = testMovie.isOfGenre("Sci-fi");
        assertFalse(isGenre);
    }

    @Test
    public void testIsAcclaimedAsEntertainmentTrue() {
        int score = 85;
        testEnt.setRTRating(score);
        assertEquals(testEnt.getRTRating(), score);
        assertTrue(testEnt.isAcclaimed());
    }

    @Test
    public void testIsAcclaimedAsEntertainmentBorderCase() {
        int score = 80;
        testEnt.setRTRating(score);
        assertEquals(testEnt.getRTRating(), score);
        assertTrue(testEnt.isAcclaimed());
    }

    @Test
    public void testIsAcclaimedAsEntertainmentFalse() {
        int score = 70;
        testEnt.setRTRating(score);
        assertEquals(testEnt.getRTRating(), score);
        assertFalse(testEnt.isAcclaimed());
    }

    @Test
    public void testShortFormInfoAsMediaEnoughInfo() {
        String name = "The Matrix";
        int year = 1999;
        testMedia.setName(name);
        assertEquals(testMedia.getName(), name);
        testMedia.setYear(year);
        assertEquals(testMedia.getYear(), year);
        String shortForm = testMedia.shortFormInfo();
        assertEquals(shortForm, name + " (" + year + ")");
    }

    @Test
    public void testShortFormInfoAsMediaNoName() {
        String name = "";
        int year = 1999;
        testMedia.setName(name);
        assertEquals(testMedia.getName(), name);
        testMedia.setYear(year);
        assertEquals(testMedia.getYear(), year);
        String shortForm = testMedia.shortFormInfo();
        assertEquals(shortForm, "");
    }

    @Test
    public void testShortFormInfoAsMediaNoYear() {
        String name = "The Matrix";
        int year = 0;
        testMedia.setName(name);
        assertEquals(testMedia.getName(), name);
        testMedia.setYear(year);
        assertEquals(testMedia.getYear(), year);
        String shortForm = testMedia.shortFormInfo();
        assertEquals(shortForm, "");
    }

    @Test
    public void testShortFormInfoAsMediaNoNameAndNoYear() {
        String name = "";
        int year = 0;
        testMedia.setName(name);
        assertEquals(testMedia.getName(), name);
        testMedia.setYear(year);
        assertEquals(testMedia.getYear(), year);
        String shortForm = testMedia.shortFormInfo();
        assertEquals(shortForm, "");
    }
}
