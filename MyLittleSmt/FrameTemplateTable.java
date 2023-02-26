package MyLittleSmt;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FrameTemplateTable {
	protected FrameTemplate frame;
	protected HashMap<String, String> sysMap, sysBase;
	protected HashMap<String, ArrayList<String>> sysAll;
	protected DefaultTableModel model;
	protected JTable table;
	protected JScrollPane jsp;
	protected JPanel rOption, rExport;
	protected JPanel panel;
	protected String procedure;
	protected int enableType;
 
	/**
	 * Cia³o buduj¹cy JScrollPane
	 * @param sysYaml
	 * @param procedure
	 * @param procParameters
	 * @param enableType
	 */
	public FrameTemplateTable(String sysYaml, String procedure, ArrayList<String> procParameters, int enableType)
	{
		this.procedure = procedure;
		this.enableType = enableType;
		frame = new FrameTemplate();
		frame.JTableHelperSys(sysYaml);
		sysMap = frame.getMapSys();
		sysBase = frame.getMapSysBase();
		sysAll = frame.getMapSysAll();
		model = getModelSim(procedure, procParameters, enableType);
		table = frame.getDefaultTable(model, sysAll);
		jsp = new JScrollPane(table);
		panel = new JPanel(new BorderLayout());
		
	}
	private DefaultTableModel getModelSim(String procedure, ArrayList<String> procParameters, int enableType)
	{
		return new StoredProcedures().genUniversalModel(procedure ,procParameters, enableType, sysMap, sysAll);
	}
	/**
	 * Podstawowy listener - przed uruchomieniem wymagany jest Ribbon
	 * @param key - klucz œledzenia zmian
	 */
	public void fullListener(ArrayList<Integer> key)
	{
		frame.addFrameTempListener(table, model, sysAll, sysBase, sysMap, key);
		frame.addListenerContextMenu();
		frame.addListenerJTable(table, model);
		frame.addListenerRibbon();
	}
	
	public JScrollPane getTableJSP()
	{
		return jsp;
	}
	
	public JPanel getTableJP()
	{
		
			panel.add(jsp, BorderLayout.CENTER);
		return panel;
	}
	//Default
	public void RibbonCreate()
	{
		rOption = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rExport = new JPanel(new FlowLayout(FlowLayout.LEFT));
	}
	
	public JPanel getrOption()
	{
		return rOption;
	}
	
	public JPanel getrExport()
	{
		return rExport;
	}
 
	public void setRibbonVisible(boolean isVisible)
	{
		rOption.setVisible(isVisible);
		rExport.setVisible(isVisible);
	}
	
	protected void getDict(String yamlSysFile) throws IOException
	{
	 
		ArrayList<ArrayList<String>> templist = new PythonBase().FromBase(false, yamlSysFile);  //getDict("IPlanAccount_dict.yml")
		txt txtlist = new txt();
		Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
	    frame.CommboBoxDefault(dictdata, sysAll, table);	
	}
	
	public void setNewModel(ArrayList<String> procParameters)
	{
		model = getModelSim(procedure, procParameters, enableType);
		frame.setModel(model);
	}
	
	protected HashMap<String, Integer> columnMapModel()
	{
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		for (int i=0;i<model.getColumnCount();i++)
		{
			result.put(sysAll.get(model.getColumnName(i)).get(0), i);
		}
		
		return result;
	}
	
	protected HashMap<String, Integer> columnMapTable()
	{
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		for (int i=0;i<table.getColumnCount();i++)
		{
			result.put(sysAll.get(table.getColumnName(i)).get(0), i);
		}
		
		return result;
	}
	
	
}
