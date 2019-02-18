package cmsc420.meeshquest.part1;

import java.awt.*;
import java.awt.geom.Point2D;

public class GreyNode implements Node{
    private Node[] quadrants;
    boolean isFull;
    float height, width;
    Point2D.Float center;

    //initialize with all white nodes as children (maybe ask for all 4 nodes?)
    public GreyNode(float height, float width, Point2D.Float center){
        quadrants = new Node[4];
        for(int i = 0; i < 4; i ++){
            quadrants[i] = WhiteNode.getInstance();
        }
        this.height = height;
        this.width = width;
        this.center = center;
    }

//what if the bucket size is >1 how to modify this so that its OK to have more than 1 black node
    public void addCity(City city) throws  CityAlreadyMappedException{
        int quadrant = quadrant(city);
        //if its a blacknode, we take the city as a tmp and then replace with grey node, and reinsert to that greynode
        if( quadrants[quadrant] instanceof BlackNode){
            if(quadrants[quadrant].equals(city)){
                //city is mapped
                throw new CityAlreadyMappedException("CityAlreadyMapped");
            }
            //this casting is ok because of the instance of we had above
            City temp = ((BlackNode) quadrants[quadrant]).getCity();
            Point2D.Float newcenter = newcenter(quadrant);
            quadrants[quadrant] = new GreyNode(height/2, width/2, newcenter(quadrant));
            ((GreyNode) quadrants[quadrant]).addCity(temp);
            ((GreyNode) quadrants[quadrant]).addCity(city);
        }else if(quadrants[quadrant] instanceof WhiteNode){ //use .instaceof method instead?
            //otherwise it is a whitenode and we can replace with a blacknode
            quadrants[quadrant] = new BlackNode(city);
        } else {//it is a greynode and we recurse
            ((GreyNode) quadrants[quadrant]).addCity(city);
        }

    }




//finds the new center of a quadrant for a new grey node
    private Point2D.Float newcenter(int quadrant){
        switch (quadrant){
            case 0:
                return new Point2D.Float((float)center.getX() - (width/2), (float)center.getY() + (height/2));
            case 1:
                return new Point2D.Float((float)center.getX() + (width/2), (float)center.getY() + (height/2));
            case 2:
                return new Point2D.Float((float)center.getX() - (width/2), (float)center.getY() - (height/2));
            case 3:
                return new Point2D.Float((float)center.getX() + (width/2), (float)center.getY() - (height/2));
        }
        return null;
    }

    /* we will return 0-3 in the
      0 | 1
      -----
      2 | 3
      form
     */
    private int quadrant(City city){
        //city is below x axis and to the left of y
        if(city.getX() < center.getX() && city.getY() < center.getY()){
            return 2;
            //city is above x axis and left of y
        }else if(city.getX() > center.getX() && city.getY() < center.getY()){
            return 0;
            //city is below x and  to the right of y
        }else if(city.getX() < center.getX() && city.getY() > center.getY()){
            return 3;
            //city is over x and to the right of y
        }else if (city.getX() > center.getX() && city.getY() > center.getY()) {
            return 1;
        }
        return -1;
    }

}
