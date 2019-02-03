package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.TreeMap;

//this for now is the top level essentially
//this may need to change significantly later
public class Map {
    TreeMap<Point2D, City> coordinateMap;
    TreeMap<String , City> nameMap;
    public Map(){

        //we have a comparator here so that we can see if there is a point that is in the same
        //place. could also use x == x and y==y is .equals doesnt work
        //TODO make this sortable so that compareTo arrays.sort works correctly
        coordinateMap = new TreeMap<>(new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
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
            }
        });
        nameMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return -o1.compareTo(o2);
            }
        });
    }

    //check that name and coordinates are not taken
    public String addCity(City city){
        //check for duplicates
        Point2D.Float coord = new Point2D.Float(city.x, city.y);
        if(coordinateMap.containsKey(coord)){
            return "duplicateCityCoordinates";
        }else if(nameMap.containsKey(city.name)){
            return "duplicateCityName";
        }else{
            coordinateMap.put(new Point2D.Float(city.x, city.y), city);
            nameMap.put(city.name, city);
            return "success";
        }
    }

    public String deleteCity(String name){
        //TODO remove from quadtree if mapped
        if(nameMap.containsKey(name)){
            //if the city exists, remove from namemap and get the city object
            City rem = nameMap.remove(name);
            //then remove from coordinate map
            coordinateMap.remove(new Point2D.Float((int)rem.getX(), (int)rem.getY()));
            //success so return null
            return null;
        }
        return "cityDoesNotExist";
    }

}
