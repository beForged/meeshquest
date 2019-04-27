package cmsc420.meeshquest.part2;


import cmsc420.geom.Geometry2D;

import java.awt.geom.Point2D;
import java.util.LinkedList;

//using float here as double precision unnecessary (and it says to in spec)
public class City extends Point2D.Float implements Geometry2D {
    //these are private and should probably have gets/sets
    String name, color;
    int radius;
    boolean isolated;
    //roads that connect (maybe end/beginnings?)
    LinkedList<Road> roads;

    //TODO add list of roads that connect to the city

    //constructor for a city
    public City(String name, int x, int y, int radius, String color){
        this.name = name;
        super.x = x;
        super.y = y;
        this.radius = radius;
        this.color = color;
    }

    //equals func if needed
/*    public boolean equals(City c){
        if(this.equals(c.name) && this.x == c.getX() && this.y == c.getY()) {
            return true;
        }else{
            return false;
        }
    }*/

    //function that returns information about the city
    public String[] cityInfo(){
        return new String[]{name, String.valueOf(x), String.valueOf(y), String.valueOf(radius), color};
    }

    public String toString(){
        return name + "\t\t" + String.valueOf(x) + "\t" + String.valueOf(y) + "\t" + String.valueOf(radius) + "\t" + color;
    }

    public Point2D.Float getCoords(){
        return new Point2D.Float(x, y);
    }


    @Override
    public int getType() {
        return 0;
    }

    public int compareTo(City c){
        if(c.name.equals(this.name)){
            return 0;
        }else
            return -c.name.compareTo(this.name);
    }
}
