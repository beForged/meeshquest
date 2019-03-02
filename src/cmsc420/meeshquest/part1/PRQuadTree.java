package cmsc420.meeshquest.part1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PRQuadTree {

    float height, width;
    int x, y;
    Node root;
    Point2D.Float center;

    public PRQuadTree(int height, int width){
        root = WhiteNode.getInstance();
        this.x = 0;
        this.y = 0;
        this.height = height; this.width = width;
    }

    //may need return type to do errors tbh
    public void addCity(City city) throws CityAlreadyMappedException, cityOutOfBoundsException {
        if (city.getX() > width || city.getX() < 0 || city.getY() > height || city.getY() < 0){
            throw new cityOutOfBoundsException("cityOutOfBounds");
        }
        if(containsCity(city)){
            throw new CityAlreadyMappedException("cityAlreadyMapped");
        }
        //if the root is a white node
        if(root.equals(WhiteNode.getInstance())) {
            root = new GreyNode(height, width, x, y);
            root.addCity(city);
        }
        else if (root instanceof GreyNode){
            root.addCity(city);
        }
    }

    //if city is found and successfully deleted, return true else false
    public boolean deleteCity(String city){
        if(containsCity(city)){
            //there is the city in it.
            root.deleteCity(city);
            return true;
        }
        return false;
    }

    public boolean containsCity(String city){
            return root.containsCity(city);
    }

    public boolean containsCity(City city){
        return containsCity(city.name);
    }

    public void clear(){
        root = WhiteNode.getInstance();
    }

    public Element printquadtree(Document doc) throws mapisEmptyException{
        if(root instanceof WhiteNode){
            throw new mapisEmptyException("mapIsEmpty");
        }
        return root.printquadtree(doc);
    }

    public ArrayList<City> rangeCities(int x, int y, int radius) throws noCitiesExistInRangeException{
        //new array put here
        ArrayList<City> citiesInRange = new ArrayList<City>();
        citiesInRange.addAll(root.rangeCities(x, y, radius));
        if(citiesInRange.size() == 0){
            throw new noCitiesExistInRangeException("noCitiesExistInRange");
        }
        Collections.sort(citiesInRange, (o1,o2) -> -o1.name.compareTo(o2.name));
        return citiesInRange;
    }

    public City nearestCity(int x, int y) throws mapisEmptyException {
        PriorityQueue<Node> cities = new PriorityQueue<>(new PriorityComparator(x,y));
        if (root instanceof WhiteNode){
            throw new mapisEmptyException("mapIsEmpty");
        }
        cities.add(root);
        while(cities.peek() instanceof GreyNode){
            Node head = cities.poll();
            PriorityQueue<Node> result = head.nearestCity(x,y);
            for(Node i:result){
                cities.add(i);
            }
        }
        //cant be white bc they arent added to the queue and while already removed greys so we gucci
        return ((BlackNode) cities.peek()).city;
    }

}
