package cmsc420.meeshquest.part1;

public class GreyNode implements Node{
    private Node NE, NW, SE, SW;
    boolean isFull;

    //initialize with all white nodes as children (maybe ask for all 4 nodes?)
    public GreyNode(){
        NE = WhiteNode.getInstance();
        NW = WhiteNode.getInstance();
        SE = WhiteNode.getInstance();
        SW = WhiteNode.getInstance();
    }

    public void setNE(Node NE) {
        this.NE = NE;
    }

    public void setNW(Node NW) {
        this.NW = NW;
    }

    public void setSE(Node SE) {
        this.SE = SE;
    }

    public void setSW(Node SW) {
        this.SW = SW;
    }

    public Node getNE() {
        return NE;
    }

    public Node getNW() {
        return NW;
    }

    public Node getSE() {
        return SE;
    }

    public Node getSW() {
        return SW;
    }
}
