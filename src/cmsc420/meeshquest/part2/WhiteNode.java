package cmsc420.meeshquest.part2;

import cmsc420.drawing.CanvasPlus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class WhiteNode extends Node{
    //a little bit of communism
    static Validator valid;
    private static WhiteNode ourInstance = new WhiteNode();

    public static void setValidator(Validator v){
        valid = v;
    }
    public static WhiteNode getInstance() {
        return ourInstance;
    }

    public boolean containsCity(String city) {
        return false;
    }

    @Override
    Node add(Float rect, Road road) {
        if(road.intersects(rect)) {
            Node newBlackNode = new BlackNode(rect, valid);
            //System.err.println(newBlackNode.toString());
            newBlackNode = newBlackNode.add(rect, road);
            return newBlackNode;
        }
        return this;
    }



    //should also be able to handle isolated cities
    Node add(Float rect, City city) {
        if(rect.contains(city)) {
            Node b = new BlackNode(rect, valid);
            b = b.add(rect, city);
            return b;
        }
        return this;
    }


    Node addRoad(Float rect, Road road){
        BlackNode b = new BlackNode(rect, valid);
        if(road.intersects(rect)) {
            b.addRoad(rect, road);
            return b;
        }
        else return this;
    }

    Node remove() {
        return null;
    }

    //does nothing
    public void addCity(City city) {
        return;
    }

    public Node deleteCity(String c) {
        return this;
    }

    public Element printquadtree(Document doc) {
        return doc.createElement("white");
    }

    @Override
    public ArrayList<City> rangeCities(int x, int y, int radius) {
        return new ArrayList<>();
    }
    @Override
    public ArrayList<Road> rangeRoads(int x, int y, int radius) {
        return new ArrayList<>();
    }

    @Override
    public PriorityQueue<Node> nearestCity(int x, int y) {
        return null;
    }

    @Override
    PriorityQueue<Node> nearestRoad(int x, int y) {
        return null;
    }

    @Override
    void saveMap(CanvasPlus canvas) {} //doesnt do anything, is just a white node

    //singleton constructor
    private WhiteNode() {
    }


}
