package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class RoadPriorityComparator implements Comparator<Node> {
    int x, y;
    Point2D.Float ref;
    PriorityComparator comp;
    public RoadPriorityComparator(int x, int y){
        this.x = x;
        this.y = y;
        this.ref = new Point2D.Float(x, y);
        comp = new PriorityComparator(x, y);
    }
    @Override
    public int compare(Node o1, Node o2) {
        if(o1 instanceof BlackNode && o2 instanceof BlackNode){
            BlackNode b1 = (BlackNode) o1;
            BlackNode b2 = (BlackNode) o2;
            return (int) (b1.nearestroadDist(x, y) - b2.nearestroadDist(x, y));
        }else{
            return comp.compare(o1, o2);
        }
    }
}
