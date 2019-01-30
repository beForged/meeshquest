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
        coordinateMap = new TreeMap<>(new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                if(o1.equals(o2)){
                    return 0;
                }else if(o1.getX() + o1.getY() < o2.getX() + o2.getY()){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
        nameMap = new TreeMap<>();
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

}
