package MyLittleSmt;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class ExcelFile {
	private String file;
	public void JTableIntoExcel(Object[][] listToExcel)
	{
		String File = getNewFileName();
		ObjectToxls(listToExcel, File);
		try 
		{
			Desktop.getDesktop().open(new File(File));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public void tryOpen(String file)
	{
		try 
		{
			Desktop.getDesktop().open(new File(file));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void ObjectToxls(Object[][] list, String File)
	{
		try 
		{
			FileWriter fw = new FileWriter(File);
			for (int i=0; i<list.length; i++)
			{
				for (int j=0; j<list[i].length; j++)
				{
					fw.write(list[i][j] + "\t");
				}
				fw.write("\n");
			}
			fw.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ArrayListToxls(ArrayList<ArrayList<String>> list, String File)
	{
		try 
		{
			FileWriter fw = new FileWriter(File);
			for (int i=0; i<list.size(); i++)
			{
				for (int j=0; j<list.get(i).size(); j++)
				{
					fw.write(list.get(i).get(j) + "\t");
				}
				fw.write("\n");
			}
			fw.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	///stara metoda do wyrzucenia
	public void JTableToExcel(JTable table)
	{
	   try {
	    	
	 		TableModel m = table.getModel();
			FileWriter fw = new FileWriter(getNewFileName());
			
			for(int i = 0; i < m.getColumnCount(); i++){
		        fw.write(m.getColumnName(i) + "\t");
		      }
			fw.write("\n");
			
			 for(int i=0; i < m.getRowCount(); i++) 
			 {	for(int j=0; j < m.getColumnCount(); j++) 
			 	{fw.write(m.getValueAt(i,j).toString()+"\t");}
			     fw.write("\n");
			     Desktop.getDesktop().open(new File(file));
			 }
			fw.close();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}
	
    private String getNewFileName()
    {
    	JFileChooser fchoose  = new JFileChooser();
    	int option = fchoose.showSaveDialog(null);
    	if(option == JFileChooser.APPROVE_OPTION)
    	{
    		String name = fchoose.getSelectedFile().getName(); 
    		String path = fchoose.getSelectedFile().getParentFile().getPath();
    		file = path + "\\" + name + ".xls";
    	}
    	return file;
    }
}
