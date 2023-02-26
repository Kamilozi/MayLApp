package MyLittleSmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Reports {
	private ILogAddToLog AddLog;
	public Reports(String dateFrom, String dateTo, String groupID)
	{
		AddLog = new ILogAddToLog();
		AddLog.AddToLog("Rozpoczêcie generacji grupy raportów:" + groupID, "(I)");
		AddLog.AddToLog("Data od:" + dateFrom, "(I)");
		AddLog.AddToLog("Data do:" + dateTo, "(I)");
		//ArrayList<ArrayList<Object>> ListLog = new ArrayList<ArrayList<Object>>();
		///ListLog.addAll(dataimport.getListLog());
		//ILog log = new ILog(300, 600, "Log importu", ListLog);
		//log.setVisible(true);
		
		
		ArrayList<ArrayList<String>> reportList = new StoredProcedures().genUniversalArray("getReports", new ArrayList<String>(Arrays.asList(groupID)));
		HashMap<String, Integer> map = new FrameTemplate().getColumnNumbers(reportList.get(0));
		for (int i=1;i<reportList.size();i++)
		{ 
			AddLog.AddToLog("Rozpoczêcie generacji raportu: " + reportList.get(i).get(map.get("Report"))  + "(" + reportList.get(i).get(map.get("ReportID")) + ") dla grupy firm: " + reportList.get(i).get(map.get("Group_")), "(I)");
			ArrayList<ArrayList<String>> parameters = new StoredProcedures().genUniversalArray("getReportsCodes", new ArrayList<String>(Arrays.asList(reportList.get(i).get(map.get("ReportID")))));
			ArrayList<ArrayList<String>> firms = new StoredProcedures().genUniversalArray("getGroupOfCompanies", new ArrayList<String>(Arrays.asList("Reports", reportList.get(i).get(map.get("Group_"))))); 
			for (int q=1;q<firms.size();q++)
			{
				AddLog.AddToLog("Generacji raportu: " + reportList.get(i).get(map.get("Report"))  + " dla firmy: " + firms.get(q).get(1), "(I)");
				if (reportList.get(i).get(map.get("Report")).equals("Balance"))
				{
					Balance(firms.get(q).get(1), dateFrom, dateTo, parameters);
				}
			}
		}
		ArrayList<ArrayList<Object>>  ListLog= AddLog.getListLog();
		ILog log = new ILog(300, 600, "Log importu", ListLog);
		log.setVisible(true);
		
	}
	
	public void Balance(String firm, String dateFrom, String dateTo, ArrayList<ArrayList<String>> parameters)
	{
		String path = null;
		String format = null;
		String Open=null;
		HashMap<String, Integer> mapPar = new FrameTemplate().getColumnNumbers(parameters.get(0));
		
		for (int i=1;i<parameters.size();i++)
		{
			if (parameters.get(i).get(mapPar.get("Code")).equals("Path"))
			{
				path = parameters.get(i).get(mapPar.get("CodeValue"));
			}else if (parameters.get(i).get(mapPar.get("Code")).equals("Format"))
			{
				format = parameters.get(i).get(mapPar.get("CodeValue"));
			}else if (parameters.get(i).get(mapPar.get("Code")).equals("Open"))
			{
				Open = parameters.get(i).get(mapPar.get("CodeValue"));
			}
		}
		ArrayList<ArrayList<String>> balance = new StoredProcedures().genUniversalArray("reportBalance", new ArrayList<String>(Arrays.asList(firm, dateFrom,dateTo)));
		
	
		
		balance.add(0, new ArrayList<String>(Arrays.asList("", "Bilans firmy:" + firm, "Od:", dateFrom, "Do:", dateTo)));
		
		if (format.equals("XLS"))
		{
			for (int q =2 ;q<balance.size();q++)
			{
			balance.get(q).set(3, String.format("%.2f",Double.valueOf(balance.get(q).get(3))));
			balance.get(q).set(4, String.format("%.2f",Double.valueOf(balance.get(q).get(4))));
			balance.get(q).set(5, String.format("%.2f",Double.valueOf(balance.get(q).get(5))));
			balance.get(q).set(6, String.format("%.2f",Double.valueOf(balance.get(q).get(6))));
			}
			String fileName = path + "Balance_" + dateTo + "." + format;
			try
			{
			new ExcelFile().ArrayListToxls(balance,fileName );
			AddLog.AddToLog("Plik zapisany w lokalizacji: " + fileName, "(I)");
			if (Open!= null && Open.equals("1"))
			{
				new ExcelFile().tryOpen(fileName);
			}
			} catch (Exception e)
			{
				AddLog.AddToLog("Proces nie mo¿e uzyskaæ dostêpu do pliku lub lokalizacji", "(I)");
			}
			
		}
		
		
	}
}
