package cmsc420.meeshquest.part1;

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
    public int compare(Node o1, Node o2) {
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
