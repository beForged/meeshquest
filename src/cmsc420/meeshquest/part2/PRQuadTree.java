/*package cmsc420.meeshquest.part2;

import cmsc420.drawing.CanvasPlus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class PRQuadTree {

    int height, width;
    int x, y;
    Node root;

    public PRQuadTree(int height, int width){
        root = WhiteNode.getInstance();
        this.x = 0;
        this.y = 0;
        this.height = height; this.width = width;
    }

    //may need return type to do errors tbh
    public void addCity(City city) throws CityAlreadyMappedException, cityOutOfBoundsException {
        if (city.getX() >= width || city.getX() < 0 || city.getY() >= height || city.getY() < 0){
            throw new cityOutOfBoundsException("cityOutOfBounds");
        }
        if(containsCity(city)){
            throw new CityAlreadyMappedException("cityAlreadyMapped");
        }
        //if the root is a white node
        if (root instanceof BlackNode){
            City temp = ((BlackNode) root).getCity();
            root = new GreyNode(height, width, x, y);
            root.addCity(temp);
            root.addCity(city);
        }
        else if(root.equals(WhiteNode.getInstance())) {
            //root = new GreyNode(height, width, x, y);
            root = new BlackNode(city);
        }
        //we change it to a greynode and then add the city
        else{//must be a greynode otherwise
            root.addCity(city);
        }
    }

    //if city is found and successfully deleted, return true else false
    public boolean deleteCity(String city){
        if(containsCity(city)){
            //there is the city in it.
            root = root.deleteCity(city);
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
        ArrayList<City> citiesInRange = new ArrayList<>();
        citiesInRange.addAll(root.rangeCities(x, y, radius));
        if(citiesInRange.size() == 0){
            throw new noCitiesExistInRangeException("noCitiesExistInRange");
        }
        //sort the return array
        Collections.sort(citiesInRange, (o1,o2) -> -o1.name.compareTo(o2.name));
        return citiesInRange;
    }

    public City nearestCity(int x, int y) throws mapisEmptyException {
        PriorityQueue<Node> cities = new PriorityQueue<>(new PriorityComparator(x,y));
        if (root instanceof WhiteNode){
            throw new mapisEmptyException("mapIsEmpty");
        }
        cities.add(root);
        //while nearest thing is a grey node
        while(cities.peek() instanceof GreyNode){
            //take the head out - TODO should check that the next thing isnt the same dist. but..
            Node head = cities.poll();
            PriorityQueue<Node> result = head.nearestCity(x,y);
            for(Node i:result){
                cities.add(i);
            }
        }
        //cant be white bc they arent added to the queue and while already removed greys so we gucci
        return ((BlackNode) cities.peek()).city;
    }


    //convention for this will be -1 if we dont want to have the radius
    public void saveMap(String name, int x, int y, int radius){
        //initiallize everything to start out
        CanvasPlus canvas = new CanvasPlus("MeeshQuest");
        canvas.setFrameSize(width, height);
        canvas.addRectangle(0, 0, width, height, Color.WHITE, true);
        canvas.addRectangle(0, 0, width, height, Color.BLACK, false);
        if(x != -1) {
            canvas.addCircle(x, y, radius, Color.BLUE, false);
        }
        root.saveMap(canvas);
        //this is to save the thing to file lol
        try {
            canvas.save(name);
        }catch (IOException e){
            e.printStackTrace();
        }
        //responsibly free memory
        canvas.dispose();
    }

}
*/