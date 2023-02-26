package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.CellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.AutoAccounting;
import MyLittleSmt.DocumentsSource;
import MyLittleSmt.FlatFile;
import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateButtons;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.FrameTemplateWindow;
import MyLittleSmt.InWords;
import MyLittleSmt.KeyAdder;
import MyLittleSmt.MainEntryDataWarehouse;
import MyLittleSmt.PythonOther;
import MyLittleSmt.SimpleUpdate;
import MyLittleSmt.StoredProcedures;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;


public class IProjectInvoice extends FrameTemplateWindow implements MouseListener, ActionListener{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//zestaw dla 5 tabel
	private HashMap<String, String> insSysBase, posSysBase, ordSysBase, codSysBase, mailSysBase, insSys, posSys, ordSys,
			codSys, mailSys;
	private HashMap<String, ArrayList<String>> insSysAll, posSysAll, ordSysAll, codSysAll, mailSysAll;
	private DefaultTableModel insModel, posModel, ordModel, codModel, mailModel;
	private JTable insTable, posTable, ordTable, codTable, mailTable;
	private FrameTemplate insFrame, posFrame, ordFrame, codFrame, mailFrame;
	private ArrayList<Integer> insKey, posKey;
	private JPanel rOption, rExport;
	private HashMap<Integer, String> dictColumnAndYaml, dictColumnAndYamlPos;
	private JCheckBox checkBoxIns, calGrossNett;
	private JTextField textFieldIns,textFieldInsSec, textFieldFirm, tfGrossOrd, tfNettoOrd, tfGrossPre, tfVATPre, tfNettoPre, tfGrossPC, tfNettoPC, tfInvDocDate, tfInvSettDate, tfInvDeliDate;
	private ArrayList<ArrayList<Object>> fullOrderList ; //<Firma< Numer zlecenia<numer lp, wiersz>>
	private ArrayList<ArrayList<String>> orderListPID; 
	private JButton editOrd, addCode, saveCode, addMail, saveMail, bInv, bPrePayEnd, bPrePay, bProForm, bAdju, bSPostings, bSShow ; 
	private JComboBox cbCode, calVAT;
	private ArrayList<String> VATstawki;
	private HashMap<String, Integer> columnIns;
	private String registrationDate;
	private ArrayList<ArrayList<String>> insDictList;
	private JRadioButton fromProForm, fromCalculator;
	private JMenuItem postingM, posDel;
	private String InvSeparator = ",";
	private boolean editOrdInUse = false;


	public IProjectInvoice(int x, int y, String title, String firm) {
		super(x, y, title);
		if (firm.equals("firma")) {firm="firm";}
		
		insFrame = new FrameTemplate();
		posFrame = new FrameTemplate();
		ordFrame = new FrameTemplate();
		codFrame = new FrameTemplate();
		mailFrame = new FrameTemplate();
		
		insFrame.JTableHelperSys("IInstruments_sys.yml", true);
		insSys = insFrame.getMapSys();
		insSysBase = insFrame.getMapSysBase();
		insSysAll = insFrame.getMapSysAll();
		insModel = getInsModel(firm);
		insTable = insFrame.getDefaultTable(insModel, insSysAll);
		insKey = new ArrayList<Integer>(Arrays.asList(1,2));
		
		
		columnIns = insFrame.getColumnNumbers(insModel, insSysAll);
		bSShow = new FrameTemplateButtons().smallButton("Poka¿ faktury projektu", FrameTemplateImageIcon.iconJButton_SmallShow());
		bSShow.addActionListener(this);
		
		checkBoxIns = new JCheckBox("Otwarte", true);
		checkBoxIns.addActionListener(this);
		textFieldIns = new JTextField();
		textFieldIns.setEnabled(false);
		textFieldIns.setPreferredSize(new Dimension(125,20));
		textFieldInsSec= new JTextField();
		textFieldInsSec.setEnabled(false);
		textFieldInsSec.setPreferredSize(new Dimension(125,20));
		
		textFieldFirm= new JTextField();
		textFieldFirm.setEnabled(false);
		textFieldFirm.setPreferredSize(new Dimension(125,20));

		
		
		posFrame.JTableHelperSys("IPositions_sys.yml");
		posSys = posFrame.getMapSys();
		posSysBase = posFrame.getMapSysBase();
		posSysAll = posFrame.getMapSysAll();
		posModel = getPosModel(firm);
		posTable = posFrame.getDefaultTable(posModel, posSysAll);
		posKey = new ArrayList<Integer>(Arrays.asList(0,1,5));
		
		//bSPostings = new FrameTemplateButtons().smallButton("Ksiêgowania", FrameTemplateImageIcon.iconJButton_smallPostring());
		//bSPostings.addActionListener(this);
		
		ordFrame.JTableHelperSys("IProjectInvoice_Orders_sys.yml");
		ordSys = ordFrame.getMapSys();
		ordSysBase = ordFrame.getMapSysBase();
		ordSysAll = ordFrame.getMapSysAll();
		ordModel = getOrdModel(firm, "XX");
		ordTable = ordFrame.getDefaultTable(ordModel, ordSysAll);
		
		tfGrossOrd = new JTextField();
		tfGrossOrd.setPreferredSize(new Dimension(125,20));
		tfGrossOrd.setEnabled(false);
		tfNettoOrd = new JTextField();
		tfNettoOrd.setPreferredSize(new Dimension(125,20));
		tfNettoOrd.setEnabled(false);
		
		
		codFrame.JTableHelperSys("IInstuments_sys_Code.yml");
		codSys = codFrame.getMapSys();
		codSysBase = codFrame.getMapSysBase();
		codSysAll = codFrame.getMapSysAll();
		codModel = getCodModel(firm,"XX");
		codTable = codFrame.getDefaultTable(codModel, codSysAll);
		
		mailFrame.JTableHelperSys("IInstuments_sys_Mail.yml");
		mailSys = mailFrame.getMapSys();
		mailSysBase = mailFrame.getMapSysBase();
		mailSysAll = mailFrame.getMapSysAll();
		mailModel =  getMailModel(firm,"XX");
		mailTable = mailFrame.getDefaultTable(mailModel, mailSysAll);
		
		editOrd = new  FrameTemplateButtons().smallButton("Edycja tabeli", FrameTemplateImageIcon.iconJButton_SmallEdit());
		editOrd.addActionListener(this);
		
		saveCode = codFrame.smallSave();
		saveCode.addActionListener(this);
		addCode = new FrameTemplateButtons().smallButton("Dodaj", FrameTemplateImageIcon.iconJButton_SmallAdd());
		addCode.addActionListener(this);
		cbCode = codFrame.getSimplComboBox("ComboBox_InstrumentsCode.yml", false);
		cbCode.setPreferredSize(new Dimension(125,20));
		
		saveMail= new FrameTemplateButtons().smallButton("Zapisz", FrameTemplateImageIcon.iconJButton_smallSave());
		saveMail.addActionListener(this);
		addMail = new FrameTemplateButtons().smallButton("Dodaj", FrameTemplateImageIcon.iconJButton_SmallAdd());
		addMail.addActionListener(this);
		
		bInv = new FrameTemplateButtons().RibbonJButton("Faktura", FrameTemplateImageIcon.iconJButton_Invoice());
		bPrePayEnd = new FrameTemplateButtons().RibbonJButton("Koñcowa", FrameTemplateImageIcon.iconJButton_endPrePayment());
		bPrePay = new FrameTemplateButtons().RibbonJButton("Zaliczkowa", FrameTemplateImageIcon.iconJButton_prePaymentInvoice());
		bProForm = new FrameTemplateButtons().RibbonJButton("Pro Forma", FrameTemplateImageIcon.iconJButton_proForm());
		bAdju = new FrameTemplateButtons().RibbonJButton("Korekta", FrameTemplateImageIcon.iconJButton_adjustment());
		fromProForm = new JRadioButton("Zaliczka z proFormy", true); 
		fromCalculator = new JRadioButton("Zaliczka z kalkulatora");
		ButtonGroup bg=new ButtonGroup();  
		bg.add(fromProForm);
		bg.add(fromCalculator);
		bProForm.addActionListener(this);
		bPrePay.addActionListener(this);
		bInv.addActionListener(this);
		bPrePayEnd.addActionListener(this);
		bAdju.addActionListener(this);
		//Calculator
		tfGrossPre = new JTextField();
		tfGrossPre.setPreferredSize(new Dimension(125,20));
		tfGrossPre.getDocument().addDocumentListener(new grossVatCalLis());
		tfVATPre= new JTextField();
		tfVATPre.setPreferredSize(new Dimension(125,20));
		tfNettoPre= new JTextField();
		tfNettoPre.setPreferredSize(new Dimension(125,20));
		tfGrossPC= new JTextField();
		tfGrossPC.setPreferredSize(new Dimension(125,20));
		tfNettoPC= new JTextField();
		tfNettoPC.setPreferredSize(new Dimension(125,20));	
		tfNettoPC.setText("%");
		tfGrossPC.setText("%");
		tfNettoPC.getDocument().addDocumentListener(new netPCCalLis() );
		tfGrossPC.getDocument().addDocumentListener(new grossPCCalLis() );
		tfNettoPre.getDocument().addDocumentListener(new netVatCalLis());
		VATstawki = new ArrayList<String>(Arrays.asList("23", "8", "5", "0"));
		calVAT = insFrame.getSimplComboBox(VATstawki, false);
		calVAT.setPreferredSize(new Dimension(125,20));
		calVAT.addActionListener(this);
		calGrossNett = new JCheckBox("Netto (Kalkulator)", true);
		tfInvDocDate = new JTextField();
		tfInvDocDate.setPreferredSize(new Dimension(125,20));
		tfInvDocDate.getDocument().addDocumentListener(new preDate());
		tfInvSettDate = new JTextField();
		tfInvSettDate.setPreferredSize(new Dimension(125,20));
		tfInvDeliDate = new JTextField();
		tfInvDeliDate.setPreferredSize(new Dimension(125,20));
		
		tfInvDocDate.setText(invDate(0));
		tfInvDeliDate.setText(invDate(0));
		tfInvSettDate.setText(invDate(0));
		
		addJTextFields();
		JPanel upPanel = new JPanel(new BorderLayout());
			upPanel.add(insFrame.GetUpMenu(false), BorderLayout.PAGE_START);
				JTabbedPane jtpUp = new JTabbedPane();
					rOption = new JPanel(new FlowLayout(FlowLayout.LEFT));
					rExport = new JPanel(new FlowLayout(FlowLayout.LEFT));
					//	rOption.add(insFrame.DefaultRibbonSim());
					//	rExport.add(insFrame.DefaultRibbonExp());
					JPanel invOption = new JPanel(new FlowLayout(FlowLayout.LEFT));
						JPanel invOptSim = new JPanel(new BorderLayout());
							JPanel simBut =  new JPanel(new FlowLayout(FlowLayout.LEFT));
								simBut.add(bInv);
								simBut.add(bPrePayEnd);
							JPanel simHelp = new JPanel(new FlowLayout(FlowLayout.LEFT));
								simHelp.add(new JLabel("<html>Generowane zbiorczo dla <br/> zaznaczonych</html>", SwingConstants.LEFT));
							invOptSim.add(simHelp, BorderLayout.PAGE_START);
							invOptSim.add(simBut, BorderLayout.CENTER);
						invOption.add(invOptSim);
						//invOption.add(bPrePayEnd);
						JPanel prePayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
							JPanel prePayButtons = new JPanel(new BorderLayout());
								JPanel prePayButD = new JPanel(new FlowLayout(FlowLayout.LEFT));
									prePayButD.add(bPrePay);
									prePayButD.add(bProForm);
								JPanel prePayButUp = new JPanel(new FlowLayout(FlowLayout.LEFT));
									prePayButUp.add(new JLabel("Wystawiane dla: "));
									prePayButUp.add(textFieldInsSec);
									prePayButtons.add(prePayButUp, BorderLayout.PAGE_START);
									prePayButtons.add(prePayButD, BorderLayout.CENTER);
							prePayPanel.add(prePayButtons);
					 
							JPanel radioPanel = new JPanel(new BorderLayout());
								radioPanel.add(fromProForm, BorderLayout.PAGE_START);
								radioPanel.add(fromCalculator, BorderLayout.CENTER);
								radioPanel.add(calGrossNett, BorderLayout.PAGE_END);
								
							prePayPanel.add(radioPanel);
							
							JPanel calculator = new JPanel(new BorderLayout());
								calculator.setBorder(new TitledBorder("Kalkulator zaliczki"));
								JPanel nettoCal = new JPanel(new FlowLayout(FlowLayout.LEFT));
									nettoCal.add(new JLabel("Netto: "));
									nettoCal.add(tfNettoPre);
									nettoCal.add(tfNettoPC);
								JPanel vatCal = new JPanel(new FlowLayout(FlowLayout.LEFT));
									vatCal.add(new JLabel("VAT:   "));
									vatCal.add(tfVATPre);
									vatCal.add(calVAT);
								JPanel grossCal = new JPanel(new FlowLayout(FlowLayout.LEFT));
									grossCal.add(new JLabel("Brutto:"));
									grossCal.add(tfGrossPre);
									grossCal.add(tfGrossPC);	
								calculator.add(nettoCal, BorderLayout.PAGE_START);
								calculator.add(vatCal, BorderLayout.LINE_START);
								calculator.add(grossCal, BorderLayout.PAGE_END);
							prePayPanel.add(calculator);
							JPanel invPrePayDates = new JPanel(new BorderLayout());
								JPanel dates0 = new JPanel (new FlowLayout(FlowLayout.RIGHT));
									dates0.add(new JLabel("Data dokumentu:"));
									dates0.add(tfInvDocDate);
								JPanel dates1 = new JPanel (new FlowLayout(FlowLayout.RIGHT));
									dates1.add(new JLabel("Data dostawy:"));
									dates1.add(tfInvDeliDate);
								JPanel dates2 = new JPanel (new FlowLayout(FlowLayout.RIGHT));
									dates2.add(new JLabel("Data p³atnoœci:"));
									dates2.add(tfInvSettDate);
							invPrePayDates.add(dates0, BorderLayout.PAGE_START);
							invPrePayDates.add(dates1, BorderLayout.CENTER);
							invPrePayDates.add(dates2, BorderLayout.PAGE_END);
						prePayPanel.add(invPrePayDates);
						prePayPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.GRAY));BorderFactory.createLineBorder(Color.gray);
						//invOption.add(bPrePay);
						//invOption.add(bProForm);
						invOption.add(prePayPanel);
						
						invOption.add(bAdju);
						rOption.add(invOption);
					jtpUp.add("Opcje", rOption);
					
					//jtpUp.add("Eksport", rExport);
			upPanel.add(jtpUp, BorderLayout.PAGE_END);
		add(upPanel, BorderLayout.PAGE_START);
		JPanel downPanel = new JPanel(new BorderLayout(5,5));
			JPanel leftPanel = new JPanel(new GridLayout(2,1));
				JPanel leftPanelUp = new JPanel(new BorderLayout());
					JPanel leftPanelUpUp = new JPanel(new FlowLayout(FlowLayout.LEFT));
						leftPanelUpUp.add(checkBoxIns);
						leftPanelUpUp.add(textFieldIns);
						leftPanelUpUp.add(textFieldFirm);
							JPanel smallButIns = insFrame.getSmallButtons();
							smallButIns.add(bSShow);
						leftPanelUpUp.add(smallButIns);
					leftPanelUp.add(leftPanelUpUp, BorderLayout.PAGE_START);
					leftPanelUp.setBorder(new TitledBorder("Instrumenty"));
					leftPanelUp.add(new JScrollPane(insTable), BorderLayout.CENTER);
				JPanel leftPanelDown = new JPanel(new BorderLayout());
					leftPanelDown.setBorder(new TitledBorder("Lista wystawionych faktur"));  
						//JPanel smallPos = new JPanel(new FlowLayout(FlowLayout.LEFT));
						//	smallPos.add(bSPostings);
					//leftPanelDown.add(smallPos, BorderLayout.PAGE_START);
					leftPanelDown.add(new JScrollPane(posTable), BorderLayout.CENTER);
				leftPanel.add(leftPanelUp);
				leftPanel.add(leftPanelDown);
			JPanel rightPanel = new JPanel(new GridLayout(3,1));
				JPanel rightPanelUp = new JPanel(new BorderLayout());
					rightPanelUp.setBorder(new TitledBorder("Pozycje faktury"));
					JPanel rightPanelUpUp = new JPanel(new FlowLayout(FlowLayout.LEFT));
						rightPanelUpUp.add(editOrd);
						rightPanelUpUp.add(new JLabel("Netto:"));
						rightPanelUpUp.add(tfNettoOrd);
						rightPanelUpUp.add(new JLabel("Brutto:"));
						rightPanelUpUp.add(tfGrossOrd);
					rightPanelUp.add(rightPanelUpUp, BorderLayout.PAGE_START);
					rightPanelUp.add(new JScrollPane(ordTable), BorderLayout.CENTER);
				JPanel rightPanelUpCenter = new JPanel(new BorderLayout());
					rightPanelUpCenter.setBorder(new TitledBorder("Kody instumentów"));
					rightPanelUpCenter.add(new JScrollPane(codTable), BorderLayout.CENTER);
					JPanel rightPanelUpCenterUp = new JPanel(new FlowLayout(FlowLayout.LEFT));
						rightPanelUpCenterUp.add(saveCode);
						rightPanelUpCenterUp.add(addCode);
						rightPanelUpCenterUp.add(cbCode);
					rightPanelUpCenter.add(rightPanelUpCenterUp, BorderLayout.PAGE_START);
				JPanel rightPanelUpDown = new JPanel(new BorderLayout());
					rightPanelUpDown.setBorder(new TitledBorder("Adresy eMail"));
					rightPanelUpDown.add(new JScrollPane(mailTable), BorderLayout.CENTER);
					JPanel rightPanelUpDownUp = new JPanel(new FlowLayout(FlowLayout.LEFT));
						rightPanelUpDownUp.add(saveMail);
						rightPanelUpDownUp.add(addMail);
					rightPanelUpDown.add(rightPanelUpDownUp, BorderLayout.PAGE_START);
			rightPanel.add(rightPanelUp);
			rightPanel.add(rightPanelUpCenter);
			rightPanel.add(rightPanelUpDown);
		downPanel.add(leftPanel, BorderLayout.CENTER);
		downPanel.add(rightPanel, BorderLayout.LINE_END);
	add(downPanel);
	postingM = new JMenuItem("Ksiêgowania");
	postingM.addActionListener(this);
	posFrame.getPopup().add(postingM);
	invTableListener();
	 otherListeners();
	 addNewListeners();
	}
	
	private void invTableListener()
	{
		insFrame.addFrameTempListener(insTable, insModel, insSysAll, insSysBase, insSys, insKey);	
			dictColumnAndYaml = new HashMap<Integer, String>();
			dictColumnAndYaml.put(3, "IInstruments_Dict_Model.yml");
			dictColumnAndYaml.put(4, "IInstruments_Dict_Model.yml");
			insFrame.addDictToModel(dictColumnAndYaml, insModel);
			insDictList = insFrame.getOryginalDictList();
		insFrame.addListenerContextMenu();
		insFrame.addListenerSmallButtons();
		insFrame.getsmallButtonAdd().setEnabled(false);
		insFrame.addListenerJTable(insTable, insModel);
		insTable.addMouseListener(this);
		insFrame.setFilter("false", 11);
		insFrame.addSkipList(new ArrayList<Integer>(Arrays.asList(0)));
		insFrame.addListenerRibbon();
		insFrame.getaddRowM().setEnabled(false);
		insFrame.getdeleteM().setEnabled(false);
		insFrame.setMenuRun(3);
		insFrame.setMenuRun(4);
		fullOrderList = new ArrayList<ArrayList<Object>>(); //<Firma< Numer zlecenia<numer lp, wiersz>>
		orderListPID = new ArrayList<ArrayList<String>>();	
		posFrame.addFrameTempListener(posTable, posModel, posSysAll, posSysBase, posSys, posKey);
		posFrame.sourceContextMenu();
		posFrame.setkeyCol(0);
		posFrame.addListenerContextMenu();
		posFrame.setSourceWorkPath("C:\\MyLittleSmt\\Szablony_HTML\\");
		posFrame.isEditContextMenu(false);
		posFrame.addListenerJTable(posTable, posModel);
		dictColumnAndYamlPos = new HashMap<Integer, String>();
		dictColumnAndYamlPos.put(7, "IInstruments_Dict_Model.yml");
		posFrame.addDictToModel(dictColumnAndYamlPos, posModel);
		posFrame.remListmDelete();
		posDel = posFrame.getdeleteM();
		posDel.setEnabled(true);
		posDel.addActionListener(this);
	}
	
	private void otherListeners()
	{
		ordFrame.addFrameTempListener(ordTable, ordModel, ordSysAll, ordSysBase, ordSys, null);
		ordFrame.addListenerContextMenu();
		ordTable.addMouseListener(this);
		ordFrame.isEditContextMenu(false);
	}
	
	public void addNewListeners()
	{
		codFrame.addFrameTempListener(codTable, codModel, codSysAll, codSysBase, codSys, new ArrayList<Integer>(Arrays.asList(0,1,2)));
		codFrame.addListenerJTable(codTable, codModel);
		codFrame.addListenerContextMenu();
		codFrame.isEditContextMenu(false);
		mailFrame.addFrameTempListener(mailTable, mailModel, mailSysAll, mailSysBase, mailSys, new ArrayList<Integer>(Arrays.asList(0,1)));
		mailFrame.addListenerJTable(mailTable, mailModel);
		mailFrame.addListenerContextMenu();
		mailFrame.isEditContextMenu(false);
	}
	
	/**
	 * Generacja modelu dla tabeli Instrumentów
	 * @param firm - wybrana firma
	 * @return
	 */
	private DefaultTableModel getInsModel(String firm)
	{
		return new StoredProcedures().genUniversalModel(true, "getInstrumentsByFirm",new ArrayList<String>(Arrays.asList(firm)), 10, insSys, insSysAll);
	}
	/**
	 * Generacja modelu dla pozycji ksiêgowañ 
	 * @param firm
	 * @return
	 */
	private DefaultTableModel getPosModel(String firm)
	{
		return new StoredProcedures().genUniversalModel("getPositionInvoice",new ArrayList<String>(Arrays.asList(firm)),  1, posSys, posSysAll);
	}
	/**
	 * Generacja modelu dla pozycji Towarów
	 * @param firm
	 * @param projectId
	 * @return
	 */
	private DefaultTableModel getOrdModel(String firm, String projectId)
	{	
		return new StoredProcedures().genUniversalModel("getOrdersToIncomeInvoices",new ArrayList<String>(Arrays.asList( projectId, firm )), 15, ordSys, ordSysAll);
		
	}
	/**
	 * Generacja modelu dla kodów instrumentów
	 * @param firm
	 * @param projectId
	 * @return
	 */
	private DefaultTableModel getCodModel(String firm, String projectId)
	{
		return new StoredProcedures().genUniversalModel("getCodeToInstr",new ArrayList<String>(Arrays.asList(projectId,firm)), 11, codSys, codSysAll);
	}
	/**
	 * Generacja modelu dla adresów mailowych
	 * @param firm
	 * @param projectId
	 * @return
	 */
	private DefaultTableModel getMailModel(String firm, String projectId)
	{
		return new StoredProcedures().genUniversalModel("getMailToInstr",new ArrayList<String>(Arrays.asList(projectId , firm)), 3, mailSys, mailSysAll);
	}

	private void addJTextFields() 
	{
		tfGrossOrd.setText("0");
		tfNettoOrd.setText("0");
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	private void ordTableRun(String firm, String newid )
	{
		//tutaj mogê za³adowaæ stare
		
		ordModel = getOrdModel(firm, newid);
		 
		ordFrame.setModel(ordModel);
		setTextFieldOrd();
		if (!newid.equals("")&&ordModel.getRowCount()>-1)
		{
			boolean isNew=true;
			for (int i=0; i<orderListPID.size();i++)
			{
				if (orderListPID.get(i).get(0).equals(newid)&&orderListPID.get(i).get(1).equals(firm))
				{
					isNew=false;
					break;
				}
			}
			
			if (isNew==true)
			{
				orderListPID.add(new ArrayList<String>(Arrays.asList(newid, firm)));
				genIdFromOrder(newid, firm);
			}else 
			{
				modOrderFromId(firm, newid);
			}
			//tak aby dodaj¹c na isNewFalse móc skorygowaæ to w chwili wystawienia faktury
			 
		}
		

	}
	

	
	private void setTextFieldOrd()
	{
	double nettoOrd = 0;
	double grossOrd = 0;
		for (int i=0;i<ordModel.getRowCount();i++)
		{
			if (ordModel.getValueAt(i, 6).equals(true))
			{
			nettoOrd += FrameTemplate.round(Double.valueOf(ordModel.getValueAt(i, 3).toString().replace(",", ".")),2);
			grossOrd += FrameTemplate.round(Double.valueOf(ordModel.getValueAt(i, 4).toString().replace(",", ".")),2);
			}
		}
		tfNettoOrd.setText(String.valueOf(FrameTemplate.round(nettoOrd,2)));
		tfGrossOrd.setText(String.valueOf(FrameTemplate.round(grossOrd,2)));
		
	}
	

	
	private void genIdFromOrder(String newID, String firm)
	{
		for (int i=0; i<ordModel.getRowCount();i++)
		{
			var tempList =new ArrayList<Object>(Arrays.asList(firm,newID)); 
			for (int j=0; j<ordModel.getColumnCount(); j++)
			{
				tempList.add(ordModel.getValueAt(i, j));
			}
			fullOrderList.add(tempList);
		}
	}
	private void modIdFromOrder(int column, int row)
	{
		
		HashMap<String, Integer> columnMap =  columnMap(ordTable);
		for (int i=0; i<fullOrderList.size();i++)
		{
			if (fullOrderList.get(i).get(0).equals(textFieldFirm.getText()) //czy firma
				&&fullOrderList.get(i).get(1).equals(textFieldIns.getText()) //czy instrument
				&&fullOrderList.get(i).get(2).equals(ordTable.getValueAt(row, columnMap.get("Lp"))))
				{
				fullOrderList.get(i).set(8,(boolean) ordTable.getValueAt(row, column));
				break;
				}
		}
		//System.out.println(ordTable.getValueAt(row, column));
	}
	
	private void modOrderFromId(String firm,String  id)
	{
		if (fullOrderList.size()>0)
		{
			HashMap<String, Integer> columnMap =  ordFrame.getColumnNumbers(ordModel, ordSysAll);
			ArrayList<Integer> deleteRow = new ArrayList<Integer>();
			for (int i=0; i<ordModel.getRowCount();i++)
			{
				for (int j=0; j<fullOrderList.size();j++)
				{
					if (fullOrderList.get(j).get(0).equals(firm)&&
							fullOrderList.get(j).get(1).equals(id))
					{
						if (fullOrderList.get(j).get(2).equals(ordModel.getValueAt(i, columnMap.get("Lp")))&&editOrdInUse==true)
						{
							ordModel.setValueAt(fullOrderList.get(j).get(8), i, columnMap.get("CHECKER"));
						}
						if (!deleteRow.contains(j))
						{deleteRow.add(j);}
					}
				}
			}
			int size_=deleteRow.size()-1;
			for (int i =size_; i>=0 ;i--)
			{
				int j=deleteRow.get(i);
				fullOrderList.remove(j);
			}
			genIdFromOrder(id, firm);
		

		}
		//System.out.println(ordTable.getValueAt(row, column));
	}
	
	private HashMap<String, Integer> columnMap(JTable tables)
	{
		var tempList = new HashMap<String, Integer>();
		for (int i=0;i<tables.getColumnCount();i++)
		{
			tempList.put(tables.getColumnName(i), i);
		}
		return tempList;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==insTable)
		{
			if (e.getClickCount()==2)
			{
				if (insTable.getColumnName(insTable.getSelectedColumn()).equals("Kontrahent")||insTable.getColumnName(insTable.getSelectedColumn()).equals("Odbiorca"))
				{ 
					insFrame.getSelectionRunWithParameters(insTable, "getConterparty",new ArrayList<String>() , "Dict_Conterparty.yml", insTable.getColumnName(insTable.getSelectedColumn()),2);
				}
			}
			if (insTable.getColumnName(insTable.getSelectedColumn()).equals("ID"))
			{
				String firmSec=null;

				for (int i=0; i<insTable.getColumnCount();i++)
				{
					if (insTable.getColumnName(i).equals("Firma") )
					{
						firmSec = insTable.getValueAt(insTable.getSelectedRow(), i).toString();
					}
				}
				HashMap<String, Integer> columnMap = insFrame.getColumnNumbers(insTable, insSysAll); 
				String instrSec=insTable.getValueAt(insTable.getSelectedRow(), columnMap.get("ID")).toString();
				editOrdInUse=true;
				changeInstrument(!firmSec.equals(" ") ? firmSec : "X", !instrSec.equals(" ")? instrSec : "X" );		
				editOrdInUse=false;
				textFieldIns.setText(insTable.getValueAt(insTable.getSelectedRow(), columnMap.get("ID")).toString());
				textFieldInsSec.setText(insTable.getValueAt(insTable.getSelectedRow(), columnMap.get("ID")).toString());
				textFieldFirm.setText((String) insTable.getValueAt(insTable.getSelectedRow(), columnMap.get("FIRM")));
				registrationDate = insTable.getValueAt(insTable.getSelectedRow(), columnMap.get("Registration")).toString();
				newCalCalc();
				tfInvSettDate.setText(invDate(Integer.valueOf((String) insTable.getValueAt(insTable.getSelectedRow(), columnMap.get("PaymentDeadline")))));
			}
			
		}else if (e.getSource()==ordTable)
		{	
			HashMap<String, Integer> columnMap = ordFrame.getColumnNumbers(ordTable, ordSysAll);

			if (ordTable.getSelectedColumn()==columnMap.get("CHECKER"))
			{
				if (ordTable.getValueAt(ordTable.getSelectedRow(), columnMap.get("Status")).equals("0")||ordTable.getValueAt(ordTable.getSelectedRow(), columnMap.get("Status")).equals("1"))
				{
					modIdFromOrder(ordTable.getSelectedColumn(), ordTable.getSelectedRow());
					setTextFieldOrd();
					newCalCalc();

					
				}
				else if ((!ordTable.getValueAt(ordTable.getSelectedRow(), columnMap.get("Status")).equals("0")||!ordTable.getValueAt(ordTable.getSelectedRow(), columnMap.get("Status")).equals("1"))&&!ordTable.getValueAt(ordTable.getSelectedRow(), columnMap.get("CHECKER")).equals(false))
				{
					JOptionPane.showMessageDialog(null, "Fakturowane mog¹ byæ jedynie pozycje o statusie 0 lub 1", "Informacja", JOptionPane.INFORMATION_MESSAGE);
					ordTable.setValueAt((boolean) false, ordTable.getSelectedRow(),  columnMap.get("CHECKER"));
				} 
			}
		}
	}

	private void changeInstrument(String firm, String newid)
	{
		
		ordTableRun(firm, newid);
		mailModel = getMailModel(firm, newid);
		mailFrame.setModel(mailModel);
		codModel = getCodModel(firm, newid);
		codFrame.setModel(codModel);
		addNewListeners();
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
		if (e.getSource()==checkBoxIns)
		{
			if (checkBoxIns.isSelected()==false)
			{
				insFrame.setRemoveFiltr();
			}else if (checkBoxIns.isSelected()==true)
			{
				insFrame.setFilter("false", 11);
			}
		}else if (e.getSource()==editOrd)
		{

			if (!textFieldIns.getText().trim().equals("")||!textFieldFirm.getText().trim().equals(""))
			{
				insDictList.get(0);
				columnIns = insFrame.getColumnNumbers(insTable, insSysAll);
				String conterparty="";
				for (int i=0;i<insDictList.size();i++)
				{
					if (insTable.getValueAt(insTable.getSelectedRow(), columnIns.get("Counterparty")).equals(insDictList.get(i).get(1)))
					{
						conterparty=insDictList.get(i).get(0);
					}
				}
				//var	isc = new scOrders("IOrders_sys.yml", "getOrders", new ArrayList<String>(Arrays.asList(textFieldIns.getText(), textFieldFirm.getText())),3,"Pozycje zamówienia",new ArrayList<Integer>(Arrays.asList(0,1,2)), textFieldFirm.getText(), textFieldIns.getText(), registrationDate, conterparty);  
				var	isc = new IOrdersJDialog("IOrders_sys.yml", "getOrders", new ArrayList<String>(Arrays.asList(textFieldIns.getText(), textFieldFirm.getText())),12,"Pozycje zamówienia",new ArrayList<Integer>(Arrays.asList(0,1,2)), textFieldFirm.getText(), textFieldIns.getText(), registrationDate, conterparty);  
				isc.setVisible(true);
				editOrdInUse = true;
				changeInstrument(textFieldFirm.getText(), textFieldIns.getText());
				editOrdInUse = false;
			}
			else
			{
				 getMessage();
			}
		}else if (e.getSource()==addCode)
		{
			if (!textFieldIns.getText().equals("")||!textFieldFirm.getText().equals(""))
			{
				if (!cbCode.getSelectedItem().equals(""))
				{
					boolean addRow = true;
					for (int i=0;i<codModel.getRowCount();i++)
					{
						if (codModel.getValueAt(i, 2).equals(cbCode.getSelectedItem()))
						{
							addRow = false;
							break;
						}
					}
					if (addRow==true)
					{
						Object[] newRow = {textFieldFirm.getText(),textFieldIns.getText(),cbCode.getSelectedItem(),""};
						codModel.insertRow(0, newRow);
					}else 
					{
						JOptionPane.showMessageDialog(null, "Taki kod ju¿ istnieje", "Informacja", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
			else
			{
				getMessage();
			}
		}else if (e.getSource()==saveCode)
		{
			codFrame.Button_Save();
		}else if (e.getSource()==saveMail)
		{
			mailFrame.Button_Save();
		}else if (e.getSource()==addMail)
		{
			if (!textFieldIns.getText().equals("")||!textFieldFirm.getText().equals(""))
			{
				Object[] newRow = {textFieldFirm.getText(),textFieldIns.getText(),""};
				mailModel.insertRow(0, newRow);
			}else
			{
				getMessage();
			}
			
		}else if (e.getSource()==bProForm||e.getSource()==bPrePay)
		{
			if (textFieldIns.getText().toString().length()>1)
			{
				int preInvType = e.getSource()==bProForm ? 1 : 0;
				int reply = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz wystawiæ fakturê " + (preInvType==1 ? "proFrome " : "zaliczkow¹ ") +"dla projektu: "  + textFieldIns.getText(), textFieldIns.getText(), JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION)
					{
						genPrePayInvoice(preInvType, textFieldIns.getText(), textFieldFirm.getText(), tfInvDocDate.getText(), tfInvDeliDate.getText(),tfInvSettDate.getText());
					}
			}else 
			{
				JOptionPane.showMessageDialog(null, "Brak zaznaczonego projektu", "Informacja", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if (e.getSource()==bInv)
		{
			genInvoice();
			
		}else if (e.getSource()==bPrePayEnd)
		{
			if (textFieldIns.getText().toString().length()>1)
			{
				int preInvType = 2;
				int reply = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz wystawiæ fakturê zaliczkow¹ koñcow¹ dla projektu: "  + textFieldIns.getText(), textFieldIns.getText(), JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION)
					{
						genPrePayInvoice(preInvType, textFieldIns.getText(), textFieldFirm.getText(), tfInvDocDate.getText(), tfInvDeliDate.getText(),tfInvSettDate.getText());
					}
			}else 
			{
				JOptionPane.showMessageDialog(null, "Brak zaznaczonego projektu", "Informacja", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if (e.getSource()==bAdju)
		{
			ArrayList<String> mew = new FlatFile().ImportFlatFile(true, "C:\\MyLittleSmt\\Szablony_HTML\\Szablon faktura.html");
			 FileWriter myWriter =null;
			try {
				HashMap<String, ArrayList<String>> map = MainEntryDataWarehouse.getFirmsMap();
				String inWords = new InWords().inPolish(123112125.341, "PLN");
				inWords = new InWords().inPolish(1987.341, "PLN");
				inWords = new InWords().inPolish(123345.341, "PLN");
				inWords = new InWords().inPolish(343.00, "PLN");
				inWords = new InWords().inPolish(20.00, "PLN");
				
				myWriter = new FileWriter(new File("C:\\MyLittleSmt\\Szablony_HTML\\Szablon faktura_test1.html"), StandardCharsets.UTF_8);
				HashMap<String, String> regExList = new HashMap<String, String>();
				regExList.put(".DOCUMENTDATE.", "2022-11-01");
				regExList.put("[|]+CITY[|]+", "Miñsk Mazowiecki");
				regExList.put(".ENDDATE.", "2022-11-01");
				regExList.put(".IVOICE_NUMBER.", "Dupny");
				
				for (int i=0;i<mew.size();i++)
				{
					String str = mew.get(i);
					for (String k: regExList.keySet())
					{
						Pattern pat = Pattern.compile(k);
						Matcher mat = pat.matcher(str);
						if (mat.find())
						{
						String newStr = mat.replaceAll(regExList.get(k));
						str = newStr;
						break;
						}
					}
						myWriter.write(str + "\n");
					 
				}
				
				 
				myWriter.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
			System.out.print("dupa");
		}else if (e.getSource()==calVAT)
		{
			if (calGrossNett.isSelected())
			{
				tfNettoPre.setText(tfNettoPre.getText().toString());
			}else
			{
				tfGrossPre.setText(tfGrossPre.getText().toString());
			}
			
		}else if (e.getSource()==postingM)
		{
			if (posTable.getSelectedColumn()>-1&&posTable.getSelectedRow()>-1)
			{
			HashMap<String, Integer> columnInsPos = insFrame.getColumnNumbers(posTable, posSysAll);
			IPostings postings = new IPostings(500, 500, 
					"Ksiêgowania projektu: " +(String) posTable.getValueAt(posTable.getSelectedRow(), columnInsPos.get("ID")), 
			 		(String) posTable.getValueAt(posTable.getSelectedRow(), columnInsPos.get("ID")),(String) posTable.getValueAt(posTable.getSelectedRow(), columnInsPos.get("FIRM")), 
					(String) posTable.getValueAt(posTable.getSelectedRow(), columnInsPos.get("OTYPE")),false);
			postings.setVisible(true);
			}
		}else if (e.getSource()==bSShow)
		{
			if (textFieldIns.getText().equals("")||textFieldFirm.getText().equals(""))
			{
				JOptionPane.showMessageDialog(null, "Brak zaznaczonego projektu", "Informacja", JOptionPane.INFORMATION_MESSAGE);
			}else
			{
				showInvoiceFilter(textFieldFirm.getText(), textFieldIns.getText());
			}
		}else if (e.getSource()==posDel)
		{
			posDeleteAction();  
		}
	}
	
	private void posDeleteAction()
	{
		if (posTable.getSelectedRow()>-1&&posTable.getSelectedColumn()>-1)
		{
			HashMap<String, Integer> columnTable = posFrame.getColumnNumbers(posTable, posSysAll);
			String delInv = (String) posTable.getValueAt(posTable.getSelectedRow(), columnTable.get("TXT"));
			String delID = (String) posTable.getValueAt(posTable.getSelectedRow(), columnTable.get("ID"));
			String delOtype = (String) posTable.getValueAt(posTable.getSelectedRow(), columnTable.get("OTYPE"));
			String delFIRM = (String) posTable.getValueAt(posTable.getSelectedRow(), columnTable.get("FIRM"));
			int reply = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz usun¹æ fakturê: " + delInv , delInv , JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
			{
				String endInv = delInv.substring(6);
				String startInv = delInv.substring(0, 2);
				String number = delInv.substring(2, 6);
				ArrayList<ArrayList<String>> postingMaxArray=  new StoredProcedures().genUniversalArray("getInvoiceMaxID", new ArrayList<String>(Arrays.asList(endInv, startInv,delOtype, delFIRM)));
				double maxNum =Double.valueOf(postingMaxArray.get(1).get(0).replace(",","."));
				double invNum = Double.valueOf(number.replace(",","."));
				if (maxNum<=invNum)
				{
						var pos = new IPostings(0, 0, "usuwanie", 
								delID, delFIRM, delOtype, true);
 						pos.dispose();
 						new DocumentsSource().deleteSource(delID, delFIRM);
 						posFrame.removeRowInModel(posModel, posTable, posTable.getSelectedRow());
 						posFrame.Button_Save();
 						
 						 
				}else
				{
					message("Nie mo¿na usun¹æ faktury. Istniej¹ faktury pozniejsze.");
				}
			}
		}
	}
	
	private void showInvoiceFilter(String firm, String instr)
	{
		ArrayList<ArrayList<String>> tempList = new StoredProcedures().genUniversalArray("getInvoiceByInstrument", new ArrayList<String>(Arrays.asList(firm,instr)));
		posFrame.setFilter(tempList, 0);
	}
	
	private void genInvoice()
	{
		ArrayList<ArrayList<String>> cInstrList = new ArrayList<ArrayList<String>>();
		 
		for (int i=0; i<insModel.getRowCount();i++)
		{
			if (insModel.getValueAt(i, columnIns.get("Check")).equals(true))
			{
				double fullNettOrd = 0;
				double fullGrossOrd = 0;
					
				boolean onList = false;
				for (int j=0; j<orderListPID.size();j++)
				{
					if (orderListPID.get(j).get(0).equals(insModel.getValueAt(i, columnIns.get("ID")))&&orderListPID.get(j).get(1).equals(insModel.getValueAt(i, columnIns.get("FIRM"))))
					{
						onList =true;
						for (int k=0; k<fullOrderList.size();k++)
						{
							if (fullOrderList.get(k).get(0).equals(insModel.getValueAt(i, columnIns.get("FIRM")))&&fullOrderList.get(k).get(1).equals(insModel.getValueAt(i, columnIns.get("ID")))&&fullOrderList.get(k).get(8).toString().toUpperCase().equals("TRUE"))
							{
								fullNettOrd += FrameTemplate.round(Double.valueOf(String.valueOf(fullOrderList.get(k).get(5)).replace(",", ".")),2);
								fullGrossOrd  += FrameTemplate.round(Double.valueOf(String.valueOf(fullOrderList.get(k).get(6)).replace(",", ".")),2);
							}

						}
						break;
					}
				}
				if (onList ==false)
				{
					ArrayList<ArrayList<String>> tempList = new StoredProcedures().genUniversalArray("getOrdersToIncomeInvoices",new ArrayList<String>(Arrays.asList((String) insModel.getValueAt(i, columnIns.get("ID")), (String) insModel.getValueAt(i, columnIns.get("FIRM")))));
					HashMap<String, Integer> columnTempList = ordFrame.getColumnNumbers(tempList.get(0));
					for (int k=1;k<tempList.size();k++)
					{
						if (tempList.get(k).get(columnTempList.get("CHECKER")).toUpperCase().equals("TRUE"))
						{
							ArrayList<Object> temp = new ArrayList<Object>();
							tempList.get(k).add(0, (String) insModel.getValueAt(i, columnIns.get("FIRM")));
							tempList.get(k).add(1, (String) insModel.getValueAt(i, columnIns.get("ID")));
							for (int p =0; p<tempList.get(k).size();p++)
								{
								 
									temp.add(tempList.get(k).get(p));
								 
								}
							fullNettOrd +=FrameTemplate.round(Double.valueOf(tempList.get(k).get(columnTempList.get("NetAmount")+2)),2);
							fullGrossOrd +=FrameTemplate.round(Double.valueOf(tempList.get(k).get(columnTempList.get("GrossAmount")+2)),2);
							fullOrderList.add(temp);
						}
					}
				}
				cInstrList.add(new ArrayList<String>(Arrays.asList((String) insModel.getValueAt(i, columnIns.get("FIRM")),(String) insModel.getValueAt(i, columnIns.get("ID")),invDate(0),invDate(0), invDate(Integer.valueOf((String) insModel.getValueAt(i, columnIns.get("PaymentDeadline")))), (String) insModel.getValueAt(i, columnIns.get("PaymentDeadline")), String.valueOf(FrameTemplate.round(fullNettOrd, 2)).replace(".", ","),String.valueOf(FrameTemplate.round(fullGrossOrd, 2)).replace(".", ",") )));
			}
		}
		if (cInstrList.size()>0)
		{
			Object[][] modelRow = ordFrame.arrayListStringToObject( cInstrList, false, true);
			
			var invoiceList = new simpInv(true, "SIMINV_List_sys.yml", "getSIMINVTemplate", new ArrayList<String>(),
					14, "Faktury do wystawienia", new ArrayList<Integer>(Arrays.asList(1,2)), modelRow);
			
			invoiceList.addToModel(modelRow);
			invoiceList.setVisible(true);
			if (invoiceList.getCancel()==false)
			{
				ArrayList<ArrayList<String>> invList = invoiceList.getInvList();
				for (int i=1;i<invList.size();i++)
				{
					genPrePayInvoice(3, invList.get(i).get(2),invList.get(i).get(1), invList.get(i).get(3), invList.get(i).get(4), invList.get(i).get(5));
				}
			}
		}else 
		{message("Nie wybrano projektu");}
	}
	
	private void message(String text)
	{
		JOptionPane.showMessageDialog(null, text, "Informacja", JOptionPane.INFORMATION_MESSAGE);
	}
	private void genPrePayInvoice(int  invType, String instr, String firm, String docDate, String delDate, String setDate)
	{
		try {
			int row = -1;	
			////////////////////numer wiersza w modelu//////////////// 
			for (int i=0;i<insModel.getRowCount();i++)
			{
				if(insModel.getValueAt(i, columnIns.get("FIRM")).equals(firm)&&insModel.getValueAt(i, columnIns.get("ID")).equals(instr))
				{
					row = i;
					break;
				}
			}
			/////////////////////zabezpieczenia///////////////////////////////////////
			boolean isOK = checkInvoice(row,  instr, firm, invType);

			HashMap<String, ArrayList<Double>> prePayPerVat = new HashMap<String, ArrayList<Double>>();
			for (int i =0; i<VATstawki.size();i++)//pochodzi z zewn¹trz
			{
				prePayPerVat.put(VATstawki.get(i), new ArrayList<Double>(Arrays.asList(0.00, 0.00, 0.00)));
			}
			String grossV = null;
			String nettoV = null;
			String vatV = null;
			String grossVB = null;
			String nettoVB = null;
			String vatVB = null;
			String currVB = null;
			String subPostID = null;
			ArrayList<ArrayList<String>> list = null;
			if (invType==1||(invType==0&&fromCalculator.isSelected()))
			{
				nettoV = tfNettoPre.getText();
				grossV = tfGrossPre.getText();
				vatV = tfVATPre.getText();
				double nettoD =Double.valueOf(nettoV.replace(",", "."));
				double grossD = Double.valueOf(grossV.replace(",", "."));
				double vatD = Double.valueOf(vatV.replace(",","."));
				 
				if (prePayPerVat.containsKey(calVAT.getSelectedItem().toString()))
				{
					prePayPerVat.get(calVAT.getSelectedItem()).set(0, FrameTemplate.round(prePayPerVat.get(calVAT.getSelectedItem()).get(0) + nettoD,2));
					prePayPerVat.get(calVAT.getSelectedItem()).set(1, FrameTemplate.round(prePayPerVat.get(calVAT.getSelectedItem()).get(1) + vatD,2));
					prePayPerVat.get(calVAT.getSelectedItem()).set(2, FrameTemplate.round(prePayPerVat.get(calVAT.getSelectedItem()).get(2) + grossD,2));
				}
				
				
				if (nettoD<=0||grossD<=0)
				{
					message("Wartoœæ netto i brutto nie mog¹ byæ mniejsze lub równe zeru dla zlecenia: " + instr);
					isOK = false;
				}else
				{
					if (FrameTemplate.round(grossD-vatD-nettoD,2)!=0)
					{
						isOK = false;
						message("Wartoœæ netto, vat i brutto nie bilansuj¹ siê dla:" + instr);
					}
				}
			}else if (invType==0||invType==2)
			{
				var proformList = new proFormCh(invType==0 ? "IProForm_List_sys.yml" :"IPREINV_List_sys.yml", invType==0 ? "getProFormList" : "getPrePayInvList",new ArrayList<String>(Arrays.asList(instr, "THMG", firm)), 13,
				"Lista otwawrtych faktur " + (invType==0 ? "proForma" : "czêœciowych"), new ArrayList<Integer>(Arrays.asList(0,1,2)), invType==0 ? false : true);
				proformList.setVisible(true);
				 
				list = proformList.getInvoiceList();
				boolean cancel = proformList.getCancel();
				if (cancel==true)
				{
					isOK = false;
					message("Nie wybrano pozycji dla instrumentu:" + instr);
				}
			}else if (invType==3)
			{}
			else
			{
				isOK = false;
			}
			//dla koñcowej i faktury liczenie pozycji
			double revdirSum = 0.00;
			
			HashMap<String, ArrayList<String>> revDimension = new HashMap<String, ArrayList<String>>();
			HashMap<String, Double> revValue =  new HashMap<String, Double>();
			HashMap<String, Double> incValue = new HashMap<String, Double>();
			HashMap<String, Double> incValueB = new HashMap<String, Double>();
			Double vatDC = 0.00;
			Double grossDC = 0.00;
			ArrayList<ArrayList<String>> fullOrderListSec = new StoredProcedures().genUniversalArray("getOrders", new ArrayList<String>(Arrays.asList(instr, firm))); 
			HashMap<String, Integer> fullOrderMap = ordFrame.getColumnNumbers(fullOrderListSec.get(0));
			HashMap<String, Integer> ordModelMap = ordFrame.getColumnNumbers(ordModel, ordSysAll);
			
			////////////////////czêœæ techniczna////////////////////////////////
		////wartoœci do ksiêgowañ
			if ((invType==0||invType==1||invType==2||invType==3)&&isOK==true)
			{
				fullOrderListSec.get(0).add(0, "CHECK");
				for (int j=1; j<fullOrderListSec.size();j++)
				{
					for (int i =0; i<fullOrderList.size();i++)//ordModel
					{
						if (firm.equals(fullOrderListSec.get(j).get(fullOrderMap.get("FIRM")))&&
							instr.equals(fullOrderListSec.get(j).get(fullOrderMap.get("Instrument_ID")))&&
							fullOrderList.get(i).get(1).equals(instr)&&
							fullOrderList.get(i).get(0).equals(firm)&&
							fullOrderList.get(i).get(ordModelMap.get("Lp")+2).equals(fullOrderListSec.get(j).get(fullOrderMap.get("Lp"))))//ordModel.getValueAt(i, ordModelMap.get("Lp")
						{
							fullOrderListSec.get(j).add(0,fullOrderList.get(i).get(ordModelMap.get("CHECKER")+2).toString());//ordModel.getValueAt(i, ordModelMap.get("CHECKER")).toString()

							break;
						}
					}
					if (fullOrderListSec.get(j).get(0).toUpperCase().equals("TRUE"))
					{
						boolean doIt = true;
						for (String i: revDimension.keySet())
						{
							if (revDimension.get(i).get(0).equals(fullOrderListSec.get(j).get(fullOrderMap.get("SellType")+1))&&
								revDimension.get(i).get(1).equals(fullOrderListSec.get(j).get(fullOrderMap.get("OwnerOrd")+1)))
							{
								revdirSum += FrameTemplate.round(Double.valueOf(fullOrderListSec.get(j).get(fullOrderMap.get("NetAmount")+1)),2);
								double revdir = FrameTemplate.round(revValue.get(i) + Double.valueOf(fullOrderListSec.get(j).get(fullOrderMap.get("NetAmount")+1)),2);
								revValue.remove(i);
								revValue.put(String.valueOf(i), revdir);
								doIt = false;
							}
						}
						if (doIt==true)
						{

							revDimension.put(fullOrderListSec.get(j).get(fullOrderMap.get("Lp")+1), new ArrayList<String>(Arrays.asList(fullOrderListSec.get(j).get(fullOrderMap.get("SellType")+1),fullOrderListSec.get(j).get(fullOrderMap.get("OwnerOrd")+1))));
							revValue.put(fullOrderListSec.get(j).get(fullOrderMap.get("Lp")+1), FrameTemplate.round(Double.valueOf(fullOrderListSec.get(j).get(fullOrderMap.get("NetAmount")+1)),2));
							revdirSum += FrameTemplate.round(Double.valueOf(fullOrderListSec.get(j).get(fullOrderMap.get("NetAmount")+1)),2);
						}
					}
				}
				if ((invType==2||invType==3)&&isOK==true)
				{	
				Double nettoInvoice =0.00;
				if (invType==2)
				{
				double incadvSumCur = 0.00;
				double incadvSumAmt = 0.00;
				HashMap<String, Integer> fullListMap = ordFrame.getColumnNumbers(list.get(0));
				//incValue
					for (int i=1;i<list.size();i++)
					{
						incadvSumCur+=FrameTemplate.round(Double.valueOf(list.get(i).get(fullListMap.get("SUM_0")).toString().replace(",", ".")) *-1,2);
						incadvSumAmt+=FrameTemplate.round(Double.valueOf(list.get(i).get(fullListMap.get("SUM_1")).toString().replace(",", ".")) *-1,2);
						if (incValue.containsKey(list.get(i).get(fullListMap.get("SUBACCOUNT"))))
						{
							double incadv = FrameTemplate.round(incValue.get(list.get(i).get(fullListMap.get("SUBACCOUNT"))) + (Double.valueOf(list.get(i).get(fullListMap.get("SUM_0")).toString().replace(",", "."))*-1),2);
							double incadvB = FrameTemplate.round(incValueB.get(list.get(i).get(fullListMap.get("SUBACCOUNT"))) + (Double.valueOf(list.get(i).get(fullListMap.get("SUM_1")).toString().replace(",", "."))*-1),2);
							incValue.remove(list.get(i).get(fullListMap.get("SUBACCOUNT")));
							incValueB.remove(list.get(i).get(fullListMap.get("SUBACCOUNT")));
							incValue.put(list.get(i).get(fullListMap.get("SUBACCOUNT")), incadv);
							incValueB.put(list.get(i).get(fullListMap.get("SUBACCOUNT")), incadvB);
							
						}else
						{
							double incadv = FrameTemplate.round(Double.valueOf(list.get(i).get(fullListMap.get("SUM_0")).toString().replace(",", "."))*-1,2);
							double incadvB = FrameTemplate.round(Double.valueOf(list.get(i).get(fullListMap.get("SUM_1")).toString().replace(",", "."))*-1,2);
							incValue.put(list.get(i).get(fullListMap.get("SUBACCOUNT")), incadv);
							incValueB.put(list.get(i).get(fullListMap.get("SUBACCOUNT")), incadvB);
						}	
					}
					nettoInvoice = FrameTemplate.round(revdirSum-incadvSumCur,2);
					
					if (incadvSumCur==0)
					{
						isOK = false;
						message("Brak faktur zaliczkowych do rozliczenia w ramach zlecenia:" + instr);
					}
				}else if(invType==3)
				{
					nettoInvoice = FrameTemplate.round(revdirSum,2);
				}
				
				
				
				
				Double VAT = FrameTemplate.round(nettoInvoice *0.23,2);
				Double gross = FrameTemplate.round(nettoInvoice *1.23,2);
				grossV = String.valueOf(gross).replace(".", ",");
				vatV = String.valueOf(VAT).replace(".", ",");
				vatDC = VAT;
				grossDC = gross;

				prePayPerVat.get("23").set(0, FrameTemplate.round(prePayPerVat.get("23").get(0) + nettoInvoice,2));
				prePayPerVat.get("23").set(1, FrameTemplate.round(prePayPerVat.get("23").get(1) + vatDC,2));
				prePayPerVat.get("23").set(2, FrameTemplate.round(prePayPerVat.get("23").get(2) + grossDC,2));
				
				if (nettoInvoice<0)
				{
					isOK = false;
					message("Suma wystawionych zaliczek jest wy¿sza ni¿ ca³ego zlecenia:" + instr);
				}
				}
			}
			
			
			
			if (isOK==true)
			{
				ArrayList<String> updateLpList = new ArrayList<String>();
				for (int i=0;i<fullOrderList.size();i++)
				{
					if (fullOrderList.get(i).get(ordModelMap.get("CHECKER")+2).toString().toUpperCase().equals("TRUE")&&
							fullOrderList.get(i).get(0).equals(firm)&&
							fullOrderList.get(i).get(1).equals(instr))
					{
						updateLpList.add(fullOrderList.get(i).get(ordModelMap.get("Lp")+2).toString());
					}
				}
				
				String headInvType = new String();
				String subInvType = new String();
				String orderText = new String();
				posFrame.setRemoveFiltr();
				String oType = null;
				String advTableText = null;
				String wasPaid = null;
				ArrayList<ArrayList<String>> ordListInv = new ArrayList<ArrayList<String>>();
				ArrayList<ArrayList<String>> prePayPartInv = new ArrayList<ArrayList<String>>();
				
				if (invType==1)
				{
					oType ="PROFOR";
					headInvType = "Faktura PRO Forma wg zam. ";
					subInvType = "";
					orderText = "";
					ordListInv.add(fullOrderListSec.get(0));
					ordListInv.add(new ArrayList<String>(Arrays.asList("true", firm, instr, "1", (String) insModel.getValueAt(row, columnIns.get("Offer")),"", "1", "szt.", nettoV, "0.00", nettoV,nettoV,((String) calVAT.getSelectedItem()), vatV, grossV)));
					ordListInv = getInvList(ordListInv);
					advTableText = "";
					wasPaid ="DO ZAP£ATY";
				}else if (invType==0&&fromCalculator.isSelected())
				{
					oType="PREINV";
					headInvType = "Faktura ";
					subInvType = "(zaliczkowa)";
					orderText = "Specyfikacja faktury do zaliczki wg zamówienia nabywcy";
					ordListInv = getInvList(fullOrderListSec);
					advTableText = "Wartosc do zap³aty";
					wasPaid ="DO ZAP£ATY";
					prePayPartInv = mainSumList(prePayPerVat);
				}
				else if (invType==0)
				{
					oType="PROINV";
					headInvType = "Faktura ";
					subInvType = "(zaliczkowa)";
					orderText = "Specyfikacja faktury do zaliczki wg zamówienia nabywcy";
					
					ordListInv = getInvList(fullOrderListSec);
					advTableText = "Wartoœæ pobranej czêœci nale¿noœci";
					wasPaid ="ZAP£ACONO";
					prePayPartInv = mainSumList(prePayPerVat);
				}else if (invType==2)
				{
					oType="ENDINV";
					headInvType = "Faktura ";
					subInvType = "(zaliczkowa koñcowa)";
					orderText = "";
					ordListInv = getInvList(fullOrderListSec);
					advTableText = "Wartosc do zap³aty po uwzglednieniu wczesniej otrzymanych czesci nale¿nosci";
					wasPaid ="DO ZAP£ATY";
					prePayPartInv = mainSumList(prePayPerVat);
				}else if (invType==3)
				{
					oType="SIMINV";
					headInvType = "Faktura ";
					subInvType = "";
					orderText = "";
					advTableText = "";
					ordListInv = getInvList(fullOrderListSec);
					wasPaid ="DO ZAP£ATY";
				}
				
				

					
				String postingID =  new KeyAdder().PostingID(docDate, firm);
				String invoiceNumber =new KeyAdder().invoiceID(docDate, firm, oType);
				String currency = (String) insModel.getValueAt(row, columnIns.get("Currency"));
				String counterparty = (String) insModel.getValueAt(row, columnIns.get("Counterparty"));
				String conterpartyID="";
				for (int i=0;i<insDictList.size();i++)
				{
					if (counterparty.equals(insDictList.get(i).get(1)))
					{
						conterpartyID=insDictList.get(i).get(0);
					}
				}
				 
				boolean recOnInv = (boolean) insModel.getValueAt(row, columnIns.get("RecipientOnInvoice"));
				String recipient = "";
				String recipientID = "x";
				if (recOnInv==true)
				{
					recipient = (String) insModel.getValueAt(row, columnIns.get("Recipient"));
					for (int i=0;i<insDictList.size();i++)
					{
						if (recipient.equals(insDictList.get(i).get(1)))
						{
							recipientID=insDictList.get(i).get(0);
						}
					}
				}
				

					
				 
				String offer = (String) insModel.getValueAt(row, columnIns.get("Offer"));
				
				AutoAccounting posting = new AutoAccounting(firm, postingID, instr, oType);
				Object[] argList = posting.getPostingList(false);
				Object[] argQTYList = posting.getPostingList(true);
				HashMap<String, String> codeMap = posting.getCodeMap();
				ArrayList<Object[]> postingList = new ArrayList<Object[]>();
				ArrayList<Object[]> postingListQTY = new ArrayList<Object[]>();
				postingList.add(argList);
				postingListQTY.add(argQTYList);
				///Position ADD
				Object[] positionModel = {postingID, firm, docDate, delDate, setDate, invoiceNumber, currency, counterparty, true, oType};
				posModel.insertRow(0, positionModel);
				 
				posFrame.Button_Save();  ///tutaj
				///LedgerTrans
				HashMap<String, ArrayList<Object>> ledgerCode=  new HashMap<String, ArrayList<Object>>();
				HashMap<String, ArrayList<Object>> ledgerCodeQTY=  new HashMap<String, ArrayList<Object>>();
				int size_=0; 
				int sizeQ_=0; 
				HashMap<String, ArrayList<Object>> qtyCode =  new HashMap<String, ArrayList<Object>>();
		
				if (invType==1||(invType==0&&fromCalculator.isSelected()))
				{
					 
					for (String code : codeMap.keySet())
					{
					  if (invType==1)
					  {
							if (code.equals("SALREC") ||code.equals("VATPRF")||code.equals("INCPRF"))
							{	ArrayList<Object> tempList = new ArrayList<Object>();
								if (code.equals("SALREC"))
								{
									tempList.add(codeMap.get(code));
									tempList.add(grossV);
									tempList.add(false);
								}else if (code.equals("VATPRF"))
								{
									tempList.add(codeMap.get(code));
									tempList.add(vatV);
									tempList.add(true);
								}else if (code.equals("INCPRF"))
								{
									tempList.add(codeMap.get(code));
									tempList.add(nettoV);
									tempList.add(true);
								}
								ledgerCode.put(code, tempList);
		
								
							}
					  }else if (invType==0)
					  {
						  if (code.equals("SALREC") ||code.equals("VATdue")||code.equals("INCADV"))
							{	ArrayList<Object> tempList = new ArrayList<Object>();
								if (code.equals("SALREC"))
								{
									tempList.add(codeMap.get(code));
									tempList.add(grossV);
									tempList.add(false);
								}else if (code.equals("VATdue"))
								{
									tempList.add(codeMap.get(code));
									tempList.add(vatV);
									tempList.add(true);
								}else if (code.equals("INCADV"))
								{
									tempList.add(codeMap.get(code));
									tempList.add(nettoV);
									tempList.add(true);
								}
								ledgerCode.put(code, tempList);
							}
					  }
					}
					for (String code: ledgerCode.keySet())
					{
					 
						Object[] newRow = {postingID, firm, postingList.size(),docDate, delDate, setDate, invoiceNumber, ledgerCode.get(code).get(0),ledgerCode.get(code).get(1) ,ledgerCode.get(code).get(2), currency,"0","0",instr,"","",conterpartyID, "", postingID, oType, true };	
						postingList.add(newRow);
					}
					
					posting.addAutoRowsToModel(postingList, false);
					newOrdStatus("1", instr, firm,updateLpList);
				}else if(invType==0)
				{
					for (String code : codeMap.keySet())
					{
						if (code.equals("VATPRF") ||code.equals("INCPRF")||code.equals("VATdue")||code.equals("INCADV"))
						{	ArrayList<Object> tempList = new ArrayList<Object>();
							if (code.equals("VATPRF"))
							{
								tempList.add(codeMap.get(code));
								tempList.add(false);
							}else if (code.equals("VATdue"))
							{
								tempList.add(codeMap.get(code));
								tempList.add(true);
							}else if (code.equals("INCPRF"))
							{
								tempList.add(codeMap.get(code));
								tempList.add(false);
							}else if (code.equals("INCADV"))
							{
								tempList.add(codeMap.get(code));
								tempList.add(true);
							}
							ledgerCode.put(code, tempList);
						}
					}
					
					HashMap<String, Integer> columnList = posFrame.getColumnNumbers(list.get(0));
					for (int i=1; i<list.size();i++)
					{
						double VatD = FrameTemplate.round(Double.valueOf(list.get(i).get(columnList.get("VAT1")).replace(",",".")) *-1,2);
						double VatDB =	FrameTemplate.round(Double.valueOf(list.get(i).get(columnList.get("VAT2")).replace(",","."))*-1,2);
						double nettoD = FrameTemplate.round(Double.valueOf(list.get(i).get(columnList.get("amout0")).replace(",","."))*-1,2);
						double nettoDB =	FrameTemplate.round(Double.valueOf(list.get(i).get(columnList.get("amout1")).replace(",","."))*-1,2);
						double currD  = FrameTemplate.round((VatD+nettoD)/(VatDB+nettoDB),2);
						
					 
							prePayPerVat.get("23").set(0, FrameTemplate.round(prePayPerVat.get("23").get(0) + nettoD,2));
							prePayPerVat.get("23").set(1, FrameTemplate.round(prePayPerVat.get("23").get(1) + VatD,2));
							prePayPerVat.get("23").set(2, FrameTemplate.round(prePayPerVat.get("23").get(2) + VatD + nettoD,2));
						 
							prePayPartInv = mainSumList(prePayPerVat);
						currVB = String.valueOf(currD).replace(".", ",");
						for (String code: ledgerCode.keySet())
						{ 	String.valueOf(VatD).replace(".", ",");
							double check=0;
							if 		(code.equals("VATPRF")) {	nettoV = String.valueOf(VatD).replace(".", ",");
																nettoVB= String.valueOf(VatDB).replace(".", ",");
																currVB = String.valueOf(currD).replace(".", ",");
																check =VatD+ VatDB;}
							else if (code.equals("VATdue")) {	nettoV = String.valueOf(VatD).replace(".", ",");
																nettoVB = "0";   
																currVB = "0";
																check =VatD;}
							else if (code.equals("INCPRF")) {	nettoV = String.valueOf(nettoD).replace(".", ",");
																nettoVB = String.valueOf(nettoDB).replace(".", ",");
																currVB = String.valueOf(currD).replace(".", ",");
																check =nettoD+nettoDB;}
							else if (code.equals("INCADV")) {	nettoV = String.valueOf(nettoD).replace(".", ",");
																nettoVB = "0";
																currVB = "0";
																check =nettoD;}
						

							if (check!=0)
							{
							Object[] newRow = {postingID, firm, postingList.size(),docDate, delDate, setDate, invoiceNumber, ledgerCode.get(code).get(0), nettoV ,ledgerCode.get(code).get(1), currency,currVB ,nettoVB,instr,"","",conterpartyID, "", list.get(i).get(columnList.get("SUBACCOUNT")), oType, true };	
							postingList.add(newRow);
							}
						}
					}
					posting.addAutoRowsToModel(postingList, false);
					newOrdStatus("1", instr, firm,updateLpList);
				}else if (invType==2||invType==3)
				{
					for (String code : codeMap.keySet())
					{
						if (code.equals("SALREC") ||code.equals("INCADV")||code.equals("VATdue")||code.equals("REVDIR")||code.equals("warehouse")||code.equals("goodsForSale"))
						{	ArrayList<Object> tempList = new ArrayList<Object>();
						size_=8;
						sizeQ_=9;
							if (code.equals("SALREC"))
							{
								if (grossDC!=0)
								{
									tempList.add(codeMap.get(code));//0
									tempList.add(grossV);//1 //gross
									tempList.add("0.00");//2 //currency
									tempList.add("0.00");//3 //base
									tempList.add("");//4 //Dimension1
									tempList.add("");//5 //Dimension2
									tempList.add(postingID);//5 //SubAccoint
									tempList.add(false);
									ledgerCode.put(code, tempList);
								}
							}else if (code.equals("INCADV")&&invType==2)
							{
								for (String i :incValue.keySet())
								{
									if (incValueB.get(i)!=0)
									{
										tempList.add(codeMap.get(code));//0
										tempList.add(String.valueOf(incValue.get(i)).replace(".", ","));//1 //gross
										tempList.add(String.valueOf(FrameTemplate.round((incValue.get(i)/incValueB.get(i)),4)).replace(".", ","));//2 //currency
										tempList.add(String.valueOf(incValueB.get(i)).replace(".", ","));//3 //base
										tempList.add("");//4 //Dimension1
										tempList.add("");//5 //Dimension2
										tempList.add(i);//5 //SubAccoint
										tempList.add(false);
										ledgerCode.put(code, tempList);
									}
								}
							}else if (code.equals("VATdue"))
							{
								if (vatDC!=0)
									{
										tempList.add(codeMap.get(code));//0
										tempList.add(vatV);//1 //gross
										tempList.add("0.00");//2 //currency
										tempList.add("0.00");//3 //base
										tempList.add("");//4 //Dimension1
										tempList.add("");//5 //Dimension2
										tempList.add(postingID);//5 //SubAccoint
										tempList.add(true);
										ledgerCode.put(code, tempList);
									}
							}else if (code.equals("REVDIR"))
							{
								for (String i:revDimension.keySet())
								{
									if (revValue.get(i)!=0)
									{
										tempList.add(codeMap.get(code));//0
										tempList.add(String.valueOf(revValue.get(i)).replace(".", ","));//1 //gross
										tempList.add("0.00");//2 //currency
										tempList.add("0.00");//3 //base
										tempList.add(revDimension.get(i).get(0).trim());//4 //Dimension1
										tempList.add(revDimension.get(i).get(1).trim());//5 //Dimension2
										tempList.add(postingID);//5 //SubAccoint
										tempList.add(true);
										ledgerCode.put(code, tempList);
									}
								}
							}else if (code.equals("warehouse")||code.equals("goodsForSale"))
							{
								for (int i=1;i<fullOrderListSec.size();i++)
								{	
									if (updateLpList.contains(fullOrderListSec.get(i).get(fullOrderMap.get("Lp")+1)))
									{
											tempList.add(codeMap.get(code));//0
											tempList.add(String.valueOf(fullOrderListSec.get(i).get(fullOrderMap.get("Qunatity")+1)).replace(".", ","));//1 //qty
											tempList.add(code.equals("warehouse") ? true : false);//2 //ct
											tempList.add(fullOrderListSec.get(i).get(fullOrderMap.get("Unit")+1));//3 //unit
											tempList.add(String.valueOf(fullOrderListSec.get(i).get(fullOrderMap.get("UnitPrice")+1)).replace(".", ","));//4 //price
											tempList.add(fullOrderListSec.get(i).get(fullOrderMap.get("SellType")+1).trim());//5 //Dimension1
											tempList.add(fullOrderListSec.get(i).get(fullOrderMap.get("OwnerOrd")+1).trim());//6 //Dimension2
											tempList.add(fullOrderListSec.get(i).get(fullOrderMap.get("POSTINGID")+1));//7 //SubAccoint
											tempList.add(fullOrderListSec.get(i).get(fullOrderMap.get("Descriptions")+1));//8 //SubAccoint
											ledgerCodeQTY.put(code, tempList);
									}
								}
							}
						}
					}
					
					for (String code: ledgerCode.keySet())
					{
						for (int i=0; i<ledgerCode.get(code).size()/size_;i++)
						{
							int q=0; if (i>0) {q=size_*i;}
						Object[] newRow = {postingID, firm, postingList.size(),docDate, delDate, setDate, invoiceNumber, ledgerCode.get(code).get(0+q),ledgerCode.get(code).get(1+q) ,ledgerCode.get(code).get(7+q), currency,ledgerCode.get(code).get(2+q),ledgerCode.get(code).get(3+q),instr,ledgerCode.get(code).get(4+q),ledgerCode.get(code).get(5+q),conterpartyID, "", ledgerCode.get(code).get(6+q), oType, true };	
						postingList.add(newRow);
						}
					}
					posting.addAutoRowsToModel(postingList, false);
					for (String code: ledgerCodeQTY.keySet())
					{
						for (int i=0; i<ledgerCodeQTY.get(code).size()/sizeQ_;i++)
						{
						int q=0; if (i>0) {q=sizeQ_*i;}
						Object[] newRow = {postingID, firm, postingListQTY.size(),docDate, delDate, setDate, invoiceNumber, ledgerCodeQTY.get(code).get(0+q),ledgerCodeQTY.get(code).get(1+q) ,ledgerCodeQTY.get(code).get(2+q), ledgerCodeQTY.get(code).get(3+q),ledgerCodeQTY.get(code).get(4+q), instr,ledgerCodeQTY.get(code).get(5+q),ledgerCodeQTY.get(code).get(6+q),conterpartyID, ledgerCodeQTY.get(code).get(7+q), oType, true,ledgerCodeQTY.get(code).get(8+q)};	
						postingListQTY.add(newRow);
						}
					}
					posting.addAutoRowsToModel(postingListQTY, true);
					newOrdStatus("2", instr, firm,updateLpList);
				}
				///
				ArrayList<ArrayList<String>> newOrdList = new StoredProcedures().genUniversalArray("getOrdersToIncomeInvoices", new ArrayList<String>(Arrays.asList(instr, firm)));
				HashMap<String, Integer> colA = ordFrame.getColumnNumbers(newOrdList.get(0));
				boolean closeInstr = true;
				for (int i=1; i<newOrdList.size();i++)
				{
					if (!newOrdList.get(i).get(colA.get("Status")).equals("2"))
					{
						closeInstr = false;
						break;
					}
				}
				if (closeInstr==true)
				{
					insModel.setValueAt(true, row, columnIns.get("Closed"));
					insFrame.Button_Save();
				}
				ArrayList<ArrayList<String>> preInvoiceList = new ArrayList<ArrayList<String>>();
				double sumPreInv = 0.00;
				if (oType.equals("PREINV")||oType.equals("PROINV")||oType.equals("ENDINV"))
				{
					ArrayList<ArrayList<String>> tempList = new StoredProcedures().genUniversalArray("getPreInvoiceListToPDF", new ArrayList<String>(Arrays.asList(firm, instr)));
					HashMap<String, Integer> columnMap = insFrame.getColumnNumbers(tempList.get(0));
					
					if (tempList.size()>1&&(oType.equals("PREINV")||oType.equals("PROINV")))
					{
						for (int i=1;i<tempList.size();i++)
						{
							if (!tempList.get(i).get(columnMap.get("TXT")).equals(invoiceNumber))
							{
							preInvoiceList.add(new ArrayList<String>(Arrays.asList(String.valueOf(i), tempList.get(i).get(columnMap.get("DOCUMENTDATE")),tempList.get(i).get(columnMap.get("TXT")),String.format("%.2f",Double.valueOf(tempList.get(i).get(columnMap.get("SUM_"))))  )));
							sumPreInv += FrameTemplate.round(Double.valueOf(tempList.get(i).get(columnMap.get("SUM_")).replace(",", ".")),2);
							}
						}
					}
					if (list!=null&&oType.equals("PROINV")||oType.equals("ENDINV"))
					{
						HashMap<String, Integer> columnMapList = insFrame.getColumnNumbers(list.get(0));
						HashMap<String, Integer> columnMappodModel = posFrame.getColumnNumbers(posModel, posSysAll);
						for (int i =1; i<list.size();i++)
						{
							if (Double.valueOf(String.valueOf(list.get(i).get(columnMapList.get("SUM_0"))).replace(",", "."))!=0)
							{
							String date = null;
							for (int j=0;j<posModel.getRowCount();j++)
							{
								if (posModel.getValueAt(j, columnMappodModel.get("TXT")).equals(list.get(i).get(columnMapList.get("TXT"))))
								{
									date =String.valueOf(posModel.getValueAt(j, columnMappodModel.get("DOCUMENTDATE")));
									break;
								}
							}
						 
							 
							preInvoiceList.add(new ArrayList<String>(Arrays.asList(String.valueOf(preInvoiceList.size()+1), date,list.get(i).get(columnMapList.get("TXT")),String.format("%.2f",Double.valueOf(list.get(i).get(columnMapList.get("SUM_0")).replace(",", "."))*-1)     )));
							sumPreInv += FrameTemplate.round(Double.valueOf(list.get(i).get(columnMapList.get("SUM_0")).replace(",", "."))*-1,2);
							}
						}
					}
				}
				
				HashMap<String, ArrayList<Double>> amountPerVat = new HashMap<String, ArrayList<Double>>();
				for (int i =0; i<VATstawki.size();i++)
				{
					 amountPerVat.put(VATstawki.get(i), new ArrayList<Double>(Arrays.asList(0.00, 0.00, 0.00)));
				}
				double nettoSum = 0.00;
				double vatSum = 0.00;
				double grossSum = 0.00;
				HashMap<String, Integer> columnMap = insFrame.getColumnNumbers(ordListInv.get(0));
				for (int j =1; j<ordListInv.size();j++)
				{
					nettoSum += FrameTemplate.round(Double.valueOf(ordListInv.get(j).get(columnMap.get("NetAmount")).replace(",", ".")),2);
					vatSum += FrameTemplate.round(Double.valueOf(ordListInv.get(j).get(columnMap.get("VATAmount")).replace(",", ".")),2);
					grossSum += FrameTemplate.round(Double.valueOf(ordListInv.get(j).get(columnMap.get("GrossAmount")).replace(",", ".")),2);	
					String VAT =	String.valueOf(Double.valueOf(ordListInv.get(j).get(columnMap.get("VAT")).replace(",", ".")).intValue());
					
					amountPerVat.get(VAT).set(0, FrameTemplate.round(amountPerVat.get(VAT).get(0) + Double.valueOf(ordListInv.get(j).get(columnMap.get("NetAmount")).replace(",", ".")),2));
					amountPerVat.get(VAT).set(1, FrameTemplate.round(amountPerVat.get(VAT).get(1) + Double.valueOf(ordListInv.get(j).get(columnMap.get("VATAmount")).replace(",", ".")),2));
					amountPerVat.get(VAT).set(2, FrameTemplate.round(amountPerVat.get(VAT).get(2) + Double.valueOf(ordListInv.get(j).get(columnMap.get("GrossAmount")).replace(",", ".")),2));
					
				}
				
				 

				double advNetSum = 0.00;
				double advVatSum = 0.00;
				double advGrossSum = 0.00;
				for (String key:prePayPerVat.keySet())
				{
					advNetSum += FrameTemplate.round(prePayPerVat.get(key).get(0), 2);
					advVatSum += FrameTemplate.round(prePayPerVat.get(key).get(1), 2);
					advGrossSum += FrameTemplate.round(prePayPerVat.get(key).get(2), 2);
				}
				
				
				ArrayList<ArrayList<String>> codeFirms = new StoredProcedures().genUniversalArray("getCodeToInstr", new ArrayList<String>(Arrays.asList(instr ,firm)));
				ArrayList<ArrayList<String>> mailFirms = new StoredProcedures().genUniversalArray("getMailToInstr",new ArrayList<String>(Arrays.asList(instr , firm)));
				String invNote = ""; 
				HashMap<String,Integer> codeColumn = codFrame.getColumnNumbers(codeFirms.get(0));
				HashMap<String,Integer> mailColumn = mailFrame.getColumnNumbers(mailFirms.get(0));
				for (int i=1; i<codeFirms.size();i++)
				{
					if (codeFirms.get(i).get(codeColumn.get("Code_Name")).equals("InvoiceNote"))
					{
						invNote = codeFirms.get(i).get(codeColumn.get("Code_Value"));
					}
				}
				///
				ArrayList<ArrayList<String>> infoFirms = new StoredProcedures().genUniversalArray("getInfoToInvoiceTemp", new ArrayList<String>(Arrays.asList(firm, conterpartyID, recipientID)));
				HashMap<String, Integer> infFirCol = insFrame.getColumnNumbers(infoFirms.get(0));
				HashMap<String, Object> parmMap = new HashMap<String, Object>();
				parmMap.put("[|]+OTYPE[|]+", oType);
				parmMap.put("[|]+CITY[|]+", infoFirms.get(1).get(infFirCol.get("City")));
				parmMap.put("[|]+DOCUMENTDATE[|]+", docDate);
				if (oType.equals("ENDINV")||oType.equals("SIMINV"))
				{
					parmMap.put("[|]+ENDDATE[|]+", delDate);
				}else
				{
					parmMap.put("[|]+ENDDATE[|]+", "");
				}
				parmMap.put("[|]+INVTYPE[|]+", headInvType);
				parmMap.put("[|]+IVOICE_NUMBER[|]+", invoiceNumber);
				parmMap.put("[|]+TYPE[|]+", subInvType);
				parmMap.put("[|]+DUPLICATE[|]+", " ");
				parmMap.put("[|]+FIRMNAME[|]+", infoFirms.get(1).get(infFirCol.get("Companie_Name")));
				parmMap.put("[|]+FIRMSTREET[|]+", infoFirms.get(1).get(infFirCol.get("Street")) + " " + infoFirms.get(1).get(infFirCol.get("BuildingNumber")) + (infoFirms.get(1).get(infFirCol.get("ApartmentNumber")).length()>0 ? "/" : "") + infoFirms.get(1).get(infFirCol.get("ApartmentNumber")) );
				parmMap.put("[|]+FIRMCITY[|]+", (infoFirms.get(1).get(infFirCol.get("ZIPCode")) + " " + infoFirms.get(1).get(infFirCol.get("City"))));
				parmMap.put("[|]+FIRMNIP[|]+", "NIP:" + infoFirms.get(1).get(infFirCol.get("NIP")));
				parmMap.put("[|]+CONTNAME[|]+",  infoFirms.get(2).get(infFirCol.get("Companie_Name")));
				parmMap.put("[|]+CONTSTREET.", infoFirms.get(2).get(infFirCol.get("Street")) + " " + infoFirms.get(2).get(infFirCol.get("BuildingNumber")) + (infoFirms.get(2).get(infFirCol.get("ApartmentNumber")).length()>0 ? "/" : "") + infoFirms.get(2).get(infFirCol.get("ApartmentNumber")) );
				parmMap.put("[|]+CONTCITY[|]+", (infoFirms.get(2).get(infFirCol.get("ZIPCode")) + " " + infoFirms.get(2).get(infFirCol.get("City"))));
				parmMap.put("[|]+CONTNIP[|]+", "NIP:" + infoFirms.get(2).get(infFirCol.get("NIP")));
				parmMap.put("recOnInv", recOnInv);
				if (recOnInv==true && infoFirms.size()==4)
					{
						parmMap.put("[|]+RECINAME[|]+", infoFirms.get(3).get(infFirCol.get("Companie_Name")));
						parmMap.put("[|]+RECISTREET[|]+", infoFirms.get(3).get(infFirCol.get("Street")) + " " + infoFirms.get(3).get(infFirCol.get("BuildingNumber")) + (infoFirms.get(3).get(infFirCol.get("ApartmentNumber")).length()>0 ? "/" : "") + infoFirms.get(3).get(infFirCol.get("ApartmentNumber")) ); 
						parmMap.put("[|]+RECICITY[|]+", (infoFirms.get(3).get(infFirCol.get("ZIPCode")) + " " + infoFirms.get(3).get(infFirCol.get("City"))));
						parmMap.put("[|]+RECINIP[|]+", "NIP:" + infoFirms.get(3).get(infFirCol.get("NIP")));
					}
				parmMap.put("[|]+PAYMETH[|]+", "przelew");
				parmMap.put("[|]+PAYTERM[|]+", setDate);
				parmMap.put("[|]+BANK[|]+", "mBank SA");
				parmMap.put("[|]+BANKACCOUNT[|]+", "PL 19 1140 2004 0000 3502 8167 0413");
				parmMap.put("[|]+ORDERTEXT[|]+", orderText);
				parmMap.put("[|]+SUMNETTO[|]+", String.format("%.2f",Double.valueOf(nettoSum)));
				parmMap.put("[|]+SUMVAT[|]+", String.format("%.2f",Double.valueOf(vatSum)));
				parmMap.put("[|]+SUMGROSS[|]+",String.format("%.2f",Double.valueOf(grossSum)));
				if (oType.equals("PROINV"))
				{
					parmMap.put("[|]+PARTTABLETEXT[|]+", "Pobrana czêœæ nale¿noœci");
				}else if (oType.equals("ENDINV")||oType.equals("PREINV"))
				{
					parmMap.put("[|]+PARTTABLETEXT[|]+", "Zestawienie otrzymanych wczesniej czesci nale¿nosci");
				}
				parmMap.put("[|]+PARTSUM[|]+",String.format("%.2f",Double.valueOf(sumPreInv)));
				parmMap.put("[|]+ADVTABLETEXT[|]+",advTableText);
				
				parmMap.put("[|]+ADVNETSUM[|]+",String.format("%.2f",Double.valueOf(advNetSum)));
				parmMap.put("[|]+ADVTAXASUM[|]+",String.format("%.2f",Double.valueOf(advVatSum)));
				parmMap.put("[|]+ADVBRUTSUM[|]+",String.format("%.2f",Double.valueOf(advGrossSum)));
				
				parmMap.put("[|]+WASPAID[|]+",wasPaid);
				parmMap.put("[|]+FULLBRUTTOAMOUNT[|]+",String.format("%.2f",Double.valueOf(advGrossSum)));
				parmMap.put("[|]+CURRENCY[|]+",currency);
				parmMap.put("[|]+INWORDS[|]+",new InWords().inPolish(advGrossSum, currency));
				parmMap.put("[|]+NOTES[|]+",invNote);
				
				new generatePDF(parmMap, ordListInv, mainSumList(amountPerVat), preInvoiceList, prePayPartInv, firm, postingID);
			}//String inWords = new InWords().inPolish(123112125.341, "PLN");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private boolean checkInvoice(int row, String instr, String firm, int invType)
	{
		boolean closed = (boolean) insModel.getValueAt(row, columnIns.get("Closed"));
		boolean active = (boolean) insModel.getValueAt(row, columnIns.get("Active"));
		
		boolean isOK = true;
		if (row==-1)
		{
			message("Nie znaleziono zlecenia numer: " + instr);
			isOK = false;
		}
		if (closed==true)
		{
			int reply = JOptionPane.showConfirmDialog(null, "Projekt " + instr + " jest zamkniêty. Czy na pewno chcesz wystawiæ fakturê?" + (invType==1 ? "proFrome " : "zaliczkow¹ ") +"dla projektu: "  + textFieldIns.getText(), textFieldIns.getText(), JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.NO_OPTION)
			{
				isOK = false;
			}	
		}
		if (active==false)
		{
			isOK = false;
			message("Zlecenie nr: " + instr + " zosta³o anulowane. Nie mo¿na wystawiæ faktury");
		}
		//Sprawdzenie czy wszystkie ordery nie s¹ zamkniête 
		
		boolean typeOrd = false;
		for (int i=0;i<fullOrderList.size();i++)
		{
			if (invType!=3&&(fullOrderList.get(i).get(7).equals("0")||fullOrderList.get(i).get(7).equals("1"))&&fullOrderList.get(i).get(0).equals(firm)&&fullOrderList.get(i).get(1).equals(instr))
			{
				typeOrd = true;
				break;
			}else if (invType==3&&fullOrderList.get(i).get(7).equals("0")&&fullOrderList.get(i).get(0).equals(firm)&&fullOrderList.get(i).get(1).equals(instr))
			{
				typeOrd = true;
				break;
			}
		}
		if (typeOrd==false)
		{
			isOK = false;
			message("Wszystkie produkty zlecenia nr: " + instr + " maj¹ status dostarczonych. Nie mo¿na wystawiæ faktury");
		}
		
		HashMap<String, ArrayList<Double>> prePayPerVat = new HashMap<String, ArrayList<Double>>();
		for (int i =0; i<VATstawki.size();i++)
		{
			prePayPerVat.put(VATstawki.get(i), new ArrayList<Double>(Arrays.asList(0.00, 0.00, 0.00)));
		}
		 
		return isOK;
	}
	
	private ArrayList<ArrayList<String>>  getInvList(ArrayList<ArrayList<String>> tempList)
	{
		ArrayList<ArrayList<String>> newList = new ArrayList<ArrayList<String>>();
		HashMap<String, Integer> column = insFrame.getColumnNumbers(tempList.get(0));
		int lp = 1;
		for (int i = 0; i<tempList.size();i++)
			
			if (i==0||tempList.get(i).get(column.get("CHECK")).toUpperCase().contains("TRUE"))
			{
				ArrayList<String> rList = new ArrayList<String>();
		
				for (int j =0; j<tempList.get(0).size();j++)
				{
					
					if (!tempList.get(0).get(j).contains("FIRM")&&!tempList.get(0).get(j).contains("CHECK")&&!tempList.get(0).get(j).contains("Instrument_ID")&&!tempList.get(0).get(j).contains("SellType")
							&&!tempList.get(0).get(j).contains("Status")&&!tempList.get(0).get(j).contains("Status")&&!tempList.get(0).get(j).contains("CREATIONDATETIME")
							&&!tempList.get(0).get(j).contains("MODDATETIME")&&!tempList.get(0).get(j).contains("MODBY")&&!tempList.get(0).get(j).contains("CREATEBY")
							&&!tempList.get(0).get(j).contains("OwnerOrd")&&!tempList.get(0).get(j).contains("POSTINGID"))
					{
						if (i!=0&&(tempList.get(0).get(j).contains("Qunatity")||tempList.get(0).get(j).contains("UnitPrice")||tempList.get(0).get(j).contains("Discount")||
								tempList.get(0).get(j).contains("NetUnitPrice")||tempList.get(0).get(j).contains("NetAmount")
								||tempList.get(0).get(j).contains("VATAmount")||tempList.get(0).get(j).contains("VAT")||tempList.get(0).get(j).contains("GrossAmount")))
								{
									String value = tempList.get(i).get(j).replace(",", ".");
									value = String.format("%.2f",Double.valueOf(value));
									
								//	value = tempList.get(i).get(j).replace(".", InvSeparator);
									 
									rList.add(value);
								}else if (i!=0&&tempList.get(0).get(j).contains("Lp"))
								{
									rList.add(String.valueOf(lp));
									lp++;
								}
								else 
								{
									rList.add(tempList.get(i).get(j));
								}
							
					}
				}
				newList.add(rList);
			}
		return newList;
	}
	
	
	private ArrayList<ArrayList<String>> mainSumList (HashMap<String, ArrayList<Double>> temp)
	{
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		for (String key: temp.keySet())
		{
			if (temp.get(key).get(0)!=0&&temp.get(key).get(2)!=0)
			{
				result.add(new ArrayList<String>(Arrays.asList(" ", String.format("%.2f",Double.valueOf(temp.get(key).get(0))),String.format("%.2f",Double.valueOf(key)) , String.format("%.2f",Double.valueOf(temp.get(key).get(1))) , String.format("%.2f",Double.valueOf(temp.get(key).get(2)))    )));
			}
		}
		return result;
		
	}
	
	
	 
	
	 private void newOrdStatus(String status, String instr, String firm, ArrayList<String> updateKey)
	 { 
		 new ordStatusChange("IOrders_sys.yml", "getOrders", new ArrayList<String>(Arrays.asList(instr, firm)),new ArrayList<Integer>(Arrays.asList(0,1,2)), status, updateKey);
		 ordTableRun(firm, instr);
	 }
	
	private void newCalCalc()
	{
		if(calGrossNett.isSelected())
		{
			
			try {
				tfNettoPC.setText((Double.valueOf(tfNettoPC.getText().toString().replace(",", "."))).toString());
			}catch (NumberFormatException ex){
				tfNettoPre.setText(tfNettoOrd.getText().toString().replace(".", ","));
			}
		}else
		{
			try {
				tfGrossPC.setText((Double.valueOf(tfGrossPC.getText().toString().replace(",", "."))).toString());
			}catch (NumberFormatException ex){
				tfGrossPre.setText(tfGrossOrd.getText().toString().replace(".", ","));
			}
		}
	}
	
	private void getMessage()
	{
		JOptionPane.showMessageDialog(null, "Brak zaznaczonego projektu", "Informacja", JOptionPane.INFORMATION_MESSAGE);
	}
	/**
	 * Klasa menu dodatkowego
	 * @author kamil
	 *
	 */
	class scOrders extends ISimpleChange implements MouseListener, TableModelListener, ActionListener
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JTextField grossOrd;
		private JTextField nettoOrd;
		private JTextField postingOrd;
		private String firm, instr, regDate, conterparty;
		private JButton bPos, bSave;

	
		
		public scOrders(String yamlSys, String procedure, ArrayList<String> procParameters, int accesType,
				String title, ArrayList<Integer> key, String firm, String instr, String date, String conterparty) {
			super(false, yamlSys, procedure, procParameters, accesType, title, key);
			this.firm = firm;
			this.instr = instr;
			this.regDate= date;
			this.conterparty = conterparty;
			grossOrd = new JTextField();
			grossOrd.setPreferredSize(new Dimension(125,20));
			grossOrd.setEnabled(false);
			nettoOrd = new JTextField();
			nettoOrd.setPreferredSize(new Dimension(125,20));
			nettoOrd.setEnabled(false);
			postingOrd = new JTextField();
			postingOrd.setPreferredSize(new Dimension(125,20));
			postingOrd.setEnabled(false);
			bPos = new FrameTemplateButtons().RibbonJButton("Ksiegowania", FrameTemplateImageIcon.iconJButton_sbigPostring());
			bPos.addActionListener(this);
			JPanel downDownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				downDownPanel.add(new JLabel("Netto:"));
				downDownPanel.add(nettoOrd);
				downDownPanel.add(new JLabel("Brutto:"));
				downDownPanel.add(grossOrd);
				downDownPanel.add(new JLabel("ID:"));
				downDownPanel.add(postingOrd);
			downPanel.add(downDownPanel, BorderLayout.PAGE_END);
			rOption.add(frame.DefaultRibbonData());
			rOption.add(bPos);
			addTablListe();
			
			// TODO Auto-generated constructor stub
		}
		
		public void addTablListe()
		{
			table.addMouseListener(this);
			frame.setMultipleMenuRun(new ArrayList<Integer>(Arrays.asList(1,3)));
			getDict("IOrders_sys_dict.yml");
			modelColumnMap();
			model.addTableModelListener(this);
			addRibbonListener();
			calPositionValue();
			calPostingID();
			bSave = frame.getbSave();
			frame.remListbSave();
			bSave.addActionListener(this);
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
			if (e.getSource()==table)
			{
				if (table.getColumnName(table.getSelectedColumn()).equals(sysMap.get("Instrument_ID"))&&e.getClickCount()==2)
				{
					frame.dictInstrumentsbyFirm(table,  firm);
				}else if (table.getColumnName(table.getSelectedColumn()).equals(sysMap.get("Descriptions"))&&e.getClickCount()==2)
				{
					frame.dictDescriptionALL(table);
				}
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

		@Override
		public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource()==model)
			{
				if( e.getType()==0&&
					(model.getColumnName(e.getColumn()).equals(sysMap.get("Qunatity"))
					||model.getColumnName(e.getColumn()).equals(sysMap.get("UnitPrice"))
					||model.getColumnName(e.getColumn()).equals(sysMap.get("Discount"))
					||model.getColumnName(e.getColumn()).equals(sysMap.get("VAT"))))
				{
					modRow(e.getFirstRow());
					calPositionValue();
					 
				}else if (e.getType()==1)
				{
					newRow(e.getFirstRow());
				}
			}
		}
		
		private void modRow(int row)
		{
			double Discount =Double.valueOf(model.getValueAt(row, 8).toString().replace(",", ".")) ;
			double quantity = Double.valueOf(model.getValueAt(row, 5).toString().replace(",", ".")) ;  
			double Unit = Double.valueOf(model.getValueAt(row, 7).toString().replace(",", ".")) ;
			double Vat = Double.valueOf(model.getValueAt(row, 11).toString().replace(",", ".")) ;
			
			double perUnit = FrameTemplate.round(((1-Discount/100)*Unit),2);
			double netValue = FrameTemplate.round(perUnit* (double)quantity,2);
			double VatValue = FrameTemplate.round((netValue/100)* Vat,2);
			double grosValue = FrameTemplate.round(((1+Vat/100)*netValue),2);
			//double grosSec= FrameTemplate.round(netValue+VatValue,2);
			model.setValueAt(FrameTemplate.setDigits(String.valueOf(perUnit)), row, modelColumnInt.get("NetUnitPrice"));
			model.setValueAt(FrameTemplate.setDigits(String.valueOf(netValue)), row, modelColumnInt.get("NetAmount"));
			model.setValueAt(FrameTemplate.setDigits(String.valueOf(VatValue)), row, modelColumnInt.get("VATAmount"));
			model.setValueAt(FrameTemplate.setDigits(String.valueOf(grosValue)), row, modelColumnInt.get("GrossAmount"));
 
		 
		}
		
		private void newRow(int row)
		{
			model.setValueAt(firm, row, modelColumnInt.get("FIRM"));
			model.setValueAt(instr, row, modelColumnInt.get("Instrument_ID"));
			model.setValueAt(model.getRowCount(), row, modelColumnInt.get("Lp"));
			double vat = 23.00;
			model.setValueAt(FrameTemplate.setDigits(String.valueOf(vat)), row, modelColumnInt.get("VAT"));
			model.setValueAt(postingOrd.getText() , row, modelColumnInt.get("POSTINGID"));
		}
		
		private void calPositionValue()
		{
		 
				double nettoOrdV = 0;
				double grossOrdV = 0;
					for (int i=0;i<model.getRowCount();i++)
					{
						nettoOrdV += FrameTemplate.round(Double.valueOf(model.getValueAt(i, modelColumnInt.get("NetAmount")).toString().replace(",", ".")),2);
						grossOrdV += FrameTemplate.round(Double.valueOf(model.getValueAt(i, modelColumnInt.get("GrossAmount")).toString().replace(",", ".")),2);
					}
					nettoOrd.setText(String.valueOf(FrameTemplate.round(nettoOrdV,2)));
					grossOrd.setText(String.valueOf(FrameTemplate.round(grossOrdV,2)));
					
			 
		}
		
		private void calPostingID()
		{ 
			String postingid="";
			for (int i=0; i<model.getRowCount();i++)
			{
				if (model.getValueAt(i, modelColumnInt.get("POSTINGID")).toString().length()==10)
				{
					postingid =(String) model.getValueAt(i, modelColumnInt.get("POSTINGID"));
					break;
				}
			}
			postingOrd.setText(postingid);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource()==bPos)
			{	//String postingId = new KeyAdder().PostingID(, String firm)	
				seePostings();
			}else if (e.getSource()==bSave)
			{
				addPosting();
				frame.Button_Save();
			}
		}
		private void seePostings()
		{
			IPostings postings = new IPostings(500, 500, "Ksiêgowania projektu: " + instr , 
					postingOrd.getText(), firm,"NEWORD",false);
			setModal(false);
			postings.setVisible(true);
			dispose();
		}
		 
		private void addPosting()
		{
			String postingID = postingOrd.getText();
			 if (postingOrd.getText().length()!=10&&model.getRowCount()>0)
			 {
				  try 
				  {
					postingID = new KeyAdder().PostingID(regDate, firm);
					postingOrd.setText(postingID);
					for (int i = 0; i<model.getRowCount();i++)
					{
						model.setValueAt(postingID, i, modelColumnInt.get("POSTINGID"));
					}
				  } 
				  catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 if (postingOrd.getText().length()==10&&model.getRowCount()>0)
			 {
					for (int i = 0; i<model.getRowCount();i++)
					{
						model.setValueAt(postingID, i, modelColumnInt.get("POSTINGID"));
					}

				 
					//tworzê pauto accounting i dwie listy -cody instrumentów i columny
					AutoAccounting posting = new AutoAccounting(firm, postingID, instr, "NEWORD");
					Object[] argList = posting.getPostingList(true);
					HashMap<String, String> codeMap = posting.getCodeMap();
					//tworzê ju¿ konkretne ksiêgowania
					ArrayList<Object[]> postingList = new ArrayList<Object[]>();
					postingList.add(argList);
					
					//[1, 2, Lp, Data dokumentu, Data alternatywna, Data rozliczenia, Opis, Konto, Iloœæ, Ct, Jednostka, Cena jedn., Projekt, Wymiar 2, Wymiar 3, Kontrahent, SUBID, Typ, Zaksiêgowane, Komentarz]
					for (int i =0;i< model.getRowCount();i++)
					{
						for (String code : codeMap.keySet())
						{
							if (code.equals("goodsForPurchase")||code.equals("goodsForSale"))
							{
							Object[] tempList = {postingID, firm, postingList.size(), registrationDate,registrationDate,registrationDate,
									model.getValueAt(i, modelColumnInt.get("Descriptions")),codeMap.get(code),  model.getValueAt(i, modelColumnInt.get("Qunatity")), code.equals("goodsForPurchase") ? false : true,
									model.getValueAt(i, modelColumnInt.get("Unit")), model.getValueAt(i, modelColumnInt.get("UnitPrice")), instr,  model.getValueAt(i, modelColumnInt.get("SellType")),
									model.getValueAt(i, modelColumnInt.get("OwnerOrd")), conterparty,postingID,  "NEWORD", true, "Za³orzenie projektu"};
							postingList.add(tempList);
							}
						}
					}
					posting.addAutoRowsToModel(postingList, true);
					
			 }	
			 if (postingOrd.getText().length()==10 && model.getRowCount()<=0)
			 {
				 AutoAccounting posting = new AutoAccounting(firm, postingID, instr, "NEWORD");
				 posting.postingDispose();
			 }	
		}		
	}
	
	class netVatCalLis implements DocumentListener
	{
		public void insertUpdate(DocumentEvent e) {
			if (calGrossNett.isSelected()) 
			{tfCalGross();tfCalcVAT();}}
		public void removeUpdate(DocumentEvent e) {}
		public void changedUpdate(DocumentEvent e) {}
	}
	class netPCCalLis implements DocumentListener
	{
		public void insertUpdate(DocumentEvent e) {
			if (calGrossNett.isSelected()) 
			{nettoPerCent(); tfCalGross();tfCalcVAT();}}
		public void removeUpdate(DocumentEvent e) {}
		public void changedUpdate(DocumentEvent e) {}
	}
	class grossVatCalLis implements DocumentListener
	{
		public void insertUpdate(DocumentEvent e)  {
			if (calGrossNett.isSelected()==false) 
			{tfCalcNetto();tfCalcVAT();}}
		public void removeUpdate(DocumentEvent e) {}
		public void changedUpdate(DocumentEvent e) {}
	}
	class grossPCCalLis implements DocumentListener
	{
		public void insertUpdate(DocumentEvent e)  {
			if (calGrossNett.isSelected()==false) 
			{grossPerCent();tfCalcNetto();tfCalcVAT();}}
		public void removeUpdate(DocumentEvent e) {}
		public void changedUpdate(DocumentEvent e) {}
	}
	private Double tfCalGross()
	{
		String gross = "0,00";
		double grossV = 0.00;
		try {
		double taxV =  FrameTemplate.round(Double.valueOf(calVAT.getSelectedItem().toString())/100,2);
		grossV = FrameTemplate.round(Double.valueOf(tfNettoPre.getText().replace(",", ".")) * ( 1 + taxV),2) ;
		}catch (NumberFormatException e){			
		}
		gross =  String.valueOf(grossV).replace(".", ",");
		tfGrossPre.setText(gross);
		return grossV;
	}
	class preDate implements DocumentListener
	{
		public void insertUpdate(DocumentEvent e) 
		{
			
			tfInvDeliDate.setText(tfInvDocDate.getText());
			tfInvSettDate.setText(tfInvDocDate.getText());
		}
		public void removeUpdate(DocumentEvent e) {}
		public void changedUpdate(DocumentEvent e) {}
	}
	private Double tfCalcVAT()
	{
		String VAT = "0,00";
		double VatV = 0.00;
		try {
		double taxV =  FrameTemplate.round(Double.valueOf(calVAT.getSelectedItem().toString())/100,2);
		VatV = FrameTemplate.round(Double.valueOf(tfNettoPre.getText().replace(",", ".")) * taxV,2) ;
		}catch (NumberFormatException e){			
		}
		VAT = String.valueOf(VatV).replace(".", ",");
		tfVATPre.setText(VAT);
		return VatV;
	}
	
	private Double tfCalcNetto()
	{
		String netto = "0,00";
		double nettoV = 0.00;
		try {		
		double taxV =  FrameTemplate.round(Double.valueOf(calVAT.getSelectedItem().toString())/100,2);
		nettoV = FrameTemplate.round(Double.valueOf(tfGrossPre.getText().replace(",", ".")) / (1+taxV),2) ;
		}catch (NumberFormatException e){	
		}
		netto = String.valueOf(nettoV).replace(".", ",");
		tfNettoPre.setText(netto);
		return nettoV;
	}
	
	private Double nettoPerCent()
	{
		String netto = "0.00";
		double nettoV = 0.00;
		try {
		
		double perCent = FrameTemplate.round(Double.valueOf(tfNettoPC.getText().replace(",", "."))/100,2);
		double sumNetto = FrameTemplate.round(Double.valueOf(tfNettoOrd.getText().replace(",", ".")), 2);
		nettoV = FrameTemplate.round(sumNetto * perCent,2);
		}catch (NumberFormatException e){
			 
		}
		tfNettoPre.setText(String.valueOf(nettoV).replace(".", ","));
		return nettoV;
	}
	
	private Double grossPerCent()
	{
		String gross = "0.00";
		double grossV = 0.00;
		try {
			
			double perCent = FrameTemplate.round(Double.valueOf(tfGrossPC.getText().replace(",", "."))/100,2);
			double sumGross = FrameTemplate.round(Double.valueOf(tfGrossOrd.getText().replace(",", ".")), 2);
			grossV = FrameTemplate.round(sumGross * perCent,2);
		}catch (NumberFormatException e){
			 
		}
		tfGrossPre.setText(String.valueOf(grossV).replace(".", ","));
		return grossV;
	}
	
	private String invDate(int addDays)
	{
		java.util.Date date = new java.util.Date();
		java.util.Calendar c = java.util.Calendar.getInstance(); 
		c.setTime(date); 
		c.add(java.util.Calendar.DATE, addDays);
		date = c.getTime();
		String sdf = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return sdf;
	}
	

	
	class proFormCh extends ISimpleChange implements ActionListener, WindowListener, MouseListener
	{
		private JButton button;
		private boolean cancel=false;
		private ArrayList<ArrayList<String>> invoiceList;
		private boolean doubleCheck;
		private JTextField sumAMT, sumCur;
		private double dSumAmt, dSumCur;
		public proFormCh(String yamlSys, String procedure, ArrayList<String> procParameters, int accesType,
				String title, ArrayList<Integer> Key, boolean doubleCheck) {
			super(true, yamlSys, procedure, procParameters, accesType, title, Key);
			// TODO Auto-generated constructor stub
			disableEdit();
			button = new JButton("Ok");
			button.setPreferredSize(new Dimension(125,20));
			JPanel but = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			sumAMT = new JTextField();
			sumAMT.setEnabled(false);
			sumAMT.setPreferredSize(new Dimension(125,20));
			sumCur = new JTextField();
			sumCur.setEnabled(false);
			sumCur.setPreferredSize(new Dimension(125,20));
				but.add(new JLabel("Suma waluta"));
				but.add(sumCur);
				but.add(new JLabel("Suma waluta bazowa"));
				but.add(sumAMT);
				but.add(button);
				downPanel.add(but, BorderLayout.PAGE_END);
			button.addActionListener(this);
			modelColumnMap();
			tableColumnMap();
			addWindowListener(this);
			this.doubleCheck=doubleCheck;
			table.addMouseListener(this);
			
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource()==button)
				invoiceList = new ArrayList<ArrayList<String>>();
			{	ArrayList<String> column = new ArrayList<String>();
				for (int i=0; i<model.getColumnCount();i++)
				{
					column.add(sysAll.get(model.getColumnName(i)).get(0));
				}
				invoiceList.add(column);

				for (int j=0; j<model.getRowCount();j++)
				{//modelColumnInt
					if (model.getValueAt(j, modelColumnInt.get("Check")).equals(true))
					{
						ArrayList<String> temp = new ArrayList<String>();
						for (int i=0; i<model.getColumnCount();i++)
						{
							temp.add(String.valueOf(model.getValueAt(j, i)));
						}
						invoiceList.add(temp);
					}
				}
				if (invoiceList.size()<=1)
				{
					cancel=true;
				}
				dispose();
			}
		}
		
		ArrayList<ArrayList<String>> getInvoiceList()
		{
			return invoiceList;
		}
		boolean getCancel()
		{
			return cancel;
		}
		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			if (getInvoiceList()==null)
			{
				cancel=true;
			}
		}
		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub

		}
		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
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

				if (e.getSource()==table&&table.getColumnName(table.getSelectedColumn()).equals("Check"))
				{			
					if (doubleCheck==true)
					{
						boolean newCheck =  (boolean) table.getValueAt(table.getSelectedRow(), tableColumnInt.get("Check"));
						String STXT = (String) table.getValueAt(table.getSelectedRow(), tableColumnInt.get("TXT"));
					
						for (int i=0; i<table.getRowCount();i++)
						{
						if (table.getValueAt(i, tableColumnInt.get("TXT")).equals(STXT))
							{
								table.setValueAt(newCheck, i, tableColumnInt.get("Check"));
							}		
						}
					}
					sumAmount();
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
		private void sumAmount()
		{
		//	sumAMT;
		//	sumCur;
			dSumAmt=0.00;
			dSumCur=0.00;
			for (int i=0;i<table.getRowCount();i++)
			{
				if (table.getValueAt(i, tableColumnInt.get("Check")).equals(true))
				{
					dSumAmt += FrameTemplate.round(Double.valueOf(table.getValueAt(i,tableColumnInt.get("SUM_0")).toString().replace(",", ".")), 2);
					dSumCur += FrameTemplate.round(Double.valueOf(table.getValueAt(i,tableColumnInt.get("SUM_1")).toString().replace(",", ".")), 2);
				}
			}
			sumAMT.setText(String.valueOf(dSumAmt).replace(".", ","));
			sumCur.setText(String.valueOf(dSumCur).replace(".", ","));
		}
		Double getDSumAmt()
		{
			return dSumAmt;
		}
		Double getDSumCur()
		{
			return dSumCur;
		}

	}
	class simpInv extends ISimpleChange implements MouseListener, TableModelListener, ActionListener, WindowListener
	{
		private boolean cancel=false;
		private JButton button;
		private ArrayList<ArrayList<String>> okList;
		public simpInv(boolean isCheck, String yamlSys, String procedure, ArrayList<String> procParameters,
				int accesType, String title, ArrayList<Integer> Key, Object[][] dataIn) {
			super(isCheck, yamlSys, procedure, procParameters, accesType, title, Key);
			disableEdit();
			cancel=false;
			model.addTableModelListener(this);
			disableEdit();
			button = new JButton("Ok");
			button.setPreferredSize(new Dimension(125,20));
			button.addActionListener(this);
				JPanel but = new JPanel(new FlowLayout(FlowLayout.RIGHT));
				but.add(button);
			downPanel.add(but , BorderLayout.PAGE_END);
			// TODO Auto-generated constructor stub
			addWindowListener(this);
			modelColumnMap();
			for (int i=0; i<dataIn.length;i++)
			{
				model.addRow(dataIn[i]);
			}
			
		}
		
		boolean getCancel()
		{
			return cancel;
		}
		
		ArrayList<ArrayList<String>> getInvList()
		{
			return okList;
		}
		void addToModel(Object[][] dataIn)
		{

		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource()==button)
			{
				if (table.getCellEditor() != null) 
				{ 
				     table.getCellEditor().stopCellEditing(); 
				} 
				okList = new ArrayList<ArrayList<String>> ();
				frame.setRemoveFiltr();
				frame.AutSorterJTable(table, 3, false);
				ArrayList<ArrayList<String>> tempOkList =  frame.FullJTableToStringList(true, table, sysAll);
				okList.add(tempOkList.get(0));
				HashMap<String, Integer> columnMap = frame.getColumnNumbers(tempOkList.get(0));
				for (int i=1; i<tempOkList.size();i++)
				{
					if (tempOkList.get(i).get(columnMap.get("Check")).equals("1")&&!tempOkList.get(i).get(7).equals("0,0"))
					{
						okList.add(tempOkList.get(i));
					}
				}
				dispose();
				
			}
		}	

		@Override
		public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			if (e.getColumn()>-1&&e.getFirstRow()>-1)
			{
			if (sysAll.get(model.getColumnName(e.getColumn())).get(0).equals("PaymentDeadline")||sysAll.get(model.getColumnName(e.getColumn())).get(0).equals("DOCUMENTDATE"))
			{
				try {
					String date = frame.addDaysToString((String) model.getValueAt(e.getFirstRow(),modelColumnInt.get("DOCUMENTDATE")),Integer.valueOf( model.getValueAt(e.getFirstRow(),modelColumnInt.get("PaymentDeadline")).toString()))  ;
					model.setValueAt(date, e.getFirstRow(), modelColumnInt.get("SETTLEMENTDATE"));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			cancel=true;
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class ordStatusChange extends SimpleUpdate
	{

		public ordStatusChange(String yaml, String procedure, ArrayList<String> parameters, ArrayList<Integer> key, String type, ArrayList<String> updateKey) {
			super(yaml, procedure, parameters, key);
			// TODO Auto-generated constructor stub
			for (int i=0; i<model.getRowCount();i++)
			{
				if (updateKey.contains(model.getValueAt(i, columnMap.get("Lp"))))
				{
					model.setValueAt(type, i, columnMap.get("Status"));
				}
			}
			frame.Button_Save();
		}
		
	}
	
	class generatePDF 
	{
		public generatePDF(HashMap<String, Object> parmMap, ArrayList<ArrayList<String>> mainList, ArrayList<ArrayList<String>> mainSumList,ArrayList<ArrayList<String>> preInvoiceList,ArrayList<ArrayList<String>> prePayPartInv, String firm, String newPosID)
		{
			HashMap<String, String> tableList = new HashMap<String, String>();
			tableList.put("class=\"tableOrder\"", tableCreator(mainList, true, "cellOrder"));
			tableList.put("class=\"tableOrderSum\"", tableCreator(mainSumList, false, "cellOrder"));
			if (preInvoiceList.size()>0)
			{
				tableList.put("class=\"tablePartTake\"", tableCreator(preInvoiceList, false, "cellOrder"));
			}
			if (prePayPartInv.size()>0)
			{
				tableList.put("class=\"tableADVTake\"", tableCreator(prePayPartInv, false, "cellOrder"));
			}
			ArrayList<String> mew = new FlatFile().ImportFlatFile(true, "C:\\MyLittleSmt\\Szablony_HTML\\Szablon_Faktura_v1.1.html");
			FileWriter myWriter =null;
			try {
				 
				HashMap<String, ArrayList<String>> map = MainEntryDataWarehouse.getFirmsMap();
				//String inWords = new InWords().inPolish(123112125.341, "PLN");
				//inWords = new InWords().inPolish(1987.341, "PLN");
				///inWords = new InWords().inPolish(123345.341, "PLN");
				//inWords = new InWords().inPolish(343.00, "PLN");
				//inWords = new InWords().inPolish(20.00, "PLN");
				String FileName =	(parmMap.get("[|]+IVOICE_NUMBER[|]+").toString().contains("/") ?  parmMap.get("[|]+IVOICE_NUMBER[|]+").toString().replace("/", "") :(String) parmMap.get(".IVOICE_NUMBER."));
				myWriter = new FileWriter(new File("C:\\MyLittleSmt\\Szablony_HTML\\" + FileName + ".html"), StandardCharsets.UTF_8);
				//HashMap<String, String> regExList = new HashMap<String, String>();
				//regExList.put(".DOCUMENTDATE.", "2022-11-01");
				//regExList.put("[|]+CITY[|]+", "Miñsk Mazowiecki");
				//regExList.put(".ENDDATE.", "2022-11-01");
				//regExList.put(".IVOICE_NUMBER.", "Dupny");

				String table = null;
				boolean tableExist = false;
				boolean addTo = true;
				boolean sumTable = false;
				int close = 0;
				for (int i=0;i<mew.size();i++)
				{
					String str = mew.get(i);
					if ((boolean) parmMap.get("recOnInv")==false&&str.trim().equals("<div class=\"conterText\">Odbiorca:</div>"))
					{
						addTo = false;
						close = 1;
					} else if (str.trim().equals("<div class=\"tablePartTake-container\">")&&preInvoiceList.size()<=0)
					{
						addTo = false;
						close = 1;
					} else if (str.trim().equals("<div class=\"tableADVTake-container\">")&&prePayPartInv.size()<=0)
					{						
						addTo = false;
						close = 1;
					}else if (str.trim().equals("<div class=\"notatki\">")&&parmMap.get("[|]+NOTES[|]+").equals(""))
					{						
						addTo = false;
						close = 1;
					}else if (str.trim().equals("<div class=\"tit\">Data dostawy/wykonania us³ugi : |ENDDATE|</div>")&&parmMap.get("[|]+ENDDATE[|]+").equals(""))
					{						
						addTo = false;
						close = 1;
					}
					
					
					
					for (String j: tableList.keySet())
					{
						Pattern pat = Pattern.compile(j);
						Matcher mat = pat.matcher(str);
						if (mat.find())
						{
							table = tableList.get(j);
							tableExist=true;
						}
					}
				//	Pattern patSUM = Pattern.compile("class=\"tableOrderSum\"");
				//	Matcher matSUM = patSUM.matcher(str);	
				//	if (matSUM.find())
				//	{
				//		sumTable = true;
				//	}
					
					//Pattern patT = Pattern.compile("</table>");
					//Matcher matT = patT.matcher(str);					
					//if (str.trim().equals("</table>")&& table!=null&&tableExist==true)
					//{
					//	tableExist=false;
					//	myWriter.write(table+ "\n");
					//	table=null;
					//}

					for (String k: parmMap.keySet())
					{
						Pattern pat = Pattern.compile(k);
						Matcher mat = pat.matcher(str);
						if (mat.find())
						{
							String newStr = mat.replaceAll(parmMap.get(k).toString());
							str = newStr;	
						}
					}
						if (addTo==true)
						{
						myWriter.write(str + "\n");
						}else
						{
							if (str.trim().equals("</div>"))
							{
								close -= 1;
								if (close==0)
								{
									addTo=true;
								}
							}
						}
						if (str.trim().equals("</tr>")&& table!=null&&tableExist==true)
						{
							tableExist=false;
							myWriter.write(table+ "\n");
							table=null;
						}
				}
				myWriter.close();
				new PythonOther().pdfGenerator("C:\\MyLittleSmt\\Szablony_HTML\\" + FileName + ".html", "C:\\MyLittleSmt\\Szablony_HTML\\" + FileName + ".pdf");
				new DocumentsSource().newAddSource(newPosID, firm, "C:\\MyLittleSmt\\Szablony_HTML\\" + FileName + ".pdf");
			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
		}
		
		private String tableCreator(ArrayList<ArrayList<String>> tempList, boolean header, String tClass)
		{
			StringBuilder table = new StringBuilder();
			int size_ = header==true ? 1:0;
			
			
			
			for (int i=size_;i<tempList.size();i++)
			{
				table.append("<tr>" + "\n");
					for (int j=0;j<tempList.get(i).size();j++)
					{
						table.append("<td class=\"" + tClass + "\">" + tempList.get(i).get(j) + "</td>" + "\n");
					}
				
				table.append("</tr>" + "\n");
			}
			
			
			
			return table.toString();
			
		}
	}
	
}
