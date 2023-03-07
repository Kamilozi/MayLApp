package MyLittleSmt;

import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DocumentsSource {
	String workPath = "C:\\Users\\kamil\\OneDrive\\THM_GROUP\\Projekty\\Koszty\\THM_Group";
	private String fileName;
	public void setWorkPath(String workPath)
	{ 
		this.workPath = workPath;
	}
	public String getFileName()
	{
		return fileName;
	}
	
	public void addSource(String ID, String FIRM)
	{
		 
		String fileName = new DataImport().setFileName(workPath);
		if (fileName!=null&&fileName.length()>0)
		{
			this.fileName = fileName;
			newAddSource(ID,FIRM,fileName);
		
		}
	}
	public void newAddSource(String ID, String FIRM, String fileName)
	{
		deleteSource(ID, FIRM);
		new sourceAddBase("IDocumentSource_sys.yml", "getDocumentSource", new ArrayList<String>(Arrays.asList(ID, FIRM)), new ArrayList<Integer>(Arrays.asList(0,1)), fileName);
	}
	
	public void deleteSource(String ID, String FIRM)
	{
		new PythonBase().runProcedure(new StoredProcedures().genQuery("deleteDocumentSource", new ArrayList<String>(Arrays.asList(ID, FIRM))));
	}
	class sourceAddBase extends SimpleUpdate
	{

		public sourceAddBase(String yaml, String procedure, ArrayList<String> parameters, ArrayList<Integer> key, String dPath) {
			super(yaml, procedure, parameters, key);
			// TODO Auto-generated constructor stub
			Object[] newRow = {parameters.get(1), parameters.get(0),dPath}; 
			model.addRow(newRow);
			 
			frame.Button_Save();
		}
		
	}
	
	public void openSource(String ID, String FIRM)
	{
		ArrayList<ArrayList<String>> docSource = new StoredProcedures().genUniversalArray( "getDocumentSource", new ArrayList<String>(Arrays.asList(ID, FIRM)));
		if (docSource.size()>1)
		{
			openFlie(docSource.get(1).get(2));  
		}
	}
	public void openFlie(String sfile)   
	{  
	try  
	{  
	//constructor of file class having file as argument  
		File file = new File(sfile);   
		if(!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not  
		{  
			System.out.println("not supported");  
			return;  
		}  
		Desktop desktop = Desktop.getDesktop();  
		if(file.exists())         //checks file exists or not  
			desktop.open(file);              //opens the specified file  
	}  
	catch(Exception e)  
	{  
		e.printStackTrace();  
	}  
	} 
	public void showSource(String ID, String FIRM)
	{
		ArrayList<ArrayList<String>> docSource = new StoredProcedures().genUniversalArray( "getDocumentSource", new ArrayList<String>(Arrays.asList(ID, FIRM)));
		if (docSource.size()>1)
		{
			JTextArea ta = new JTextArea(10, 10);
            ta.setText("Lokalizacja ID:" + ID + " Firma: " + FIRM + ":\n"
                    + docSource.get(1).get(2));
            ta.setWrapStyleWord(true);
            ta.setLineWrap(true);
            ta.setCaretPosition(0);
            ta.setEditable(false);
			JOptionPane.showMessageDialog(null,  new JScrollPane(ta), "Lokalizacja:", JOptionPane.INFORMATION_MESSAGE);
		}else
		{
			JOptionPane.showMessageDialog(null, "Brak zród³a ID: " + ID + " Firma: " + FIRM, "B³¹d", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
