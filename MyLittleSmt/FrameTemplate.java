package MyLittleSmt;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
///import MyLittleSmt.FrameTemplate.MyListDataListener;

//import MyLittleSmtFrame.ComboItem;
import MyLittleSmtFrame.ISelectionRun;



public class FrameTemplate   
{
private ICompanySelection select;
private JButton firm; 
private static String FirmCode;
private JMenuItem addRowM, deleteM, UsuFiltM, filtr, cocel,coall,corow, excelM, pasteM, csvM, addSour, openSour, delSour, showSour;
private JMenuItem  SaveAllUp, ExcelUp, ImpUp, DocuUp, csvUp;
private JMenuItem AddRowMUp;
private JPopupMenu popup;
private JButton bXLS=null, bCSV=null, bImp=null, bDoc=null, bSave=null, bAdd=null, bDup=null, bSel=null, bPos=null, bCon=null;
private int[] key;
private FrameTemplateListener listener;
private HashMap<String, String> mapsys, mapsysBase; 
private HashMap<String, ArrayList<String>> mapsysAll;
private JTable table;
private DefaultTableModel Model;
private JPanel smallButtons;
private JButton smallButtonAdd, smallButtonSave, smallButtonExcel, smallButtonCSV, smallButtonImport, smallButtonDoc;
private ArrayList<ArrayList<String>> oryginalDictList;
private ArrayList<ArrayList<String>> ListFromBase;


///ListenersClass
//private DefaultTableModel Model;
	///Tutaj umieszczam get*
	///
	///
	///Dodatkowe przyciski 



public void cancelEditing() { 
if (table.getCellEditor() != null) 
{ 
     table.getCellEditor().cancelCellEditing(); 
} 
} 
	public JTable setModel(JTable table, DefaultTableModel Model)
	{
		table.setModel(Model);
		return table;
	}

	 public void SortFals()
	 {
		 listener.RowSorterFalse();
	 }
	
	public void Button_addEmptyRow(DefaultTableModel Model, HashMap<String, ArrayList<String>> sysall, JTable table)
	{	
		listener.runEmptuRow(Model, sysall, table);
	}
	public void Button_addEmptyRow(DefaultTableModel Model, HashMap<String, ArrayList<String>> sysall, JTable table, int row)
	{	
		listener.runEmptuRow(Model, sysall, table, row);
	}
	
	public void Button_Save()
	{
		listener.Button_Save();
	}
	public void Button_Save(String addKeyDate, String addKeyFirm)
	{
		listener.Button_Save();
	}
	public void Button_Duplicate(int row)
	{
		listener.runDuplicateButton(row);
	}
	
	
	public void Button_XLS(DefaultTableModel Model) 
	{
		listener.JTExcel(Model);
	}
	
	public void Button_CSV(DefaultTableModel Model)
	{
		listener.JTCSV(Model);
	}
	
	public void Button_Docum(HashMap<String, ArrayList<String>> sysall, JTable table)
	{
		listener.ImpDocSimpl(sysall, table);
	}
	
	public void Button_Import(HashMap<String, ArrayList<String>> sysall)
	{
		listener.importData(sysall, null, null, 0);
	}
	

	/**
	 * Metoda pomagajπca szybko zrzuciÊ podstawowe elementy JTable
	 * @param queryName - nazwa zapytania systemowego.
	 */
	
	public void JTableHelperSys(String queryName)
	{
		PythonBase sysread = new PythonBase();
		try {
			ArrayList<ArrayList<String>> syslist = sysread.FromBase(true, queryName);
			mapsys = HashMapMask(syslist);
			mapsysAll = HashMapAll(syslist);
			mapsysBase = HashMapBase(syslist);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * Metoda pomagajπca szybko zrzuciÊ podstawowe elementy JTable z dodatkowπ kolumnπ Check
	 * @param queryName
	 * @param isCheck
	 */
	public void JTableHelperSys(String queryName, boolean isCheck)
	{
		PythonBase sysread = new PythonBase();
		try {
			ArrayList<ArrayList<String>> syslist = sysread.FromBase(true, queryName);
			mapsys = HashMapMask(syslist);
			mapsysAll = HashMapAll(syslist);
			mapsysBase = HashMapBase(syslist);
			if (isCheck==true)
			{
			mapsys.put("Check", "Check");
			mapsysBase.put("Check", "Check");
			mapsysAll.put("Check", new ArrayList<String>(Arrays.asList("Check", "NO", "bit", "0")));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//		
	//mapsysall.put("Check", new ArrayList<String>(Arrays.asList("RecipientOnInvoice", "NO", "bit", "0")));
	/**
	 * Modyfikator dostÍpu do map kolumn
	 * @return - s≥ownik mapa vs alias
	 */
	public HashMap<String, String> getMapSys()
	{
		return mapsys;
	}
	/**
	 * Modyfikator dostÍpu do podstawowego s≥ownika sys tabeli
	 * @return - s≥ownik podstawowy sys
	 */
	public HashMap<String, ArrayList<String>> getMapSysAll()
	{
		return mapsysAll;
	}
	/**
	 * Modyfikator dostÍpu do s≥ownika kolumna vs tabela
	 * @return - s≥ownik kolumna oryginalna vs tabela.
	 */
	public HashMap<String, String> getMapSysBase()
	{
		return mapsysBase;
	}
	
	public int getAddRow()
	{
		return listener.getAddRowNum();
	}
	public void addSkipList(ArrayList<Integer> list)
	{
		listener.addSkipList(list);
	}
	///
	///
	///przerabianie danych tabeli z bazy 
	///
	///
	public HashMap<String, ArrayList<String>> HashMapTable (ArrayList<ArrayList<String>> templist)
	{
		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		result = null;
		for (ArrayList<String> t1 : templist )
		{
			for (String t0: t1)
			{
				System.out.print(t0);
			}
		}
		
		
		return result;
	}
	
	public HashMap<String, String> HashMapMask(ArrayList<ArrayList<String>> templist)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		for (int i = 1 ; i < templist.size(); i++)
		{
			result.put(templist.get(i).get(1), templist.get(i).get(5));
		}
		
		return result;
	}
	
	public HashMap<String, String> HashMapMaskRev (ArrayList<ArrayList<String>> templist)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		for (int i=1; i<templist.size(); i++)
		{
			result.put(templist.get(i).get(1), templist.get(i).get(5));
		}
		
		return result;
	}
	

	public HashMap<String, ArrayList<String>> HashMapAll (ArrayList<ArrayList<String>> templist)
	{
		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		for (int i = 1; i<templist.size(); i++)
		{
			ArrayList<String> newtemplist = new ArrayList<String>();
			for (int q = 1; q<templist.get(i).size(); q++)
			{
				if (q==1||q==2||q==3||q==4||q==6)
				{
					newtemplist.add(templist.get(i).get(q));
				}
			}
			result.put(templist.get(i).get(5), newtemplist);
		}
		
		
		return result;
	}
	
	public HashMap<String, String> HashMapBase (ArrayList<ArrayList<String>> templist)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		for (int i = 1; i<templist.size(); i++)
		{
			result.put(templist.get(i).get(1), templist.get(i).get(0));
		}
		
		return result;
	}	
	
	public JPanel getSmallButtons()
	{
		
		JPanel smallButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// smallButtons.setBorder(BorderFactory.createLineBorder(Color.gray));
		//smallButtons.setBorder(null);
			smallButtonAdd = new JButton();
			smallButtonAdd.setIcon(FrameTemplateImageIcon.iconJButton_SmallAdd());
			smallButtonAdd.setContentAreaFilled(false);
			smallButtonAdd.setBorderPainted(false);
			smallButtonAdd.setToolTipText("Dodaj wiersz");
			smallButtonSave = new JButton();
			smallButtonSave.setIcon(FrameTemplateImageIcon.iconJButton_smallSave());
			smallButtonSave.setContentAreaFilled(false);
			smallButtonSave.setBorderPainted(false);
			smallButtonSave.setToolTipText("Zapisz");
			
			smallButtonExcel = new JButton();
			smallButtonExcel.setIcon(FrameTemplateImageIcon.iconJButton_smallXLS());
			smallButtonExcel.setContentAreaFilled(false);
			smallButtonExcel.setBorderPainted(false);
			smallButtonExcel.setToolTipText("Eksport do pliku XLS");

			smallButtonCSV = new JButton();
			smallButtonCSV = new JButton();
			smallButtonCSV.setIcon(FrameTemplateImageIcon.iconJButton_SmallCSV());
			smallButtonCSV.setContentAreaFilled(false);
			smallButtonCSV.setBorderPainted(false);
			smallButtonCSV.setToolTipText("Eksport do pliku CSV");			
			
			smallButtonImport = new JButton();
			smallButtonImport = new JButton();
			smallButtonImport = new JButton();
			smallButtonImport.setIcon(FrameTemplateImageIcon.iconJButton_SmallImport());
			smallButtonImport.setContentAreaFilled(false);
			smallButtonImport.setBorderPainted(false);
			smallButtonImport.setToolTipText("Import danych");				

			smallButtonDoc= new JButton();
			smallButtonDoc = new JButton();
			smallButtonDoc = new JButton();
			smallButtonDoc.setIcon(FrameTemplateImageIcon.iconJButton_SmallDocumentation());
			smallButtonDoc.setContentAreaFilled(false);
			smallButtonDoc.setBorderPainted(false);
			smallButtonDoc.setToolTipText("Eksport dokumentacji importu");				
			
		smallButtons.add(smallButtonAdd);
		smallButtons.add(smallButtonSave);
		smallButtons.add(smallButtonExcel);
		smallButtons.add(smallButtonCSV);
		smallButtons.add(smallButtonImport);
		smallButtons.add(smallButtonDoc);
		
		
		return smallButtons;
		
	}
	
	public JButton getsmallButtonAdd()
	{
		return smallButtonAdd;
	}
	
	public void removeListenerSmallButtonAdd()
	{
		smallButtonAdd.removeActionListener(listener);
	}
	public JButton getsmallButtonSave()
	{
		return smallButtonSave;
	}
	public JButton getSmallButtonImport()
	{
		return smallButtonImport;
	}
	public JButton getSmallButtonDoc()
	{
		return smallButtonDoc;
	}
	public JButton smallSave()
	{
		smallButtonSave = new FrameTemplateButtons().smallButton("Zapisz", FrameTemplateImageIcon.iconJButton_smallSave());
		return smallButtonSave; 
	}
	
	
	public void addSimpleComboBoxToTable(JTable tables, int columnNum, String procedure, ArrayList<String> parameters ,boolean isEditable)
	{
		TableColumn modColumn = table.getColumnModel().getColumn(columnNum);
		JComboBox comboBox = getSimplComboBox(procedure, parameters, isEditable);
		modColumn.setCellEditor(new DefaultCellEditor(comboBox));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        modColumn.setCellRenderer(renderer);
        
	}
 
	
	public void addAutoComplitComboBoxToTable(JTable tables, int columnNum, String procedure, ArrayList<String> parameters ,boolean isEditable)
	{
		TableColumn modColumn = table.getColumnModel().getColumn(columnNum);
		JComboBox comboBox = getAutoComplitComboBox(procedure, parameters, isEditable);
		
		modColumn.setCellEditor(new DefaultCellEditor(comboBox));
		
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        modColumn.setCellRenderer(renderer);
        
	}
	
	public JComboBox<String> getSimplComboBox(String procedure, ArrayList<String> parameters ,boolean isEditable)  
	{
		ArrayList<ArrayList<String>> templist = new StoredProcedures().genUniversalArray(procedure, parameters);
	 
		String[] tempStr = new String[templist.size()-1];
		for (int i=1; i<templist.size();i++)
		{
			tempStr[i-1] = templist.get(i).get(0);
		}

			JComboBox<String> patternCB = new JComboBox(tempStr);
			patternCB.getEditor().setItem(tempStr);
			patternCB.setEditable(isEditable);
			return patternCB;
	
	}
	public JComboBox<String> getAutoComplitComboBox(String procedure, ArrayList<String> parameters ,boolean isEditable)  
	{
		ArrayList<ArrayList<String>> templist = new StoredProcedures().genUniversalArray(procedure, parameters);
		ArrayList<String> temp = new ArrayList<String>();
		for (int i=1;i< templist.size();i++)
		{
			temp.add(templist.get(i).get(0));
		}

		Java2sAutoComboBox patternCB = new Java2sAutoComboBox(temp);
		patternCB.setDataList(temp);
		patternCB.setMaximumRowCount(10);
		patternCB.setEditable(isEditable);
		patternCB.setStrict(false);
			return patternCB;
	
	}

	
	public JComboBox<String> addToSimplComboBox(JComboBox comboBox, String procedure, ArrayList<String> parameters)  
	{
		ArrayList<ArrayList<String>> templist = new StoredProcedures().genUniversalArray(procedure, parameters);
		comboBox.removeAllItems();
		
		 String[] tempStr = new String[templist.size()-1];
		for (int i=1; i<templist.size();i++)
		{
			comboBox.addItem(  templist.get(i).get(0));
		}
		return comboBox;
	
	}
	public JComboBox getSimplComboBox(String yamlGen, boolean isEditable)  
	{
 
		ArrayList<ArrayList<String>> templist;
		try {
			templist = new PythonBase().FromBase(false, yamlGen);
			String[] tempStr = new String[templist.size()-1];
			for (int i=1; i<templist.size();i++)
			{
				tempStr[i-1] = templist.get(i).get(0);
			}
			
			
			var patternCB = new JComboBox(tempStr);
			patternCB.setEditable(isEditable);
			return patternCB;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public JComboBox getSimplComboBox(ArrayList<String> templist ,boolean isEditable)  
	{
		 
	 
		String[] tempStr = new String[templist.size()];
		for (int i=0; i<templist.size();i++)
		{
			tempStr[i] = templist.get(i);
		}

			var patternCB = new JComboBox(tempStr);
			patternCB.setEditable(isEditable);
			return patternCB;
	
	}
	/**
	 * Ribbon podstawowych opcji manipulacji danych
	 * @return
	 */
	public JButton startConterparty()
	{
		bCon = new FrameTemplateButtons().RibbonJButton("Kontrahent", FrameTemplateImageIcon.iconJButton_Conterparty());
		return bCon;
		
	}
	
	public JPanel DefaultRibbonData()
	{
		JPanel modJP = new JPanel(new FlowLayout(FlowLayout.LEFT));
			bDup= new JButton("Duplikuj", FrameTemplateImageIcon.iconJButton_Duplicate());
			bDup.setVerticalTextPosition(SwingConstants.BOTTOM);
			bDup.setHorizontalTextPosition(SwingConstants.CENTER);
			bDup.setContentAreaFilled(false);
			bDup.setBorderPainted(false);
		modJP.add(bDup);
		return modJP;
	}
	
	public JPanel DefaultRibbonPosting()
	{
		JPanel posJP = new JPanel(new FlowLayout(FlowLayout.LEFT));
			bPos= new JButton("KsiÍgowania", FrameTemplateImageIcon.iconJButton_Postings());
			bPos.setVerticalTextPosition(SwingConstants.BOTTOM);
			bPos.setHorizontalTextPosition(SwingConstants.CENTER);
			bPos.setContentAreaFilled(false);
			bPos.setBorderPainted(false);
		posJP.add(bPos);
		posJP.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));BorderFactory.createLineBorder(Color.gray);
		return posJP;
	}
	
	public JButton getBPos()
	{
		return bPos;
	}
	
	public JPanel DefaultRibbonSelect()
	{
		JPanel modJP = new JPanel(new FlowLayout(FlowLayout.LEFT));
			bSel= new JButton("Import", FrameTemplateImageIcon.iconJButton_MultiSelect());
			bSel.setVerticalTextPosition(SwingConstants.BOTTOM);
			bSel.setHorizontalTextPosition(SwingConstants.CENTER);
			bSel.setContentAreaFilled(false);
			bSel.setBorderPainted(false);
		modJP.add(bSel);
		return modJP;
	}
	
	public JButton getbSel()
	{
		return bSel;
	}
	/**
	 * Domyúlny Ribbon eksportu plikÛw
	 * @return
	 */
	public JPanel DefaultRibbonExp()
	{
		JPanel expJP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		///expJP.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));BorderFactory.createLineBorder(Color.gray)
		//expJP.setBorder(BorderFactory.createLineBorder(Color.gray));
			bXLS= new JButton("XLS", FrameTemplateImageIcon.iconJButton_XLS());
			bXLS.setVerticalTextPosition(SwingConstants.BOTTOM);
			bXLS.setHorizontalTextPosition(SwingConstants.CENTER);
			bXLS.setContentAreaFilled(false);
			bXLS.setBorderPainted(false);
			 			
			bCSV= new JButton("CSV", FrameTemplateImageIcon.iconJButton_CSV());
			bCSV.setVerticalTextPosition(SwingConstants.BOTTOM);
			bCSV.setHorizontalTextPosition(SwingConstants.CENTER);
			bCSV.setContentAreaFilled(false);
			bCSV.setBorderPainted(false);
		expJP.add(bXLS);
		expJP.add(bCSV);
		return expJP;

	}
	/**
	 * Modyfikator dostÍpu bXLS
	 * @return bXLS
	 */
	public JButton getbXLS()
	{
		return bXLS;
	}
	/**
	 * Modyfikator dostÍpu bCSV
	 * @return bCSV
	 */
	public JButton getbCSV()
	{
		return bCSV;
	}
	/**
	 * Domyúlny Ribbon importu danych
	 * @return JPanel impJP
	 */
	public JPanel DefaultRibbonImp()
	{
		JPanel impJP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		
			bImp= new JButton("Import", FrameTemplateImageIcon.iconJButton_Import());
			bImp.setVerticalTextPosition(SwingConstants.BOTTOM);
			bImp.setHorizontalTextPosition(SwingConstants.CENTER);
			bImp.setContentAreaFilled(false);
			bImp.setBorderPainted(false);
			
			bDoc= new JButton("Dokumentacja", FrameTemplateImageIcon.iconJButton_Documentation());
			bDoc.setVerticalTextPosition(SwingConstants.BOTTOM);
			bDoc.setHorizontalTextPosition(SwingConstants.CENTER);
			bDoc.setContentAreaFilled(false);
			bDoc.setBorderPainted(false);	
		impJP.add(bImp);
		impJP.add(bDoc);
		//impJP.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));BorderFactory.createLineBorder(Color.gray)
		//impJP.setBorder(BorderFactory.createLineBorder(Color.gray));
		return impJP;
	}
	/**
	 * Modyfikator dostÍpu bImp
	 * @return bImp
	 */
	public JButton getbImp()
	{
		return bImp;
	}
	/**
	 * Modyfikator dostÍpu bDoc
	 * @return bDoc
	 */
	public JButton getbDoc()
	{
		return bDoc;
	}
	/**
	 * Domyúlny Ribbon podstawowy
	 * @return simJP
	 */
	public JPanel DefaultRibbonSim()
	{
		JPanel simJP = new JPanel(new FlowLayout(FlowLayout.LEFT));				
		
		
			bSave = new JButton("Zapisz", FrameTemplateImageIcon.iconJButton_Save());
			bSave.setVerticalTextPosition(SwingConstants.BOTTOM);
			bSave.setHorizontalTextPosition(SwingConstants.CENTER);
			bSave.setContentAreaFilled(false);
			bSave.setBorderPainted(false);
	 
							
			bAdd = new JButton("Dodaj", FrameTemplateImageIcon.iconJButton_Add());
			bAdd.setVerticalTextPosition(SwingConstants.BOTTOM);
			bAdd.setHorizontalTextPosition(SwingConstants.CENTER);
			bAdd.setContentAreaFilled(false);
			bAdd.setBorderPainted(false);


		simJP.add(bSave);
		simJP.add(bAdd);
		simJP.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
		return simJP;
	}
 /**
  * Modyfikator dostÍpu bSave
  * @return bSave
  */
	public JButton getbSave()
	{
		return bSave;
	}
	/**
	 * Modyfikator dostÍpu do bDup
	 * @return  bDup
	 */
	public JButton getbDup()
	{
		return bDup;
	}
	/**
	 * Modyfikator dostÍpu bAdd
	 * @return bAdd
	 */
	public JButton getbAdd()
	{
		return bAdd;
	}
	
	
	public void remListbSave()
	{
		bSave.removeActionListener(listener);
	}
	
	public void remListbAdd()
	{
		bAdd.removeActionListener(listener);
		
	}
	
	public void addListbAdd()
	{
		bAdd.addActionListener(listener);
	}
	
	public JButton RibbonJButton(String name, ImageIcon icon)
	{
		JButton button = new JButton(name, icon);
		button.setVerticalTextPosition(SwingConstants.BOTTOM);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		return button;	
	}
	//
	//
	//
	// Budowa MENUBAR
	//
	//
	//
	
	public JMenuBar DefaultJTableMenu(JTable table, DefaultTableModel Model,  HashMap<String, ArrayList<String>> sysall)
	{
		//ListenersClass listener = new ListenersClass(table, Model, sysall);
		JMenuBar menu = new JMenuBar();
		menu.add(JTableMenuOne());
		menu.add(JTableMenuTwo());
		//DocuUp.addActionListener(listener);
		return menu;
		
	}
	
	
	public JMenu JTableMenuOne()
	{
		
		JMenu result = new JMenu("Tabela");
			AddRowMUp = new JMenuItem("Dodaj wiersz", FrameTemplateImageIcon.iconJMenuItem_Add());
			SaveAllUp = new JMenuItem("Zapisz", FrameTemplateImageIcon.iconJMenuItem_Save());
			AddRowMUp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		result.add(AddRowMUp);
		result.add(SaveAllUp);
		
		
		return result;
		
	}
	
	public JMenu JTableMenuTwo()
	{
		JMenu result = new JMenu("Eksport/Import");
			JMenu exp = new JMenu("Eksport");
				ExcelUp = new JMenuItem("Excel", FrameTemplateImageIcon.iconJMenuItem_xls());
				ExcelUp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
				csvUp = new JMenuItem("CSV", FrameTemplateImageIcon.iconJMenuItem_CSV());
			exp.add(ExcelUp);
			exp.add(csvUp);
			JMenu imp = new JMenu("Import");
				ImpUp = new JMenuItem("Import", FrameTemplateImageIcon.iconJMenuItem_Downloadk());
				DocuUp = new JMenuItem("Dokumentacja", FrameTemplateImageIcon.iconJMenuItem_description());
			imp.add(ImpUp);
			imp.add(DocuUp);
		result.add(exp);
		result.add(imp);
		return result;
		
	}
	//
	//
	//
	//
	// budowa defaultowej tabeli 
	//
	//
	//
	// 
 
	
	
	public DefaultTableModel NewModel(int enable, String[] column, Object[][] data, HashMap<String, String> columnmap, HashMap<String, ArrayList<String>> sysall) 
	{
		 Model = new DefaultTableModel(){
			
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				if (enable==1)
				{
					return false;
				}else if (enable==2) //tylko pierwsza kolumna
				{
					if (column==0)
					{ return false;}
					else
					{ return true;}
				}else if (enable==3) //tylko pierwsza i druga kolumna
				{
					if (column==0||column==1)
					{ return false;}
					else
					{ return true;}
				}else if (enable==4) //tylko   druga ,trzecia  i czwartπ kolumna
				{
					if (column==1||column==2||column==3)
					{ return false;}
					else
					{ return true;}
				}else if (enable==5) //tylko   druga 
				{
					if (column==1)
					{ return false;}
					else
					{ return true;}
				}else if (enable==6) //model 6 przygotowany pod positions;
				{
					if (column==0||column==7||column==8||column==9)
					{
						return false;
					}
					else if (Model.getValueAt(row,8).equals(true))
					{
						return false;
						
					}else if (Model.getValueAt(row, 0).toString().length()>0)
					{
						if (column==1||column==0||column==7||column==8||column==9)
						{
							return false;
						}else
						{
							return true;
						}
						
					}
					
					
					else
					{
						return true;
					}
				}else if (enable==7) ///Posting dla MANPOS LedgetTrans
				{
					if (column==0||column==1||column==2||column==3||column==4||column==5||column==7||column==10||column==13||column==14||column==15||column==16||column==19||column==20)
					{
						return false;
					}else
					{
						return true;
					}
				}
				else if (enable==8) ///Posting dla MANPOS LedgerQTYTrans
				{
					if (column==0||column==1||column==2||column==3||column==4||column==5||column==7||column==12||column==13||column==14||column==15||column==17||column==18)
					{
						return false;
					}else
					{
						return true;
					}
				}
				else if (enable==9) ///Posting dla Firms
				{
					if (column==11)
					{
						return false;
					}else if (Model.getValueAt(row, 0).toString().length()>0&&column==0)
					{
						return false;
					}else
					{
						return true;
					}
				}else if (enable==10) //  druga i trzecia czwarta piπtea 
				{
					if (column==1||column==2||column==3||column==4)
					{ return false;}
					else
					{ return true;}
				}else if (enable==11) // pierwa, druga, trzecia
				{
					if (column==0||column==1||column==2)
					{ return false;}
					else
					{ return true;}
				}else if (enable==12) // pod orders
				{
					if (column==0||column==1||column==2||column==14||column==16||column==17)
					{ return false;}
					else
					{ return true;}
				}else if (enable==13) // tylko pierwsza edytowalna
				{
					if (column==0)
					{ return true;}
					else
					{ return false;}
				}else if (enable==14) // tylko pierwsza edytowalna
				{
					if (column==1||column==2||column==7||column==8)
					{ return false;}
					else
					{ return true;}
				}else if (enable==15) // tylko pierwsza edytowalna
				{
					if (column==0||column==1||column==2||column==3||column==4||column==5)
					{ return false;}
					else
					{ return true;}
				}else if (enable==16) // rozliczenie ledger
				{
					if (column==0||column==3||column==4)
					{ return true;}
					else
					{ return false;}
				}else if (enable==17) // 
				{	
					if (column==0||column==6||column==7)
					{ 
						return false;
					}else if (Model.getValueAt(row, 0).toString().length()>1&&column==1)
					{
						
						return false;
					}else
					{ 
						return true;
					}
				}else if (enable==18) // rozliczenie ledger
				{
					if (Model.getValueAt(row,7).equals(true))
					{
						return false;
						
					}else if (column==0||column==3||column==4||column==5||column==6||column==7)
					{ 
						return false;
						
					}else if (Model.getValueAt(row, 0).toString().length()>1&&column==1)
					{
						return false;
					} 
					else
					{ 
						return true;
					}
				}else if (enable==19) // rozliczenie ledger
				{
					if (Model.getValueAt(row,11).equals(true))
					{
						return false;
						
					}else if (column==0||column==2||column==4||column==6||column==9||column==11||column==12||column==13||column==14||column==15)
					{ 
						return false;
						
					} 
					else
					{ 
						return true;
					}
				}else if (enable==20) // rozliczenie ledger
				{
					if (Model.getValueAt(row,11).equals(true))
					{
						return false;
						
					}else if (column==1||column==2||column==3||column==9)
					{ 
						return false;
						
					} 
					else
					{ 
						return true;
					}
				}else if (enable==21) // tylko pierwsza edytowalna
				{
					if (column==0||column==6||column==7||column==8||column==9||column==10)
					{ return false;}
					else
					{ return true;}
				}else if (enable==22) //tylko   druga ,trzecia  i czwartπ kolumna
				{
					if (column==0||column==2||column==9||column==10)
					{ return false;}
					else
					{ return true;}
				}else if (enable==23) //tylko   druga ,trzecia  i czwartπ kolumna
				{
					if (column==0||column==10||column==11||column==12)
					{ return false;}
					else
					{ return true;}
				}else if (enable==24) //tylko   druga ,trzecia  i czwartπ kolumna
				{
					if (column==0||column==5)
					{ return true;}
					else
					{ return false;}
				}else if (enable==25) // tylko pierwsza i druga edytowalna
				{
					if (column==0||column==1)
					{ return true;}
					else
					{ return false;}
				}else if (enable==26) //model 6 przygotowany pod positions;
				{
					if (column==0||column==1||column==2||column==9||column==3||column==4||column==5||column==12||column==16||column==17||column==18)
					{
						return false;
					}
					else if (Model.getValueAt(row,18).equals(true))
					{
						return false;
						
					}else if (Model.getValueAt(row, 0).toString().length()>0)
					{
						if (column==0||column==1||column==2||column==9||column==3||column==4||column==5||column==12||column==16||column==17||column==18)
						{
							return false;
						}else
						{
							return true;
						}
						
					}
					
					
					else
					{
						return true;
					}
				}
				else
				{
					return true;
				}
	        }};
		for (String tstr : column)
		{
			Model.addColumn(columnmap.get(tstr));
		}
		
		int numeric = 2;
		if (data.length>0)
		{
			for (int i=0;i<=data.length-1;i++)
			{
				Object[] templist = new Object[data[i].length];
				for (int q=0;q<column.length; q++)
				{
					if (sysall.get(columnmap.get(column[q])).get(2).equals("bit"))
					{
						if (data[i][q].toString().equals("True"))
						{
							templist[q] = true;
						}else
						{
							templist[q] = false;
						}
						
					}else if(sysall.get(columnmap.get(column[q])).get(2).equals("date"))
					{
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						templist[q]= LocalDate.parse((CharSequence) data[i][q], formatter);
						 
					 
					}else if(sysall.get(columnmap.get(column[q])).get(2).equals("numeric"))
					{

						templist[q] = setDigits(data[i][q].toString());//-Double.parseDouble( data[i][q].toString() );
					}else if (sysall.get(columnmap.get(column[q])).get(2).equals("Image"))
					{
						templist[q] = data[i][q];
					}
					else
					{
					templist[q] = data[i][q].toString().trim();
					}
					
					
					
				}
				Model.addRow(templist);
			}
		}
	//else if (data.length==0)
	//	{
	//		Object[] templistEmpty = new Object[column.length];
	//		for (int i =0; i< column.length;i++)
	//		{
	//			if (sysall.get(columnmap.get(column[i])).get(2).equals("bit"))
	//			{
	//				templistEmpty[i] = false;	
	//			}
	//			else if(sysall.get(columnmap.get(column[i])).get(2).equals("date"))
	//			{
	//				templistEmpty[i] = "1900-01-01";	
	//			}else if(sysall.get(columnmap.get(column[i])).get(2).equals("numeric"))
	//			{
	//				templistEmpty[i] = 0;	
	//			}else
	//			{
	//				templistEmpty[i] = "";
	//			}
	//		}
	//		Model.addRow(templistEmpty);
	//	}
		return Model;
	}
	
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	
	public HashMap<String, HashMap<String, String>> getAccountMap()
	{
		ArrayList<ArrayList<String>> planAccountCodes = new StoredProcedures().genUniversalArray("getPlanAccountCodes", new ArrayList<String>());
		HashMap<String, HashMap<String, String>> accountMap= new HashMap<String, HashMap<String, String>>();
		for (int i=1;i<planAccountCodes.size();i++)
		{
			if (!accountMap.containsKey(planAccountCodes.get(i).get(0)))
			{
				accountMap.put(planAccountCodes.get(i).get(0), new HashMap<String, String>());
			}
			accountMap.get(planAccountCodes.get(i).get(0)).put(planAccountCodes.get(i).get(1), planAccountCodes.get(i).get(2));
		}
		
		return accountMap;
	}
	
	
	public HashMap<String, Integer> getColumnNumbers(JTable table, HashMap<String, ArrayList<String>> sysAll)
	{
		HashMap<String, Integer> columnNumbers = new HashMap<String, Integer>();
		for (int i=0; i<table.getColumnCount();i++)
		{
			columnNumbers.put(sysAll.get(table.getColumnName(i)).get(0), i);
		}
		
		return columnNumbers;
		 
	}
	
	public HashMap<String, Integer> getColumnNumbers(DefaultTableModel model, HashMap<String, ArrayList<String>> sysAll)
	{
		HashMap<String, Integer> columnNumbers = new HashMap<String, Integer>();
		for (int i=0; i<model.getColumnCount();i++)
		{
			columnNumbers.put(sysAll.get(model.getColumnName(i)).get(0), i);
		}
		
		return columnNumbers;
		 
	}
	public HashMap<String, Integer> getColumnNumbers(ArrayList<String> columnArray)
	{
		HashMap<String, Integer> columnNumbers = new HashMap<String, Integer>();
		for (int i=0; i<columnArray.size();i++)
		{
			columnNumbers.put(columnArray.get(i), i);
		}
		
		return columnNumbers;
		 
	}
	
	public HashMap<String, Integer> getColumnNumbers(Object[] columnArray)
	{
		HashMap<String, Integer> columnNumbers = new HashMap<String, Integer>();
		for (int i=0; i<columnArray.length;i++)
		{
			columnNumbers.put((String) columnArray[i], i);
		}
		
		return columnNumbers;
		 
	}
	
	public void addDictToModel(HashMap<Integer, String> dictColumnAndYaml, DefaultTableModel Model)
	{
		if (listener!=null)
		{
			HashMap<Integer,ArrayList<ArrayList<String>>> HashMapDict = genHashMapDict(dictColumnAndYaml);
			listener.setHashMapDict(HashMapDict);
			listener.setIsModListWork(false);
			setModelDict(HashMapDict, Model);
			listener.setIsModListWork(true);
			
			
		}else
		{
			System.out.println("Najpierw ustaw listener!");
		}
	}
	
    public void setDeleteEnable(boolean deleteEnable)
    {
		if (listener!=null)
		{
			listener.setDeleteEnable(deleteEnable);
		}
    }
	
	public void setModelDict(HashMap<Integer,ArrayList<ArrayList<String>>> HashMapDict, 
							DefaultTableModel Model)
	{
		for (int i=0; i<Model.getRowCount();i++)
		{
			for (int j=0; j<Model.getColumnCount();j++)
			{
				if(HashMapDict.containsKey(j))
				{
					for (int k=0; k<HashMapDict.get(j).size();k++) 
					{
						if (HashMapDict.get(j).get(k).get(0).equals(Model.getValueAt(i, j)))
						{
							Model.setValueAt(HashMapDict.get(j).get(k).get(1), i, j);
							break;
						}
					}
				}
			}
		}
	}
	
	public HashMap<Integer,ArrayList<ArrayList<String>>> genHashMapDict(HashMap<Integer, String> dictList)
	{
		HashMap<Integer,ArrayList<ArrayList<String>>> dictFullMap = new HashMap<Integer,ArrayList<ArrayList<String>>>();
	//	for (int i=0; i<dictList.size();i++)
		for (Integer i: dictList.keySet())
		{ 
			dictFullMap.put(i, genFromBaseDictList(dictList.get(i)));
		}	
		return dictFullMap;
	}
	public ArrayList<ArrayList<String>> getOryginalDictList()
	{
		return oryginalDictList;
	}
	
	public ArrayList<ArrayList<String>> getListFromBase()
	{
		return ListFromBase;
	}
	
	public ArrayList<ArrayList<String>> genFromBaseDictList(String yaml)
	{
		try {
			PythonBase readers = new PythonBase();
			ArrayList<ArrayList<String>> templist = readers.FromBase(false, yaml);
			oryginalDictList = templist;			
			return templist;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void getDict (HashMap<String, ArrayList<String>> mapsysall, JTable table, String yaml) 
	{
		
		try {
			PythonBase readers = new PythonBase();
			ArrayList<ArrayList<String>> templist;
			templist = readers.FromBase(false, yaml);
			txt txtlist = new txt();
			Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
			CommboBoxDefault(dictdata, mapsysall, table);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void getCurrencyDict (JTable table, int col) 
	{
		addSimpleComboBoxToTable(table, col, "getCurrencyCode",new ArrayList<String>() ,false);
	}
	public void getFirmDict (JTable table, int col) 
	{
		addSimpleComboBoxToTable(table, col, "getCompanyShort",new ArrayList<String>() ,false);
	}
	public void getOrdersUnitDict (JTable table, int col) 
	{
		addSimpleComboBoxToTable(table, col, "getDomainValue", new ArrayList<String>(Arrays.asList("Orders.Unit")),false);
	}	
	public static String setDigits(String editValue)
	{
		int numeric=2;
		if (String.valueOf(Double.parseDouble( editValue.toString())).length()-String.valueOf(Double.parseDouble( editValue.toString())).lastIndexOf(".")-1>2)
		{
			numeric=String.valueOf(Double.parseDouble(editValue.toString())).length()-String.valueOf(Double.parseDouble( editValue.toString())).lastIndexOf(".");
		}else 
		{
			numeric =2;
		}
		return  String.format(" %."+ numeric +"f",  Double.parseDouble(editValue.toString() ));
	}
	
	
	public JTable getDefaultTable(DefaultTableModel Models, HashMap<String, ArrayList<String>> sysall)
	{//int enable,
		 this.Model = Models;
		 table = new JTable(Model){
			
			private static final long serialVersionUID = 1L;
	        @SuppressWarnings({ "unchecked", "rawtypes" })
			public  Class getColumnClass(int header) {
	         if (sysall.get(Model.getColumnName(header)).get(2).equals("bit"))
	         {
	        	 return Boolean.class;
	         }
	         else if (sysall.get(Model.getColumnName(header)).get(2).equals("Image"))
	         {
	        	 return getValueAt(0, header).getClass();
	         } else if (sysall.get(Model.getColumnName(header)).get(2).equals("numeric"))
	         {
	        	 
	        	 return BigDecimal.class;
	         }else if (sysall.get(Model.getColumnName(header)).get(2).equals("smallint"))
	         {
	        	 
	        	 return Short.class;
	         }    
	         else
	         {
	        	 return String.class;
	         }
	        }
	    };
	    
	    table.setDefaultEditor(BigDecimal.class, new FrameTemplateEditCell());
	    table.setFillsViewportHeight(true);

	    table.setCellSelectionEnabled(true);
	    table.setRowSelectionAllowed(true);
	  //  table.setAutoCreateRowSorter(true);
	    table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	    addContextMenu(table);
		//addFrameTempListener(table, Model, sysall);
	    
		return table;
	}
	/**
	 * Wyszarzenie pola pod s≥ownik
	 * @param column - numerz wyszarzanej kolumny
	 */
	public void setMenuRun(int column)
	{
		setJtableColor(column);
	}	
	
	/**
	 * Wielokrotne wyrzarzenie pola pod s≥ownik
	 * @param list
	 */
	public void setMultipleMenuRun (ArrayList<Integer> list)
	{
		for (int i: list)
		{
			setMenuRun(i);
		}
	}
	
	public void setJtableColor(int column)
	{
		TableColumn col = table.getColumnModel().getColumn(column);
		col.setCellRenderer(new MyRenderer(Color.lightGray, Color.black));
	}
	
	
	public void setModel(DefaultTableModel Models)
	{
		this.Model = Models;
		listener.setModel(Models);
	 	addListenerJTable(table, Models);
		table.setModel(Model);
		
		
	}
	public void setModelC( )
	{
		 
		table.repaint();
		 
	}
	public JTable getTable()
	{
		return table;
	}
	
	public void getSelectionRunWithParameters(JTable table,String procedure, ArrayList<String> parameters, String yamlDictSys, String title, int column)
	{
		String strValue=null;
		try
		{
			strValue = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString()=="" ? null : table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString(); 
		}catch (java.lang.NullPointerException a)
		{
			strValue = null;
		}
		
		ISelectionRun jdialog = new ISelectionRun(strValue , yamlDictSys, procedure, parameters, title, column);
		ListFromBase = jdialog.getTableList();
		table.setValueAt(jdialog.getSelection(), table.getSelectedRow(), table.getSelectedColumn());	
	}
	/**
	 * Popularny s≥ownik - lista projektÛw
	 * @param table
	 * @param firm
	 */
	public void dictInstrumentsbyFirm(JTable table,  String firm)
	{
		 getSelectionRunWithParameters(table, "getInstrumentsByFirmDict" ,new ArrayList<String>(Arrays.asList(firm)), "Dict_InstrumentsByFirm.yml", "Projekty", 1);
	}
	/**
	 * Popularny s≥ownik - lista pkontrahentÛw
	 * @param table
	 * @param firm
	 */
	public void dictCounterpartyNameFirm(JTable table)
	{
		getSelectionRunWithParameters(table, "getConterparty", new ArrayList<String>(), "Dict_Conterparty.yml", "Kontrahenci", 2);
	}
	/**
	 * Popularny s≥ownik - opisy towarÛw
	 * @param table
	 */
	public void dictDescriptionALL(JTable table)
	{
		table.getCellEditor().stopCellEditing();
		getSelectionRunWithParameters(table, "getOrdersDescription" ,new ArrayList<String>(), "Dict_OrderDescription.yml", "Opisy", 1);
	}
	/**
	 * Popularny s≥ownik - opisy kontrahentÛw
	 * @param table
	 */
	public void dictCounterparty(JTable table)
	{
		table.getCellEditor().stopCellEditing();
		ISelectionRun jdialog = new ISelectionRun("ISelectConterparty_sys.yml", "ISelectConterparty.yml", "Kontahent", 2);
		table.setValueAt(jdialog.getSelection(), table.getSelectedRow(), table.getSelectedColumn());
	}
 
	public void AutSorterJTable(JTable table, int numberOfColumn, boolean twoTimes)
	{	    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(Model);
		   table.setRowSorter(sorter);
		   sorter.setSortsOnUpdates(true);
		table.getRowSorter().toggleSortOrder(numberOfColumn);
		if (twoTimes==true)
		{
			table.getRowSorter().toggleSortOrder(numberOfColumn);
		}
	}
	
/**
 * Definicja ContextMenu
 * @param table tabela do ktÛrej ma byÊ podpiÍty ContextMenu
 */
	public void addContextMenu(JTable table)
	{
    	popup = new JPopupMenu();
			filtr = new JMenuItem("Filtrowanie", FrameTemplateImageIcon.iconJMenuItem_Find());
			UsuFiltM = new JMenuItem("UsuÒ filtr");
			JMenu copyM = new JMenu("Kopiuj");
				cocel = new JMenuItem("Zaznaczone", FrameTemplateImageIcon.iconJMenuItem_CopySimp());
				coall = new JMenuItem("Wszystko", FrameTemplateImageIcon.iconJMenuItem_CopyAll());
				corow = new JMenuItem("Wiersz", FrameTemplateImageIcon.iconJMenuItem_CopyRow());
			copyM.add(cocel);
			copyM.add(corow);
			copyM.add(coall);
			pasteM =new JMenuItem("Wklej", FrameTemplateImageIcon.iconJMenuItem_Paste());
			addRowM = new JMenuItem("Dodaj Wiersz", FrameTemplateImageIcon.iconJMenuItem_Add());
			deleteM = new JMenuItem("UsuÒ wiersz", FrameTemplateImageIcon.iconJMenuItem_Delete());
			JMenu expoM = new JMenu("Eksport");
				excelM = new JMenuItem("Excel", FrameTemplateImageIcon.iconJMenuItem_xls());
				csvM= new JMenuItem("CSV", FrameTemplateImageIcon.iconJMenuItem_CSV());
			expoM.add(excelM);
			expoM.add(csvM);
		popup.add(filtr);
		popup.add(UsuFiltM);
		popup.addSeparator();
		popup.add(addRowM);
		popup.add(deleteM);
		popup.add(copyM);	
		popup.add(pasteM);
		popup.addSeparator();
		popup.add(expoM);
		table.setComponentPopupMenu(popup);
	    addRowM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
	    UsuFiltM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
	    deleteM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
	    cocel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
	    corow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
	    coall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
	    pasteM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
	    excelM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
	
	}
	
	public void sourceContextMenu()
	{
		JMenu sourceCM = new JMenu("èrÛd≥o");
			addSour = new JMenuItem("Dodaj", FrameTemplateImageIcon.iconJMenuItem_addSource());
			openSour = new JMenuItem("OtwÛrz", FrameTemplateImageIcon.iconJMenuItem_openSource());
			delSour = new JMenuItem("UsuÒ", FrameTemplateImageIcon.iconJMenuItem_deleteSource());
			showSour= new JMenuItem("Poka", FrameTemplateImageIcon.iconJMenuItem_ShowSource());
		sourceCM.add(addSour);
		sourceCM.add(openSour);
		sourceCM.add(delSour);
		sourceCM.add(showSour);
		popup.addSeparator();
		popup.add(sourceCM);
		//return sourceCM;
		//DataImport imp = new DataImport();
		//String fileName = imp.setFileName(workPath);
	}
	
	public void setSourceWorkPath(String newWorkPath)
	{
		listener.setSourceWorkPath(newWorkPath);
	}
	
	
	public JPopupMenu getPopup()
	{
		return popup;
	}
	public void isEditContextMenu(boolean isEdit)
	{
		deleteM.setEnabled(isEdit);
		addRowM.setEnabled(isEdit);
	}
	
	public void isEditRibbon(boolean isEdit)
	{
		bSave.setEnabled(isEdit);
		bAdd.setEnabled(isEdit);
		bDoc.setEnabled(isEdit);
		bImp.setEnabled(isEdit);
	}
	/**
	 * Modyfikator dostÍpu do JMenuItem ContextMenu
	 * @return AddRowUp
	 */
	public JMenuItem getaddRowM()
	{
		return addRowM;		
	}
	/**
	 * Modyfikator dostÍpu do JMenuItem ContextMenu
	 * @return deleteM
	 */
	public JMenuItem getdeleteM()
	{
		return deleteM;
	}
	/**
	 * Modyfikator dostÍpu do JMenuItem ContextMenu
	 * @return pasteM
	 */
	public JMenuItem getpasteM()
	{
		return pasteM;
		
	}
	/**
	 * Modyfikator dostÍpu do JMenuItem ContextMenu
	 * @return filtr
	 */
	public JMenuItem getFiltr()
	{
		return filtr;
	}
	/**
	 * Modyfikator dostÍpu do JMenuItem ContextMenu
	 * @return usunFiltrM
	 */
	public JMenuItem getUsuFiltM()
	{
		return UsuFiltM;
	}
	
	public void remListmAdd()
	{
		addRowM.removeActionListener(listener);
	}
	
	public void remListmDelete()
	{
		deleteM.removeActionListener(listener);
	}
	private HashMap<String, Integer> columnTable(JTable table, HashMap<String, ArrayList<String>> sysAll)
	{
		HashMap<String, Integer> column = new HashMap<String, Integer>();
		for (int i=0; i<table.getRowCount();i++)
		{
			column.put(sysAll.get(table.getColumnName(i)).get(0), i);
		}
		return column;
	}
	//
	//
	//
	//
	//Listenery
	
	public void addFrameTempListener(JTable table, DefaultTableModel Model, 
			HashMap<String, ArrayList<String>> sysall,
			HashMap<String, String> mapsysBase, 
			HashMap<String, String> mapsys,
			ArrayList<Integer> TableKey)
	{

		listener = new FrameTemplateListener(table, Model, sysall, mapsysBase, mapsys, TableKey);
		

	}
	public void removeFrameTempListener()
	{
		listener=null;
	}
    public void setkeyCol(int keyCol )
    {
    	listener.setkeyCol(keyCol);
 
    }
	
	public void setGgenKey(int keyCol, int genKeyType, int genKeyModel)
	{
		listener.setGgenKey(keyCol, genKeyType, genKeyModel);
	}
	public void setGgenKey(int keyCol, int genKeyType, int genKeyModel, String addKeyDate, String addKeyFirm)
	{
		listener.setGgenKey(keyCol, genKeyType, genKeyModel);
	}
	
	/**
	 * Listener podstawowych komponentÛw wstπøki
	 */
	public void addListenerRibbon()
	{  
		if (bAdd!=null&& bAdd.getActionListeners().length==0)
		{
			//removeActionListenersRibbonFull();
		
		JButton Button = new FrameTemplateButtons().RibbonJButton("DUPA", null);
		if (bAdd==null) 
		{this.bAdd= Button;}
		if (bSave==null) 
		{this.bSave= Button;}
		if (bXLS==null) 
		{this.bXLS= Button;}
		if (bCSV==null) 
		{this.bCSV= Button;}
		if (bImp==null) 
		{this.bImp= Button;}
		if (bDoc==null) 
		{this.bDoc= Button;}
		if (bDup==null)
		{this.bDup=Button ;}
		if(this.bCon==null) { this.bCon=Button ; }

		listener.getRibbon(bAdd, bSave, bXLS, bCSV, bImp, bDoc, bDup, bCon);
		bAdd.addActionListener(listener);
		bSave.addActionListener(listener);
		bXLS.addActionListener(listener);
		bCSV.addActionListener(listener);
		bImp.addActionListener(listener);
		bDoc.addActionListener(listener);
		bDup.addActionListener(listener);
		bCon.addActionListener(listener);
		}
	}

	public void removeActionListenersRibbon()
	{
		bSave.removeActionListener(listener);
	}

	public void removeActionListenersRibbonFull()
	{
		bAdd.removeActionListener(listener);
		bSave.removeActionListener(listener);
		bXLS.removeActionListener(listener);
		bCSV.removeActionListener(listener);
		bImp.removeActionListener(listener);
		bDoc.removeActionListener(listener);
		bDup.removeActionListener(listener);
		bCon.removeActionListener(listener);
	}	
 
	
	
	/**
	 * Listener JTable rozszerczony. Dodaje KeyListener, MouseListener, TableModelListener
	 * @param table - tabela dla ktÛrej uruchamiany jest listener
	 * @param Model - Model tej tabeli
	 */
	public void addListenerJTable(JTable table, DefaultTableModel Model)
	{
		listener.getJTabelProp(Model, table);
		//Do naprawy
		// table.addKeyListener(listener);
		table.addMouseListener(listener);
		Object[][] data = ModelToObject(Model);
		listener.startModList(data);
		Model.addTableModelListener(listener);		
	}
	
	public void addListenerModel(DefaultTableModel model)
	{
		model.addTableModelListener(listener);	
	}
	
	
	
	public void removeKeyListener(JTable table)
	{
		table.removeKeyListener(listener);
	}
	
	
 
	public void addListenerJTable(JTable table)
	{
		table.getSelectionModel().addListSelectionListener(listener);
	}
	
	/**
	 * Dodawanie nas≥uchiwania podsrawowych elementÛw menu kontekstowego
	 */
	public void addListenerContextMenu()
	{
		 
		if (filtr.getActionListeners().length==0)
		{
			 
		
		listener.getContextMenu(addRowM,deleteM,UsuFiltM,filtr,cocel,coall, corow,excelM,pasteM,csvM, addSour, openSour, delSour, showSour);
	    filtr.addActionListener(listener);
	    UsuFiltM.addActionListener(listener);
	    addRowM.addActionListener(listener);
	    deleteM.addActionListener(listener);
	    cocel.addActionListener(listener);
	    corow.addActionListener(listener);
	    coall.addActionListener(listener);
	    pasteM.addActionListener(listener);
	    excelM.addActionListener(listener);
	    csvM.addActionListener(listener);
		    if (addSour!=null)
		    {
		    addSour.addActionListener(listener);
		    openSour.addActionListener(listener);
		    delSour.addActionListener(listener);
		    showSour.addActionListener(listener);
		    }
		}
	}
	
	public void removeListenersAddSour()
	{
		addSour.removeActionListener(listener);
	}
	public void removeListenerDelSour()
	{
		delSour.removeActionListener(listener);
	}
	
	public void removeContextMenuListeners()
	{
	    filtr.removeActionListener(listener);
	    UsuFiltM.removeActionListener(listener);
	    addRowM.removeActionListener(listener);
	    deleteM.removeActionListener(listener);
	    cocel.removeActionListener(listener);
	    corow.removeActionListener(listener);
	    coall.removeActionListener(listener);
	    pasteM.removeActionListener(listener);
	    excelM.removeActionListener(listener);
	    csvM.removeActionListener(listener);
	    if (addSour!=null)
	    {
	    addSour.removeActionListener(listener);
	    openSour.removeActionListener(listener);
	    delSour.removeActionListener(listener);
	    showSour.removeActionListener(listener);
	    }
	}
	
	public JMenuItem getDelSour()
	{
		return delSour;
	}
	public JMenuItem getAddSour()
	{
		return addSour;
	}
	public JMenuItem getOpenSour()
	{
		return  openSour;
	}
	public JMenuItem getShowSour()
	{
		return showSour;
	}
	public void addListenerSmallButtons()
	{
		if (smallButtonAdd!=null)
		{
		listener.getSmallButtons(smallButtonAdd,smallButtonSave, smallButtonExcel,smallButtonCSV,smallButtonImport,smallButtonDoc);
		smallButtonAdd.addActionListener(listener);
		smallButtonSave.addActionListener(listener);
		smallButtonExcel.addActionListener(listener);
		smallButtonCSV.addActionListener(listener);
		smallButtonImport.addActionListener(listener);
		smallButtonDoc.addActionListener(listener);
		}
	}
	public void addListenerUpMenu()
	{
		listener.getUpMenu(SaveAllUp,ExcelUp,ImpUp,DocuUp,AddRowMUp, csvUp);
		SaveAllUp.addActionListener(listener);
		ExcelUp.addActionListener(listener);
		ImpUp.addActionListener(listener);
		DocuUp.addActionListener(listener);
		AddRowMUp.addActionListener(listener);
		csvUp.addActionListener(listener);
	}
	
	public void addFirm(int column, JButton firmCode)
	{
		listener.setFirmCode(column, firmCode);
	}
	
	public void setFilter(String str, int column)
	{
		listener.StringFiltrModel(str, column);
	}
	public void setFilter(ArrayList<ArrayList<String>> list, int column)
	{
		listener.StringFiltrModel(list, column);
	}
	
	public void setRemoveFiltr()
	{
		listener.removeFiltr();
	}
 
	public void removeAllInModel()
	{
		listener.deleteAllInModel( );
	}
	public void removeRowInModel(DefaultTableModel Model, JTable table, int selectedRow)
	{
		listener.removeRow(Model,table, selectedRow);
	}	
	
	private Object[][] ModelToObject(DefaultTableModel Model)
	{
		Object[][] result = new Object[Model.getRowCount()][Model.getColumnCount()];
		for (int i=0; i< Model.getRowCount(); i++)
		{
			Object[] TempResult = new Object[Model.getColumnCount()];
			for (int j=0; j< Model.getColumnCount(); j++)
			{
				TempResult[j] = Model.getValueAt(i, j);
			}
			result[i] = TempResult;
		}
		return result;
	}
	
	public Object[][]  arrayListStringToObject(ArrayList<ArrayList<String>> list, boolean isHead, boolean addChecker)
	{
		int size_ = 0;
		int j = 0;
		int q = 0;
		if (isHead==true)
		{
			size_ = list.size()-1;
			j=1;
		}else
		{
			size_=list.size();
			j = 0;
		}
		if (addChecker==true)
		{
			q = 1;
		}
		
		Object[][] result = new Object[size_][list.get(0).size()+q];
		
		for (int i=j;i<list.size();i++)
		{
			if (addChecker==true)
			{
				result[i][0] = true;
			}
			for (int k=0;k<list.get(0).size();k++)
			{
				result[i][k+q] = list.get(i).get(k);
			}
		}
		
		return result;
		
	}
	
	public String addDaysToString(String inDate,int addDays) throws ParseException
	{
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = formatter.parse(inDate);
		java.util.Calendar c = java.util.Calendar.getInstance(); 
		c.setTime(date); 
		c.add(java.util.Calendar.DATE, addDays);
		date = c.getTime();
		String sdf = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return sdf;
	}
	
	public ArrayList<ArrayList<String>> FullJTableToStringList(boolean All, JTable table, HashMap<String, ArrayList<String>> sysall)
	{ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
	if (All==true)
	{
		ArrayList<String> TempCol = new ArrayList<String>();
		for (int i=0; i< table.getColumnCount(); i++)
		{
			
			TempCol.add(table.getColumnName(i));
		}
		result.add(TempCol);
	}
	for (int i=0; i<table.getRowCount();i++)
	{
		ArrayList<String> TempRow = new ArrayList<String>();
		for (int j=0; j<table.getColumnCount(); j++)
		{
			if (sysall.get(table.getColumnName(j)).get(2).equals("bit"))
			{
				if (table.getValueAt(i, j).equals(true))
				{
					TempRow.add("1");
				}else if (table.getValueAt(i, j).equals(false))
				{
					TempRow.add("0");
				}
			}else 
			{
			TempRow.add(String.valueOf(table.getValueAt(i, j)));
			}
		}
		result.add(TempRow);
	}
 
	return result;}
	
	public ArrayList<ArrayList<String>> FullModelToStringList(boolean All, DefaultTableModel Model, HashMap<String, ArrayList<String>> sysall)
	{
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		if (All==true)
		{
			ArrayList<String> TempCol = new ArrayList<String>();
			for (int i=0; i< Model.getColumnCount(); i++)
			{
				
				TempCol.add(Model.getColumnName(i));
			}
			result.add(TempCol);
		}
		for (int i=0; i<Model.getRowCount();i++)
		{
			ArrayList<String> TempRow = new ArrayList<String>();
			for (int j=0; j<Model.getColumnCount(); j++)
			{
				if (sysall.get(Model.getColumnName(j)).get(2).equals("bit"))
				{
					if (Model.getValueAt(i, j).equals(true))
					{
						TempRow.add("1");
					}else if (Model.getValueAt(i, j).equals(false))
					{
						TempRow.add("0");
					}
				}else 
				{
				TempRow.add(String.valueOf(Model.getValueAt(i, j)));
				}
			}
			result.add(TempRow);
		}
	 
		return result;
	}
	
	

	public void getDoClickAddRow()
	{
		UsuFiltM.doClick();
	}
/////
/////	
/////	
/////keylistener
/////
////
////
	
	class ListenersClass implements KeyListener, MouseListener, ActionListener, TableModelListener 
	{
		private JTable table;
		private DefaultTableModel Model;
		private HashMap<String, ArrayList<String>> sysall;
		//private JPopupMenu popup;
		//private JMenuItem filtr, UsuFiltM;
		private TableRowSorter<TableModel> sorter;
		private int column, row;
		 
        private static final String LINE_BREAK = "\n"; 
        private static final String CELL_BREAK = "\t"; 
        private final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
        private HashMap< Integer , ArrayList<String>> ModList;
        private ArrayList<String> DeList;
		public ListenersClass(JTable table, DefaultTableModel Model, HashMap<String, ArrayList<String>> sysall)
		{
			this.table = table;
			this.Model = Model;
			this.sysall = sysall;
			 
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void keyPressed(KeyEvent eve) {
			// TODO Auto-generated method stub
			if ((eve.getKeyCode() == KeyEvent.VK_N) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
			{
				addRowM.doClick();
            }else if ((eve.getKeyCode() == KeyEvent.VK_F) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
            {
            	removeFiltr();
            }else if ((eve.getKeyCode() == KeyEvent.VK_D) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
            {
            	deleteM.doClick();
            }else if ((eve.getKeyCode() == KeyEvent.VK_C) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
            {
            	copyToClipboard(false);
            }else if ((eve.getKeyCode() == KeyEvent.VK_R) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
            {
            	table.setColumnSelectionInterval(0, table.getColumnCount()-1);
            	copyToClipboard(false);
            }else if ((eve.getKeyCode() == KeyEvent.VK_A) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
            {
            	table.selectAll();
            	copyToClipboard(false);
            }else if ((eve.getKeyCode() == KeyEvent.VK_V) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
            {
            	try {
					pasteFromClipboard();
				} catch (UnsupportedFlavorException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }else if ((eve.getKeyCode() == KeyEvent.VK_E) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
            {
            	ExcelFile newexcel = new ExcelFile();
            	newexcel.JTableToExcel(table);
            }else if ((eve.getKeyCode() == KeyEvent.VK_S) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
            {
            }
		} //SaveMode
		@Override
		public void keyReleased(KeyEvent eve) {
			int c = eve.getKeyCode();
			if (KeyEvent.VK_DOWN==c)
			{
				if (table.getSelectedRow()==table.getRowCount()-1)
				{
					addEmtpyRow(table.getSelectedRow() + 1, Model, sysall, table);
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
           // if (e.isPopupTrigger()) {
			if (SwingUtilities.isRightMouseButton(e)) 
				{
				if (e.getSource()==table)
					{
						row = table.rowAtPoint(e.getPoint());
						column = table.columnAtPoint(e.getPoint());
						if (row>=0 && column>=0)
						{
							filtr.setForeground(Color.BLACK);
							
							
						}
						else
						{
							filtr.setForeground(Color.GRAY);
						}
					}
            	}
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

		
			Object eve = e.getSource();
			//table.addRowSelectionInterval(0, 0);
			//table.removeRowSelectionInterval(0, 0);
			if (eve==filtr && filtr.getForeground()==Color.BLACK)
			{
			//	table.getSelectionModel().clearSelection();
			//	table.editCellAt(0, 0);
				//table.editingCanceled(null);
				cancelEditing();
				if (sysall.get(table.getColumnName(column)).get(2).equals("nvarchar") ||
						sysall.get(table.getColumnName(column)).get(2).equals("char") ||
						sysall.get(table.getColumnName(column)).get(2).equals("nchar") ||
						sysall.get(table.getColumnName(column)).get(2).equals("bit"))
				{
					
					String strfiltr =JOptionPane.showInputDialog(null,"Wprowadü szukanπ frazÍ", table.getValueAt(row, column)); 
					if (strfiltr!=null)
					{
						RowFilter<Object, Object> filterrow = new RowFilter<Object, Object>()
						{
							public boolean include (Entry entry)
							{
								return entry.getStringValue(column).contains(strfiltr);
							}
						};
						sorter = new TableRowSorter<TableModel>(Model);
						sorter.setRowFilter(filterrow);
						table.setRowSorter(sorter);
					} else
					{
						removeFiltr();
					}
				}	
			} else if (eve==UsuFiltM)
			{	
					removeFiltr();
			} else if (eve==addRowM)
			{
				int rownum;
				if (table.getSelectedRow()<0)
				{
					rownum=0;
				}else
				{
					rownum=table.getSelectedRow();
				}
				
				addEmtpyRow(rownum, Model, sysall, table);
			}else if (eve==deleteM)
			{
				if (table.getSelectedRow()>=0)
				{
					Model.removeRow(table.getSelectedRow());
				}
			}else if (eve==cocel)
			{
				copyToClipboard(false);
			}
			else if (eve==corow)
			{
				table.setColumnSelectionInterval(0, table.getColumnCount()-1);
				copyToClipboard(false);
			}
			else if (eve==coall)
			{
				table.selectAll();
				copyToClipboard(false);
			}else if (eve==pasteM)
			{
				 try {
					pasteFromClipboard();
				} catch (UnsupportedFlavorException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else if (eve==excelM || eve==ExcelUp)
			{
            	ExcelFile newexcel = new ExcelFile();
            	newexcel.JTableToExcel(table);
			}
			else if (eve==SaveAllUp)
			{
			cancelEditing();
			}

			
		}
		
		public void addEmtpyRow(int rownum, DefaultTableModel Model, HashMap<String, ArrayList<String>> sysall, JTable table)
		{
			//getDoClickAddRow();
			Object[] emptyrow = new Object[Model.getColumnCount()];
			for  (int i=0; i<Model.getColumnCount(); i++)
			{
				//System.out.print(i + ":" + table.getColumnName(i));
				
				if (sysall.get(Model.getColumnName(i)).get(2).equals("bit"))
				{
					emptyrow[i]=false;
				}
				else
				{
					emptyrow[i]="";
				}
			}
			Model.insertRow(rownum, emptyrow);
			table.setRowSelectionInterval(rownum, rownum);
		}		
		
		
		public void removeFiltr()
		{
			if (sorter!=null)
			{
			sorter.setRowFilter(null);
			}
		}
		private void cancelEditing() { 
            if (table.getCellEditor() != null) { 
                    table.getCellEditor().cancelCellEditing(); 
        } 
    } 
	public void copyToClipboard(boolean isCut)
	{
		int numRows = table.getSelectedRowCount(); //liczone od 0 zaznaczony wiersz
		int numCols = table.getSelectedColumnCount(); ///liczona o 0 zaznaczona kolumna
		int[] rowsSelected = table.getSelectedRows();  /// liczone o 0 zaznaczone wiersze
		int[] colsSelected = table.getSelectedColumns(); // liczone od 0 zaznaczone kolumny 
		
		StringBuffer excelStr=new StringBuffer(); 
		for (int i=0; i<numRows; i++) { 
            for (int j=0; j<numCols; j++) { 
                    excelStr.append(escape(table.getValueAt(rowsSelected[i], colsSelected[j]))); 
                    if (isCut) { 
                            table.setValueAt(null, rowsSelected[i], colsSelected[j]); 
                    } 
                    if (j<numCols-1) { 
                            excelStr.append(CELL_BREAK); 
                    } 
            } 
            excelStr.append(LINE_BREAK); 
    } 
    StringSelection sel  = new StringSelection(excelStr.toString()); 
    CLIPBOARD.setContents(sel, sel); 	
	}
	public void pasteFromClipboard() throws UnsupportedFlavorException, IOException
	{
		int numRowsPas = table.getSelectedRow(); //liczone od 0 zaznaczony wiersz
		int numColsPas = table.getSelectedColumn(); ///liczona o 0 zaznaczona kolumna
		
		if (numRowsPas>=0 &&  numColsPas>=0)
		{
			String pasteString = (String)(CLIPBOARD.getContents(this).getTransferData(DataFlavor.stringFlavor)); 
			table.setValueAt(pasteString, numRowsPas,  numColsPas);
		}
	}
    private String escape(Object cell) 
    { 
        return cell.toString().replace(LINE_BREAK, " ").replace(CELL_BREAK, " "); 
    }
  ///
  ////
  ///
  ////
  ////
  ////
  	public void tableChanged(TableModelEvent e) {
  		// TODO Auto-generated method stub		
  		//getMods(e.getType(),e.getFirstRow());	
  		DefaultTableModel Model = (DefaultTableModel) e.getSource();
  		this.ModList = modModList(e.getType(),e.getFirstRow(), Model);
  	}
  	
  	public HashMap< Integer , ArrayList<String>> startModList(Object[][] data)
  	{
  		/// key-numer wiersza, typ, numer wiersza oryginalnego, klucz
  		this.ModList = new HashMap< Integer , ArrayList<String>>();
  		this.DeList = new ArrayList<String>();
  		for (int i=0; i<data.length;i++)
  		{
  			ArrayList<String> templist = new ArrayList<String>();
  			templist.add("2");
  			templist.add(String.valueOf(i));
  			templist.add(keyTable(data[i]));
  			ModList.put(i, templist);
  		}
  	return ModList;
  	}	
  	public String keyTable(Object[] tempobj)
  	{   StringBuilder result = new StringBuilder();
  		for (int i=0;i<key.length;i++)
  		{result.append(tempobj[i].toString());}
  		
  		return String.valueOf(result);
  	}
  	//
  	
  	
  	
  	//
  	public HashMap< Integer , ArrayList<String>> modModList(int type, int firstrow, DefaultTableModel Model)
  	{   Object[] tempobj = new Object[Model.getColumnCount()];
  		ArrayList<String> templist = new ArrayList<String>();
  		HashMap< Integer , ArrayList<String>> TeList = new HashMap<Integer, ArrayList<String>>();
  	if (type!=-1)
  	{
  		for (int i=0; i<Model.getColumnCount(); i++)
  			{if (Model.getValueAt(firstrow, i)!="")
  			 {
  				tempobj[i]=Model.getValueAt(firstrow, i);
  			 }else
  			 	{
  				tempobj[i]="";
  			 	}
  			}
  	}
  		if (type==1){	
  			templist.add(String.valueOf(type));
  			templist.add("x");
  			templist.add(keyTable(tempobj));
  			if (ModList.get(firstrow)==null)
  				{ModList.put(firstrow, templist);}
  			else{
  				 
  				TeList.put(firstrow, templist); 
  				HashMap< Integer , ArrayList<String>> ModListTemp = new HashMap< Integer , ArrayList<String>>(ModList);
  				for (int i : ModListTemp.keySet())
  				 {
  					 if (i>=firstrow)
  					 {
  						 TeList.put(i+1, ModList.get(i));
  						 ModList.remove(i);
  					 }
  				 }
  				ModList.putAll(TeList);
  				}
  		}else if (type==0)
  		{
  			if (ModList.get(firstrow).get(0).equals("2") || ModList.get(firstrow).get(0).equals("0"))
  			{
  				ModList.get(firstrow).set(0, "0");
  			} else if (ModList.get(firstrow).get(0).equals("1"))
  			{
  				ModList.get(firstrow).set(2, keyTable(tempobj));
  			}
  		}else if (type==-1)
  		{
  			if (ModList.get(firstrow).get(0).equals("2") || ModList.get(firstrow).get(0).equals("0"))
  			{
  				DeList.addAll(ModList.get(firstrow));
  				ModList.remove(firstrow);
  			} else if (ModList.get(firstrow).get(0).equals("1"))
  			{
  				ModList.remove(firstrow);
  			}
  			HashMap< Integer , ArrayList<String>> ModListTemp = new HashMap< Integer , ArrayList<String>>(ModList);
  			for (Integer i : ModListTemp.keySet())
  			{
  				if (i>firstrow)
  				{
  					TeList.put(i -1, ModList.get(i));
  					ModList.remove(i);
  				}
  			}
  			ModList.putAll(TeList);
  		}	
  		///getModListC(ModList);
  		System.out.print("dupa");
  		return ModList;
  	}
  	public HashMap<Integer, ArrayList<String>> getModList()
  	{
  		return ModList;
  	}
  	public void ssss()
  	{
  		System.out.print(ModList);
  	}
  ////
  ////
  ////
  ////

  ///
}
	
//sterowanie comboboxem(osobno od innych metod)
	public void CommboBoxDefault(Object[][] dictlist, HashMap<String, ArrayList<String>> sysall, JTable table)
	{
		for (int i=0; i<table.getColumnCount(); i++)
		{
			if (sysall.get(table.getColumnName(i)).size()==5)
			{
				(new CombBox()).setUpColumnNew(table, table.getColumnModel().getColumn(i), sysall.get(table.getColumnName(i)).get(4).toString(), dictlist);
			}
		}
	}

 //Tutaj dzieje sie ComboBox
 class CombBox
 {
    public void setUpColumnNew(JTable table, TableColumn sportColumn, String dictname, Object[][] dictlist)
	{
		Vector items = new Vector();
		
		for (int i=0; i<dictlist.length; i++)
		{
			if (dictlist[i][0].equals(dictname))
			{
				items.addElement( new Item(dictlist[i][1].toString(), dictlist[i][2].toString()));
			}
		}
		JComboBox comboBox = new JComboBox(items);
		comboBox.setRenderer(new TextPaneRenderer(3));
		sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
	}    
 
	class Item
	{
		private String id;
		private String description;

		public Item(String id, String description)
		{
			this.id = id;
			this.description = description;
		}

		public String getId()
		{
			return id;
		}

		public String getDescription()
		{
			return description;
		}

		public String toString()
		{
			return id;
		}
	}
	class TextPaneRenderer extends JTextPane implements ListCellRenderer
	{
		public TextPaneRenderer(int tabColumn)
		{
			setMargin( new Insets(0, 0, 0, 0) );

			FontMetrics fm = getFontMetrics( getFont() );
			int width = fm.charWidth( 'w' ) * tabColumn;

			TabStop[] tabs = new TabStop[1];
			tabs[0] = new TabStop( width, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE );
			TabSet tabSet = new TabSet(tabs);

			SimpleAttributeSet attributes = new SimpleAttributeSet();
			StyleConstants.setTabSet(attributes, tabSet);
			getStyledDocument().setParagraphAttributes(0, 0, attributes, false);
		}

		public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus)
		{
			Item item = (Item)value;

			if (index == -1)
				setText( item.getDescription() );
			else
				setText(item.getId() + "\t" + item.getDescription());
			setBackground(isSelected ? list.getSelectionBackground() : null);
			setForeground(isSelected ? list.getSelectionForeground() : null);
			return this;
		}
	} 
 }
	
	public static ImageIcon GetImg()
	{
	ImageIcon img = new ImageIcon("C:\\Java_tech\\File\\ikona.PNG");
	return img;
	}
	public JPanel GetUpMenu(boolean setFirm)
	{
		GridLayout gridlayout = new GridLayout(1,3);
		JPanel upmenu = new JPanel(gridlayout);
		upmenu.setBorder(new TitledBorder(""));
			String FirmName;
			if (FirmCode==null)
			    {FirmName="firma";}
			else {
				FirmName=FirmCode;}
			firm = new JButton(FirmName);
			firm.setForeground(new Color(47,117,181));
		upmenu.add(new JLabel(""));
		upmenu.add(firm);	
		upmenu.add(new JLabel(""));
		handlere handlere = new handlere(setFirm, firm);
		firm.addActionListener(handlere);	
		return upmenu;	
	}
	public JButton getBFirm()
	{
		return firm;
	}
	public void setFirmCode()
	{ 
 
		firm.setText(select.getFirmCode());
		   
	}
	public static String getFirmCode(String FirmCodes)
	{
		FirmCode = FirmCodes;
		 
		return FirmCode;
	}
	public void setFirmCodeSec()
	{
		System.out.print(FirmCode);
	}
	
	
	
static class handlere implements ActionListener
	{
		private JButton firm;
		private boolean setFirm;
		public handlere (boolean setFirm, JButton firm)
		{
			this.firm = firm;
			this.setFirm = setFirm;
		}
		public void actionPerformed(ActionEvent e) {
			Object z = e.getSource();
			if (z==firm)
			{
				ICompanySelection select = new ICompanySelection();
				select.setF(setFirm);
				select.setFirm(firm);
				select.setVisible(true);
			}
		}	
	}
}

class MyRenderer extends DefaultTableCellRenderer 
{
   Color bg, fg;
   public MyRenderer(Color bg, Color fg) {
      super();
      this.bg = bg;
      this.fg = fg;
   }
   public Component getTableCellRendererComponent(JTable table, Object 
   value, boolean isSelected, boolean hasFocus, int row, int column) 
   {
      Component cell = super.getTableCellRendererComponent(table, value, 
      isSelected, hasFocus, row, column);
      cell.setBackground(bg);
      cell.setForeground(fg);
      return cell;
   }
}
