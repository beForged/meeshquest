package cmsc420.meeshquest.part3;

public class PM3Quadtree extends PMQuadtree{

    Node root;

    public PM3Quadtree(int width, int height){
        super.width = width;
        super.height = height;
        PM3Validator v = new PM3Validator();
        WhiteNode.setValidator(v);
        root = WhiteNode.getInstance();
    }





}
