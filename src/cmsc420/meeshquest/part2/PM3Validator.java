package cmsc420.meeshquest.part2;

public class PM3Validator implements Validator{

    //default constructor

    public Node validate(Node n, Road road) {
        //can always add roads to it
        if(n instanceof BlackNode){
            //add road to list and return
            ((BlackNode)n).roads.add(road);
            return n;
        }else{
            return n.addRoad(n, road);
        }
    }

    public Node validate(Node n, City city, boolean isolated) {
        if(n instanceof BlackNode) {//
            if(((BlackNode)n).city.equals(city)){
                //todo trow city already mapped
                return n;
            }
            if (((BlackNode)n).city != null) {
                //partiion
                Node g = new GreyNode(n, this);
                g.add(((BlackNode)n).city);
                if(isolated) {
                    g.addCity(n, city);
                }else {
                    g.add(n, city);
                }
                return g;
            } else {//dont need to partition, no city so just add it
                ((BlackNode) n).city = city;
                return n;
            }
        }else {
            return n.add(n, city);
        }
    }
    public Node validate(Node n, City city){
        return validate(n, city, false);
    }

    @Override
    public Node validateCity(Node n, City city) {
        return validate(n, city, true);
    }
}
