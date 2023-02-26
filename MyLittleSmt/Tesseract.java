package MyLittleSmt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.nio.file.StandardCopyOption.*;
public class Tesseract {
private String workPath ="C:\\Users\\kamil\\OneDrive\\THM_GROUP\\Projekty\\Koszty\\THM_Group";
private String oldPath = "C:\\Users\\kamil\\OneDrive\\THM GROUP_ROBOCZY\\TesseractOld";
private FrameTemplate frame = new FrameTemplate();
private  ArrayList<ArrayList<Object>> finalList;
private HashMap<String, Object> detail;
private  ArrayList<ArrayList<String>>  fileListStat;
private ArrayList<ArrayList<String>> detailList, tableList;
private HashMap<String, ArrayList<String>> tableRegExSec;
private HashMap<String, HashMap<String, ArrayList<String>>> tableRegExDetailSec;
private ILogAddToLog AddLog;

public ArrayList<ArrayList<Object>> getFinalList()
{
	return finalList;
}

public ArrayList<ArrayList<String>> getDetailList()
{
	return detailList;
}

public  ArrayList<ArrayList<String>> getTableList()
{
	return tableList;
}
public HashMap<String, Object> getDetail()
{
	return detail;	
}

public ArrayList<ArrayList<String>> getFileListStat()
{
	return fileListStat;
}
public void mainInvoice()
{
	
	File[] files = new DataImport().setMultiFileName(workPath);
	
///	String fileName = getFlie();
AddLog = new ILogAddToLog();
AddLog.AddToLog("Uruchomienie tesseracta ", "(I)");

ArrayList<String> commands = new ArrayList<String>(Arrays.asList("tesseract_ver_Prod.py", "tesseract_ver_Prod_v2.py"));
detailList = new ArrayList<ArrayList<String>>(Arrays.asList(new ArrayList<String>(Arrays.asList("ID", "FIRM", "DIMENSION_4", "DOCUMENTDATE", "ALTDOCUMENTDATE", "SETTLEMENTDATE", "TXT", "CURRENCYCODE", "OTYPE","ACCOUNTNUM","SUBACCOUNT"))));
tableList = new ArrayList<ArrayList<String>>(Arrays.asList(new ArrayList<String>(Arrays.asList("ID", "Lp", "Descriptions", "Qunatity", "Unit", "NetUnitPrice", "NetAmount", "VAT", "VATAmount","GrossAmount","DIMENSION",  "DIMENSION_2", "DIMENSION_3" ))));
fileListStat = new ArrayList<ArrayList<String>>();
int Lp = 0;
if (files!=null)
{
for (int k=0; k<files.length;k++)
{
String fileName = String.valueOf(files[k]);
finalList = new ArrayList<ArrayList<Object>>();	 
ArrayList<ArrayList<String>> templist = null;
if (fileName!=null&&fileName.substring(fileName.length()-3).toUpperCase().equals("PDF"))
	 {
		AddLog.AddToLog("Przetwarzanie pliku "+ fileName, "(I)");
		///nazwa pliku, Lp, status (0-fa³sz, 1-sukces
		fileListStat.add(new ArrayList<String>(Arrays.asList(fileName.toString(), "-1", "0")));
		int x = fileName.lastIndexOf("\\");
		String file = fileName.substring(x+1, fileName.length());
		String path = fileName.substring(0,  x);
		File directory = new File(path + "\\" + file + ".txt");
		
			for (int i=0;i<commands.size();i++)
			{
				AddLog.AddToLog("Metoda "+String.valueOf(commands.get(i)), "(I)");
				if (directory.exists())
					{directory.delete();}
				if (new PythonOther().runTesseract(file, path, commands.get(i)))
				{
					AddLog.AddToLog("Przetwarzanie pliku "+ file, "(I)");
					if (invoice(templist = new FlatFile().ImportFlatFile(false, path + "\\" + file + ".txt", "\\t")))
					{
						Lp++;
						SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
						Date ActDate = new Date(System.currentTimeMillis());
						String actDate = formatter.format(ActDate);
						for (int q=0;q<finalList.size();q++)
						{ ///String.format("%.2f",Double.valueOf(valueABS));
							double qty =  finalList.get(q).get(2).toString().trim().length()<=0?(double) 0:(double) finalList.get(q).get(2);
							double unitPrice =  finalList.get(q).get(3).toString().trim().length()<=0?(double) 0.00:(double) finalList.get(q).get(3);
							double netto = FrameTemplate.round(qty*unitPrice,2); 
							double vat = finalList.get(q).get(4).toString().trim().length()<=0?(double) 23.00:(double) finalList.get(q).get(4);
							double vatAmount = FrameTemplate.round((vat*netto)/100,2); 
							double gross = FrameTemplate.round(vatAmount+netto,2); 
							
							 

							
							
							 tableList.add(new ArrayList<String>(Arrays.asList(String.valueOf(Lp), 
									 											finalList.get(q).get(0).toString(), 
									 											finalList.get(q).get(1).toString().length()<=0?" ": finalList.get(q).get(1).toString(), 
									 													String.valueOf(qty), "szt.", String.valueOf(unitPrice), 
									 													String.valueOf(netto),String.valueOf(vat), String.valueOf(vatAmount), 
									 													String.valueOf(gross), "","", ""
									 											)));
						}
						detailList.add(new ArrayList<String>(Arrays.asList(String.valueOf(Lp), 
																		detail.containsKey("FirmID") ? detail.get("FirmID").toString() : "",
																		detail.containsKey("ContrID") ? detail.get("ContrID").toString() : "",
																		detail.containsKey("DocDate") ? (detail.get("DocDate").toString().trim().length()>0? detail.get("DocDate").toString():actDate) : actDate,
																		detail.containsKey("AltDate") ? (detail.get("AltDate").toString().trim().length()>0? detail.get("AltDate").toString():actDate) : actDate,
																		detail.containsKey("SetDate") ? (detail.get("SetDate").toString().trim().length()>0? detail.get("SetDate").toString():actDate) : actDate,
																		detail.containsKey("InvNum") ? detail.get("InvNum").toString(): "",
																		detail.containsKey("Currency") ? detail.get("Currency").toString(): "", "COSSIM", "", "")));
						for (int g=0;g<fileListStat.size();g++)
						{
							if (fileListStat.get(g).get(0).equals(fileName))
							{
								fileListStat.get(g).set(1, String.valueOf(Lp));
								fileListStat.get(g).set(2, String.valueOf("1"));
							}
						}
						new DocumentsSource().openFlie(fileName);
						AddLog.AddToLog("Przetwarzanie zakoñczone sukcesem, plik: "+ file, "(I)");
						break;
					}
						 
				}else
				{
					AddLog.AddToLog("B³¹d importu pliku "+ file, "(E)");
				}
			}
			File Olddirectory = new File(oldPath + "\\" + file + ".txt");
			Path target = Paths.get(String.valueOf(Olddirectory));
			Path source = Paths.get(String.valueOf(directory));
			try {
				Files.move(source, target, REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				AddLog.AddToLog("B³¹d. Nie uda³o siê usun¹æ pliku pomocniczego "+ file, "(E)");
			}
	 }
}

}
for (int g=0;g<fileListStat.size();g++)
{
	System.out.println(fileListStat.get(g).get(0) + " : " + fileListStat.get(g).get(1) + " : " + fileListStat.get(g).get(2));
}
ArrayList<ArrayList<Object>>  ListLog= AddLog.getListLog();
ILog log = new ILog(300, 600, "Log importu", ListLog);
log.setVisible(true);
}

public boolean invoice(ArrayList<ArrayList<String>> templist)
{
	//ArrayList<ArrayList<String>> templist = importFlie();
	boolean go= true;
	if 	(templist!=null)
	{
		
		HashMap<String, HashMap<String, ArrayList<String>>> tableRegExDetail = new HashMap<String, HashMap<String, ArrayList<String>>>();

		ArrayList<String> tableRegExDict = new ArrayList<String>(Arrays.asList("Lp", "Nazwa", "Ilosc", "Netto", "VAT"));
		//ArrayList<Object> tableHeader = new ArrayList<Object>(Arrays.asList("ID" , "Lp", "Descriptions", "QTYAMOUNT", "UNITPRICE", "VAT"));
		HashMap<String, Integer> columnTes =frame.getColumnNumbers(templist.get(0));
	
		ArrayList<ArrayList<Integer>> edges = detectFrame(columnTes, templist);
		//HashMap<String, ArrayList<String>> tableRegEx = new HashMap<String, ArrayList<String>>();
		//pierwszy jest zawsze kluczem
		//tableRegEx.put("Ergo", new ArrayList<String>(Arrays.asList("LP", "NAZWA", "LOŒÆ","CENA NETTO", "STAWKA VAT")));	/// : [A-Z]*
	//	tableRegEx.put("Ergo2", new ArrayList<String>(Arrays.asList("LP", "KOD", "LOŒÆ","CENA NETTO", "STAWKA VAT")));	/// : [A-Z]*
		//tableRegEx.put("Joanna", new ArrayList<String>(Arrays.asList("LP", "NAZWA", "LOŒÆ","CENA NETTO", "STAWKA — WT")));
		//tableRegEx.put("Joanna2", new ArrayList<String>(Arrays.asList("LP", "NAZWA", "LOŒÆ","CENA NETTO", "STAWKA VAT")));	
		//tableRegEx.put("DTZ", new ArrayList<String>(Arrays.asList("LP", "NAZWA", "LOŒÆ","CENA NETTO", "VAT %")));
	//	tableRegEx.put("maxbert", new ArrayList<String>(Arrays.asList("LP", "NAZWA", "LOŒÆ","CENA NETTO", "VAT")));
	//	tableRegEx.put("Bogna", new ArrayList<String>(Arrays.asList("LP", "NAZWA", "LOŒÆ","CENA NETTO", "VAT")));
	//	tableRegEx.put("ISOZ", new ArrayList<String>(Arrays.asList("LP", "NAZWA", "LOŒÆ","WARTOŒÆ NETTO", "KWOTA VAT")));
		HashMap<String, ArrayList<String>> tableRegEx = getTableRegEx();
		
		//tableRegExDetail.put("Frims", new HashMap<String, ArrayList<String>>());
		//tableRegExDetail.get("Frims").put("Ergo", new ArrayList<String>(Arrays.asList("Sprzedawca", "Nabywca")));
		//tableRegExDetail.put("ID", new HashMap<String, ArrayList<String>>());
		//tableRegExDetail.get("ID").put("Ergo", new ArrayList<String>(Arrays.asList("NIP", "NIP")));
		//tableRegExDetail.get("ID").put("Ergo", new ArrayList<String>(Arrays.asList("NIP:", "NIP:")));
		//tableRegExDetail.put("DocDate", new HashMap<String, ArrayList<String>>());
		//tableRegExDetail.get("DocDate").put("Ergo", new ArrayList<String>(Arrays.asList("data (.) miejsce wystawienia dokumentu")));
		//tableRegExDetail.get("DocDate").put("Joanna", new ArrayList<String>(Arrays.asList("Data wystawienia:")));
		//tableRegExDetail.put("AltDate", new HashMap<String, ArrayList<String>>());
		//tableRegExDetail.get("AltDate").put("Ergo", new ArrayList<String>(Arrays.asList("data zakoñczenia dostawy towaru")));
		//tableRegExDetail.get("AltDate").put("Joanna", new ArrayList<String>(Arrays.asList("Data dostawy/wykonania us³ugi:")));	
		//tableRegExDetail.get("AltDate").put("Maxbert", new ArrayList<String>(Arrays.asList("Data dostawy (.)* wykonania us³ugi:")));
		//tableRegExDetail.put("SetDate", new HashMap<String, ArrayList<String>>());
		//tableRegExDetail.get("SetDate").put("Ergo", new ArrayList<String>(Arrays.asList("Termin p.atno.ci:")));
		//tableRegExDetail.get("SetDate").put("Joanna", new ArrayList<String>(Arrays.asList("Termin p³atnoœci:")));	
		//tableRegExDetail.get("SetDate").put("Maxbert", new ArrayList<String>(Arrays.asList("Termin")));
		//tableRegExDetail.put("DateFormat", new HashMap<String, ArrayList<String>>());
		//tableRegExDetail.get("DateFormat").put("1", new ArrayList<String>(Arrays.asList("\\d\\d\\d\\d-\\d\\d-\\d\\d", "yyyy-MM-dd")));
		//tableRegExDetail.get("DateFormat").put("2", new ArrayList<String>(Arrays.asList("\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d", "dd.MM.yyyy")));
		//tableRegExDetail.get("DateFormat").put("3", new ArrayList<String>(Arrays.asList("\\d\\d/\\d\\d/\\d\\d", "dd/MM/yy")));
		//tableRegExDetail.put("InvNum", new HashMap<String, ArrayList<String>>());
		//tableRegExDetail.get("InvNum").put("InvNum", new ArrayList<String>(Arrays.asList("Faktura")));	
		//tableRegExDetail.put("InvNumPlus", new HashMap<String, ArrayList<String>>());
		//tableRegExDetail.get("InvNumPlus").put("InvNumPlus", new ArrayList<String>(Arrays.asList("VAT", "NR")));
		//tableRegExDetail.put("Currency", new HashMap<String, ArrayList<String>>());
		//tableRegExDetail.get("Currency").put("Currency", new ArrayList<String>(Arrays.asList("S³ownie:", "Razem do zap³aty")));	
		//tableRegExDetail.put("CurrencyName", new HashMap<String, ArrayList<String>>());
		//tableRegExDetail.get("CurrencyName").put("PLN", new ArrayList<String>(Arrays.asList("PLN", "z³otych")));	
		//ustawienia dodatkowe
		//tableRegExDetailSec
		tableRegExDetail =tableRegExDetail();
		for (String key:tableRegEx.keySet())
		{
			if (go==true)
			{
			HashMap<String, ArrayList<Integer>> result = new HashMap<String, ArrayList<Integer>>();
			for (int i=1; i<templist.size();i++)
			{
				if (templist.get(i).get(columnTes.get("level")).equals("5")&&regEx(tableRegEx.get(key).get(0).toUpperCase(), templist.get(i).get(columnTes.get("text")).toUpperCase()))
				{
					
					result = new HashMap<String, ArrayList<Integer>>();
					result.put(tableRegExDict.get(0),  listToResultInvoice(i, columnTes, templist));
					int line=Integer.parseInt(templist.get(i).get(columnTes.get("line_num")));
					///k - wyszukiawnie pocz¹tku wiersza - 1
					//int k=i;
					//while(Integer.parseInt(templist.get(k).get(columnTes.get("line_num")))>=(line-1))
					//{--k;}
					//k++;
					int contSum0 = 2;
					int k=i;
					while (contSum0>0)
					{
						if (Integer.parseInt(templist.get(k).get(columnTes.get("level")))==4)
						{
							contSum0--;
						}
						k--;
					}
					k++;
					contSum0 = 2;
					int t=i;
					while (contSum0>0)
					{
						if (Integer.parseInt(templist.get(t).get(columnTes.get("level")))==4)
						{
							contSum0--;
						}
						t++;
					}
					t--;
					for (int j = 1; j<tableRegEx.get(key).size();j++)
					{
						String[] splitString = tableRegEx.get(key).get(j).split(" ");
						int q = k;
						String _first =  splitString[0];
						while(q<t)
						{	
							if (templist.get(q).get(columnTes.get("level")).equals("5")&&regEx(_first.toUpperCase(), templist.get(q).get(columnTes.get("text")).toUpperCase()))
							{
								if (splitString.length==1)
								{
									result.put(tableRegExDict.get(j),  listToResultInvoice(q, columnTes, templist));
								}else if  (splitString.length>1)
								{
									//nale¿y poszukaæ obramowania
									ArrayList<ArrayList<Integer>> location = new ArrayList<ArrayList<Integer>>(Arrays.asList(listToResultInvoice(q, columnTes, templist))); 
									boolean isOK = true;
									for (int e=1; e<splitString.length;e++)
									{
										HashMap<String, ArrayList<Integer>> next = nextText(q, columnTes,templist, location.get(location.size()-1), edges, true);
										HashMap<String, ArrayList<Integer>> under = underText(q, columnTes,templist, location.get(location.size()-1), edges);
										String nextString = null;
										String underString = null;
										if (next.size()>0) {for (String nextKey: next.keySet()) {nextString=nextKey;}}
										if (under.size()>0) {for (String underKey: under.keySet()) {underString=underKey;}}
										
										if (nextString!=null&&regEx(splitString[e], nextString))
										{
											location.add(next.get(nextString));
										}
										else if (underString!=null&&regEx(splitString[e], underString))
										{
											location.add(under.get(underString));
										}else
										{
											isOK = false;
										}
										if (isOK==false)
										{
											break;
										}
									}
									if (isOK==true)
									{
										int left = 0;
										int top = 0;
										int right = 0;
										int down = 0;
										for (int e=0; e<location.size();e++)
										{
											if (left==0||left>location.get(e).get(0))
											{
												left = location.get(e).get(0);
											}
											if (top==0||top>location.get(e).get(1))
											{
												top = location.get(e).get(1);
											}
											if (right==0||right<location.get(e).get(4))
											{
												right = location.get(e).get(4);
											}
											if (down==0||down<location.get(e).get(5))
											{
												down = location.get(e).get(5);
											}
										}
										result.put(tableRegExDict.get(j), new ArrayList<Integer>(Arrays.asList(left,top,right-left, down-top,right,down)));
									}
								}
								
							}
							q++;
						}
					}
					
				}
				//
				
				
				//dodanie warunku sprawdzaj¹cego czy LP jest najbardziej na lewo. 
				if (result.size()==tableRegExDict.size()&&isLpOnLeft(result))
				{
					///odnajdywanie tabelki
					sizeFrameCorrect(result, edges,columnTes,templist);
					ArrayList<ArrayList<String>> tableList = new ArrayList<ArrayList<String>>(Arrays.asList(tableRegExDict));
					int preSize = tableRange(tableList, columnTes,templist,result ) + 1;
					///dodatkowa walidacja i poszerzanie kolumn, jeœli nie wykryto ramki
					 for (String value: result.keySet())
					 {
						 if (result.get(value).get(6)==0)
						 {
								sizeZeroCorrect(value, 
										preSize, 
										result,
										columnTes,
										templist);	
						 }
					 }
					 int size = realTableSize(preSize, columnTes, 
								templist, 
								result);
					 ArrayList<Integer> header = getHeaderSize(result);
					 ArrayList<Integer> range = new ArrayList<Integer>(Arrays.asList(header.get(0), 
							 														 header.get(5) , 
							 														 header.get(2),
							 														 size-header.get(5), 
							 														 header.get(4), 
							 														 size   ));
					 finalList = getTable(range, header, result, edges,columnTes,templist);
					 go=false;
					 detail  = new HashMap<String, Object>();
					 getDetail(detail, header, range, columnTes, templist, tableRegExDetail, edges);
					 break;

				}else
				{
					if (result.size()==tableRegExDict.size())
					{
						AddLog.AddToLog("Nie znaleziono pierwszego s³owa kluczowego", "(E)");
					} 
				}
			}
		}
		}	
		if (finalList!=null&&finalList.size()>0)
		{		 
			///finalList.add(0, tableHeader);
			return true;
		}else if (finalList!=null&&finalList.size()==0)
		{
			AddLog.AddToLog("Nie znaleziono ¿adnego s³owa kluczowego", "(E)");
		}else if (finalList!=null&&finalList.size()>0)
		{
			AddLog.AddToLog("Nie znaleziono wszystkich s³ów kluczowych", "(E)");
		}
	}	 
	return false;
}

private boolean isLpOnLeft(HashMap<String, ArrayList<Integer>> result)
{///"Lp", "Nazwa", "Ilosc", "Netto", "VAT"
	int A = result.get("Lp").get(0);
	
	for (String key:result.keySet())
	{
		if (!key.equals("Lp")&&result.get(key).get(0)<A)
		{
			return false;
		}
	}
	return true;
}


private void getDetail(HashMap<String, Object> detail, 
					   ArrayList<Integer> header, ArrayList<Integer> range,
					   HashMap<String, Integer> column,
					   ArrayList<ArrayList<String>> templist,
					   HashMap<String, HashMap<String, ArrayList<String>>> tableRegExDetail,
					   ArrayList<ArrayList<Integer>> edges)
{
	//badamy tylko obszar powy¿ej i poni¿ej tabeli
	//wszystko z obszaru górnego
	 //HashMap<Integer, ArrayList<Integer>> linesinUpRange = linesInRange(range.get(0), range.get(4), range.get(1),range.get(5),  column,  templist);

	ArrayList<String> status = new ArrayList<String>();
	for (String key: tableRegExDetail.keySet())
		for (String keyST: tableRegExDetail.get(key).keySet())
		{
			if (key.equals("Frims")&&!status.contains(key))
			{
				boolean firmStat = false;
				boolean conterStat = false;
				int lContr = findFirstInTemplis(column, templist, 1, templist.size(), tableRegExDetail.get(key).get(keyST).get(0), true, new ArrayList<Integer>());
				int lFirm = findFirstInTemplis(column, templist, 1, templist.size(), tableRegExDetail.get(key).get(keyST).get(1), true, new ArrayList<Integer>());
				String patFir = tableRegExDetail.get("ID").containsKey(keyST) ? tableRegExDetail.get("ID").get(keyST).get(0) : "NIP";
				String patSec = tableRegExDetail.get("ID").containsKey(keyST) ? tableRegExDetail.get("ID").get(keyST).get(1) : "NIP";
				int lIDFir = findFirstInTemplis(column, templist, lContr==0? 1:lContr, templist.size(), patFir, true, new ArrayList<Integer>());
				int lIDSec = findFirstInTemplis(column, templist, lIDFir==0? 1:lIDFir, templist.size(), patSec, true,lIDFir>0 ? new ArrayList<Integer>(Arrays.asList(lIDFir)): new ArrayList<Integer>());
				if (lContr!=0&&lFirm!=0&&lIDFir!=0&&lIDSec!=0)
				{
				 ArrayList<Integer>  contrLoc =  listToResultInvoice(lContr, column, templist);
				 ArrayList<Integer>  firmLoc =  listToResultInvoice(lFirm, column, templist);
				 ArrayList<Integer>  firIDLoc =  listToResultInvoice(lIDFir, column, templist);
				 ArrayList<Integer>  secIDLoc =  listToResultInvoice(lIDSec, column, templist);

				 int topConFir = firIDLoc.get(1)-contrLoc.get(5); 
				 int topConSec = secIDLoc.get(1)-contrLoc.get(5); 
				 int topFirmFir = firIDLoc.get(1)-firmLoc.get(5);
				 int topFirmSec = secIDLoc.get(1)-firmLoc.get(5);
				 
				 ArrayList<Integer>  contrID = topConFir >0 && topConFir<topConSec ? firIDLoc : secIDLoc;
				 int contrIDQ = topConFir>0 && topConFir<topConSec ? lIDFir : lIDSec;
				 ArrayList<Integer> firmID = topConFir >0 && topConFir<topConSec ? secIDLoc : firIDLoc;
				 int firmIDQ =topConFir>0 && topConFir<topConSec ? lIDSec : lIDFir;
				 
				 
				 HashMap<String, ArrayList<Integer>> nextContr = nextText(contrIDQ, column,templist, contrID, edges, true);
				 HashMap<String, ArrayList<Integer>> nextContr1 = new  HashMap<String, ArrayList<Integer>>();
				 HashMap<String, ArrayList<Integer>> nextFirm = nextText(firmIDQ, column,templist, firmID, edges, true);
				 HashMap<String, ArrayList<Integer>> nextFirm1 = new HashMap<String, ArrayList<Integer>>();
				 
				 String contrId = null;
				 String firmId = null;
				 for (String conKey: nextContr.keySet())
				 {
					 if (conKey.length()<10)
					 {
						 nextContr1 = nextText(getLocationOnTemplist(column,templist, nextContr.get(conKey)), column,templist, firmID, edges, true);
						 for (String nextKey: nextContr1.keySet())
						 {
							 contrId =  getString(nextKey, "(\\d)*");
						 }
					 }else 
					 {
						 contrId = getString(conKey, "(\\d)*");
					 }
				 }
				 for (String firmKey: nextFirm.keySet())
				 {
					 if (firmKey.length()<10)
					 {
						 nextFirm1 = nextText(getLocationOnTemplist(column,templist, nextFirm.get(firmKey)), column,templist, firmID, edges, true);
						 for (String nextKey: nextFirm1.keySet())
						 {
							 firmId = getString(nextKey, "(\\d)*");
						 }
					 }else
					 {
						 firmId =getString(firmKey, "(\\d)*");
					 }
				 }
				 if (!detail.containsKey("FirmID"))
				 {
					 detail.put("FirmID", firmId);
				 }
				 if (!detail.containsKey("ContrID"))
				 {
					 detail.put("ContrID", contrId);
				 }				 
				 if (detail.containsKey("ContrID")&&detail.containsKey("FirmID"))
				 {
					status.add(key);
				 }	
			}
			}else if ((key.equals("DocDate")||key.equals("AltDate")||key.equals("SetDate"))&&!status.contains(key))
			{
				for (int i=0;i<tableRegExDetail.get(key).get(keyST).size();i++)
				{
					if (!status.contains(key))
					{	
					String[] splitString = tableRegExDetail.get(key).get(keyST).get(i).split(" ");

 					for (int j=1;j<templist.size();j++)
					{
 						HashMap<Integer, ArrayList<Integer>> dateResult = new HashMap<Integer, ArrayList<Integer>>();
 						HashMap<Integer, String> dateResultString = new HashMap<Integer, String>();
						if(	FrameTemplate.round(Double.valueOf(templist.get(j).get(column.get("conf"))), 0)!=95
							&&Integer.parseInt(templist.get(j).get(column.get("level")))==5
							&&regEx(splitString[0].toUpperCase(), templist.get(j).get(column.get("text"))))
						{
							
							int left = Integer.parseInt(templist.get(j).get(column.get("left")));
							int top = Integer.parseInt(templist.get(j).get(column.get("top")));
							int width = Integer.parseInt(templist.get(j).get(column.get("width")));
							int height = Integer.parseInt(templist.get(j).get(column.get("height")));
							int leftPlus = Integer.parseInt(templist.get(j).get(column.get("left")))+Integer.parseInt(templist.get(j).get(column.get("width")));
							int topPlus = Integer.parseInt(templist.get(j).get(column.get("top"))) +Integer.parseInt(templist.get(j).get(column.get("height")));
							int q = j;
							dateResult.put(j, new ArrayList<Integer>(Arrays.asList(left, top,width, height,leftPlus, topPlus)));
							dateResultString.put(j, templist.get(j).get(column.get("text")));
							ArrayList<Integer> lWord = new ArrayList<Integer>(Arrays.asList(left, top,width, height,leftPlus, topPlus));
							for (int k =1;k<splitString.length;k++)
							{
								HashMap<String, ArrayList<Integer>> next = nextText( q, column,templist, lWord, edges, true);
								for (String nextKey:next.keySet())
								{
									q = getLocationOnTemplist(column,templist, next.get(nextKey));
									lWord = next.get(nextKey);
									if (regEx( splitString[k].toUpperCase(), nextKey))
									{
										dateResult.put(q, next.get(nextKey));
										dateResultString.put(q, nextKey);
										 
									}
									
									//dateResult.put(key, range)
									
								}
							

							}
					///		HashMap<Integer, ArrayList<ArrayList<Integer>>> allWordInRange = allWordInRangeByLine(linesinRange, column, left,  leftPlus);
							if (dateResult.size()==splitString.length)
							{					
								ArrayList<Integer> size = getRangeSize(dateResult);
								int maxR = 0;
								int maxQ = 0;
								  
								for (int lastQ: dateResult.keySet())
								{
									if (dateResult.get(lastQ).get(4)>maxR )
									{
										maxR = dateResult.get(lastQ).get(4);
										maxQ = lastQ;
									}
								}
								HashMap<String, ArrayList<Integer>> uptext = upText(column, templist, size,edges, false);
								HashMap<String, ArrayList<Integer>> nexttext = nextText(maxQ, column, templist, size,edges, true);
								String sdf = getDateInvoice(nexttext,  tableRegExDetail);
								if (sdf.length()<10)
								{
									sdf = getDateInvoice(uptext,  tableRegExDetail);
								}
								
								
								detail.put(key, sdf);
								status.add(key);
								break;
							}
						}
						 
					}
				}
				}
			}else if (key.equals("InvNum")&&!status.contains(key))
			{
				
				for (int i=0;i<tableRegExDetail.get(key).get(key).size();i++)
				{
					for (int j=1; j<templist.size();j++)
					{
					if(	FrameTemplate.round(Double.valueOf(templist.get(j).get(column.get("conf"))), 0)!=95
							&&Integer.parseInt(templist.get(j).get(column.get("level")))==5
							&&regEx(tableRegExDetail.get(key).get(key).get(i).toUpperCase(), templist.get(j).get(column.get("text"))))
						{
							boolean isOk=false;
							int q = j;
							while (isOk==false)
							{
								HashMap<String, ArrayList<Integer>> nexttext;
								if (nextText(j, column, templist, listToResultInvoice(j, column, templist),edges, false).size()<=0)
								{
									nexttext = underText(j, column, templist, listToResultInvoice(j, column, templist),edges);
								}else
								{
									nexttext = nextText(j, column, templist, listToResultInvoice(j, column, templist),edges, false);
								}
								boolean isNext = false;
								String word = "";
								for (String text: nexttext.keySet())
								{
									word = text;
									for (int k=0; k<tableRegExDetail.get("InvNumPlus").get("InvNumPlus").size();k++)
									{
										if (regEx(tableRegExDetail.get("InvNumPlus").get("InvNumPlus").get(k), text))
										{
											isNext =true;
											
											break;
										}
									}
								}
								if (isNext==false)
								{
									isOk = true;
									detail.put(key, word);
									status.add(key);

								}
								j++;
							}
						}
					}
				}
			}else if (key.equals("Currency")&&!status.contains(key))
			{
				for (int i=0;i<tableRegExDetail.get(key).get(keyST).size();i++)
				{
					if (!status.contains(key))
					{
						String[] splitString = tableRegExDetail.get(key).get(keyST).get(i).split(" ");
						boolean isCom = false;
						HashMap<Integer, ArrayList<Integer>> dateResult=new HashMap<Integer, ArrayList<Integer>>();
						ArrayList<String> result=new ArrayList<String>();
						HashMap<Integer, String> dateResultString;
							int q =0;
							for (int j=1;j<templist.size();j++)
							{
								dateResult = new HashMap<Integer, ArrayList<Integer>>();
								dateResultString = new HashMap<Integer, String>();
								if(	FrameTemplate.round(Double.valueOf(templist.get(j).get(column.get("conf"))), 0)!=95
										&&Integer.parseInt(templist.get(j).get(column.get("level")))==5)
									{
										if (regEx(splitString[0].toUpperCase(), templist.get(j).get(column.get("text"))))
										{
											dateResult.put(j, listToResultInvoice(j, column, templist));
											dateResultString.put(j, templist.get(j).get(column.get("text")));
											result.add(templist.get(j).get(column.get("text")));
											q =j;
											for (int k=1; k<splitString.length;k++)
											{
												HashMap<String, ArrayList<Integer>> word = nextText(q, column, templist, listToResultInvoice(q, column, templist),edges, false);
												for (String e:word.keySet())
												{
													if (word.size()>=0&&regEx(splitString[k],e))
													{
														dateResult.put(getLocationOnTemplist(column,templist, word.get(e)), listToResultInvoice(j, column, templist));
														dateResultString.put(getLocationOnTemplist(column,templist, word.get(e)), templist.get(getLocationOnTemplist(column,templist, word.get(e))).get(column.get("text")));
													}
												}
											}
											
											if (dateResult.size()==splitString.length)
											{
												
												break;
											}
										}
									}
							}
							if (dateResult.size()==splitString.length&&dateResult.size()>0)
							{
								ArrayList<Integer> size = getRangeSize(dateResult);
								
								

								
								for (int j=1;j<templist.size();j++)
								{
									if(	FrameTemplate.round(Double.valueOf(templist.get(j).get(column.get("conf"))), 0)!=95
											&&Integer.parseInt(templist.get(j).get(column.get("level")))==5
											&&sameLine(q, j, column,templist))
									{
										result.add(templist.get(j).get(column.get("text")));
									}
								}
								
								for (String curName: tableRegExDetail.get("CurrencyName").keySet())
								{
									for (int k=0;k<result.size();k++)
									{
										for (int j=0;j<tableRegExDetail.get("CurrencyName").get(curName).size();j++)
										{					
									
											if (regEx(tableRegExDetail.get("CurrencyName").get(curName).get(j).toUpperCase(),result.get(k).toUpperCase()))
											{
												detail.put(key, curName);
												status.add(key);
												break;
											}
										}
										if (status.contains(key))
										{break;}
									}
									if (status.contains(key))
									{break;}
								}
							//	System.out.print("Test");
							}
							
						
					}
				}
			}
		}
//	 System.out.print("Test");
}


private String getDateInvoice(HashMap<String, ArrayList<Integer>> text,  HashMap<String, HashMap<String, ArrayList<String>>> tableRegExDetail)
{
	String result = "";
	for (String next: text.keySet())
	{
		for (String format: tableRegExDetail.get("DateFormat").keySet())
		{
			if (regEx(tableRegExDetail.get("DateFormat").get(format).get(0), next))
			{
				Date date;
				try {
					date = new SimpleDateFormat(tableRegExDetail.get("DateFormat").get(format).get(1)).parse(next);
					result = new SimpleDateFormat("yyyy-MM-dd").format(date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
					date = new Date(System.currentTimeMillis());
					result = formatter.format(date);
				}
				
			}
		}
	}
	return result;
}

private String getString ( String mat, String pat)
{
String result= "";
	   Pattern regex = Pattern.compile(pat);
	   Matcher regexMatcher = regex.matcher(mat);
	   
	   while (regexMatcher.find())
	   		{
			   if  (regexMatcher.group().length()>0)
			   	{
				   result= result + regexMatcher.group();
			   	}				      
		   }
return result;
}
private int getLocationOnTemplist(HashMap<String, Integer> column,
		   							ArrayList<ArrayList<String>> templist,
		   							ArrayList<Integer> line
									)
{
	int result = 0;
	for (int i=1;i<templist.size();i++)
	{
		if (Integer.parseInt(templist.get(i).get(column.get("left")))==line.get(0)
				&&Integer.parseInt(templist.get(i).get(column.get("top")))==line.get(1)
				&&Integer.parseInt(templist.get(i).get(column.get("width")))==line.get(2)
				&&Integer.parseInt(templist.get(i).get(column.get("height")))==line.get(3)
				&&FrameTemplate.round(Double.valueOf(templist.get(i).get(column.get("conf"))), 0)!=95
				&&Integer.parseInt(templist.get(i).get(column.get("level")))==5)
		{
			result = i;
			break;
		}
	}
	return result;
}

/**
 * Sprawdza czy dwa s³owa s¹ w tej samej linii
 * @param i - g³owne s³owo
 * @param q - s³owo porównywane
 * @param column
 * @param templist
 * @return
 */
private boolean sameLine(int i, 
						 int q, 
						 HashMap<String, Integer> column,
						 ArrayList<ArrayList<String>> templist)
{
	boolean result = false;
	ArrayList<Integer> fir =  listToResultInvoice(i, column, templist);
	ArrayList<Integer> sec = listToResultInvoice(q, column, templist);
	int A = fir.get(1);
	int B = fir.get(5);
	int C = sec.get(1);
	int D = sec.get(5);
	if ((C>=A&&C<=B)||(D>=A&&D<=B)||(A>=C&&A<=D)||(B>=C&&B<=D))
	{
		result= true;
	}
	
	return result;
}

private int findFirstInTemplis(HashMap<String, Integer> column,
						   ArrayList<ArrayList<String>> templist,
						   int start,
						   int end,
						   String find,
						   boolean textOnly, 
						   ArrayList<Integer> qExept)
{
	int q = 0;

	for (int i=start;i<end;i++)
	{
		if ((	textOnly==true
				&&FrameTemplate.round(Double.valueOf(templist.get(i).get(column.get("conf"))), 0)!=95
				&&Integer.parseInt(templist.get(i).get(column.get("level")))==5)||(textOnly==false))
			{
				;
				if (regEx(find.toUpperCase(), templist.get(i).get(column.get("text")).toUpperCase()))
				{
					if (qExept.size()>0&&qExept.contains(i))
					{
						continue;
					}
					
					q = i;
					break;
				}
			}
	}
	return q;
}


private ArrayList<ArrayList<Object>> getTable(	ArrayList<Integer> range, 
						ArrayList<Integer> header, 
						HashMap<String, ArrayList<Integer>> sizeMap, 
						ArrayList<ArrayList<Integer>> edges,
						HashMap<String, Integer> column,
						ArrayList<ArrayList<String>> templist )
{
		///podzieliæ na linie
	 HashMap<Integer, ArrayList<Integer>> linesinRange = linesInRange(range.get(0), range.get(4), range.get(1),range.get(5),  column,  templist);
	///podzieliæ linie na s³owa
	 HashMap<Integer, ArrayList<ArrayList<Integer>>> allWordInRange = allWordInRangeByLine(linesinRange, column, templist,range.get(0),  range.get(4));
	 HashMap<Integer, ArrayList<ArrayList<Integer>>> allLpInRange = allWordInRangeByLine(linesinRange, column, templist,sizeMap.get("Lp").get(0),  sizeMap.get("Lp").get(4));
	 HashMap<Integer, ArrayList<ArrayList<Integer>>> prePos =new HashMap<Integer, ArrayList<ArrayList<Integer>>>();
	 HashMap<Integer, ArrayList<Integer>> prePosNum =new HashMap<Integer, ArrayList<Integer>>();
	 ArrayList<ArrayList<Object>> finalList =new  ArrayList<ArrayList<Object>>();
	 for (Integer key:allLpInRange.keySet())
	 {
		 for (int i=0;i<allLpInRange.get(key).size();i++)
		 {
			 if (allLpInRange.get(key).get(i).get(0)!=0
					 &&allLpInRange.get(key).get(i).get(1)!=0
					 &&allLpInRange.get(key).get(i).get(2)!=0
					 &&allLpInRange.get(key).get(i).get(3)!=0)
			 {
				 for (int k=1;k<templist.size();k++)
				 {
					 if (allLpInRange.get(key).get(i).get(0)==Integer.parseInt(templist.get(k).get(column.get("left")))
							 &&allLpInRange.get(key).get(i).get(1)==Integer.parseInt(templist.get(k).get(column.get("top")))
							 &&allLpInRange.get(key).get(i).get(2)==Integer.parseInt(templist.get(k).get(column.get("width")))
							 &&allLpInRange.get(key).get(i).get(3)==Integer.parseInt(templist.get(k).get(column.get("height"))))
						{
						 
							 
								Pattern pat = Pattern.compile("[0-9]*");
								Matcher mat = pat.matcher(templist.get(k).get(column.get("text")));
								if (mat.find())
								{
									//System.out.println(mat.group());
									if (mat.group().length()>0)
									{
										prePos.put(Integer.parseInt(mat.group()), new ArrayList<ArrayList<Integer>>(Arrays.asList(linesinRange.get(key))));
										finalList.add(new  ArrayList<Object>(Arrays.asList(Integer.parseInt(mat.group()), " ", " " , " ", " ")));
										prePosNum.put(Integer.parseInt(mat.group()),new ArrayList<Integer>(Arrays.asList(key)));
									}
								}
						}
				 }
			 }
		 }
	 }
	 ///œrednia odleg³oœæ
	 //HashMap<Integer, ArrayList<ArrayList<Integer>>> allDescriptionInRange = allWordInRangeByLine(linesinRange, column, templist,sizeMap.get("Nazwa").get(0),  sizeMap.get("Nazwa").get(4));
	 HashMap<Integer, ArrayList<Integer>> linesinDesc = linesInRange(sizeMap.get("Nazwa").get(0), sizeMap.get("Nazwa").get(4), range.get(1),range.get(5),  column,  templist);
	 int sumDis =0;
	 int numDis =0;
	  
	 for (int descKey:linesinDesc.keySet())
	 {
		 if (linesinDesc.containsKey(descKey-1))
		 {
		 numDis++;
		 sumDis = linesinDesc.get(descKey).get(1)-linesinDesc.get(descKey-1).get(5);
		 }
	}
	 int mediumDis=0;
	 if (sumDis>0&&numDis>0)
	 {
		 mediumDis = (int) ((sumDis/numDis) *1.10);
	 }
	 
	 ///allLpInRange
	 int blockNum = 0;
	 for (Integer line:linesinRange.keySet())
	 {
		 boolean isNew = true;
		 int preTop = 0;
		 int preMax = 10000;
		 int posPrev =0;
		 int posNext = 0;
		 int numLinePrev = 0;
		 int numLineNext = 0;
		 for (int keyPrePos:prePos.keySet())
		 {
			 for (int i=0;i<prePos.get(keyPrePos).size();i++)
			 {
				if (prePos.get(keyPrePos).get(i).get(0)==linesinRange.get(line).get(0)
						&&prePos.get(keyPrePos).get(i).get(1)==linesinRange.get(line).get(1)
						&&prePos.get(keyPrePos).get(i).get(2)==linesinRange.get(line).get(2)
						&&prePos.get(keyPrePos).get(i).get(3)==linesinRange.get(line).get(3)
						)
				{
					isNew= false;	 
				}else 
				{
					   if (linesinRange.get(line).get(1)-prePos.get(keyPrePos).get(i).get(5)>0&&prePos.get(keyPrePos).get(i).get(5)>preTop)
					   {
						   preTop = prePos.get(keyPrePos).get(i).get(5);
						   posPrev = keyPrePos;
						   
					   }
					   if (prePos.get(keyPrePos).get(i).get(1)-linesinRange.get(line).get(5)>0&&prePos.get(keyPrePos).get(i).get(1)<preMax)
					   {
						   preMax = prePos.get(keyPrePos).get(i).get(1);
						   posNext = keyPrePos;
					   }
				}
			 }
			 
		 }
		 
		 if (isNew==true)
		 {
			 //obs³u¿enie pierwszej niedopasowanej
			 if (posPrev==0&&posNext==1)
			 {
				 prePos.get(1).add(linesinRange.get(line));
				 prePosNum.get(1).add(line);
			 }//obs³uga ostatniej niedopasowanej
			 else if (posPrev>0&&posNext==0)
			 {
				 prePos.get(posPrev).add(linesinRange.get(line));
				 prePosNum.get(posPrev).add(line);
			 }//uproszczenie jeœli istnieje tylko jedna zidentyfikowana pozycja 
			 else if (prePos.size()==1)
			 {
				 prePos.get(1).add(linesinRange.get(line)); 
				 prePosNum.get(1).add(line);
			 }
			 //obs³uga tych w œrodku
			 else
			 {
				 ///jeœli istnieje ramka
				 boolean isDone = false;
				 for (int i=0;i<edges.size();i++)
				 {
					 if (edges.get(i).get(0)==1&&edges.get(i).get(2)>preTop&&edges.get(i).get(6)<preMax)
					 {
						 if (edges.get(i).get(2)>linesinRange.get(line).get(1))
						 {
							 prePos.get(posPrev).add(linesinRange.get(line));
							 prePosNum.get(posPrev).add(line);
						 }else if (edges.get(i).get(2)<linesinRange.get(line).get(1))
						 {
							 prePos.get(posNext).add(linesinRange.get(line));
							 prePosNum.get(posNext).add(line);
						 }
						 isDone=true;
						 break;	 
					 }
				 }
				 //jeœli nie istnieje ramka 
				 if (isDone == false)
				 {
					 ////jeœli jest tylko jeden wybór
					 if (mediumDis>0)
					 {
						 int prevDis =   linesinRange.get(line-1).get(5) ;
						 int nextDis = linesinRange.get(line+1).get(1);
						 if (prevDis-linesinRange.get(line).get(1)<=mediumDis)
						 {
							 prePos.get(posPrev).add(linesinRange.get(line));
							 prePosNum.get(posPrev).add(line);
						 }else 
						 {
							 prePos.get(posNext).add(linesinRange.get(line));
							 prePosNum.get(posNext).add(line);
						 }
					 }else
					 {
						 prePos.get(posPrev).add(linesinRange.get(line));
						 prePosNum.get(posPrev).add(line);
					 } 
					 
				 }
			 }
			 
		 }
	 }
	 for (int key:prePosNum.keySet())
	 {
		 Collections.sort(prePosNum.get(key));
	 }
	 
	 
	 //pêtla po liœcie wynikowej która jest uzupe³niona sizeMap "Lp", "Nazwa", "Ilosc", "Netto", "VAT"
	 for (int i=0; i<finalList.size();i++)
	 {
		 for (int q=0; q< prePosNum.get((int)finalList.get(i).get(0)).size();q++)
		 { 
			   
			 for (int w=0; w< allWordInRange.get(prePosNum.get(finalList.get(i).get(0)).get(q)).size();w++)
			 {
				 for (String mapKey: sizeMap.keySet())
				 {
					 if (!mapKey.equals("Lp"))
					 {
					 int left =allWordInRange.get(prePosNum.get(finalList.get(i).get(0)).get(q)).get(w).get(0);
					 int top =allWordInRange.get(prePosNum.get(finalList.get(i).get(0)).get(q)).get(w).get(1);
					 int width =allWordInRange.get(prePosNum.get(finalList.get(i).get(0)).get(q)).get(w).get(2);
					 int height =allWordInRange.get(prePosNum.get(finalList.get(i).get(0)).get(q)).get(w).get(3);
					 
					if (allWordInRange.get(prePosNum.get(finalList.get(i).get(0)).get(q)).get(w).get(0)>=sizeMap.get(mapKey).get(0)&&
							allWordInRange.get(prePosNum.get(finalList.get(i).get(0)).get(q)).get(w).get(0)<=sizeMap.get(mapKey).get(4))
					{
						for (int e=1;e<templist.size();e++)
						{
							if (Integer.parseInt(templist.get(e).get(column.get("left")))==left
									&&Integer.parseInt(templist.get(e).get(column.get("top")))==top
									&&Integer.parseInt(templist.get(e).get(column.get("width")))==width
									&&Integer.parseInt(templist.get(e).get(column.get("height")))==height
									&&FrameTemplate.round(Double.valueOf(templist.get(e).get(column.get("conf"))), 0)!=95
									&&Integer.parseInt(templist.get(e).get(column.get("level")))==5
									)
							{
								if (mapKey.equals("Nazwa"))
								{
									
									String str = finalList.get(i).get(1) + " " + templist.get(e).get(column.get("text"));
									finalList.get(i).set(1, str);
								}else if (mapKey.equals("Ilosc"))
								{
									String str = finalList.get(i).get(2) + " " + templist.get(e).get(column.get("text"));
									finalList.get(i).set(2, str);
								}else if (mapKey.equals("Netto"))
								{
									String str = finalList.get(i).get(3) + " " + templist.get(e).get(column.get("text"));
									finalList.get(i).set(3, str);
								}else if (mapKey.equals("VAT"))
								{
									String str = finalList.get(i).get(4) + " " + templist.get(e).get(column.get("text"));
									finalList.get(i).set(4, str);
								}
							}
						}
					}
					 }
				 }
			 }
		 }
	 }
	 for (int i=0; i<finalList.size();i++)
	 {
		 for (int q = 1; q<finalList.get(i).size();q++)
		 {
			 if (q==1)
			 {
				 String str = finalList.get(i).get(q).toString().replace("|", "").trim();
				 finalList.get(i).set(q , str);
			 }else
			 {
				 	String str = finalList.get(i).get(q).toString().replace(",", ".");
				 	str.trim();
				 	String strB =String.valueOf(str).replace(" ", "");
		 
				 	 
					Pattern pat = Pattern.compile("[\\d]+\\.?\\d*");
					Matcher mat = pat.matcher(strB);
					if (mat.find())
					{
				 
						if (mat.group().length()>0)
						{
							///prePos.put(Integer.parseInt(mat.group()), new ArrayList<ArrayList<Integer>>(Arrays.asList(linesinRange.get(key))));
							///finalList.add(new  ArrayList<Object>(Arrays.asList(Integer.parseInt(mat.group()), " ", " " , " ", " ")));
							///prePosNum.put(Integer.parseInt(mat.group()),new ArrayList<Integer>(Arrays.asList(key)));
							Double value = Double.valueOf(mat.group());
							finalList.get(i).set(q,value );
							//System.out.println(value);
						}
					}///dodanie else 20230125
					else
					{
						finalList.get(i).set(q,"" );
					}
			 }
		 }
	 }
	 
 
	return finalList;
}


 
private  ArrayList<Integer> getHeaderSize(HashMap<String, ArrayList<Integer>> sizeMap)
{
	ArrayList<ArrayList<Integer>> fullList = new ArrayList<ArrayList<Integer>>();
	
	 for (String key : sizeMap.keySet())
	 {
		 fullList.add(sizeMap.get(key));
	 }
	 HashMap<Integer, ArrayList<ArrayList<Integer>>> toRange = new HashMap<Integer, ArrayList<ArrayList<Integer>>>();
	 toRange.put(0, fullList);
	 return rangeMax(toRange);
	 
}
private  ArrayList<Integer> getRangeSize(HashMap<Integer, ArrayList<Integer>> sizeMap)
{
	ArrayList<ArrayList<Integer>> fullList = new ArrayList<ArrayList<Integer>>();
	
	 for (Integer key : sizeMap.keySet())
	 {
		 fullList.add(sizeMap.get(key));
	 }
	 HashMap<Integer, ArrayList<ArrayList<Integer>>> toRange = new HashMap<Integer, ArrayList<ArrayList<Integer>>>();
	 toRange.put(0, fullList);
	 return rangeMax(toRange);
	 
}
/**
 * Korekta d³ugoœci zakresu na podstawie dodatkowych danych
 * @param simSize - stara wartoœæ
 * @param column - nazwy kolumn w tabeli z tesseracta
 * @param templist - tabela tesseracta
 * @param sizeMap - kolumny 
 * @return
 */
private Integer realTableSize(int simSize, HashMap<String, Integer> column, 
								ArrayList<ArrayList<String>> templist, 
								HashMap<String, ArrayList<Integer>> sizeMap )
{
int lMax = 0;
int rMax = 0;
int upMax = 0;
int dMax = 0;
Integer result = 0;
for (int i=1; i<templist.size();i++)
{
	int top= Integer.parseInt(templist.get(i).get(column.get("top")));
	if (top>simSize)
	{
		int left= Integer.parseInt(templist.get(i).get(column.get("left")));
		int width= Integer.parseInt(templist.get(i).get(column.get("width")));
		int height= Integer.parseInt(templist.get(i).get(column.get("height")));
		int rM = Integer.parseInt(templist.get(i).get(column.get("left")))+Integer.parseInt(templist.get(i).get(column.get("width"))) ;
		int dM= Integer.parseInt(templist.get(i).get(column.get("top"))) + Integer.parseInt(templist.get(i).get(column.get("height")));
		if (lMax==0&&rMax==0&&upMax==0&&dMax==0) 
		{
			lMax=left;
			rMax=rM;
			upMax=top;
			dMax=dM;
		}else 
		{
			if (lMax>left)
			{
				lMax=left;
			}
			if (rMax<rM)
			{
				rMax = rM;
			}
			if (dM>dMax)
			{
				dMax= dM;
			}
		}
	}

}
ArrayList<Integer> fullMax = new ArrayList<Integer>();
HashMap<Integer, ArrayList<Integer>> linesinRange;
if (rMax!=0&&upMax!=0&&dMax!=0)
{
	 fullMax = new ArrayList<Integer>(Arrays.asList(lMax,upMax, rMax-lMax,dMax-upMax,rMax,dMax));
	 linesinRange = linesInRange(lMax -1, rMax + 1, upMax - 1,dMax,  column,  templist);
	 HashMap<Integer, ArrayList<ArrayList<Integer>>> allWordInKey = allWordInRangeByLine(linesinRange, column, templist,lMax,  rMax);
	 ///warunki do spe³nienia aby kwalifikowaæ jako nastêpn¹ liniê. 
	 ///1. Lp zawsze musi byæ puste
	 boolean isOk=true;
	 ArrayList<Integer> addList =new ArrayList<Integer>();
	 for (Integer lineKey: allWordInKey.keySet())
	 {
		 ArrayList<Integer> descr =new ArrayList<Integer>();
		 for (String colKey: sizeMap.keySet())
		 {
				 int lCol = sizeMap.get(colKey).get(0);
				 int rCol = sizeMap.get(colKey).get(4);
				 for (int i=0;i<allWordInKey.get(lineKey).size(); i++)
				 {
					 int lWord = allWordInKey.get(lineKey).get(i).get(0);
					 int rWord = allWordInKey.get(lineKey).get(i).get(4);
					 ///columna lp ma pozostaæ pusta
					 if (colKey.equals("Lp")&&((lWord>lCol&&lWord<rCol)||(rWord>lCol&&rWord<rCol)||(lCol>lWord&&lCol<rWord)))
					 {
						 isOk = false;
						 break;
					 }else 
					 {	///sprawdzam czy coœ jest
						 if ((lWord>lCol&&lWord<rCol)||(rWord>lCol&&rWord<rCol)||(lCol>lWord&&lCol<rWord))
						 {   ///sprawdzam czy siê mieœci w zakresie
							 if ((lWord>lCol&&rWord>rCol)||(rWord>lCol&&rWord>rCol)||(lCol>lWord))
							 {
								 isOk = false;
								 break;
							 }else
							 { 
								 if (colKey.equals("Nazwa"))
								 {
									 descr.add(lineKey);
								 }
							}	 
						 }
						 
					 }
				 }
				 if (descr.size()<1&&colKey.equals("Nazwa"))
				 {
					 isOk = false;
					 
				 }
		 }
		 if (isOk==true)
		 { 
			 addList.add(lineKey);
		 }
	}

	 if (addList.size()>0)
	 {
		 int maxline = 0;
		 for (int i=0;i<addList.size();i++)
		 {
			 if (maxline==0||maxline<i)
			 {
				 maxline = addList.get(i);
			 }
		 }
		 result = linesinRange.get(maxline).get(5)+1;
	 }
	
	 
	/// System.out.print(false);	
}
///System.out.print(false);	
if (result == 0)
	{
		return simSize;
	}
else 
	{
		
		return result;	
	
	}
}
/**
 * Uproszczony badany zakres, przyjmuj¹cy koniec na poziomie ostatniego Lp.
 * @param result - uproszczony koniec
 * @param column - nazwy kolumn templist
 * @param templist - tabela z tesseracta
 * @param sizeMap -mapa rozmiarów
 * @return
 */
private Integer tableRange(ArrayList<ArrayList<String>> result, 
						HashMap<String, Integer> column, 
						ArrayList<ArrayList<String>> templist, 
						HashMap<String, ArrayList<Integer>> sizeMap)
{
	//for (String key:sizeMap.keySet())
	//{
		//A-lewy róg klucza, B-prawy róg klucza, C lewy róg zmiennej, D lewy róg zmiennej
		int A = sizeMap.get("Lp").get(0);
		int B = sizeMap.get("Lp").get(4);
		int topA = sizeMap.get("Lp").get(1);
		int preSize = 0;
		for (int i=1;i<templist.size();i++)
		{
			int C = Integer.parseInt(templist.get(i).get(column.get("left")));
			int D = Integer.parseInt(templist.get(i).get(column.get("left"))) + Integer.parseInt(templist.get(i).get(column.get("width")));
			int topC = Integer.parseInt(templist.get(i).get(column.get("top")));
			if ((topC>topA)&&templist.get(i).get(column.get("level")).equals("5")&&
					FrameTemplate.round(Double.valueOf(templist.get(i).get(column.get("conf"))), 0)!=95
					&&((C>A&&C<B)||(D>A&&D<B)))
			{
				Pattern pat = Pattern.compile("[0-9]*");
				Matcher mat = pat.matcher(templist.get(i).get(column.get("text")));
				if (mat.find())
				{
					//System.out.println(mat.group());
					if (mat.group().length()>0)
					{
					//System.out.println(topC+Integer.parseInt(templist.get(i).get(column.get("height"))));
					preSize = topC+Integer.parseInt(templist.get(i).get(column.get("height")));
					}
				}
			}
		}
		
		return preSize;
	//}
}
/**
 * Korekta zakresu jeœli nie wykryto ramki
 * @param size - dolny zakres przeszukiwanego obszaru
 * @param edges - mapa tabelek
 * @param column - mapa nazw kolumn w tabeli g³ównej 
 * @param templist - tabela g³óna
 */
private void sizeZeroCorrect(String value, 
		int size, 
		HashMap<String, ArrayList<Integer>> sizeMap,
		HashMap<String, Integer> column,
		ArrayList<ArrayList<String>> templist)
{
	//okreœlenie boków zakresu
	int sizeLeft = 0;
	int sizeRight = 0;
	int lValue = sizeMap.get(value).get(0);
	int rValue = sizeMap.get(value).get(4);
	int puValue = sizeMap.get(value).get(1); 
	int pdValue = sizeMap.get(value).get(5); 
	int lMax = 0;
	int rMax = 0;
	int lFullMax = 10000;
	int rFullMax = 0;
	///badanie pionowego przedzia³u nag³owka
	int A=0;
	int B=0;
	
	for (String key: sizeMap.keySet())
	{
		if (A==0||sizeMap.get(key).get(1)<A)
		{
			A=sizeMap.get(key).get(1);
		}
		if (B==0||sizeMap.get(key).get(5)>B)
		{
			B=sizeMap.get(key).get(5);
		}			
	}
	///badanie granic wzglêdem ju¿ istniej¹cych elementów
	for (String key:sizeMap.keySet())
	{
		int lKey = sizeMap.get(key).get(0);
		int rKey = sizeMap.get(key).get(4);
		//badanie w linii kolummny
		if (key!=value&&A<=puValue&&B>=pdValue)
		{
			if (lValue-rKey>0&&(rMax==0||lMax<rKey))
			{
				lMax= rKey;
			}
			if (lKey-rValue>0&&(rMax==0||rMax>lKey))
			{
				rMax = lKey;
			}
		}
		///badanie ca³ego zakresu 
		if (lFullMax==0||lFullMax>lKey)
		{
			lFullMax= lKey;
		}
		if (rFullMax==0||rFullMax<rKey)
		{
			rFullMax= rKey;
		}
		
	}
	HashMap<Integer, ArrayList<Integer>> linesinRange = linesInRange(lMax, rMax, B,size,  column,  templist);

	HashMap<Integer, ArrayList<ArrayList<Integer>>> wordInRange = new HashMap<Integer, ArrayList<ArrayList<Integer>>>();
	HashMap<Integer, ArrayList<ArrayList<Integer>>> allWordInKey  = allWordInRangeByLine(linesinRange, column, templist,lMax,  rMax);
	HashMap<Integer, ArrayList<ArrayList<Integer>>> allWordInKeyT =allWordInRangeByLine(linesinRange, column, templist,lMax,  rMax);
	for (int keyRem:allWordInKeyT.keySet())
	{
		
		if (allWordInKeyT.get(keyRem).get(0).get(0)==0
				&&allWordInKeyT.get(keyRem).get(0).get(1)==0
				&&allWordInKeyT.get(keyRem).get(0).get(2)==0
				&&allWordInKeyT.get(keyRem).get(0).get(3)==0)
		{
			allWordInKey.remove(keyRem);
		}
	}
	
	for (int key: linesinRange.keySet())
	{
		int upKey =  linesinRange.get(key).get(1);
		int dKey =  linesinRange.get(key).get(5);
		
		for (int i=1;i<templist.size();i++)
		{
			int li = Integer.parseInt(templist.get(i).get(column.get("left")));
			int ri = Integer.parseInt(templist.get(i).get(column.get("left"))) + Integer.parseInt(templist.get(i).get(column.get("width")));
			int ui = Integer.parseInt(templist.get(i).get(column.get("top")));
			int di = Integer.parseInt(templist.get(i).get(column.get("top"))) + Integer.parseInt(templist.get(i).get(column.get("height")));
			if (li>lMax&&ri<rMax&&ui>=upKey&&di<=dKey&&
					templist.get(i).get(column.get("level")).equals("5")
					&&FrameTemplate.round(Double.valueOf(templist.get(i).get(column.get("conf"))), 0)!=95)
			{
				if ((li>=lValue&&li<=rValue)||(ri>=lValue&&ri<=rValue))
				{
					if (wordInRange.containsKey(key))
					{
						wordInRange.get(key).add(listToResultInvoice(i, column, templist));
					}else
					{
						wordInRange.put(key, new ArrayList<ArrayList<Integer>>(Arrays.asList(listToResultInvoice(i, column, templist))));
					}
				}
				
			}
		}
		if (!wordInRange.containsKey(key))
		{ 	int q = -1;
			int min = 10000;
			if (allWordInKey.containsKey(key))
			{	
			for (int i=0 ; i< allWordInKey.get(key).size();i++)
			{
				int li = allWordInKey.get(key).get(i).get(0);
				int ri = li + allWordInKey.get(key).get(i).get(2); 
				if ((lValue-ri>0)&&(lValue-ri<min))
				{
					min = lValue-ri;
					q = i;
				}else if ((li-rValue>0)&&(li-rValue<min))
				{
					min = li-rValue;
					q = i;
				}
			}
			if (q>-1)
			{
				wordInRange.put(key, new ArrayList<ArrayList<Integer>>(Arrays.asList(allWordInKey.get(key).get(q))));

			}
		}
			
		}
		


		/////
	}
		
	////jeœli size>1 policzyæ spacje +5%  i sprawdziæ kolejne s³owa po prawej i lewej
	HashMap<Integer, ArrayList<Integer>> spaces = new HashMap<Integer, ArrayList<Integer>>();
	HashMap<Integer, ArrayList<Integer>> rangeMax = new HashMap<Integer, ArrayList<Integer>>();
	int sumSpace = 0;
	int  number = 0;
	for (Integer wirKey:wordInRange.keySet())
	{
		if (wordInRange.get(wirKey).size()>1)
		{
			for (int i=1; i<wordInRange.get(wirKey).size();i++)
			{	
				if (wordInRange.get(wirKey).get(i).get(0)-wordInRange.get(wirKey).get(i-1).get(4)>0)
				{
					sumSpace += wordInRange.get(wirKey).get(i).get(0)-wordInRange.get(wirKey).get(i-1).get(4);
					number++;
						if (spaces.containsKey(wirKey))
						{
							spaces.get(wirKey).add(wordInRange.get(wirKey).get(i).get(0)-wordInRange.get(wirKey).get(i-1).get(4));
						}else
						{
							spaces.put(wirKey, new ArrayList<Integer>(Arrays.asList((wordInRange.get(wirKey).get(i).get(0)-wordInRange.get(wirKey).get(i-1).get(4)))));
						}
				}
			}
		}
		///okreœlanie skrajnego lewego i prawego boku
		if (wordInRange.get(wirKey).size()>0) 
		{
			int l=10000;
			int r=0;
			for (int i=0; i<wordInRange.get(wirKey).size();i++)
			{
				if (wordInRange.get(wirKey).get(i).get(4)>r)
				{
					r=wordInRange.get(wirKey).get(i).get(4);
				}
				if (wordInRange.get(wirKey).get(i).get(0)<l)
				{
					l=wordInRange.get(wirKey).get(i).get(0);
				}
			}
					
			rangeMax.put(wirKey, new ArrayList<Integer>(Arrays.asList(l,r)));	
			
		}
	}
	 
	if (sumSpace>0&&number>0)
	{ 
		int averageSpaceD = (int)((sumSpace/number) * 1.10);
		for (int awKey: allWordInKey.keySet())
		{
			ArrayList<ArrayList<Integer>> containter = new ArrayList<ArrayList<Integer>>();
 			if (allWordInKey.get(awKey).size()>1)
			{
				
				for (int i=0; i<allWordInKey.get(awKey).size();i++)
				{   ///szukamy lewego 
					if (rangeMax.get(awKey).get(0)>allWordInKey.get(awKey).get(i).get(4)
							&&rangeMax.get(awKey).get(0)-allWordInKey.get(awKey).get(i).get(4)<=averageSpaceD)
					{
						rangeMax.get(awKey).set(0, allWordInKey.get(awKey).get(i).get(0));
						containter.add(allWordInKey.get(awKey).get(i));
					}
					///szukamy prawego
					if (rangeMax.get(awKey).get(1)<allWordInKey.get(awKey).get(i).get(0)
							&&allWordInKey.get(awKey).get(i).get(0)-rangeMax.get(awKey).get(1)<=averageSpaceD)
					{
						rangeMax.get(awKey).set(1, allWordInKey.get(awKey).get(i).get(4));
						containter.add(allWordInKey.get(awKey).get(i));
					}
				}
				if (containter.size()>0)
				{
				wordInRange.get(awKey).addAll(containter);
				}
				
			}
		}
		

	}
 
	ArrayList<Integer>  fullRangeMax = rangeMax (wordInRange);

	if (fullRangeMax.size()>0)
	{
		if (sizeMap.get(value).get(0)>fullRangeMax.get(0))
		{
			sizeMap.get(value).set(0, fullRangeMax.get(0) -1);
			sizeMap.get(value).set(2, sizeMap.get(value).get(4) - fullRangeMax.get(0)-1);
		}		
		if (sizeMap.get(value).get(4)<fullRangeMax.get(4))
		{
			sizeMap.get(value).set(4, fullRangeMax.get(4)+1);
			sizeMap.get(value).set(2,fullRangeMax.get(4) +1 - sizeMap.get(value).get(0));
		}
	}
 
}
private  ArrayList<Integer> rangeMax(HashMap<Integer, ArrayList<ArrayList<Integer>>> wordInRange)
{
	 ArrayList<Integer> result = new ArrayList<Integer>();
	 if (wordInRange.size()>0)
	 {
		int lMax = 100000;
		int rMax = 0;
		int upMax = 100000;
		int dMax = 0;
		
		for (Integer key:wordInRange.keySet())
		{
			for (int i =0;i<wordInRange.get(key).size();i++)
			{
				if (lMax>wordInRange.get(key).get(i).get(0))
				{
					lMax = wordInRange.get(key).get(i).get(0);
				}
				if (rMax<wordInRange.get(key).get(i).get(4))
				{
					rMax= wordInRange.get(key).get(i).get(4);
				}
				if (upMax>wordInRange.get(key).get(i).get(1))
				{
					upMax=wordInRange.get(key).get(i).get(1);
				}
				if (dMax<wordInRange.get(key).get(i).get(5))
				{
					dMax=wordInRange.get(key).get(i).get(5);
				}
			}
		}
		if (lMax>=0&&upMax>=0&&rMax-lMax>0&&dMax-upMax>0&&rMax>0&&dMax>0)
		{
		result = new ArrayList<Integer>(Arrays.asList(lMax,upMax, rMax-lMax,dMax-upMax,rMax, dMax));
		}
	}
return result;
}


private HashMap<Integer, ArrayList<ArrayList<Integer>>> allWordInRangeByLine(	HashMap<Integer, ArrayList<Integer>> linesinRange, 
																				HashMap<String, Integer> column,
																				ArrayList<ArrayList<String>> templist,
																				int lMax, int rMax)
{
	HashMap<Integer, ArrayList<ArrayList<Integer>>> result = new HashMap<Integer, ArrayList<ArrayList<Integer>>>();
	for (int key: linesinRange.keySet())
	{
		int upKey =  linesinRange.get(key).get(1);
		int dKey =  linesinRange.get(key).get(5);
		for (int i=1;i<templist.size();i++)
		{
			int li = Integer.parseInt(templist.get(i).get(column.get("left")));
			int ri = Integer.parseInt(templist.get(i).get(column.get("left"))) + Integer.parseInt(templist.get(i).get(column.get("width")));
			int ui = Integer.parseInt(templist.get(i).get(column.get("top")));
			int di = Integer.parseInt(templist.get(i).get(column.get("top"))) + Integer.parseInt(templist.get(i).get(column.get("height")));

			if (li>lMax&&ri<rMax&&ui>=upKey&&di<=dKey&&
					templist.get(i).get(column.get("level")).equals("5")
					&&FrameTemplate.round(Double.valueOf(templist.get(i).get(column.get("conf"))), 0)!=95)
			{
					if (result.containsKey(key))
					{
						result.get(key).add(listToResultInvoice(i, column, templist));
					}else
					{
						result.put(key, new ArrayList<ArrayList<Integer>>(Arrays.asList(listToResultInvoice(i, column, templist))));						
					}
			}
		
		}
		if (!result.containsKey(key))
		{
			result.put(key, new ArrayList<ArrayList<Integer>>(Arrays.asList(new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0)))));	
		}
	}

	return result;
}

/**
 * Korekta zakresu zgodnie z ramkami
 * @param sizeMap - mapa wymiarów s³ów kluczowych
 * @param edges - mapa tabelek 
 * @param column - mapa nazwa kolumn w tabeli g³ównej
 * @param templist - tabela g³ówna
 */
private void sizeFrameCorrect(	HashMap<String, ArrayList<Integer>> sizeMap, 
							ArrayList<ArrayList<Integer>> edges,
							HashMap<String, Integer> column,
							ArrayList<ArrayList<String>> templist)
{
	
	///min i max to pionowy przedzia³ nag³ówka
	int A=0;
	int B=0;
	
	for (String key: sizeMap.keySet())
	{
		if (A==0||sizeMap.get(key).get(1)<A)
		{
			A=sizeMap.get(key).get(1);
		}
		if (B==0||sizeMap.get(key).get(5)>B)
		{
			B=sizeMap.get(key).get(5);
		}		
		
	}
	///lokalizacja s³owa, lokalizacja nastêpnego s³owa
	ArrayList<ArrayList<Integer>> alter = new ArrayList<ArrayList<Integer>>();
	if (B!=0&&A!=0)
	{
		for (int i=1;i<templist.size();i++)
		{
			int C = Integer.parseInt(templist.get(i).get(column.get("top")));
			int D = Integer.parseInt(templist.get(i).get(column.get("top"))) + Integer.parseInt(templist.get(i).get(column.get("height")));
			///1.zmienny zawiera siê w sta³ym, 2. Sta³y zawiera sie w zmiennym, 3. Górny sta³y zawiera siê w zmiennym 4.dolny róg zawiera sie w zmiennym  
			if ((C>=A&&D<=B)||(A>=C&&B<=D)||(C<A&&D>A)||(B<D&&B>C))
			{
				if (templist.get(i).get(column.get("level")).equals("5")
					&&FrameTemplate.round(Double.valueOf(templist.get(i).get(column.get("conf"))), 0)==95)
				{
					ArrayList<Integer> list = listToResultInvoice(i, column, templist);
					list.add(1);
					alter.add(list);
				}else if (templist.get(i).get(column.get("level")).equals("5"))
				{
					ArrayList<Integer> list = listToResultInvoice(i, column, templist);
					list.add(0);
					alter.add(list);
				}
			}	 
		}
	}

	
	for (String key:sizeMap.keySet())
	{
		//M - prawy górny róg
		int M = sizeMap.get(key).get(4);
		int max = 10000;
		int type = 0;
		//L - lewy górny róg
		int L = sizeMap.get(key).get(0);
		int min =0;
		int type0 = 0;
	
		for (int i=0;i<alter.size();i++)
		{
			/// - lewy górny róg zmiennej
			int AL = alter.get(i).get(0);
			if ((AL-M)>0&&AL<max)
			{
				type = alter.get(i).get(6);
				max = AL;
			}
			// - prawy górny róg zmiennej
			int AR = alter.get(i).get(4);
			if ((L-AR)>0&&AR>min)
			{
				type0 = alter.get(i).get(6);
				min = AR;
			}
			
		}
		
		if (type0==1&&min<L)
		{
			if (type==0)
			{
				sizeMap.get(key).set(2, sizeMap.get(key).get(4)-min);
			}
			sizeMap.get(key).set(0, min);
			sizeMap.get(key).set(4, min+sizeMap.get(key).get(2));
		}
		
		if (type==1 && max>M)
		{
			sizeMap.get(key).set(4, max);
			sizeMap.get(key).set(2, max-sizeMap.get(key).get(0));
			sizeMap.get(key).add(1);
		}else
		{
			sizeMap.get(key).add(0);
		}
	}
}

private HashMap<Integer, ArrayList<Integer>> linesInRange (int lMax, int rMax, int upMax,int dMax, HashMap<String, Integer> column, ArrayList<ArrayList<String>> templist)
{
	HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();
	int lp = 0;
	for (int i=1; i<templist.size();i++)
	{
		int left =Integer.parseInt(templist.get(i).get(column.get("left")));  
		int top = Integer.parseInt(templist.get(i).get(column.get("top")));
		int width =  Integer.parseInt(templist.get(i).get(column.get("width")));
		int height =  Integer.parseInt(templist.get(i).get(column.get("height")));
		///zamkniêcie w zakresie
		if (left>lMax&&left<rMax&&top>upMax&&top<dMax&&///top+height<(dMax+10)&&&&left+width<rMax
				templist.get(i).get(column.get("level")).equals("5")
				&&FrameTemplate.round(Double.valueOf(templist.get(i).get(column.get("conf"))), 0)!=95)
		{
			if (result.size()<1)
			{
				result.put(++lp, listToResultInvoice(i, column, templist));
			}else
			{
				boolean isNew = false;
				for (Integer key:result.keySet())
				{
					int upKey = result.get(key).get(1);
					int dKey = result.get(key).get(5);
					int lKey = result.get(key).get(0);
					int rKey = result.get(key).get(4);
					if ((top>=upKey&&top<=dKey)||((top+height)>=upKey&&(top+height)<=dKey)
						||(upKey>=top&&upKey<=(top+height))||(dKey>=top&&dKey<=(top+height)))
					{
						if (top<=upKey)
						{
							result.get(key).set(1, top);
							result.get(key).set(3, result.get(key).get(5)-top);
							///result.get(key).set(5, dKey);
							
						}
						if (top+height>=dKey)
						{
							result.get(key).set(5, top+height);
							result.get(key).set(3, top+height-result.get(key).get(1));
						}
						if (left<lKey)
						{
							result.get(key).set(0, left);
							result.get(key).set(2, result.get(key).get(4)-left); 
						}
						if (left+width>rKey)
						{
							result.get(key).set(4, left+width);
							result.get(key).set(2, left+width-result.get(key).get(0)); 
						}
						isNew = false;
						break;
					}else
					{
						isNew = true;
						//result.put(++lp, listToResultInvoice(i, column, templist));
						//break;
					}
					
					
				}
				if (isNew==true)
				{
					result.put(++lp, listToResultInvoice(i, column, templist));
				}	
			}
			
		}
	}
	
	
	
	return result;
}


private HashMap<String, ArrayList<Integer>> nextText(int i, HashMap<String, Integer> column, ArrayList<ArrayList<String>> templist, ArrayList<Integer> reference, ArrayList<ArrayList<Integer>> edges, boolean withFrame)
{
	int A = reference.get(1);///górny | róg 
	int B = reference.get(5);///dolny | róg
	int R = reference.get(4);///prawy --- róg
	int min = 10000;		
	int resPos = -1;
	HashMap<String, ArrayList<Integer>> resultMap = new HashMap<String, ArrayList<Integer>>();
	for (int q=1; q<templist.size(); q++)
	{
		if (templist.get(q).get(column.get("level")).equals("5")&&!templist.get(q).get(column.get("text")).trim().equals(""))
		{
			int C = Integer.parseInt(templist.get(q).get(column.get("top")));
			int D = Integer.parseInt(templist.get(q).get(column.get("top"))) + Integer.parseInt(templist.get(i).get(column.get("height")));
			///badanie tylko na tej samej linii
			
		//	if (q==114)
		//	{System.out.print("Dupa");}
			if (q!=i&&(A==C&&C==D)||(C>=A&&C<B) || (D>A&&D<=B) || (A>C&&A<D) || (B>C&&B<D))
			{
				////lewy --- róg
				int RB = Integer.parseInt(templist.get(q).get(column.get("left")));
				if ((RB-R)>0&&(RB-R)<min)
				{
					min = RB-R;
					resPos = q;
				}
			}
		}
	}
	
	if (withFrame==true&&edges.size()>0)
	{
		for (int q = 0; q<edges.size();q++)
			
			if (edges.get(q).get(0)==0&&(edges.get(q).get(1)-R)>0&&(edges.get(q).get(1)-R)<min&&((A>=edges.get(q).get(2)&&A<=edges.get(q).get(6))||(B>=edges.get(q).get(2)&&B<=edges.get(q).get(6)))) 
			{
				resPos=-1;
				break;
			}
	}
	
	
	if (resPos>-1)
	{
		resultMap.put(templist.get(resPos).get(column.get("text")), listToResultInvoice(resPos, column, templist) );
	}
	return resultMap;
	 
}



private HashMap<String, ArrayList<Integer>> underText(int i, HashMap<String, Integer> column, ArrayList<ArrayList<String>> templist, ArrayList<Integer> reference, ArrayList<ArrayList<Integer>> edges)
{
	int A = reference.get(0);///lewy --- róg 
	int B = reference.get(4);///prawy --- róg
	int R = reference.get(5);///dolny | róg
	int min = 10000;		
	int resPos = -1;
	String result = null;
	HashMap<String, ArrayList<Integer>> resultMap = new HashMap<String, ArrayList<Integer>>();
	for (int q=1; q<templist.size(); q++)
	{
		if (templist.get(q).get(column.get("level")).equals("5")&&!templist.get(q).get(column.get("text")).trim().equals(""))
		{	
			int C = Integer.parseInt(templist.get(q).get(column.get("left")));
			int D = Integer.parseInt(templist.get(q).get(column.get("left"))) + Integer.parseInt(templist.get(i).get(column.get("width")));
			///badanie tylko na tej samej linii
			////badanie wspólnej kolumny 
			if ((C>A&&C<B) || (D>A&&D<B) || (A>C&&A<D) || (B>C&&B<D))
			{
				////lewy | róg
				int RB = Integer.parseInt(templist.get(q).get(column.get("top")));
				if ((RB-R)>0&&(RB-R)<min)
				{
					min = RB-R;
					resPos = q;
				}
			}
		}
	}
	
	if (edges.size()>0)
	{
		for (int q = 0; q<edges.size();q++)
			
			if (edges.get(q).get(0)==1&&(edges.get(q).get(2)-R)>0&&(edges.get(q).get(2)-R)<min) 
			{
				resPos=-1;
				break;
			}
	}
	
	
	if (resPos>-1)
	{
		resultMap.put(templist.get(resPos).get(column.get("text")), listToResultInvoice(resPos, column, templist) );
	}
	return resultMap;
}

private HashMap<String, ArrayList<Integer>> upText(HashMap<String, Integer> column, 
													ArrayList<ArrayList<String>> templist, 
													ArrayList<Integer> reference,
													ArrayList<ArrayList<Integer>> edges,
													boolean frame)
{
	HashMap<String, ArrayList<Integer>> result = new HashMap<String, ArrayList<Integer>>();
	int A = reference.get(0); 
	int B = reference.get(4); 
	int U = reference.get(1); 
	int Z = reference.get(5);
	ArrayList<ArrayList<Integer>> word = new ArrayList<ArrayList<Integer>>();
	HashMap<Integer, ArrayList<ArrayList<Integer>>> wordMap = new  HashMap<Integer, ArrayList<ArrayList<Integer>>>();
	for (int q =1; q<templist.size();q++)
	{
		if(templist.get(q).get(column.get("level")).equals("5")&&!templist.get(q).get(column.get("text")).trim().equals(""))
		{
			int C = Integer.parseInt(templist.get(q).get(column.get("left")));
			int D = Integer.parseInt(templist.get(q).get(column.get("left"))) +Integer.parseInt(templist.get(q).get(column.get("width")));
			int R =Integer.parseInt(templist.get(q).get(column.get("top")));
			if(U-R>0&&((C>=A&&C<B)||(D>=A&&D<=B)||(A>=C&&A<=D)))
			{
				 
				wordMap.put(q, new ArrayList<ArrayList<Integer>>(Arrays.asList(listToResultInvoice(q,column,templist))));
			}
		}
	}
	 ArrayList<Integer> range = rangeMax(wordMap);
	 HashMap<Integer, ArrayList<Integer>> linesinRange = linesInRange(range.get(0) -1, range.get(4) +1, range.get(1)- 1,range.get(5)+1,  column,  templist);
	 HashMap<Integer, ArrayList<ArrayList<Integer>>> allWordInRange = allWordInRangeByLine(linesinRange, column, templist,range.get(0) -1,  range.get(4) +1);
	 int minLeft = 0;
	 int minTop = 0;
	 int line = 0;
	 int up=0;
	 for (int i:allWordInRange.keySet())
	 {
		 for (int k=0; k<allWordInRange.get(i).size();k++)
		 {
			 if (minTop==0||(U-allWordInRange.get(i).get(k).get(1)<minTop))
			 {
				 minTop = U-allWordInRange.get(i).get(k).get(1);
				 line = i;
			 }
		 }
	 }
	 for (int k=0;k<allWordInRange.get(line).size();k++)
	 {
		 if (minLeft==0||(Math.abs(A-allWordInRange.get(line).get(k).get(0))<minLeft))
		 {
			 minLeft= Math.abs(A-allWordInRange.get(line).get(k).get(0));
			 result.put(
					 	templist.get(getLocationOnTemplist(column,templist, allWordInRange.get(line).get(k))).get(column.get("text")), allWordInRange.get(line).get(k));
			 up=allWordInRange.get(line).get(k).get(1);
		 }
	 }
	 
	 if (frame==true)
	 {
		 for (int i=0;i<edges.size();i++)
		 {
			 if (edges.get(i).get(0)==1&&edges.get(i).get(2)>up&&edges.get(i).get(6)<U&&A>=edges.get(i).get(1)&&A<=edges.get(i).get(6))
			 {
				 result = null;
			 }
		 }
	 }
	 
	return result;

}

private ArrayList<Integer> listToResultInvoice(int i, HashMap<String, Integer> column, ArrayList<ArrayList<String>> templist)
{	//  ---lewa góra, | lewa góra, --- d³, | wysokoœæ, --- prawa góra, | dolny lewy    
	return new ArrayList<Integer>(Arrays.asList(
			  Integer.parseInt(templist.get(i).get(column.get("left"))),  /// 0
			  Integer.parseInt(templist.get(i).get(column.get("top"))),  /// 1
			  Integer.parseInt(templist.get(i).get(column.get("width"))), ///2
			  Integer.parseInt(templist.get(i).get(column.get("height"))),  ////3
			  Integer.parseInt(templist.get(i).get(column.get("left"))) + Integer.parseInt(templist.get(i).get(column.get("width"))),  ///4
			  Integer.parseInt(templist.get(i).get(column.get("top"))) + Integer.parseInt(templist.get(i).get(column.get("height")))  ////5
			  ));
}

private boolean regEx(String patS, String matS)
{
	Pattern pat = Pattern.compile(patS);
	Matcher mat = pat.matcher(matS.toUpperCase());
	if (mat.find())
		{return true;}
	return false;
}

private ArrayList<ArrayList<Integer>> detectFrame(HashMap<String, Integer> column, ArrayList<ArrayList<String>> templist)
{
	int left= -1;
	int top =-1;
	int width = -1;
	int height = -1;
	ArrayList<ArrayList<Integer>> edges = new ArrayList<ArrayList<Integer>>();
	for (int i=1; i<templist.size();i++)
	{
		if (templist.get(i).get(column.get("level")).equals("4"))
		{
			left= Integer.parseInt(templist.get(i).get(column.get("left")));
			top= Integer.parseInt(templist.get(i).get(column.get("top")));
			width= Integer.parseInt(templist.get(i).get(column.get("width")));
			height= Integer.parseInt(templist.get(i).get(column.get("height")));
		} else if ((templist.get(i).get(column.get("level")).equals("5")
				&&Integer.parseInt(templist.get(i).get(column.get("left")))==left
				&&Integer.parseInt(templist.get(i).get(column.get("top")))==top
				&&Integer.parseInt(templist.get(i).get(column.get("width")))==width
				&&Integer.parseInt(templist.get(i).get(column.get("height")))==height
			 	&&FrameTemplate.round(Double.valueOf(templist.get(i).get(column.get("conf"))), 0)==95
				)||
				(templist.get(i).get(column.get("level")).equals("1"))
				)
		{	///0 na pionowo (bo mo¿na spaœæ mniej i liczyæ siê mniej ni¿ zero) |, 1 na poziomo ------
			edges.add(new ArrayList<Integer>(	Arrays.asList(Integer.parseInt(templist.get(i).get(column.get("height")))<Integer.parseInt(templist.get(i).get(column.get("width")))? 1 : 0,///0
															  Integer.parseInt(templist.get(i).get(column.get("left"))), ///1
															  Integer.parseInt(templist.get(i).get(column.get("top"))), ////2
															  Integer.parseInt(templist.get(i).get(column.get("width"))), ///3
															  Integer.parseInt(templist.get(i).get(column.get("height"))), ////4
															  Integer.parseInt(templist.get(i).get(column.get("left"))) + Integer.parseInt(templist.get(i).get(column.get("width"))), ///5
															  Integer.parseInt(templist.get(i).get(column.get("top"))) + Integer.parseInt(templist.get(i).get(column.get("height"))) ////6
					)));
 
		}
	}
	//System.out.print("test");
	return edges;
}

 


private ArrayList<ArrayList<String>> importFlie()
{
	DataImport imp = new DataImport();
	String fileName = imp.setFileName(workPath);

	ArrayList<ArrayList<String>> templist = null;
	if (fileName!=null&&fileName.substring(fileName.length()-3).toUpperCase().equals("PDF"))
	{
		int x = fileName.lastIndexOf("\\");
		String file = fileName.substring(x+1, fileName.length());
		String path = fileName.substring(0,  x);
		File directory = new File(path + "\\" + file + ".txt");
		if (directory.exists())
		{directory.delete();}
	//	if (new PythonOther().runTesseract(file, path))
//		{
	//		 templist = new FlatFile().ImportFlatFile(false, path + "\\" + file + ".txt", "\\t"); 
	//	}
	}
	return templist;
}


private String getFlie()
{
	DataImport imp = new DataImport();
	String fileName = imp.setFileName(workPath);


	return fileName;
}
private HashMap<String, HashMap<String, ArrayList<String>>> tableRegExDetail()
{
	 HashMap<String, HashMap<String, ArrayList<String>>> result = new  HashMap<String, HashMap<String, ArrayList<String>>>();
	 ArrayList<ArrayList<String>> temp = new StoredProcedures().genUniversalArray("getTesseractPar", new ArrayList<String>());
	 HashMap<String, Integer> colA = new FrameTemplate().getColumnNumbers(temp.get(0));
		for (int i = 1;i<temp.size();i++)
		{
			if (temp.get(i).get(colA.get("Active")).equals("True"))
			{
				if (result.containsKey(temp.get(i).get(colA.get("TTYPE")))) 
				{
					if (result.get(temp.get(i).get(colA.get("TTYPE"))).containsKey(temp.get(i).get(colA.get("GroupName"))))
					{
						result.get(temp.get(i).get(colA.get("TTYPE"))).get(temp.get(i).get(colA.get("GroupName"))).add(temp.get(i).get(colA.get("REGEX")));
					}else 
					{
					result.get(temp.get(i).get(colA.get("TTYPE"))).put(temp.get(i).get(colA.get("GroupName")), new ArrayList<String>(Arrays.asList(temp.get(i).get(colA.get("REGEX")))));
					}
					
				}else 
				{
				 
					result.put(temp.get(i).get(colA.get("TTYPE")), new HashMap<String, ArrayList<String>>());
					result.get(temp.get(i).get(colA.get("TTYPE"))).put(temp.get(i).get(colA.get("GroupName")), new ArrayList<String>(Arrays.asList(temp.get(i).get(colA.get("REGEX")))));
					
				}
				
			}
		}
return result;
}

private HashMap<String, ArrayList<String>> getTableRegEx()
{
	HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
	ArrayList<ArrayList<String>> temp = new StoredProcedures().genUniversalArray("getTesseractTable", new ArrayList<String>());
	HashMap<String, Integer> colA = new FrameTemplate().getColumnNumbers(temp.get(0));
	for (int i = 1;i<temp.size();i++)
	{
		if (temp.get(i).get(colA.get("Active")).equals("True"))
		{
			if ( result.containsKey(temp.get(i).get(colA.get("GroupName"))))
			{
				////looooog
			}else
			{
				result.put(temp.get(i).get(colA.get("GroupName")), new ArrayList<String>(Arrays.asList(
																										temp.get(i).get(colA.get("T_LP")),
																										temp.get(i).get(colA.get("T_DESCRIPTIONS")),
																										temp.get(i).get(colA.get("T_QUANTITY")),
																										temp.get(i).get(colA.get("T_UnitPrice")),
																										temp.get(i).get(colA.get("T_VAT"))
																										)));
			}
		}		
	}
	return result;

}
}
