package cmsc420.meeshquest.part1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.geom.Point2D;
import java.util.*;


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


        /*
        --------------------------------------------------------------------------------------------------------------
         */

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
                //just making city elements here
                Element output = doc.createElement("cityList");
                if(sortBy.equals("name")){
                    //goes through the natural ordering of the treemap
                    for(City c:map.nameMap.values()){
                        Element city = doc.createElement("city");
                        city.setAttribute("name", c.name);
                        city.setAttribute("x", String.valueOf((int)c.x));
                        city.setAttribute("y", String.valueOf((int)c.y));
                        city.setAttribute("radius", String.valueOf(c.radius));
                        city.setAttribute("color", c.color);
                        output.appendChild(city);
                    }
                    return outputBuilder(null, "listCities", params, values, output);
                }else{
                    for(City c:map.coordinateMap.values()){
                        Element city = doc.createElement("city");
                        city.setAttribute("name", c.name);
                        city.setAttribute("x", String.valueOf((int)c.x));
                        city.setAttribute("y", String.valueOf((int)c.y));
                        city.setAttribute("radius", String.valueOf(c.radius));
                        city.setAttribute("color", c.color);
                        output.appendChild(city);
                    }
                    return outputBuilder(null, "listCities", params, values, output);
                }
            }
        }
        /*
        --------------------------------------------------------------------------------------------------------------
        */

        /*
         *Resets all structures and clears them, cannot fail
         */
        if(node.getNodeName().equals("clearAll")){
            map.coordinateMap.clear();
            map.nameMap.clear();
            String[] empty = {};
            return outputBuilder(null, "clearAll", empty, empty, null);
        }



        /*
        --------------------------------------------------------------------------------------------------------------
        */

        if(node.getNodeName().equals("deleteCity")){
            //TODO remove the city from the quadtree and create output stuff
            //get the name to be deleted
            String cityName = node.getAttribute("name");
            //delete the map TODO figure out how to tell if it was mapped or not
            String succ = map.deleteCity(cityName);
            //parameters and values
            String[] params = {"name"}; String[] values = {cityName};
            //return the output builder

            return outputBuilder(succ, "deleteCity", params, values, null);
        }

        /*
        --------------------------------------------------------------------------------------------------------------
        */

        if(node.getNodeName().equals("mapCity")){
            String name = node.getAttribute("name");
            //TODO add city to the map 3 diff errors possible
            return null;
        }

        /*
        --------------------------------------------------------------------------------------------------------------
        */

        //more inputs can go here
        if(node.getNodeName().equals("placeholder")){
            return null;
        }
        //TODO throw an error unknown/malformed input
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
    need to give it null if success else the error msg, the command that is given, list of parameter and the values
    that were given and then build the output element
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
        ret.appendChild(parameters);
        for(int i = 0; i < params.length; i ++){
            Element a = doc.createElement(params[i]);
            a.setAttribute("value", values[i]);
            parameters.appendChild(a);
        }

        //output stuff, just makes an output tag and then appends if any output is given
        if(error == null){
            Element out = doc.createElement("output");
            ret.appendChild(out);
            if(output != null){
                out.appendChild(output);
            }
        }
        return ret;
    }
}
