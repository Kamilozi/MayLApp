package MyLittleSmt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class xmlFile 
{
	
	public HashMap<Integer, HashMap<String, String>> SimpleXMLImp(String FilePath, String tagName)
	{
		
		HashMap<Integer, HashMap<String, String>> MapOfMap  = XML (FilePath, tagName);
		return MapOfMap;
		
	}
	
	public HashMap<Integer, HashMap<String, String>> XML (String FilePath, String tagName)
	{
		HashMap<Integer, HashMap<String, String>> MapOfMap = new HashMap<Integer,HashMap<String, String>>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try 
		 {	
			 dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			 DocumentBuilder db = dbf.newDocumentBuilder();
			 Document doc = db.parse(new File(FilePath));
			 doc.getDocumentElement().normalize();
			 NodeList list = doc.getElementsByTagName(tagName);

			 for (int temp = 0; temp < list.getLength(); temp++) {
				 HashMap<String, String> map = new HashMap<String, String>();
				 Node node = list.item(temp);
				 NodeList nodes = (NodeList) list.item(temp);
				 for (int noo = 0 ; noo < nodes.getLength() ; noo++)
				 {		
					 Node notoes = nodes.item(noo);
					 map.put(nodes.item(noo).getNodeName(), notoes.getTextContent());
				 }
				 MapOfMap.put(temp, map);
			 }
		 }
		 catch (ParserConfigurationException | SAXException | IOException e) {
	          e.printStackTrace();
		 }
		
		return MapOfMap;
}
 
}
 
