package cmsc420.meeshquest.part1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class GreyNode extends Node {
    private Node[] quadrants;
    boolean isFull;

    //initialize with all white nodes as children (maybe ask for all 4 nodes?)
    public GreyNode(float height, float width, float x, float y) {
        quadrants = new Node[4];
        for (int i = 0; i < 4; i++) {
            quadrants[i] = WhiteNode.getInstance();
        }
        super.height = height;
        super.width = width;
        super.x = x;
        super.y = y;
    }

    //what if the bucket size is >1 how to modify this so that its OK to have more than 1 black node
    public void addCity(City city) {
        int quadrant = quadrant(city);
        //if its a blacknode, we take the city as a tmp and then replace with grey node, and reinsert to that greynode
        if (quadrants[quadrant] instanceof BlackNode) {
            //this casting is ok because of the instance of we had above
            City temp = ((BlackNode) quadrants[quadrant]).getCity();
            //we create a new grey node
            Point2D.Float corner= newcenter(quadrant);
            quadrants[quadrant] = new GreyNode(height / 2, width / 2, corner.x, corner.y);
            //and then insert into it
            quadrants[quadrant].addCity(temp);
            quadrants[quadrant].addCity(city);
        } else if (quadrants[quadrant] instanceof WhiteNode) { //use .instaceof method instead?
            //otherwise it is a whitenode and we can replace with a blacknode
            quadrants[quadrant] = new BlackNode(city);
        } else {//it is a greynode and we recurse
            quadrants[quadrant].addCity(city);
        }

    }


    //finds the new bottom left of a quadrant for a new grey node
    private Point2D.Float newcenter(int quadrant) {
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

    /* we will return 0-3 in the
      0 | 1
      -----
      2 | 3
      form
     */
    private int quadrant(City city) {
        //city is below x axis and to the left of y
        float w = width / 2;
        float h = height / 2;
        if (city.getX() < w && city.getY() < h) {
            return 2;
            //city is above x axis and left of y
        } else if (city.getX() > w && city.getY() < h) {
            return 0;
            //city is below x and  to the right of y
        } else if (city.getX() < w && city.getY() > h) {
            return 3;
            //city is over x and to the right of y
        } else if (city.getX() > w && city.getY() > h) {
            return 1;
        }
        return -1;
    }

    public boolean containsCity(String city) {
        for (Node i : quadrants) {
            if (i.containsCity(city)) {
                return true;
            }
        }
        return false;
    }

    public Node deleteCity(String city) {
        int grey = 0;
        int black = 0;
        //replace the node with itself (if doesnt match) or with whitenode (returned from blknode)
        for (int i = 0; i < quadrants.length; i++) {
            Node n = quadrants[i].deleteCity(city);
            if (n instanceof GreyNode) {
                grey++;
            }
            if (n instanceof BlackNode) {
                black++;
            }
            quadrants[i] = n;
        }
        //if 1 blacknode and no grey nodes, return just black node
        if (grey == 0 && black <= 1) {
            for (Node i : quadrants) {
                if (i instanceof BlackNode) {
                    return i;
                }
            }
            return WhiteNode.getInstance();
        }
        return this;
    }

    //we create elements here and return them
    public Element printquadtree(Document doc) {
        Element out = doc.createElement("gray");
        out.setAttribute("x", String.valueOf((int) this.x));
        out.setAttribute("y", String.valueOf((int) this.x));
        for (Node i : quadrants) {
            out.appendChild(i.printquadtree(doc));
        }
        return out;
    }


    public ArrayList<City> rangeCities(int x, int y, int radius) {
        ArrayList<City> rangeCity = new ArrayList<>();
        for (Node i : quadrants) {
            rangeCity.addAll(i.rangeCities(x, y, radius));
        }
        return rangeCity;
    }

    @Override
    PriorityQueue<Node> nearestCity(int x, int y) {
        PriorityQueue<Node> nodes = new PriorityQueue<>();
        for (Node i:quadrants ) {
            if(i != null) {
                nodes.addAll(i.nearestCity(x, y));
            }
        }
        return nodes;
    }
}
