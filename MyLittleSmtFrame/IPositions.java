package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.AutoAccounting;
import MyLittleSmt.DataImport;
import MyLittleSmt.DocumentsSource;
import MyLittleSmt.FlatFile;
import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateButtons;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.FrameTemplateWindow;
import MyLittleSmt.KeyAdder;
import MyLittleSmt.PythonBase;
import MyLittleSmt.PythonOther;
import MyLittleSmt.StoredProcedures;
import MyLittleSmt.Tesseract;
import MyLittleSmt.txt;

public class IPositions extends FrameTemplateWindow implements ActionListener, TableModelListener, MouseListener {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private FrameTemplate posFrame;
private HashMap<String, ArrayList<String>> posSysAll;
private HashMap<String, String> posSysBase, posSys;
private DefaultTableModel posModel;
private ArrayList<Integer> posKey;
private JPanel rOption, rExport, rImport;
private String firm;
private JTable posTable;
private JButton bPos, bSave, bBook, bUnBook, bTese, bFast, bDup;
private JMenuItem bCMDel;
private String oType;
private HashMap<String, Integer> columnNumber;
private boolean autoAdd =true;
private int newRow;
private ArrayList<ArrayList<String>> insDictList;

 	public IPositions(int x, int y, String title, String oType) {
		super(x, y, title);
		this.oType=oType;
		simpleOpenFrame();		
	}
 	
  
 	
 	private void simpleOpenFrame()
 	{
		posFrame = new FrameTemplate();
		
		JPanel upPanel = new JPanel(new BorderLayout());
		JPanel downPanel = new JPanel(new BorderLayout());
			upPanel.add(posFrame.GetUpMenu(false), BorderLayout.PAGE_START);
				JTabbedPane jtpUp = new JTabbedPane();
					rOption = new JPanel(new FlowLayout(FlowLayout.LEFT));
					rExport = new JPanel(new FlowLayout(FlowLayout.LEFT));
					rImport = new JPanel(new FlowLayout(FlowLayout.LEFT));
					setPosRibbon();
				jtpUp.add("Opcje", rOption);
				jtpUp.add("Eksport", rExport);
				jtpUp.add("Import", rImport);
			upPanel.add(jtpUp, BorderLayout.PAGE_END);
		
		
		posFrame.JTableHelperSys("IPositions_sys.yml");
		posSys = posFrame.getMapSys();
		posSysBase = posFrame.getMapSysBase();
		posSysAll = posFrame.getMapSysAll();
		this.firm = posFrame.getBFirm().getText();
		posModel = getPosModel(firm);
		posKey = new ArrayList<Integer>(Arrays.asList(0,1));
		
		posTable =  posFrame.getDefaultTable(posModel, posSysAll);
		
		//getDict(posSysAll, posTable);
		posFrame.getCurrencyDict(posTable, 6);
		posFrame.getFirmDict(posTable, 1);
		downPanel.add(new JScrollPane(posTable), BorderLayout.CENTER);
		add(upPanel,BorderLayout.PAGE_START);
		add(downPanel, BorderLayout.CENTER);
		posModel.addTableModelListener(this);
		addPosListeners();
		pack();
 	}
 	private void addPosListeners()
 	{
 		
 		posFrame.addFrameTempListener(posTable, posModel, posSysAll, posSysBase, posSys, posKey);
 		posFrame.addListenerJTable(posTable, posModel);
 		posFrame.addListenerRibbon();
 		posFrame.addContextMenu(posTable);
 		posFrame.sourceContextMenu();
 		posFrame.addListenerContextMenu();
 		posFrame.setGgenKey(0,3,0);
 		if (oType.equals("COSINV"))
 		{
 			posFrame.setMenuRun(7);
 			posTable.addMouseListener(this);
 		}
 		bCMDel = posFrame.getdeleteM();
 		posFrame.remListmDelete();
 		bCMDel.addActionListener(this);
 		//posTable.removeColumn(posTable.getColumnModel().getColumn(7));
 		HashMap<Integer, String> dictColumnAndYaml = new HashMap<Integer, String>();
 		dictColumnAndYaml.put(7, "IInstruments_Dict_Model.yml");
 		posFrame.addDictToModel(dictColumnAndYaml, posModel);
 		insDictList = posFrame.getOryginalDictList();
 		
 	}
 	
 	private void setPosRibbon()
 	{
 		rOption.add(posFrame.DefaultRibbonSim());
 			rOption.add(posFrame.startConterparty());
 		rOption.add(posFrame.DefaultRibbonPosting());
 		rExport.add(posFrame.DefaultRibbonExp());
 			bBook = new FrameTemplateButtons().RibbonJButton("Zaksiêguj", FrameTemplateImageIcon.iconJButton_SavePostring());
 			bBook.addActionListener(this);
 			bUnBook=new FrameTemplateButtons().RibbonJButton("Odksiêguj", FrameTemplateImageIcon.iconJButton_UnPostring());
 			bUnBook.addActionListener(this);
 			bTese = new FrameTemplateButtons().RibbonJButton("Tesseract", FrameTemplateImageIcon.iconJButton_Tesseract());
 			bTese.addActionListener(this);
 			bFast = new FrameTemplateButtons().RibbonJButton("Szybkie", FrameTemplateImageIcon.iconJButton_FastPost());
 			bFast.addActionListener(this);
 			bDup=  new FrameTemplateButtons().RibbonJButton("Duplikuj", FrameTemplateImageIcon.iconJButton_Duplicate());
 			bDup.addActionListener(this);
 		rOption.add(bBook);
 		rOption.add(bUnBook);
 		rOption.add(bTese);
 		rOption.add(bFast);
 		rOption.add(bDup);
 		bPos = posFrame.getBPos();
 		bPos.addActionListener(this);
 		bSave = posFrame.getbSave();
 		posFrame.remListbSave();
		bSave.addActionListener(this);
 	}
 	
 	private DefaultTableModel getPosModel(String firma)
 	{
		StoredProcedures sp = new StoredProcedures();
		return sp.genModelPositions(firma, oType, 6, posSys, posSysAll);
 	}
 	
	private void getDict (HashMap<String, ArrayList<String>> mapsysall, JTable table) 
	{
		
		try {
			PythonBase readers = new PythonBase();
			ArrayList<ArrayList<String>> templist;
			templist = readers.FromBase(false, "IPositions_dict.yml");
			txt txtlist = new txt();
			Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
			posFrame.CommboBoxDefault(dictdata, mapsysall, table);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	 	if (e.getSource()==bPos||e.getSource()==bSave||e.getSource()==bBook||e.getSource()==bUnBook||e.getSource()==bCMDel)
	 	{
	 		columnNumber();
	 		if (posTable.getSelectedRow()>-1&&posTable.getSelectedColumn()>-1)
	 		{
		 		for (int row=posTable.getSelectedRow();row<(posTable.getSelectedRow()+posTable.getSelectedRowCount());row++)
		 		{	
		 			String tabID = posTable.getValueAt(row, columnNumber.get("ID")).toString();
		 			String tabFirm = posTable.getValueAt(row, columnNumber.get("Firma")).toString();
		 			String tabDocD= posTable.getValueAt(row, columnNumber.get("Data dokumentu")).toString();
		 			String tabDocA = posTable.getValueAt(row, columnNumber.get("Data alternatywna")).toString();
		 			String tabDocS = posTable.getValueAt(row, columnNumber.get("Data rozliczenia")).toString();
		 			String tabTXT = posTable.getValueAt(row, columnNumber.get("Opis")).toString();
		 			String tabCur = posTable.getValueAt(row, columnNumber.get("Waluta")).toString();
		 			String tabCon = posTable.getValueAt(row, columnNumber.get("Kontrahent")).toString();
		 			boolean tabBook = (boolean) posTable.getValueAt(row, columnNumber.get("Zaksiêgowane"));
		 			String tabOType  = posTable.getValueAt(row, columnNumber.get("OTYPE")).toString();
		 			
			 		if (tabID.length()==10&&
			 			tabFirm.length()==4&&
			 			tabDocD.length()==10&&
			 			tabDocA.length()==10&&
			 			tabDocS.length()==10&&
			 			tabCur.length()==3&&!tabCur.equals("   "))
			 		{
				 		if (e.getSource()==bPos)
				 		{
				 			if (oType=="MANPOS")
				 			{
				 			JFrame postings = new IPostings(500, 1000, "Ksiêgowania:" + tabID, 
				 									tabID, tabFirm, tabDocD, tabDocA, tabDocS, tabTXT,tabCur, tabOType, tabBook);
				 			postings.setVisible(true);
				 			}
				 			else if (oType=="COSINV")
				 			{
					 		JFrame postings = new IPostings(500, 1000, "Ksiêgowania:" + tabID, 
	 								tabID, tabFirm, tabDocD, tabDocA, tabDocS, tabTXT,tabCur, findContrID(tabCon) , tabOType, tabBook);
					 		postings.setVisible(true);	
				 			}
				 		}else if (e.getSource()==bSave)
				 		{
				 			JFrame postings = new IPostings(500, 1000, "Ksiêgowania:" + tabID, 
								tabID, tabFirm, tabDocD,tabCur, tabOType, tabBook, addHashMapToPostings(row));
				 			((IPostings) postings).saveButtons();
				 			JOptionPane.showMessageDialog(null, "Zapisano zmiany ID: " + tabID + " Firma: " + tabFirm, "B³¹d", JOptionPane.INFORMATION_MESSAGE);
				 		}else if (e.getSource()==bBook)
				 		{
					 		posTable.setValueAt(true, row, columnNumber.get("Zaksiêgowane"));
					 		IPostings postings = new IPostings(500, 1000, "Ksiêgowania:" + tabID, 
										tabID, tabFirm, tabDocD,tabCur, tabOType, tabBook, addHashMapToPostings(row));
					 		if (postings.getSumCurrency()==0&&postings.getSumAmount()==0&&postings.getSumQTY()==0)
					 		{
				 			postings.saveButtons();
				 			posFrame.Button_Save();
					 		}else
					 		{
					 			posTable.setValueAt(false, row, columnNumber.get("Zaksiêgowane"));
					 			JOptionPane.showMessageDialog(null, "Ksiêgowania dla ID: " + tabID + " firma: " + tabFirm + " nie bilansuj¹ siê","B³¹d", JOptionPane.ERROR_MESSAGE);
					 		}
					 		JOptionPane.showMessageDialog(null, "Zapisano zmiany ID: " + tabID + " Firma: " + tabFirm, "B³¹d", JOptionPane.INFORMATION_MESSAGE);
				 		}else if (e.getSource()==bUnBook)
				 		{
					 		
					 		posTable.setValueAt(false, row, columnNumber.get("Zaksiêgowane"));
					 		IPostings postings = new IPostings(500, 1000, "Ksiêgowania:" + tabID, 
										tabID, tabFirm, tabDocD,tabCur, tabOType, tabBook, addHashMapToPostings(row));
				 			postings.saveButtons();
				 			
				 			posFrame.Button_Save();
				 			
				 		}else if (bCMDel==e.getSource())
				 			{	HashMap<String, Integer> columnMap = posFrame.getColumnNumbers(posTable, posSysAll);
				 				if (posTable.getValueAt(row, columnMap.get("BOOK")).equals(true))
				 					{
				 						JOptionPane.showMessageDialog(null,"Nie mo¿na usun¹æ zaksiêgowanego rekordu ID:" + tabID + " Firma: " + tabFirm,"B³¹d", JOptionPane.ERROR_MESSAGE);
				 					}else if (posTable.getValueAt(posTable.getSelectedRow(), columnMap.get("BOOK")).equals(false))
				 					{
				 						var pos = new IPostings(0, 0, "usuwanie", 
				 								tabID, tabFirm, tabOType, true);
				 						pos.dispose();
				 						posFrame.removeRowInModel(posModel, posTable, row);
				 						posFrame.Button_Save();
				 						pos.dispose();
				 						if (tabID.length()>0&&tabFirm.length()>0)
				 						{
				 							new DocumentsSource().deleteSource(tabID, tabFirm);
				 						}
				 						
		 				}
		 			}
				 		
		 			}else
		 			{
		 				JOptionPane.showMessageDialog(null, (tabID.length()>0&&tabFirm.length()>0) ? "Brak wymaganych danych. ID: " + tabID + " Firma: " + tabFirm : "Brak wymaganych danych w wierszu " + row,"B³¹d", JOptionPane.ERROR_MESSAGE);
		 			}
		 		}
	 		}
	 	}else if (bTese==e.getSource())
	 	{
	 		Tesseract tesseract = new Tesseract();
	 		tesseract.mainInvoice();
	 		ITesseract iTess = new ITesseract(tesseract.getTableList(), tesseract.getDetailList(),  tesseract.getFileListStat(), oType );
	 		addFromTesseract(iTess.getPosition() ,iTess.getPostingLed(),iTess.getpostingQTY(), iTess.getFileStat());
	 	}else if (bFast==e.getSource())
	 	{
	 		ITesseract iTess = new ITesseract(new ArrayList<ArrayList<String>>(Arrays.asList(new ArrayList<String>(Arrays.asList("ID", "Lp", "Descriptions", "Qunatity", "Unit", "NetUnitPrice", "NetAmount", "VAT", "VATAmount","GrossAmount","DIMENSION",  "DIMENSION_2", "DIMENSION_3")))), 
	 																						 new ArrayList<ArrayList<String>>(Arrays.asList(new ArrayList<String>(Arrays.asList("ID", "FIRM", "DIMENSION_4", "DOCUMENTDATE", "ALTDOCUMENTDATE", "SETTLEMENTDATE", "TXT", "CURRENCYCODE", "OTYPE","ACCOUNTNUM","SUBACCOUNT"))))
	 																						, oType,
	 																						new ArrayList<ArrayList<String>>(Arrays.asList()));
	 		addFromTesseract(iTess.getPosition() ,iTess.getPostingLed(),iTess.getpostingQTY(), iTess.getFileStat());
	 	}else if (bDup==e.getSource())
	 	{
	 		if (posTable.getSelectedColumn()>-1&&posTable.getSelectedRow()>-1)
	 		{
	 			HashMap<String, Integer> colT = posFrame.getColumnNumbers(posTable, posSysAll);
	 			String id = posTable.getValueAt(posTable.getSelectedRow(), colT.get("ID")).toString();
	 			String firm = posTable.getValueAt(posTable.getSelectedRow(), colT.get("FIRM")).toString();
	 			
	 			HashMap<String, Object[]> position = new HashMap<String, Object[]>();
				SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
				Date ActDate = new Date(System.currentTimeMillis());
				String actDate = formatter.format(ActDate);
	 			Object[] newPostition = {"", firm,actDate,actDate,actDate, posTable.getValueAt(posTable.getSelectedRow(), colT.get("TXT")).toString(),
	 					posTable.getValueAt(posTable.getSelectedRow(), colT.get("CURRENCYCODE")).toString(), findContrID(posTable.getValueAt(posTable.getSelectedRow(), colT.get("CONTERPARTYID")).toString()),
	 					false, posTable.getValueAt(posTable.getSelectedRow(), colT.get("OTYPE")).toString()};
	 			position.put("1",newPostition);
	 			ArrayList<Object[]> led = new ArrayList<Object[]>();
	 			ArrayList<ArrayList<String>> listPostLed = new StoredProcedures().genUniversalArray("getLedgerTransPosting", new ArrayList<String>(Arrays.asList(id, firm)));
	 			int LPL = 0;
	 			HashMap<String, Integer> colA = posFrame.getColumnNumbers(listPostLed.get(0));
	 			for (int i =1;i<listPostLed.size();i++)
	 			{
	 				
	 				Object[] newLed = {"",
	 						firm, 
	 						++LPL, 
	 						actDate,
	 						actDate,
	 						actDate, 
	 						listPostLed.get(i).get(colA.get("TXT")),
	 						listPostLed.get(i).get(colA.get("ACCOUNTNUM")),
	 						listPostLed.get(i).get(colA.get("AMOUNTCUR")).replace(".", ","),
	 						listPostLed.get(i).get(colA.get("CREDITING")).equals("True") ? true : false,
	 						listPostLed.get(i).get(colA.get("CURRENCYCODE")),
	 						"0", "0",
	 						listPostLed.get(i).get(colA.get("DIMENSION")),
	 						listPostLed.get(i).get(colA.get("DIMENSION_2")),
	 						listPostLed.get(i).get(colA.get("DIMENSION_3")),
	 						listPostLed.get(i).get(colA.get("DIMENSION_4")),
	 						listPostLed.get(i).get(colA.get("COMENT")),
	 						"",
	 						listPostLed.get(i).get(colA.get("OTYPE")),
	 						false};
	 				led.add(newLed);
	 			}
	 			HashMap<String, ArrayList<Object[]>> postingLed = new HashMap<String, ArrayList<Object[]>>();
	 			postingLed.put("1", led);
	 			ArrayList<Object[]> qty = new ArrayList<Object[]>();
	 			ArrayList<ArrayList<String>> listPostQTY = new StoredProcedures().genUniversalArray("getLedgerQtyTransPosting", new ArrayList<String>(Arrays.asList(id, firm)));
	 			int LPQ = 0;
	 			HashMap<String, Integer> colAQ = posFrame.getColumnNumbers(listPostQTY.get(0));
	 			for (int i =1;i<listPostQTY.size();i++)
	 			{
	 				Object[] newQTY = {"", firm, ++LPL, actDate,actDate,actDate, 
	 						listPostQTY.get(i).get(colA.get("TXT")),
	 						listPostQTY.get(i).get(colA.get("ACCOUNTNUM")),
	 						listPostQTY.get(i).get(colA.get("QTYAMOUNT")).replace(".", ","),
	 						listPostQTY.get(i).get(colA.get("CREDITING")).equals("True") ? true : false,
	 						listPostQTY.get(i).get(colA.get("UNIT")),
	 						listPostQTY.get(i).get(colA.get("UNITPRICE")),
	 						listPostQTY.get(i).get(colA.get("AMOUNTMST")).replace(".", ","),
	 						listPostQTY.get(i).get(colA.get("DIMENSION")),
	 						listPostQTY.get(i).get(colA.get("DIMENSION_2")),
	 						listPostQTY.get(i).get(colA.get("DIMENSION_3")),
	 						listPostQTY.get(i).get(colA.get("DIMENSION_4")),
	 						"",
	 						listPostQTY.get(i).get(colA.get("OTYPE")),
	 						false,
	 						listPostQTY.get(i).get(colA.get("COMENT")) 
	 						
	 						
	 						};
	 				qty.add(newQTY);
	 			}
	 			HashMap<String, ArrayList<Object[]>> postingQty = new HashMap<String, ArrayList<Object[]>>();
	 			postingQty.put("1", qty);
	 			 addFromTesseract(  position, postingLed, postingQty,
				 new ArrayList<ArrayList<String>>());
	 		}	
	 	}
 
	}
	
	private void addFromTesseract(HashMap<String, Object[]> position, HashMap<String, ArrayList<Object[]>> postingLed,HashMap<String, ArrayList<Object[]>> postingQTY,
				ArrayList<ArrayList<String>> FileStat)
	{
		 
		for (String key:position.keySet())
		{
		//	posModel.addRow(position.get(key));
			autoAdd = false;
			try {
				
				String posKey = new KeyAdder().PostingID(String.valueOf(position.get(key)[2]), String.valueOf(position.get(key)[1]));
				//position.get(key)[0] = posKey;
				//posModel.addRow(position.get(key));
				position.get(key)[7] = findContrName(position.get(key)[7].toString());
				posModel.insertRow(0, position.get(key));
				posFrame.Button_Save();
				position.get(key)[0] = posModel.getValueAt(0, 0);
				for (int q=0;q<FileStat.size();q++)
				{
					if (FileStat.get(q).get(1).equals(key)&&FileStat.get(q).get(0).length()>0)
					{
						new DocumentsSource().newAddSource(position.get(key)[0].toString(),  position.get(key)[1].toString(), FileStat.get(q).get(0));
					}
				}
				//new DocumentsSource().addSource(position.get(key)[0].toString(), position.get(key)[1].toString());
				posFrame.Button_Save();
				AutoAccounting autoPosting = new AutoAccounting(String.valueOf(position.get(key)[1]), String.valueOf(position.get(key)[0]),String.valueOf(position.get(key)[9]));
				if (postingLed.containsKey(key))
				{
					autoPosting.addAutoRowsToModel(postingLed.get(key), false, false);
				}
				if (postingQTY.containsKey(key))
				{
					autoPosting.addAutoRowsToModel(postingQTY.get(key), true, false);
				}
				autoAdd = true;
				System.out.println(position.get(key));
				
			} catch (ParseException e) {
				autoAdd = true;
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	 
	}

	
	private HashMap<String, String> addHashMapToPostings(int row)
	{
		var newMap = new HashMap<String, String>();
		for (int i=0; i<posTable.getColumnCount(); i++)
		{
			//System.out.println(oType + ":" + posTable.getColumnName(i));
			if (oType.equals("COSINV") && posTable.getColumnName(i).equals("Kontrahent"))//Zareaguje tylko dla COSINV, aby uzupe³ni³ siê warunek MANPOS powinien byæ bez zmian 
			{
			newMap.put("DIMENSION_4", findContrID(posTable.getValueAt(row, i).toString()));
			}else
			{
				newMap.put(posSysAll.get(posTable.getColumnName(i)).get(0), posTable.getValueAt(row, i).toString());
			}
		}
		
		return newMap;
		
	}
	
	private String findContrID(String name)
	{
		String result ="";
		for (int i=1; i<insDictList.size();i++)
		{
			if (insDictList.get(i).get(1).equals(name))
			{
				return insDictList.get(i).get(0);
				
			}
		}
		return result;
	}
	private String findContrName(String id)
	{
		for (int i=1; i<insDictList.size();i++)
		{
			if (insDictList.get(i).get(0).equals(id))
			{
				return insDictList.get(i).get(1);
			}
		}
		
		return "";
		
	}
	
	private void columnNumber()
	{
		columnNumber = new HashMap<String, Integer>();
		for (int i = 0; i<posTable.getColumnCount();i++)
		{
			if (posTable.getColumnName(i)!="ID"&&posTable.getColumnName(i)!="FIRM"&&posTable.getColumnName(i)!="OTYPE")
			columnNumber.put(posTable.getColumnName(i), i);
		}
 
	}
	

	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		if (e.getType()==1)
		{
		newRow = e.getFirstRow();
		}
		if (e.getType()==1&&autoAdd==true)
		{
			addRow(e.getFirstRow());
		}
	}
	
	private void addRow(int firstRow)
	{
		posModel.setValueAt(posFrame.getBFirm().getText(), firstRow, 1);
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date(System.currentTimeMillis());
			posModel.setValueAt(formatter.format(date), firstRow, 2);
			posModel.setValueAt(formatter.format(date), firstRow, 3);
			posModel.setValueAt(formatter.format(date), firstRow, 4);
			posModel.setValueAt(oType, firstRow, 9);
	}



	@Override//tylko COSINV
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==posTable)
		{
			if (e.getClickCount()==2)
			{
				if (posTable.getColumnName(posTable.getSelectedColumn()).equals("Kontrahent"))
				{ 
					ArrayList<String> parameters = new ArrayList<String>();
					posFrame.getSelectionRunWithParameters(posTable, "getConterparty",parameters, "Dict_Conterparty.yml", "Projekty", 1);
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

}
