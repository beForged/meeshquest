package cmsc420.meeshquest.part1;

import cmsc420.drawing.CanvasPlus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class WhiteNode extends Node{
    //a little bit of communism
    private static WhiteNode ourInstance = new WhiteNode();

    public static WhiteNode getInstance() {
        return ourInstance;
    }

    public boolean containsCity(String city) {
        return false;
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
    public PriorityQueue<Node> nearestCity(int x, int y) {
        return null;
    }

    @Override
    void saveMap(CanvasPlus canvas) {} //doesnt do anything, is just a white node

    //singleton constructor
    private WhiteNode() {
    }


}
