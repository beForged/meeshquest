package cmsc420.meeshquest.part3;

public class PM1Quadtree extends PMQuadtree{


    Node root;

    public PM1Quadtree(int width, int height, int pmorder){
        super.width = width;
        super.height = height;
        PM1Validator v = new PM1Validator();
        WhiteNode.setValidator(v);
        root = WhiteNode.getInstance();
    }

    //todo maybe add return for info?
    public void add(Road road){
        root.add(root, road);
    }




}
