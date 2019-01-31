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

        //this is the listcities command, needs to output the cities in the map
        if(node.getNodeName().equals("listCities")){

            //attribute of how to sort the thin
            String sortBy = node.getAttribute("sortBy");

            String[] params = {"sortBy"};
            String[] values = {sortBy};
            //both maps should either be not empty or empty: problems exist if one or the other is wrong
            if(map.nameMap.isEmpty() || map.coordinateMap.isEmpty()){
                return outputBuilder("noCitiesToList", "listCities", params, values, null);
            }else{
                //we need to list either sorted by coordinates or names
                if(sortBy.equals("name")){
                    //sort by name - we convert a keyset to an array to be sorted
                    String[] keys =  map.nameMap.keySet().toArray(new String[map.nameMap.size()]);
                    //sorts in ascending order, needs to be reversed
                    Arrays.sort(keys);
                    //we sort the keys so that we can get the cities in the correct order.
                    //does arrays.sort use the string.compareTo() - yes
                    ArrayList<City> ret = new ArrayList<>();
                    for(int i = 0; i < keys.length; i++){
                        //this is a sorted list of cities going down
                        ret.add(map.nameMap.get(keys[i]));
                    }
                    //a 2d array that contains each cities information in the cols and
                    //the cities are sorted asciibetically by row
                    String[][] a= new String[keys.length][];
                    for(int i = 0; i < keys.length; i ++){
                        a[i] = ret.get(i).cityInfo();
                    }
                    //just making city elements here
                    Element output = doc.createElement("cityList");
                    for(int i = 0; i < a.length; i ++){
                        Element city = doc.createElement("city");
                        city.setAttribute("name", a[i][0]);
                        city.setAttribute("x", String.valueOf((int) Float.parseFloat(a[i][1])));
                        city.setAttribute("y", String.valueOf((int)Float.parseFloat(a[i][2])));
                        city.setAttribute("radius", a[i][3]);
                        city.setAttribute("color", a[i][4]);
                        output.appendChild(city);
                    }

                    return outputBuilder(null, "listCities", params, values, output);

                }else{
                    //TODO sort by coordinates
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
            return outputBuilder(null, "clearALL", empty, empty, null);
        }
        //more inputs can go here
        if(node.getNodeName().equals("placeholder")){
            return null;
        }
        return null;
    }

    //we create add the city to the map and then build xml here to output?
    private Element createCity(String name, int x, int y, int radius, String color){
        //makes a new city and then inserts it, map checks if it is a dupe or not
        City city = new City(name, x, y, radius, color);
        String recv = map.addCity(city); //returns success or the error msg
        String[] values= {name, String.valueOf(x), String.valueOf(y), String.valueOf(radius), color};
        String[] params = {"name", "x", "y", "radius", "color"};

        //we want to return the success xml here so it can be appended to the document in main
        if(recv.equals("success")){
            return outputBuilder( null, "createCity", params, values, null);
        }else{
            return outputBuilder( recv, "createCity", params, values, null);
        }
    }


    /*
    since xml (probably) doesnt care if its an int or a string as long as it says the same thing
    we can just give a correctly ordered array of values and parameters and build  correct output
    IMPORTANT IF ERROR IS NULL THEN IT IS A SUCCESSFUL OPERATION
    TODO perhaps it might be needed to change params so that they behave like output where elements are made in methods
    */
    private Element outputBuilder( String error, String command, String[] params, String[] values, Element output){
        //creates the "root" of this success output either error or success
        Element ret;
        if(error == null){
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

        //output stuff, just makes an output tag and then appends if any output is given
        Element out = doc.createElement("output");
        ret.appendChild(out);
        if(output != null){
            out.appendChild(output);
        }
        return ret;
    }
}
