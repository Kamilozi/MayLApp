package MyLittleSmt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BankAccount {
	private static String FilePath = "C:\\Java_tech\\File\\THM_GROUP_122021_test.csv";
	public static ArrayList<String[]> BankStat;
	public static String[][] BankStat_Str;
	public static void main (String[] args) throws FileNotFoundException, IOException
	
	{
		FlatFile flat = new FlatFile();
		//flat.ImportFlat();
		
		String[] temp = flat.IFtoString();

		Pattern re = Pattern.compile("(.)*mBank S.A. Bankowoœæ Detaliczna(.)*");
		Matcher ma = re.matcher(temp[0]);
		boolean mat = ma.find();
		if (mat==true)
		{
			BankStat = new ArrayList<String[]>();
			BankStat_Str = new String[temp.length - 4][];
			for (int i = 37 ; i<temp.length - 4 ; i++)
			{
				
				String[] array_temp = SplitFlatLine(temp[i]);
				BankStat.add(array_temp);
				BankStat_Str[i-37] = array_temp;
				
				
			}

		}
		

	}

	private static String[] SplitFlatLine(String str)
	{
		String[] re_temp = str.split(";"); 
		for (String val : re_temp)
		{
			//System.out.print(val);
		}
		return re_temp;
		
	}
	

	
	
	
}
