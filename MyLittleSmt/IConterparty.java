package MyLittleSmt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import MyLittleSmtFrame.IPlanAccount;

public class IConterparty extends JFrame implements ActionListener{

	private HashMap<String, String> mapsys;
	private JTable jtplanaccount;
	private HashMap<String, ArrayList<String>> mapsysAll, mapsysdict;
	private DefaultTableModel newModel;
	private HashMap<String, String> mapsysBase;
	private FrameTemplate frametemplate;
	private ArrayList<Integer> TableKey;
	private JButton bSave, bAdd, bXLS, bCSV, bImp, bDoc;
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	public IConterparty() {
		setSize(500,750);
		setTitle("Kontrahent");
		setLayout(new BorderLayout(0,0));
		frametemplate = new FrameTemplate();
		
		setIconImage(FrameTemplateImageIcon.iconSys().getImage());
		
		JPanel upJP = new JPanel(new BorderLayout());
		upJP.add(frametemplate.GetUpMenu(false), BorderLayout.PAGE_START);
		JTabbedPane ribbon = new JTabbedPane();
			JPanel fileRIB = new JPanel(new FlowLayout(FlowLayout.LEFT));
				JPanel basicJP = new JPanel(new FlowLayout(FlowLayout.LEFT));
				basicJP.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
			JPanel ExpRIB = new JPanel(new FlowLayout(FlowLayout.LEFT));
				JPanel expJP = new JPanel(new FlowLayout(FlowLayout.LEFT));
				JPanel ImpJP = new JPanel(new FlowLayout(FlowLayout.LEFT));
				expJP.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
				ImpJP.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
					bSave = new JButton("Zapisz", FrameTemplateImageIcon.iconJButton_Save());
					bSave.setVerticalTextPosition(SwingConstants.BOTTOM);
					bSave.setHorizontalTextPosition(SwingConstants.CENTER);
					bSave.setBorderPainted(false);
					bSave.setContentAreaFilled(false);
					bSave.addActionListener(this);
					
					bAdd = new JButton("Dodaj", FrameTemplateImageIcon.iconJButton_Add());
					bAdd.setVerticalTextPosition(SwingConstants.BOTTOM);
					bAdd.setHorizontalTextPosition(SwingConstants.CENTER);
					bAdd.setBorderPainted(false);
					bAdd.setContentAreaFilled(false);
					bAdd.addActionListener(this);
				basicJP.add(bSave);
				basicJP.add(bAdd);
			fileRIB.add(basicJP);
					bXLS = new JButton("XLS", FrameTemplateImageIcon.iconJButton_XLS());
					bXLS.setVerticalTextPosition(SwingConstants.BOTTOM);
					bXLS.setHorizontalTextPosition(SwingConstants.CENTER);
					bXLS.setBorderPainted(false);
					bXLS.setContentAreaFilled(false);
					bXLS.addActionListener(this);
					
					bCSV = new JButton("Zapisz", FrameTemplateImageIcon.iconJButton_CSV());
					bCSV.setVerticalTextPosition(SwingConstants.BOTTOM);
					bCSV.setHorizontalTextPosition(SwingConstants.CENTER);
					bCSV.setBorderPainted(false);
					bCSV.setContentAreaFilled(false);
					bCSV.addActionListener(this);
					
				expJP.add(bXLS);
				expJP.add(bCSV);
			
					bImp = new JButton("Import", FrameTemplateImageIcon.iconJButton_Import());
					bImp.setVerticalTextPosition(SwingConstants.BOTTOM);
					bImp.setHorizontalTextPosition(SwingConstants.CENTER);
					bImp.setBorderPainted(false);
					bImp.setContentAreaFilled(false);
					bImp.addActionListener(this);
					
					bDoc = new JButton("Dokumentacja", FrameTemplateImageIcon.iconJButton_Documentation());
					bDoc.setVerticalTextPosition(SwingConstants.BOTTOM);
					bDoc.setHorizontalTextPosition(SwingConstants.CENTER);
					bDoc.setBorderPainted(false);
					bDoc.setContentAreaFilled(false);
					bDoc.addActionListener(this);
					
				ImpJP.add(bImp);
				ImpJP.add(bDoc);
			ExpRIB.add(expJP);
			ExpRIB.add(ImpJP);
		ribbon.add("Opcje",fileRIB);
		ribbon.add("Export", ExpRIB);
		upJP.add(ribbon, BorderLayout.PAGE_END);
		add(upJP, BorderLayout.PAGE_START);
		
		//Tworzenie list systemowych
		PythonBase sysread = new PythonBase();
		ArrayList<ArrayList<String>> syslist;
		try {
			syslist = sysread.FromBase(true, "IConterparty_sys.yml");
			mapsys = frametemplate.HashMapMask(syslist);
			mapsysAll = frametemplate.HashMapAll(syslist);
			mapsysBase = frametemplate.HashMapBase(syslist);
			//tworzenie modelu
			newModel = getData(0, mapsys, mapsysAll);
			
			//tworzenie tabeli
			jtplanaccount = frametemplate.getDefaultTable(newModel,mapsysAll);
			TableKey = new ArrayList<Integer>();
			TableKey.add(0);
			
				
			//getDict(mapsysAll, jtplanaccount);
			//dodawanie MenuBar Default
			JMenuBar menu =new JMenuBar();
			menu.add(frametemplate.DefaultJTableMenu(jtplanaccount, newModel, mapsysAll));
			setJMenuBar(menu);
			//dodawanie listenera
			frametemplate.addFrameTempListener(jtplanaccount, newModel,mapsysAll, mapsysBase, mapsys, TableKey);
			//koniec 
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
		okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private DefaultTableModel getData(int Enable, HashMap<String, String> mapsys, HashMap<String, ArrayList<String>> mapsysall) throws IOException
	{
		PythonBase readers = new PythonBase();
		FrameTemplate frametemplate = new FrameTemplate();
		ArrayList<ArrayList<String>> templist = readers.FromBase(false, "IConterparty.yml");
		txt txtlist = new txt();
		Object[][] moddata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
		String[] modcol = Arrays.copyOfRange(txtlist.ArrayListToStringOne(templist.get(0)), 0, 9);
		DefaultTableModel result =frametemplate.NewModel(Enable, modcol, moddata, mapsys, mapsysall);
		return result;
	
	}

	
	public void actionPerformed(ActionEvent e) 
	{
		Object source=e.getSource();
		
		
	}
	private void getDict (HashMap<String, ArrayList<String>> mapsysall, JTable table) throws IOException
	{

		PythonBase readers = new PythonBase();
		ArrayList<ArrayList<String>> templist = readers.FromBase(false, "IPlanAccount_dict.yml");
		txt txtlist = new txt();
		Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
		FrameTemplate frametemplate = new FrameTemplate();
	    frametemplate.CommboBoxDefault(dictdata, mapsysall, table);
			
	}
}
