package cmsc420.meeshquest.part2;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Road extends Line2D.Float {
    Point2D.Float start, end;

    public Road(Point2D.Float start, Point2D.Float end){
        this.start = start;
        this.end = end;
    }

    public String[] roadInfo(){
        return new String[] {start.toString(), end.toString()};
    }

    public Point2D.Float getStart(){
        return start;
    }

    public Point2D.Float getEnd(){
        return end;
    }

}
