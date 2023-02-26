package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.PythonBase;
import MyLittleSmt.StoredProcedures;

public class ISelectionRun extends  JDialog implements  MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FrameTemplate ImportMain;
	private HashMap<String, String> mapSysMain, mapSysBaseMain;
	private HashMap<String, ArrayList<String>> mapSysAllMain;
	private DefaultTableModel ModelMain;
	private JTable tableMain;
	private String selection="";
	private int columnMain;
	private ArrayList<ArrayList<String>> tableList;
	public ISelectionRun(String yamlSys, String yamlData, String title, int getcolumnMain)
	{
	  //  JDialog d = new JDialog(new JFrame() , "Dialog Example", true); 
		setModal(true);
		setTitle(title);
		setSize(300,300);    
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		columnMain= getcolumnMain;
		ImportMain = new FrameTemplate();
		ImportMain.JTableHelperSys(yamlSys);
		mapSysMain =ImportMain.getMapSys();
		mapSysBaseMain = ImportMain.getMapSysBase();
		mapSysAllMain = ImportMain.getMapSysAll();
		ModelMain =  getModel(yamlData);
		frameSimpleBody();	
 
	}
	
	public ISelectionRun(String inSelection, String yamlSys, String yamlData, String title, int getcolumnMain)
	{
	  //  JDialog d = new JDialog(new JFrame() , "Dialog Example", true); 
		setModal(true);
		setTitle(title);
		setSize(300,300);    
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.selection=inSelection;
		columnMain= getcolumnMain;
		ImportMain = new FrameTemplate();
		ImportMain.JTableHelperSys(yamlSys);
		mapSysMain =ImportMain.getMapSys();
		mapSysBaseMain = ImportMain.getMapSysBase();
		mapSysAllMain = ImportMain.getMapSysAll();
		ModelMain =  getModel(yamlData);
		frameSimpleBody();	
		
	}
	public ISelectionRun(String inSelection, String yamlSys, String procedure, ArrayList<String> parameters, String title, int getcolumnMain)
	{
	  //  JDialog d = new JDialog(new JFrame() , "Dialog Example", true); 
		setModal(true);
		setTitle(title);
		setSize(300,300);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.selection=inSelection;
		columnMain= getcolumnMain;
		ImportMain = new FrameTemplate();
		ImportMain.JTableHelperSys(yamlSys);
		mapSysMain =ImportMain.getMapSys();
		mapSysBaseMain = ImportMain.getMapSysBase();
		mapSysAllMain = ImportMain.getMapSysAll();
		ModelMain = getModel(procedure,  parameters);
		
		frameSimpleBody();
 
	
	}
	private void frameSimpleBody()
	{
		tableMain = ImportMain.getDefaultTable(ModelMain, mapSysAllMain);
		tableMain.setCellSelectionEnabled(false);
		tableMain.setRowSelectionAllowed(true);		
		tableMain.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tableMain.addMouseListener(this);
		
		
		ImportMain.addFrameTempListener(tableMain, ModelMain, mapSysAllMain, mapSysBaseMain, mapSysMain, null);
		ImportMain.addListenerContextMenu();
		
		JPanel panelMain = new JPanel(new BorderLayout(5,5));
			JTabbedPane jtp = new JTabbedPane();
				JPanel main = new JPanel(new BorderLayout(5,5));
					main.add(new JScrollPane(tableMain),BorderLayout.CENTER);
				jtp.add("G³ówna", main);
			panelMain.add(jtp);
		add(panelMain,BorderLayout.CENTER);
		tableMain.setAutoResizeMode(2);
		repaint();
		pack();
		addListeners();
		addSelectionRow();
 
		//ModelMain.insertRow(0, emtyRow());
		ImportMain.Button_addEmptyRow(ModelMain,mapSysAllMain, tableMain, 0);
		setVisible(true);
		
	}
	private Object[] emtyRow()
	{
		Object[] result = new Object[ModelMain.getColumnCount()];
		for (int i = 0; i<ModelMain.getColumnCount();i++)
		{
			result[i] = ""; 
		}
		return result;
	}
	
	private void addListeners()
	{
		ImportMain.addFrameTempListener(tableMain, ModelMain, mapSysAllMain, mapSysBaseMain, mapSysMain, null);
		ImportMain.addListenerContextMenu();
		ImportMain.getaddRowM().setEnabled(false);
		ImportMain.getdeleteM().setEnabled(false);
		ImportMain.getpasteM().setEnabled(false);
	}
	
	private DefaultTableModel getModel(String yamlData)
	{
		try {
			PythonBase readers = new PythonBase();
			DefaultTableModel model =readers.getDataToJT(1, mapSysMain, mapSysAllMain, yamlData);
			tableList = readers.getListFromBase();
			return model;
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return null;
		}
	}
	
	private DefaultTableModel getModel(String procedure, ArrayList<String> parameters)
	{
		StoredProcedures sp = new StoredProcedures();
		DefaultTableModel model = sp.genUniversalModel(procedure, parameters, 1, mapSysMain, mapSysAllMain);
		tableList = sp.getListFromBase();
		
		return model;
	}
 
	public ArrayList<ArrayList<String>> getTableList()
	{
		return tableList;
	}
	 
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==tableMain)
		{
	       if(e.getClickCount()==2)
	        {
	    	   if (tableMain.getSelectedColumn()>-1&&tableMain.getSelectedRow()>-1)
	    	   {
	    	   selection = String.valueOf(tableMain.getValueAt(tableMain.getSelectedRow(), columnMain));
	    	   dispose();
	    	   }
	    	   
	        }
	        
	     }
	}
	public String getSelection()
	{
		return selection;
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void addSelectionRow()
	{
	 if (selection!=null)
	 {
		for (int i=0;i<ModelMain.getRowCount();i++)
		{
			if (tableMain.getValueAt(i, columnMain).equals(selection))
			{
				tableMain.addRowSelectionInterval(i, i);
				return;
			}
		}
	 }
	}

	
}
