package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.AutoAccounting;
import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateButtons;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.FrameTemplateTable;
import MyLittleSmt.FrameTemplateTableNew;
import MyLittleSmt.FrameTemplateWindow;
import MyLittleSmt.KeyAdder;
import MyLittleSmt.MainEntryDataWarehouse;
import MyLittleSmt.StoredProcedures;

public class IBankStatement {
	private String date, bankAcc, firm, settId;
	private FrameTemplate ssFrame, bsFrame, maFrame;
	private HashMap<String, String> ssSys, ssSysBase, bsSys, bsSysBase;
	private HashMap<String, ArrayList<String>> ssSysAll, bsSysAll;
	private ArrayList<Integer> ssKey, bsKey;
	private DefaultTableModel ssModel,bsModel;
	private JTable ssTable, bsTable;
	private JTextField accountSum, altAccountSum, ssDate, ssID;
	private Window frame;
	public IBankStatement (String firm, String settID, String date, String bankAcc)
	{
		this.firm=firm;
		this.settId=settID;
		this.date= date;
		this.bankAcc = bankAcc;
		ssFrame = new FrameTemplate();
		bsFrame = new FrameTemplate();
		maFrame = new FrameTemplate();
		Window frame = new Window(500,600, "Wyci¹g bankowy", firm, settID);
		frame.setVisible(true);
		
	}
	public IBankStatement (boolean isUp, String firm, String settID, String date, String bankAcc)
	{
		this.firm=firm;
		this.settId=settID;
		this.date= date;
		this.bankAcc = bankAcc;
		ssFrame = new FrameTemplate();
		bsFrame = new FrameTemplate();
		maFrame = new FrameTemplate();
		frame = new Window(500,600, "Wyci¹g bankowy", firm, settID);
		 
		
	}
	
	public void addAndSaveSsModel(ArrayList<Object[]> list, boolean header)
	{
		frame.addAndSaveSsModel(list, header);
		frame.setVisible(true);
		//frame.dispose();
	}

	class Window extends FrameTemplateWindow implements ActionListener, TableModelListener, MouseListener
	{
		private JPanel rOption;
		private JButton bSave, bRec, bPos, bPosAll, bPosOne, bUnPos, bImpS, bMatch, bSShow, bSShowSec;
		private HashMap<String, Integer> ssColumnModel,  bsColumnModel, baColumnModel, bsColumnTable;
		private ArrayList<ArrayList<String>> bankAccount;
		private ArrayList<Object[]> recList;
		private HashMap<String, ArrayList<String>> firmsMap;
		private boolean notAdd = false;
		private JMenuItem postingM,  bDel, source;
		private HashMap<Integer, String> dictColumnAndYamlPos;
		private ArrayList<ArrayList<String>> insDictList;
		private boolean notAddSS =false;
		public Window(int x, int y, String title, String firm, String settID) {
			super(x, y, title);
			bankAccount = new ArrayList<ArrayList<String>>();
			ssFrame.JTableHelperSys("StatementSource_sys.yml");
			ssSys = ssFrame.getMapSys();
			ssSysBase = ssFrame.getMapSysBase();
			ssSysAll = ssFrame.getMapSysAll();
			ssModel = getSsModel(firm, settID);
			ssTable= ssFrame.getDefaultTable(ssModel, ssSysAll);
			ssKey = new ArrayList<Integer>(Arrays.asList(1,2,3));
			
			bsFrame.JTableHelperSys("BankStatement_sys.yml");
			bsSys = bsFrame.getMapSys();
			bsSysBase = bsFrame.getMapSysBase();
			bsSysAll = bsFrame.getMapSysAll();
			bsModel = getBsModel(firm, settID);
			bsTable = bsFrame.getDefaultTable(bsModel, bsSysAll);
			bsKey = new ArrayList<Integer>(Arrays.asList(0,1));
			
			bRec = new FrameTemplateButtons().smallButton("Rozliczenie", FrameTemplateImageIcon.iconJButton_smallReck());
			bRec.addActionListener(this);
			bPos = new FrameTemplateButtons().smallButton("Stwórz ksiêgowanie", FrameTemplateImageIcon.iconJButton_smallPostring());
			bPos.addActionListener(this);
			bPosOne =  new FrameTemplateButtons().smallButton("Ksiêguj",  FrameTemplateImageIcon.iconJButton_smallBook());
			bPosOne.addActionListener(this);
			bPosAll = new FrameTemplateButtons().smallButton("Ksiêguj wszystko",  FrameTemplateImageIcon.iconJButton_SmallBookAll());
			bPosAll.addActionListener(this);
			bUnPos = new FrameTemplateButtons().smallButton("Odksieguj",  FrameTemplateImageIcon.iconJButton_SmallUnAll());
			bUnPos.addActionListener(this);
			bMatch = new FrameTemplateButtons().smallButton("Dopasowanie",  FrameTemplateImageIcon.iconJButton_SmallMatch());
			bMatch.addActionListener(this);
			bSShow = new FrameTemplateButtons().smallButton("Poka¿ zród³o", FrameTemplateImageIcon.iconJButton_SmallShow());
			bSShow.addActionListener(this);
			bSShowSec = new FrameTemplateButtons().smallButton("Poka¿ zród³o", FrameTemplateImageIcon.iconJButton_SmallShow());
			bSShowSec.addActionListener(this);
			accountSum = new JTextField();
			accountSum.setPreferredSize(new Dimension(125,20));
			accountSum.setEditable(false);
			
			altAccountSum = new JTextField();
			altAccountSum.setPreferredSize(new Dimension(125,20));
			altAccountSum.setEditable(false);
			
			ssDate = new FrameTemplateButtons().getTextField();
			ssID = new FrameTemplateButtons().getTextField();
			
			JPanel upMenu = new JPanel(new BorderLayout());
				upMenu.add(maFrame.GetUpMenu(false), BorderLayout.PAGE_START);
				JTabbedPane jtp = new JTabbedPane();
					rOption = new JPanel(new FlowLayout(FlowLayout.LEFT));
					rOption.add(maFrame.DefaultRibbonSim(), BorderLayout.PAGE_END);
				jtp.add("Opcje", rOption);
				upMenu.add(jtp);
			add(upMenu, BorderLayout.PAGE_START);
			JPanel downMenu = new JPanel(new BorderLayout());
				JPanel downUp = new JPanel(new FlowLayout(FlowLayout.LEFT));
					downUp.add(new JLabel("Saldo:"));
					downUp.add(accountSum);
					downUp.add(new JLabel("Niezaksiegowane:"));
					downUp.add(altAccountSum);
				JPanel downTManu = new JPanel(new GridLayout(2,1));
					JPanel downUpManu = new JPanel(new BorderLayout());
						JPanel smallButtonsUp = ssFrame.getSmallButtons();
							smallButtonsUp.add(bMatch);
							smallButtonsUp.add(bSShowSec);
							smallButtonsUp.add(ssDate);
							smallButtonsUp.add(ssID);
						downUpManu.add(smallButtonsUp, BorderLayout.PAGE_START);
						downUpManu.add(new JScrollPane(ssTable), BorderLayout.CENTER);
					JPanel downDownMenu = new JPanel(new BorderLayout());
						JPanel smallButtons = bsFrame.getSmallButtons();
						smallButtons.add(bRec);
						smallButtons.add(bPos);
						smallButtons.add(bPosOne);
						smallButtons.add(bPosAll);
						smallButtons.add(bUnPos);
						smallButtons.add(bSShow);
						downDownMenu.add(smallButtons, BorderLayout.PAGE_START);
						downDownMenu.add(new JScrollPane(bsTable), BorderLayout.CENTER);
					downTManu.add(downUpManu);
					downTManu.add(downDownMenu);
			downMenu.add(downUp, BorderLayout.PAGE_START);
			downMenu.add(downTManu, BorderLayout.CENTER);
			add(downMenu, BorderLayout.CENTER);
			
			maFrame.getbAdd().setEnabled(false);
			bSave = maFrame.getbSave();
			bSave.addActionListener(this);
			ssModel.addTableModelListener(this);
			bsModel.addTableModelListener(this);
			ssListeners();	
			bsListeners();
			ssFrame.getsmallButtonSave().setEnabled(false);
			bsFrame.getsmallButtonSave().setEnabled(false);
			ssColumnModel = ssFrame.getColumnNumbers(ssModel, ssSysAll);
			bsColumnModel = bsFrame.getColumnNumbers(bsModel, bsSysAll);
			getBankAccount();
			baColumnModel = ssFrame.getColumnNumbers(bankAccount.get(0));
		
			recList = new ArrayList<Object[]>();
		
			firmsMap = MainEntryDataWarehouse.getFirmsMap();
			getAccountBalance(firm, bankAccount.get(1).get(baColumnModel.get("ACCOUNTNUM")) ,bankAccount.get(1).get(baColumnModel.get("CURRENCYCODE")), date );
			calculateAltsAccountSum();
			
			postingM = new JMenuItem("Ksiêgowania");
			postingM.addActionListener(this);
			source = new JMenuItem("Rozliczenie");
			source.addActionListener(this);
			bsFrame.getPopup().add(postingM);
			bsFrame.getPopup().add(source);
			bsFrame.remListmDelete();
			bDel = bsFrame.getdeleteM();
			bDel.addActionListener(this);
		}
		
		private DefaultTableModel getSsModel(String firm, String settID)
		{
			return new StoredProcedures().genUniversalModel("getStatementSource", new ArrayList<String>(Arrays.asList(firm, settID)), 20, ssSys, ssSysAll);
		}
		
		private DefaultTableModel getBsModel(String firm, String settID)
		{
			return new StoredProcedures().genUniversalModel("getBankStatement", new ArrayList<String>(Arrays.asList(firm, settID)), 19, bsSys, bsSysAll);
		}
		
		private void getBankAccount()
		{
			bankAccount = new StoredProcedures().genUniversalArray("getBankAccountById", new ArrayList<String>(Arrays.asList(firm, bankAcc )));
		}
		
		private void ssListeners()
		{
			ssFrame.addFrameTempListener(ssTable, ssModel, ssSysAll, ssSysBase, ssSys, ssKey);
			ssFrame.addListenerJTable(ssTable, ssModel);
			ssFrame.addListenerSmallButtons();
			ssFrame.addListenerContextMenu();
			ssTable.addMouseListener(this);
			ssFrame.addRowSorter();
		}
		private void bsListeners()
		{
			bsFrame.addFrameTempListener(bsTable, bsModel, bsSysAll, bsSysBase, bsSys, bsKey);
			bsFrame.addListenerJTable(bsTable, bsModel);
			bsFrame.addListenerSmallButtons();
			bsFrame.addListenerContextMenu();
			bsTable.addMouseListener(this);
			bsFrame.setMenuRun(6);
			bsFrame.setMenuRun(12);
			bsFrame.setMenuRun(13);
			bsFrame.setMenuRun(14);
			bsFrame.setMenuRun(15);
			bsFrame.setGgenKey(0,6,0);
			dictColumnAndYamlPos = new HashMap<Integer, String>();
			dictColumnAndYamlPos.put(7, "IInstruments_Dict_Model.yml");
			bsFrame.addDictToModel(dictColumnAndYamlPos, bsModel);
			insDictList = bsFrame.getOryginalDictList();
			bsFrame.addRowSorter();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource()==bSave)
			{
				bsFrame.Button_Save();
				ssFrame.Button_Save();
			}else if (e.getSource()==bRec)
			{
				notAdd = true;
				var reckoning = new IReckoning(true, firm, "XX",date,date,date,"RECSET", bankAccount.get(1).get(baColumnModel.get("CURRENCYCODE")));
				String dateP = ssDate.getText().length()>0? ssDate.getText():date;
				ArrayList<Object[]> postingList = reckoning.getPostingList();
				bsColumnTable = bsFrame.getColumnNumbers(bsTable, bsSysAll);
				for (int i=0;i<postingList.size();i++)
				{	
					
					String dim4 = "";
					for (int j=1;j<insDictList.size();j++)
					{
						
						if (postingList.get(i)[16].toString().equals(insDictList.get(j).get(0)))
						{
							dim4=insDictList.get(j).get(1);
							break;
						}
					}
					
					Object[] bsRow = {"", ssID.getText(), settId, dateP, firm, "",  postingList.get(i)[7], postingList.get(i)[8], postingList.get(i)[9], postingList.get(i)[19], postingList.get(i)[18], false,
							postingList.get(i)[13],postingList.get(i)[14],postingList.get(i)[15],dim4};
					recList.add(postingList.get(i));
					bsModel.addRow(bsRow);
				}
				calculateAltsAccountSum();
				notAdd = false;
				
			}else if (bPos==e.getSource())
			{
				if (bsTable.getSelectedRow()>-1&&bsTable.getSelectedColumn()>-1)
				{
					postingGenerate(bsTable.getSelectedRow(), false);
				}
			}else if (e.getSource()==postingM)
			{
				if (bsTable.getSelectedColumn()>-1&&bsTable.getSelectedRow()>-1)
				{
				HashMap<String, Integer> columnInsPos = bsFrame.getColumnNumbers(bsTable, bsSysAll);
				IPostings postings = new IPostings(500, 500, 
						"Ksiêgowania: " +(String) bsTable.getValueAt(bsTable.getSelectedRow(), columnInsPos.get("ID")), 
				 		(String) bsTable.getValueAt(bsTable.getSelectedRow(), columnInsPos.get("ID")),(String) bsTable.getValueAt(bsTable.getSelectedRow(), columnInsPos.get("FIRM")), 
						(String) bsTable.getValueAt(bsTable.getSelectedRow(), columnInsPos.get("OTYPE")),false);
				postings.setVisible(true);
				}
			}else if (e.getSource()==bDel||bPosOne==e.getSource()||bUnPos==e.getSource())
			{
		 		for (int row=bsTable.getSelectedRow();row<(bsTable.getSelectedRow()+bsTable.getSelectedRowCount());row++)
		 		{
		 			bsColumnTable = bsFrame.getColumnNumbers(bsTable, bsSysAll);
		 			if(e.getSource()==bDel)
		 			{
		 				
		 				if (bsTable.getValueAt(row, bsColumnTable.get("BOOK")).equals(true))
	 					{
	 						JOptionPane.showMessageDialog(null,"Nie mo¿na usun¹æ zaksiêgowanego rekordu ID:" + bsTable.getValueAt(row, bsColumnTable.get("ID")) + " Firma: " +  bsTable.getValueAt(row, bsColumnTable.get("FIRM")) ,"B³¹d", JOptionPane.ERROR_MESSAGE);
	 					}else if (bsTable.getValueAt(bsTable.getSelectedRow(), bsColumnTable.get("BOOK")).equals(false))
	 					{
	 						var pos = new IPostings(0, 0, "usuwanie", 
	 							(String) bsTable.getValueAt(row, bsColumnTable.get("ID")), 
	 							(String) bsTable.getValueAt(row, bsColumnTable.get("FIRM")), 
	 							(String) bsTable.getValueAt(row, bsColumnTable.get("OTYPE")), true);
	 						pos.dispose();
	 						bsFrame.removeRowInModel(bsModel, bsTable, row);
	 						bsFrame.Button_Save();
	 						pos.dispose();
	 						
	 					}
		 			}else if (bPosOne==e.getSource())
		 			{
							postingGenerate(row, true);
							bsTable.setValueAt(true, row,bsColumnTable.get("BOOK"));
							bsFrame.Button_Save();
							getAccountBalance((String)bsTable.getValueAt(row, bsColumnTable.get("FIRM")), 
									bankAccount.get(1).get(baColumnModel.get("ACCOUNTNUM")) ,
									bankAccount.get(1).get(baColumnModel.get("CURRENCYCODE")), date);
							calculateAltsAccountSum();
		 			}else if (bUnPos==e.getSource())
		 			{
						postingGenerate(row, false);
						bsTable.setValueAt(false, row,bsColumnTable.get("BOOK"));
						bsFrame.Button_Save();
						getAccountBalance((String)bsTable.getValueAt(row, bsColumnTable.get("FIRM")), 
								bankAccount.get(1).get(baColumnModel.get("ACCOUNTNUM")) ,
								bankAccount.get(1).get(baColumnModel.get("CURRENCYCODE")), date);
						calculateAltsAccountSum();
		 			}
		 		}
			}else if (e.getSource()==bPosAll)
			{	 
				for (int i=0; i< bsModel.getRowCount();i++)
				{
					if ((boolean)bsModel.getValueAt(i, bsColumnModel.get("BOOK"))==false)
					{
					postingGenerate(i, true);
					bsModel.setValueAt(true, i,bsColumnModel.get("BOOK"));
					bsFrame.Button_Save();
					getAccountBalance((String)bsModel.getValueAt(i, bsColumnModel.get("FIRM")), 
										bankAccount.get(1).get(baColumnModel.get("ACCOUNTNUM")) ,
										bankAccount.get(1).get(baColumnModel.get("CURRENCYCODE")), date);
					calculateAltsAccountSum();
					}
				}
				
			}else if (e.getSource()==bMatch)
			{
				bMach();
			}else if (bSShow==e.getSource())
			{
				
				if (bsTable.getSelectedColumn()>-1&&bsTable.getSelectedRow()>-1)
				{
					bsColumnTable = bsFrame.getColumnNumbers(bsTable, bsSysAll);
					ssFrame.setFilter((String)bsTable.getValueAt(bsTable.getSelectedRow(), bsColumnTable.get("ROWID")), 1);
				}
			}else if (source==e.getSource())
			{
				if (bsTable.getSelectedColumn()>-1&&bsTable.getSelectedRow()>-1)
				{
					HashMap<String, Integer> colTBS = bsFrame.getColumnNumbers(bsTable, bsSysAll);
				new sourceWindow(500,800, "Poka¿ zród³o", bsTable.getValueAt(bsTable.getSelectedRow(),colTBS.get("FIRM")).toString(),
						bsTable.getValueAt(bsTable.getSelectedRow(),colTBS.get("ACCOUNTNUM")).toString(),
						bsTable.getValueAt(bsTable.getSelectedRow(),colTBS.get("SUBACCOUNT")).toString());
				}
			}else if (e.getSource()==bSShowSec)
			{
				if (ssTable.getSelectedColumn()>-1&&ssTable.getSelectedColumn()>-1)
				{
					HashMap<String, Integer> ssColT = ssFrame.getColumnNumbers(ssTable, ssSysAll);
					bsFrame.setFilter((String) ssTable.getValueAt(ssTable.getSelectedRow(), ssColT.get("ID")), 1);
				}
			}
			
		}		
		private void bMach()
		{
		ArrayList<ArrayList<String>> recList =  new StoredProcedures().genUniversalArray("getLedgerReckoning", new ArrayList<String>(Arrays.asList(firm, bankAccount.get(1).get(baColumnModel.get("CURRENCYCODE")))));
		ArrayList<ArrayList<String>> regExList = new StoredProcedures().genUniversalArray("getBankSettingsbyImportType", new ArrayList<String>(Arrays.asList(bankAccount.get(1).get(baColumnModel.get("IMPORTTYPE")))));
		HashMap<String, Integer> recColumn = bsFrame.getColumnNumbers(recList.get(0));
		HashMap<String, Integer> regExColumn = bsFrame.getColumnNumbers(regExList.get(0));
		ArrayList<Object[]> result = new ArrayList<Object[]>();
		for (int i=0; i<ssModel.getRowCount();i++)
		{	boolean firts = false;
			if ((boolean) ssModel.getValueAt(i, ssColumnModel.get("Check_"))==false);
			{ 
				
				for (int j=0; j<regExList.size();j++)
				{
					//text 1
					boolean isMatch = true;
					if (regExList.get(j).get(regExColumn.get("TEXT1REG")).length()>1)
					{
						Pattern pat = Pattern.compile(regExList.get(j).get(regExColumn.get("TEXT1REG")));
						Matcher mat = pat.matcher((String)ssModel.getValueAt(i, ssColumnModel.get("TXT1_")));
						if (mat.find())
						{
							isMatch = true;
						} else 
						{ 
							isMatch = false;
						}
					}
					if (regExList.get(j).get(regExColumn.get("TEXT2REG")).length()>1&&isMatch==true)
					{
						Pattern pat = Pattern.compile(regExList.get(j).get(regExColumn.get("TEXT2REG")));
						Matcher mat = pat.matcher((String)ssModel.getValueAt(i, ssColumnModel.get("TXT2_")));
						if (mat.find())
						{
							isMatch = true;
						} else 
						{ 
							isMatch = false;
						}						
					}
					if (regExList.get(j).get(regExColumn.get("TEXT3REG")).length()>1&&isMatch==true)
					{
						Pattern pat = Pattern.compile(regExList.get(j).get(regExColumn.get("TEXT3REG")));
						Matcher mat = pat.matcher((String)ssModel.getValueAt(i, ssColumnModel.get("TXT3_")));
						if (mat.find())
						{
							isMatch = true;
						} else 
						{ 
							isMatch = false;
						}	
					}
					if (regExList.get(j).get(regExColumn.get("TEXT4REG")).length()>1&&isMatch==true)
					{
						Pattern pat = Pattern.compile(regExList.get(j).get(regExColumn.get("TEXT4REG")));
						Matcher mat = pat.matcher((String)ssModel.getValueAt(i, ssColumnModel.get("BankAccount")));
						if (mat.find())
						{
							isMatch = true;
						} else 
						{ 
							isMatch = false;
						}
					}
					if (isMatch == true)
					{
						double value = Double.valueOf((String.valueOf(ssModel.getValueAt(i, ssColumnModel.get("AMOUNTCUR"))).replace(",", ".")));
						double valueABS = Math.abs(value);
						String valueS = String.format("%.2f",Double.valueOf(valueABS));
						boolean crediting = value>0 ? true : false;
						String idContr = regExList.get(j).get(regExColumn.get("DIMENSION_4"));
						String dim4 ="";
						if (idContr.length()>1)
						{
						for (int k=0;k<insDictList.size();k++)
						{
							if (idContr.equals(insDictList.get(k).get(0)))
							{
								dim4=insDictList.get(k).get(1);
							}
						}
						}
						Object[] temp = {"", (String)ssModel.getValueAt(i, ssColumnModel.get("ID")), (String)ssModel.getValueAt(i, ssColumnModel.get("STATEMENTID"))
								, String.valueOf(ssModel.getValueAt(i, ssColumnModel.get("DOCUMENTDATE"))), (String)ssModel.getValueAt(i, ssColumnModel.get("FIRM")), "",
								regExList.get(j).get(regExColumn.get("ACCOUNTNUM")),valueS, crediting, "AUTSET", "", false,  
								regExList.get(j).get(regExColumn.get("DIMENSION")),regExList.get(j).get(regExColumn.get("DIMENSION_2")),regExList.get(j).get(regExColumn.get("DIMENSION_3")),
								dim4};
						result.add(temp);
						ssModel.setValueAt(true, i, ssColumnModel.get("Check_"));
						firts = true;
						break;
					}
				}
			}
			
			
			
			//dopasowanie kwot
			if ((boolean)ssModel.getValueAt(i, ssColumnModel.get("Check_"))==false)
			{ 
				double statVal =  Double.valueOf(String.valueOf(ssModel.getValueAt(i,  ssColumnModel.get("AMOUNTCUR"))).replace(",", ".")) ;
				for (int j=1; j<recList.size();j++)
				{
				
					 
					double recValue = FrameTemplate.round(Double.valueOf(recList.get(j).get(recColumn.get("AMOUNTCUR"))) * (recList.get(j).get(recColumn.get("CREDITING")).equals("True") ? -1 : 1),2) ;
					if (recValue==statVal&&recValue!=0.00)
					{
						double value = Double.valueOf(recList.get(j).get(recColumn.get("AMOUNTCUR")).replace(",", "."));
						String valueS = String.format("%.2f",Double.valueOf(value));
						String idContr = recList.get(j).get(recColumn.get("DIMENSION_4"));
						String dim4 ="";
						if (idContr.length()>1)
						{
						for (int k=0;k<insDictList.size();k++)
						{
							if (idContr.equals(insDictList.get(k).get(0)))
							{
								dim4=insDictList.get(k).get(1);
							}
						}
						}
						Object[] temp = {"", (String)ssModel.getValueAt(i, ssColumnModel.get("ID")), (String)ssModel.getValueAt(i, ssColumnModel.get("STATEMENTID"))
								, String.valueOf(ssModel.getValueAt(i, ssColumnModel.get("DOCUMENTDATE"))), (String)ssModel.getValueAt(i, ssColumnModel.get("FIRM")), "",
								recList.get(j).get(recColumn.get("Account")), valueS,
								(recList.get(j).get(recColumn.get("CREDITING")).equals("True") ? false : true), "RECSET" ,recList.get(j).get(recColumn.get("SUBACCOUNT")), false
								,recList.get(j).get(recColumn.get("DIMENSION")),recList.get(j).get(recColumn.get("DIMENSION_2")),recList.get(j).get(recColumn.get("DIMENSION_3"))
								,dim4};
						result.add(temp);
						recList.get(j).set(recColumn.get("AMOUNTCUR"), "0.00");
						ssModel.setValueAt(true, i, ssColumnModel.get("Check_"));
						break;
					}
				}
			}
		}
		for (int i =0; i<result.size();i++)
		{
			notAdd= true;
			bsModel.addRow(result.get(i));
			notAdd = false;
		}
		}
		
		

		@Override
		public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource()==ssModel&&e.getType()==1)
			{
				addRowSsModel(e.getFirstRow());
				
			}
			else if (e.getSource()==bsModel&&e.getType()==1)
			{
				addRowBsModel(e.getFirstRow());
			}
			else if (e.getSource()==bsModel&&e.getType()==0&&(e.getColumn()==bsColumnModel.get("AMOUNTCUR")||e.getColumn()==bsColumnModel.get("CREDITING")))
			{
				calculateAltsAccountSum();
			}
		}
		
		private void addRowSsModel(int row)
		{
			if (notAdd == false)
			{
			 try {
				
				HashMap<String, Integer> columnArray = ssFrame.getColumnNumbers(bankAccount.get(0));
				if  (notAddSS==false)
				{
				ssModel.setValueAt(date, row, ssColumnModel.get("DOCUMENTDATE"));
				ssModel.setValueAt(firm, row, ssColumnModel.get("FIRM"));
				ssModel.setValueAt(date, row, ssColumnModel.get("SETTLEMENTDATE"));
				ssModel.setValueAt(settId, row, ssColumnModel.get("STATEMENTID"));
				ssModel.setValueAt(bankAcc, row, ssColumnModel.get("BankAccount"));
				}
				ssModel.setValueAt(new KeyAdder().SourceRowStatementID(date,firm), row,ssColumnModel.get("ID"));
			 } catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		
		private void addRowBsModel(int row)
		{ 
			if (notAdd == false)
			{
			bsModel.setValueAt(settId, row, bsColumnModel.get("STATEMENTID"));
			bsModel.setValueAt(date, row, bsColumnModel.get("DOCUMENTDATE"));
			bsModel.setValueAt(firm, row, bsColumnModel.get("FIRM"));
			bsModel.setValueAt("MANSET", row, bsColumnModel.get("OTYPE"));
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
			if (e.getClickCount()==2&&e.getSource()==bsTable)
			{
				bsColumnTable = bsFrame.getColumnNumbers(bsTable, bsSysAll);
				if (bsTable.getSelectedColumn()==bsColumnTable.get("ACCOUNTNUM"))
				{
					bsTable.setAutoCreateRowSorter(false);
					bsFrame.getSelectionRunWithParameters(bsTable,"getAccountToPostings",new ArrayList<String>(Arrays.asList("1",firmsMap.get(firm).get(1))), "Dict_PlanAccount_sys.yml", "Konto", 0);
					bsTable.setAutoCreateRowSorter(true);
					
				}else if (bsTable.getSelectedColumn()==bsColumnTable.get("DIMENSION"))
				{
					bsFrame.dictInstrumentsbyFirm(bsTable, firm);
				}else if (bsTable.getSelectedColumn()==bsColumnTable.get("DIMENSION_2"))
				{
					 
					bsFrame.getSelectionRunWithParameters(bsTable, "getDomainValue",new ArrayList<String>(Arrays.asList("Dimension_2.Type")), "Dict_DomainValue.yml", "Typ", 0);
				}else if (bsTable.getSelectedColumn()==bsColumnTable.get("DIMENSION_3"))
				{
					bsFrame.getSelectionRunWithParameters(bsTable, "getDomainValue",new ArrayList<String>(Arrays.asList("Dimension_3.InternalContractor")), "Dict_DomainValue.yml", "Kontrahent wewnêtrzny", 0);
				
				}else if (bsTable.getSelectedColumn()==bsColumnTable.get("DIMENSION_4"))
				{
					bsFrame.dictCounterpartyNameFirm(bsTable);
				}
			}else if (e.getSource()==ssTable&&ssTable.getSelectedColumn()>=0&&ssTable.getSelectedRow()>=0)
			{
				HashMap<String, Integer> ssColT = ssFrame.getColumnNumbers(ssTable, ssSysAll);
				ssDate.setText(String.valueOf(ssTable.getValueAt(ssTable.getSelectedRow(), ssColT.get("DOCUMENTDATE"))));
				ssID.setText((String)ssTable.getValueAt(ssTable.getSelectedRow(), ssColT.get("ID")));
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		private void getAccountBalance(String firm, String acc, String curr, String date)
		{
			double newValue=Double.valueOf(new StoredProcedures().genUniversalArray("getAccountBallance", new ArrayList<String>(Arrays.asList(firm, acc , curr,  date ))).get(1).get(0).replace(",","."));
			accountSum.setText(String.format("%.2f",Double.valueOf(newValue)));
		}
		
		private void calculateAltsAccountSum()
		{
			double accountSumAlt =Double.valueOf(accountSum.getText().replace(",", "."));
			for (int i=0;i<bsModel.getRowCount();i++)
			{
				if ((boolean)bsModel.getValueAt(i, bsColumnModel.get("BOOK"))!=true)
				{
					if ((boolean)bsModel.getValueAt(i, bsColumnModel.get("CREDITING")))
					{
						accountSumAlt += Double.valueOf(bsModel.getValueAt(i, bsColumnModel.get("AMOUNTCUR")).toString().replace(",", "."));
					}else
					{
						accountSumAlt -= Double.valueOf(bsModel.getValueAt(i, bsColumnModel.get("AMOUNTCUR")).toString().replace(",", "."));
					}
				}
			}
			altAccountSum.setText(String.format("%.2f",Double.valueOf(accountSumAlt)));
		}
		
		private void postingGenerate(int row, boolean book)
		{
			bsColumnTable = bsFrame.getColumnNumbers(bsTable, bsSysAll);
			
			if(bsTable.getValueAt(row, bsColumnTable.get("ID")).toString().length()<10)
			{
				bsFrame.Button_Save();
			}
			String instr = bsTable.getValueAt(row, bsColumnTable.get("DIMENSION")).toString();
			String subPos = bsTable.getValueAt(row, bsColumnTable.get("SUBACCOUNT")).toString();
			String oType = bsTable.getValueAt(row, bsColumnTable.get("OTYPE")).toString();
			String dim2 = bsTable.getValueAt(row, bsColumnTable.get("DIMENSION_2")).toString();
			String dim3 = bsTable.getValueAt(row, bsColumnTable.get("DIMENSION_3")).toString();
			String dim4 = "";
			String accountnumB = bankAccount.get(1).get(baColumnModel.get("ACCOUNTNUM"));
			String docDate = bsTable.getValueAt(row, bsColumnTable.get("DOCUMENTDATE")).toString();
			String txt = bsTable.getValueAt(row, bsColumnTable.get("TXT")).toString();
			String currency = bankAccount.get(1).get(baColumnModel.get("CURRENCYCODE"));
			boolean ctB = ((boolean) bsTable.getValueAt(row, bsColumnTable.get("CREDITING"))==true? false : true);
			boolean ct =(boolean) bsTable.getValueAt(row, bsColumnTable.get("CREDITING"));
			
			for (int i=0;i<insDictList.size();i++)
			{
				if (bsTable.getValueAt(row, bsColumnTable.get("DIMENSION_4")).toString().equals(insDictList.get(i).get(1)))
				{
					dim4=insDictList.get(i).get(0);
				}
			}
			
		//	if(bsModel.getValueAt(row, bsColumnTable.get("SUBACCOUNT")).toString().length()>1&&bsModel.getValueAt(row, bsColumnTable.get("OTYPE")).equals("RECSET"))
		//	{
		//		for (int i=0;i<recList.size();i++)
		//		{
		//			
		//			if ((recList.get(i)[18].toString()).equals(subPos))
		//			{
		//				instr =(String) recList.get(i)[13];
		//				dim2 =(String) recList.get(i)[14];
		//				dim3 =(String) recList.get(i)[15];
		//				dim4 =(String) recList.get(i)[16];
		//			}
		//		}
		//	}
			
			
			AutoAccounting posting = new AutoAccounting(firm, bsTable.getValueAt(row, bsColumnTable.get("ID")).toString(), instr, oType);
			Object[] argList = posting.getPostingList(false);
			ArrayList<Object[]> postingList = new ArrayList<Object[]>();
			postingList.add(argList);
			String subbAccount = bsTable.getValueAt(row, bsColumnTable.get("SUBACCOUNT")).toString().length()>1 ? bsTable.getValueAt(row, bsColumnTable.get("SUBACCOUNT")).toString(): bsTable.getValueAt(row, bsColumnTable.get("ID")).toString();
			
			
			for (int i=0; i<2; i++)
			{
				Object[] newRow = {bsTable.getValueAt(row, bsColumnTable.get("ID")).toString(), bsTable.getValueAt(row, bsColumnTable.get("FIRM")).toString(), 
									postingList.size(),docDate, docDate, docDate, txt, (i==0 ?accountnumB: bsTable.getValueAt(row, bsColumnTable.get("ACCOUNTNUM")).toString() ),
									bsTable.getValueAt(row, bsColumnTable.get("AMOUNTCUR")).toString() ,(i==0 ? ctB: ct), currency,"0","0",instr,dim2,dim3,dim4, "", 
									subbAccount, oType, book };	
				postingList.add(newRow);
			}
			posting.addAutoRowsToModel(postingList, false);
			
		}
		
		void addAndSaveSsModel(ArrayList<Object[]> list, boolean header)
		{
			notAddSS = true;
			int head = (header==true? 1:0);
			for (int i=head; i<list.size();i++)
			{
				ssModel.addRow(list.get(i));
			}
			notAddSS = false;
			ssFrame.Button_Save();
		}
		
		class sourceWindow extends FrameTemplateWindow
		{
			private cTable cTab;
			public sourceWindow(int x, int y, String title, String firm, String acc, String subAcc) {
				super(x, y, title);
				cTab = new cTable("IPosting_Ledger_sys.yml", "getLedgerAccBySubAcc", new ArrayList<String>(Arrays.asList(firm, acc, subAcc)), 1 , false);
				// TODO Auto-generated constructor stub
				add(cTab.getFrame().GetUpMenu(false), BorderLayout.PAGE_START);
				add(cTab.getPanel(), BorderLayout.CENTER);
				pack();
				setVisible(true);
				setExtendedState(JFrame.MAXIMIZED_BOTH);
			
			}
			class cTable extends FrameTemplateTableNew
			{

				public cTable(String sysYaml, String procedure, ArrayList<String> procParameters, int enableType, boolean check) {
					super(sysYaml, procedure, procParameters, enableType, check);
					// TODO Auto-generated constructor stub
				}
				JPanel getPanel()
				{
					JPanel panel = new JPanel(new BorderLayout());
						panel.add(new JScrollPane(table), BorderLayout.CENTER);
						frame.sourceContextMenu();
						addListeners(new ArrayList<Integer>(Arrays.asList(0,1,2)));
						frame.setkeyCol(0);
					return panel;		
				}
			}
		}
	
	}
	
}
