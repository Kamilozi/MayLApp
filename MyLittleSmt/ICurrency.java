package MyLittleSmt;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;



public class ICurrency extends FrameTemplateWindow implements ActionListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FrameTemplate ImportData;
	private HashMap<String, String> mapsys, mapsysBase;
	private HashMap<String, ArrayList<String>> mapsysAll;
	private HashMap<String, String> CHSYS, BASYS, MASYS, CHSYSBA,  MASYSBA;//BASYSBA
	private HashMap<String, ArrayList<String>> CHSYSALL, BASYSALL, MASYSALL;
	private DefaultTableModel Model;
	private DefaultTableModel CHModel, BAModel, MAModel;
	private PythonBase readers = new PythonBase();
	private JTable CHTable, BATable, MATable;
	private JButton webImp;
	ArrayList<String> CHColumnB, BAColumnB,MAColumnB,Columns;
	private JSpinner spinner0, spinner1;
	private String FilePath = "C:\\MyLittleSmt\\XML";
	private ArrayList<Integer> MATableKey,CHTableKey,BATableKey, TableKey;
	private FrameTemplate maFrame, chFrame, baFrame;
	FrameTemplateAlterModel altModMA, altModCH, altModBA;
	public ICurrency(int x, int y, String title)
	{
		super(x, y, title);
		
		ImportData = new FrameTemplate();
		ImportData.JTableHelperSys("ICurrency_sys.yml");
		mapsys = ImportData.getMapSys();
		mapsysAll = ImportData.getMapSysAll();
		mapsysBase = ImportData.getMapSysBase();
		Model = getData(0);
		JTable Table = ImportData.getDefaultTable(Model, mapsysAll);
		CHColumnB = new ArrayList<String>(Arrays.asList("CurrencyCode", "CurrencyName"));
		BAColumnB = new ArrayList<String>(Arrays.asList("BaseCurrency", "BaseCurrencyName"));
		MAColumnB = new ArrayList<String>(Arrays.asList("BaseCurrency","CurrencyCode", "Converter", "CurrencyDate", "ExchangeRate","ExchangeRatePerUnit"));


		Columns = new ArrayList<String>(Arrays.asList("BaseCurrency","BaseCurrencyName", "CurrencyCode","CurrencyName", "Converter", "CurrencyDate", "ExchangeRate","ExchangeRatePerUnit"));
		
		MATableKey = new ArrayList<Integer>(Arrays.asList(0, 1, 3));
		CHTableKey = new ArrayList<Integer>(Arrays.asList(0));
		BATableKey = new ArrayList<Integer>(Arrays.asList(0));
		
		///TableKey Modelu g³ównego
		TableKey = new ArrayList<Integer>(Arrays.asList(0, 2, 5));
		
		///Model pochodny, tabela podstawowa
		altModMA = new FrameTemplateAlterModel(); ///tworzenie nowego obiektu
		altModMA.genModelHashMaps(mapsys, mapsysBase, mapsysAll, MAColumnB); //Tworzenie map pomocniczych
		MASYS = altModMA.getNewMapSys(); // pobieranie mapSys
		MASYSBA = altModMA.getNewMapBase();//pobieranie mapSysBase
		MASYSALL = altModMA.getNewSysMapAll(); //pobieranie mapSysAll
		MAModel = altModMA.genModelFromModel(0, null, Model, MAColumnB, altModMA.getNewMapSys());//Tworzenie modelu
		MAModel.addTableModelListener(altModMA); ///dodawanie listenera*
		//Model pochdny, tabela wyboru waluty kursu
		altModCH =new FrameTemplateAlterModel();
		altModCH.genModelHashMaps(mapsys, mapsysBase,mapsysAll, CHColumnB);
		CHSYS = altModCH.getNewMapSys();
		CHSYSBA = altModCH.getNewMapBase();
		CHSYSALL = altModCH.getNewSysMapAll();
		CHModel = altModMA.genModelFromModel(1, null, Model, CHColumnB,CHSYS);
		//Model pochodny, tabela wyboru waluty bazowej
		altModBA = new FrameTemplateAlterModel();
		altModBA.genModelHashMaps(mapsys, mapsysBase, mapsysAll, BAColumnB);
		BASYS = altModBA.getNewMapSys();
	//	BASYSBA = altModBA.getNewMapBase();
		BASYSALL = altModBA.getNewSysMapAll();
		BAModel = altModBA.genModelFromModel(1, null, Model, BAColumnB, BASYS);
		
		//Budowa tabel na podstawie stworzonych ju¿ Modeli
		maFrame = new FrameTemplate();
		chFrame = new FrameTemplate();
		baFrame = new FrameTemplate();
		
		MATable = maFrame.getDefaultTable(MAModel, MASYSALL);
		try {
			getDict(MASYSALL, MATable);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		CHTable = chFrame.getDefaultTable(CHModel, CHSYSALL);
		BATable = baFrame.getDefaultTable(BAModel, BASYSALL);
		
		CHTable.addMouseListener(this);
		BATable.addMouseListener(this);
		modModel();
		

		CHTable.setCellSelectionEnabled(false);
		CHTable.setRowSelectionAllowed(true);		
		CHTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		BATable.setCellSelectionEnabled(false);
		BATable.setRowSelectionAllowed(true);		
		BATable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		JScrollPane chJSP = new JScrollPane(CHTable);
		JScrollPane baJSP = new JScrollPane(BATable);
		JScrollPane maJSP = new JScrollPane(MATable);

		
		JPanel upPanel = new JPanel(new BorderLayout());
		upPanel.add(ImportData.GetUpMenu(false), BorderLayout.PAGE_START);	
			JTabbedPane JTPRibbon = new JTabbedPane();
				JPanel ROption = new JPanel(new FlowLayout(FlowLayout.LEFT));
					JPanel JPOption = ImportData.DefaultRibbonSim();
					JPanel JPOptCus = new JPanel(new FlowLayout(FlowLayout.LEFT));
						webImp = new JButton("Import NBP", FrameTemplateImageIcon.iconJButton_DowloadURL());
						webImp.addActionListener(this);
						webImp.setVerticalTextPosition(SwingConstants.BOTTOM);
						webImp.setHorizontalTextPosition(SwingConstants.CENTER);
						webImp.setContentAreaFilled(false);
						webImp.setBorderPainted(false);
							JPanel JPSpin = new JPanel(new GridLayout(2,2,1,1));
							JPSpin.setBorder(new TitledBorder("Daty importu"));
								JLabel jlDate0 = new JLabel("Od");
								JLabel jlDate1 = new JLabel("Do");
								spinner0 = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
								JSpinner.DateEditor editor0 = new JSpinner.DateEditor(spinner0, "dd-MM-yyyy");
								spinner0.setEditor(editor0);
							
								spinner1 = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
								JSpinner.DateEditor editor1 = new JSpinner.DateEditor(spinner1, "dd-MM-yyyy");
								spinner1.setEditor(editor1);
							JPSpin.add(jlDate0);							
							JPSpin.add(spinner0);	
							JPSpin.add(jlDate1);
							JPSpin.add(spinner1);
							
						JPOptCus.add(webImp);
						JPOptCus.add(JPSpin);
				ROption.add(JPOption);
				ROption.add(JPOptCus);
				JPanel RExpImp = new JPanel(new FlowLayout(FlowLayout.LEFT));
					JPanel JExmp = ImportData.DefaultRibbonExp();
					JPanel JImp =ImportData.DefaultRibbonImp();
				RExpImp.add(JExmp);
				RExpImp.add(JImp);
				JTPRibbon.add("Opcje", ROption);
				JTPRibbon.add("Eksport",RExpImp);
		upPanel.add(JTPRibbon, BorderLayout.PAGE_END);
		add(upPanel, BorderLayout.PAGE_START);
		
		JPanel downPanel = new JPanel(new BorderLayout());
			JPanel leftPanel = new JPanel(new GridLayout(2,1));
				JPanel leftUpPanel = new JPanel (new BorderLayout());
					leftUpPanel.setBorder(new TitledBorder("Waluta"));
					leftUpPanel.add(chJSP, BorderLayout.CENTER);
				JPanel leftDownPanel = new JPanel(new BorderLayout());
					leftDownPanel.setBorder(new TitledBorder("Waluta Bazowa"));
					leftDownPanel.add(baJSP, BorderLayout.CENTER);		
				leftPanel.add(leftUpPanel);
				leftPanel.add(leftDownPanel);
			JPanel rightPanel = new JPanel(new BorderLayout()); 	
				rightPanel.setBorder(new TitledBorder("Kurs"));
				rightPanel.add(maJSP , BorderLayout.CENTER);

		downPanel.add(leftPanel, BorderLayout.LINE_START);
		downPanel.add(rightPanel, BorderLayout.CENTER);
		add(downPanel);

		
		//dodawanie listenera i mechanizmów do ca³oœci
		ImportData.addFrameTempListener(Table, Model, mapsysAll, mapsysBase,mapsys, TableKey);
		ImportData.addListenerRibbon();
		ImportData.addListenerJTable(Table, Model);
		ImportData.addListenerModel(Model);
		
		
		maFrame.addFrameTempListener(MATable, MAModel, MASYSALL, MASYSBA, MASYS, MATableKey);
		maFrame.addContextMenu(MATable);
		maFrame.AutSorterJTable(MATable, 0, false);
		maFrame.addListenerJTable(MATable, MAModel);
		maFrame.addListenerContextMenu();
		chFrame.addFrameTempListener(CHTable, CHModel, CHSYSALL, CHSYSBA, CHSYS, CHTableKey);
		chFrame.addContextMenu(CHTable);
			chFrame.getpasteM().setEnabled(false);
			chFrame.getdeleteM().setEnabled(false);
			chFrame.getaddRowM().setEnabled(false);
			chFrame.getFiltr().setEnabled(false);
			chFrame.getUsuFiltM().setEnabled(false);
		chFrame.AutSorterJTable(CHTable, 0, false);
		chFrame.addListenerJTable(CHTable);	
		
		//dodaje modelListener
		HashMap<Integer, ArrayList<Integer>> MAKeyMap = new HashMap<Integer, ArrayList<Integer>>();
		HashMap<Integer, DefaultTableModel> MAmapModel = new HashMap<Integer, DefaultTableModel>();
		MAKeyMap.put(0, new ArrayList<Integer>(Arrays.asList(1)));
		MAKeyMap.put(1, new ArrayList<Integer>(Arrays.asList(0)));
		MAmapModel.put(0, CHModel);
		MAmapModel.put(1, BAModel);
		//MAModel.addTableModelListener(altModMA);
		altModMA.addMyModelListener(Model, MAModel,MAmapModel,MAKeyMap, mapsysAll, MATableKey);
		MAModel.addTableModelListener(Table);
	}
	
	
	
	public boolean ModelContains(DefaultTableModel Model, Object Value)
	{
		for (int i=0; i<Model.getRowCount();i++)
		{
			for (int j=0; j<Model.getColumnCount(); j++)
			{
				if (Model.getValueAt(i, j).equals(Value))
				{
					return false;
				}
			}
		}
		return true;
		
	}
	
	private void modModel()
	{
		altModMA.genDataToModel(Model, MAModel, MATableKey);
		altModCH.genDataToModelDistinctKey(Model, CHModel, CHTableKey);
		altModBA.genDataToModelDistinctKey(Model, BAModel, BATableKey);
		setToMainEntryDataWarehouse();
	}
	
	private void setToMainEntryDataWarehouse()
	{
		MainEntryDataWarehouse.setCurrency(ImportData.FullModelToStringList(true, MAModel, MASYSALL));
	}
	private DefaultTableModel getData(int Enable)
	{
		
		try {
			return readers.getDataToJT(Enable, mapsys, mapsysAll, "ICurrency.yml");
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<ArrayList<Object>> ListLog = new ArrayList<ArrayList<Object>>();
		ILogAddToLog AddLog = new ILogAddToLog();
		
		
		if (e.getSource()==webImp)
		{
			String NewFormat = "yyyy-MM-dd";
			String OldFormat = "EEE MMM dd HH:mm:ss Z yyyy";
		
			String strStartDate = spinner0.getValue().toString();
			String strEndtDate = spinner1.getValue().toString();
			//String newDateString;

			SimpleDateFormat sdf = new SimpleDateFormat(OldFormat, Locale.ENGLISH);
			SimpleDateFormat sdf2 = new SimpleDateFormat(OldFormat, Locale.ENGLISH);
			 
		try {
				Date ds = sdf.parse(strStartDate);
				sdf.applyPattern(NewFormat);
				strStartDate = sdf.format(ds);
			
				Date de = sdf2.parse(strEndtDate);
				sdf2.applyPattern(NewFormat);
				strEndtDate = sdf2.format(de);
				AddLog.AddToLog("Rozpoczêcie importu kursów œrednich NBP dla dat " + strStartDate + "..." + strEndtDate, "(I)");
				java.sql.Date startDate = java.sql.Date.valueOf(strStartDate);
				java.sql.Date endDate = java.sql.Date.valueOf(strEndtDate);
			
				if (endDate.after(startDate)||endDate.equals(startDate))
				{
					StringBuilder urlString = new StringBuilder();
					//MainEntryDataWarehouse medw = new MainEntryDataWarehouse();
					//ArrayList<ArrayList<String>> WorkDayYear = medw.getCalendar();
						Calendar cal = Calendar.getInstance();
						cal.setTime(startDate);
					//	int year = cal.get(Calendar.YEAR);
						StoredProcedures sp = new StoredProcedures();
					ArrayList<ArrayList<String>> DateList =  sp.NumberOfDaysNBP(strStartDate, strEndtDate);
				
				ArrayList<ArrayList<String>> ImportRate =  new ArrayList<ArrayList<String>>();
				ArrayList<String> columnName = new ArrayList<String>();
				for (int t=0;t<Model.getColumnCount(); t++)
				{
					columnName.add(Model.getColumnName(t)); 
				}
				ImportRate.add(columnName);
				
				for (int i=1; i<DateList.size();i++)
				{	
					
					if (!DateList.get(i).get(1).equals("0"))
					{	
						urlString = new StringBuilder();
						urlString.append("https://www.nbp.pl/kursy/xml/a");
						java.sql.Date checkDate = java.sql.Date.valueOf(DateList.get(i).get(0));
						Calendar calCheck = Calendar.getInstance();
						calCheck.setTime(checkDate);
						int monthCheckInt =calCheck.get(Calendar.MONTH) + 1;
						String monthCheck = String.valueOf(monthCheckInt);
						String dayCheck = String.valueOf(calCheck.get(Calendar.DAY_OF_MONTH));
						String yearCheck = String.valueOf(calCheck.get(Calendar.YEAR));
						yearCheck = yearCheck.substring(2, 4);
						if (dayCheck.length()==1) dayCheck= "0" + dayCheck;
						if (monthCheck.length()==1) monthCheck= "0" + monthCheck; 
						String number = DateList.get(i).get(1);
						if (number.length()==1) {number = "00" + number;} else if (number.length()==2) {number = "0" + number;}
						
						
						urlString.append(number + "z" + yearCheck + monthCheck + dayCheck + ".xml");
						String FileName = "NBP_" + yearCheck + monthCheck + dayCheck + ".xml";
						AddLog.AddToLog("Rozpoczêcie importu dla daty " + DateList.get(i).get(0), "(I)");
						ListLog.addAll(AddLog.getListLog());
						PythonOther py = new PythonOther();
						py.importXMLFromNBP(FileName, FilePath, urlString.toString());
						ListLog.addAll(py.getListLog());
						xmlFile xml = new xmlFile();
						
						HashMap<Integer, HashMap<String, String>> map = xml.SimpleXMLImp("C:\\MyLittleSmt\\XML\\NBP_220722.xml", "pozycja");
						
						for (Integer j:map.keySet())
						{
							ArrayList<String> templist = new ArrayList<String>();
							templist.add("PLN");
							templist.add("z³oty polski");
							templist.add(map.get(j).get("kod_waluty"));
							templist.add(map.get(j).get("nazwa_waluty"));
							//String val = String.valueOf().replace(",", ".");
							templist.add(FrameTemplate.setDigits(map.get(j).get("przelicznik")));
							templist.add(DateList.get(i).get(0));
							templist.add(FrameTemplate.setDigits((map.get(j).get("kurs_sredni")).replace(",", ".")));
							templist.add(FrameTemplate.setDigits(String.valueOf((new BigDecimal((map.get(j).get("kurs_sredni")).replace(",", "."))).divide(new BigDecimal((map.get(j).get("przelicznik")).replace(",", "."))))));
						ImportRate.add(templist);
 
						}	
					}else if (DateList.get(i).get(1).equals("0"))
						AddLog.AddToLog("Data " + DateList.get(i).get(0) + " pominiêta jako dzieñ nieroboczy", "(W)");
				}
				
				DataImport dataimport= new DataImport();
				ArrayList<ArrayList<String>> ToJTable = dataimport.DataToJTableNBP(mapsysAll, ImportData.FullModelToStringList(true, Model, mapsysAll) , TableKey, ImportRate);
				DefaultTableModel changeModel=new DefaultTableModel();
				for (int i=0; i<Model.getColumnCount();i++)
				{
					changeModel.addColumn(Model.getColumnName(i));
				}
				if (ToJTable!=null)
				{
					for (int i=1;i<ToJTable.size();i++)
					{	
						Object[] tempobj = new Object[Model.getColumnCount()];
						for (int j=0; j<ToJTable.get(i).size();j++)
						{
							tempobj[j] = ToJTable.get(i).get(j);
						}
						Model.addRow(tempobj);
						changeModel.addRow(tempobj);
					}
				}
				//getJTableData(changeModel);
				modModel();
				ListLog.addAll(AddLog.getListLog());
				ListLog.addAll(dataimport.getListLog());
				

			}else
			{
				AddLog.AddToLog("Data " + strStartDate + " nie mo¿e byæ starsza od daty " + strEndtDate, "(I)");
			} 
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ListLog.addAll(AddLog.getListLog());
		ILog log = new ILog(300, 600, "Log importu", ListLog);
		log.setVisible(true);
	  }	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==CHTable)
		{
			 
			maFrame.setFilter(String.valueOf(CHTable.getValueAt(CHTable.getSelectedRow(), 0)), 1);
		}else if (e.getSource()==BATable)
		{
			maFrame.setFilter(String.valueOf(BATable.getValueAt(BATable.getSelectedRow(), 0)), 0);
		}
		
	}

	private void getDict (HashMap<String, ArrayList<String>> mapsysall, JTable table) throws IOException
	{
		PythonBase readers = new PythonBase();
		ArrayList<ArrayList<String>> templist = readers.FromBase(false, "ICurrency_dict.yml");
		txt txtlist = new txt();
		Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
		maFrame.CommboBoxDefault(dictdata, mapsysall, table);	
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
 

}
