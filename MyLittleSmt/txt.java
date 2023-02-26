package MyLittleSmt;

import java.util.ArrayList;
///-1. plik pythona wyrzuca dodatkowy pusty wiersz
public class txt {
	//ArrayLista do listy z pominieciem pierwszego wiersza
	public String[] ArrayListToStringOne(ArrayList<String> inlist)
	{
		String[] outlist = new String[inlist.size()];
		for (int i = 0; i<inlist.size(); i++)
		{
			outlist[i] = inlist.get(i);
			
		}
		return outlist;
	}
	
	public String[][] ArrayListToString(ArrayList<ArrayList<String>> inlist)
	{
		
		String[][] outlist = new String[inlist.size()][inlist.get(0).size()];
		for (int q=0; q<inlist.size(); q++)
		{
			String[] tempstr = new String[inlist.get(0).size()];
			for (int i=0; i<inlist.get(q).size(); i++)
			{
				tempstr[i]=inlist.get(q).get(i);
			}
			outlist[q] = tempstr;
		}
		
		
		return outlist;
		
	}
	public Object[][] ArrayListToObject(ArrayList<ArrayList<String>> inlist)
	{
		int size = inlist.size();
		Object[][] outlist = new Object[size][inlist.get(0).size()];
		for (int i = 0; i<size; i++)
		{
			Object[] tempobj = new Object[inlist.get(0).size()];
			for (int q = 0; q<inlist.get(0).size(); q++)
			{
				tempobj[q]=inlist.get(i).get(q);
			}
			outlist[i] = tempobj;
		}
		return outlist;
	}


 }
