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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateButtons;
import MyLittleSmt.FrameTemplateDataWarehouse;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.FrameTemplateWindow;
import MyLittleSmt.MainEntryDataWarehouse;
import MyLittleSmt.PythonBase;
import MyLittleSmt.StoredProcedures;
import MyLittleSmt.txt;

public class IPostings extends FrameTemplateWindow implements TableModelListener, ActionListener, ChangeListener, MouseListener{
/**
	 * 
	 */
private static final long serialVersionUID = 1L;
private FrameTemplate qtyFrame, ledFrame;
private DefaultTableModel qtyModel, ledModel;
private ArrayList<Integer> qtyKey, ledKey;
private HashMap<String, String> qtySys, qtySysBase, ledSys, ledSysBase;
private HashMap<String, ArrayList<String>> qtySysAll, ledSysAll;
private JTable qtyTable, ledTable;
private JPanel qtyOption, qtyImport, qtyData, ledOption, ledImport, ledData, ribbonOption, ribbonImport;
private int enableLedType, enableQtyType;
private String posId, firmId, altDate, docDate, setDate, txt, currency,  oType, conterparty;
private boolean booking;
private JButton bAddLed, bAddQty, bSaveLed, bClcQty, bClcLed, bSimLed, bSimQty, bRec, bRecQTY;
private JMenuItem   mAddLed, mAddQty;
private int qtyRow, ledRow;
private JTabbedPane tabels;
private String CurrencyRate=null;
private boolean setNewLine;
private JTextField sumCurrencyAmountJTF, sumCurrencyJTF, qtyAmountJTF, ledDtSum, LedCtSum, qtyDtSum, qtyCtSum;
private  HashMap<String, String> mapColumnPosition;
private HashMap<String, ArrayList<String>> firmsMap;
private String instr;
private JComboBox vatList;
private String dueVatAcc="300-1000-0000", incVatAcc="200-1000-0000";
private boolean isReck=false;
/**
 * Podatowyw konstruktor
 * @param x - D³ugoœæ
 * @param y - Szerokoœæ
 * @param title - tytu³
 */
	public IPostings(int x, int y, String title) 
	{
		super(x, y, title);
		enableLedType = 0;
		enableQtyType = 0;
		simpleFrame();
	}
	/**
	 * Konstruktor pod ksiêgowania rêczne
	 * @param x
	 * @param y
	 * @param title
	 * @param posId
	 * @param firmId
	 * @param docDate
	 * @param altDate
	 * @param setDate
	 * @param txt
	 * @param currency
	 * @param oType
	 * @param booking
	 */
	public IPostings(int x, int y, String title, 
			String posId, String firmId, String docDate, String altDate,  String setDate, String txt, String currency,  String oType, boolean booking) {
		super(x, y, title);
		if (booking==true)
		{
			enableLedType = 1;
			enableQtyType = 1;	
		}
		else if (oType.equals("MANPOS"))
		{
			enableLedType = 7;
			enableQtyType = 8;
		}else
		{
			enableLedType = 0;
			enableQtyType = 0;
		}
			this.posId = posId;
			this.firmId = firmId;
			this.altDate = altDate;
			this.docDate=docDate;
			this.setDate=setDate;
			this.txt= txt;
			this.currency=currency;
			this.oType= oType;
			this.booking = booking;
			firmsMap = MainEntryDataWarehouse.getFirmsMap();
			simpleFrame();	
	}
	/**
	 * Konstruktor pod ksiêgowanie
	 * @param x
	 * @param y
	 * @param title
	 * @param posId
	 * @param firmId
	 * @param docDate
	 * @param currency
	 * @param oType
	 * @param booking
	 * @param mapColumnPosition
	 */
	public IPostings(int x, int y, String title, 
			String posId, String firmId,String docDate,String currency, String oType, boolean booking, HashMap<String, String> mapColumnPosition) {
		super(x, y, title);
		if (booking==true)
		{
		enableLedType = 1;
		enableQtyType = 1;	
		}
		else if (oType.equals("MANPOS"))
		{
		enableLedType = 7;
		enableQtyType = 8;
		}else
		{
		enableLedType = 0;
		enableQtyType = 0;
		}
		this.posId = posId;
		this.firmId = firmId;
		this.docDate=docDate;
		this.oType= oType;
		this.booking = booking;
		this.currency=currency;
		firmsMap = MainEntryDataWarehouse.getFirmsMap();
		simpleFrame();
		setNewValueFromPosting(mapColumnPosition);
		
	}
	/**
	 * Konstruktor dla faktur kosztowych
	 * @param x
	 * @param y
	 * @param title
	 * @param posId
	 * @param firmId
	 * @param docDate
	 * @param altDate
	 * @param setDate
	 * @param txt
	 * @param currency
	 * @param oType
	 * @param booking
	 */
	public IPostings(int x, int y, String title, 
			String posId, String firmId, String docDate, String altDate,  String setDate, String txt, String currency,String  conterparty,  String oType, boolean booking) {
		super(x, y, title);
		if (booking==true)
		{
		enableLedType = 1;
		enableQtyType = 1;	
		}
		else if (oType.equals("MANPOS")||oType.equals("COSINV"))
		{
		enableLedType = 7;
		enableQtyType = 8;
		}else
		{
		enableLedType = 0;
		enableQtyType = 0;
		}

		this.posId = posId;
		this.firmId = firmId;
		this.altDate = altDate;
		this.docDate=docDate;
		this.setDate=setDate;
		this.txt= txt;
		this.currency=currency;
		this.conterparty = conterparty;
		this.oType= oType;
		this.booking = booking;
		firmsMap = MainEntryDataWarehouse.getFirmsMap();
		simpleFrame();
		
	}
	/**
	 * Kontruktor pod usuwanie danych i autoksiêgowania
	 * @param x
	 * @param y
	 * @param title
	 * @param posId
	 * @param firmId
	 */
	public IPostings(int x, int y, String title, 
			String posId, String firmId,String oType, boolean isDelete)
	{
		super(x, y, title);
		this.posId = posId;
		this.firmId = firmId;	
		this.oType = oType;
		enableLedType = 1;
		enableQtyType = 1;	
		firmsMap = MainEntryDataWarehouse.getFirmsMap();
		simpleFrame();
		if (isDelete==true)
		{
			deleteFromModel();
		}
		
	}
	/**
	 * Konstruktor pod generacje automatyczne
	 * @param x - szerokoœæ
	 * @param y - wysokosæ
	 * @param title - tytu³
	 * @param posId - id projektu
	 * @param firmId - firma
	 * @param oType - typ 
	 * @param parameterList -lista parametrów  
	 */
	public IPostings(int x, int y, String title, 
			String posId, String firmId,String oType, ArrayList<HashMap<String, String>> postingsList)
	{
		super(x, y, title);
		this.posId = posId;
		this.firmId = firmId;	
		this.oType = oType;
		simpleFrame();
		
	}
	
	 
	public void deleteAllInModel()
	{

		var column = new ArrayList<Object>();
		for (int i = 0; i<ledModel.getColumnCount();i++)
		{
			column.add(ledModel.getColumnName(i));
		}
		
		 while (ledModel.getRowCount() > 0) 
		 {
		 	ArrayList<Object> temp = new ArrayList<Object>();
		 	ArrayList<ArrayList<Object>> deleteItem = new ArrayList<ArrayList<Object>>();
		 	
		 	deleteItem.add(column);
		 		for (int j=0; j<ledModel.getColumnCount();j++)
		 		{
		 			temp.add(ledModel.getValueAt(0, j));
		 		}
		 	deleteItem.add(temp);
		 	FrameTemplateDataWarehouse.setOneDeleteItem(deleteItem);
		 	ledModel.removeRow(0);
		}
		 
	}
	private void simpleFrame()
	{
		
		
		qtyFrame = new FrameTemplate();
		ledFrame = new FrameTemplate();
		
		qtyFrame.JTableHelperSys("IPosting_QTY_sys.yml");
		qtySys = qtyFrame.getMapSys();
		qtySysBase = qtyFrame.getMapSysBase();
		qtySysAll = qtyFrame.getMapSysAll();
		qtyModel = getQtyModel(posId,firmId);
		qtyTable = qtyFrame.getDefaultTable(qtyModel, qtySysAll);
		qtyKey = new ArrayList<Integer>(Arrays.asList(0,1,2));
		getQTYDict(qtySysAll, qtyTable);
		
		ledFrame.JTableHelperSys("IPosting_Ledger_sys.yml");
		ledSys = ledFrame.getMapSys();
		ledSysBase = ledFrame.getMapSysBase();
		ledSysAll = ledFrame.getMapSysAll();
		ledModel = getLedgerModel(posId, firmId);
		ledTable = ledFrame.getDefaultTable(ledModel, ledSysAll);
		ledKey = new ArrayList<Integer>(Arrays.asList(0,1,2));
		
		//sumy kontrolne
		sumCurrencyJTF= new JTextField();
		sumCurrencyJTF.setEnabled(false);
		sumCurrencyJTF.setPreferredSize(new Dimension(125,20));
		
		sumCurrencyAmountJTF = new JTextField();
		sumCurrencyAmountJTF.setEnabled(false);
		sumCurrencyAmountJTF.setPreferredSize(new Dimension(125,20));
		
		qtyAmountJTF = new JTextField();
		qtyAmountJTF.setEnabled(false);
		qtyAmountJTF.setPreferredSize(new Dimension(125,20));
	
		ledDtSum = new JTextField();
		ledDtSum.setEnabled(false);
		ledDtSum.setPreferredSize(new Dimension(125,20));
		
		LedCtSum = new JTextField();
		LedCtSum.setEnabled(false);
		LedCtSum.setPreferredSize(new Dimension(125,20));
		
		qtyDtSum  = new JTextField();
		qtyDtSum .setEnabled(false);
		qtyDtSum.setPreferredSize(new Dimension(125,20));
		
		qtyCtSum = new JTextField();
		qtyCtSum.setEnabled(false);
		qtyCtSum.setPreferredSize(new Dimension(125,20));
		
		JPanel upPanel = new JPanel(new BorderLayout());
			upPanel.add(ledFrame.GetUpMenu(false), BorderLayout.PAGE_START);
				JTabbedPane Ribbon = new JTabbedPane();
					ribbonOption = new JPanel(new FlowLayout(FlowLayout.LEFT));
					ribbonImport = new JPanel(new FlowLayout(FlowLayout.LEFT));
					genQtyRibbon();
					setQtyRibbonVisible(false);
					genLedRibbon();
					setLedRibbonVisible(true);
					Ribbon.add("Opcje", ribbonOption);
					Ribbon.add("Eksport", ribbonImport);
			upPanel.add(Ribbon, BorderLayout.PAGE_END);
		JPanel downPanel = new JPanel (new BorderLayout());
			 tabels = new JTabbedPane();
				JPanel ledPanel = new JPanel(new BorderLayout());
					ledPanel.add(new JScrollPane(ledTable), BorderLayout.CENTER);
					JPanel sumPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
						sumPanel.add(new JLabel("Suma w walucie: "));
						sumPanel.add(sumCurrencyAmountJTF);
						sumPanel.add(new JLabel("Suma w bazowej: "));
						sumPanel.add(sumCurrencyJTF);
						sumPanel.add(new JLabel("Suma Dt: "));
						sumPanel.add(ledDtSum);
						sumPanel.add(new JLabel("Suma Ct: "));
						sumPanel.add(LedCtSum);
					ledPanel.add(sumPanel, BorderLayout.PAGE_START);
				JPanel qtyPanel = new JPanel(new BorderLayout());
					qtyPanel.add(new JScrollPane(qtyTable), BorderLayout.CENTER);
					JPanel sumQTY = new JPanel(new FlowLayout(FlowLayout.LEFT));
						sumQTY.add(new JLabel("Suma iloœæ"));
						sumQTY.add(qtyAmountJTF);
						sumQTY.add(new JLabel("Suma Dt"));
						sumQTY.add(qtyDtSum);
						sumQTY.add(new JLabel("Suma Ct"));
						sumQTY.add(qtyCtSum);
					qtyPanel.add(sumQTY, BorderLayout.PAGE_START);
				tabels.add("Wartoœciowe", ledPanel);
				tabels.add("Iloœciowe", qtyPanel);
				tabels.addChangeListener(this);
			downPanel.add(tabels);
		add(upPanel, BorderLayout.PAGE_START);
		add(downPanel, BorderLayout.CENTER);
		pack();
		qtyListeners();
		ledListeners();
		setSumAmountFromModel();
		if(enableLedType ==1||enableQtyType == 1)
		{
			functionEnabled();
		}
	}
	
	private void qtyListeners()
	{
		qtyModel.addTableModelListener(this);
		qtyFrame.addFrameTempListener(qtyTable, qtyModel, qtySysAll, qtySysBase, qtySys, qtyKey);
		qtyFrame.addListenerJTable(qtyTable, qtyModel);
		qtyFrame.addListenerContextMenu();
		qtyFrame.addListenerRibbon();
		
		if (oType.equals("MANPOS")||oType.equals("COSINV"))
		{
			qtyFrame.setMenuRun(7);
			qtyFrame.setMenuRun(12);
			qtyFrame.setMenuRun(13);
			qtyFrame.setMenuRun(14);
			if (oType.equals("MANPOS"))
			{
			qtyFrame.setMenuRun(15);
			}
			qtyButton();
			qtyTable.addMouseListener(this);
		}else if (oType.equals("NEWORD"))
		{
			qtyFrame.getbSave().setEnabled(false);
			qtyFrame.getbDup().setEnabled(false);
			qtyFrame.getbAdd().setEnabled(false);
			qtyFrame.isEditContextMenu(false);
		}

		
		
	}
	
	private void ledListeners()
	{
		ledModel.addTableModelListener(this);
		ledFrame.addFrameTempListener(ledTable, ledModel, ledSysAll, ledSysBase, ledSys, ledKey);
		ledFrame.addListenerJTable(ledTable, ledModel);
		ledFrame.addListenerContextMenu();
		ledFrame.addListenerRibbon();
		if (oType.equals("MANPOS")||oType.equals("COSINV"))
		{
			ledFrame.setMenuRun(7);
			ledFrame.setMenuRun(13);
			ledFrame.setMenuRun(14);
			ledFrame.setMenuRun(15);
			if (oType.equals("MANPOS"))
			{
				ledFrame.setMenuRun(16);
			}
			ledTable.addMouseListener(this);
			ledButton();
		}else if (oType.equals("NEWORD"))
		{
			ledFrame.getbSave().setEnabled(false);
			ledFrame.getbDup().setEnabled(false);
			ledFrame.getbAdd().setEnabled(false);
			ledFrame.isEditContextMenu(false);
		}
	}
	
	private void qtyButton()
	{
		bAddQty = qtyFrame.getbAdd();
		qtyFrame.remListbAdd();
		bAddQty.addActionListener(this);
		mAddQty = qtyFrame.getaddRowM();
		qtyFrame.remListmAdd();
		mAddQty.addActionListener(this);

			
	}
	
	private void ledButton()
	{
		bAddLed = ledFrame.getbAdd();
		ledFrame.remListbAdd();
		bAddLed.addActionListener(this);
		mAddLed = ledFrame.getaddRowM();
		ledFrame.remListmAdd();
		mAddLed.addActionListener(this);
		
	
	}
	
	private void ribbon()
	{
		if(tabels.getSelectedIndex()==0)
		{
			setQtyRibbonVisible(false);
			setLedRibbonVisible(true);
		}else if (tabels.getSelectedIndex()==1)
		{
			setLedRibbonVisible(false);
			setQtyRibbonVisible(true);
			
		}
		
	}
	
	
	private void genQtyRibbon()
	{
		JPanel AccStm = new JPanel(new FlowLayout(FlowLayout.LEFT));
				bClcQty = new FrameTemplateButtons().RibbonJButton("Koszty/VAT", FrameTemplateImageIcon.iconJButton_PostingSum());
				bClcQty.addActionListener(this);
				bRecQTY = new FrameTemplateButtons().RibbonJButton("Rozliczenie", FrameTemplateImageIcon.iconJButton_Reckoning());
				bRecQTY.addActionListener(this);
				AccStm.add(bClcQty);
				
		qtyData = qtyFrame.DefaultRibbonData();
		qtyOption = qtyFrame.DefaultRibbonSim();
		qtyOption.add(qtyFrame.startConterparty());
		qtyImport = qtyFrame.DefaultRibbonExp();
		qtyData.add(bClcQty);
		qtyData.add(bRecQTY);
		
		ribbonOption.add(qtyOption);
		ribbonOption.add(qtyData);
		ribbonImport.add(qtyImport);
	}
	
	private void setQtyRibbonVisible(boolean visible)
	{
		qtyData.setVisible(visible);
		qtyOption.setVisible(visible);
		qtyImport.setVisible(visible);
	}
	
	@SuppressWarnings("unchecked")
	private void genLedRibbon()
	{
			JPanel AccStm = new JPanel(new BorderLayout());
				String[] vatStrings = {"ZW","0%","5%","8%","23%"};
				vatList = new JComboBox(vatStrings);
				vatList.setSelectedIndex(4);
				vatList.setPreferredSize(new Dimension(50,20));
				bClcLed = new FrameTemplateButtons().RibbonJButton("przeciwstawne", FrameTemplateImageIcon.iconJButton_PostingSum());
				bClcLed.addActionListener(this);	
				bSimLed = new FrameTemplateButtons().RibbonJButton("lustrzane", FrameTemplateImageIcon.iconJButton_PostingMir());
				bSimLed.addActionListener(this);
				bRec = new FrameTemplateButtons().RibbonJButton("Rozliczenie", FrameTemplateImageIcon.iconJButton_Reckoning());
				bRec.addActionListener(this);
			AccStm.add(bClcLed, BorderLayout.CENTER);
			AccStm.add(vatList, BorderLayout.PAGE_START);
		ledData = ledFrame.DefaultRibbonData();
		ledOption = ledFrame.DefaultRibbonSim();
		ledOption.add(ledFrame.startConterparty());
		ledData.add(AccStm);
		ledData.add(bSimLed);
		ledData.add(bRec);
		ledImport = ledFrame.DefaultRibbonExp();

		ribbonOption.add(ledOption);
		ribbonOption.add(ledData);
		ribbonImport.add(ledImport);
	}
	private void setLedRibbonVisible(boolean visible)
	{
		ledData.setVisible(visible);
		ledOption.setVisible(visible);
		ledImport.setVisible(visible);
	}
	private DefaultTableModel getQtyModel(String ID, String Firm)
	{
		StoredProcedures sp = new StoredProcedures();
		return sp.genModelPostingsLedgerQtyTrans(ID,Firm, enableQtyType , qtySys, qtySysAll);
		
	}
	
	private DefaultTableModel getLedgerModel(String ID, String Firm)
	{
		StoredProcedures sp = new StoredProcedures();
		return sp.genModelPostingsLedgerTrans(ID,Firm, enableLedType , ledSys, ledSysAll);
		
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getType()==1&&e.getSource()==qtyModel)
		{
			this.qtyRow = e.getFirstRow();
			qtyNewRowPositions();
			setNewLine=true;
		}
		else if (e.getType()==1&&e.getSource()==ledModel)
		{
			this.ledRow = e.getFirstRow();
			ledNewRowPositions();
			setNewLine=true;
		}else if (e.getType()==0&&e.getColumn()==11&&e.getSource()==ledModel)
		{
			ledModRowPositions(e.getFirstRow());
		}
		else if (e.getType()==0&&e.getColumn()==8&&e.getSource()==ledModel)
		{
			ledModRowPositions(e.getFirstRow());
			setSumAmountFromModel();
		}else if (e.getType()==0&&e.getColumn()==9&&e.getSource()==ledModel)
		{
			setSumAmountFromModel();
		}else if (e.getType()==0&&e.getColumn()==10&&e.getSource()==ledModel)
		{
			setSumAmountFromModel();
		}else if (e.getType()==0&&e.getColumn()==8&&e.getSource()==qtyModel)
		{
			setSumAmountFromModel();
		}else if (e.getType()==0&&e.getColumn()==9&&e.getSource()==qtyModel)
		{
			setSumAmountFromModel();
		}
	}
	
	private void ledModRowPositions(  int firstRow)
	{
		double amountCurency =Double.valueOf(ledModel.getValueAt(firstRow, 8).toString().replace(",", ".")) ;
		double currencyRate =Double.valueOf(ledModel.getValueAt(firstRow, 11).toString().replace(",", ".")) ;
		double amount = FrameTemplate.round(amountCurency * currencyRate,2);
		ledModel.setValueAt(FrameTemplate.setDigits(String.valueOf(amount)), firstRow, 12);
	}
	private void ledNewRowPositions()
	{
		int row = ledRow;
		ledFrame.setRemoveFiltr();
		if (CurrencyRate==null)
		{
			CurrencyRate = getCurrency(docDate, firmsMap.get(firmId).get(0), currency);
		}
		//wy³¹czenie autouzupe³niania dla automatycznych ksiêgowañ
		if (oType.equals("COSINV")||oType.equals("MANPOS"))
		{
			ledModel.setValueAt(posId, row, 0);
			ledModel.setValueAt(firmId, row, 1);
			ledModel.setValueAt(ledModel.getRowCount(), row, 2);
			ledModel.setValueAt(docDate, row, 3);
			ledModel.setValueAt(altDate, row, 4);
			ledModel.setValueAt(setDate, row, 5);
			ledModel.setValueAt(txt, row, 6);
			if (oType.equals("COSINV"))
			{
				ledModel.setValueAt(conterparty, row,16);	
			}
			if (isReck==false)
			{
				if (ledModel.getValueAt(row,18).toString().trim().length()<1)
				{
				ledModel.setValueAt(posId, row,18);
				}
			}
			ledModel.setValueAt(oType, row,19);
			
		}
		ledModel.setValueAt(currency, row,10);	
		ledModel.setValueAt(CurrencyRate, row,11);
		
		
	}
	
	private void qtyNewRowPositions()
	{
		if (qtyModel.getRowCount()>=qtyRow)
		{
			int row = qtyRow;
			qtyModel.setValueAt(posId, row, 0);
			qtyModel.setValueAt(firmId, row, 1);
			qtyModel.setValueAt(qtyModel.getRowCount(), row, 2);
			qtyModel.setValueAt(docDate, row, 3);
			qtyModel.setValueAt(altDate, row, 4);
			qtyModel.setValueAt(setDate, row, 5);
			qtyModel.setValueAt(txt, row, 6); 
			if (oType.equals("COSINV"))
			{
				qtyModel.setValueAt(conterparty, row,15);
			}
			if (qtyModel.getValueAt(row,16).toString().trim().length()<1)
			{
			qtyModel.setValueAt(posId, row,16);
			}
			qtyModel.setValueAt(oType, row,17);
		}
	}

	@SuppressWarnings({ "unused", "unused", "unused", "unused" })
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==bAddQty||e.getSource()==mAddQty)
		{
			qtyFrame.Button_addEmptyRow(qtyModel, qtySysAll, qtyTable);
			qtyNewRowPositions();
		}else if (e.getSource()==bAddLed||e.getSource()==mAddLed)
		{
			ledFrame.Button_addEmptyRow(ledModel, ledSysAll, ledTable);
			ledNewRowPositions();
		}else if (e.getSource()==bClcLed)
		{
			ButtonsCalculon buttonSum = new ButtonsCalculon(ledTable, ledFrame, ledSysAll);
			buttonSum.buttonsSum(); 
		}else if (e.getSource()==bSimLed)
		{
			ButtonsCalculon buttonSum = new ButtonsCalculon(ledTable, ledFrame, ledSysAll);
			buttonSum.buttonsSingle(); 
		}else if (e.getSource()==bRec||e.getSource()==bRecQTY)
		{
			IReckoning reckoning = new IReckoning(e.getSource()==bRec ? true : false, firmId, posId, altDate, docDate, setDate, oType, currency);
			ArrayList<Object[]> reckoningList = reckoning.getPostingList();
			isReck = true;
				for (int i=0;i<reckoningList.size();i++)
				{
					if (e.getSource()==bRec)
					{
						ledModel.addRow(reckoningList.get(i));
					}
					else if (e.getSource()==bRecQTY)
					{
						qtyModel.addRow(reckoningList.get(i));
					}
				}
			isReck = false;
			
		}
	}
	
	
	class ButtonsCalculon
	{	
		private HashMap<String, Double> costMap = new HashMap<String, Double>(); //konto/wartoœæ w walucie suma
		private HashMap<Integer, ArrayList<Object>> costMapSin= new HashMap<Integer, ArrayList<Object>>(); //numer wiersza/ numer konta
		private HashMap<String, Integer> rowToCopy = new HashMap<String, Integer>();
		private HashMap<String, Integer> columnNumbers; //konto wartoœæ w bazowej
		private HashMap<String, ArrayList<String>> planAccount;//= getPlanAccount(true)
		private JTable table;
		private FrameTemplate frame;
		private HashMap<String, ArrayList<String>> sysAll;
		private double VAT;
		private ButtonsCalculon(JTable table, FrameTemplate frame, HashMap<String, ArrayList<String>> sysAll)
		{
			this.table = table; this.frame=frame; this.sysAll=sysAll;
			columnNumbers = getcolumnNumbers();	
			planAccount = getPlanAccount(table==ledTable ? true : false);
			HashMapMaker();
			if (table==ledTable) { VAT =  FrameTemplate.round((Double.valueOf(vatList.getSelectedItem().equals("ZW") ? "0" : vatList.getSelectedItem().toString().substring(0, vatList.getSelectedItem().toString().length()-1))/100),2); }
			else if (table==qtyTable) { VAT = 0.0; }
			System.out.println("dupa");
		}
		
		private void buttonsSum()
		{
			 double vatAmount =0;
			 boolean credit=false;
			 for (String accnum: costMap.keySet())
			 {
				 credit = costMap.get(accnum)>0? true : false;
				 String newAccNum = accountFind(accnum);
				 double Amount = FrameTemplate.round((costMap.get(accnum) * (credit==true ? 1 : -1))  / (1+VAT),2);
				 vatAmount += FrameTemplate.round(((costMap.get(accnum)* (credit==true ? 1 : -1))-Amount),2);
				// System.out.print("Dupa");
				 frame.Button_Duplicate(0);
				 table.setValueAt(credit, 0, columnNumbers.get("CREDITING"));
				 table.setValueAt(newAccNum, 0, columnNumbers.get("ACCOUNTNUM"));
				 table.setValueAt(Amount, 0, columnNumbers.get("AMOUNTCUR"));
				 table.setValueAt("", 0, columnNumbers.get("DIMENSION"));
				 table.setValueAt("", 0, columnNumbers.get("DIMENSION_2"));
				 table.setValueAt("", 0, columnNumbers.get("DIMENSION_3"));
				 
			 }
			 addVatRow( FrameTemplate.round(vatAmount,2), credit);

		}
		
		private void buttonsSingle()
		{
			double vatAmount = 0;
			boolean credit = false;
			int addC = 0;
			 for (int row: costMapSin.keySet())
			 {	 
				 int newRow= row+addC;
				 credit = (double)costMapSin.get(row).get(1)>0  ? true : false;
				 String newAccNum = accountFind(costMapSin.get(row).get(0).toString());
				 double Amount = FrameTemplate.round(((double) costMapSin.get(row).get(1) * (credit==true ? 1 : -1))  / (1+VAT),2);
				 vatAmount += FrameTemplate.round((((double) costMapSin.get(row).get(1) * (credit==true ? 1 : -1))-Amount)* (credit==true ? 1 : -1),2);
				 frame.Button_Duplicate(newRow);
				 table.setValueAt(credit, 0, columnNumbers.get("CREDITING"));
				 table.setValueAt(newAccNum, 0, columnNumbers.get("ACCOUNTNUM"));
				 table.setValueAt(Amount, 0, columnNumbers.get("AMOUNTCUR"));
				 addC++;
			 }
			 addVatRow( FrameTemplate.round(vatAmount,2), credit);
		}
		
		private void addVatRow(double vatAmount, boolean credit)
		{
			if (vatAmount!=0)
			{
				credit = vatAmount>0? true : false;
				frame.Button_Duplicate(0);
				 table.setValueAt(credit, 0, columnNumbers.get("CREDITING"));
				 table.setValueAt(credit==true ? dueVatAcc: incVatAcc, 0, columnNumbers.get("ACCOUNTNUM"));
				 table.setValueAt(FrameTemplate.round(vatAmount* (credit==true ? 1 : -1),2), 0, columnNumbers.get("AMOUNTCUR"));
				 table.setValueAt("", 0, columnNumbers.get("DIMENSION"));
				 table.setValueAt("", 0, columnNumbers.get("DIMENSION_2"));
				 table.setValueAt("", 0, columnNumbers.get("DIMENSION_3"));
			}
		}
		private String accountFind(String accnum)
		{
			String costCount = "4" + accnum.substring(1);
			if (planAccount.containsKey(costCount)) {return costCount;}
			else {return "";}	
		}
		
		
		private HashMap<String, Integer> getcolumnNumbers()
		{
			return  frame.getColumnNumbers(table, sysAll);
		}
		
		private void HashMapMaker()
		{
			for (int i=0;i<table.getRowCount();i++)
			{
				double commitment=0;
				boolean ct = (boolean) table.getValueAt(i, columnNumbers.get("CREDITING"));
				String columnName = table.getValueAt(i, columnNumbers.get("ACCOUNTNUM")).toString();
				
				
				 if (costMap.containsKey(columnName))
				 {
					 commitment = FrameTemplate.round(Double.valueOf(table.getValueAt(i, columnNumbers.get("AMOUNTCUR")).toString().replace(",", ".")) + costMap.get(table.getValueAt(i ,columnNumbers.get("ACCOUNTNUM"))) * (ct==true ? -1 : 1),2);
					 costMap.remove(columnName);
					 costMap.put(columnName, commitment);
					 costMapSin.put(i, new ArrayList<Object>(Arrays.asList(columnName, FrameTemplate.round(Double.valueOf(table.getValueAt(i, columnNumbers.get("AMOUNTCUR")).toString().replace(",", "."))* (ct==true ? -1 : 1),2))));
					  
				 }else if (planAccount.get(columnName).get(2).equals("Z")) //je¿eli mamy konto zobowi¹zañ. W dalszych pracach mo¿na podmieniæ na oznaczenie w PlanAccount
				 {
					 commitment = FrameTemplate.round(Double.valueOf(table.getValueAt(i, columnNumbers.get("AMOUNTCUR")).toString().replace(",", ".")) * (ct==true ? -1 : 1),2);
					 costMap.put(columnName, commitment);
					 costMapSin.put(i, new ArrayList<Object>(Arrays.asList(columnName, FrameTemplate.round(Double.valueOf(table.getValueAt(i, columnNumbers.get("AMOUNTCUR")).toString().replace(",", "."))* (ct==true ? -1 : 1),2))));
					 
				 }
			}
		}
		
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==tabels)
		{
			 ribbon();
		} 
	}
	
	public String getCurrency(String date, String BaseCurrency, String Currency)
	{
		StoredProcedures sp = new StoredProcedures();
		return FrameTemplate.setDigits(sp.getCurrencyToPosting (date,BaseCurrency, Currency).get(1).get(1));
	}

	public void setSumAmountFromModel()
	{
		double curAmount = 0;
		double basAmount = 0;
		double qtyAmount = 0;
		double lCtSum = 0;
		double lDtSum = 0;
		double qCtSum = 0;
		double qDtSum = 0;
		for (int i=0; i< ledModel.getRowCount();i++)
		{
			if ((boolean) ledModel.getValueAt(i, 9)==true)
			{
				curAmount += FrameTemplate.round((Double.valueOf(ledModel.getValueAt(i, 8).toString().replace(",", "."))*-1),2) ;
				basAmount += FrameTemplate.round((Double.valueOf(ledModel.getValueAt(i, 12).toString().replace(",", "."))*-1),2) ;
				lCtSum += FrameTemplate.round((Double.valueOf(ledModel.getValueAt(i, 8).toString().replace(",", "."))*-1),2) ; 
			} else if ((boolean) ledModel.getValueAt(i, 9)==false)
			{
				curAmount += FrameTemplate.round((Double.valueOf(ledModel.getValueAt(i, 8).toString().replace(",", "."))),2);
				basAmount += FrameTemplate.round((Double.valueOf(ledModel.getValueAt(i, 12).toString().replace(",", "."))),2);
				lDtSum+= FrameTemplate.round((Double.valueOf(ledModel.getValueAt(i, 8).toString().replace(",", "."))),2);
			}
		}
		
		for (int i=0; i< qtyModel.getRowCount();i++)
		{
			if ((boolean) qtyModel.getValueAt(i,9)==true)
			{
				qtyAmount += FrameTemplate.round((Double.valueOf(qtyModel.getValueAt(i, 8).toString().replace(",", "."))*-1),2);
				qCtSum+= FrameTemplate.round((Double.valueOf(qtyModel.getValueAt(i, 8).toString().replace(",", "."))*-1),2);
			}else if ((boolean) qtyModel.getValueAt(i, 9)==false)
			{
				qtyAmount += FrameTemplate.round((Double.valueOf(qtyModel.getValueAt(i, 8).toString().replace(",", "."))),2);
				qDtSum += FrameTemplate.round((Double.valueOf(qtyModel.getValueAt(i, 8).toString().replace(",", "."))),2);
			}
		}
		
		sumCurrencyAmountJTF.setText(String.valueOf(FrameTemplate.round(curAmount,2)));
		sumCurrencyJTF.setText(String.valueOf(FrameTemplate.round(basAmount,2)));
		qtyAmountJTF.setText(String.valueOf(FrameTemplate.round(qtyAmount,2)));
		ledDtSum.setText(String.valueOf(FrameTemplate.round(lDtSum,2)));
		LedCtSum.setText(String.valueOf(FrameTemplate.round(lCtSum,2)));
		qtyDtSum.setText(String.valueOf(FrameTemplate.round(qDtSum,2)));
		qtyCtSum.setText(String.valueOf(FrameTemplate.round(qCtSum,2)));
		
	}
	
	private void deleteFromModel()
	{
		ledFrame.removeAllInModel();
		qtyFrame.removeAllInModel();
		ledFrame.Button_Save();
		qtyFrame.Button_Save();
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	if (booking==false)
	{
		if (e.getSource()==ledTable)
		{
			if (e.getClickCount()==2)
			{	
				if (ledTable.getColumnName(ledTable.getSelectedColumn()).equals("Konto"))
				{
					ArrayList<String> parameters = new ArrayList<String>(Arrays.asList("1",firmsMap.get(firmId).get(1)));
					ISelectionRun jdialog = new ISelectionRun(ledTable.getValueAt(ledTable.getSelectedRow(), ledTable.getSelectedColumn()).toString() , "Dict_PlanAccount_sys.yml", "getAccountToPostings", parameters, "Konto", 0);
					ledTable.setValueAt(jdialog.getSelection(), ledTable.getSelectedRow(), ledTable.getSelectedColumn());
				}else if (ledTable.getColumnName(ledTable.getSelectedColumn()).equals("Projekt"))
				{
					ArrayList<String> parameters = new ArrayList<String>(Arrays.asList(firmId));
					ledFrame.getSelectionRunWithParameters(ledTable, " getInstrumentToPosting ",parameters, "Dict_Instruments_sys.yml", "Projekty", 0);
				}else if (ledTable.getColumnName(ledTable.getSelectedColumn()).equals("Kontrahent")&&!oType.equals("COSINV"))
				{
					ArrayList<String> parameters = new ArrayList<String>();
					ledFrame.getSelectionRunWithParameters(ledTable, "getConterparty",parameters, "Dict_Conterparty.yml", "Projekty", 0);
				}else if (ledTable.getColumnName(ledTable.getSelectedColumn()).equals("Wymiar 2"))
				{
					ArrayList<String> parameters = new ArrayList<String>(Arrays.asList("Dimension_2.Type"));
					ledFrame.getSelectionRunWithParameters(ledTable, "getDomainValue",parameters, "Dict_DomainValue.yml", "Typ", 0);
				}else if (ledTable.getColumnName(ledTable.getSelectedColumn()).equals("Wymiar 3"))
				{
					ArrayList<String> parameters = new ArrayList<String>(Arrays.asList("Dimension_3.InternalContractor"));
					ledFrame.getSelectionRunWithParameters(ledTable, "getDomainValue",parameters, "Dict_DomainValue.yml", "Kontrahent wewnêtrzny", 0);
				}
				
			}
		}else if (e.getSource()==qtyTable)
		{ 
			if (e.getClickCount()==2)
			{
				if (qtyTable.getColumnName(qtyTable.getSelectedColumn()).equals("Konto"))
				{
				ArrayList<String> parameters = new ArrayList<String>(Arrays.asList("0",firmsMap.get(firmId).get(1)));
				qtyFrame.getSelectionRunWithParameters(qtyTable,"getAccountToPostings",parameters, "Dict_PlanAccount_sys.yml", "Konto", 0);
				}else if (qtyTable.getColumnName(qtyTable.getSelectedColumn()).equals("Projekt"))
				{
					ArrayList<String> parameters = new ArrayList<String>(Arrays.asList(firmId));
					qtyFrame.getSelectionRunWithParameters(qtyTable, " getInstrumentToPosting ",parameters, "Dict_Instruments_sys.yml", "Projekty", 0);
				}else if (qtyTable.getColumnName(qtyTable.getSelectedColumn()).equals("Kontrahent")&&!oType.equals("COSINV"))
				{
					ArrayList<String> parameters = new ArrayList<String>();
					qtyFrame.getSelectionRunWithParameters(qtyTable, "getConterparty",parameters, "Dict_Conterparty.yml", "Projekty", 0);
				}else if (qtyTable.getColumnName(qtyTable.getSelectedColumn()).equals("Wymiar 2"))
				{
					ArrayList<String> parameters = new ArrayList<String>(Arrays.asList("'Dimension_2.Type'"));
					qtyFrame.getSelectionRunWithParameters(qtyTable, " getDomainValue ",parameters, "Dict_DomainValue.yml", "Typ", 0);
				}else if (qtyTable.getColumnName(qtyTable.getSelectedColumn()).equals("Wymiar 3"))
				{
					ArrayList<String> parameters = new ArrayList<String>(Arrays.asList("'Dimension_3.InternalContractor'"));
					qtyFrame.getSelectionRunWithParameters(qtyTable, " getDomainValue ",parameters, "Dict_DomainValue.yml", "Kontrahent wewnêtrzny", 0);
				}
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
	
	
	
	public void setNewValueFromPosting(HashMap<String, String> columnValueMap)
	{
		setNewValueToModel(ledModel, columnValueMap, ledSysAll);
		setNewValueToModel(qtyModel, columnValueMap, qtySysAll);
 
	}
	
	private void setNewValueToModel(DefaultTableModel Model, HashMap<String, String> columnValueMap, HashMap<String, ArrayList<String>> sysMapAll)
	{
		for (int i =0; i<Model.getRowCount();i++)
		{
			for (int j=0;j<Model.getColumnCount();j++)
			{
				if (columnValueMap.containsKey(sysMapAll.get(Model.getColumnName(j)).get(0))==true)
				{
					if (sysMapAll.get(Model.getColumnName(j)).get(0).equals("CURRENCYCODE"))
					{
						if (!sysMapAll.get(Model.getColumnName(j)).get(0).equals(Model.getValueAt(i, j)))
						{
							Model.setValueAt(columnValueMap.get(sysMapAll.get(Model.getColumnName(j)).get(0)), i, j);
							if (CurrencyRate==null)
							{
								CurrencyRate = getCurrency(docDate, firmsMap.get(firmId).get(0), Model.getValueAt(i, j).toString());
							}
							for(int k=0;k<Model.getColumnCount();k++)
							{
								if (sysMapAll.get(Model.getColumnName(k)).get(0).equals("CURRENCY"))
								{
									Model.setValueAt(CurrencyRate, i, k);
									break;
								}
							}
						}else
						{
							Model.setValueAt(columnValueMap.get(sysMapAll.get(Model.getColumnName(j)).get(0)), i, j);
						}
						
					}else
					{	
						if (columnValueMap.get(sysMapAll.get(Model.getColumnName(j)).get(0)).equals(null))
						{
							Model.setValueAt("", i, j);
						}else if (sysMapAll.get(Model.getColumnName(j)).get(2).equals("bit"))
						{
						
								Model.setValueAt(Boolean.valueOf(columnValueMap.get(sysMapAll.get(Model.getColumnName(j)).get(0))), i, j);
						
						}
						else
						{
							Model.setValueAt(columnValueMap.get(sysMapAll.get(Model.getColumnName(j)).get(0)), i, j);
						}
					}
				}
			}
		}
	}
	
	public void saveButtons()
	{
		ledFrame.Button_Save();
		qtyFrame.Button_Save();
	}
	
	private HashMap<String, ArrayList<String>> getPlanAccount(boolean isLed)
	{
		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		ArrayList<String> parameters = new ArrayList<String>(Arrays.asList(isLed==true ? "1" : "0",firmsMap.get(firmId).get(1))) ;
		ArrayList<ArrayList<String>> arrayList = new StoredProcedures().genUniversalArray("getAccountToPostings", parameters);
		for (int i =1; i<arrayList.size();i++)
		{ 
		 
			result.put(arrayList.get(i).get(0).toString(), arrayList.get(i));
		}
		return result;
		
	}
	
	private void getQTYDict (HashMap<String, ArrayList<String>> mapsysall, JTable table) 
	{
		
		try {
			PythonBase readers = new PythonBase();
			ArrayList<ArrayList<String>> templist;
			templist = readers.FromBase(false, "IPosting_QTY_sys_dict .yml");
			txt txtlist = new txt();
			Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
			qtyFrame.CommboBoxDefault(dictdata, mapsysall, table);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public double getSumCurrency()
	{
		//sumCurrencyAmountJTF, sumCurrencyJTF, qtyAmountJTF
		return FrameTemplate.round((Double.valueOf(sumCurrencyAmountJTF.getText())),2);	
	}
	public double getSumAmount()
	{
		return FrameTemplate.round((Double.valueOf(sumCurrencyJTF.getText())),2);	
	}
	public double getSumQTY()
	{
		return FrameTemplate.round((Double.valueOf(qtyAmountJTF.getText())),2);	
	}
	public DefaultTableModel getLedModel()
	{
		return ledModel;
	}
	public DefaultTableModel getQtyModel()
	{
		return qtyModel;
	}
	
	public void addAutoRowsToModel(ArrayList<Object[]> list, boolean isQty)
	{ 
			for (int i=1; i<list.size();i++)
			{
				if (isQty==true) { 
					this.docDate =String.valueOf(list.get(i)[3]);
					this.setDate =String.valueOf(list.get(i)[4]);
					this.altDate =String.valueOf(list.get(i)[5]);
					qtyModel.addRow(list.get(i));
					
					}
				else {
				//	HashMap<String, Integer> columnMap = ledFrame.getColumnNumbers(list.get(0));
					this.currency =(String) list.get(i)[10];
					this.docDate =String.valueOf(list.get(i)[3]);
					this.setDate =String.valueOf(list.get(i)[4]);
					this.altDate =String.valueOf(list.get(i)[5]);

					ledModel.addRow(list.get(i));
				}	
			}
			setSumAmountFromModel();
			double sumCurCh = Double.valueOf(sumCurrencyJTF.getText());
			double sumAmoCh = Double.valueOf(sumCurrencyAmountJTF.getText());
			
			if (sumCurCh!=0||sumAmoCh!=0)
			{
				JOptionPane.showMessageDialog(null, "Ksiêgowanie o numerze: " + list.get(0)[0] + " nie sumuje siê do zera! Nie zostanie zaksiêgowane." , "Informacja", JOptionPane.INFORMATION_MESSAGE);
				HashMap<String, Integer> columnMap = ledFrame.getColumnNumbers(ledModel, ledSysAll);
				for (int i=0; i<ledModel.getRowCount();i++)
				{
					ledModel.setValueAt(false, i, columnMap.get("BOOK"));
					
				}
			}
			qtyFrame.Button_Save();
			ledFrame.Button_Save();
	}
	public void addAutoRowsToModel(ArrayList<Object[]> list, boolean isQty, boolean book)
	{  			
			
			for (int i=0; i<list.size();i++)
			{
				if (isQty==true) { 
					this.docDate =String.valueOf(list.get(i)[3]);
					this.setDate =String.valueOf(list.get(i)[4]);
					this.altDate =String.valueOf(list.get(i)[5]);
					this.conterparty = String.valueOf(list.get(i)[15]);
					this.txt =  String.valueOf(list.get(i)[6]);
					qtyModel.addRow(list.get(i));
					
					}
				else {
				//	HashMap<String, Integer> columnMap = ledFrame.getColumnNumbers(list.get(0));
					this.currency =(String) list.get(i)[10];
					this.docDate =String.valueOf(list.get(i)[3]);
					this.setDate =String.valueOf(list.get(i)[4]);
					this.altDate =String.valueOf(list.get(i)[5]);
					this.conterparty = String.valueOf(list.get(i)[16]);
					this.txt =  String.valueOf(list.get(i)[6]);
					ledModel.addRow(list.get(i));
				}	
			}
			setSumAmountFromModel();
			double sumCurCh = Double.valueOf(sumCurrencyJTF.getText());
			double sumAmoCh = Double.valueOf(sumCurrencyAmountJTF.getText());
			
			if ((sumCurCh!=0||sumAmoCh!=0)&&book==true)
			{
				JOptionPane.showMessageDialog(null, "Ksiêgowanie o numerze: " + list.get(0)[0] + " nie sumuje siê do zera! Nie zostanie zaksiêgowane." , "Informacja", JOptionPane.INFORMATION_MESSAGE);
				HashMap<String, Integer> columnMap = ledFrame.getColumnNumbers(ledModel, ledSysAll);
				for (int i=0; i<ledModel.getRowCount();i++)
				{
					ledModel.setValueAt(false, i, columnMap.get("BOOK"));
					
				}
			}
			qtyFrame.Button_Save();
			ledFrame.Button_Save();
	}	
	private void functionEnabled()
	{
		qtyFrame.isEditContextMenu(false);
		qtyFrame.isEditRibbon(false);
		ledFrame.isEditContextMenu(false);
		ledFrame.isEditRibbon(false);
		bClcQty.setEnabled(false); 
		bClcLed.setEnabled(false);  
		bSimLed.setEnabled(false); 
		ledFrame.getbDup().setEnabled(false);
		qtyFrame.getbDup().setEnabled(false);
		bRec.setEnabled(false);
		bRecQTY.setEnabled(false);
	//	bSimQty.setEnabled(false); 
	}
	
}
