package cmsc420.meeshquest.part2;

import cmsc420.geom.Geometry2D;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Road extends Line2D.Float implements Geometry2D, Comparable{
    City start, end;

    public Road(City start, City end){
        super.x1 = start.x;
        super.y1 = start.y;

        super.x2 = end.x;
        super.y2 = end.y;

        String e= end.name;
        String s= start.name;
        if(e.compareTo(s) < 0){
            City temp = start;
            start = end;
            end = temp;
        }
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

    public int compareTo(Object road){
        if(road instanceof Road) {
            Road r = (Road)road;
            if (r.start.name.equals(this.start.name) && r.end.name.equals(this.end.name)) {
                return 0;
            } else if(r.start.name.compareTo(this.start.name) == 0){
                return r.end.name.compareTo(this.end.name);
            }
                return r.start.name.compareTo(this.start.name);
        }
        return -1;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Road){
            if(((Road) o).compareTo(this) == 0){
                return true;
            }
        }
        return false;
    }

}
