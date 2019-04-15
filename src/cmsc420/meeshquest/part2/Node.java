package cmsc420.meeshquest.part2;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Geometry2D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.css.Rect;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public abstract class Node extends Rectangle2D.Float{

    abstract boolean containsCity(String city);


    //todo maybe add a default func that auto adds both cities and the road
    abstract Node add(Rectangle2D.Float rect, Road road);

    abstract Node add(Rectangle2D.Float rect, City city);

    //abstract Node add(Geometry2D geom);

    //this is for isolated cities
    abstract Node addCity(Rectangle2D.Float rect, City city);

    abstract Node addRoad(Rectangle2D.Float rect, Road road);
    //part 3 dont want to implement if no spec
    //abstract Node deleteCity(String city);
    Node delete(Road road){
        throw new UnsupportedOperationException();
    }
    Node delete(City city) {
        throw new UnsupportedOperationException();
    }

    /* we will return 0-3 in the
      0 | 1
      -----
      2 | 3
      form - inclusive on bottom and left and exclusive on top and right
     */
    LinkedList<Integer> quadrant(City city) {
        //city is below x axis and to the left of y
        LinkedList<Integer> quads = new LinkedList<>();
        float w = (x + (width / 2));
        float h = y + (height / 2);
        //System.err.println("width " + w + " height " + h);
        if (city.getX() <= w && city.getY() <= h) {
            quads.add(2);
        } if (city.getX() >= w && city.getY() <= h) {
            quads.add(3);
        } if (city.getX() <= w && city.getY() >= h) {
            quads.add(0);
        } if (city.getX() >= w && city.getY() >= h) {
            quads.add(1);
        }
        return quads;
    }

    //finds the new bottom left of a quadrant for a new grey node
    Point2D.Float newcenter(int quadrant) {
        switch (quadrant) {
            case 0:
                return new Point2D.Float(x , y + height / 2);
            case 1:
                return new Point2D.Float(x + width / 2, y + height / 2);
            case 2:
                return new Point2D.Float(x, y);
            case 3:
                return new Point2D.Float(x + width / 2, y );
        }
        return null;
    }

    Rectangle2D.Float getChildRect(Rectangle2D.Float parentSquare, int quadrant){
        float height = parentSquare.height/2;
        float width = parentSquare.width/2;
        Point2D.Float cent = newcenter(quadrant);
        return new Rectangle2D.Float(width, height, cent.x, cent.y );
    }
    // and some other stuff
    abstract Element printquadtree(Document doc);

    abstract ArrayList<City> rangeCities(int x, int y, int radius);

    abstract PriorityQueue<Node> nearestCity(int x, int y);

    abstract void saveMap(CanvasPlus canvas);

}
