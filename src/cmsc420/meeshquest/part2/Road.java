package cmsc420.meeshquest.part2;

import cmsc420.geom.Geometry2D;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Road extends Line2D.Float implements Geometry2D {
    City start, end;

    public Road(City start, City end){
        super.x1 = start.x;
        super.y1 = start.y;

        super.x2 = end.x;
        super.y2 = end.y;

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

    @Override
    public int getType() {
        return 1;
    }
}
