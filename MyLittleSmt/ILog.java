package MyLittleSmt;

import java.awt.Image;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public class ILog extends FrameTemplateWindow
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private ArrayList<ArrayList<Object>> ListLog;
FrameTemplate frameLog;
private HashMap<String, String> mapsys;
private HashMap<String, ArrayList<String>> mapsysAll;
private DefaultTableModel Model;
 
	public ILog(int x, int y, String title, ArrayList<ArrayList<Object>> ListLogK) {
		super(500, 500, title);
		this.ListLog = new ArrayList<ArrayList<Object>>();
		this.ListLog.addAll(ListLogK);
		
		frameLog = new FrameTemplate();
		
		mapsys = new HashMap<String, String>();
		mapsys.put("Data", "Data");
		mapsys.put("Uzytkownik", "Uzytkownik");
		mapsys.put("Typ", "Typ");
		mapsys.put("Text", "Text");
		
		mapsysAll=new HashMap<String, ArrayList<String>>();
		mapsysAll.put("Data", new ArrayList<String>(Arrays.asList("Data", "NO", "datetime", "0")));
		mapsysAll.put("Uzytkownik", new ArrayList<String>(Arrays.asList("Uzytkownik", "NO", "nvarchar", "50")));
		mapsysAll.put("Typ", new ArrayList<String>(Arrays.asList("Typ", "NO", "Image", "0")));
		mapsysAll.put("Text", new ArrayList<String>(Arrays.asList("Text", "NO", "nvarchar", "250")));
		String[] column = {"Data", "Uzytkownik", "Typ", "Text"};
		Object[][] data = getDate(ListLog);
		Model = frameLog.NewModel(1, column, data, mapsys, mapsysAll);
		JTable LogTable = frameLog.getDefaultTable(Model, mapsysAll);
		JScrollPane LogJSP = new JScrollPane(LogTable);
		TableColumn col = null;
		for (int i = 0; i < column.length; i++) {
			 col = LogTable.getColumnModel().getColumn(i);
		    if (i == 0|| i==1) {
		    	col.setPreferredWidth(40); //third column is bigger
		    }else if (i == 2)
		    {
		    	col.setPreferredWidth(1);
		    }
		    else if (i==3)
		    {
		    	col.setPreferredWidth(200);
		    }
		    else {
		    	col.setPreferredWidth(50);
		    }
		}
		frameLog.addFrameTempListener(LogTable,  Model, mapsysAll, mapsys, mapsys, null);
		frameLog.addListenerContextMenu();
		frameLog.getdeleteM().setEnabled(false);
		frameLog.getaddRowM().setEnabled(false);
		
		frameLog.getpasteM().setEnabled(false);
	 
		add(LogJSP);
		
	}
	
	
	public void addToLog(String txt, String type)
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
	
	
	
	
	public Object[][] getDate(ArrayList<ArrayList<Object>> LogListObject)
	{
		
		Icon inform = FrameTemplateImageIcon.iconJTable_inf();
		Icon warning = FrameTemplateImageIcon.iconJTable_war();
		Icon error = FrameTemplateImageIcon.iconJTable_err();
		Object[][] result = new Object[LogListObject.size()][LogListObject.get(0).size()];
		for (int i =0; i<LogListObject.size();i++)
		{
			Object[] temList = new Object[LogListObject.get(0).size()];
			for (int j=0; j<LogListObject.get(i).size(); j++)
			{
				if (LogListObject.get(i).get(j).equals("(I)"))
				{
					temList[j] = inform;
				}else if (LogListObject.get(i).get(j).equals("(W)"))
				{
					temList[j] = warning;
				}else if (LogListObject.get(i).get(j).equals("(E)"))
				{
					temList[j] = error;
				}else
				{
				temList[j] = LogListObject.get(i).get(j);
				}
			}
			result[i] = temList;
		}
		
		return result;
		
	}
 
}
