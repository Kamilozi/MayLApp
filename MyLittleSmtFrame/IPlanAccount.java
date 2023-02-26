package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.FrameTemplateTable;
import MyLittleSmt.PythonBase;
import MyLittleSmt.txt;

public class IPlanAccount extends JFrame implements ActionListener {

private JPanel leftpanel, centerpanel;
private JButton bZapisz, bDodaj, bCSV, bDoc, bImp;
private JButton bXLS;
private JTable tglowna, tpomoc;
private JScrollPane JSPglowna, JSPpomoc;
private HashMap<String, String> mapsys, mapsysBase;
private JTable jtplanaccount;
private HashMap<String, ArrayList<String>> mapsysAll, mapsysdict;
private DefaultTableModel newModel;
private FrameTemplate frametemplate;
private ArrayList<Integer> TableKey;


public IPlanAccount() 
{
	setSize(500,750);
	setTitle("Plan kont");
	setLayout(new BorderLayout(0,0));
	this.frametemplate = new FrameTemplate();
	
	JPanel up = new JPanel(new BorderLayout());
	
	setIconImage(FrameTemplateImageIcon.iconSys().getImage());
	up.add(frametemplate.GetUpMenu(false), BorderLayout.PAGE_START);
	

	JTabbedPane upJTP = new JTabbedPane();
		JPanel plikJP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel ImpExpJP = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JPanel simJP = frametemplate.DefaultRibbonSim();
			JPanel expJP = frametemplate.DefaultRibbonExp();
			JPanel impJP = frametemplate.DefaultRibbonImp();
				
		plikJP.add(simJP);
		ImpExpJP.add(expJP);
		ImpExpJP.add(impJP);	
	upJTP.add("Opcje", plikJP);
	upJTP.add("Export", ImpExpJP);
	up.add(upJTP, BorderLayout.PAGE_END);
	add(up, BorderLayout.PAGE_START);
	
	centerpanel = new JPanel();
		JSPglowna = new JScrollPane();
		tglowna = new JTable();

	add(centerpanel, BorderLayout.CENTER);

	//Tworzenie list systemowych
	PythonBase sysread = new PythonBase();
	ArrayList<ArrayList<String>> syslist;
	try {
		syslist = sysread.FromBase(true, "IPlanAccount_sys.yml");
		mapsys = frametemplate.HashMapMask(syslist);
		
		mapsysAll = frametemplate.HashMapAll(syslist);
		mapsysBase = frametemplate.HashMapBase(syslist);
		TableKey = new ArrayList<Integer>();
		TableKey.add(0);
		TableKey.add(8);
		newModel = getData(0, mapsys, mapsysAll);
		
		//tworzenie tabeli
		jtplanaccount = frametemplate.getDefaultTable( newModel, mapsysAll);
		getDict(mapsysAll, jtplanaccount);

		//dodawanie MenuBar Default
		JMenuBar menu =new JMenuBar();
		menu.add(frametemplate.DefaultJTableMenu(jtplanaccount, newModel, mapsysAll));
		setJMenuBar(menu);
		//dodawanie listenera (na zewn¹trz dopiero gdy wszystkie elementy s¹ gotowe)
		frametemplate.addFrameTempListener(jtplanaccount, newModel,mapsysAll, mapsysBase, mapsys, TableKey);
		//koniec 
		frametemplate.addListenerUpMenu();
		frametemplate.addListenerContextMenu();
		frametemplate.addListenerRibbon();
		frametemplate.addListenerJTable(jtplanaccount, newModel);
		JScrollPane jspplanaccount = new JScrollPane(jtplanaccount);
		add(jspplanaccount);
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	

	
	
}
	public static void main (String[] args)
	{
		IPlanAccount okno = new IPlanAccount();
		okno.setVisible(true);
		okno.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		Object z = e.getSource();
		if (z==bZapisz)
		{
			frametemplate.Button_Save();
		}else if (z==bDodaj)
		{
			frametemplate.Button_addEmptyRow(newModel, mapsysAll, jtplanaccount);	
		}else if (z==bXLS)
		{
			frametemplate.Button_XLS(newModel);
		}else if (z==bCSV)
		{
			frametemplate.Button_CSV(newModel);
		}
		else if (z==bImp)
		{
			frametemplate.Button_Import(mapsysAll);
		}
		else if (z==bDoc)
		{
			frametemplate.Button_Docum(mapsysAll, jtplanaccount);
		}
	}

	private DefaultTableModel getData(int enable, HashMap<String, String> mapsys, HashMap<String, ArrayList<String>> mapsysall) throws IOException
	{
		
		PythonBase readers = new PythonBase();
		String query = "IPlanAccount.yml";
		return readers.getDataToJT(enable, mapsys, mapsysall, query);
		
	}

	private void getDict (HashMap<String, ArrayList<String>> mapsysall, JTable table) throws IOException
	{
		PythonBase readers = new PythonBase();
		ArrayList<ArrayList<String>> templist = readers.FromBase(false, "IPlanAccount_dict.yml");
		txt txtlist = new txt();
		Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
	    frametemplate.CommboBoxDefault(dictdata, mapsysall, table);	
	}
}

class PlanAccountCode extends FrameTemplateTable
{

	public PlanAccountCode(String sysYaml, String procedure, ArrayList<String> procParameters, int enableType) {
		super(sysYaml, procedure, procParameters, enableType);
		 
	}
	
}
