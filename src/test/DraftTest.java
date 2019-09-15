package test;

import model.Company;
import model.Movie;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class DraftTest {
    @Test
    public void Test() {
        Set<Company> companyList = new HashSet<>();
         Movie m1 = new Movie();
         m1.setName("Superman");
         Movie m2 = new Movie();
         m2.setName("Batman");
         Company wb = new Company("WB");
         m1.setCompany(wb);
         companyList.add(wb);
         m2.setCompany(wb);
         System.out.println(wb.getMediaNames());

    }
}
