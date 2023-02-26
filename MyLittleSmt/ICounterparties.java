package MyLittleSmt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.CellEditor;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class ICounterparties extends FrameTemplateWindow implements MouseListener, ActionListener, ChangeListener{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private FrameTemplate ImportData, ImportMain, ImportAdress, ImportContact;
private JPanel SimOpt, OptExp, OptImp;
private HashMap<String, ArrayList<String>> mainSysAll, adressSysAll, contactSysAll;
private HashMap<String,String> mainSys, mainSysBase, adressSys, adressSysBase, contactSysBase, contactSys;
private DefaultTableModel MainModel, AdressModel, ContactModel;
private JTabbedPane jtpJTable;
private JPanel ROption, RExport, mainJP,  adressJP, contactJP;
private JTabbedPane Ribbon;
private JTable adressTable, MainTable, ContactTable;
private JButton AddButton, bSave, ImportSave, AddContact, AddContactSec;
private JScrollPane AdressJSP, MainJSP, ContactJSP;
private ArrayList<Integer> AdressTableKey, MainTableKey, ContactTableKey;

	public ICounterparties(int x, int y, String title) 
	{
		super(x, y, title);
		
		ImportData = new FrameTemplate();
		ImportMain = new FrameTemplate();
		ImportAdress = new FrameTemplate();
		ImportContact = new FrameTemplate();
		
		
			JPanel upPanel = new JPanel(new BorderLayout());
				upPanel.add(ImportData.GetUpMenu(false), BorderLayout.PAGE_START);
				Ribbon = new JTabbedPane();
					ROption = new JPanel(new FlowLayout(FlowLayout.LEFT));
						JPanel customSim = new JPanel();
							bSave = new FrameTemplateButtons().RibbonJButton("Zapisz wszystko", FrameTemplateImageIcon.iconJButton_SaveAll());
						customSim.add(bSave);
					ROption.add(customSim);
					customSim.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
				Ribbon.add("Opcje", ROption);
					RExport = new JPanel(new FlowLayout(FlowLayout.LEFT));
				Ribbon.add("Export", RExport);
			upPanel.add(Ribbon, BorderLayout.PAGE_END);
		add(upPanel, BorderLayout.PAGE_START);

		ImportAdress.JTableHelperSys("IConterparty_sys.yml");
		adressSysAll = ImportAdress.getMapSysAll();
		adressSysBase = ImportAdress.getMapSysBase();
		adressSys = ImportAdress.getMapSys();
		AdressModel = getAdressModell("I0000001");
		AdressTableKey = new ArrayList<Integer>(Arrays.asList(0,8));	
		adressTable = ImportAdress.getDefaultTable(AdressModel , adressSysAll);
		
		ImportMain.JTableHelperSys("IConterparty_sys_Main.yml");
		mainSysAll = ImportMain.getMapSysAll();
		mainSysBase = ImportMain.getMapSysBase();
		mainSys = ImportMain.getMapSys();
		MainModel = getMainModell();
		MainTableKey = new ArrayList<Integer>(Arrays.asList(0,3));			
		MainTable = ImportMain.getDefaultTable(this.MainModel, mainSysAll);
		MainTable.setCellSelectionEnabled(false);
		MainTable.setRowSelectionAllowed(true);		
		MainTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		MainTable.addMouseListener(this);
		
		ImportContact.JTableHelperSys("IConterparty_sys_Contact.yml");
		contactSysAll = ImportContact.getMapSysAll();
		contactSysBase = ImportContact.getMapSysBase();
		contactSys = ImportContact.getMapSys();
		ContactModel = getContactModell("I0000001");
		ContactTable = ImportContact.getDefaultTable(ContactModel, contactSysAll);
		ContactTableKey = new ArrayList<Integer>(Arrays.asList(0,2));	
		
		jtpJTable = new JTabbedPane();
			mainJP = new JPanel(new BorderLayout());
				MainJSP = new JScrollPane(this.MainTable);
			mainJP.add(MainJSP, BorderLayout.CENTER);
			adressJP = new JPanel(new BorderLayout());
				AdressJSP = new JScrollPane(adressTable);
			adressJP.add(AdressJSP, BorderLayout.CENTER);
			contactJP = new JPanel(new BorderLayout());
				ContactJSP = new JScrollPane(ContactTable);
			contactJP.add(ContactJSP);
			
		jtpJTable.add("G³ówna" ,mainJP);
		jtpJTable.add("Adres", adressJP);
		jtpJTable.add("Kontakt", contactJP);
		add(jtpJTable, BorderLayout.CENTER);
		jtpJTable.addChangeListener(this);
		
		mainListeners();
		adressListeners();
		contactListeners();
		setRibbonMain();
		pack();
	}

	
	private void mainListeners()
	{
		ImportMain.addFrameTempListener(MainTable, MainModel, mainSysAll, mainSysBase, mainSys , MainTableKey);
		//ImportMain.addContextMenu(MainTable);
		ImportMain.addListenerContextMenu();
		ImportMain.addListenerJTable(MainTable ,MainModel);
		ImportMain.getpasteM().setEnabled(false);
		ImportMain.getaddRowM().setEnabled(false);
		ImportMain.getdeleteM().setEnabled(false);
		
		
	}
	
	private void adressListeners() 
	{
		ImportAdress.addFrameTempListener(adressTable, AdressModel, adressSysAll, adressSysBase, adressSys, AdressTableKey);
		ImportAdress.addListenerContextMenu();
		ImportAdress.addListenerJTable(adressTable , AdressModel);
		ImportAdress.removeKeyListener(adressTable);
		ImportAdress.setGgenKey(0,0,0);
		
	}
	
	private void contactListeners()
	{
		ImportContact.addFrameTempListener(ContactTable, ContactModel, contactSysAll, contactSysBase, contactSys, ContactTableKey);
		ImportContact.addListenerContextMenu();
		ImportContact.addListenerJTable(ContactTable , ContactModel);
		ImportContact.removeKeyListener(ContactTable);
	    ImportContact.setGgenKey(0,1,0);

	}
	
	private void setRibbon()
	{
		if (jtpJTable.getSelectedIndex()==1)
		{
			setRibbonAdress();	
		}
		else if (jtpJTable.getSelectedIndex()==0)
		{			
			setRibbonMain();
		}
		else if (jtpJTable.getSelectedIndex()==2)
		{
			setRibbonContact();

		}
	}
	


	private void setRibbonAdress()
	{	
		removeRibbon();
			SimOpt = ImportAdress.DefaultRibbonSim();
				AddButton = ImportAdress.getbAdd();
				AddButton.setEnabled(false);
			OptExp = ImportAdress.DefaultRibbonExp();
			OptImp = ImportAdress.DefaultRibbonImp();
		ROption.add(SimOpt);
		RExport.add(OptExp);
		RExport.add(OptImp);
			ImportAdress.getbImp().setEnabled(true);
			ImportAdress.getbDoc().setEnabled(true);
			ImportAdress.getbSave().setEnabled(true);
			ImportAdress.getbSave().addActionListener(this);
			ImportAdress.addListenerRibbon();

		bSave.setEnabled(true);
		ImportSave = ImportAdress.getbSave();
		ImportSave.addActionListener(this);
		Ribbon.setSelectedIndex(1);
		Ribbon.setSelectedIndex(0);
		
	}	
	private void setRibbonMain()
	{
		removeRibbon();
	
			SimOpt = ImportAdress.DefaultRibbonSim();
			OptExp = ImportMain.DefaultRibbonExp();
			ImportAdress.getbSave().setEnabled(false);
			
		ROption.add(SimOpt);
		RExport.add(OptExp);
		AddButton = ImportAdress.getbAdd();
		AddButton.setEnabled(true);
		AddButton.addActionListener(this);
			ImportAdress.addListenerRibbon();
			ImportMain.addListenerRibbon();
		bSave.setEnabled(false);
		Ribbon.setSelectedIndex(1);
		Ribbon.setSelectedIndex(0);
			
	}
	private void setRibbonContact()
	{
		removeRibbon();
			SimOpt = ImportContact.DefaultRibbonSim();
			OptExp = ImportContact.DefaultRibbonExp();
			OptImp = ImportContact.DefaultRibbonImp();
				AddContactSec = new FrameTemplateButtons().RibbonJButton("Dodaj", FrameTemplateImageIcon.iconJButton_Add());
				AddContactSec.addActionListener(this);
			SimOpt.add(AddContactSec);
		ROption.add(SimOpt);
		RExport.add(OptExp);
		RExport.add(OptImp);
		ImportContact.addListenerRibbon();
		AddContact = ImportContact.getbAdd();
		AddContact.setVisible(false);

		Ribbon.setSelectedIndex(1);
		Ribbon.setSelectedIndex(0);
	//	ImportContact.remListbAdd();
	//	AddContact = ImportContact.getbAdd();
		//AddContact.addActionListener(this);
	}
	
	private void removeRibbon()
	{
		if (SimOpt!=null)
		{
			ROption.remove(SimOpt);
		 }
		if (OptExp!=null)
		{
			RExport.remove(OptExp);
		}
		if (OptImp!=null)
		{
			RExport.remove(OptImp);
		}
	}
	
	private DefaultTableModel getMainModell()  
	{
			StoredProcedures sp = new StoredProcedures();
			DefaultTableModel Model = sp.genModelConterpartiesMainTable(1,mainSys, mainSysAll);
			return Model;
	}
	
	private DefaultTableModel getAdressModell(String ID)
	{
			StoredProcedures sp = new StoredProcedures();
			DefaultTableModel Model =sp.genModelConterparties(ID, 2, adressSys, adressSysAll);
			return Model;
	}
	private DefaultTableModel getContactModell(String ID)
	{
			StoredProcedures sp = new StoredProcedures();
			DefaultTableModel Model =sp.genModelConterpartiesContact(ID, 3, contactSys, contactSysAll);
			return Model;
	}
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getSource()==MainTable)
		{
	        if(e.getClickCount()==2)
	        	{
				 	String ConterpartiesID = String.valueOf(MainTable.getValueAt(MainTable.getSelectedRow(), 0));
				 	AdressModel = getAdressModell(ConterpartiesID);
				 	adressTable.setModel(AdressModel);
				 	adressListeners();
				 	ContactModel = getContactModell(ConterpartiesID);
				 	ContactTable.setModel(ContactModel);
				 	contactListeners();
				 	jtpJTable.setSelectedIndex(1);
				 	setRibbonAdress();
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
		
		if (jtpJTable.getSelectedIndex()==0 &&
			e.getSource()==AddButton)	
		{
			AdressModel = getAdressModell(" ");
			adressTable.setModel(AdressModel);
		 	adressListeners();
		 	ImportAdress.Button_addEmptyRow(AdressModel, adressSysAll, adressTable);
		 	jtpJTable.setSelectedIndex(1);
		 	setRibbonAdress();
		 	
		}
		else if (e.getSource()==ImportSave)
		{
			ImportMain.SortFals();
			MainModel= getMainModell();
			MainTable.setModel(MainModel);
			mainListeners();
		} 
		else if (e.getSource()==AddContactSec)
		{	
			
		
			ImportContact.Button_addEmptyRow(ContactModel, contactSysAll, ContactTable);
			int rownum=ImportContact.getAddRow();
			String conId = String.valueOf(adressTable.getValueAt(0, 0));
			if (conId!="" )
			{
				ContactModel.setValueAt(conId, rownum, 1);
			}
		}
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource()==jtpJTable)
		{
			setRibbon();
		}
	}

}
