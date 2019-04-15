package cmsc420.meeshquest.part2;

import cmsc420.drawing.CanvasPlus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class BlackNode extends Node {
    //can have a city w no roads
    //or a city w roads
    City city;

    //these are the roads
    LinkedList<Road> roads;

    Validator valid;

    //this should imply that this includes a road
    //this means that cities must be passed to black node
    public BlackNode(Rectangle2D.Float rect, Validator valid){
        //city = road.getEnd(); TODO get city up and running which side of road are we on
        roads = new LinkedList<>();
        super.height = rect.height;
        super.width = rect.width;
        super.x = rect.x;
        super.y = rect.y;
        this.valid = valid;
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

    //adds a isolated city
    public Node addCity(Float rect, City city){
        return valid.validate(this, city);
    }

    public Node addRoad(Road road){
        //todo maybe you need to throw road cant be mapped or somethi
        roads.add(road);
        return this;
    }
    //isolated cities?
    public Node add(Float rect, City city) {
        Node ret = this;
        if(this.contains(city))
            ret = valid.validate(ret, city);
        //if pm3 then as long as there isnt a city here, we add the city else partition
        //if pm1 then only 1 line or city with edges from it
        return ret;
    }

    Node add(Float rect,Road road) {
        //if anything is contained or intersects then we add
        Node ret = this;
        if(ret.contains(road.start))
            ret = ret.add(ret, road.start);
        if(ret.contains(road.end))
            ret = ret.add(ret, road.end);
        if(road.intersects(ret))
            ret = valid.validate(ret, road);
        return ret;
    }


    Node partition(City city){
        GreyNode g = new GreyNode(this, valid);
        g.add(this.city);
        g.add(city);
        //g.add(this.roads);
        return g;
    }

    //can return null if city doesnt exist
    public Point2D.Float getCoords() {
        return city.getCoords();
    }

    //can be null
    public boolean containsCity(String city) {
        if (this.city != null && this.city.name.equals(city)) {
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
