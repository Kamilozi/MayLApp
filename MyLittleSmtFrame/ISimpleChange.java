package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.PythonBase;
import MyLittleSmt.StoredProcedures;
import MyLittleSmt.txt;

/**
 * SimpleChange to JDialog do prostych modufikacji. Tylko jedna tabela. 
 * @author kamil
 *
 */
public class ISimpleChange extends  JDialog
{
	protected HashMap<String,String> sysBase, sysMap;
	protected HashMap<String, ArrayList<String>> sysAll;
	protected JTable table;
	protected DefaultTableModel model;
	protected ArrayList<Integer> Key;
	protected FrameTemplate frame = new FrameTemplate();
	protected JButton okButton;
 
	
	protected JPanel downPanel, upPanel;
	protected JPanel rOption, rExport;
	protected HashMap<String, Integer> modelColumnInt; 
	protected HashMap<String, Integer> tableColumnInt;
	
	public ISimpleChange(boolean isCheck, String yamlSys, String procedure, ArrayList<String> procParameters,int accesType, String title, ArrayList<Integer> Key)
	{
		setModal(true);
		setTitle(title);
		setSize(1200,600);   
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setIconImage(FrameTemplateImageIcon.iconSys().getImage());
		
		if (isCheck==true){frame.JTableHelperSys(yamlSys, true);} else {frame.JTableHelperSys(yamlSys);}
		sysMap = frame.getMapSys();
		sysBase = frame.getMapSysBase();
		sysAll = frame.getMapSysAll();
		model = isCheck==true ? getModel(isCheck, procedure, procParameters, accesType) :  getModel(procedure, procParameters, accesType);
		table = frame.getDefaultTable(model, sysAll);
		rOption = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
		rExport = new JPanel(new FlowLayout(FlowLayout.LEFT));
			rOption.add(frame.DefaultRibbonSim());
			rExport.add(frame.DefaultRibbonExp());
			rExport.add(frame.DefaultRibbonImp());
		upPanel = new JPanel(new BorderLayout());
			JTabbedPane jtp = new JTabbedPane();
				jtp.add("Opcje", rOption);
				jtp.add("Eksport", rExport);
			upPanel.add(jtp, BorderLayout.CENTER);
		add(upPanel, BorderLayout.PAGE_START);	
		downPanel = new JPanel(new BorderLayout());
			downPanel.add(new JScrollPane(table), BorderLayout.CENTER);
		add(downPanel, BorderLayout.CENTER);
		//setListener(Key);
		if (isCheck==true)
		{
			setListener(Key);
			frame.addSkipList(new ArrayList<Integer>(Arrays.asList(0)));
		}
	}
	
	private DefaultTableModel getModel(String procedure, ArrayList<String> procParameters, int accesType)
	{
		return new StoredProcedures().genUniversalModel(procedure, procParameters, accesType, sysMap, sysAll);
	}
	private DefaultTableModel getModel(boolean isCheck, String procedure, ArrayList<String> procParameters, int accesType)
	{
		return new StoredProcedures().genUniversalModel(isCheck, procedure, procParameters, accesType, sysMap, sysAll);
	}
	
	protected void setListener(ArrayList<Integer> Key)
	{
		frame.addFrameTempListener(table, model, sysAll, sysBase, sysMap, Key);
		frame.addListenerJTable(table, model);	
		frame.addListenerContextMenu();
		
	}
	
	protected void addRibbonListener()
	{
		frame.addListenerRibbon();
	}
	
	public void setGenKey(int column, int keyType, int keyModel)
	{
		frame.setGgenKey(column, keyType, keyModel);
	}
	
	protected void getDict (String yamlDict) 
	{
		
		try {
			PythonBase readers = new PythonBase();
			ArrayList<ArrayList<String>> templist;
			templist = readers.FromBase(false, yamlDict); //
			txt txtlist = new txt();
			Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
			frame.CommboBoxDefault(dictdata, sysAll, table);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("unlikely-arg-type")
	protected void modelColumnMap()
	{
		modelColumnInt =new HashMap<String, Integer>();
		for (int i=0; i<model.getColumnCount();i++)
		{
			modelColumnInt.put(sysAll.get(model.getColumnName(i)).get(0), i);
		}
	}
	
	protected void tableColumnMap()
	{
		tableColumnInt = frame.getColumnNumbers(table, sysAll);
		
	}
	
	protected void disableEdit()
	{
		frame.isEditContextMenu(false);
		frame.isEditRibbon(false);
 
	}
}
