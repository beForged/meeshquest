package cmsc420.meeshquest.part1;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmsc420.xml.XmlUtility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class commandParser {
    Element commandNode;
    Map map;
    Document doc;

    public commandParser(Map map, Document doc){
        this.doc = doc;
        this.map = map;
    }

    //given some node we get all of its attributes and then execute its task
    Element in(Element node){
        //create city
        if(node.getNodeName().equals("createCity")){
            String color, name = "";
            int x, y, radius;

            //for each attribute add(parameters) - by xsd all are required
            name = node.getAttribute("name");
            color = node.getAttribute("color");
            x = Integer.parseInt(node.getAttribute("x"));
            y = Integer.parseInt(node.getAttribute("y"));
            radius = Integer.parseInt(node.getAttribute("radius"));

            return createCity(name, x, y, radius, color);
        }

        if(node.getNodeName().equals("listCities")){
            String sortBy = node.getAttribute("sortBy");

            //both maps should either be not empty or empty: problems exist if one or the other is wrong
            if(map.nameMap.isEmpty() || map.coordinateMap.isEmpty()){
                String[] params = {"sortBy"};
                String[] values = {sortBy};
                return outputBuilder("noCitiesToList", "listCities", params, values);
            }else{
                //we need to list either sorted by coordinates or names
                if(sortBy.equals("name")){
                    //sort by name - we convert a keyset to an array to be sorted
                    String[] keys = (String[]) map.nameMap.keySet().toArray();
                    //sorts in ascending order, needs to be reversed
                    Arrays.sort(keys);
                    //we sort the keys so that we can get the cities in the correct order.
                    //does arrays.sort use the string.compareTo() - yes
                    ArrayList<City> ret = new ArrayList<>();
                    for(int i = keys.length; i > 0; i--){
                        //this is a sorted list of cities going down
                        ret.add(map.nameMap.get(keys[i]));
                    }
                    String[][] a= new String[keys.length][];

                    for(int i = 0; i < keys.length; i ++){
                        a[i] = ret.get(i).cityInfo();
                    }

                }else{
                    //sort by coord
                }
            }
        }

        /*
         *Resets all structures and clears them, cannot fail
         */
        if(node.getNodeName().equals("clearAll")){
            map.coordinateMap.clear();
            map.nameMap.clear();
            String[] empty = {};
            return outputBuilder(null, "clearALL", empty, empty);
        }
        //more inputs can go here
        if(node.getNodeName().equals("placeholder")){
            return null;
        }
        return null;
    }

    //we create add the city to the map and then build xml here to output?
    private Element createCity(String name, int x, int y, int radius, String color){
        City city = new City(name, x, y, radius, color);
        String recv = map.addCity(city);
        String[] values= {name, String.valueOf(x), String.valueOf(y), String.valueOf(radius), color};
        String[] params = {"name", "x", "y", "radius", "color"};

        //we want to return the success xml here so it can be appended to the document in main
        if(recv.equals("success")){
            return outputBuilder( null, "createCity", params, values);
        }else{
            return outputBuilder( recv, "createCity", params, values);
        }
    }

    /*
    since xml (probably) doesnt care if its an int or a string as long as it says the same thing
    we can just give a correctly ordered array of values and parameters and build  correct output
    IMPORTANT IF ERROR IS NULL THEN IT IS A SUCCESSFUL OPERATION
    TODO add output capability to output builder and adjust the other methods
    */
    private Element outputBuilder( String error, String command, String[] params, String[] values){
        //creates the "root" of this success output
        Element ret;
        if(error != null){
            ret = doc.createElement("success");
        }else{
            ret = doc.createElement("error");
            ret.setAttribute("type", error);
        }

        //creates the command
        Element comm = doc.createElement("command");
        comm.setAttribute("name", command);
        ret.appendChild(comm);

        //we make and list parameters here
        Element parameters = doc.createElement("parameters");
        ret.appendChild(parameters);//hopefully can still add things after parameters
        for(int i = 0; i < params.length; i ++){
            Element a = doc.createElement(params[i]);
            a.setAttribute("value", values[i]);
            parameters.appendChild(a);
        }
        return ret;
    }
}
