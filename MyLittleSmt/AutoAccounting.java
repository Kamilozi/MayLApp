package MyLittleSmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

import MyLittleSmtFrame.IPostings;

public class AutoAccounting {
	private HashMap<String, String> codeMap = new HashMap<String, String>();
	private IPostings postring;
	public AutoAccounting(String firm, String postingId, String instrumentID, String oType)
	{
		//id-g³++, firma-g³++, Lp tutaj, data dokumentu, data alternatywna i data rozliczenia = registerDate, Opis = ord, Konto z codeMap,
		//iloœæ- ord, ct-auto, jedn-ord, cena-ord, projekt - g³, wymiar2 = ord, wymiar3- ord, wymiar4- ins, sub g³, otyp-g³, book=1
		getCodeMap(firm);
		postring = new IPostings(0, 0, "ksiêgowanie automatyczne", 
				postingId, firm, oType, true);
		
	}
	public AutoAccounting(String firm, String postingId,  String oType)
	{
		//id-g³++, firma-g³++, Lp tutaj, data dokumentu, data alternatywna i data rozliczenia = registerDate, Opis = ord, Konto z codeMap,
		//iloœæ- ord, ct-auto, jedn-ord, cena-ord, projekt - g³, wymiar2 = ord, wymiar3- ord, wymiar4- ins, sub g³, otyp-g³, book=1
		getCodeMap(firm);
		postring = new IPostings(0, 0, "ksiêgowanie automatyczne", 
				postingId, firm, oType, true);
		
	}
	public void postingDispose()
	{
		postring.dispose();
	}
	
	public Object[] getPostingList(boolean isQtyModel)
	{
		
		DefaultTableModel model = (isQtyModel==true ? postring.getQtyModel() : postring.getLedModel());
		Object[] columnMap =new Object[model.getColumnCount()];
		for (int i =0; i<model.getColumnCount();i++)
		{
			columnMap[i] = model.getColumnName(i);
		}
		return columnMap;
	}
	public void addAutoRowsToModel(ArrayList<Object[]>list, boolean isQty)
	{
		postring.addAutoRowsToModel(list,isQty);
		//postring.setVisible(true);
	}
	public void addAutoRowsToModel(ArrayList<Object[]>list, boolean isQty, boolean book)
	{
		postring.addAutoRowsToModel(list,isQty, book);
		//postring.setVisible(true);
	}	
	public void autoOrderAdd(String firm)
	{
		ArrayList<ArrayList<String>> planAccountCode= new StoredProcedures().genUniversalArray("getPlanAccountCodesForGroup",new ArrayList<String>(Arrays.asList(MainEntryDataWarehouse.getFirmsMap().get(firm).get(1))));
	}
	
	public void getCodeMap(String firm)
	{
		ArrayList<ArrayList<String>> planAccountCode= new StoredProcedures().genUniversalArray("getPlanAccountCodesForGroup",new ArrayList<String>(Arrays.asList(MainEntryDataWarehouse.getFirmsMap().get(firm).get(1))));
		if (planAccountCode.size()>0)
		{
			for (int i=1;i<planAccountCode.size();i++)
			{
				codeMap.put(planAccountCode.get(i).get(1), planAccountCode.get(i).get(2).trim());
			}
		}
	}
	
	public HashMap<String, String> getCodeMap()
	{
		return codeMap;
	}

}
