package MyLittleSmt;

import java.awt.Color;
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
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
 
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
 
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class FrameTemplateListener implements KeyListener, MouseListener, ActionListener, TableModelListener, ListSelectionListener 
{
	private JTable table;//
	private DefaultTableModel Model;//
	private HashMap<String, ArrayList<String>> sysall;//
	private JMenuItem addRowM, deleteM, UsuFiltM, filtr, cocel,coall,corow, excelM, pasteM, csvM, addSourM, openSourM,delSourM, showSourM;
	private JMenuItem SaveAllUp, ExcelUp, ImpUp, DocuUp, AddRowMUp, csvUp;
	private JButton bSave, bAdd, bImp, bDoc, bXLS, bCSV, bDup, bCon;
    private HashMap< Integer , ArrayList<String>> ModList;
	private ArrayList<KeyValue> DeList2;
    private static final String LINE_BREAK = "\n"; 
    private static final String CELL_BREAK = "\t"; 
    private final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
    private int column, row; 
    private HashMap<String, String> mapsysBase, mapsys;
    private  ArrayList<ArrayList<String>> ModelInStringList;
    private ArrayList<ArrayList<Object>> deleteItem;
    TableRowSorter<TableModel>  sorter ;
    ArrayList<Integer> TableKey;
    HashMap<Integer, KeyValue> ModList2;
    private int keyCol = -1; ///kolumna, do której dodajemy klucz
    private int genKeyType=-1; ///rodzaj klucza, np 0- klucz dla CoterpartiesId
    private int genKeyModel=-1; ///ca³y model ododawania klucza, np nadawanie klucza w chwili zapisu pola
    private int addRowNum;
    private boolean isModListWork = true;
    private int FirmColumn=-1;
    private JButton FirmCode;
    private HashMap<Integer,ArrayList<ArrayList<String>>> HashMapDict; ///Ten s³ownik okreœla kolumnê i s³ownik jaki ma zostaæ podmieniony w trakcie pracy
    private String addKeyDate, addKeyFirm;
    private ArrayList<Integer> skipList;
    private JButton smallButtonAdd, smallButtonSave, smallButtonExcel,smallButtonCSV, smallButtonImport,smallButtonDoc;
    private ILogAddToLog AddLog;
    private String newWorkPath=null;
    private boolean deleteEnable = true;
 
    public void setDeleteEnable(boolean deleteEnable)
    {
    	this.deleteEnable=deleteEnable;
    }
    
    public int getAddRowNum()
    {
    	return addRowNum;
    }
    /**
     * Inicjowanie nadawanie klucza na zapisie
     * @param keyCol - kolumna w której powinien byæ umiejscowiony klucz
     * @param genKeyType - typ klucza, np 0 to klucz ConterpartiesID
     * @param genKeyModel - Model generacji klucza, np 0 - generowanie podczas zapisu
     */
    public void setGgenKey(int keyCol, int genKeyType, int genKeyModel)
    {
    	this.keyCol = keyCol;
    	this.genKeyType = genKeyType;
    	this.genKeyModel=genKeyModel;
    }
    
    public void setkeyCol(int keyCol )
    {
    	this.keyCol = keyCol;
 
    }
       
    public void setGgenKey(int keyCol, int genKeyType, int genKeyModel, String addKeyDate, String addKeyFirm)
    {
    	this.keyCol = keyCol;
    	this.genKeyType = genKeyType;
    	this.genKeyModel=genKeyModel;
    	this.addKeyDate = addKeyDate;
    	this.addKeyFirm = addKeyFirm;
    }
    
	public FrameTemplateListener(JTable table, DefaultTableModel Model, 
			HashMap<String, ArrayList<String>> sysall, 
			HashMap<String, String> mapsysBase2, 
			HashMap<String, String> mapsys2,
			ArrayList<Integer> TableKey)
	{	
		this.table = table;
		this.Model = Model;
		this.sysall = sysall; 
		this.mapsysBase=mapsysBase2;
		this.mapsys = mapsys2;
		this.TableKey = TableKey;	
	}
	
	
	void getContextMenu(JMenuItem addRowM,JMenuItem deleteM, JMenuItem UsuFiltM, JMenuItem filtr, 
			JMenuItem cocel,JMenuItem coall,JMenuItem corow,JMenuItem excelM,JMenuItem pasteM, JMenuItem csvM,
			JMenuItem addSourM, JMenuItem openSourM,JMenuItem delSour,JMenuItem showSour)
	{
		this.addRowM = addRowM;
		this.deleteM = deleteM;
		this.UsuFiltM = UsuFiltM;
		this.filtr = filtr;
		this.cocel = cocel;
		this.coall = coall;
		this.corow = corow;
		this.excelM = excelM;
		this.pasteM = pasteM;
		this.csvM = csvM;
		this.addSourM = addSourM;
		this.openSourM = openSourM;
		this.delSourM =delSour;
		this.showSourM=showSour;
	}
	
	void getUpMenu(JMenuItem SaveAllUp,JMenuItem ExcelUp,JMenuItem ImpUp,JMenuItem DocuUp,
					JMenuItem AddRowMUp, JMenuItem csvUp)
	{
		this.SaveAllUp =SaveAllUp;
		this.ExcelUp =ExcelUp;
		this.ImpUp =ImpUp;
		this.DocuUp = DocuUp;
		this.AddRowMUp =AddRowMUp;
		this.csvUp=csvUp;
	}
	
	void getSmallButtons(JButton smallButtonAdd,JButton smallButtonSave,JButton smallButtonExcel,JButton smallButtonCSV,JButton smallButtonImport,JButton smallButtonDoc)
	{
		this.smallButtonAdd = smallButtonAdd;
		this.smallButtonSave = smallButtonSave;
		this.smallButtonExcel = smallButtonExcel;
		this.smallButtonCSV = smallButtonCSV;
		this.smallButtonImport = smallButtonImport;
		this.smallButtonDoc = smallButtonDoc;
	}
	
	void getJTabelProp(DefaultTableModel Model, JTable table)
	{
		this.Model = Model;
		this.table = table;
	}
	void setModel(DefaultTableModel Modele)
	{
		this.Model = Modele;
	}	
	void getRibbon(JButton bAdd, JButton bSave, JButton bXLS, JButton bCSV, JButton bImp, JButton bDoc, JButton bDup, JButton bCon)
	{
	
		
		this.bAdd = bAdd;
		this.bSave = bSave;
		this.bXLS = bXLS;
		this.bCSV = bCSV;
		this.bImp = bImp;
		this.bDoc = bDoc;
		this.bDup= bDup;
		this.bCon = bCon;
	}
	
	public void setFirm(JButton firmButton)
	{
		this.FirmCode = firmButton;
		// this.FirmCode = firmCode;
	}
	
	public void setFirmCode(int firmcolumn, JButton firmButton)
	{
		this.FirmColumn = firmcolumn;
		this.FirmCode = firmButton;
	}
	public void setHashMapDict(HashMap<Integer,ArrayList<ArrayList<String>>> HashMapDict)
	{
		this.HashMapDict =HashMapDict;
	}
	
	private DefaultTableModel getModel()
	{
		return Model;
	}
	
	@Override
	public void keyPressed(KeyEvent eve) {
		if ((eve.getKeyCode() == KeyEvent.VK_N) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
		{
			runEmptuRow(Model, sysall, table);	//ok
        }else if ((eve.getKeyCode() == KeyEvent.VK_F) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
        {
        	removeFiltr();                     //ok
        }else if ((eve.getKeyCode() == KeyEvent.VK_D) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
        {
        	removeSelectedRow(Model, table);  //ok
        }else if ((eve.getKeyCode() == KeyEvent.VK_C) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
        {
        	CopyCell(table);//ok
        }else if ((eve.getKeyCode() == KeyEvent.VK_R) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
        {
        	CopyRow(table);//ok
        }else if ((eve.getKeyCode() == KeyEvent.VK_A) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
        {
        	CopyAll(table);//ok
        }else if ((eve.getKeyCode() == KeyEvent.VK_V) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
        {
        	try 
        	{
				pasteFromClipboard(table);
			} catch (UnsupportedFlavorException | IOException e) 
        	{
				e.printStackTrace();
			}
        }else if ((eve.getKeyCode() == KeyEvent.VK_E) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
        {
        	JTExcel(Model);
        }else if ((eve.getKeyCode() == KeyEvent.VK_S) && ((eve.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0)) 
        {	
        cancelEditing();
        SaveJTable();
        }
	}
	
	
	//SaveMode
	@Override
	public void keyReleased(KeyEvent eve) 
	{
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
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) 
			{
			if (e.getSource()==table)
				{
					row = table.rowAtPoint(e.getPoint());
					column = table.columnAtPoint(e.getPoint());
					if (filtr!=null)
						{if (row>=0 && column>=0)
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
	}


	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object eve = e.getSource();

		if (eve==filtr )//&& filtr.getForeground()==Color.BLACK)
		{
			ButtonFiltrModel();
		} else if (eve==UsuFiltM)
		{	
				removeFiltr();
		} else if (eve==addRowM||eve==AddRowMUp||eve==bAdd||eve==smallButtonAdd)
		{
			runEmptuRow(Model, sysall, table);
		}else if (eve==deleteM)
		{
			 removeSelectedRow(Model, table);
		}else if (eve==cocel)
		{
			CopyCell(table);
		}
		else if (eve==corow)
		{
			CopyRow(table);
		}
		else if (eve==coall)
		{
			CopyAll(table);
		}
		else if (eve==pasteM)
		{
			 try 
			 {
				pasteFromClipboard(table);
			} catch (UnsupportedFlavorException | IOException e1) {
				e1.printStackTrace();
			}
		}else if (eve==excelM || eve==ExcelUp||eve==bXLS||eve==smallButtonExcel)
		{
			JTExcel(Model);
		}
		else if (eve==SaveAllUp||eve==bSave||eve==smallButtonSave)
		{
			Button_Save();
		}
		else if (eve==DocuUp||eve==bDoc||eve==smallButtonDoc)
		{
			ImpDocSimpl(sysall, table);
		}
		else if (eve==csvM||eve==csvUp||eve==bCSV||eve==smallButtonCSV)
		{
			JTCSV(Model);
		}
		else if (eve==ImpUp||eve==bImp||eve==smallButtonImport)
		{
			importData(sysall, Model, TableKey, keyCol);
		}else if (eve==bDup)
		{
			DuplicateButton(table.getSelectedRow(), table, Model,TableKey);
			//runEmptuRow(Model, sysall, table);
			
		}else if (eve==bCon)
		{
			JFrame conter = new ICounterparties(500,500, "Kontrahenci");
			conter.setVisible(true);	
			conter.setExtendedState(JFrame.MAXIMIZED_BOTH);
			//runEmptuRow(Model, sysall, table);
			
		}else if (eve==addSourM)
		{
			documentSource(1);
		}else if (eve==openSourM)
		{
			documentSource(0);
		}else if (eve==delSourM)
		{
			documentSource(2);
		}else if (eve==showSourM)
		{
			documentSource(3);
		}
	}
	public void setSourceWorkPath(String newWorkPath)
	{
		this.newWorkPath=newWorkPath;
	}
	

	public void documentSource(int type)
	{
		if (table.getSelectedColumn()>=0&&table.getSelectedRow()>=0&&keyCol>=0)
		{
			var nds = new DocumentsSource();
			if (newWorkPath!=null)
			{
				nds.setWorkPath(newWorkPath);
			}
			HashMap<String, Integer> colT = new FrameTemplate().getColumnNumbers(table, sysall);
			String colName = Model.getColumnName(keyCol);
			String id = table.getValueAt(table.getSelectedRow(), colT.get(colName)).toString();
			String firm = table.getValueAt(table.getSelectedRow(), colT.get("FIRM")).toString();
			if (id.trim().length()>0&&firm.trim().length()>0)
			{
				if (type==1)
				{
					nds.addSource(id, firm);
				}else if (type==0)
				{
					nds.openSource(id, firm);
				}
				else if (type==2)
				{
					nds.deleteSource(id, firm);
				}else if (type==3)
				{
					nds.showSource(id, firm);
				}
			}
		}
	}
	
	public void runDuplicateButton(int row)
	{
		DuplicateButton(row, table, Model,TableKey);
	}
 
	public void DuplicateButton(int inRow, JTable table, DefaultTableModel Model, ArrayList<Integer> TableKey)
	{
		
		if (inRow>-1)
		{
		//	int row = ModelRowByJTable(table, TableKey);
			int row = 0;
			cancelEditing();
			Object[] templist = new Object[table.getColumnCount()];
			for (int i=0; i<table.getColumnCount();i++)
			{
				if (i!=keyCol)
				{
					templist[i]=table.getValueAt(row, i);
				}
			}
			
			Model.insertRow(row, templist);
			table.setRowSelectionInterval(0,0);
			table.setColumnSelectionInterval(0, 0); 
			
		}
		
		
	}
	
	public int ModelRowByJTable(JTable table, ArrayList<Integer> TableKey)
	{
		int value = 0;
		HashMap<Integer, String> keyHashMap = new HashMap<Integer, String>();
		int sRow = table.getSelectedRow();
		int sColumn = table.getSelectedColumn();
		for(int i=0; i<TableKey.size(); i++)
		{
			keyHashMap.put(TableKey.get(i), table.getValueAt(sRow, TableKey.get(i)).toString());
		}

		boolean setValue = false;
		for (int i=0; i<Model.getColumnCount(); i++)
		{
			int v=-1;
			for (int j: keyHashMap.keySet())
			{
				v = j;
				if (keyHashMap.get(j).equals(Model.getValueAt(i, j).toString()))
				{
					setValue = true;
				}else
				{
					setValue = false;
					continue;
				}
			}
			if (setValue == true&&v>-1)
			{
				value = v;
				break;
			}
		}
		//System.out.print(value);
		return value;
	}
 
	
	
	public void ButtonFiltrModel()
	{	//table.setAutoCreateRowSorter(false);
		cancelEditing();
		int column = table.getSelectedColumn();
		int row = table.getSelectedRow();
	///	System.out.println(column + " : " + row);
		if (column<0||row<0) 
		{
			return;
			}
		
		if (sysall.get(table.getColumnName(column)).get(2).equals("nvarchar") ||
				sysall.get(table.getColumnName(column)).get(2).equals("char") ||
				sysall.get(table.getColumnName(column)).get(2).equals("nchar") ||
				sysall.get(table.getColumnName(column)).get(2).equals("bit")||
				sysall.get(table.getColumnName(column)).get(2).equals("tinyint "))
			
		{
			String strfiltr =JOptionPane.showInputDialog(null,"WprowadŸ szukan¹ frazê", table.getValueAt(row, column)); 
			StringFiltrModel(strfiltr, column);
		}else if (sysall.get(table.getColumnName(column)).get(2).equals("date"))
		{
		String strfiltr =JOptionPane.showInputDialog(null,"WprowadŸ szukan¹ frazê", table.getValueAt(row, column));
		if  (strfiltr!=null)
		{
			StringBuilder ValOper = new StringBuilder(strfiltr);
			
			int between = ValOper.lastIndexOf("...");
			int big = ValOper.lastIndexOf(">");
			int smal = ValOper.lastIndexOf("<");
			String strStartDate = null;
			String strEndtDate = null;
			if (between>-1)
			{
				strStartDate = ValOper.substring(0, between);
				strEndtDate = ValOper.substring(between + 3 , ValOper.length());///endDate
			}else if (big>-1)
			{
				if (big==0)
				{
					strStartDate= ValOper.substring(1, ValOper.length());///startdate
				}else 
				{
					strEndtDate= ValOper.substring(0, big);///enddate
				}
			}else if (smal>-1)
			{
				if (smal==0)
				{
					strEndtDate = ValOper.substring(1, ValOper.length());///end date
				}else
				{
					strStartDate =  ValOper.substring(0, smal) ;///start date
				}
			} else 
			{
				strStartDate = ValOper.toString();
				strEndtDate = ValOper.toString();
				
			}
			try {

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					Date startDate = null;
					Date endDate = null;
					
					if (strEndtDate!=null && strStartDate!=null)
					{
						strEndtDate = LocalDate.parse(strEndtDate , formatter).plusDays(1).format(formatter);
						strStartDate = LocalDate.parse(strStartDate , formatter).minusDays(1).format(formatter);
						endDate = Date.valueOf(strEndtDate );
						startDate = Date.valueOf(strStartDate); ///-1
					}	
					else if(strEndtDate!=null)
					{
						strEndtDate = LocalDate.parse(strEndtDate , formatter).plusDays(1).format(formatter);
						endDate = Date.valueOf(strEndtDate );
						startDate = Date.valueOf("1900-01-01");
					}else if (strStartDate!=null)
					{
						endDate = Date.valueOf("2999-12-31");
						strStartDate = LocalDate.parse(strStartDate , formatter).minusDays(1).format(formatter);
						startDate = Date.valueOf(strStartDate); ///-1
					}

					DateFiltrModel(startDate  , endDate);
			} catch (DateTimeParseException e) {
				// TODO Auto-generated catch block
				//System.out.print(e.getErrorIndex()); ///error 338
			}
		}
		}
		//table.setAutoCreateRowSorter(true);
	}
	public void DateFiltrModel(Date startDate, Date endDate)
	{
		int column = table.getSelectedColumn();
		cancelEditing();
		RowFilter<Object, Object> filterrow = new RowFilter<Object, Object>()
				
		{
			public boolean include (Entry entry)
			{
				if (Date.valueOf(entry.getStringValue(column)).before(endDate)&&
						Date.valueOf(entry.getStringValue(column)).after(startDate))
					return true;
				else
					return false;
			}
		};
		sorter = new TableRowSorter<TableModel>(Model);
		sorter.setRowFilter(filterrow);
		table.setRowSorter(sorter);
		
	
		
	}
	
	
	public void StringFiltrModel(String strfiltr, int column)
	{
		
		cancelEditing();
			if (strfiltr!=null)
			{
				RowFilter<Object, Object> filterrow = new RowFilter<Object, Object>()
				{
					public boolean include (Entry entry)
					{
						return entry.getStringValue(column).toUpperCase().contains(strfiltr.toUpperCase());
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
	public void StringFiltrModel(ArrayList<ArrayList<String>> list, int column)
	{
		
		cancelEditing();
			if (list.size()>1)
			{
				List<RowFilter<Object,Object>> filters0 = new ArrayList<RowFilter<Object,Object>>(0);
				 
				
				
				RowFilter<Object, Object> filterrow = new RowFilter<Object, Object>()
				{
					public boolean include (Entry entry)
					{	boolean isOK = false;
						for (int i =1;i<list.size();i++)
						{	
						String strfiltr = list.get(i).get(0);
						if (entry.getStringValue(column).toUpperCase().contains(strfiltr.toUpperCase()))
						{
							isOK= entry.getStringValue(column).toUpperCase().contains(strfiltr.toUpperCase());
							break;
						}
						}
						return isOK;
					}
				};
					filters0.add(filterrow);
				
 
				var rf = RowFilter.andFilter(filters0);
				sorter = new TableRowSorter<TableModel>(Model);
				sorter.setRowFilter(rf);
				table.setRowSorter(sorter);
	
			} else
			{
				StringFiltrModel(" ", column);
				//removeFiltr();
			}
		
	}
	public void Button_Save(String addKeyDate, String addKeyFirm)
	{
		this.addKeyDate= addKeyDate;
		this.addKeyFirm= addKeyFirm;
		
        cancelEditing();
        SaveJTable();
	}
	
	public void Button_Save()
	{
        cancelEditing();
        SaveJTable();
        
	}
	/**
	 * Lista kolumn które maj¹ nie byæ brane pod uwagê podczas zapisu 
	 * @param list
	 */
	public void addSkipList(ArrayList<Integer> list)
	{
		 
		this.skipList = list;
	}
	
	public void SaveJTable()
	{ 
		AddLog = new ILogAddToLog();
		HashMap<Integer, ArrayList<String>> Condition= new HashMap<Integer, ArrayList<String>>();
		HashMap<Integer, ArrayList<String>> Update = new HashMap<Integer, ArrayList<String>>();
		HashMap<Integer, ArrayList<String>> Insert = new HashMap<Integer, ArrayList<String>>();
		HashMap<Integer, ArrayList<String>> Delete = new HashMap<Integer, ArrayList<String>>();
		HashMap<String, String> mapsysSec = new HashMap<String, String>();
		ArrayList<String> ColumnName = new ArrayList<String>();
		ArrayList<String> ColumnCondition = new ArrayList<String>();
		ArrayList<ArrayList<String>> UpdateCL = new ArrayList<ArrayList<String>>();
		if (genKeyModel==0)
		{
			if (keyCol>-1&&genKeyType>-1)
			{
				genKey();
			}
		}
		for (String i: mapsys.keySet())
		{
			if (skipList!=null&& skipList.contains(i))
			{
				///log o pominiêciu kolumny 
			}else
			{
				mapsysSec.put(mapsys.get(i), i);
			}
		}
		
		for (int i=0; i<Model.getColumnCount();i++)
		{
			if (skipList!=null&& skipList.contains(i))
			{
				///log pominiêcia kolumny
			}else
			{	
			ColumnName.add(mapsysSec.get(Model.getColumnName(i)));
			}
		}
		 
		DefaultTableModel skipMod = Model;
		for (int i=0; i< TableKey.size();i++)
		{	
		 
		}
		int q =0;
		boolean skipRun=false;
		for (int i=0; i< TableKey.size();i++)
		{
			//if (skipList!=null&& skipList.contains(i))
		//	{
			//	q++;
		////		ColumnCondition.add(mapsysSec.get(Model.getColumnName(TableKey.get(i)+q)));
		//		skipRun=true;
				
		//	}else if (skipRun==true)
		//	{
		//		ColumnCondition.add(mapsysSec.get(Model.getColumnName(TableKey.get(i)+q)));
		////	}
		//	else
		////	{
				ColumnCondition.add(mapsysSec.get(Model.getColumnName(TableKey.get(i))));
			
		//	}
			 
		}
		Condition.put(-1, ColumnCondition);
		Update.put(-1, ColumnName);
		Insert.put(-1, ColumnName);
		Delete.put(-1, ColumnCondition);
		for (Integer i : ModList2.keySet())
		{ 
			ArrayList<String> TempList = new ArrayList<String>();
			for (int j=0; j<Model.getColumnCount(); j++)
			{	
				if (skipList!=null&& skipList.contains(j))
				{
					///log pominiêcia kolumny
				}else
				{
					if (sysall.get(Model.getColumnName(j)).get(2).equals("bit"))
					{
					  if (Model.getValueAt(i, j).equals(true))
					  {
						  TempList.add("1");
					  }
					  else if (Model.getValueAt(i, j).equals(false))
					  {
						  TempList.add("0");
					  }
					}
					else if (sysall.get(Model.getColumnName(j)).get(2).equals("numeric"))
					{
						TempList.add(Model.getValueAt(i, j).toString().replace(",", "."));
					}
					else if (HashMapDict!=null&&HashMapDict.containsKey(j))
					{
							String var = "";
							for (int k=1; k<HashMapDict.get(j).size(); k++)
							{
								if (HashMapDict.get(j).get(k).get(1).equals(Model.getValueAt(i, j)))
								{
									var = HashMapDict.get(j).get(k).get(0);
									
									break;
								}
							}
							
							TempList.add(var);
						
					}
					else
					{
						if (Model.getValueAt(i, j)==null)
						{
							TempList.add("");	
						}else
						{
							String str = Model.getValueAt(i, j).toString().trim();
							str = str.contains("\\") ?  Model.getValueAt(i, j).toString().replace("\\", "\\\\") : Model.getValueAt(i, j).toString();
							str = str.replace("\"", "\\\"");

							TempList.add(str);
						}
					}
				}
			}
			if (ModList2.get(i).getTyp().equals("1"))
			{
				
			Condition.put(i, ModList2.get(i).getList());
			Insert.put(i, TempList);
			}else if (ModList2.get(i).getTyp().equals("0"))
			{
			Condition.put(i, ModList2.get(i).getList());
			UpdateCL.add(TempList);
			Update.put(i, TempList);
			}
		}
		
		
		
		for (int i=0;i<DeList2.size();i++)
		{
			Delete.put(i, DeList2.get(i).getList());
		}
		///Tutaj 
		DataImport dataimport = new DataImport();
		boolean isCorect = dataimport.CheckData(UpdateCL, sysall, keyCol);
		PythonBase pythonbase = new PythonBase();
		if (Condition.size()>1||Delete.size()>1) 
		{
			if (skipList!=null)
			{
				for (int i=0;i<Model.getColumnCount();i++)
				{
					if (skipList.contains(i))
					{
						mapsysBase.remove(Model.getColumnName(i));
					}
				}
			}
			AddLog.AddToLog("Modyfikacja:" + (Update.size()-1) + "; dodanie: " + (Insert.size()-1) + "; usuniêcie:" + (Delete.size()-1) + "; rekordów.", "(I)");
			pythonbase.JTableToBase(mapsysBase,Condition, Update,Insert,Delete);
			DeList2.clear();
			ModList2.clear();
			startModList(FullModelToObject(false, Model));
			ArrayList<ArrayList<Object>>  ListLog= AddLog.getListLog();
			ListLog.addAll(pythonbase.getListLog());
			ILog log = new ILog(300, 600, "Log importu", ListLog);
			log.setVisible(true);

			
		}
		
		
//		ArrayList<ArrayList<Object>> ListLog = new ArrayList<ArrayList<Object>>();
//		ListLog.addAll(dataimport.getListLog());
//		ILog log = new ILog(300, 600, "Log importu", ListLog);
//		log.setVisible(true);
	}
	
	/////////////////
	///
	///
	///Metody
	///
	///
	///
	/////////////////
	//
	//
	//Dodawanie liniii
	public void runEmptuRow(DefaultTableModel Model, HashMap<String, ArrayList<String>> sysall, JTable table)
	{
		int rownum;
		if (table.getSelectedRow()<0)
		{
			rownum=0;
		}else
		{
			rownum=table.getSelectedRow();
		}
		addRowNum= rownum;
		addEmtpyRow(rownum, Model, sysall, table);
	}
	public void runEmptuRow(DefaultTableModel Model, HashMap<String, ArrayList<String>> sysall, JTable table, int rownum)
	{
		addRowNum= rownum;
		addEmtpyRow(rownum, Model, sysall, table);
	}
	
	public void addEmtpyRow(int rownum, DefaultTableModel Models, HashMap<String, ArrayList<String>> sysall, JTable table)
	{
		//getDoClickAddRow();
		
		Object[] emptyrow = new Object[Model.getColumnCount()];
		for  (int i=0; i<Model.getColumnCount(); i++)
		{
			
			if (sysall.get(Model.getColumnName(i)).get(2).equals("bit"))
			{
				emptyrow[i]=false;
			}else if (sysall.get(Model.getColumnName(i)).get(2).equals("date"))
			{
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				emptyrow[i]= LocalDate.parse((CharSequence) "1901-01-01", formatter);
				 
			}else if (sysall.get(Model.getColumnName(i)).get(2).equals("smallint")
					||sysall.get(Model.getColumnName(i)).get(2).equals("numeric"))
			{
				emptyrow[i]=0;
				 
			}else if (FirmColumn>=0&&i==FirmColumn)
			{
				emptyrow[i]=FirmCode.getText();
			}
			else
			{
				emptyrow[i]="";
			}
		}
 
	//	runEmptuRow(Model, sysall, table);
		if (table!=null)
		{
		table.setAutoCreateRowSorter(false);
		}
		Model.insertRow(rownum, emptyrow);
 
		if (table!=null&&table.getRowCount()>-1)
		{
		table.setRowSelectionInterval(rownum, rownum);
		table.setAutoCreateRowSorter(true);
		}
	}	
	
	
	public void RowSorterFalse()
	{
		if (table!=null&&table.getRowCount()>0)
		{
			table.setAutoCreateRowSorter(false);
			table.setRowSelectionInterval(0,0);
 			table.setAutoCreateRowSorter(true);
		}
	}
	//
	//
	//Usuwanie filtru
	public void removeFiltr()
	{
		if (sorter!=null)
		{
		table.setAutoCreateRowSorter(false);
		sorter.setRowFilter(null);
		table.setAutoCreateRowSorter(true);
		}
	}
	//
	//
	//Import 
	public void  importData(HashMap<String, ArrayList<String>> sysall, DefaultTableModel Model, ArrayList<Integer> TableKey, int keyCol)
	{
		
		DataImport dataimport= new DataImport();
		ArrayList<ArrayList<String>> plusdata = dataimport.DataToJTable(sysall, FullModelToStringList(true, Model, sysall), TableKey, keyCol);
		
		if (plusdata!=null)
		{
			for (int i=1;i<plusdata.size();i++)
			{
				Object[] tempobj = new Object[Model.getColumnCount()];
				for (int j=0; j<plusdata.get(i).size();j++)
				{
					
					if (sysall.get(plusdata.get(0).get(j)).get(2).equals("bit"))
					{
						if (plusdata.get(i).get(j).equals("true")||
							plusdata.get(i).get(j).equals("1")||
							plusdata.get(i).get(j).equals("PRAWDA"))
						{tempobj[j] = true;}
						else if (plusdata.get(i).get(j).equals("false")||
								plusdata.get(i).get(j).equals("0")||
								plusdata.get(i).get(j).equals("FA£SZ")
								)
						{tempobj[j] = false;}
					}else if(HashMapDict!=null&&HashMapDict.containsKey(j))
					{
						for (int q=1;q<HashMapDict.get(j).size();q++)
						{
							if (HashMapDict.get(j).get(q).get(0).equals(plusdata.get(i).get(j)))
									{
										tempobj[j] = HashMapDict.get(j).get(q).get(1);
									}
						}
					}
					else
					{
						tempobj[j] = plusdata.get(i).get(j);
					}
					
					
				}
				
				
				this.Model.addRow(tempobj);
				
			}
		}
		ArrayList<ArrayList<Object>> ListLog = new ArrayList<ArrayList<Object>>();
		ListLog.addAll(dataimport.getListLog());
		ILog log = new ILog(300, 600, "Log importu", ListLog);
		log.setVisible(true);
	}
 
	
	//
	//
	//Ogólne
	
 
	
	private void cancelEditing() 
	{ 
        if (table.getCellEditor() != null) 
        { 
        	 table.getCellEditor().stopCellEditing();
            //   table.getCellEditor().cancelCellEditing(); 
        } 
	} 
 
	public void removeRow(DefaultTableModel Model, JTable table, int selectedRow)
	{	
		if (selectedRow>=0)
		{
			//Techniczne do FrameTemplateDataWarehouse
			deleteItem=new ArrayList<ArrayList<Object>>();
			ArrayList<Object> column = new ArrayList<Object>();
			ArrayList<Object> row = new ArrayList<Object>();
			Object[] delRow = new Object[Model.getColumnCount()];
			for (int i=0; i<Model.getColumnCount();i++)
			{ 
				column.add(table.getColumnName(i));
				row.add(table.getValueAt(selectedRow, i));
				delRow[i]=table.getValueAt(selectedRow, i);
			}
			deleteItem.add(column);
			deleteItem.add(row);
			int firstrow=-1;
			FrameTemplateDataWarehouse.setOneDeleteItem(deleteItem);
			ArrayList<String> keyDel = keyOnTable(delRow);
			for (int i=0;i<Model.getRowCount();i++)
			{
				Object[] RowMod = new Object[Model.getColumnCount()];
				for (int j=0; j<Model.getColumnCount();j++)
				{
					RowMod[j]=Model.getValueAt(i, j);
				}
				ArrayList<String> keyMod = keyOnTable(RowMod);
				if (keyMod.equals(keyDel))
				{
					firstrow=i;
				}
			}
			
			table.setAutoCreateRowSorter(false);
			Model.removeRow(firstrow);
			table.setAutoCreateRowSorter(true);
			
		}
	}	
	public void removeSelectedRow(DefaultTableModel Model, JTable table)
	{
		if (table.getSelectedRow()>=0)
		{
			//Techniczne do FrameTemplateDataWarehouse
			deleteItem=new ArrayList<ArrayList<Object>>();
			ArrayList<Object> column = new ArrayList<Object>();
			ArrayList<Object> row = new ArrayList<Object>();
			Object[] delRow = new Object[Model.getColumnCount()];
			for (int i=0; i<Model.getColumnCount();i++)
			{ 
				column.add(table.getColumnName(i));
				row.add(table.getValueAt(table.getSelectedRow(), i));
				delRow[i]=table.getValueAt(table.getSelectedRow(), i);
			}
			deleteItem.add(column);
			deleteItem.add(row);
			int firstrow=-1;
			FrameTemplateDataWarehouse.setOneDeleteItem(deleteItem);
			ArrayList<String> keyDel = keyOnTable(delRow);
			for (int i=0;i<Model.getRowCount();i++)
			{
				Object[] RowMod = new Object[Model.getColumnCount()];
				for (int j=0; j<Model.getColumnCount();j++)
				{
					RowMod[j]=Model.getValueAt(i, j);
				}
				ArrayList<String> keyMod = keyOnTable(RowMod);
				if (keyMod.equals(keyDel))
				{
					firstrow=i;
				}
			}
			
			
			
			if (firstrow>-1)
			{
			table.setAutoCreateRowSorter(false);
			Model.removeRow(firstrow);
			table.setAutoCreateRowSorter(true);
			}
			
		}
	}
	public void deleteAllInModel( )
	{

		var column = new ArrayList<Object>();
		for (int i = 0; i<Model.getColumnCount();i++)
		{
			column.add(Model.getColumnName(i));
		}
		
		 while (Model.getRowCount() > 0) 
		 {
		 	ArrayList<Object> temp = new ArrayList<Object>();
		 	ArrayList<ArrayList<Object>> deleteItem = new ArrayList<ArrayList<Object>>();
		 	
		 	deleteItem.add(column);
		 		for (int j=0; j<Model.getColumnCount();j++)
		 		{
		 			temp.add(Model.getValueAt(0, j));
		 		}
		 	deleteItem.add(temp);
		 	FrameTemplateDataWarehouse.setOneDeleteItem(deleteItem);
		 	Model.removeRow(0);
		}
 
	}
	 
	 
	public void CopyCell(JTable table)
	{
		copyToClipboard(false, table);
	}
	
	public void CopyRow(JTable table)
	{
		table.setColumnSelectionInterval(0, table.getColumnCount()-1);
		copyToClipboard(false, table);
	}
	public void CopyAll(JTable table)
	{
		table.selectAll();
		copyToClipboard(false, table);
	}
	
	//
	//
	//Dokumentacja importu
	public void ImpDocSimpl(HashMap<String, ArrayList<String>> sysall, JTable table)
	{
		int max = 0;
		int _size = 4;
		for (String i : sysall.keySet())
			{
			  if (max<sysall.get(i).size())
			  {
				  max = sysall.get(i).size();
			  }
			}
		sysall.remove("CREATEBY");
		sysall.remove("MODBY");
		sysall.remove("MODDATETIME");
		sysall.remove("CREATIONDATETIME");
		Object[][] result =new Object[_size+1][table.getColumnCount()+1];
			
			for (int j=0; j<=_size; j++)
			{
				Object[] tempresult = new Object[table.getColumnCount()+1];
				int q = 0;
				for (int i=0; i<=table.getColumnCount();i++)
				{		
					
						if (q==0&&j==0){tempresult[q]="Mask";}else if (j==0){tempresult[q]= table.getColumnName(i-1);}
						if (q==0&&j==1){tempresult[q]="IS_NULLABLE";}else if (j==1){tempresult[q]=sysall.get(table.getColumnName(i-1)).get(1);}
						if (q==0&&j==2){tempresult[q]="DATA_TYPE";}else if (j==2){tempresult[q]=sysall.get(table.getColumnName(i-1)).get(2);}
						if (q==0&&j==3){tempresult[q]="CHARACTER_MAXIMUM_LENGTH";}else if (j==3){tempresult[q]=sysall.get(table.getColumnName(i-1)).get(3);}
						if (q==0&&j==4)
							{tempresult[q]="Dict";}
						else if (j==4)
								{
									if (sysall.get(table.getColumnName(i-1)).size()>j)
										{tempresult[q]=sysall.get(table.getColumnName(i-1)).get(j);}
									else
										{tempresult[q]=" ";}
								}	
						q++;
				}
				result[j] =tempresult; 	
		}
		FlatFile flatfile = new FlatFile();
		flatfile.JTableIntoCSV(result);
		
	}
	
	//
	//
	//Excel lub CSV + model to object na potrzeby generacji excela
	
	
	public void JTCSV(DefaultTableModel Model)
	{
		FlatFile newcsv =new FlatFile();
		newcsv.JTableIntoCSV(FullModelToObject(true, Model));
	}
	
	public void JTExcel(DefaultTableModel Model)
	{
		ExcelFile newexcel = new ExcelFile();
		newexcel.JTableIntoExcel(FullModelToObject(true, Model));
	}
	
	public ArrayList<ArrayList<String>> getFullModelToStringList()
	{
		return ModelInStringList;
	}
	
	
	
	private ArrayList<ArrayList<String>> FullModelToStringList(boolean All, DefaultTableModel Model, HashMap<String, ArrayList<String>> sysall)
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
		this.ModelInStringList = result;
		return result;
	}
	
	private Object[][] FullModelToObject(boolean All, DefaultTableModel Model)
	{
		int _size;
		if (All==true)
		{
			_size=1;
		}else
		{
			_size=0;
		}
		Object[][] result = new Object[Model.getRowCount()+_size][Model.getColumnCount()];
		Object[] TempColumn = new Object[Model.getColumnCount()];
		if (All==true)
		{
			for (int i=0; i<Model.getColumnCount();i++)
			{
				TempColumn[i] = Model.getColumnName(i);
			}
			result[0]=TempColumn;
		}
		for (int i=0; i< Model.getRowCount(); i++)
		{
			
			Object[] TempResult = new Object[Model.getColumnCount()];
			for (int j=0; j< Model.getColumnCount(); j++)
			{
			
				if (Model.getValueAt(i, j)==null)
				{
					TempResult[j] ="";
				}
				else if (String.valueOf(Model.getValueAt(i, j).getClass()).equals("class javax.swing.ImageIcon"))
				{
					if (Model.getValueAt(i, j).toString().contains("inf.png"))
					{
						TempResult[j] = "(I)";
					} else if (Model.getValueAt(i, j).toString().contains("war.png"))
					{
						TempResult[j] = "(W)";
					} else if (Model.getValueAt(i, j).toString().contains("err.png"))
					{
						TempResult[j] = "(E)";
					}
					else
					{
						TempResult[j] = Model.getValueAt(i, j);
					}
				}else
				{
				TempResult[j] = Model.getValueAt(i, j);
				}
			}
			result[i+_size] = TempResult;
		}
		return result;
	}
	//
	//
	//Kopiowanie
	public void copyToClipboard(boolean isCut, JTable table)
	{
		int numRows = table.getSelectedRowCount(); //liczone od 0 zaznaczony wiersz
		int numCols = table.getSelectedColumnCount(); ///liczona o 0 zaznaczona kolumna
		int[] rowsSelected = table.getSelectedRows();  /// liczone o 0 zaznaczone wiersze
		int[] colsSelected = table.getSelectedColumns(); // liczone od 0 zaznaczone kolumny 
	
		StringBuffer excelStr=new StringBuffer(); 
		for (int i=0; i<numRows; i++) 
		{ 
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
	
	//
	//
	//Wklejanie
	
	public void pasteFromClipboard(JTable table) throws UnsupportedFlavorException, IOException
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
////
////
////
////
////
////Monit zmian tabeli
	
	public void setIsModListWork(boolean enable)
	{
		this.isModListWork = enable;
	}
	
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub		
		//getMods(e.getType(),e.getFirstRow());	
		
	if (isModListWork == true)
	{
		DefaultTableModel Model = (DefaultTableModel) e.getSource();
		this.ModList = modModList(e.getType(),e.getFirstRow(), Model);
	}	
	}
	
	public HashMap< Integer , ArrayList<String>> startModList(Object[][] data)
	{
		/// key-numer wiersza, typ, numer wiersza oryginalnego, klucz
		
		this.ModList = new HashMap< Integer , ArrayList<String>>();
		ModList2 = new HashMap<Integer, KeyValue>();
		this.DeList2 =new ArrayList<KeyValue>();
		for (int i=0; i<data.length;i++)
		{

			ModList2.put(i, new KeyValue("2", String.valueOf(i),  keyOnTable(data[i])));
			
			
			ArrayList<String> templist = new ArrayList<String>();
			templist.add("2");
			templist.add(String.valueOf(i));
			templist.add(keyTable(data[i]));
			ModList.put(i, templist);
		}
	return ModList;
	}	
	
	public ArrayList<String> keyOnTable(Object[] list)
	{
		ArrayList<String> result = new ArrayList<String>();
		for (int i=0; i< TableKey.size(); i++)
		{
			if (list[TableKey.get(i)]!=null)
			{
				result.add(String.valueOf(list[TableKey.get(i)]));
			}
		}
		
		return result;
	}
	
	
	public String keyTable(Object[] tempobj)
	{   StringBuilder result = new StringBuilder();
		for (int i=0;i<TableKey.size();i++)
		{result.append(tempobj[i].toString());}
		
		return String.valueOf(result);
	}
	public HashMap< Integer , ArrayList<String>> modModList(int type, int firstrow, DefaultTableModel Model)
	{   Object[] tempobj = new Object[Model.getColumnCount()];
		HashMap<Integer, KeyValue> TeMod = new HashMap<Integer, KeyValue>();
		//import wartoœci z modyfikowanego wiersza
		if (type==-1)
		{
			ArrayList<ArrayList<Object>> deltem =FrameTemplateDataWarehouse.getOneDeleteItem();
			for (int i=0; i<deltem.get(0).size();i++)
			{
				tempobj[i] = deltem.get(1).get(i);
			}
			
		}else
		{
		for (int i=0; i<Model.getColumnCount(); i++)
			{
			if (Model.getValueAt(firstrow, i)!="")
				{
					tempobj[i]=Model.getValueAt(firstrow, i);
				}
			else
			 	{
					tempobj[i]="";
			 	}
			}
		}
	// Type 1 -insert
		if (type==1)
		{
			KeyValue tempKey = new KeyValue(String.valueOf(type), "x",  keyOnTable(tempobj));
			//Je¿eli nie ma jeszcze wiersza o tym numerze, po prostu go dodaje.
			if (ModList2.get(firstrow)==null)
				{ModList2.put(firstrow,  tempKey);}
			//Je¿eli ju¿ taki wiersz istnieje, tworzy techniczny ModListTemp2 który uzupe³nia nowymi wartoœciami.
			else
			{
				TeMod.put(firstrow, tempKey); 
				HashMap<Integer, KeyValue> ModListTemp2 = new HashMap<Integer, KeyValue>(ModList2);
				for (int i : ModListTemp2.keySet())
				 {
					 if (i>=firstrow)
					 {
						 TeMod.put(i+1, ModList2.get(i));
						 ModList2.remove(i);
					 }
				 }
				ModList2.putAll(TeMod);
			}	
		}
		///typ zero-modyifkacja. Je¿eli wczeœniej typ klucza=2, zmienia go na 0 (czyli do modyfikacji). Jeœli 1, zostawia bez zmian typ, ale re³aduje klucz.
		else if (type==0&&ModList2.size()>0)
		{
			if (ModList2.get(firstrow).getTyp().equals("2") || ModList2.get(firstrow).getTyp().equals("0"))
			{
				ModList2.get(firstrow).setType("0");
			} else if (ModList2.get(firstrow).getTyp().equals("1"))
			{
				ModList2.get(firstrow).setKey(keyOnTable(tempobj));
			}
		//Typ -1-usuwanie
		}else if (type==-1)
		{
			ArrayList<String> delKey = keyOnTable(tempobj);
			for (int i=0;i<Model.getRowCount();i++)
			{
				Object[] ofc = new Object[Model.getColumnCount()];
				for (int j=0;j<Model.getColumnCount();j++)
				{
					ofc[j] = Model.getValueAt(i, j);
				}
				ArrayList<String> chmod = keyOnTable(ofc);
				if (chmod.equals(delKey))
				{
					firstrow=i;
				}
			}
			if (ModList2.get(firstrow).getTyp().equals("2") || ModList2.get(firstrow).getTyp().equals("0"))
				{
				   DeList2.add(new KeyValue(ModList2.get(firstrow).getTyp(), ModList2.get(firstrow).getCol(), ModList2.get(firstrow).getList()));
				   ModList2.remove(firstrow);
				} else if (ModList2.get(firstrow).getTyp().equals("1"))
				{
					ModList2.remove(firstrow);
				}
			    
				HashMap<Integer, KeyValue> ModListTemp2 = new HashMap<Integer, KeyValue>(ModList2);
				
			    for (Integer i : ModListTemp2.keySet())
				{
					if (i>firstrow)
					{
						TeMod.put(i -1, ModList2.get(i));
						ModList2.remove(i);
					}
				}
			ModList2.putAll(TeMod);
		}
				
		return ModList;
	}
	
	
	
	private void genKey()
	{
		String id=null;
	//	if (genKeyType==0)
	//	{
	//		KeyAdder key = new KeyAdder();
	//		id = key.Conterparties();
	//	}else if (genKeyType==1)
	//	{
	//		KeyAdder key = new KeyAdder();
	//		id = key.ContactId();
	//	}
		
		//if (id!=null)
		//{
			for (Integer i: ModList2.keySet())
			{
 
			if (ModList2.get(i).getTyp().equals("1")&&getModel().getValueAt(i, keyCol).toString().trim().length()==0)
				{
				if (genKeyType==0)
				{
					KeyAdder key = new KeyAdder();
					id = key.Conterparties();
				}else if (genKeyType==1)
				{
					KeyAdder key = new KeyAdder();
					id = key.ContactId();
				}else if (genKeyType==2)
				{
					KeyAdder key = new KeyAdder();
					id = key.InstrumentId();
				}else if (genKeyType==3)
				{
					
					KeyAdder key = new KeyAdder();
					try {
						String oldDate =Model.getValueAt(i, 3).toString();
						
						id = key. PostingID(oldDate, Model.getValueAt(i, 1).toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if (genKeyType==4)
				{
					KeyAdder key = new KeyAdder();
					id = key.BankAccountId();
				}else if (genKeyType==5)
				{
					
					KeyAdder key = new KeyAdder();
					try {
						String oldDate =Model.getValueAt(i, 2).toString();
						id = key.BankStatementID(oldDate, Model.getValueAt(i, 1).toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if (genKeyType==6)
				{
					
					KeyAdder key = new KeyAdder();
					try {
						String oldDate =Model.getValueAt(i, 3).toString();
						
						id = key. PostingID(oldDate, Model.getValueAt(i, 4).toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if (genKeyType==7)
				{
					KeyAdder key = new KeyAdder();
					id = key.Conterparties();
				} else if (genKeyType==8)
				{
					KeyAdder key = new KeyAdder();
					id = key.reportsGroups();
				} else if (genKeyType==9)
				{
					KeyAdder key = new KeyAdder();
					id = key.reports();
				}
				
					getModel().setValueAt(id, i, keyCol);
				}
			}
		//}
		 
	}
	
	////
	////
	////
	////
	////
	////
	////
	//// get 
	public HashMap<Integer, ArrayList<String>> getModList()
	{
		return ModList;
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void valueChanged(ListSelectionEvent e) {}
}

class KeyValue
{
	private String typ;
	private String col;
	private ArrayList<String> list;

	public KeyValue(String typ, String col, ArrayList<String> list)
	{
		this.typ = typ;
		this.col = col;
		this.list = list;
	}
	
	public void setKey(ArrayList<String> keyOnTable) {
		// TODO Auto-generated method stub
		this.list = keyOnTable;
	}

	public void setType(String string) {
		// TODO Auto-generated method stub
		this.typ = string;
	}

	public String getTyp()
	{
		return typ;
	}
	
	public String getCol()
	{
		return col;
	}
	
	public ArrayList<String> getList()
	{
		return list;
	}
}
