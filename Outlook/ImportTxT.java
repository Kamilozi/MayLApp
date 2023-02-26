package Outlook;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ImportTxT 
{
	static String filename = "C:/Users/kamil/Desktop/table.txt";
	public static void main (String[] args) throws IOException
	{
		impTXTtoArray dane = new impTXTtoArray(filename);
		
		ArrayList<String> result = dane.impfrofiles();
		System.out.println(result.get(0));
		ArrayList<ArrayList<String>> array = dane.getSplit();		
		System.out.print(array);
	     
	}

}

class impTXTtoArray 

{
	private String plik;
	private ArrayList<String> result;
	private ArrayList<ArrayList<String>> array;
	String[] lines;
	public impTXTtoArray(String plik)
	{
		this.plik=plik;
				
	}
	

	public ArrayList<String> impfrofiles () throws IOException
	{

		
		result = new ArrayList<>();

		try (FileReader f = new FileReader(plik)) {
		    StringBuffer sb = new StringBuffer();
		    while (f.ready()) {
		        char c = (char) f.read();
		        if (c == '\n') {
		            result.add(sb.toString());
		            sb = new StringBuffer();
		        } else {
		            sb.append(c);
		        }
		    }
		    if (sb.length() > 0) {
		        result.add(sb.toString());
		    }
		}       
		return result;
	}	
public void splitByNewLine(String dane)
{
	lines = dane.split(System.lineSeparator());	
}
	

public String[] getLines()
{
	return lines;
}

public ArrayList<ArrayList<String>> getSplit()
{
	
	array = new ArrayList<ArrayList<String>>();
	for (String valu : result)
		
		{
			String[] temp_valu = valu.split(";", -1);
			ArrayList<String> array_temp = new ArrayList<String>();
			for (String e : temp_valu)
			{
				array_temp.add(e);
			}
			array.add(array_temp);
		}
	///return array;
	return array;
}

public Object[] ArrayStringToObject(ArrayList<String> a)
{
	Object[] str = new Object[a.size()];
	for (int i = 0; i < a.size(); i++)
	{
		str[i] = a.get(i);
	}
	return str;
}
}
