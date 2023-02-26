package MyLittleSmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

public class MainEntryDataWarehouse {
private static ArrayList<ArrayList<String>> Calendar;
private static ArrayList<ArrayList<String>> Currency;
private static HashMap<String, ArrayList<String>> firmsMap; //firma, <Waluta, Plan Kont >

	public static ArrayList<ArrayList<String>> getCurrency()
	{
		return Currency;
	}
	
	public static void setCurrency(ArrayList<ArrayList<String>> Currency)
	{
		MainEntryDataWarehouse.Currency=Currency;
	}	
	public ArrayList<ArrayList<String>> getCalendar()
	{
		return Calendar;
	}
	public void setCalendar(ArrayList<ArrayList<String>> Calendar)
	{
		MainEntryDataWarehouse.Calendar=Calendar;
	}	
	
	public static void firmMapFromModel(DefaultTableModel model)
	{
		 firmsMap = new HashMap<String, ArrayList<String>>();
		 for (int i=0;i<model.getRowCount();i++)
		 {
			 firmsMap.put(model.getValueAt(i, 0).toString(), new ArrayList<String>(Arrays.asList(model.getValueAt(i, 11).toString(), model.getValueAt(i, 12).toString())));			 
		 }
	}
	
	public static HashMap<String, ArrayList<String>> getFirmsMap()
	{
		return firmsMap;
	}
	
}