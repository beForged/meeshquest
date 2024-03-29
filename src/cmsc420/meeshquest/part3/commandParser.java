package cmsc420.meeshquest.part3;

import cmsc420.sortedmap.Treap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.geom.Arc2D;
import java.util.*;
import java.util.stream.Collectors;


public class commandParser {
    Element commandNode;
    Map map;
    Document doc;
    String[] empty = {};
    String id;

    public commandParser(Map map, Document doc){
        this.doc = doc;
        this.map = map;
    }

    //given some node we get all of its attributes and then execute its task
    Element in(Element node){
        id = node.getAttribute("id");

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
            map.treapMap.clear();
            return outputBuilder(null, "clearAll", empty, empty, null);
        }



        /*
        --------------------------------------------------------------------------------------------------------------
        */

        if(node.getNodeName().equals("printTreap")){
            //todo empty treap
            Treap temp = map.treapMap;
            Element treap = doc.createElement("treap");
            treap.setAttribute("cardinality", String.valueOf(((Treap) temp).size()));
            try {
                treap.appendChild(temp.printTreap(doc));
            } catch (GenericException e) {
                return outputBuilder(e.getMessage(), "printTreap", empty, empty, null);
            }
            return outputBuilder(null, "printTreap", empty, empty, treap);
        }

        /*
        --------------------------------------------------------------------------------------------------------------
        */

        if(node.getNodeName().equals("mapRoad")) {
            String start = node.getAttribute("start");
            String end = node.getAttribute("end");
            String[] params = {"start", "end"};
            String[] values = {start, end};
            try {
                if(!map.nameMap.containsKey(start)){ throw new GenericException("startPointDoesNotExist"); }
                if(!map.nameMap.containsKey(end))
                    throw new GenericException("endPointDoesNotExist");

                Road r = new Road(map.nameMap.get(start), map.nameMap.get(end));
                map.addRoad(r);
            }catch (GenericException e){
                return outputBuilder(e.getMessage(), "mapRoad", params, values, null);
            }
            Element out = doc.createElement("roadCreated");
            out.setAttribute("start", start);
            out.setAttribute("end", end);
            return outputBuilder(null, "mapRoad", params, values, out);
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
        //maps an isolated city
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
                map.nameMap.remove(name);
                c.setIsolated(true);
                //reput to make sure city is isolated (this overwrites)
                map.nameMap.put(name, c);
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
            //todo
            //map.quadTree.deleteCity(name);//should be true tbh as if it doesnt contain it will already ret
            return outputBuilder(null, unmap, params, values, null);
        }


        /*
        --------------------------------------------------------------------------------------------------------------
        */

        if(node.getNodeName().equals("printPMQuadtree")){
            Element output = null;
            try {
                output = doc.createElement("quadtree");
                output.setAttribute("order", String.valueOf(map.order));
                output.appendChild(map.quadTree.printquadtree(doc));
            }catch (mapisEmptyException e){
                return outputBuilder(e.getMessage(), "printPMQuadtree", empty, empty, null);
            }
            return outputBuilder(null, "printPMQuadtree", empty, empty, output);
        }

        /*
        --------------------------------------------------------------------------------------------------------------
        */

        String save = "saveMap";
        if(node.getNodeName().equals(save)){
            String name = node.getAttribute("name");
            map.quadTree.saveMap(name, -1, -1, -1);
            String[]params = {"name"};
            String[] values = {name};
            return outputBuilder(null, save, params, values,null);
        }


        /*
        --------------------------------------------------------------------------------------------------------------
        */
        String range = "rangeCities";
        if(node.getNodeName().equals(range)){
            String x= node.getAttribute("x");
            String y= node.getAttribute("y");
            String radius= node.getAttribute("radius");
            String saveMap= node.getAttribute("saveMap");
            ArrayList<City> a;
            String[] params;
            String[] values;
            if(saveMap.equals("") ){
                params = new String[]{"x", "y", "radius"};
                values = new String[]{x, y, radius};
            }else {
                params = new String[]{"x", "y", "radius", "saveMap"};
                values = new String[]{x, y, radius,saveMap};
                map.quadTree.saveMap(saveMap,Integer.parseInt(x),Integer.parseInt(y),Integer.parseInt(radius));
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
                c = map.quadTree.nearestCity(Integer.parseInt(x), Integer.parseInt(y), false);
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

        /*
        --------------------------------------------------------------------------------------------------------------
        */

        if (node.getNodeName().equals("nearestIsolatedCity")){
            String x= node.getAttribute("x");
            String y= node.getAttribute("y");
            String[] params = {"x", "y"};
            String[] values = {x, y};
            City c;
            try {
                c = map.quadTree.nearestCity(Integer.parseInt(x), Integer.parseInt(y), true);
            }catch(mapisEmptyException e){
                return outputBuilder(e.getMessage(), "nearestIsolatedCity", params, values, null);
            }
            Element out = doc.createElement("isolatedCity");
            out.setAttribute("name", c.name);
            out.setAttribute("x", String.valueOf((int)c.x));
            out.setAttribute("y", String.valueOf((int)c.y));
            out.setAttribute("color", c.color);
            out.setAttribute("radius", String.valueOf(c.radius));
            return outputBuilder(null, "nearestIsolatedCity", params, values, out);
        }
        /*
        --------------------------------------------------------------------------------------------------------------
        */

        if(node.getNodeName().equals("rangeRoads")) {
            String rangeroad = "rangeRoads";
            String x = node.getAttribute("x");
            String y = node.getAttribute("y");
            String radius = node.getAttribute("radius");
            String saveMap = node.getAttribute("saveMap");
            ArrayList<Road> a;
            String[] params;
            String[] values;
            if(saveMap.equals("") ){
                params = new String[]{"x", "y", "radius"};
                values = new String[]{x, y, radius};
            }else {
                params = new String[]{"x", "y", "radius", "saveMap"};
                values = new String[]{x, y, radius,saveMap};
                map.quadTree.saveMap(saveMap,Integer.parseInt(x),Integer.parseInt(y),Integer.parseInt(radius));
            }
            List<Road> b;
            try {
                a = map.quadTree.rangeRoads(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(radius));
                b = a.stream().distinct().collect(Collectors.toList());
                Collections.sort(b,new RoadComparator());
            }catch (noCitiesExistInRangeException e){
                return outputBuilder(e.getMessage(), rangeroad, params, values, null);
            }
            Element output = doc.createElement("roadList");
            for (Road i:b ) {
                Element city = doc.createElement("road");
                city.setAttribute("start", i.start.name);
                city.setAttribute("end", i.end.name);
                output.appendChild(city);
            }
            return outputBuilder(null, rangeroad, params, values, output);
        }

        /*
        --------------------------------------------------------------------------------------------------------------
        */
        if(node.getNodeName().equals("nearestRoad")){
            String x = node.getAttribute("x");
            String y = node.getAttribute("y");
            String params[] = {"x", "y"};
            String values [] = {x, y};
            Road road;
            try {
                road = map.quadTree.nearestRoad(Integer.parseInt(x), Integer.parseInt(y));
            }catch (mapisEmptyException e) {
                return outputBuilder(e.getMessage(), "nearestRoad", params, values, null);
            }
            Element output = doc.createElement("road");
            output.setAttribute("start", road.start.name);
            output.setAttribute("end", road.end.name);
            return outputBuilder(null, "nearestRoad", params, values, output);
        }

        /*
        --------------------------------------------------------------------------------------------------------------
        */
        if(node.getNodeName().equals("nearestCityToRoad")){

            //todo
            return null;
        }
        /*
        --------------------------------------------------------------------------------------------------------------
        */
        if(node.getNodeName().equals("shortestPath")){
            String start = node.getAttribute("start");
            String end = node.getAttribute("end");
            String saveMap = node.getAttribute("saveMap");
            String saveHtml= node.getAttribute("saveHTML");
            ArrayList<City> path;
            String params[] = {"start", "end"};
            String[] values = {start, end};
            try {
                path = map.getShortest(start, end);
            } catch (GenericException e) {
                return outputBuilder(e.getMessage(), "shortestPath", params, values, null);
            }
            City last = path.get(path.size()-1);
            Element output = doc.createElement("path");
            output.setAttribute("hops", String.valueOf(path.size() - 1));
            output.setAttribute("length", String.valueOf(last.distance));
            for (int i = 0; i < path.size() - 1; i++) {
                Element road = doc.createElement("road");
                City one = path.get(i);
                City two = path.get(i + 1);
                road.setAttribute("start", one.name);
                road.setAttribute("end", two.name);
                output.appendChild(road);
                if(i + 2 < path.size()){
                    City three = path.get(i + 2);
                    Arc2D.Double arc = new Arc2D.Double();
                    arc.setArcByTangent(one, two, three, 1);
                    double angle = arc.getAngleExtent();
                    //System.err.println(angle);
                    if(angle <= 45){
                    output.appendChild(doc.createElement("right"));
                    }else if(angle <= 135){
                        output.appendChild(doc.createElement("straight"));
                    }else{
                        output.appendChild(doc.createElement("left"));
                    }
                }
            }
            return outputBuilder(null, "shortestPath", params, values, output);
        }
        //y6y tbh
        /*
        --------------------------------------------------------------------------------------------------------------
        */
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
        if(!id.equals("")) { comm.setAttribute("id", id); }
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
