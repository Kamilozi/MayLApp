package MyLittleSmt;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Klasa komunikuj¹ca siê z Pythonem w celu pobrania danych
 * @author kamil
 * @version 1.0
 */
public class PythonBase {
private ILogAddToLog AddLog = new ILogAddToLog();
private ArrayList<ArrayList<Object>> ListLog = new ArrayList<ArrayList<Object>>();
private static String pythonpath = "C:\\MyLittleSmt\\FromBase.py";
private static String pythonpathTo = "C:\\MyLittleSmt\\ToBase.py";
private static String ymlpath = "C:\\MyLittleSmt\\SQL";
private static String ymlname = "ICompany.yml"; 
private static String filepath = "C:\\MyLittleSmt\\From\\";
private static String querypath = "C:\\MyLittleSmt\\To";
private ArrayList<ArrayList<String>> ListFromBase;

	public void setYmlPath(String path)
	{
		this.ymlpath = path;
	}
	public String getYamlPath()
	{
		return this.ymlpath;
	}
	public ArrayList<ArrayList<Object>> getListLog()
	{
		return ListLog;
	}
/**
 * Wysy³a zapytanie i odbiera dane z bazy
 * @param ymlname
 * @return Wynik zapytania
 * @throws IOException
 */
	public ArrayList<ArrayList<String>> FromBase(boolean ifUTF8, String ymlname) throws IOException
	{
		ArrayList<ArrayList<String>> templistlist = new ArrayList<ArrayList<String>>();
		PythonBaseMethods asd = new PythonBaseMethods(); 
			int hashcode= asd.getHashCode();
			SimpleDateFormat formatter= new SimpleDateFormat("yyMMddHHmmss");
			Date date = new Date(System.currentTimeMillis());
			String nameFile =  filepath + String.valueOf(hashcode) + formatter.format(date) + ".txt";		
		
		asd.PythonRun(pythonpath, ymlpath, ymlname, nameFile);
		File tempFile = new File(nameFile);		
		File tempFileKontr = new File(nameFile + "_kont");	
		if (asd.WaitForMe(tempFile)==true & asd.WaitForMe(tempFileKontr)==true)
		{
			FlatFile flatfile = new FlatFile();	
			templistlist=flatfile.ImportFlatFile(ifUTF8, nameFile, ";");		
				tempFile.delete();
				tempFileKontr.delete();
		}
		else
		{
			System.out.print("Plik nie znaleziony czy coœ");
		}	
		return templistlist;
	}

public void JTableHelper()
{
	
 
}

/**
 * Modyfikacja danych w bazie podzielona na Update/Insert/Condition
 * @param mapsysBase
 * @param Condition
 * @param Update
 * @param Insert
 * @param Delete
 */
public void JTableToBase(HashMap<String, String> mapsysBase,
		HashMap<Integer, ArrayList<String>> Condition,
		HashMap<Integer, ArrayList<String>> Update,
		HashMap<Integer, ArrayList<String>> Insert,
		HashMap<Integer, ArrayList<String>> Delete)
{
///	StringBuilder Query = new StringBuilder();
	AddLog = new ILogAddToLog();
	StringBuilder result = new StringBuilder();
	ArrayList<String> tempcol = new ArrayList<String>();
	HashMap<String, ArrayList<String>> BaseUpdate = new HashMap<String, ArrayList<String>>();

	for (String i: mapsysBase.keySet())
	{
		if (tempcol.isEmpty())
		{
			tempcol.add(mapsysBase.get(i));
		}else if (!tempcol.contains(mapsysBase.get(i)))
		{
			tempcol.add(i);
		}
	}
	for (int i=0; i<tempcol.size();i++)
	{	
		for(String j: mapsysBase.keySet())
		{
			if (mapsysBase.get(j).equals(tempcol.get(i)))
			{
				ArrayList<String> verytemp = new ArrayList<String>();
				verytemp.add(j);
				if (!BaseUpdate.containsKey(tempcol.get(i)))
				{
					BaseUpdate.put(tempcol.get(i), verytemp);
				}else
				{
					BaseUpdate.get(tempcol.get(i)).add(j);
				}
			}
		}
	}
//	
///Tutaj update	
	for (String str: BaseUpdate.keySet())
	{
		if (Delete.size()>0)
		{
			result.append(DeleteBase(str, Delete));
		}
		if (Update.size()>0)
		{		
			HashMap<Integer, ArrayList<String>> THMUpdate = new HashMap<Integer, ArrayList<String>>();
			for (Integer i : Update.keySet())
			{
				ArrayList<String> TLUpdate = new ArrayList<String>();
				for (int j=0; j< Update.get(i).size(); j++)
				{
					if (mapsysBase.get(Update.get(-1).get(j)).equals(str))
					{
						TLUpdate.add(Update.get(i).get(j));
					}
				}
				THMUpdate.put(i, TLUpdate);
			}
			result.append(updateBase(true, str, mapsysBase,THMUpdate, Condition));
		}
		if (Insert.size()>0)
		{
			HashMap<Integer, ArrayList<String>> THMInsert = new HashMap<Integer, ArrayList<String>>();
			for (Integer i: Insert.keySet())
			{
				ArrayList<String> TLInsert = new ArrayList<String>();
				for(int j=0; j< Insert.get(i).size(); j++)
				{
					if (mapsysBase.get(Insert.get(-1).get(j)).equals(str))
					{
						TLInsert.add(Insert.get(i).get(j));
					}
				}
				THMInsert.put(i, TLInsert);
			}
			result.append(insertBase(true, str, mapsysBase,THMInsert));
		}
	}
////	
	PythonBaseMethods asdTo = new PythonBaseMethods();
	int hashcode= asdTo.getHashCode();
	SimpleDateFormat formatter= new SimpleDateFormat("yyMMddHHmmss");
	Date date = new Date(System.currentTimeMillis());
	String filename= String.valueOf(hashcode) + formatter.format(date) + ".yml";
	String filepath = querypath + "\\" + filename;	
	File myObj = new File(filepath);
	try {
		FileWriter myWriter = new FileWriter(filepath);
		myWriter.write("query : \"" + result.toString() + "\"\n" );
		myWriter.close();
		asdTo.PythonRun(pythonpathTo, ymlpath, querypath, filename);
		if (asdTo.WaitImGone(myObj))
		{
		//	JOptionPane.showMessageDialog(null, "Zapis zakoñczony","Informacja", JOptionPane.INFORMATION_MESSAGE);
			AddLog.AddToLog("Zapis zakoñczony", "(I)");
		}
		else
		{//System.out.print("Plik nie znikn¹³");
		AddLog.AddToLog("Plik nie znikn¹³", "(E)");}
	} catch (IOException e) {

		e.printStackTrace();
	}
	ListLog.addAll(AddLog.getListLog());
}

public void runProcedure(String query)
{///genQuery(procedureName, procedureParameters)
	PythonBaseMethods asdTo = new PythonBaseMethods();
	int hashcode= asdTo.getHashCode();
	SimpleDateFormat formatter= new SimpleDateFormat("yyMMddHHmmss");
	Date date = new Date(System.currentTimeMillis());
	String filename= String.valueOf(hashcode) + formatter.format(date) + ".yml";
	String filepath = querypath + "\\" + filename;	
	File myObj = new File(filepath);
	try {
		FileWriter myWriter = new FileWriter(filepath);
		myWriter.write("query : \"" + query.toString() + "\"\n" );
		myWriter.close();
		asdTo.PythonRun(pythonpathTo, ymlpath, querypath, filename);
		if (asdTo.WaitImGone(myObj))
		{
		//	JOptionPane.showMessageDialog(null, "Procedura wykonana","Informacja", JOptionPane.INFORMATION_MESSAGE);
			AddLog.AddToLog("Zapis zakoñczony", "(I)");
		}
		else
		{
			AddLog.AddToLog("Plik nie znikn¹³", "(E)");
		
		}
	} catch (IOException e) {

		e.printStackTrace();
	}	
	ListLog.addAll(AddLog.getListLog());
}
/**
 * Usuwanie danych z bazy - realizowane jako pierwsze
 */
public StringBuilder DeleteBase(String base,
		HashMap<Integer, ArrayList<String>> Delete)
{	
	StringBuilder result = new StringBuilder();
	for (Integer i: Delete.keySet())
	{
		if (i>=0)
		{
			result.append("DELETE FROM " + base + " WHERE ");
			for (int j=0; j<Delete.get(i).size();j++)
			{
				result.append(Delete.get(-1).get(j) + "='" + Delete.get(i).get(j) + "'and ");
			}
			result.delete(result.length()-4, result.length()-1);
			result.append(";\n");
			
		}
	}

	return result;
}

/**
 * Dodawanie rekordów do bazy
 */
public StringBuilder insertBase(boolean modby, String base,
		HashMap<String, String> mapsysBase,
		HashMap<Integer, ArrayList<String>> Insert)
{
	StringBuilder result = new StringBuilder();
	
	for (Integer i: Insert.keySet())
	{
		if (i==-1)
		{}
		else if (i>=0)
		{
			result.append("INSERT INTO " + base + " (");
			StringBuilder Column = new StringBuilder();
			StringBuilder Values = new StringBuilder();
			for (int j=0; j<Insert.get(i).size(); j++)
			{
				Column.append(Insert.get(-1).get(j) + ", ");
				Values.append("'" + Insert.get(i).get(j) + "', ");
			}
			if (modby==true)
			{
				Column.append("CREATIONDATETIME, MODDATETIME, MODBY, CREATEBY, ");
				Values.append("GETDATE(), GETDATE(), 'ADMIN', 'ADMIN', ");
			}
			Column.deleteCharAt(Column.length()-2);
			Values.deleteCharAt(Values.length()-2);
			result.append(Column + ") Values (" + Values + ");\n");
		}
	}
	return result;
	
}

/**
 * Modyfikacja istniej¹cych rekordów. 
 */
public StringBuilder updateBase(boolean modby, String base,
		HashMap<String, String> mapsysBase,
		HashMap<Integer, ArrayList<String>> Update,
		HashMap<Integer, ArrayList<String>> Condition
		)
{
	StringBuilder result = new StringBuilder();
//	System.out.print("dupa");
	for (Integer i: Update.keySet())
	{   
		
		if (i>=0)
		{
			result.append("UPDATE " + base + " SET ");
			for (int j=0; j<Update.get(i).size();j++)
			{
				result.append(Update.get(-1).get(j) + "='" + Update.get(i).get(j) + "', ");
			}
			if (modby==true)
			{
				result.append("MODDATETIME=GETDATE(), MODBY='ADMIN', ");
			}
			result.deleteCharAt(result.length()-2);
			
			result.append("WHERE ");
			for (int q=0; q<Condition.get(i).size();q++)
			{
				result.append(Condition.get(-1).get(q) + "='" + Condition.get(i).get(q) + "' and ");
				
			}
			result.delete(result.length()-4, result.length()-1);
			result.append(";\n");	
		} else if (i==-1)
		{}
		
	}

	return result;
}
/**
 * Elementy nieprzypisane pobierania danych do Modelu.
 * @throws IOException 
 */
public DefaultTableModel getDataToJT(int Enable, HashMap<String, String> mapsys, 
		HashMap<String, ArrayList<String>> mapsysall, String query) throws IOException 	
{	

FrameTemplate frametemplate = new FrameTemplate();
ArrayList<ArrayList<String>> templist = FromBase(false, query);
ListFromBase =new ArrayList<ArrayList<String>>(templist);
txt txtlist = new txt();
	ArrayList<Integer> remList = new ArrayList<Integer>();
	for (int i=0;i<templist.get(0).size();i++)
	{
		if (templist.get(0).get(i).equals("CREATIONDATETIME")||templist.get(0).get(i).equals("MODDATETIME")||
			templist.get(0).get(i).equals("MODBY")||templist.get(0).get(i).equals("CREATEBY"))
		{
			for (int j=0; j<templist.size();j++)
			{
				templist.get(j).remove(i);
			}
			i--;
		}
	}

Object[][] moddata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
String[] modcol = Arrays.copyOfRange(txtlist.ArrayListToStringOne(templist.get(0)), 0, templist.get(0).size());
DefaultTableModel result =frametemplate.NewModel(Enable ,modcol, moddata, mapsys, mapsysall);

return result;
}

public ArrayList<ArrayList<String>> getListFromBase()
{
	return ListFromBase;
}
	
	
	
//
//
//
//
//
//Stary kawa³ek kodu 
	
	public void ToBase(Object[] keylist, Object[][] datalist, String[] column, String table)
	{
///
		StringBuilder query = new StringBuilder();
		for (int q=0; q<datalist.length; q++)
		{
			if (Arrays.stream(keylist).anyMatch(datalist[q][0]::equals))
			{
				query.append("UPDATE " + table +" SET");
				for (int i =0; i<column.length; i++)
				{
					
				
					if (datalist[q][i].getClass().getName().toString()=="java.lang.Boolean")
					{
						if ((boolean) datalist[q][i])
						{
							query.append(" " + column[i] + "='1',");
						}else
						{
							query.append(" " + column[i] + "='0',");
						}
					} else
					{
					query.append(" " + column[i] + "='" + datalist[q][i]+"',");
					}
					}
				query.deleteCharAt(query.length() - 1);
				query.append( " Where " + column[0] + "='" +datalist[q][0] + "'; \n");
				
			}else
			{
				query.append("INSERT INTO " + table + " ");
				StringBuilder dataquery = new StringBuilder();
				StringBuilder coluquery = new StringBuilder();
				coluquery.append(" ( ");
				for (int i = 0; i < column.length; i++)	
				{
					coluquery.append(column[i] + " ,");
				}
					coluquery.deleteCharAt(coluquery.length() - 1);
					coluquery.append(", CREATIONDATETIME, MODDATETIME, MODBY, CREATEBY) ");
					dataquery.append(" Values (");
				for (int i = 0; i<datalist[q].length; i++)
				{
					if (datalist[q][i].getClass().getName().toString()=="java.lang.Boolean")
					{
						if ((boolean) datalist[q][i])
						{
							dataquery.append("'1' ,");	
						}else
						{
							dataquery.append("'0' ,");
						}
					}else
					{
					dataquery.append("'" + datalist[q][i] + "' ,");
					}
				}
				dataquery.deleteCharAt(dataquery.length() - 1);
				dataquery.append(", GETDATE(), GETDATE(), 'ADMIN', 'ADMIN') ");
				query.append(coluquery.toString() + dataquery.toString() + "; \n");
			}	
			
		}

		PythonBaseMethods asdTo = new PythonBaseMethods();
		int hashcode= asdTo.getHashCode();
		SimpleDateFormat formatter= new SimpleDateFormat("yyMMddHHmmss");
		Date date = new Date(System.currentTimeMillis());
		String filename= String.valueOf(hashcode) + formatter.format(date) + ".yml";
		String filepath = querypath + "\\" + filename;	
		//File filepathFile = new File(filepath);
		File myObj = new File(filepath);
		try {
			FileWriter myWriter = new FileWriter(filepath);
			myWriter.write("query : \"" + query + "\"\n" );
			myWriter.close();
			asdTo.PythonRun(pythonpathTo, ymlpath, querypath, filename);
			//JOptionPane.showMessageDialog(this, "dupa!", "" ,JOptionPane.INFORMATION_MESSAGE);
			if (asdTo.WaitImGone(myObj))
			{
			//JOptionPane.showMessageDialog(null, "Zapis zakoñczony","Informacja", JOptionPane.INFORMATION_MESSAGE);
				AddLog.AddToLog("Zapis zakoñczony", "(I)");
			}else
			{
				////System.out.print("Plik nie znikn¹³");
				AddLog.AddToLog("Plik nie znikn¹³", "(E)");
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ListLog.addAll(AddLog.getListLog());
	}

}

class PythonBaseMethods
{
	public boolean WaitImGone (File pathfile)
	{
		boolean waitforgone = false;
		Instant later = Instant.now().plusSeconds(10) ;
		do{
			if (!pathfile.exists()||later.isBefore(Instant.now()))
			{
				waitforgone=true; 
			}
		}while ((waitforgone==false)); 
		
		if (pathfile.exists()==true)
		{
			waitforgone=false;	
		}

		return waitforgone;
	}
	
	public boolean WaitForMe (File pathfile)
	{
		boolean waitforfile = false; 
		Instant later = Instant.now().plusSeconds(10) ;
			do{
				if (pathfile.exists()||later.isBefore(Instant.now()))
				{
					waitforfile=true; 
				}
			}while ((waitforfile==false)); 
			
			if (pathfile.exists()==false)
			{
				waitforfile=false;	
			}

		return waitforfile;
	}
	
	public void PythonRun(String Path,String YMLPath, String ymlname, String code)
	{
		
		String command = "py \"" + Path + "\" \"" + YMLPath + "\" \"" + ymlname + "\" \"" + code + "\"";
		try 
		{
			Process p = Runtime.getRuntime().exec(command);
	
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	}
	public void PythonRunSec(String Path,String YMLPath, String ymlname, String code)
	{
		
		String command = "py \"" + Path + "\" \"" + YMLPath + "\" \"" + ymlname + "\" \"" + code + "\"";
		try 
		{
			Process p = Runtime.getRuntime().exec(command);
	
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	}
	
	
	public int getHashCode ()
	{
		return System.identityHashCode(this);
	}
}
