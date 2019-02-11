package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;

public class BlackNode implements Node{
    City city;
    public BlackNode(City city){
        this.city = city;
    }

    public City getCity(){
        return city;
    }

    public void setCity(City city){
        this.city = city;
    }

    public Point2D.Float getCoords(){
        return city.getCoords();
    }
}
