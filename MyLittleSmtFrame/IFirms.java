package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateWindow;
import MyLittleSmt.MainEntryDataWarehouse;
import MyLittleSmt.PythonBase;
import MyLittleSmt.StoredProcedures;
import MyLittleSmt.txt;

public class IFirms extends FrameTemplateWindow implements MouseListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FrameTemplate firmFrame;
	private HashMap<String, String> firmSysBase, firmSys;
	private HashMap<String, ArrayList<String>> firmSysAll;
	private ArrayList<Integer> firmKey;
	private DefaultTableModel firmModel;
	private JTable firmTable;
	private JPanel ribbonOption , ribbonExport;
	private JPanel firmOption, firmExport, firmImport, firmData;
	private JButton bSave;
	
	public IFirms(int x, int y, String title) {
		super(x, y, title);
		firmFrame= new FrameTemplate();
		firmFrame.JTableHelperSys("ICompanies_sys.yml");
		
		firmSys = firmFrame.getMapSys();
		firmSysBase= firmFrame.getMapSysBase();
		firmSysAll = firmFrame.getMapSysAll();

		
		firmModel = firmModel();
		firmTable = firmFrame.getDefaultTable(firmModel, firmSysAll);
		firmKey = new ArrayList<Integer>(Arrays.asList(0));
		JPanel upPanel = new JPanel(new BorderLayout());
		JPanel downPanel = new JPanel(new BorderLayout());
			upPanel.add(firmFrame.GetUpMenu(false), BorderLayout.PAGE_START);
				JTabbedPane ribbon = new JTabbedPane();
					ribbonOption  = new JPanel(new FlowLayout(FlowLayout.LEFT));
					ribbonExport  = new JPanel(new FlowLayout(FlowLayout.LEFT));
					getFirmRibbon();
				ribbon.add("Opcje", ribbonOption);
				ribbon.add("Eksport", ribbonExport);
			upPanel.add(ribbon, BorderLayout.PAGE_END);
		downPanel.add(new JScrollPane(firmTable), BorderLayout.CENTER);
		add(upPanel, BorderLayout.PAGE_START);
		add(downPanel, BorderLayout.CENTER);
		setListenersFirms();
		
		try {
			getDict(firmSysAll, firmTable);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pack();
		// TODO Auto-generated constructor stub
	}
	
	private void setListenersFirms()
	{
		firmFrame.addFrameTempListener(firmTable, firmModel, firmSysAll, firmSysBase, firmSys, firmKey);
		firmFrame.addListenerJTable(firmTable, firmModel);
		firmFrame.setMenuRun(11);
		firmFrame.addListenerContextMenu();
		firmFrame.addListenerRibbon();
		firmTable.addMouseListener(this);
		bSave = firmFrame.getbSave();
		firmFrame.remListbSave();
		bSave.addActionListener(this);
	}
	
	private DefaultTableModel firmModel()
	{
		DefaultTableModel Model = new StoredProcedures().genModelFirmsAll(9, firmSys, firmSysAll);
		MainEntryDataWarehouse.firmMapFromModel(Model);
		return Model;
	}
	

	
	private void getFirmRibbon()
	{
		firmOption = firmFrame.DefaultRibbonSim();
		firmExport = firmFrame.DefaultRibbonExp();
		firmImport = firmFrame.DefaultRibbonImp();
		firmData = firmFrame.DefaultRibbonData();
		
		ribbonOption.add(firmOption);
		ribbonOption.add(firmData);
		ribbonExport.add(firmExport);
		ribbonExport.add(firmImport);
	}
	private void getDict(HashMap<String, ArrayList<String>> mapsysall, JTable table) throws IOException
	{
		PythonBase readers = new PythonBase();
		ArrayList<ArrayList<String>> templist = readers.FromBase(false, "ICompanies_sys_dict.yml");
		txt txtlist = new txt();
		Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
		firmFrame.CommboBoxDefault(dictdata, mapsysall, table);	
	}
	@Override	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==firmTable)
		{
			if (e.getClickCount()==2)
			{
				if(firmTable.getSelectedColumn()==11)
				{
					ISelectionRun jdialog = new ISelectionRun(firmTable.getValueAt(firmTable.getSelectedRow(), firmTable.getSelectedColumn()).toString(), "Dict_BaseCurrency_sys.yml", "Dict_BaseCurrency.yml", "Waluta bazowa", 0);
					firmTable.setValueAt(jdialog.getSelection(), firmTable.getSelectedRow(), firmTable.getSelectedColumn());
				}				
			}

		}
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==bSave)
		{
			MainEntryDataWarehouse.firmMapFromModel(firmModel);
			firmFrame.Button_Save();
		}
	}
}
