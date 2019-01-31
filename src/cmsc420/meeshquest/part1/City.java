package cmsc420.meeshquest.part1;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

import java.awt.geom.Point2D;

//using float here as double precision unnecessary (and it says to in spec)
public class City extends Point2D.Float {
    //these are private and should probably have gets/sets
    String name, color;
    int radius;

    //constructor for a city
    public City(String name, int x, int y, int radius, String color){
        this.name = name;
        super.x = x;
        super.y = y;
        this.radius = radius;
        this.color = color;
    }

    //function that returns information about the city
    public String[] cityInfo(){
        return new String[]{name, String.valueOf(x), String.valueOf(y), String.valueOf(radius), color};
    }

  }
