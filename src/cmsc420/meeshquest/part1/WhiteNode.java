package cmsc420.meeshquest.part1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WhiteNode implements Node{
    //a little bit of communism
    private static WhiteNode ourInstance = new WhiteNode();

    public static WhiteNode getInstance() {
        return ourInstance;
    }

    @Override
    public boolean containsCity(String city) {
        return false;
    }

    //does nothing
    public void addCity(City city){
        return;
    }

    public Node deleteCity(String c){
        return this;
    }

    public Element printquadtree(Document doc){
        return doc.createElement("white");
    }

    private WhiteNode() {
    }
}
