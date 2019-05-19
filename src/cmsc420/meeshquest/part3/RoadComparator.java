package cmsc420.meeshquest.part3;

import java.util.Comparator;

public class RoadComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Road r1 = (Road)o1;
        Road r2 = (Road) o2;
        if(r1.start.name.compareTo(r2.start.name) == 0){
            return -r1.end.name.compareTo(r2.end.name);
        }else
            return -r1.start.name.compareTo(r2.start.name);
    }
}
