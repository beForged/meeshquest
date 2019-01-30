package cmsc420.meeshquest.part1;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
    	Map map = new Map();

    	//this is the result xml that will be output
        Document results = null;


        try {
        	Document doc = XmlUtility.validateNoNamespace(new File("test.xml"));
        	//Document doc = XmlUtility.validateNoNamespace(System.in);

			//this is the results document that gets printed as output
        	results = XmlUtility.getDocumentBuilder().newDocument();


			//this class takes in elements and executes the commands
			commandParser parser = new commandParser(map, results);

			//we generate a root element here to start the output as it is on every output (iirc)
			Element root = results.createElement("results");
			results.appendChild(root);
        	Element commandNode = doc.getDocumentElement();

        	final NodeList nl = commandNode.getChildNodes();
        	for (int i = 0; i < nl.getLength(); i++) {
        		if (nl.item(i).getNodeType() == Document.ELEMENT_NODE) {
        			commandNode = (Element) nl.item(i);

        			//we want to take the element here
					Element add = parser.in(commandNode);
					root.appendChild(add);

        		}
        	}
        } catch (SAXException | IOException | ParserConfigurationException e) {

        	//temp placeholder
            System.out.println("<fatalError/>");

		} finally {
            try {
				XmlUtility.print(results);
			} catch (TransformerException e) {
				e.printStackTrace();
			}
        }
    }
}
