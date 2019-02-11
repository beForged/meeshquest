package cmsc420.meeshquest.part1;

public class WhiteNode implements Node{
    //a little bit of communism
    private static WhiteNode ourInstance = new WhiteNode();

    public static WhiteNode getInstance() {
        return ourInstance;
    }

    private WhiteNode() {
    }
}
