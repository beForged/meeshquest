package cmsc420.meeshquest.part2;

import cmsc420.sortedmap.Treap;

import java.awt.geom.Point2D;
import java.util.TreeMap;
import java.util.TreeSet;

//this for now is the top level essentially
//this may need to change significantly later
public class Map {
    TreeMap<Point2D, City> coordinateMap;
    TreeMap<String , City> nameMap;
    PMQuadtree quadTree;
    TreeMap<String, TreeSet<Road>> adjacencyList;
    Treap treapMap;
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

        treapMap = new Treap<>((o1, o2) -> -((String)o1).compareTo((String)o2));
        adjacencyList = new TreeMap<>();
    }

    public void addRoad(Road road) throws GenericException {
        if(!nameMap.containsKey(road.start.name)){
            throw new GenericException("startPointDoesNotExist");
        }
        if(!nameMap.containsKey(road.end.name))
            throw new GenericException("endPointDoesNotExist");
        if(road.start.name.equals(road.end.name))
            throw new GenericException("startEqualsEnd");
        if(nameMap.get(road.start.name).isolated || nameMap.get(road.end.name).isolated)
            throw new GenericException("startOrEndIsIsolated");
        quadTree.add(road);
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
