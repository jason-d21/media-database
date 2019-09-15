package test;

import model.Company;
import model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.ManageMedia;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ManageMovieSaveAndLoadTest {
    private ManageMedia mlu;
    private Movie blankMovie;
    private Movie movieOneGenre;
    private Movie movie1;
    private Movie movie2;
    private ArrayList<String> genres1 = new ArrayList<>();
    private ArrayList<String> genres2 = new ArrayList<>();
    private ArrayList<String> oneGenre = new ArrayList<>();

    @BeforeEach
    public void setup() throws IOException {
        PrintWriter writer = new PrintWriter("save");
        writer.close();

        genres1.clear();
        genres2.clear();
        oneGenre.clear();

        oneGenre.add("Action");
        movieOneGenre = new Movie("Inception", 2010, 825532764, oneGenre,
                86, new Company("WB"));

        genres1.add("Action");
        genres1.add("Adventure");
        genres1.add("Sci-fi");
        genres2.add("Mystery");
        genres2.add("Thriller");

        mlu = new ManageMedia("test");
        mlu.getMovieList().getMovieList().clear();
        blankMovie = new Movie();


        movie1 = new Movie("Inception", 2010, 825532764, genres1,
                86, new Company("WB"));
        movie2 = new Movie("Memento", 2000, 39723096, genres2,
                92, new Company("Newmarket Films"));
    }

    @Test
    public void testLoadBlankFile() throws IOException {
        mlu.load("testloadblank");
        assertEquals(mlu.getMovieList().getMovieList().size(), 0);
    }

    @Test
    public void testLoadOneGenre() {
        try {
            mlu.load("testloadonegenre");
            assertEquals(mlu.getMovieList().getMovieList().size(), 1);
            assertTrue(mlu.getMovieList().getMovieList().get(0).isSameMovie(movieOneGenre));
        }
        catch (IOException e) {
            fail("unexpected load failure. File not found");
        }
    }

    @Test
    public void testLoadManyGenres() {
        try {
            mlu.load("testloadmanygenres");
            assertEquals(mlu.getMovieList().getMovieList().size(), 2);
            assertTrue(mlu.getMovieList().getMovieList().get(0).isSameMovie(movie1));
            assertTrue(mlu.getMovieList().getMovieList().get(1).isSameMovie(movie2));
        }
        catch (IOException e) {
            fail("unexpected load failure. File not found");
        }
    }

    @Test
    public void testLoadBlankMovies() {
        try {
            mlu.load("testloadblankmovies");
            assertEquals(mlu.getMovieList().getMovieList().size(), 1);
            assertTrue(mlu.getMovieList().getMovieList().get(0).isBlank());
        }
        catch (IOException e) {
            fail("unexpected load failure. File not found");
        }
    }

    @Test
    public void testLoadFileNotFound() {
        try {
            mlu.load("");
            fail("unexpected load pass");
        }
        catch (IOException e) {
            System.out.println("Expected! File not found");
        }
    }


    @Test
    public void testSaveNoMovies() {
        try {
            mlu.save("testsave");
            List<String> lines = Files.readAllLines(Paths.get("testsave"));
            ;
            assertTrue(lines.isEmpty());
        }
        catch (IOException e) {
            fail("Unexpected save failure");
        }
    }

    @Test
    public void testSaveOneGenre() {
        try {
            mlu.addToList(movieOneGenre);
            mlu.save("testsave");
            List<String> lines = Files.readAllLines(Paths.get("testsave"));
            ;
            assertEquals(lines.size(), 1);
            assertEquals(lines.get(0), "Movie  Inception  2010  8.25532764E8  Action  86  WB");
        }
        catch (IOException e) {
            fail("Unexpected save failure");
        }
    }

    @Test
    public void testSaveManyGenres() {
        try {
            mlu.addToList(movie1);
            mlu.addToList(movie2);
            mlu.save("testsave");
            List<String> lines = Files.readAllLines(Paths.get("testsave"));
            ;
            assertEquals(lines.size(), 2);
            assertEquals(lines.get(0), "Movie  Inception  2010  8.25532764E8  Action  Adventure  Sci-fi  86  WB");
            assertEquals(lines.get(1), "Movie  Memento  2000  3.9723096E7  Mystery  Thriller  92  Newmarket Films");
        }
        catch (IOException e) {
            fail("Unexpected save failure");
        }
    }

    @Test
    public void testSaveBlankMovies() {
        try {
            mlu.addToList(blankMovie);
            mlu.save("testsave");
            List<String> lines = Files.readAllLines(Paths.get("testsave"));
            ;
            assertEquals(lines.size(), 1);
            assertEquals(lines.get(0), "Movie  unknown  unknown  unknown  unknown  unknown  unknown");
        }
        catch (IOException e) {
            fail("Unexpected save failure");
        }
    }

    @Test
    public void testSaveFailed() {
        try {
            mlu.addToList(blankMovie);
            mlu.save("");
            fail("Unexpected pass");
        }
        catch (IOException e) {
            System.out.println("Expected! File save failed");
        }
    }
}
