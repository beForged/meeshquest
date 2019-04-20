package cmsc420.meeshquest.part2;

import cmsc420.geom.Geometry2D;

public class PM3Validator implements Validator{

    //default constructor

    public Node validate(Node n) {
        //want to see if the node is valid else we partition
        if(!valid(n)) {
            //n has to be a blacknode in this case
            GreyNode g = new GreyNode(n, this);
            for (Geometry2D c : ((BlackNode) n).roads){
                if (c instanceof City)
                    g.add(g, (City) c);
                else
                    g.add(g, (Road) c);
            }
            return g;
        }//it is valid so we return it
        return n;
    }

    private boolean valid(Node n){
        //for pm3 we just need to check if there is more than one city
        if(n instanceof BlackNode) {
            int citycount = 0;
            for (Geometry2D g : ((BlackNode) n).roads) {
                if (g instanceof City) {
                    citycount++;
                }
            }
            return !(citycount > 1);
        }else
            return true;

    }

}
