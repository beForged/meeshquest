package cmsc420.meeshquest.part1;

public class GreyNode implements Node{
    private Node[] quadrants;
    boolean isFull;

    //initialize with all white nodes as children (maybe ask for all 4 nodes?)
    public GreyNode(){
        quadrants = new Node[4];
        for(int i = 0; i < 4; i ++){
            quadrants[i] = WhiteNode.getInstance();
        }
    }

    public void setNode(int quadrant, Node node){
        //TODO if all of node consists of black or grey nodes then it is full
        quadrants[quadrant] = node;
    }

}
