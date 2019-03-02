package cmsc420.meeshquest.part1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.geom.Point2D;
import java.util.*;


public class commandParser {
    Element commandNode;
    Map map;
    Document doc;
    String[] empty = {};

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
            map.quadTree.clear();
            return outputBuilder(null, "clearAll", empty, empty, null);
        }



        /*
        --------------------------------------------------------------------------------------------------------------
        */

        if(node.getNodeName().equals("deleteCity")){
            //get the name to be deleted
            String cityName = node.getAttribute("name");
            //parameters and values
            String[] params = {"name"}; String[] values = {cityName};
            Element out = null;
            try {
                //delete the map
                City rem = map.deleteCity(cityName);
                if(rem != null){
                    out = doc.createElement("cityUnmapped");
                    out.setAttribute("name", rem.name);
                    out.setAttribute("x", String.valueOf((int)rem.x));
                    out.setAttribute("y", String.valueOf((int)rem.y));
                    out.setAttribute("color", rem.color);
                    out.setAttribute("radius", String.valueOf(rem.radius));
                }
            //return the output builder
            }catch (cityDoesNotExistException e){
                return outputBuilder(e.getMessage(), "deleteCity", params, values, null);
            }

            return outputBuilder(null, "deleteCity", params, values, out);
        }

        /*
        --------------------------------------------------------------------------------------------------------------
        */
        //city already mapped, name not in dict, city out of bounds
        String mapCity = "mapCity";
        if(node.getNodeName().equals(mapCity)){
            String name = node.getAttribute("name");
            String[] params = {"name"};
            String[] values = {name};
            try{
                City c = map.nameMap.get(name);
                if(c == null){
                    return outputBuilder("nameNotInDictionary", mapCity, params, values, null);
                }
                map.quadTree.addCity(c);
            }catch(CityAlreadyMappedException e){
                return outputBuilder(e.getMessage(), mapCity, params, values, null);
            }catch(cityOutOfBoundsException e) {
                return outputBuilder(e.getMessage(), mapCity, params, values, null);
            }
            return outputBuilder(null, mapCity, params, values, null);
        }

        /*
        --------------------------------------------------------------------------------------------------------------
        */

        //unmap the city, catches name not in dict and city not mapped, returns params name and no output
        String unmap = "unmapCity";
        if(node.getNodeName().equals(unmap)){
            String name = node.getAttribute("name");
            String[] params = {"name"};
            String[] values = {name};
            if(map.nameMap.get(name) == null){
                return outputBuilder("nameNotInDictionary", unmap, params, values, null);
            }
            if(!map.quadTree.containsCity(name)){
                return outputBuilder("cityNotMapped", unmap, params, values, null);
            }
            map.quadTree.deleteCity(name);//should be true tbh as if it doesnt contain it will already ret
            return outputBuilder(null, unmap, params, values, null);
        }


        /*
        --------------------------------------------------------------------------------------------------------------
        */

        if(node.getNodeName().equals("printPRQuadtree")){
            Element output = null;
            try {
                output = doc.createElement("quadtree");
                output.appendChild(map.quadTree.printquadtree(doc));
            }catch (mapisEmptyException e){
                return outputBuilder(e.getMessage(), "printPRQuadtree", empty, empty, null);
            }
            return outputBuilder(null, "printPRQuadtree", empty, empty, output);
        }

        /*
        --------------------------------------------------------------------------------------------------------------
        */

        String save = "saveMap";
        if(node.getNodeName().equals(save)){
            String name = node.getAttribute("name");

        }


        /*
        --------------------------------------------------------------------------------------------------------------
        */
        String range = "rangeCities";
        if(node.getNodeName().equals(range)){
            String x= node.getAttribute("x");
            String y= node.getAttribute("y");
            String radius= node.getAttribute("radius");
            String saveMap= node.getAttribute("saveMap");//TODO could be null still needs output maybe
            ArrayList<City> a;
            String[] params;
            String[] values;
            if(saveMap == null){
                params = new String[]{"x", "y", "radius"};
                values = new String[]{x, y, radius};
            }else {
                params = new String[]{"x", "y", "radius", "saveMap"};
                values = new String[]{x, y, radius,saveMap};
            }
            try {
                a = map.quadTree.rangeCities(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(radius));
            }catch (noCitiesExistInRangeException e){
                return outputBuilder(e.getMessage(), range, params, values, null);
            }

            Element output = doc.createElement("cityList");
            for (City i:a ) {
                Element city = doc.createElement("city");
                city.setAttribute("name", i.name);
                city.setAttribute("x", String.valueOf((int)i.x));
                city.setAttribute("y", String.valueOf((int)i.y));
                city.setAttribute("color", i.color);
                city.setAttribute("radius", String.valueOf(i.radius));
                output.appendChild(city);
            }
            return outputBuilder(null, range, params, values, output);
        }

        /*
        --------------------------------------------------------------------------------------------------------------
        */

        if(node.getNodeName().equals("nearestCity")){
            String x= node.getAttribute("x");
            String y= node.getAttribute("y");
            String[] params = {"x", "y"};
            String[] values = {x, y};
            City c;
            try {
                c = map.quadTree.nearestCity(Integer.parseInt(x), Integer.parseInt(y));
            }catch(mapisEmptyException e){
                return outputBuilder(e.getMessage(), "nearestCity", params, values, null);
            }
            Element out = doc.createElement("city");
            out.setAttribute("name", c.name);
            out.setAttribute("x", String.valueOf((int)c.x));
            out.setAttribute("y", String.valueOf((int)c.y));
            out.setAttribute("color", c.color);
            out.setAttribute("radius", String.valueOf(c.radius));
            return outputBuilder(null, "nearestCity", params, values, out);

        }

        //y6y tbh
        /*
        --------------------------------------------------------------------------------------------------------------
        */
        if(node.getNodeName().equals("placeholder")){
            return null;
        }

        //TODO throw an error unknown/malformed input - fatal error
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
