package cmsc420.meeshquest.part2;

import cmsc420.drawing.CanvasPlus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class BlackNode extends Node {
    City city;
    List geometry;



    public BlackNode(){

    }

    //i guess if this is done then this is an orphaned city
    public BlackNode(City city) {
        this.city = city;
        super.height = 0;
        super.width = 0;
        super.x = city.x;
        super.y = city.y;

    }

    public City getCity() {
        return city;
    }

    public void addCity(City city) {
        this.city = city;
    }

    public Point2D.Float getCoords() {
        return city.getCoords();
    }

    public boolean containsCity(String city) {
        if (this.city.name.equals(city)) {
            return true;
        }
        return false;
    }

    @Override
    Node add() {
        return null;
    }

    @Override
    Node remove() {
        return null;
    }

    public Node deleteCity(String city) {
        if (city.equals(this.city.name)) {
            return WhiteNode.getInstance();
        } else {
            return this;
        }
    }

    public Element printquadtree(Document doc) {
        Element black = doc.createElement("black");
        black.setAttribute("name", city.name);
        black.setAttribute("x", String.valueOf((int) getCity().getX()));
        black.setAttribute("y", String.valueOf((int) getCity().getY()));
        return black;
    }

    public ArrayList<City> rangeCities(int x, int y, int radius) {
        ArrayList<City> citiesInRange = new ArrayList<>();
        if (this.city.distance(new Point2D.Float((float) x, (float) y)) <= radius) {
            citiesInRange.add(this.city);
        }
        return citiesInRange;
    }

    @Override
    PriorityQueue<Node> nearestCity(int x, int y) {
        PriorityQueue<Node> a = new PriorityQueue<>(new PriorityComparator(x,y));
        a.add(this);
        return a;
    }

    @Override
    void saveMap(CanvasPlus canvas) {
        canvas.addPoint(city.name, x, y, Color.BLACK);
    }

}
