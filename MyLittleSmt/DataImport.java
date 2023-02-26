package MyLittleSmt;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.JFileChooser;
 

public class DataImport {
	private ArrayList<ArrayList<Object>> ListLog = new ArrayList<ArrayList<Object>>();
	//private ILog log;
	/**
	 * Modyfikator dostêpu 
	 * @return
	 */
	public ArrayList<ArrayList<Object>> getListLog()
	{
		return ListLog;
	}
	
	/**
	 * Import danych do JTable z pliku 
	 * @param sysall
	 * @param act
	 * @param TableKey
	 * @param keyCol
	 * @return
	 */
	public ArrayList<ArrayList<String>> DataToJTable(HashMap<String, 
													ArrayList<String>> sysall,
													ArrayList<ArrayList<String>> act, 
													ArrayList<Integer> TableKey,
													int keyCol
													)
	{	 
		ILogAddToLog AddLog = new ILogAddToLog();
		AddLog.AddToLog("Rozpoczêcie importu pliku", "(I)");

		 FlatFile flatfile = new FlatFile();
		 String FileName = setFileName();
		 ArrayList<ArrayList<String>> templist = flatfile.ImportFlatFile(false, FileName, ";");
		 AddLog.AddToLog("Plik:" + FileName, "(I)");
		 AddLog.AddToLog("Liczba wierszy w pliku: " + (templist.size()-1), "(I)");
		 ListLog.addAll(AddLog.getListLog());
		 if (CheckData(templist, sysall, keyCol))
		 {
			 ArrayList<ArrayList<String>> newList = CheckKey(true, templist, act, TableKey);
			 
			 return newList;
		 }
		 return null;
	}
	
	public ArrayList<ArrayList<String>> SaveJTable(HashMap<String,ArrayList<String>> sysall,
													ArrayList<ArrayList<String>> act, 
													ArrayList<ArrayList<String>> templist,
													ArrayList<Integer> TableKey)
	{
		
		
		return null;
	}
	
	public String setFileName()
	{
		String file = null;
		JFileChooser fchoose = new JFileChooser();
		fchoose.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fchoose.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
    		file =String.valueOf(fchoose.getSelectedFile());
		}
		return file;
	}
	public String setFileName(String defaltDirectory)
	{
		String file = null;
		JFileChooser fchoose = new JFileChooser();
		fchoose.setCurrentDirectory(new File(defaltDirectory));
		int result = fchoose.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
    		file =String.valueOf(fchoose.getSelectedFile());
		}
		return file;
	}
	
	
	public File[] setMultiFileName(String defaltDirectory)
	{
		File[] files = null;
		JFileChooser fchoose = new JFileChooser();
		fchoose.setCurrentDirectory(new File(defaltDirectory));
		fchoose.setMultiSelectionEnabled(true);
		int result = fchoose.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			files = fchoose.getSelectedFiles();
		}
		
		return files;
	}
	
	public ArrayList<ArrayList<String>> DataToJTableNBP(HashMap<String, ArrayList<String>> sysall,
														ArrayList<ArrayList<String>> act, 
														ArrayList<Integer> TableKey,
														ArrayList<ArrayList<String>> tempList)
	{
		ILogAddToLog AddLog = new ILogAddToLog();
		AddLog.AddToLog("Rozpoczêcie sprawdzanie importowanego zakresu", "(I)");
		ListLog.addAll(AddLog.getListLog());
		 if (CheckData(tempList, sysall, -1))
		 {		
			 
			  ArrayList<ArrayList<String>> newList = CheckKey(true, tempList, act, TableKey);
			  
			  return newList;
		 }
		 return null;
		
	}
	/**
	 * Sprawdzanie danych z ustawieniem bazy.
	 * Obs³u¿one elementy "bit", "string" (d³ugoœæ), "date". Nieobecna z powodu braku mo¿liwoœci sprawdzenia decimal/BigDecimal
	 * @param data
	 * @param sysall
	 * @return true/false czy poprawny
	 */

	public boolean CheckData(ArrayList<ArrayList<String>> data,HashMap<String, ArrayList<String>> sysall, int keyCol)
	{
		ILogAddToLog AddLog = new ILogAddToLog();
		AddLog.AddToLog("Sprawdzanie poprawnoœci danych", "(I)");
		boolean result = true;
		for (int i=1; i<data.size(); i++)
		{
		
			for (int j=0; j<data.get(i).size();j++)
			{	
				if (sysall.containsKey(data.get(0).get(j)))
				{
					
						if (sysall.get(data.get(0).get(j)).get(1).equals("NO")&&data.get(i).get(j).length()<=0&&j!=keyCol
								&&!sysall.get(data.get(0).get(j)).get(2).equals("nvarchar")
								&&!sysall.get(data.get(0).get(j)).get(2).equals("char")
								&&!sysall.get(data.get(0).get(j)).get(2).equals("nchar"))
						{
						//	addToLog("Niedopuszczalna pusta komórka w wierszuR["+ i + 1 +"]C["+ j + 1 +"]");
						//	result = false;
							AddLog.AddToLog("B³¹d 1: Niedopuszczalna pusta komórka R["+ (i + 1) + "]C["+ (j + 1) +"]", "(E)");
						}
						else
						{
							if (sysall.get(data.get(0).get(j)).get(2).equals("bit"))
							{
								if (data.get(i).get(j).equals("0")||data.get(i).get(j).equals("1"))
								{
									
								}else
								{
									AddLog.AddToLog("B³¹d 4: B³êdna wartoœæ typu boolean w komórce R["+ (i + 1) + "]C["+ (j + 1) +"]", "(E)");
									result=false;
								}

								
							}else if (sysall.get(data.get(0).get(j)).get(2).equals("date")) 
							{
								try {
								DateTimeFormatter fomatter = DateTimeFormatter.ISO_LOCAL_DATE;
								LocalDate dt = LocalDate.parse(data.get(i).get(j), fomatter);
								} catch (DateTimeParseException e) {
									// TODO Auto-generated catch block
									//System.out.print(e.getErrorIndex()); ///error 338
									AddLog.AddToLog("B³¹d 2: B³êdny format daty w komórce R["+ (i + 1) + "]C["+ (j + 1) +"]", "(E)");
									result = false;
								}
 
							} else if (sysall.get(data.get(0).get(j)).get(2).equals("numeric")
									||sysall.get(data.get(0).get(j)).get(2).equals("smallint")
									||sysall.get(data.get(0).get(j)).get(2).equals("tinyint"))
							{
								if (data.get(i).get(j) == null) {
									AddLog.AddToLog("B³¹d 7: Pusta wartoœæ w komórce R["+ (i + 1) + "]C["+ (j + 1) +"]", "(E)");
									result = false;
							    }
							    try {
							        double d = Double.parseDouble(data.get(i).get(j).replace(",", "."));
							    } catch (NumberFormatException nfe) {
							    	AddLog.AddToLog("B³¹d 8: Nienumeryczna wartoœæ w komórce R["+ (i + 1) + "]C["+ (j + 1) +"]", "(E)");
									result = false;
							    }
							     
							}
							else
							{
								if (Integer.valueOf(sysall.get(data.get(0).get(j)).get(3))<data.get(i).get(j).length())
								{
									AddLog.AddToLog("B³¹d 3: Zbut du¿o znaków w komórce R["+ (i + 1) + "]C["+ (j + 1) +"]", "(E)");
									result = false;
								}		
							}
						}
					
				}
				else
				{
					AddLog.AddToLog("B³¹d 6: Niezmapowana kolumna " + data.get(0).get(j), "(E)");
					result = false;
				}
			}
		}
		if (result==true)
		{
			AddLog.AddToLog("Nie wykryto b³êdów", "(I)");
		}else if (result==false)
		{
			AddLog.AddToLog("Wykryto b³êdy", "(W)");
		}
		
		ListLog.addAll(AddLog.getListLog());
		return result;
	}
	
	public ArrayList<ArrayList<String>> CheckKey(boolean Delete, 
							ArrayList<ArrayList<String>> data, 
							ArrayList<ArrayList<String>> act, 
							ArrayList<Integer> TableKey)
	{
	ArrayList<ArrayList<String>> newData = new ArrayList<ArrayList<String>>(data);	
	if (TableKey.size()>0)
	{	
		
		ILogAddToLog AddLog = new ILogAddToLog();
		AddLog.AddToLog("Sprawdzanie unikalnoœci danych", "(I)");
		boolean isGood = true;
		ArrayList<ArrayList<String>> actKey = new ArrayList<ArrayList<String>>(); 
		for (int i=1; i<act.size();i++)
		{
			ArrayList<String> temActKey = new ArrayList<String>();
			for (int j=0; j<TableKey.size(); j++)
			{
				temActKey.add(act.get(i).get(TableKey.get(j)));
			}
			actKey.add(temActKey);
		}
		int q=0;
		for (int i=1; i<data.size();i++)
		{
			q++;
			ArrayList<String> temdataKey = new ArrayList<String>();
			for (int j=0; j<TableKey.size(); j++)
			{
				temdataKey.add(data.get(i).get(TableKey.get(j)));
			}
			if (actKey.contains(temdataKey))
			{
				if (Delete==true)
				{
					newData.remove(q);
					q--;
					AddLog.AddToLog("Wiersz R["+ (i + 1) + "] istnieje ju¿ w systemie. Rekord zostanie pominiêty", "(W)");
				}else if (Delete==false)
				{
					isGood = false;
					AddLog.AddToLog("Wiersz R["+ (i + 1) + "] istnieje ju¿ w systemie", "(W)");
				}
			}
			
			
		}

		if (isGood==true)
		{
			AddLog.AddToLog("Zakoñczono sprawdzanie unikalnoœci. Zaimportowano " + (newData.size()-1) + " wierszy", "(I)");
			ListLog.addAll(AddLog.getListLog());
			return newData;
		}else if (isGood==false)
		{
			AddLog.AddToLog("B³¹d 5. Zadanie przerwane. Brak unikalnoœci danych", "(E)");
			return null;	 
		}
		

	}
	return newData;
	}

	
	
}
