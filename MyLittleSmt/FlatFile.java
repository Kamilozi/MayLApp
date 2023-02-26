package MyLittleSmt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFileChooser;

public class FlatFile {
private ArrayList<String> StringArray;
private String str;	


public void JTableIntoCSV(Object[][] listToCSV)
{
	String File = getNewFileNameCSV();
	ObjectToCSV(File, listToCSV);
	
}
///Krok 0. wskazywanie miejsca zapisu i nazwy
private String getNewFileNameCSV()
{
	JFileChooser fchoose  = new JFileChooser();
	int option = fchoose.showSaveDialog(null);
	String file=null;
	if(option == JFileChooser.APPROVE_OPTION)
	{
		String name = fchoose.getSelectedFile().getName(); 
		String path = fchoose.getSelectedFile().getParentFile().getPath();
		file= path + "\\" + name + ".csv";
	}
	
	return file;
}
///Krok 1.Tworzenie pliku 
public void ObjectToCSV(String File, Object[][] list) 
	
{
    File csvOutputFile = new File(File);
    try {
		try (PrintWriter writer = new PrintWriter(csvOutputFile)) {
			StringBuilder sb = new StringBuilder();
			for (int i=0;i<list.length;i++)
			{
				for (int j=0; j<list[i].length;j++)
					{
					sb.append(escapeSpecialCharacters(list[i][j].toString()));
						if (j!=list[i].length-1)
						{sb.append(";");}
					}
				sb.append("\n");
			}
			writer.write(sb.toString());
		}
		
	} catch (FileNotFoundException e) 
    {
		e.printStackTrace();
	} 
}

//Krok 2.usuwanie znaków specjalnych
public String escapeSpecialCharacters(String data) {
    String escapedData = data.replaceAll("\\R", " ");
    if (data.contains(";") || data.contains("\"") || data.contains("'")) {
        data = data.replace("\"", "\"\"");
        escapedData = "\"" + data + "\"";
    }
    return escapedData;
} 
//
//
//
//
//
//
//Import p³askiego pliku do ArrayList<ArrayList<String>>
@SuppressWarnings("resource")
public ArrayList<ArrayList<String>> ImportFlatFile(boolean ifUTF8, String nameFile, String spliter)
{
ArrayList<ArrayList<String>> templistlist = new ArrayList<ArrayList<String>>();
try {
	//FileInputStream fis = new FileInputStream(nameFile);
	//InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
	//BufferedReader reader = new BufferedReader(new FileReader(nameFile, StandardCharsets.UTF_8));
	//Path path = Paths.get(nameFile);
	//String s = Files.readString(path, StandardCharsets.UTF_8);
	BufferedReader reader = null;
	if (ifUTF8==true)
	{
		reader = new BufferedReader(new FileReader(nameFile, StandardCharsets.UTF_8));
	}else
	{   boolean waitforfile = false; 
		Instant later = Instant.now().plusSeconds(5) ;
		do{
			waitforfile = true; 
			if (later.isBefore(Instant.now()))
			{
				waitforfile=false; 
			}else
			{
				 try {
				reader = new BufferedReader(new FileReader(nameFile));
				waitforfile=true; 
				 } catch (Exception e) {
					 System.out.println("Plik zablokowany");
					 waitforfile=false; 
				 } 
			}	 
			
		}while ((waitforfile==false)); 
	//	reader = new BufferedReader(new FileReader(nameFile));
	}
	
	String line;
	line = reader.readLine();
	var templist = new ArrayList<String>();
	templist = GetSplit(line,spliter);
	templistlist.add(GetSplit(line,spliter));
		while (line!=null )
		{
			line = reader.readLine();
			if (line!=null)
			{
				templistlist.add(GetSplit(line,spliter));
			}
		}
	reader.close();

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

return templistlist;
}

public ArrayList<String> ImportFlatFile(boolean ifUTF8, String nameFile)
{
ArrayList<String> templistlist = new ArrayList<String>();
try {
	//FileInputStream fis = new FileInputStream(nameFile);
	//InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
	//BufferedReader reader = new BufferedReader(new FileReader(nameFile, StandardCharsets.UTF_8));
	//Path path = Paths.get(nameFile);
	//String s = Files.readString(path, StandardCharsets.UTF_8);
	BufferedReader reader;
	if (ifUTF8==true)
	{
		reader = new BufferedReader(new FileReader(nameFile, StandardCharsets.UTF_8));
	}else
	{
		reader = new BufferedReader(new FileReader(nameFile));
	}
	
	String line;
	line = reader.readLine();
		while (line!=null )
		{
			line = reader.readLine();
			if (line!=null)
			{
				templistlist.add(line);
			}
		}
	reader.close();

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

return templistlist;
}
//
//
//
//

//
//
//Splitowanie pliku rozdzielonego 
private ArrayList<String> GetSplit(String args, String spliter)
{
	String[] templist = args.split(spliter);
	ArrayList<String> list = new ArrayList<String>();
	for (String temp : templist)
	{
		list.add(temp);
	}
	return list;
}



//public FlatFile (String str)
//	{
//	this.str = str;
//	}

///Stare do sprawdzenia gdzie ja tego u¿ywam 
public ArrayList<String> ImportFlat() throws FileNotFoundException, IOException
{
	StringArray = new ArrayList<String>();
	
	try (FileReader temp = new FileReader(str))
	{
		StringBuffer sb = new StringBuffer();
		while (temp.ready())
		{
			char c = (char) temp.read();
			if (c == '\n')
			{
				StringArray.add(sb.toString());
				sb = new StringBuffer();
			}
			else
			{
				sb.append(c);
			}
		}
	}

	
	return StringArray;
		
}

public String[] IFtoString () throws FileNotFoundException, IOException
{
	ArrayList<String> a_temp = ImportFlat();
	String[] StaticString = new String[a_temp.size()];

	for (int i = 0 ; i<a_temp.size(); i++)
	{
		StaticString[i] =  a_temp.get(i).toString();
	}
	return StaticString;
	
}


}


 