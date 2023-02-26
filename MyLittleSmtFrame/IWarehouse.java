package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateButtons;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.FrameTemplateTableNew;
import MyLittleSmt.FrameTemplateWindow;
import MyLittleSmt.KeyAdder;
import MyLittleSmt.MainEntryDataWarehouse;
import MyLittleSmt.StoredProcedures;

public class IWarehouse {
	private String firm;
	private Window window;
	private HashMap<String, ArrayList<String>> firmsMap; 

	public IWarehouse(String firm)
	{
		this.firm = firm;
		firmsMap = MainEntryDataWarehouse.getFirmsMap();
		if (firmsMap.containsKey(firm))
		{
			Window window = new Window(500, 500, "Magazyn");
			window.setExtendedState(JFrame.MAXIMIZED_BOTH);
			window.setVisible(true);
		}else 
		{
			JOptionPane.showMessageDialog(null, "Nierozpoznanan firma" , "Informacja", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	class Window extends FrameTemplateWindow implements ActionListener, TableModelListener
	{
		private HashMap<String, Integer> cosColM, ordColM, posColM;
		private HashMap<String, String> cosSys, ordSys, posSys, cosSysBase, ordSysBase, posSysBase, sys, sysBase;
		private HashMap<String, ArrayList<String>> cosSysAll, ordSysAll, posSysAll, sysAll;
		private FrameTemplate cosFrame, posFrame, ordFrame, sysFrame;
		private DefaultTableModel cosModel, posModel, ordModel;
		private JTable cosTable, posTable, ordTable;
		private HashMap<String, HashMap<String, String>> accountMap;
		private JButton dimension, bMatch, bMatchS,  bAddB;
		private Table pos, cos, ord;
		private JTextField ct, dt, sum, posID, docDate;
		private postingTab posTab;
		private String minDate, actDate;
		private boolean dateCh;
		
		public Window(int x, int y, String title) {
			super(x, y, title);
			// TODO Auto-generated constructor stub
			sysFrame = new FrameTemplate();
			dimension = new JButton("");
			dimension.setPreferredSize(new Dimension(125,20));
			dimension.addActionListener(this);
			bAddB = new JButton("Dodaj");
			bAddB.setPreferredSize(new Dimension(125,20));
			bAddB.addActionListener(this);
			accountMap = new FrameTemplate().getAccountMap();
			ord = new Table("IWarehouse_QTY_sys.yml" , "getWarehouse", new ArrayList<String>(Arrays.asList(firm, 
																					"NEWORD", "NEWORD","WARORD",  dimension.getText() 
																					,accountMap.get(firmsMap.get(firm).get(1)).get("goodsForPurchase") )), 24, true);
			ordTable = ord.getTable();
			
			cos = new Table("IWarehouse_QTY_sys.yml" , "getWarehouse", new ArrayList<String>(Arrays.asList(firm, 
																					"MANPOS", "COSINV","WARCOS",  dimension.getText() 
																					,accountMap.get(firmsMap.get(firm).get(1)).get("goodsForPurchase"))), 24, true);
			cosTable = cos.getTable();
			
		//	pos = new Table("IPosting_QTY_sys.yml" , " getLedgerQtyTransPostinWarHou", new ArrayList<String>(Arrays.asList( dimension.getText() , firm)), 1, false);
		//	posTable = pos.getTable();
			posTab=new postingTab("IPosting_QTY_sys.yml" , " getLedgerQtyTransPostinWarHou", new ArrayList<String>(Arrays.asList( dimension.getText() , firm)), 1, false);
			//posColM = pos.getColM();
			
			//	posTable = posTab.getTable();
			
			
			
			//posFrame = pos.getFrame();
			ordFrame = ord.getFrame();
			cosFrame = cos.getFrame();
			
			ordColM = ord.getColM();
			cosColM = cos.getColM();
			setModel();
			//posModel = pos.getModel();
			//ordModel = ord.getModel();
			//cosModel = cos.getModel();
			//posModel = pos.getModel();
			ordModel = ord.getModel();
			cosModel = cos.getModel();
			bMatch= new FrameTemplateButtons().smallButton("Dopasowanie po kwotach", FrameTemplateImageIcon.iconJButton_SmallMatchSec());
			bMatchS= new FrameTemplateButtons().smallButton("Dopasowanie po nazwach", FrameTemplateImageIcon.iconJButton_SmallMatchThr());

			bMatchS.addActionListener(this);
			bMatch.addActionListener(this);
		
			ct = new FrameTemplateButtons().getTextField();
			dt = new FrameTemplateButtons().getTextField();
			sum = new FrameTemplateButtons().getTextField();
			docDate= new FrameTemplateButtons().getTextField();
			
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
			Date ActDate = new Date(System.currentTimeMillis());
			minDate = formatter.format(ActDate);
			actDate =formatter.format(ActDate);
			docDate.setText(minDate);
			docDate.setEnabled(true);
			docDate.getDocument().addDocumentListener(new checkDate());
			posID= new FrameTemplateButtons().getTextField();
			dateCh = true;
			JPanel upPanel = new JPanel(new BorderLayout());
				upPanel.add(sysFrame.GetUpMenu(false), BorderLayout.PAGE_START);
				JPanel downUpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
					downUpPanel.add(new JLabel("Projekt:"));
					downUpPanel.add(dimension);
					downUpPanel.add(bMatch);
					downUpPanel.add(bMatchS);
					downUpPanel.add(docDate);
				upPanel.add(downUpPanel, BorderLayout.PAGE_END);
			JPanel mainPanel = new JPanel(new GridLayout(2,1));
				JPanel central = new JPanel(new BorderLayout());
				JPanel centralPanel = new JPanel (new GridLayout(1,2));
					JPanel centralLeft = new JPanel(new BorderLayout());
						JPanel upCentralLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
							upCentralLeft.add(ordFrame.getSmallButtons());
						centralLeft.add(upCentralLeft, BorderLayout.PAGE_START);
						centralLeft.add(new JScrollPane(ordTable), BorderLayout.CENTER);

					centralPanel.add(centralLeft);
					JPanel centralRight = new JPanel(new BorderLayout());
						JPanel upCentralRight = new JPanel(new FlowLayout(FlowLayout.LEFT));
							upCentralRight.add(cosFrame.getSmallButtons());
						centralRight.add(upCentralRight, BorderLayout.PAGE_START);
						centralRight.add(new JScrollPane(cosTable), BorderLayout.CENTER);
					centralPanel.add(centralRight);
				JPanel centralDown = new JPanel(new FlowLayout(FlowLayout.LEFT));
					centralDown.add(new JLabel("Dt:"));
					centralDown.add(dt);
					centralDown.add(new JLabel("Ct:"));
					centralDown.add(ct);
					centralDown.add(new JLabel("Suma:"));
					centralDown.add(sum);
					centralDown.add(bAddB);
				central.add(centralPanel, BorderLayout.CENTER);
				central.add(centralDown, BorderLayout.PAGE_END);

				mainPanel.add(central);
				mainPanel.add(posTab.getPanel());
				add(upPanel, BorderLayout.PAGE_START);
				add(mainPanel, BorderLayout.CENTER);
			//	add(downPanel, BorderLayout.PAGE_END);
			diActive(ordFrame);
			//diActive(posFrame);
			//posFrame.getsmallButtonSave().setEnabled(true);
			//posFrame.getdeleteM().setEnabled(true);
			diActive(cosFrame);
			listeners();
			addTableListeners();
			
		}
		
		private void setModel()
		{
		//	posModel = pos.getModel();
			ordModel = ord.getModel();
			cosModel = cos.getModel();
		}

		private void listeners()
		{
			cosModel.addTableModelListener(this);
			ordModel.addTableModelListener(this);
	//		posModel.addTableModelListener(this);
			
		}
		private void addTableListeners()
		{
			ord.addListeners(new ArrayList<Integer>(Arrays.asList(1)));
			cos.addListeners(new ArrayList<Integer>(Arrays.asList(1)));
			//pos.addListeners(new ArrayList<Integer>(Arrays.asList(0,1,2)));
			
			cosFrame.addListenerSmallButtons();
		///	posFrame.addListenerSmallButtons();
			ordFrame.addListenerSmallButtons();
		}
	 
		private void diActive(FrameTemplate frameDi)
		{
			frameDi.getsmallButtonAdd().setEnabled(false);
			frameDi.getsmallButtonSave().setEnabled(false);
			frameDi.getSmallButtonDoc().setEnabled(false);
			frameDi.getSmallButtonImport().setEnabled(false);
			frameDi.getaddRowM().setEnabled(false);
			frameDi.getdeleteM().setEnabled(false);
			frameDi.getpasteM().setEnabled(false);
			
		}
		
		class Table
		{
			private FrameTemplate frame;
			private HashMap<String, String> sys, sysBase;
			private HashMap<String, ArrayList<String>> sysAll;
			public DefaultTableModel model;
			private JTable table;
			private String modelProcedure;
			private int  enableType;
			private boolean isCheck;
			ArrayList<ArrayList<String>> cleanData; 
			private HashMap<String, Integer> colM;
			
			
			public Table(String yamlSys, String modelProcedure, ArrayList<String> modelParameters, int enableType,boolean isCheck)
			{
				this.enableType= enableType;
				this.modelProcedure = modelProcedure;
				this.isCheck = isCheck;
				frame = new FrameTemplate();
				if (isCheck==true)
				{
					frame.JTableHelperSys(yamlSys, true);
				}else
				{
					frame.JTableHelperSys(yamlSys);
				}
				sys = frame.getMapSys();
				sysBase = frame.getMapSysBase();
				sysAll = frame.getMapSysAll();
				model =  isCheck==true ?  getModel(modelParameters, isCheck): getModel(modelParameters);
				table =frame.getDefaultTable(model, sysAll);
				colM = frame.getColumnNumbers(model, sysAll);
			}
			
		    protected HashMap<String, Integer> getColM()
		    {
		    	return colM;
		    }
			protected FrameTemplate getFrame()
			{
				return frame;
			}
			
			protected JTable getTable()
			{
				return table;
			}
			
			protected DefaultTableModel getModel()
			{
				return model;
			}
			protected HashMap<String, ArrayList<String>> getSysAll()
			{
				return sysAll;
			}
			private DefaultTableModel getModel(ArrayList<String> modelParameters)
			{
				var sp = new StoredProcedures();
				DefaultTableModel model = sp.genUniversalModel(modelProcedure, modelParameters, enableType, sys, sysAll);
				cleanData = sp.getListFromBase();
				return model;
	
			}
			
			private DefaultTableModel getModel(ArrayList<String> modelParameters, boolean isCheck)
			{
				var sp = new StoredProcedures();
				DefaultTableModel model = sp.genUniversalModel(isCheck, modelProcedure, modelParameters, enableType, sys, sysAll);
				cleanData = sp.getListFromBase();
				return model;
	
			}
			
			
			protected void newModel(ArrayList<String> modelParameters)
			{
				model =  isCheck==true ?  getModel(modelParameters, isCheck): getModel(modelParameters);
				frame.setModel(model);
				table.setModel(model);
			}
			
			public void addRow(ArrayList<Object[]> list)
			{
				for (int i = 0; i<list.size();i++)
				{
					model.addRow(list.get(i));
					frame.Button_Save();
				}
			}
			
			public void addListeners(ArrayList<Integer> keys)
			{
				frame.addFrameTempListener(table, model, sysAll, sysBase, sys, keys);
				frame.addListenerJTable(table, model);
				frame.addListenerContextMenu();
			}
			
			public void setGenKey(int x, int y,int z)
			{
				posFrame.setGgenKey(x,y,z);
			}
			
		 
		}


		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource()==dimension)
			{	
				dateCh = false;
				String oldValue = dimension.getText();
				ISelectionRun jdialog = new ISelectionRun(oldValue , "Dict_InstrumentsWarH_sys.yml", " getInstrumentToWarehouse ", new ArrayList<String>(Arrays.asList(firm)), "Projekty", 0);
				ArrayList<ArrayList<String>> ListFromBase = jdialog.getTableList();
				dimension.setText(jdialog.getSelection());
				cos.newModel( new ArrayList<String>(Arrays.asList(firm, "MANPOS", "COSINV","WARCOS", dimension.getText()
																,accountMap.get(firmsMap.get(firm).get(1)).get("goodsForPurchase"))));
				ord.newModel(new ArrayList<String>(Arrays.asList(firm, "NEWORD", "NEWORD","WARORD", dimension.getText() 
																,accountMap.get(firmsMap.get(firm).get(1)).get("goodsForPurchase") )));
				//pos.newModel(new ArrayList<String>(Arrays.asList( dimension.getText(), firm)));
				posTab.newModelP(new ArrayList<String>(Arrays.asList( dimension.getText(), firm)));
				setModel();

				listeners();
				addTableListeners();
				getUpSum();
				getPostingDate();
				dateCh = true;
				
			}else if (e.getSource()==bAddB)
			{
				genPosting();
					
				 
		 
			
			}else if (e.getSource()==bMatch)
			{
				machAmmount();
			}else if (e.getSource()==bMatchS)
			{
				
			}
		}
		
		private void getPostingDate()
		{
			ArrayList<ArrayList<String>> date = new StoredProcedures().genUniversalArray("getMaxWarDate", new ArrayList<String>(Arrays.asList(firm, dimension.getText())));
			minDate =date.get(1).get(0);
			docDate.setText(minDate);
		}
		
		//jeœli mamy równ¹ iloœæ i wartoœæ ofery stanowi od 75% do 95%
		private void machAmmount()
		{
			for (int i=0;i<ordModel.getRowCount();i++)
			{
				if ((boolean) ordModel.getValueAt(i, ordColM.get("Check"))==false)
				{
					double qtyOrd = Double.valueOf(ordModel.getValueAt(i, ordColM.get("QTYAMOUNT")).toString().replace(",", "."));
					double amoOrd = Double.valueOf(ordModel.getValueAt(i, ordColM.get("UNITPRICE")).toString().replace(",", "."));
					for (int q=0;q<cosModel.getRowCount();q++)
					{
						if ((boolean) cosModel.getValueAt(q, cosColM.get("Check"))==false)
						{
							double qtyCos =FrameTemplate.round(Double.valueOf(cosModel.getValueAt(q, cosColM.get("QTYAMOUNT")).toString().replace(",", ".")) *-1,2);
							double amoCos = Double.valueOf(cosModel.getValueAt(q, cosColM.get("UNITPRICE")).toString().replace(",", "."));
							double prof = FrameTemplate.round(((amoCos/amoOrd)*100),2);
							if (qtyCos!=0&&qtyCos==qtyOrd&&(prof>75&&prof<90))
							{
							//	 pos.addRow(postGen(i, q));
								posTab.addRowList(postGen(i, q));
								ordModel.setValueAt(true, i, ordColM.get("Check"));
								cosModel.setValueAt(true, q, cosColM.get("Check"));
								break;
							}
						} 
					}
					
				}
			}
		}
		
		private void refreshQty(DefaultTableModel model, HashMap<String, Integer> colM, int row)
		{
		 
				if (row>=0&&(boolean)model.getValueAt(row,colM.get("Check"))==true)
				{
					double value =FrameTemplate.round(Double.valueOf(model.getValueAt(row, colM.get("QTYAMOUNTSEC")).toString().replace(",", "."))-Double.valueOf(model.getValueAt(row, colM.get("QTYAMOUNT")).toString().replace(",", ".")),2);
					model.setValueAt(String.format("%.2f",value), row,  colM.get("QTYAMOUNT"));
					model.setValueAt(false, row,colM.get("Check"));
				}
			 
		}
		
		private void genPosting()
		{
			ArrayList<Integer> dt = genPostingHelp(ordModel);
			ArrayList<Integer> ct = genPostingHelp(cosModel);
			if ((dt.size()==1&&ct.size()>=0)||(dt.size()==0&&ct.size()>=1))
			{
				 
			 posTab.addRow( postGen(dt.size()==0? -1 : dt.get(0), ct.size()==0? -1 : ct.get(0)));
			//	posFrame.Button_Save();
			}else
			{
				JOptionPane.showMessageDialog(null, "U¿ywaj po³¹czenia jeden do jednego" , "Informacja", JOptionPane.INFORMATION_MESSAGE);
			}
			
		}
		
		private ArrayList<Object[]> postGen(int colOrd, int colCos)
		{
			HashMap<String, Integer> cosColT = cosFrame.getColumnNumbers(cosTable, cos.getSysAll());
			HashMap<String, Integer> ordColT = ordFrame.getColumnNumbers(ordTable, ord.getSysAll());			
			
			double ctD= FrameTemplate.round(Double.valueOf(ct.getText().replace(",", "."))*-1,2);
			double dtD= Double.valueOf(dt.getText().replace(",", "."));
			double restD = FrameTemplate.round(dtD+ctD, 2);
			HashMap<String, ArrayList<Object[]>> map = new HashMap<String, ArrayList<Object[]>>();
			int lp = posTab.getModel().getRowCount();
			///istotana kolejnoœæ dodawania->
			Object[] dtO = {dtD,++lp};
			ArrayList<Object[]> temp = new ArrayList<Object[]>();
			temp.add(dtO);
			map.put("D",temp); 
			temp = new ArrayList<Object[]>();
			for (int i=0; i<cosModel.getRowCount();i++)
			{
				if ((boolean)cosModel.getValueAt(i, cosColM.get("Check"))==true)
				{
					
					Object[] ctO = {Double.valueOf(cosModel.getValueAt(i, cosColM.get("QTYAMOUNT")).toString().replace(",", ".")),++lp,i};
					
					if (map.containsKey("C"))
					{
						map.get("C").add(ctO);
					}else 
					{
						temp.add(ctO);
						map.put("C", temp);
					}
				}
				 
			}
 
			Object[] rtO ={restD,++lp};
			temp = new ArrayList<Object[]>();
			temp.add(rtO);
			map.put("R", temp);
	
			ArrayList<Object[]> list = new ArrayList<Object[]>();
			if (posID.getText().trim().equals(""))
			{
				try {
					posID.setText(new KeyAdder().PostingID(docDate.getText(), firm));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			for (String key : map.keySet())
			{
				for (int i=0;i<map.get(key).size();i++)
				{
				if ((double) map.get(key).get(i)[0]!=0)
					{
					String subAcc ="";
					String oType ="";
					if (key.equals("C"))
					{
						subAcc= cosTable.getValueAt( (int)map.get(key).get(i)[2], cosColT.get("SUBACCOUNT")).toString();
						 oType ="WARCOS";
					}else if (key.equals("D"))
					{
						subAcc=ordTable.getValueAt(colOrd, ordColT.get("SUBACCOUNT")).toString();
						 oType ="WARORD";
					}
					else if (key.equals("R"))
					{
						subAcc=posID.getText();
						 oType ="WARRES";
					}
						 
					int cosRow = key.equals("C") ?(int)map.get(key).get(i)[2] : colCos; 
						 
						
					Object[] podting = {posID.getText().toString(), firm,(int) map.get(key).get(i)[1], docDate.getText(),docDate.getText(),docDate.getText(), 
							(double) map.get(key).get(i)[0]<0 ? String.valueOf(map.get("D").get(0)[1]) :cosRow<0?ordTable.getValueAt(colOrd, ordColT.get("COMENT")): cosTable.getValueAt(cosRow, cosColT.get("COMENT")),
						 
							accountMap.get(firmsMap.get(firm).get(1)).get("goodsForPurchase").trim(),
							(double) map.get(key).get(i)[0]<0 ? String.format("%.2f",FrameTemplate.round((double)map.get(key).get(i)[0]*-1, 2)) : String.format("%.2f",(double) map.get(key).get(i)[0], 2) ,
							(double) map.get(key).get(i)[0]>0 ?key.equals("R") ? false : true :key.equals("R") ? true : false,	
							(double) map.get(key).get(i)[0]>0 ? ordTable.getValueAt(colOrd, ordColT.get("UNIT")) : cosTable.getValueAt(colCos, cosColT.get("UNIT")),
							(double) map.get(key).get(i)[0]>0 ? ordTable.getValueAt(colOrd, ordColT.get("UNITPRICE")) : cosTable.getValueAt(cosRow, cosColT.get("UNITPRICE")), 
							dimension.getText(), 
							(double) map.get(key).get(i)[0]>0 ? ordTable.getValueAt(colOrd, ordColT.get("DIMENSION_2")) : cosTable.getValueAt(cosRow, cosColT.get("DIMENSION_3")),
							(double) map.get(key).get(i)[0]>0 ? ordTable.getValueAt(colOrd, ordColT.get("DIMENSION_3")) : cosTable.getValueAt(cosRow, cosColT.get("DIMENSION_3")),
							(double) map.get(key).get(i)[0]>0 ? ordTable.getValueAt(colOrd, ordColT.get("DIMENSION_4")) : cosTable.getValueAt(cosRow, cosColT.get("DIMENSION_4")),
							subAcc,
							oType, false,
						//	(double) map.get(key).get(i)[0]<0 ? colOrd<0?  cosTable.getValueAt(cosRow, cosColT.get("COMENT")) : ordTable.getValueAt(colOrd, ordColT.get("COMENT")) :cosRow<0?ordTable.getValueAt(colOrd, ordColT.get("COMENT")): cosTable.getValueAt(cosRow, cosColT.get("COMENT"))
							(double) map.get(key).get(i)[0]<0 ? cosTable.getValueAt(cosRow, cosColT.get("COMENT")) :ordTable.getValueAt(colOrd, ordColT.get("COMENT"))
							};
							list.add(podting);
							if (key.equals("C")) {refreshQty(cosModel, cosColM, cosRow);}else{refreshQty(ordModel, ordColM ,colOrd );}
					}
				}
				}
			
			return list;
		}
		
		private ArrayList<Integer> genPostingHelp(DefaultTableModel model)
		{
			ArrayList<Integer> result = new ArrayList<Integer>();
			for (int i=0;i<model.getRowCount();i++)
			{
				if ((boolean)model.getValueAt(i, 0)==true)
				{
					result.add(i);
				}
			}
			return result;
		}
		
		private void getUpSum()
		{
			double left = getSumFromTable(ordColM.get("QTYAMOUNT"), ordModel,true,0);
			double right = getSumFromTable(cosColM.get("QTYAMOUNT"), cosModel,true,0);
			double sumd = FrameTemplate.round(left + right, 2);
			
			ct.setText(String.format("%.2f",FrameTemplate.round(right * -1,2)));
			dt.setText(String.format("%.2f",left));;
			sum.setText(String.format("%.2f",sumd));;
		}
		

		
		
		private double getSumFromTable(int column, DefaultTableModel model, boolean check, int checkCol)
		{
			double value = 0.00;
			for (int i=0; i<model.getRowCount();i++)
			{
				if ( (boolean) model.getValueAt(i, checkCol)==check)
				{
					value +=FrameTemplate.round(Double.valueOf(String.valueOf(model.getValueAt(i, column)).replace(",",".")), 2);
				}
			}
			return FrameTemplate.round(value, 2);
		}


		@Override
		public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			if ( e.getSource()==cosModel||e.getSource()==ordModel)
			{
				getUpSum();
			}//if (e.getSource()==posModel)
			//{
				//getDownSum();
			//}
		}
		
		private void chdocDate()
		{
			if (  dateCh ==true)
			{
				try{
			
			
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
				String strNewDate = LocalDate.parse(docDate.getText() , formatter).plusDays(1).format(formatter);
				String strOldDate = LocalDate.parse(minDate , formatter).minusDays(1).format(formatter);
				java.sql.Date newDate = java.sql.Date.valueOf(docDate.getText() );
				java.sql.Date oldDate= java.sql.Date.valueOf(minDate );
				if (newDate.before(oldDate))
				{

					JOptionPane.showMessageDialog(null, "Data nie powinna byæ starsza od: " + oldDate, "Informacja", JOptionPane.INFORMATION_MESSAGE);
					
				}
				}
				catch (java.time.format.DateTimeParseException a){  }
			}
		}

	
	class postingTab extends FrameTemplateTableNew implements TableModelListener, ActionListener
	{
		private JButton bPost, bUnPost;
		private JTextField ctP, dtP, sumP;
		public postingTab(String yamlSys, String modelProcedure, ArrayList<String> modelParameters, int enableType,
				boolean isCheck) {
			super(yamlSys, modelProcedure, modelParameters, enableType, isCheck);
			// TODO Auto-generated constructor stub
			ctP = new FrameTemplateButtons().getTextField();
			dtP = new FrameTemplateButtons().getTextField();
			sumP = new FrameTemplateButtons().getTextField();
			
			bPost= new FrameTemplateButtons().smallButton("Ksiêgowanie", FrameTemplateImageIcon.iconJButton_smallPostring());
			bUnPost= new FrameTemplateButtons().smallButton("Odksiêguj", FrameTemplateImageIcon.iconJButton_SmallUnAll());
			bPost.addActionListener(this);
			bUnPost.addActionListener(this);
			

		}

		public JPanel getPanel()
		{
	 
			JPanel result = new JPanel(new BorderLayout());
			JPanel downPanelUp = new JPanel(new FlowLayout(FlowLayout.LEFT));
				downPanelUp.add(frame.getSmallButtons());
				downPanelUp.add(bPost);
				downPanelUp.add(bUnPost);
			result.add(downPanelUp, BorderLayout.PAGE_START);
			result.add(new JScrollPane(table), BorderLayout.CENTER);
				JPanel downDownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
					downDownPanel.add(posID);
					downDownPanel.add(new JLabel("Dt:"));
					downDownPanel.add(ctP);
					downDownPanel.add(new JLabel("Ct:"));
					downDownPanel.add(dtP);
					downDownPanel.add(new JLabel("Suma:"));
					downDownPanel.add(sumP);
			result.add(downDownPanel, BorderLayout.PAGE_END);	
			addListener();
			diAct();
			
			return result;
		}
		
		@Override
		public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			 
			 
			if(e.getType()==-1
					&&(boolean)model.getValueAt(e.getFirstRow(), colM.get("BOOK"))==true)
			{
				frame.setDeleteEnable(false);
			}else
			{
				frame.setDeleteEnable(true);
			}
				
		}
		
		private void diAct()
		{
			frame.getsmallButtonAdd().setEnabled(false);
			frame.getSmallButtonDoc().setEnabled(false);
			frame.getSmallButtonImport().setEnabled(false);
			frame.getaddRowM().setEnabled(false);
			frame.getpasteM().setEnabled(false);
		}
		
		public void addListener()
		{
			model.addTableModelListener(this);
			addListeners(new ArrayList<Integer>(Arrays.asList(0,1,2)));
			///frame.setGgenKey(0, 3, 0);
			frame.addListenerSmallButtons();
			getDownSum();
			if (model.getRowCount()>0)
			{
				posID.setText(model.getValueAt(0, colM.get("ID")).toString());
			}
			
		}
		
		public void newModelP(ArrayList<String> parameters)
		{
			
			newModel(parameters);
		 		//	addListener();
		}
		
		
		public void addRowList( ArrayList<Object[]> list)
		{
			addRow(list);
		//	addListener();
		}
		private void getDownSum()
		{
			double ctD = getSumFromTable(colM.get("QTYAMOUNT"), model,true,colM.get("CREDITING"));
			double dtD = getSumFromTable(colM.get("QTYAMOUNT"), model,false,colM.get("CREDITING"));
			double sumD = FrameTemplate.round((-1*ctD)+dtD, 2);
			
			ctP.setText(String.format("%.2f",ctD));
			dtP.setText(String.format("%.2f",dtD));;
			sumP.setText(String.format("%.2f",sumD));;
			
			
			
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource()==bPost)
			{
				if (Double.valueOf(sumP.getText().toString().replace(",", "."))==0)
				{
					for (int i=0;i<model.getRowCount(); i++)
					{
						model.setValueAt(true, i,colM.get("BOOK"));
					}
					frame.Button_Save();
				}else if (e.getSource()==bUnPost)
				{
					for (int i=0;i<model.getRowCount(); i++)
					{
						model.setValueAt(false, i,colM.get("BOOK"));
					}
					frame.Button_Save();
				}
			}
		}
	}

	class checkDate implements DocumentListener
	{
		public void insertUpdate(DocumentEvent e)  
		{
		 	chdocDate();
		}
		public void removeUpdate(DocumentEvent e) {}
		public void changedUpdate(DocumentEvent e) { }
		

	}
	 
	}
}
