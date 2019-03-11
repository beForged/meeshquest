package cmsc420.meeshquest.part2;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmsc420.xml.XmlUtility;

public class MeeshQuest {

    public static void main(String[] args) {
        //new map holds all the cities
        Map map;

        //this is the result xml that will be output
        Document results = null;


        try {

            //this is the results document that gets printed as output
            results = XmlUtility.getDocumentBuilder().newDocument();

            //Document doc = XmlUtility.validateNoNamespace(new File("testing/test8.xml"));
            Document doc = XmlUtility.validateNoNamespace(System.in);


            //we generate a root element here to start the output as it is on every output (iirc)
            Element root = results.createElement("results");
            results.appendChild(root);

            //this is the root node so we get height and width from that
            Element commandNode = doc.getDocumentElement();
            int spatialWidth = Integer.parseInt(commandNode.getAttribute("spatialWidth"));
            int spatialHeight = Integer.parseInt(commandNode.getAttribute("spatialHeight"));
            //creates the data structures
            map = new Map(spatialWidth, spatialHeight);

            //starts the command parser
            //this class takes in elements and executes the commands
            commandParser parser = new commandParser(map, results);

            final NodeList nl = commandNode.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Document.ELEMENT_NODE) {
                    commandNode = (Element) nl.item(i);

                    //System.out.println(map.coordinateMap.toString());
                    //System.out.println("--------------");
                    //we want to take the element here
                    Element add = parser.in(commandNode);
                    root.appendChild(add);

                }
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
//maybe need to clear the dom tree first
                Element root = results.createElement("fatalError");
                while (results.hasChildNodes()) {
                    results.removeChild(root.getFirstChild());
                }
                results.appendChild(root);
                e.printStackTrace();

        } finally {
            try {
                XmlUtility.print(results);

            } catch (TransformerException e) {
                //this is an undefined error
                e.printStackTrace();
            }
        }
    }
}
