package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;

public class PRQuadTree {

    int height, width;
    Node root;
    Point2D.Float center;

    public PRQuadTree(int height, int width){
        root = WhiteNode.getInstance();
        center = new Point2D.Float(height/2, width/2);
        this.height = height; this.width = width;
    }

    //may need return type to do errors tbh
    public void addCity(City city){
        //if the root is a white node
        if(root.equals(WhiteNode.getInstance())){
            root = new GreyNode();
            quadrant(city, root);
        }
    }


    /* we will return 0-3 in the
      0 | 1
      -----
      2 | 3
      form
     */
    private void quadrant(City city, Node root){
        //TODO add check to make sure root is a grey node
        //city is below x axis and to the left of y
       if(city.getX() < center.getX() && city.getY() < center.getY()){
           root.setSW(new BlackNode(city));
           //city is above x axis and left of y
       }else if(city.getX() > center.getX() && city.getY() < center.getY()){
           root.setNW(new BlackNode(city));
           //city is below x and  to the right of y
       }else if(city.getX() < center.getX() && city.getY() > center.getY()){
           root.setSE(new BlackNode(city));
           //city is over x and to the right of y
       }else if (city.getX() > center.getX() && city.getY() > center.getY()) {
           root.setNE(new BlackNode(city));
       }
    }
}
