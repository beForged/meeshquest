package cmsc420.meeshquest.part1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

public interface Node {
//nothing here yet

    boolean containsCity(String city);

    void addCity(City city);

    Node deleteCity(String city);

    Element printquadtree(Document doc);

    ArrayList<City> rangeCities(int x, int y, int radius);
}
