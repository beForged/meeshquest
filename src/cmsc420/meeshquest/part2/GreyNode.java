package cmsc420.meeshquest.part2;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Shape2DDistanceCalculator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class GreyNode extends Node {
    private Node[] quadrants;
    private float centerx, centery;

    Validator validate;

    //initialize with all white nodes as children (maybe ask for all 4 nodes?)
    public GreyNode(Rectangle2D.Float rect, Validator validate) {
        quadrants = new Node[4];
        for (int i = 0; i < 4; i++) {
            quadrants[i] = WhiteNode.getInstance();
        }
        super.height = rect.height;
        super.width = rect.width;
        super.x = rect.x;
        super.y = rect.y;
        centerx = x + width/2;
        centery = y + width/2;

        this.validate =  validate;
    }

    //we need to have a road and city difference
    //this adds the endpoints of roads and isolated cities
    public Node add(Float rect, City city) {
        //LinkedList<Integer> quad= quadrant(city);
        for(int quadrant = 0;quadrant < 4; quadrant++) {
            quadrants[quadrant] = quadrants[quadrant].add(getChildRect(this, quadrant), city);
        }

        return this;
    }

    @Deprecated
    public Node addCity(Float rect, City c) throws cityOutOfBoundsException {
        LinkedList<Integer> q = quadrant(c);
        for(int quad:q){
            quadrants[quad] = quadrants[quad].add(getChildRect(this, quad), c);
        }
        return this;
    }

    public Node addRoad(Float rect, Road r){
        for(int i = 0; i < quadrants.length; i++){
            quadrants[i] = quadrants[i].addRoad(getChildRect(this, i), r);
        }
        return this;
    }
    public Node add(Rectangle2D.Float rect, Road road){

        //first we find what it intersects
        if(road.intersects(this)){
            //if it intersects this grey node then we add
            for(int i = 0; i < 4; i++){
                quadrants[i] = quadrants[i].add(getChildRect(this, i), road);
            }
        }
        return this;
    }


    @Deprecated
    public Node addroads(LinkedList<Road> r){
        for(int i = 0; i < quadrants.length; i++){
            for(Road road:r){
                if(road.intersects(quadrants[i])){
                    quadrants[i] = quadrants[i].add(getChildRect(this, i), road);
                }
            }
        }
        return this;
    }


    public boolean containsCity(String city) {
        for (Node i : quadrants) {
            if (i.containsCity(city)) {
                return true;
            }
        }
        return false;
    }

    /* TODO delete
    public Node deleteCity(String city) {
        int grey = 0;
        int black = 0;
        //replace the node with itself (if doesnt match) or with whitenode (returned from blknode)
        for (int i = 0; i < quadrants.length; i++) {
            Node n = quadrants[i].deleteCity(city);
            if (n instanceof GreyNode) {
                grey++;
            }
            if (n instanceof BlackNode) {
                black++;
            }
            quadrants[i] = n;
        }
        //if 1 blacknode and no grey nodes, return just black node
        if (grey == 0 && black <= 1) {
            for (Node i : quadrants) {
                if (i instanceof BlackNode) {
                    return i;
                }
            }
            return WhiteNode.getInstance();
        }
        return this;
    }
*/

    //we create elements here and return them
    public Element printquadtree(Document doc) {
        Element out = doc.createElement("gray");
        out.setAttribute("x", String.valueOf((int) this.centerx));
        out.setAttribute("y", String.valueOf((int) this.centery));
        for (Node i : quadrants) {
            out.appendChild(i.printquadtree(doc));
        }
        return out;
    }


    public ArrayList<City> rangeCities(int x, int y, int radius) {
        ArrayList<City> rangeCity = new ArrayList<>();
        for (Node i : quadrants) {
            rangeCity.addAll(i.rangeCities(x, y, radius));
        }
        return rangeCity;
    }
    public ArrayList<Road> rangeRoads(int x, int y, int radius) {
        ArrayList<Road> rangeRoad = new ArrayList<>();
        for (Node i : quadrants) {
            rangeRoad.addAll(i.rangeRoads(x, y, radius));
        }
        return rangeRoad;
    }

    @Override
    PriorityQueue<Node> nearestCity(int x, int y) {
        PriorityQueue<Node> nodes = new PriorityQueue<>(new PriorityComparator(x,y));
        for (Node i:quadrants ) {
            PriorityQueue t = i.nearestCity(x,y);
            if(t != null) {
                nodes.addAll(t);
            }
        }
        return nodes;
    }

    @Override
    PriorityQueue<Node> nearestRoad(int x, int y) {
        PriorityQueue<Node> nodes = new PriorityQueue<>(new PriorityComparator(x, y));
        for(Node i:quadrants){
            PriorityQueue t = i.nearestRoad(x,y);
            if(t != null)
                nodes.addAll(t);
        }
        return nodes;
    }

    @Override
    void saveMap(CanvasPlus canvas) {
        //need to draw a cross at center
        canvas.addCross(centerx,centery, height/2, Color.GRAY);
        for(Node i:quadrants){
            i.saveMap(canvas);
        }
    }
}
