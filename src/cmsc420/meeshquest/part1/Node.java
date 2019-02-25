package cmsc420.meeshquest.part1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface Node {
//nothing here yet

    boolean containsCity(String city);

    void addCity(City city);

    Node deleteCity(String city);

    Element printquadtree(Document doc);
}
