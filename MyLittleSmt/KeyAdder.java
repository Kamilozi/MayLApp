package MyLittleSmt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.DefaultTableModel;

public class KeyAdder {
private static Integer ContepartyID;
private static Integer reportGroupID;
private static Integer reportID;
private static Integer BankSettingID;
private static Integer ContactID;
private static Integer InstrumentId;
private static Integer BankAccountID;
private static HashMap<String, ArrayList<String>>  postingHashMapId = new HashMap<String, ArrayList<String>>();
private static HashMap<String, ArrayList<String>>  invoiceHashMapId = new HashMap<String, ArrayList<String>>();
private static HashMap<String, ArrayList<String>>  bankHashMapId = new HashMap<String, ArrayList<String>>();
private static HashMap<String, ArrayList<String>>  statSourceHashMapId = new HashMap<String, ArrayList<String>>();
	public String Conterparties()
	{
		ContepartyID =  multiKey(simpleReg("\\d\\d\\d\\d\\d\\d\\d", new StoredProcedures().genUniversalArray("ConterpartyID", new ArrayList<String>()).get(1).get(0)), ContepartyID);///ConterpartiesMaxID()
		String formatted = String.format("%07d", ContepartyID);
		return "I" + formatted;
	}
	
	public String reportsGroups()
	{
		reportGroupID =  multiKey(simpleReg("\\d\\d\\d\\d\\d\\d\\d", new StoredProcedures().genUniversalArray("reportGroupID", new ArrayList<String>()).get(1).get(0)), reportGroupID);///ConterpartiesMaxID()
		String formatted = String.format("%07d", reportGroupID);
		return "D" + formatted;
	}	
	public String reports()
	{
		reportGroupID =  multiKey(simpleReg("\\d\\d\\d\\d\\d\\d\\d", new StoredProcedures().genUniversalArray("getReportID", new ArrayList<String>()).get(1).get(0)), reportID);///ConterpartiesMaxID()
		String formatted = String.format("%07d", reportGroupID);
		return "E" + formatted;
	}		
	public String BankSettings()
	{
		BankSettingID =  multiKey(simpleReg("\\d\\d\\d\\d\\d\\d\\d", new StoredProcedures().genUniversalArray("getBankSettingsbyMaxID", new ArrayList<String>()).get(1).get(0)), ContepartyID);
		String formatted = String.format("%07d", BankSettingID);
		return "R" + formatted;
	}	
	public String ContactId()
	{
		//String formatted = String.format("%07d", simpleReg("\\d\\d\\d\\d\\d\\d\\d", new StoredProcedures().ContactMaxID().get(1).get(0)));
		ContactID = multiKey(simpleReg("\\d\\d\\d\\d\\d\\d\\d", new StoredProcedures().ContactMaxID().get(1).get(0)),ContactID);
		String formatted = String.format("%07d", ContactID);
		return "C" + formatted;
	}
	
	public String BankAccountId()
	{
		BankAccountID =  multiKey(simpleReg("\\d\\d\\d\\d\\d\\d\\d", new StoredProcedures().genUniversalArray("getMaxBankAccountId", new ArrayList<String>()) .get(1).get(0)), BankAccountID);
		String formatted = String.format("%07d", BankAccountID);
		return "A" + formatted;
	}	
	public String InstrumentId()
	{

		String date = new SimpleDateFormat("yyMM").format(new Date());
		String InstrumentMax= new StoredProcedures().InstrumentMaxID("Z" + date).get(1).get(0);
		InstrumentId = multiKey(simpleReg("\\d\\d\\d\\d\\d", InstrumentMax.substring(5)),InstrumentId);
		String formatted = String.format("%05d", InstrumentId);
 		//	InstrumentId = ;
		return "Z" + date + formatted;
		
	}
	
	public String invoiceID(String sDate, String firm, String oType) throws ParseException
	{
		//tworzê date z podanej wartoœci
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date inDate = formatter.parse(sDate);
		String date = new SimpleDateFormat("/MM/yy").format(inDate);
		ArrayList<ArrayList<String>> postingMaxArray=  new StoredProcedures().genUniversalArray("getInvoiceMaxID", new ArrayList<String>(Arrays.asList(date, oType=="PROFOR" ? "P/" : "F/", oType, firm)));
		int intInvoiceId;
		if (postingMaxArray.size()<=1)
		{
			intInvoiceId  = 1;
		}else
		{
			intInvoiceId  = Integer.valueOf(postingMaxArray.get(1).get(0)) + 1 ;
		}//invoiceHashMapId
		
		//int intInvoiceId = multiKey(simpleReg("\\d\\d\\d\\d\\d", InvoiceId.substring(5)),Integer.valueOf(InvoiceId.substring(5)));
		String formatted = String.format("%04d", intInvoiceId);
		String returnID = (oType=="PROFOR" ? "P/" : "F/") + formatted + date ;
		return returnID;
		
	}
	
	public String PostingID(String sDate, String firm) throws ParseException
	{
		//tworzê date z podanej wartoœci
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date inDate = formatter.parse(sDate);
		String date = new SimpleDateFormat("yyMM").format(inDate);
		//PostingMaxId œci¹gam aktualnie najwy¿sz¹ wartoœæ z bazy
		ArrayList<ArrayList<String>> postingMaxArray=  new StoredProcedures().PostingMaxID("K" + date, firm);
		//String PostingMaxId = new StoredProcedures().PostingMaxID("K" + date, firm).get(1).get(0);
		String PostingMaxId;
		if (postingMaxArray.size()<=1)
		{
			PostingMaxId = "K" + date + "00000";
		}else
		{
			PostingMaxId =postingMaxArray.get(1).get(1);
		}
		// jeœli nie mam ksiêgowañ, aby zapobiec pustce tworzê nowy sztuczny ID
	//	if (PostingMaxId==null)

		//sprawdzam czy istnieje dla firmy jakiœ wczeœniej przydzielony posting
		String PostingId=null;
		int j = -1;
		if (postingHashMapId.containsKey(firm))
		{
			for (int i=0; i<postingHashMapId.get(firm).size();i++)
			{
				if (postingHashMapId.get(firm).get(i).substring(0, 5).equals("K" + date))
				{
					PostingId = postingHashMapId.get(firm).get(i);
					j = i;
					break;
				}else
				{
					PostingId = "K" + date + "00000";
				}
			}
		}
		else
		{
			PostingId = "K" + date + "00000";
		}
		
		
		int intPostingId = multiKey(simpleReg("\\d\\d\\d\\d\\d", PostingMaxId.substring(5)),Integer.valueOf(PostingId.substring(5)));
		String formatted = String.format("%05d", intPostingId);
		String returnID =  "K" + date + formatted;
		if (postingHashMapId.containsKey(firm))
		{
			if (j>=0)
			{
				postingHashMapId.get(firm).set(j,  returnID);
			}else 
			{
				postingHashMapId.get(firm).add(returnID);
			}
		}else
		{
			postingHashMapId.put(firm, new ArrayList<String>(Arrays.asList(returnID)));
		}
 		//	InstrumentId = ;
		return returnID;
		
	}
	public String BankStatementID(String sDate, String firm) throws ParseException
	{
		//tworzê date z podanej wartoœci
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date inDate = formatter.parse(sDate);
		String date = new SimpleDateFormat("yyMM").format(inDate);
		//PostingMaxId œci¹gam aktualnie najwy¿sz¹ wartoœæ z bazy
		ArrayList<ArrayList<String>> postingMaxArray=  new StoredProcedures().genUniversalArray("getMaxBankStatementID", new ArrayList<String>(Arrays.asList( firm,"B" + date))) ;        
		//String PostingMaxId = new StoredProcedures().PostingMaxID("K" + date, firm).get(1).get(0);
		String PostingMaxId;
		if (postingMaxArray.size()<=1)
		{
			PostingMaxId = "B" + date + "00000";
		}else
		{
			PostingMaxId =postingMaxArray.get(1).get(1);
		}
		// jeœli nie mam ksiêgowañ, aby zapobiec pustce tworzê nowy sztuczny ID
	//	if (PostingMaxId==null)

		//sprawdzam czy istnieje dla firmy jakiœ wczeœniej przydzielony posting
		String PostingId=null;
		int j = -1;
		if (bankHashMapId.containsKey(firm))
		{
			for (int i=0; i<bankHashMapId.get(firm).size();i++)
			{
				if (bankHashMapId.get(firm).get(i).substring(0, 5).equals("K" + date))
				{
					PostingId = bankHashMapId.get(firm).get(i);
					j = i;
					break;
				}else
				{
					PostingId = "B" + date + "00000";
				}
			}
		}
		else
		{
			PostingId = "B" + date + "00000";
		}
		
		
		int intPostingId = multiKey(simpleReg("\\d\\d\\d\\d\\d", PostingMaxId.substring(5)),Integer.valueOf(PostingId.substring(5)));
		String formatted = String.format("%05d", intPostingId);
		String returnID =  "B" + date + formatted;
		if (bankHashMapId.containsKey(firm))
		{
			if (j>=0)
			{
				bankHashMapId.get(firm).set(j,  returnID);
			}else 
			{
				bankHashMapId.get(firm).add(returnID);
			}
		}else
		{
			bankHashMapId.put(firm, new ArrayList<String>(Arrays.asList(returnID)));
		}
 		//	InstrumentId = ;
		return returnID;
		
	}
	public String SourceRowStatementID(String sDate, String firm) throws ParseException
	{
		//tworzê date z podanej wartoœci
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date inDate = formatter.parse(sDate);
		String date = new SimpleDateFormat("yyMM").format(inDate);
		//PostingMaxId œci¹gam aktualnie najwy¿sz¹ wartoœæ z bazy
		ArrayList<ArrayList<String>> postingMaxArray=  new StoredProcedures().genUniversalArray("getSettSourceMaxID", new ArrayList<String>(Arrays.asList( firm,"S" + date))) ;        
		//String PostingMaxId = new StoredProcedures().PostingMaxID("K" + date, firm).get(1).get(0);
		String PostingMaxId;
		if (postingMaxArray.size()<=1)
		{
			PostingMaxId = "S" + date + "00000";
		}else
		{
			PostingMaxId =postingMaxArray.get(1).get(1);
		}
		// jeœli nie mam ksiêgowañ, aby zapobiec pustce tworzê nowy sztuczny ID
	//	if (PostingMaxId==null)

		//sprawdzam czy istnieje dla firmy jakiœ wczeœniej przydzielony posting
		String PostingId=null;
		int j = -1;
		if (statSourceHashMapId.containsKey(firm))
		{
			for (int i=0; i<statSourceHashMapId.get(firm).size();i++)
			{
				if (statSourceHashMapId.get(firm).get(i).substring(0, 5).equals("S" + date))
				{
					PostingId = statSourceHashMapId.get(firm).get(i);
					j = i;
					break;
				}else
				{
					PostingId = "S" + date + "00000";
				}
			}
		}
		else
		{
			PostingId = "S" + date + "00000";
		}
		
		
		int intPostingId = multiKey(simpleReg("\\d\\d\\d\\d\\d", PostingMaxId.substring(5)),Integer.valueOf(PostingId.substring(5)));
		String formatted = String.format("%05d", intPostingId);
		String returnID =  "S" + date + formatted;
		if (statSourceHashMapId.containsKey(firm))
		{
			if (j>=0)
			{
				statSourceHashMapId.get(firm).set(j,  returnID);
			}else 
			{
				statSourceHashMapId.get(firm).add(returnID);
			}
		}else
		{
			statSourceHashMapId.put(firm, new ArrayList<String>(Arrays.asList(returnID)));
		}
 		//	InstrumentId = ;
		return returnID;
		
	}
	public Integer simpleReg(String pattern, String matcher)
	{	
			Pattern pat = Pattern.compile(pattern);
			Matcher mat = pat.matcher(matcher);
			while(mat.find())
			{
				return Integer.valueOf(mat.group()) + 1;
			}
			return 1;
			
		
	}
	private Integer multiKey(Integer num, Integer actual)
	{
		 
		if (actual!=null)
		{
			if (num<=actual)
			{
				num= actual+1;
			}
		}
		return num;
		
	}
	
}
