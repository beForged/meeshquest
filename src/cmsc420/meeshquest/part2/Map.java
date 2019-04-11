package cmsc420.meeshquest.part2;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.TreeMap;

//this for now is the top level essentially
//this may need to change significantly later
public class Map {
    TreeMap<Point2D, City> coordinateMap;
    TreeMap<String , City> nameMap;
    PRQuadTree quadTree;
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

        quadTree = new PRQuadTree(height, width);
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
        boolean deleted = quadTree.deleteCity(name);
        //if the city exists, remove from namemap and get the city object
        City rem = nameMap.remove(name);
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
