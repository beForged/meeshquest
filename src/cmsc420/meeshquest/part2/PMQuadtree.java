package cmsc420.meeshquest.part2;

import cmsc420.drawing.CanvasPlus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.*;
import java.util.List;

public abstract class PMQuadtree {

    //height and width of the map
    public int width, height;


    //set of all roads in the quadtree
    TreeSet<Road> all;

    //number of isolated cities
    public int isolatedCities;

    //list of all cities and roads that start there
    //TODO maybe another one with road terminations?
    TreeMap<City, List<Road>> cities;

    //this is the root node and we set it to singleton white at first
    public Node root = WhiteNode.getInstance();

    //TODO part 3
    public void remove(){
        throw new UnsupportedOperationException();
    }

    public void add(Road road){
        root = root.add(new Rectangle2D.Float(0,0, width, height), road);
    }

    public void addCity(City city) throws cityOutOfBoundsException, CityAlreadyMappedException {

        if(root.containsCity(city.name)){//todo probably fix this
            throw new CityAlreadyMappedException("cityAlreadyMapped");
        }
        Rectangle2D.Float rect = new Rectangle2D.Float(0, 0, width, height);
        if(!rect.contains(city)){
            throw new cityOutOfBoundsException("cityOutOfBouds");
        }
        root.add(root, city);
    }

    public boolean containsCity(String c){
        return root.containsCity(c);
    }

    public Element printquadtree(Document doc) throws mapisEmptyException{
        if(root instanceof WhiteNode){
            throw new mapisEmptyException("mapIsEmpty");
        }
        return root.printquadtree(doc);
    }

    public void clear(){
        root = WhiteNode.getInstance();
    }

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

    public City nearestCity(int x, int y, boolean isolated) throws mapisEmptyException {
        PriorityQueue<Node> cities = new PriorityQueue<>(new PriorityComparator(x,y));
        if (root instanceof WhiteNode){
            throw new mapisEmptyException("cityNotFound");
        }
        cities.add(root);
        //while nearest thing is a grey node
        while(cities.peek() instanceof GreyNode){
            //take the head out - TODO should check that the next thing isnt the same dist.
            Node head = cities.poll();
            PriorityQueue<Node> result = head.nearestCity(x,y);
            for(Node i:result){
                if(i instanceof BlackNode){
                    if(((BlackNode) i).getCity().isolated && isolated){
                        cities.add(i);
                    }else if(!((BlackNode) i).getCity().isolated && !isolated){
                        cities.add(i);
                    }
                }else
                    cities.add(i);
            }
        }
        //cant be white bc they arent added to the queue and while already removed greys so we gucci
        return ((BlackNode) cities.peek()).getCity();
    }

    public Road nearestRoad(int x, int y) throws mapisEmptyException {
        PriorityQueue<Node> roads = new PriorityQueue<>(new RoadPriorityComparator(x,y));
        if(root instanceof WhiteNode){
            throw new mapisEmptyException("cityNotFound");
        }
        roads.add(root);
        while(roads.peek() instanceof GreyNode){
            Node head = roads.poll();
            PriorityQueue<Node> result = head.nearestRoad(x, y);
            for(Node i: result){
                roads.add(i);
            }
        }
        return ((BlackNode)roads.peek()).nearestRoadroad(x, y);
    }

    public ArrayList<City> rangeCities(int x, int y, int radius) throws noCitiesExistInRangeException{
        //new array put here
        ArrayList<City> citiesInRange = new ArrayList<>();
        citiesInRange.addAll(root.rangeCities(x, y, radius));
        if(citiesInRange.size() == 0){
            throw new noCitiesExistInRangeException("noCitiesExistInRange");
        }
        //sort the return array
        Collections.sort(citiesInRange, (o1, o2) -> -o1.name.compareTo(o2.name));
        return citiesInRange;
    }

    public ArrayList<Road> rangeRoads(int x, int y, int radius) throws noCitiesExistInRangeException{
        ArrayList<Road> roadsInRange = new ArrayList<>();
        roadsInRange.addAll(root.rangeRoads(x,y,radius));
        if(roadsInRange.size() == 0){
            throw new noCitiesExistInRangeException("noRoadsExistInRange");
        }
        Collections.sort(roadsInRange, (o1, o2) -> -o1.start.name.compareTo(o2.start.name));
        return roadsInRange;
    }

}
