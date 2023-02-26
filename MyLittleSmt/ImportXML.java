package MyLittleSmt;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

public class ImportXML {
	private static String FilePath = "C:\\Users\\kamil\\OneDrive\\THM_GROUP\\Projekty\\JPK_VAT\\JPK_V7M_202110.xml";
	public static void main(String[] args)
	{
		
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		
		var XMLImp = new XMLImp(FilePath, "tns:SprzedazWiersz");
		Map<Integer, Map<String, String>> map = XMLImp.getFilePath();
		ArrayList<ArrayList<Object>> sell = new ArrayList<ArrayList<Object>>();
		
		for (int temp=0  ; temp< map.size(); temp++)
		{	
			ArrayList<Object> tempArray = new ArrayList<Object>();
			tempArray.add(map.get(temp).get("tns:LpSprzedazy"));
			tempArray.add(map.get(temp).get("tns:KodKrajuNadaniaTIN"));
			tempArray.add(map.get(temp).get("tns:NrKontrahenta"));
			tempArray.add(map.get(temp).get("tns:NazwaKontrahenta"));
			tempArray.add(map.get(temp).get("tns:DowodSprzedazy"));
			tempArray.add(map.get(temp).get("tns:DataWystawienia"));
			tempArray.add(map.get(temp).get("tns:K_19"));
			tempArray.add(map.get(temp).get("tns:K_20"));
			tempArray.add(Float.parseFloat(map.get(temp).get("tns:K_20")) + Float.parseFloat(map.get(temp).get("tns:K_19")));
			sell.add(tempArray);
		}
		XMLImp = new XMLImp(FilePath, "tns:SprzedazCtrl");
		Map<Integer, Map<String, String>> sellCheck = XMLImp.getFilePath();
		float SumSell = 0;
		for (int temp=0; temp<sell.size(); temp++)
		{
			String SellTemp = sell.get(temp).get(sell.get(temp).size()-2).toString();
			SumSell = SumSell + Float.parseFloat(SellTemp);
		}
		System.out.print(Float.parseFloat(df.format(SumSell).replace(',', '.')));
		System.out.print(Float.parseFloat(sellCheck.get(0).get("tns:PodatekNalezny").toString()));
		if (Float.parseFloat(sellCheck.get(0).get("tns:PodatekNalezny").toString())==Float.parseFloat(df.format(SumSell).replace(',', '.')))
		{
			System.out.print("Mamy zgodnosæ");
		}
		else
		{
			System.out.print("B³¹d na podatku nale¿nym");	
		}
		
		if (Integer.parseInt(sellCheck.get(0).get("tns:LiczbaWierszySprzedazy").toString())==sell.size())
		{
			System.out.print("Mamy zgodnosæ");
		}
		else
		{
			System.out.print("B³¹d na iloœc wierszy sprzedazy");
		}
			

		XMLImp = new XMLImp(FilePath, "tns:ZakupWiersz");
		map = XMLImp.getFilePath();
		ArrayList<ArrayList<Object>> buy = new ArrayList<ArrayList<Object>>();
		for (int temp=0; temp<map.size(); temp++)
		{
			ArrayList<Object> tempArray = new ArrayList<Object>();
			tempArray.add(map.get(temp).get("tns:LpZakupu"));
			tempArray.add(map.get(temp).get("tns:KodKrajuNadaniaTIN"));
			tempArray.add(map.get(temp).get("tns:NrDostawcy"));
			tempArray.add(map.get(temp).get("tns:NazwaDostawcy"));
			tempArray.add(map.get(temp).get("tns:DowodZakupu"));
			tempArray.add(map.get(temp).get("tns:DataZakupu"));
			tempArray.add(map.get(temp).get("tns:K_42"));
			tempArray.add(map.get(temp).get("tns:K_43"));
			tempArray.add(Float.parseFloat(map.get(temp).get("tns:K_42")) + Float.parseFloat(map.get(temp).get("tns:K_43")));
			buy.add(tempArray);
		}
		for (int temp=0; temp<map.size(); temp++)
		{

		}
		XMLImp = new XMLImp(FilePath, "tns:ZakupCtrl");
		Map<Integer, Map<String, String>> buyCheck = XMLImp.getFilePath();
		float SumBuy = 0;
		for (int temp=0; temp<buy.size(); temp++)
		{
			String SumBuyTemp = buy.get(temp).get(buy.get(temp).size()-2).toString();
			SumBuy = SumBuy + Float.parseFloat(SumBuyTemp);
		}	


		if (Float.parseFloat(buyCheck.get(0).get("tns:PodatekNaliczony"))==Float.parseFloat(df.format(SumBuy).replace(',', '.')))
		{
			System.out.print("Mamy zgodnoœæ");
		}
		else
		{
			System.out.print("Brak zgodnoœci");
		}
		
		if (Integer.parseInt(buyCheck.get(0).get("tns:LiczbaWierszyZakupow"))==map.size())
		{
			System.out.print("Mamy zgodnoœæ");
		}
		else
		{
			System.out.print("Brak zgodnoœci");
		}
	}

}

class XMLImp
{
	private String FilePath, aFilePath;
	private String tagName, atagName;
	Map<Integer, Map<String, String>> MapOfMap = new HashMap<Integer, Map<String, String>>();
	public XMLImp(String aFilePath,String atagName)
	{
		FilePath = aFilePath;
		tagName = atagName;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		 try 
		 {	
			 
			 
			 dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			 DocumentBuilder db = dbf.newDocumentBuilder();
			 Document doc = db.parse(new File(FilePath));
			 doc.getDocumentElement().normalize();
			 NodeList list = doc.getElementsByTagName(tagName);

			 for (int temp = 0; temp < list.getLength(); temp++) {
				 Map<String, String> map = new HashMap<String, String>();
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
	
	}
	public Map<Integer, Map<String, String>> getFilePath()
	{
		return MapOfMap;
	}
	
	
}