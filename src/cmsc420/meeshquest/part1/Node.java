package cmsc420.meeshquest.part1;

import cmsc420.drawing.CanvasPlus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.PriorityQueue;

public abstract class Node extends Rectangle2D.Float{

    abstract boolean containsCity(String city);

    abstract void addCity(City city);

    abstract Node deleteCity(String city);

    abstract Element printquadtree(Document doc);

    abstract ArrayList<City> rangeCities(int x, int y, int radius);

    abstract PriorityQueue<Node> nearestCity(int x, int y);

    abstract void saveMap(CanvasPlus canvas);

}
