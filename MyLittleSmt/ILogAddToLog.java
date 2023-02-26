package MyLittleSmt;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ILogAddToLog {
	private ArrayList<ArrayList<Object>> ListLog= new ArrayList<ArrayList<Object>>();
	
	public void AddToLog(String txt, String type)
	{
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		
		ArrayList<Object> LogRow =new  ArrayList<Object>();
		LogRow.add(formatter.format(date).toString());
		LogRow.add("Admin");
		LogRow.add(type);
		LogRow.add(txt);
		ListLog.add(LogRow);
	}
	
	public ArrayList<ArrayList<Object>> getListLog()
	{
		ArrayList<ArrayList<Object>> ReturnListLog = new  ArrayList<ArrayList<Object>>(ListLog);
		ListLog.clear();
		return ReturnListLog;
	}
	
}
