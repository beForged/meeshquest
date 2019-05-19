package cmsc420.meeshquest.part3;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Geometry2D;
import cmsc420.geom.Shape2DDistanceCalculator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class BlackNode extends Node {

    //these are the roads
    TreeSet<Geometry2D> roads = new TreeSet<>( ((o1, o2) -> {
        if(o1 instanceof Road && o2 instanceof City){
            return 1;
        }if(o1 instanceof City && o2 instanceof Road){
            return -1;
        }
        else if(o1 instanceof City && o2 instanceof City){
            return ((City)o1).compareTo((City)o2);
        }else
            return ((Road)o1).compareTo((Road)o2);
    }));
    //ArrayList<Geometry2D> roads = new ArrayList<>();

    Validator valid;

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
        if(roads.size() != 0) {
            for(Geometry2D g:roads){
                if(g  instanceof City){
                    return (City)g;
                }
            }
        }
        return null;
    }

    public boolean isIsolated(){
        if(getCity() != null){
            City city = getCity();
            return city.isolated;
        }
        return true;
    }

    Node add(Float rect,Road road) {
        Node ret = this;
        //add both cities
        if(this.containsb(road.start))
            ret = ret.add(ret, road.start);
        //System.err.println(ret.toString());
        //System.err.println(ret.contains(road.end));
        if(this.containsb(road.end))
            ret = ret.add(ret, road.end);
        //finally add the road
        if(road.intersects(ret))
            ret.addRoad(this, road);
        ret = valid.validate(ret);
        return ret;
    }

    Node addRoad(Float rect, Road road){
        if(road.intersects(this)){
            roads.add(road);
        }
        Node ret = this;
        ret = valid.validate(ret);
        return ret;
    }

    public boolean containsb(City c){
        int x = (int)c.x;
        int y = (int)c.y;
        return (x >= this.x&&
                y >= this.y&&
                x <= this.x + getWidth() &&
                y <= this.y + getHeight());
    }

    Node add(Float rect, City city){
        if(this.containsb(city)) {
            //System.out.println(this.toString());
            roads.add(city);
        }
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
        for (Geometry2D n:roads) {
            //System.err.println(n.toString());
            if(n instanceof City){
                String str = (isIsolated()) ? "isolatedCity":"city";
                Element sub = doc.createElement(str);
                sub.setAttribute("name", getCity().name);
                sub.setAttribute("color", getCity().color);
                sub.setAttribute("radius", String.valueOf(getCity().radius));
                sub.setAttribute("x", String.valueOf((int) getCity().getX()));
                sub.setAttribute("y", String.valueOf((int) getCity().getY()));
                black.appendChild(sub);
            }
            if(n instanceof Road) {
                Element sub = doc.createElement("road");
                sub.setAttribute("end", ((Road) n).end.name);
                sub.setAttribute("start", ((Road) n).start.name);
                black.appendChild(sub);
            }
        }
        return black;
    }

    public ArrayList<City> rangeCities(int x, int y, int radius) {
        ArrayList<City> citiesInRange = new ArrayList<>();
        if(this.getCity() == null){
            return citiesInRange;
        }
        if (this.getCity().distance(new Point2D.Float((float) x, (float) y)) <= radius) {
            citiesInRange.add(this.getCity());
        }
        return citiesInRange;
    }

    public ArrayList<Road> rangeRoads(int x, int y, int radius) {
        Rectangle2D.Float rect = new Rectangle2D.Float(x, y, 0, 0);
        ArrayList<Road> roadsInRange = new ArrayList<>();
        for(Geometry2D r:roads){
            if(r instanceof Road){
                //THis is disgusting
                if(Shape2DDistanceCalculator.distance((Road)r, rect) <= radius){
                    roadsInRange.add((Road)r);

                }
            }
        }
        return roadsInRange;
    }

    @Override
    PriorityQueue<Node> nearestCity(int x, int y) {
        PriorityQueue<Node> a = new PriorityQueue<>(new PriorityComparator(x,y));
        if(this.getCity() != null) {
            a.add(this);
            return a;
        }
        return null;
    }

    @Override
    PriorityQueue<Node> nearestRoad(int x, int y) {
        Rectangle2D.Float rect = new Rectangle2D.Float(x, y, 0, 0);
        PriorityQueue<Node> a = new PriorityQueue<>(new RoadPriorityComparator(x, y));
        for(Geometry2D g:roads){
            if(g instanceof Road){
                a.add(this);
                return a;
            }
        }
        return null;
    }

    public Road nearestRoadroad(int x, int y){
        Rectangle2D.Float rect = new Rectangle2D.Float(x, y, 0, 0);
        double dist = java.lang.Double.MAX_VALUE;
        Road r = null;
        for(Geometry2D g:roads){
            if (g instanceof Road){
                if(Shape2DDistanceCalculator.distance((Road)g, rect) < dist){
                    dist = Shape2DDistanceCalculator.distance((Road)g, rect);
                    r = (Road)g;
                }
            }
        }
        return r;
    }
    public float nearestroadDist(int x, int y){
        Rectangle2D.Float rect = new Rectangle2D.Float(x, y, 0, 0);
        double dist = java.lang.Double.MAX_VALUE;
        Road nearest = null;
        for(Geometry2D g:roads) {
            if (g instanceof Road) {
                if (dist > Shape2DDistanceCalculator.distance((Road) g, rect)){
                    dist = Shape2DDistanceCalculator.distance((Road) g, rect);
                }
            }
        }
        return (float)dist;
    }

    @Override
    void saveMap(CanvasPlus canvas) {
        for (Geometry2D g:roads) {
            switch (g.getType()){
                case 0:
                    City c = (City)g;
                    canvas.addPoint(c.name, c.x, c.y, Color.BLACK);
                    break;
                case 1:
                    Road r = (Road)g;
                    canvas.addLine(r.x1, r.y1, r.x2, r.y2, Color.BLACK);
            }
        }
    }

    public String str(){
        return "x: "+ x + " y: "+y +" width: "+ width +" height: " + height + " roads: " + roads.toString();
    }

}
