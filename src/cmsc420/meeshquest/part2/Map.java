package cmsc420.meeshquest.part2;

import cmsc420.geom.Circle2D;
import cmsc420.sortedmap.Treap;
import cmsc420.sortedmap.Treap2;

import java.awt.geom.Point2D;
import java.util.*;

//this for now is the top level essentially
//this may need to change significantly later
public class Map {
    TreeMap<Point2D, City> coordinateMap;
    TreeMap<String , City> nameMap;
    PMQuadtree quadTree;
    TreeMap<City, TreeSet<City>> adjacencyList;
    Treap<String, Point2D> treapMap;
    TreeMap<String, Point2D> testingTreap;
    int width, height;
    int order;

    public Map(int width, int height, int order){

        //TODO replace with treap at somepoint
        //comparator that compares by number
        coordinateMap = new TreeMap<>((o1, o2) -> {
                    if(o1.getX() == o2.getX() && o1.getY() == o2.getY()){
                        return 0;
                    }else if(o1.getY() > o2.getY()) {
                        return 1;
                    }else if(o1.getY() < o2.getY()){
                            return -1;
                    }else if(o1.getX() > o2.getX()){
                        return 1;
                    }else{
                        return -1;
            }
        });
        //reverse the regular string compareto
        nameMap = new TreeMap<>((o1, o2) -> -o1.compareTo(o2));

        this.width = width;
        this.height = height;

        this.order = order;

        //System.err.println(width + ", " + height);
        if(order == 3){
            quadTree = new PM3Quadtree(width, height);
        }
        //I guess this is not reverse asciibetical?
        treapMap = new Treap<String, Point2D>(((o1, o2) -> -((String)o1).compareTo((String)o2)));
        adjacencyList = new TreeMap<>();
    }

    public void addRoad(Road road) throws GenericException {
        if(road.start.name.equals(road.end.name))
            throw new GenericException("startEqualsEnd");
        if(nameMap.get(road.start.name).isolated || nameMap.get(road.end.name).isolated)
            throw new GenericException("startOrEndIsIsolated");
        quadTree.add(road);
        addRoadAdj(road);
    }

    public void addRoadAdj(Road road){
        if(adjacencyList.get(road.end) == null){
            TreeSet<City> r = new TreeSet<>();
            r.add(road.start);
            adjacencyList.put(road.end,  r);
        }else{
            TreeSet<City> r = adjacencyList.get(road.end);
            r.add(road.start);
            adjacencyList.put(road.end, r);
        }
        if(adjacencyList.get(road.start) == null){
            TreeSet<City> r = new TreeSet<>();
            r.add(road.end);
            adjacencyList.put(road.start,  r);
        }else{
            TreeSet<City> r = adjacencyList.get(road.start);
            r.add(road.end);
            adjacencyList.put(road.start, r);
        }
    }

    //djikstras helper
    private class CityNode{
        String name;
        City city;
        Double pathdist;
        public List<CityNode> shortestPath = new LinkedList<>();
        public double dist = Double.MAX_VALUE;
        HashMap<City, Double> adjacent = new HashMap<City, Double>();
        CityNode(String n, Double p ) {
            name = n;
            pathdist = p;
            city = nameMap.get(name);
        }
        void addNeighbor(List<City> a){
            for(City c:a){
                adjacent.put(c, city.distance(c));
            }
        }
    }

    public ArrayList djikstras(String start, String end) throws GenericException {//todo
        City s = nameMap.get(start);
        City e = nameMap.get(end);

        if(adjacencyList.get(s) == null)
            throw new GenericException("nonExistentStart");
        if(adjacencyList.get(e) == null)
            throw new GenericException("nonExistentEnd");


        ArrayList<City> settled = new ArrayList<>();
        TreeSet<City> unsettled = new TreeSet<>(((o1, o2) -> (int)o1.distance(o2)));


        ArrayList<City> path= new ArrayList<>();
        unsettled.add(s);
        City work;
        while((work = unsettled.pollFirst()) != null || !settled.contains(e)){
            for(City neighbor: adjacencyList.get(work)){
                System.out.println(settled);
                double dist = neighbor.distance(work);
                if(!settled.contains(neighbor)){
                    System.out.println(work.name + " to " + neighbor.name);
                    if(dist < neighbor.distance){
                        neighbor.distance = dist;
                        neighbor.prev = work;
                    }
                    unsettled.add(neighbor);
                }
            }
            settled.add(work);
        }
        s.prev = null;
        City prev = e;
        while(prev != null){
            path.add(prev);
            prev = prev.prev;
        }
        path.add(s);
        Collections.reverse(path);
        return path;
    }
    public City getLowestDist(City start){
        TreeSet<City> adjacent = adjacencyList.get(start);
        double dist = adjacent.first().distance(start);
        City closest = adjacent.first();
        for(City c:adjacent){
            if(c.distance(start) < dist){
                dist = c.distance(start);
                closest = c;
            }
        }
        return closest;

        //return (start.equals(closest.start.name)) ? closest.end.name : closest.start.name;
    }

    public ArrayList<City> getShortest(String start, String end) throws GenericException {
        ArrayList<City> a = djikstras(start,end);
        if(a.size() == 0){
            throw new GenericException("noPathExists");
        }
        return a;
    }
    //check that name and coordinates are not taken
    public String addCity(City city){
        //check for duplicates
        Point2D.Float coord = new Point2D.Float(city.x, city.y);
        if(coordinateMap.containsKey(coord)){
            return "duplicateCityCoordinates";
            //TODO throw new duplicateCityCoordinates();
        }else if(nameMap.containsKey(city.name)){
            return "duplicateCityName";
            //TODO throw new duplicateCityName();
        }else{
            treapMap.put(city.name, new Point2D.Float(city.x, city.y));
            coordinateMap.put(new Point2D.Float(city.x, city.y), city);
            nameMap.put(city.name, city);
            return "success";
        }
    }

    public City deleteCity(String name) throws cityDoesNotExistException{
        if(!nameMap.containsKey(name)){
            throw new cityDoesNotExistException("cityDoesNotExist");
        }
        //remove from quadtree if mapped
        //boolean deleted = quadTree.deleteCity(name);
        boolean deleted = true;
        //if the city exists, remove from namemap and get the city object
        City rem = nameMap.remove(name);
        //todo remove for treap
        //then remove from coordinate map
        coordinateMap.remove(new Point2D.Float((int)rem.getX(), (int)rem.getY()));
        //success so return null
        if(deleted){
            return rem;
        }else{
            return null;
        }
    }


}
