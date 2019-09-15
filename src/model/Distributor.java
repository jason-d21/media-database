package model;

import java.util.List;
import java.util.Objects;

public abstract class Distributor {
    private String name;

    public Distributor(String name) {
        this.name = name;
    }

    public abstract List<String> getMediaNames();

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distributor that = (Distributor) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
