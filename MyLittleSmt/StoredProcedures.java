package MyLittleSmt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

public class StoredProcedures {
	private String querypath="C:\\MyLittleSmt\\SQL_Procedures";
	private ArrayList<ArrayList<String>> ListFromBase;

	/**
	 * Modyfikator dostêpu do pe³nej tabeli
	 * @return - zwraca niezmodyfikowan¹ tabelê
	 */
	public ArrayList<ArrayList<String>> getListFromBase()
	{
		return ListFromBase;
	}
	/**
	 * Generacja uniwersalnego modelu
	 * @param procedureName - nazwa procedury
	 * @param procedureParameters - parametry procedury
	 * @param Enable - typ dostêpu
	 * @param mapsys - mapa pomocnicza
	 * @param mapsysall - mapa pomocnicza
	 * @return Model uniwersalny 
	 */
	
	public DefaultTableModel genUniversalModel(String procedureName, ArrayList<String> procedureParameters, int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{
		
		ListFromBase = yamlGenerator(querypath, genQuery(procedureName, procedureParameters));
		return genModel(ListFromBase, Enable, mapsys, mapsysall);
	}
	public DefaultTableModel genUniversalModel(boolean isCheck, String procedureName, ArrayList<String> procedureParameters, int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{
 
		
		ListFromBase = yamlGenerator(querypath, genQuery(procedureName, procedureParameters));
		if (isCheck==true)
		{
			ListFromBase.get(0).add(0, "Check");
			for (int i =1; i<ListFromBase.size();i++)
			{
				ListFromBase.get(i).add(0, "0");
			}
		}
		return genModel(ListFromBase, Enable, mapsys, mapsysall);
	}
	
	public String genQuery(String procedureName, ArrayList<String> procedureParameters)
	{
		StringBuilder query = new StringBuilder();
		query.append("EXEC " + procedureName + "  ");
		//dataquery.deleteCharAt(dataquery.length() - 1);
		for (int i=0;i<procedureParameters.size();i++)
		{
			if(procedureParameters.get(i)!=null)
			{
				//if (procedureParameters.get(i).toString().indexOf(".")>0)
					//{
					 query.append("'" + procedureParameters.get(i).toString() + "',");
					//}else 
					//{
					//	query.append(procedureParameters.get(i).toString() + ",");
					//}
				
			}
		}
		query.deleteCharAt(query.length() - 1);
		query.append(";");
		return query.toString();
	}
	
	public ArrayList<ArrayList<String>> genUniversalArray(String procedureName, ArrayList<String> procedureParameters)
	{
		StringBuilder query = new StringBuilder();
		query.append("EXEC " + procedureName + "  ");
		//dataquery.deleteCharAt(dataquery.length() - 1);
		for (int i=0;i<procedureParameters.size();i++)
		{
			if(procedureParameters.get(i)!=null)
			{
				query.append("'" + procedureParameters.get(i).replace("\\", "\\\\") + "',");
			}
		}
		query.deleteCharAt(query.length() - 1);
		query.append(";");
		
		ArrayList<ArrayList<String>> temp = yamlGenerator(querypath, query.toString());
		
		//"EXEC NumberOfDaysNBP @startDate='" + startDate + "', @endDate='" + endDate + "';";
		return  yamlGenerator(querypath, query.toString());
	}
	
	/**
	 * Generacja modelu do tabli Main formatki Conterparties
	 * @param ID
	 * @param Enable
	 * @param mapsys
	 * @param mapsysall
	 * @return
	 */
	public DefaultTableModel genModelConterpartiesMainTable(int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{
		return genModel(ConterpartiesMainTable(), Enable, mapsys, mapsysall);
	}
	
	
	public DefaultTableModel genModelConterparties(String ID, int Enable, 
													HashMap<String, String> mapsys, 
													HashMap<String, ArrayList<String>> mapsysall)
	{
		 return genModel(ConterpartiesByID(ID), Enable, mapsys, mapsysall);
	}

	public DefaultTableModel genModelInstruments(int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{
	return genModel(InstrumentsMainTable(), Enable, mapsys, mapsysall);
	}
	//
	public DefaultTableModel genModelOrders(String ID, String Firm, int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{
		
	return genModel(OrdersByID(ID, Firm), Enable, mapsys, mapsysall);
	}
	//
	public DefaultTableModel genModelOrdersAll(String Firm, int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{	
	return genModel(OrdersAll(Firm), Enable, mapsys, mapsysall);
	}
	//
	public DefaultTableModel genModelFirmsAll(int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{	
	return genModel( getCompany(), Enable, mapsys, mapsysall);
	}
//	
	public DefaultTableModel genModelOrdersToMain(String ID, String Firm, int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{
		
	return genModel(getOrdersToMain(ID, Firm), Enable, mapsys, mapsysall);
	}
	
	public DefaultTableModel genModelPostingsLedgerTrans(String ID, String Firm, int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{
		
	return genModel(getLedgerTransPosting(ID, Firm), Enable, mapsys, mapsysall);
	}
	public DefaultTableModel genModelPostingsLedgerQtyTrans(String ID, String Firm, int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{
		
	return genModel(getLedgerQtyTransPosting(ID, Firm), Enable, mapsys, mapsysall);
	}
	
	public DefaultTableModel genModelPositions(String Firm, String oType, int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{
		
	return genModel(getPositions(Firm, oType), Enable, mapsys, mapsysall);
	}
	public DefaultTableModel genModelCodeToInstr(String ID, String Firm, int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{
		
	return genModel(getCodeToInstr(ID, Firm), Enable, mapsys, mapsysall);
	}
	
	public DefaultTableModel genModelMailToInstr(String ID, String Firm, int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{
		
	return genModel(getMailToInstr(ID, Firm), Enable, mapsys, mapsysall);
	}
	
	//
	
	public DefaultTableModel genModelConterpartiesContact(String ID, int Enable, 
			HashMap<String, String> mapsys, 
			HashMap<String, ArrayList<String>> mapsysall)
	{
	return genModel(ConterpartiesContactByID(ID), Enable, mapsys, mapsysall);
	}

 
	
	
	public DefaultTableModel genModel(ArrayList<ArrayList<String>> templist,
										int Enable, 
										HashMap<String, String> mapsys, 
										HashMap<String, ArrayList<String>> mapsysall)
	{
		FrameTemplate frametemplate = new FrameTemplate();
		ListFromBase =new ArrayList<ArrayList<String>>(templist);
		txt txtlist = new txt();
			//ArrayList<Integer> remList = new ArrayList<Integer>();
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
	
	public ArrayList<ArrayList<String>> NumberOfDaysNBP(String startDate, String endDate)
	{
		String  query = "EXEC NumberOfDaysNBP @startDate='" + startDate + "', @endDate='" + endDate + "';";
		return yamlGenerator(querypath, query);
	}
	
	public ArrayList<ArrayList<String>> ConterpartiesMaxID()
	{
		String  query = "Exec ConterpartyID;";
		return yamlGenerator(querypath, query);
	}
	
	public ArrayList<ArrayList<String>> ContactMaxID()
	{
		String  query = "Exec ContactMaxID;";
		return yamlGenerator(querypath, query);
	}	
	
	public ArrayList<ArrayList<String>> InstrumentMaxID(String date)
	{
		String  query = "Exec  InstrumentMaxID " + date + ";";
		return yamlGenerator(querypath, query);
	}	
	
	
	
	
	public ArrayList<ArrayList<String>> PostingMaxID(String date, String firm)
	{
		String  query = "Exec  kaPostringsMaxID '" +  firm + "', '" + date + "';";
		return yamlGenerator(querypath, query);
	}	
	
	public ArrayList<ArrayList<String>> ConterpartiesByID(String ID)
	{
		String  query = "Exec getConterpartyById '"+ ID +"';";
		return yamlGenerator(querypath, query);
	}	
	
	public ArrayList<ArrayList<String>> ConterpartiesContactByID(String ID)
	{
		String  query = "Exec IConterparty_Contact '"+ ID +"';";
		return yamlGenerator(querypath, query);
	}
	
	public ArrayList<ArrayList<String>> ConterpartiesMainTable()
	{
		String  query = "EXEC IConterparty_Main;";
		return yamlGenerator(querypath, query);
	}	
	public ArrayList<ArrayList<String>> InstrumentsMainTable()
	{
		String  query = "EXEC getInstruments ;";
		return yamlGenerator(querypath, query);
	}	
	public ArrayList<ArrayList<String>> OrdersByID(String ID, String Firm)
	{
		String  query = "Exec getOrders '"+ ID + "', '" + Firm + "';";
		return yamlGenerator(querypath, query);
	}
	public ArrayList<ArrayList<String>> OrdersAll( String Firm)
	{
		String  query = "Exec getOrdersAll '" + Firm + "';";
		return yamlGenerator(querypath, query);
	}
	
	public ArrayList<ArrayList<String>> getCurrencyToPosting(String date, String baseCurrency, String currency)
	{
		String  query = "Exec getCurrencyToPosting '" + date + "', '" + baseCurrency + "', '" + currency + "';";
		return yamlGenerator(querypath, query);
	}
	
	public ArrayList<ArrayList<String>> getOrdersToMain(String ID, String Firm)
	{
		String  query = "Exec getOrdersToMain '"+ ID + "', '" + Firm + "';";
		return yamlGenerator(querypath, query);
	}
	public ArrayList<ArrayList<String>> getCompany()
	{
		String  query = "Exec getCompany;";
		return yamlGenerator(querypath, query);
	}
	public ArrayList<ArrayList<String>> getLedgerTransPosting(String ID, String Firm)
	{
		String  query = "Exec getLedgerTransPosting '"+ ID + "', '" + Firm + "';";
		return yamlGenerator(querypath, query);
	}	
	public ArrayList<ArrayList<String>> getLedgerQtyTransPosting(String ID, String Firm)
	{
		String  query = "Exec getLedgerQtyTransPosting '"+ ID + "', '" + Firm + "';";
		return yamlGenerator(querypath, query);
	}	
	
	public ArrayList<ArrayList<String>> getPositions(String Firm, String oType)
	{
		String  query = "Exec  getPositions '"+  Firm + "', '" + oType + "';";
		return yamlGenerator(querypath, query);
	}
	public ArrayList<ArrayList<String>> getCodeToInstr(String ID, String Firm)
	{
		String  query = "Exec getCodeToInstr '"+ ID + "', '" + Firm + "';";
		return yamlGenerator(querypath, query);
	}
	
	public ArrayList<ArrayList<String>> getMailToInstr(String ID, String Firm)
	{
		String  query = "Exec getMailToInstr '"+ ID + "', '" + Firm + "';";
		return yamlGenerator(querypath, query);
	}
	
	public ArrayList<ArrayList<String>> getAlterMailToInstr(String Counter)
	{
		String  query = "Exec getAlterMailToInstr '"+ Counter + "';";
		return yamlGenerator(querypath, query);
	}
	
	private ArrayList<ArrayList<String>> yamlGenerator(String path, String queryToExec)
	{
		PythonBase reader = new PythonBase();
		String realYamlPath = reader.getYamlPath();
		
		
		int hashcode=  this.hashCode();
		SimpleDateFormat formatter= new SimpleDateFormat("yyMMddHHmmss");
		Date date = new Date(System.currentTimeMillis());
		String filename= String.valueOf(hashcode) + formatter.format(date) + ".yml";
		String filepath = path + "\\" + filename;
		File myObj = new File(filepath);
			try {
				FileWriter myWriter = new FileWriter(filepath);
				myWriter.write("query : \"" + queryToExec + "\"\n" );
				myWriter.close();
				reader.setYmlPath(querypath);
				ArrayList<ArrayList<String>> tempist = reader.FromBase(false,  filename);
				(new File(filepath)).delete();
				reader.setYmlPath(realYamlPath);
				return tempist;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			reader.setYmlPath(realYamlPath);
			return null;
		
		
	}
	
	
}
