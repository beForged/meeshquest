package cmsc420.meeshquest.part2;

import cmsc420.geom.Shape2DDistanceCalculator;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class PriorityComparator implements Comparator<Node>{
    int x,y;
    Point2D.Float ref;
    public PriorityComparator(int x, int y){
        this.x = x;
        this.y = y;
        this.ref = new Point2D.Float(x,y);
    }

    @Override
    public int compare(Node o1, Node o2){
        if(o1 instanceof BlackNode && o2 instanceof BlackNode) {
            int dist1 = (int) ((BlackNode) o1).getCity().distance(ref);
            int dist2 = (int) ((BlackNode) o2).getCity().distance(ref);
            return dist1 - dist2;
        }else if(o1 instanceof BlackNode){
            int cityDist = (int) ((BlackNode) o1).getCity().distance(ref);
            int nodeDist = (int) Shape2DDistanceCalculator.distance(ref, o2);
            return cityDist - nodeDist;
        }else if(o2 instanceof BlackNode){
            int cityDist = (int) ((BlackNode) o2).getCity().distance(ref);
            int nodeDist = (int) Shape2DDistanceCalculator.distance(ref, o1);
            return cityDist - nodeDist;
        }
        return comp(o1, o2);
    }

    /**
     * This compares the distance from point x,y to each node square so is just node distance
     * good for greys, not for blacks
     * @param o1 node 1
     * @param o2 node 2
     * @return
     */
    public int comp(Node o1, Node o2) {
        if(Shape2DDistanceCalculator.distance(ref, o1) < Shape2DDistanceCalculator.distance(ref, o2)){
            return -1;
        }else if(Shape2DDistanceCalculator.distance(ref, o1) > Shape2DDistanceCalculator.distance(ref, o2)){
            return 1;
        }else{
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
