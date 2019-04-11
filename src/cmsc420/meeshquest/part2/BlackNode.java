package cmsc420.meeshquest.part2;

import cmsc420.drawing.CanvasPlus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class BlackNode extends Node {
    //can have a city w no roads
    //or a city w roads
    City city;

    //these are the roads
    LinkedList<Road> roads;


    //this should imply that this includes a road
    //this means that cities must be passed to black node
    public BlackNode(float height, float width, float x, float y){
        //city = road.getEnd(); TODO get city up and running which side of road are we on
        this.city = city;
        roads = new LinkedList<>();
        super.height = height;
        super.width = width;
        super.x = x;
        super.y = y;
    }

    //we only have a const that makes the squares then add stuff in. dont do too much at once
/*
    //i guess if this is done then this is an orphaned city
    public BlackNode(float height, float width, City city) {
        this.city = city;
        super.height = height;
        super.width = width;
        super.x = city.x;
        super.y = city.y;
        roads = new LinkedList<>();
    }
*/
//should only be zero or one cities per black node
    public City getCity() {
        return city;
    }

    //
    public Node add(City city) {
        if(this.city == null){
            this.city = city;
        }else{
            //throw new CityAlreadyMappedException("City Already Mapped");
        }
        //TODO validator here
        //if valid return this, else partition

        return this;
    }

    Node add(Road road) {
        //TODO check for need to partition as well as intersection
        //or something like that
        if(road.intersects(this)) {
            roads.add(road);
        }
        //TODO validator here
        return this;
    }

    Node partition(City city){
        GreyNode g = new GreyNode(this.height, this.width, this.x, this.y);
        g.add(this.city);
        g.add(city);
        //g.add(this.roads);
        return g;
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

    //this should add a road?

/*
    public Node deleteCity(String city) {
        if (city.equals(this.city.name)) {
            return WhiteNode.getInstance();
        } else {
            return this;
        }
    }
*/

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
