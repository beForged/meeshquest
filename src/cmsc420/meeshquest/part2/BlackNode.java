package cmsc420.meeshquest.part2;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Geometry2D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class BlackNode extends Node {
    //can have a city w no roads
    //or a city w roads

    //these are the roads
    TreeSet<Geometry2D> roads = new TreeSet<>( ((o1, o2) -> {
        if(o1 instanceof Road && o2 instanceof City){
            return -1;
        }if(o1 instanceof City && o2 instanceof Road){
            return 1;
        }else return 0;
    }));

    Validator valid;

    public boolean isIsolated;
    //this should imply that this includes a road
    //this means that cities must be passed to black node
    public BlackNode(Rectangle2D.Float rect, Validator valid){
        super.height = rect.height;
        super.width = rect.width;
        super.x = rect.x;
        super.y = rect.y;
        this.valid = valid;
    }

/*
    //i guess if this is done then this is an orphaned city
    public BlackNode(float height, float width, City city) {
        this.city = city;
        super.height = height;
        super.width = width;
        super.x = city.x;
        super.y = city.y;
        roads = new LinkedList<>();
    }
*/
//should only be zero or one cities per black node

    public City getCity() {
        if(roads.first() instanceof City){
            return (City)roads.first();
        }
        return null;
    }

    //adds a isolated city
    Node add(Float rect,Road road) {
        Node ret = this;
        //add both cities
        if(ret.contains(road.start))
            ret = ret.add(ret, road.start);
        if(ret.contains(road.end))
            ret = ret.add(ret, road.end);
        //finally add the road
        if(road.intersects(ret))
            ret = valid.validate(ret);
        return ret;
    }

    Node add(Float rect, City city){
        roads.add(city);
        Node ret = this;
        ret = valid.validate(ret);
        return ret;
    }

    //can return null if city doesnt exist
    public Point2D.Float getCoords() {
        if(this.getCity() == null){
            return null;
        }
        return getCity().getCoords();
    }

    //can be null
    public boolean containsCity(String city) {
        if (this.getCity()!= null && this.getCity().name.equals(city)) {
            return true;
        }
        return false;
    }

    //this should add a road?

/*
    public Node deleteCity(String city) {
        if (city.equals(this.city.name)) {
            return WhiteNode.getInstance();
        } else {
            return this;
        }
    }
*/

    public Element printquadtree(Document doc) {
        Element black = doc.createElement("black");
        black.setAttribute("cardinality", String.valueOf(roads.size()));
        if(roads.first() instanceof City){
            Element sub = doc.createElement("city");
            black.setAttribute("name", getCity().name);
            black.setAttribute("color", getCity().color);
            black.setAttribute("radius", String.valueOf(getCity().radius));
            black.setAttribute("x", String.valueOf((int) getCity().getX()));
            black.setAttribute("y", String.valueOf((int) getCity().getY()));
            black.appendChild(sub);
        }
        for (Geometry2D n:roads) {
            if(n instanceof Road) {
                Element sub = doc.createElement("road");
                sub.setAttribute("end", ((Road)n).end.name);
                sub.setAttribute("start", ((Road)n).start.name);
            }
        }
        return black;
    }

    public ArrayList<City> rangeCities(int x, int y, int radius) {
        ArrayList<City> citiesInRange = new ArrayList<>();
        if (this.getCity().distance(new Point2D.Float((float) x, (float) y)) <= radius) {
            citiesInRange.add(this.getCity());
        }
        return citiesInRange;
    }

    @Override
    PriorityQueue<Node> nearestCity(int x, int y) {
        PriorityQueue<Node> a = new PriorityQueue<>(new PriorityComparator(x,y));
        a.add(this);
        return a;
    }

    @Override
    void saveMap(CanvasPlus canvas) {
        canvas.addPoint(getCity().name, x, y, Color.BLACK);
    }

}
