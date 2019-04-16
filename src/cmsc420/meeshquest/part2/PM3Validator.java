package cmsc420.meeshquest.part2;

public class PM3Validator implements Validator{

    //default constructor

    public Node validate(Node n) {
        //want to see if the node is valid else we partition
        if(valid(n)){

        }
        return null;
    }

    private boolean valid(BlackNode n){
        //for pm3 we just need to check if there is more than one city
        if(n.roads.first() instanceof City){

        }

    }
/*
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
                ((BlackNode) n).isIsolated= isolated;
                return n;
            }
        }else {
            return n.add(n, city);
        }
    }

    @Override
    public Node validateCity(Node n, City city) {
        return validate(n, city, true);
    }
    */
}
