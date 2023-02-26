package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateButtons;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.FrameTemplateWindow;
import MyLittleSmt.PythonBase;
import MyLittleSmt.StoredProcedures;
import MyLittleSmt.txt;

public class IInstruments extends FrameTemplateWindow implements  MouseListener, ActionListener , ChangeListener, TableModelListener {
private FrameTemplate ImportFrame, MainFrame, OrderFrame, CostFrame, CodeFrame, EmailFrame;
private HashMap <String, String> mainSys, orderSys, costSys, mainSysBase, orderSysBase, costSysBase, codeSys, codeSysBase, mailSys, mailSysBase;
private HashMap <String, ArrayList<String>> mainSysAll, orderSysAll, costSysAll, codeSysAll, mailSysAll;
private JTabbedPane JTPRibbon;
private JPanel rOption, rExport, code;
private JPanel ribbonOption, ribbonExport, ribbonImport, ribbonData, ribbonCodeOption, ribbonCodeExport, ribbonCodeImport, ribbonMailOption, ribbonMailExport, ribbonMailImport,ribbonMailSelect;
private DefaultTableModel mainModel, orderModel, codeModel, mailModel;
private ArrayList<Integer> mainKey, orderKey, codeKey, mailKey;
private JTable mainTable, orderTable, codeTable, mailTable;
private String conterparty;
private HashMap<Integer, String> dictColumnAndYaml;
private JCheckBox checkBox1;
private JTextField instrument, nettoOrder, grosOrder ; 
private JButton smallAdd, bSel;
private JTabbedPane JTPTable;
private String chInst,firma, firmFullName, regDate;
private boolean codeInsertEnable=false, mailInsertEnable=false;
private ArrayList<ArrayList<String>> insDictList;
	public IInstruments(int x, int y, String title)  {
		super(x, y, title);
		// TODO Auto-generated constructor stub
		ImportFrame = new FrameTemplate();
		MainFrame = new FrameTemplate();
		OrderFrame = new FrameTemplate();
		CostFrame = new FrameTemplate();
		CodeFrame = new FrameTemplate();
		EmailFrame = new FrameTemplate();
		//Ribbon 
		///JPanel smallOrderButtons=OrderFrame.getSmallButtons();
		JPanel smallOrderButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
			smallAdd = new FrameTemplateButtons().smallButton("Edycja pozycji", FrameTemplateImageIcon.iconJButton_SmallAdd());
			smallAdd.setEnabled(false);
			smallAdd.addActionListener(this);
		smallOrderButtons.add(smallAdd);
		
		JPanel upPanel = new JPanel(new BorderLayout());
		upPanel.add(ImportFrame.GetUpMenu(false), BorderLayout.PAGE_START);
			JTPRibbon = new JTabbedPane();
				rOption = new JPanel(new FlowLayout(FlowLayout.LEFT));
				rExport = new JPanel(new FlowLayout(FlowLayout.LEFT));
					MainRibbon();
					codeRibbon();
					mailRibbon();
					setVisibleCodeRibbon(false);
					setVisibleMailRibbon(false);
			JTPRibbon.add("Opcje" , rOption);
			JTPRibbon.add("Eksport", rExport);
		upPanel.add(JTPRibbon, BorderLayout.PAGE_END);
		add(upPanel,BorderLayout.PAGE_START);
		
		checkBox1 = new JCheckBox("Otwarte", true);  
		checkBox1.addActionListener(this);
		
		instrument = new JTextField();
		instrument.setEnabled(false);
		instrument.setPreferredSize(new Dimension(125,20));
 
		nettoOrder = new JTextField();
		nettoOrder.setEnabled(false);
		nettoOrder.setPreferredSize(new Dimension(125,20));
		
		grosOrder = new JTextField();
		grosOrder.setEnabled(false);
		grosOrder.setPreferredSize(new Dimension(125,20));		
		
		JPanel instTF = new JPanel();
		instTF.add(instrument);
		JPanel netOr = new JPanel();
		JPanel groOr = new JPanel();
			netOr.add(nettoOrder);
			groOr.add(grosOrder);
		
		
		//tabele
			//Main
			MainFrame.JTableHelperSys("IInstruments_sys.yml");
			mainSys = MainFrame.getMapSys();
			mainSysBase = MainFrame.getMapSysBase();
			mainSysAll = MainFrame.getMapSysAll();
			mainKey = new ArrayList<Integer>(Arrays.asList(0,1));
			mainModel = getMainModel();
			mainTable = MainFrame.getDefaultTable(mainModel, mainSysAll);
			dictColumnAndYaml = new HashMap<Integer, String>();
			dictColumnAndYaml.put(2, "IInstruments_Dict_Model.yml");
			dictColumnAndYaml.put(3, "IInstruments_Dict_Model.yml");
			
			OrderFrame.JTableHelperSys("IInstruments_Orders_sys.yml");
			orderSys=OrderFrame.getMapSys();
			orderSysBase = OrderFrame.getMapSysBase();
			orderSysAll = OrderFrame.getMapSysAll();
			orderKey = new ArrayList<Integer>(Arrays.asList(0,1));
			orderModel =  getOrderModel("", "");
			orderTable = OrderFrame.getDefaultTable(orderModel, orderSysAll);
			
			CodeFrame.JTableHelperSys("IInstuments_sys_Code.yml");
			codeSys = CodeFrame.getMapSys();
			codeSysBase = CodeFrame.getMapSysBase();
			codeSysAll = CodeFrame.getMapSysAll();
			codeKey =  new ArrayList<Integer>(Arrays.asList(0,1,2));
			codeModel = getCodeModel("","");
			codeTable = CodeFrame.getDefaultTable(codeModel, codeSysAll);
			
			EmailFrame.JTableHelperSys("IInstuments_sys_Mail.yml");
			mailSys = EmailFrame.getMapSys();
			mailSysBase = EmailFrame.getMapSysBase();
			mailSysAll = EmailFrame.getMapSysAll();
			mailModel = getMailModel("","");
			mailKey =  new ArrayList<Integer>(Arrays.asList(0,1,2));
			mailTable = EmailFrame.getDefaultTable(mailModel, mailSysAll);
			
			JPanel dwonPanel = new JPanel(new BorderLayout(5,5));
				JPanel leftPanel = new JPanel(new BorderLayout(5,5));			
					JTPTable = new JTabbedPane();
						JPanel leftUp = new JPanel(new FlowLayout(FlowLayout.LEFT));
							leftUp.add(checkBox1);
							leftUp.add(instTF);
						JPanel main= new JPanel(new BorderLayout(5,5));
						///main.add(leftUp, BorderLayout.PAGE_START);
						main.add(new JScrollPane(mainTable),BorderLayout.CENTER);
						code = new JPanel(new BorderLayout(5,5));
						code.add(new JScrollPane(codeTable), BorderLayout.CENTER);
						JPanel mail = new JPanel(new BorderLayout(5,5));
						mail.add(new JScrollPane(mailTable), BorderLayout.CENTER);
						leftPanel.add(leftUp, BorderLayout.PAGE_START);
					JTPTable.add("Instrumenty", main);
					JTPTable.add("Kody", code);
					JTPTable.add("Adresy eMail", mail);
				//	code.setEnabled(false);
				leftPanel.add(JTPTable);
				JPanel rightPanel = new JPanel(new BorderLayout(5,5));
					JPanel downRightUpPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
						downRightUpPanel.add(new JLabel("Netto"));
						downRightUpPanel.add(netOr);
						downRightUpPanel.add(new JLabel("Brutto"));
						downRightUpPanel.add(groOr);
					
					JPanel rightUpPanel = new JPanel(new BorderLayout(5,5));
					rightUpPanel.add(smallOrderButtons,BorderLayout.PAGE_START);
					rightUpPanel.add(new JScrollPane(orderTable),BorderLayout.CENTER);
					rightUpPanel.add(downRightUpPanel, BorderLayout.PAGE_END);
				rightPanel.add(rightUpPanel, BorderLayout.PAGE_START);
			dwonPanel.add(leftPanel, BorderLayout.CENTER);
			dwonPanel.add(rightPanel, BorderLayout.LINE_END);
			add(dwonPanel);
			JTPTable.addChangeListener(this);
			pack();
			
			setMainListeners();
			 

	}
	private void setMainListeners()
	{
		MainFrame.addFrameTempListener(mainTable, mainModel,mainSysAll, mainSysBase, mainSys, mainKey);
		MainFrame.addDictToModel(dictColumnAndYaml, mainModel);
		MainFrame.addListenerContextMenu();
		MainFrame.addListenerJTable(mainTable, mainModel);
		MainFrame.addListenerRibbon();
		MainFrame.setGgenKey(1,2,0);
		mainTable.addMouseListener(this);
		MainFrame.addFirm(0, ImportFrame.getBFirm());
		setMenuRun(); 
		MainFrame.setFilter("false", 10);			
		try {
			getDict(mainSysAll, mainTable);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setCodeListeners()
	{
		CodeFrame.removeActionListenersRibbon();
		CodeFrame.addFrameTempListener(codeTable, codeModel, codeSysAll, codeSysBase, codeSys, codeKey);
		CodeFrame.addListenerJTable(codeTable, codeModel);
		CodeFrame.addListenerContextMenu();
		CodeFrame.addListenerRibbon();
		codeTable.addMouseListener(this);
	 	CodeFrame.addFirm(0, ImportFrame.getBFirm());
	 	codeModel.addTableModelListener(this);
	 	getDictCode (codeSysAll, codeTable);
	}
	private void setMailListeners()
	{   if (bSel!=null)
			{
				bSel.removeActionListener(this);
			}
		EmailFrame.removeActionListenersRibbon();
		EmailFrame.addFrameTempListener(mailTable, mailModel, mailSysAll, mailSysBase, mailSys, mailKey);
		EmailFrame.addListenerJTable(mailTable, mailModel);
		EmailFrame.addListenerContextMenu();
		EmailFrame.addListenerRibbon();
		mailTable.addMouseListener(this);
	 	EmailFrame.addFirm(0, ImportFrame.getBFirm());
	 	mailModel.addTableModelListener(this);
		bSel = EmailFrame.getbSel();
	  	bSel.addActionListener(this);
	}
	
	
	private void setMenuRun()
	{
		MainFrame.setMenuRun(2);
		MainFrame.setMenuRun(3);
	}
	
	private void MainRibbon()
	{
		
	//	RemoveRibbon();
			ribbonOption = MainFrame.DefaultRibbonSim();
			ribbonExport = MainFrame.DefaultRibbonExp(); 
			ribbonImport = MainFrame.DefaultRibbonImp();
			ribbonData = MainFrame.DefaultRibbonData();
		rOption.add(ribbonOption);
		rOption.add(ribbonData);
		rExport.add(ribbonExport);
		rExport.add(ribbonImport);
		
		//ribbonImport.setVisible(false);
	}
	
	private void setVisibleMainRibbon(boolean hide)
	{
		ribbonOption.setVisible(hide);
		ribbonExport.setVisible(hide);
		ribbonImport.setVisible(hide);
		ribbonData.setVisible(hide);
	}
	
 
	
	private void codeRibbon()
	{
		ribbonCodeOption = CodeFrame.DefaultRibbonSim();
		ribbonCodeExport = CodeFrame.DefaultRibbonExp(); 
		ribbonCodeImport = CodeFrame.DefaultRibbonImp();
	rOption.add(ribbonCodeOption);
	rExport.add(ribbonCodeExport);
	rExport.add(ribbonCodeImport);
	}
	
	private void  setVisibleCodeRibbon(boolean hide)
	{
		ribbonCodeOption.setVisible(hide);
		ribbonCodeExport.setVisible(hide);
		ribbonCodeImport.setVisible(hide);
	}
	
	private void mailRibbon()
	{
		ribbonMailOption = EmailFrame.DefaultRibbonSim();
		ribbonMailSelect = EmailFrame.DefaultRibbonSelect();
		ribbonMailExport = EmailFrame.DefaultRibbonExp(); 
		ribbonMailImport = EmailFrame.DefaultRibbonImp();
	rOption.add(ribbonMailOption);
	rOption.add(ribbonMailSelect);
	rExport.add(ribbonMailExport);
	rExport.add(ribbonMailImport);
	}
	private void  setVisibleMailRibbon(boolean hide)
	{
		ribbonMailOption.setVisible(hide);
		ribbonMailExport.setVisible(hide);
		ribbonMailImport.setVisible(hide);
		ribbonMailSelect.setVisible(hide);
	}
	private void setRibbon()
	{
		if (JTPTable.getSelectedIndex()==1)
		{
			setVisibleMainRibbon(false);
			setVisibleCodeRibbon(true);
			setVisibleMailRibbon(false);
		}else if (JTPTable.getSelectedIndex()==0)
		{
			setVisibleCodeRibbon(false);
			setVisibleMainRibbon(true);
			setVisibleMailRibbon(false);
		}else if (JTPTable.getSelectedIndex()==2)
		{
			setVisibleCodeRibbon(false);
			setVisibleMainRibbon(false);
			setVisibleMailRibbon(true);
		}
	}
	private DefaultTableModel getMainModel()
	{
		StoredProcedures sp = new StoredProcedures();
		return sp.genModelInstruments(4, mainSys, mainSysAll);
	}
	
	private DefaultTableModel getOrderModel(String ID, String Firm)
	{
		StoredProcedures sp = new StoredProcedures();
		return sp.genModelOrdersToMain(ID,Firm, 1, orderSys, orderSysAll);
		
	}
	
	private DefaultTableModel getCodeModel(String ID, String Firm)
	{
		StoredProcedures sp = new StoredProcedures();
		return sp.genModelCodeToInstr(ID,Firm, 3, codeSys, codeSysAll);
		
	}
	
	private DefaultTableModel getMailModel(String ID, String Firm)
	{
		StoredProcedures sp = new StoredProcedures();
		return sp.genModelMailToInstr(ID,Firm, 3, mailSys, mailSysAll);
		
		
	}
	
	private ArrayList<ArrayList<String>> getAlterMailModel(String firmFullName)
	{
		StoredProcedures sp = new StoredProcedures();
		return sp.getAlterMailToInstr(firmFullName);

	}
	private void getDict (HashMap<String, ArrayList<String>> mapsysall, JTable table) throws IOException
	{
		PythonBase readers = new PythonBase();
		ArrayList<ArrayList<String>> templist = readers.FromBase(false, "IInstruments_sys_dict.yml");
		txt txtlist = new txt();
		Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
		MainFrame.CommboBoxDefault(dictdata, mapsysall, table);	
	}
	private void getDictCode (HashMap<String, ArrayList<String>> mapsysall, JTable table) 
	{
		
		try {
			PythonBase readers = new PythonBase();
			ArrayList<ArrayList<String>> templist;
		 
			templist = readers.FromBase(false, "IInstrumentsCode_sys_dict.yml");

			
			txt txtlist = new txt();
			Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
			CodeFrame.CommboBoxDefault(dictdata, mapsysall, table);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public ArrayList<ArrayList<String>> getDictToModel(String dict)
	{

		try {
			PythonBase readers = new PythonBase();
			ArrayList<ArrayList<String>> templist;
			templist = readers.FromBase(false, dict);
			return templist;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==mainTable)
		{

			chInst = mainTable.getValueAt(mainTable.getSelectedRow(), 1).toString();
			firma = mainTable.getValueAt(mainTable.getSelectedRow(), 0).toString();
			firmFullName = mainTable.getValueAt(mainTable.getSelectedRow(), 2).toString();
			
			
			
	
			
			
			if (!instrument.getText().equals(chInst))
			{
				insDictList = MainFrame.getOryginalDictList();
				HashMap<String, Integer> columnIns = MainFrame.getColumnNumbers(mainTable, mainSysAll);
				conterparty="";
				for (int i=0;i<insDictList.size();i++)
				  {
					if (mainTable.getValueAt(mainTable.getSelectedRow(), columnIns.get("Counterparty")).equals(insDictList.get(i).get(1)))
					{
						conterparty=insDictList.get(i).get(0);
					} 
				}
				regDate = mainTable.getValueAt(mainTable.getSelectedRow(), columnIns.get("Registration")).toString();
				instrument.setText(chInst);
				orderModel = getOrderModel(chInst, mainTable.getValueAt(mainTable.getSelectedRow(), 0).toString());
				orderTable.setModel(orderModel);
				codeModel = getCodeModel(chInst, firma);
				codeTable.setModel(codeModel);
				setCodeListeners();
				mailModel = getMailModel(chInst, firma);
				mailTable.setModel(mailModel);
				setMailListeners();
				double netto = 0;
				double gross = 0;
				for (int i=0; i<orderModel.getRowCount(); i++)
				{
					netto = FrameTemplate.round(netto,2) + FrameTemplate.round(Double.valueOf(orderModel.getValueAt(i, 4).toString().replace(",", ".")),2);
					gross = FrameTemplate.round(gross,2) + FrameTemplate.round(Double.valueOf(orderModel.getValueAt(i, 6).toString().replace(",", ".")),2);
				}
				nettoOrder.setText(String.valueOf(FrameTemplate.round(netto,2)));
				grosOrder.setText(String.valueOf(FrameTemplate.round(gross,2)));
			}
			
			if (chInst.equals(""))
			{
				smallAdd.setEnabled(false);	
			}
			else
			{
				smallAdd.setEnabled(true);
				codeInsertEnable=true;
				mailInsertEnable=true;
			}
			
			if (firma.equals("")||firma.equals("firma"))
			{
				firma = "";
				firmFullName ="";
			}
			

			if(e.getClickCount()==2 )
			{
			if (mainTable.getSelectedColumn()==2)
			{
				ISelectionRun jdialog = new ISelectionRun("ISelectConterparty_sys.yml", "ISelectConterparty.yml", "Kontahent", 2);
				mainTable.setValueAt(jdialog.getSelection(), mainTable.getSelectedRow(), mainTable.getSelectedColumn());
			

			}else if (mainTable.getSelectedColumn()==3)
			{
				ISelectionRun jdialog = new ISelectionRun("ISelectConterparty_sys.yml", "ISelectConterparty.yml", "Kontahent", 2);
				mainTable.setValueAt(jdialog.getSelection(), mainTable.getSelectedRow(), mainTable.getSelectedColumn());
			}
			}	
		}else if (e.getSource()==codeTable)
		{
			codeInsertEnable=true;
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
		if (e.getSource()==checkBox1)
		{
			if (checkBox1.isSelected()==false)
			{
				MainFrame.setRemoveFiltr();
			}else if (checkBox1.isSelected()==true)
			{
				MainFrame.setFilter("false", 10);
			}
			//System.out.println(checkBox1.isSelected());
		}else if (e.getSource()==smallAdd)
		{
			//IOrders orders = new IOrders(500, 900, "Pozycje zmówienia", instrument.getText(), mainTable.getValueAt(mainTable.getSelectedRow(), 0).toString());
			//orders.setVisible(true);
			//orders.setExtendedState(JFrame.MAXIMIZED_BOTH);
			var	isc = new IOrdersJDialog("IOrders_sys.yml", "getOrders", new ArrayList<String>(Arrays.asList(instrument.getText(), firma)), 12 ,"Pozycje zamówienia",new ArrayList<Integer>(Arrays.asList(0,1,2)), firma, instrument.getText(), regDate, conterparty);  
			isc.setVisible(true);
			orderModel = getOrderModel(instrument.getText(),firma);
			OrderFrame.setModel(orderModel);
			//orderTable.setModel(orderModel);
			
		}else if (e.getSource()==bSel)
		{
			ArrayList<ArrayList<String>> templist = getAlterMailModel(firmFullName);
			if (templist.size()>1)
			{
				for (int i=1; i<templist.size();i++)
				{
					Object[] temp = new Object[3];
					temp[0] = firma;
					temp[1] = chInst;
					temp[2] = templist.get(i).get(0);
					mailModel.insertRow(mailModel.getRowCount(), temp);	
				}
			}
		}
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==JTPTable)
		{
			setRibbon();
		}
	}
	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub

			if(e.getType()==0&& e.getSource()== codeModel&& codeInsertEnable==true)
			{
				newRowCodeTable(e.getFirstRow());
				codeInsertEnable=false;
			}else if(e.getType()==0&&e.getSource()== mailModel&& mailInsertEnable==true)
			{
				newRowMailTable(e.getFirstRow());
				mailInsertEnable=false;
			}
	}

	private void newRowCodeTable(int firstRow)
	{
		codeInsertEnable=false;
	 	codeTable.setValueAt(firma, firstRow, 0);
		codeTable.setValueAt(chInst, firstRow, 1);
	}
	private void newRowMailTable(int firstRow)
	{
		mailInsertEnable=false;
	 	mailTable.setValueAt(firma, firstRow, 0);
		mailTable.setValueAt(chInst, firstRow, 1);
	}
	
}
