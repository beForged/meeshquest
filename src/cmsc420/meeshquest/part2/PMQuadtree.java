package cmsc420.meeshquest.part2;

import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public abstract class PMQuadtree {

    //height and width of the map
    public int width, height;

    //pmorder - maybe somehow make it final?
    public int order;

    //set of all roads in the quadtree
    TreeSet<Road> all;

    //number of isolated cities
    public int isolatedCities;

    //list of all cities and roads that start there
    //TODO maybe another one with road terminations?
    TreeMap<City, List<Road>> cities;

    //this is the root node and we set it to singleton white at first
    public Node root = WhiteNode.getInstance();

}
