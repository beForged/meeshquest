package cmsc420.meeshquest.part2;

public interface Validator {

    Node validate(Node n, Road road);

    Node validate(Node n, City city);

    Node validateCity(Node n, City city);

    //Node validate(GreyNode g, Road road);

    //Node validate(GreyNode g, City city);


}
