package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;

public class PRQuadTree {

    float height, width;
    Node root;
    Point2D.Float center;

    public PRQuadTree(int height, int width){
        root = WhiteNode.getInstance();
        center = new Point2D.Float(height/2, width/2);
        this.height = height; this.width = width;
    }

    //may need return type to do errors tbh
    //TODO check bounds and name/coord overlap
    public void addCity(City city) throws CityAlreadyMappedException, cityOutOfBoundsException {
        if (city.getX() > width || city.getX() < 0 || city.getY() > height || city.getY() < 0){
            throw new cityOutOfBoundsException("cityOutOfBounds");
        }
        //if the root is a white node
        if(root.equals(WhiteNode.getInstance())) {
            root = new GreyNode(height, width, center);
            ((GreyNode) root).addCity(city);
        }//TODO else if root is a greynode, iterate further
        else if (root instanceof GreyNode){
            ((GreyNode)root).addCity(city);
        }
    }

}
