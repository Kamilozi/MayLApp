package MyLittleSmt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


/**
 * @author kamil
 *
 */
public class ICompany extends JFrame implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JMenuBar menuB;
	private JMenu plik, firma;
	private JMenuItem zakoncz, zapisz, otworz; 
	private JButton LZapisz, LUsun;
	private JPanel wyborfirmy, firmaszcz, inne;
	private JTable JTadress, JTFirmSzcz;
	private JScrollPane JSPadress, JSPFirmSzcz;
	private String[] modecol;
	private Object[] firms;
	private Object[][] FirmSzcz= {{"","","","","","","","","","","",false}}, moddata; 
	private JTabbedPane tabbedPane;
	//private PythonBase reader = new PythonBase();
	private DefaultTableModel modelNew;
	private Object[][] newdate;
	
	public ICompany() 
	{
		//ikona
		FrameTemplate frametemplate = new FrameTemplate();
		PythonBase reader = new PythonBase();
		ImageIcon img = frametemplate.GetImg();
		setIconImage(img.getImage());
		//ogólne
		setSize(600,300);
		setTitle("Fimry");
		setLayout(new BorderLayout(0,0));
		//actionlistener
		thehandler handler = new thehandler();
				
		
		//MenuBar
		var menuB = new JMenuBar();
			var plik = new JMenu("Plik");
				plik.add(new JSeparator());
				var zakoncz = new JMenuItem("Zakoñcz");
			plik.add(zakoncz);
		menuB.add(plik);
			var firma = new JMenu("Firma");
				var otworz =new JMenuItem("Otworz");
			firma.add(otworz);
				var zapisz = new JMenuItem("Zapisz");
			firma.add(zapisz);
		menuB.add(firma);
		setJMenuBar(menuB);
		
		//JPanelUp
		JPanel upmenuglob;
		FrameTemplate frametemplates = new FrameTemplate();
		upmenuglob = frametemplates.GetUpMenu(false);
		add(upmenuglob, BorderLayout.PAGE_START);
		//JPanelLeft
		JPanel leftmenu = new JPanel(new GridBagLayout());
		leftmenu.setBorder(new TitledBorder(""));
		GridBagConstraints gbc = new GridBagConstraints();
				LZapisz = new JButton("Zapisz");
				LZapisz.addActionListener(handler);
				LUsun = new JButton("Usuñ");
				LUsun.addActionListener(handler);

			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.gridx = 0;
			gbc.gridy = 0;
			leftmenu.add(LZapisz, gbc);
			

			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weighty = 1; 
			leftmenu.add(LUsun, gbc);
		add(leftmenu,BorderLayout.LINE_START );
		
		//add JPanelCenter
		tabbedPane = new JTabbedPane();
			wyborfirmy = new JPanel(new GridBagLayout());
			tabbedPane.add("Wybór Firmy", wyborfirmy);
			firmaszcz = new JPanel(new GridBagLayout());
			tabbedPane.add("Firma szczegó³y", firmaszcz); 
			inne = new JPanel(new GridBagLayout());
			tabbedPane.add("Inne", inne);
		add(tabbedPane);
		

		
		try {
			
			//ArrayList<ArrayList<String>> templis = reader.FromBase("ICompany.yml");
			//txt txtlist = new txt();
			//String[] columns = txtlist.ArrayListToStringOne(templis.get(0));
			//Object[][] data =  txtlist.ArrayListToObject(templis);
			//moddata = Arrays.copyOfRange(data, 1, templis.size());
			//modecol = Arrays.copyOfRange(columns, 0, 12);
			//firms = new Object[moddata.length];
			
			//DefaultTableModel model = new DefaultTableModel();
			
			//for (Object e : modecol)
			//{
			//	model.addColumn(e);
			//}

			
			// for (int i = 0; i <= moddata.length - 1; i++)
			// {
			//	 firms[i]= moddata[i][0];
			//	 Object[] elements = new Object[12];
			//	 for (int v = 0; v<=11; v++ )
			//	 	{
			//		 	if (v==11)
			//		 	{
			//		 		if (String.valueOf(moddata[i][11]).equals(String.valueOf("1")))
			//		 		{
			//		 			elements[v] = true;	
			//		 		}
			//		 		else
			//		 		{
			//		 			elements[v] = false;
			//		 		}
			//		 	}
			//		 	else
			//		 	{
			//		 	elements[v] = moddata[i][v];	
			//		 	}
			//	 	} 
			//	 model.addRow(elements); 
			// }	
			//newdate = getData();		
			//newdate, modecol
			getData();
			JTadress = new JTable(modelNew){
		        /**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) {                
	                return false;               
		        };
		        public  Class getColumnClass(int header) {
		            switch (header) {
		                case 11:
		                    return Boolean.class;
		                default:
		                    return String.class;
		            }
		        }
		    };
 
			JSPadress = new JScrollPane(JTadress);
			JTadress.setFillsViewportHeight(true);
			
			TableColumn column = null;
			for (int i = 0; i < modecol.length; i++) {
			    column = JTadress.getColumnModel().getColumn(i);
			    if (i == 2 || i==6) {
			    	column.setPreferredWidth(100); //third column is bigger
			    }else if (i == 1)
			    {
			    	column.setPreferredWidth(200);
			    }
			    else if (i==8 ||i==9 || i==10)
			    {
			    	column.setPreferredWidth(80);
			    }
			    else {
			        column.setPreferredWidth(50);
			    }
			}
			
			ListSelectionModel cellSelectionModel = JTadress.getSelectionModel();
			cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

			JTadress.addMouseListener(this);
			
			GridBagConstraints gbc1 = new GridBagConstraints();
			gbc1.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc1.fill = GridBagConstraints.BOTH;
			gbc1.gridx = GridBagConstraints.RELATIVE;
			gbc1.gridy=GridBagConstraints.RELATIVE;
			gbc1.weightx=1;
			gbc1.weighty=1;
			wyborfirmy.add(JSPadress, gbc1);
			
		
			JTFirmSzcz = new JTable(FirmSzcz, modecol) {
			    public  Class getColumnClass(int header) {
			        switch (header) {
			            case 11:
			                return Boolean.class;
			            default:
			                return String.class;
			        }
			}};
			JTFirmSzcz.getColumnModel().getColumn(0).setPreferredWidth(120);
			
			JSPFirmSzcz= new JScrollPane(JTFirmSzcz);
			
			firmaszcz.add(JSPFirmSzcz,gbc1);
			 
		//
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		
	}

	public static void main (String[] args) throws IOException
	{
		ICompany okno = new ICompany();
		okno.setVisible(true);
		okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private class thehandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Object z = e.getSource();
			if (z==LZapisz)
			{	
				try {
				JTFirmSzcz.editCellAt(-1, -1);
				PythonBase reader = new PythonBase();
				reader.ToBase(firms, FirmSzcz, modecol, "Companies");
				//newdate = getData();
				getData();
				JTadress.setModel(modelNew);
				ColumnJTadress();
				tabbedPane.setSelectedIndex(0);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}

	
	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object z = e.getSource();
		if (z==JTadress)
		{
				Object[] firmszcz = new Object[JTadress.getColumnCount()];
				for (int i = 0; i<JTadress.getColumnCount(); i++)
				{
					firmszcz[i] = JTadress.getValueAt(JTadress.getSelectedRow(),  i);
				}
				FirmSzcz[0] = firmszcz;
				if(e.getClickCount()==2)
				{
					tabbedPane.setSelectedIndex(1);
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
	
	private void getData() throws IOException
	{
		modelNew = null;
		PythonBase readers = new PythonBase();
		ArrayList<ArrayList<String>> templis = readers.FromBase("ICompany.yml");
		txt txtlist = new txt();
		String[] columns = txtlist.ArrayListToStringOne(templis.get(0));
		Object[][] data =  txtlist.ArrayListToObject(templis);
		moddata = Arrays.copyOfRange(data, 1, templis.size());
		modecol = Arrays.copyOfRange(columns, 0, 12);
		firms = new Object[moddata.length];
		
		modelNew = new DefaultTableModel();
		
	///Object[][] adrdata = new Object[moddata.length][12];
		for (Object e : modecol)
		{
			modelNew.addColumn(e);
		//	JTadress.setMod
		}
		
		 for (int i = 0; i <= moddata.length - 1; i++)
		 {
			 firms[i]= moddata[i][0];
			 Object[] elements = new Object[12];
			 for (int v = 0; v<=11; v++ )
			 	{
				 	if (v==11)
				 	{
				 		if (String.valueOf(moddata[i][11]).equals(String.valueOf("1")))
				 		{
				 			elements[v] = true;	
				 		}
				 		else
				 		{
				 			elements[v] = false;
				 		}
				 	}
				 	else
				 	{
				 	elements[v] = moddata[i][v];	
				 	}
			 	} 
			//  adrdata[i]= elements;
			modelNew.addRow(elements);
		 }
		//return modelNew;	
	
	}
	private void ColumnJTadress ()
	{
		TableColumn column = null;
		for (int i = 0; i < modecol.length; i++) {
		    column = JTadress.getColumnModel().getColumn(i);
		    if (i == 2 || i==6) {
		    	column.setPreferredWidth(100); //third column is bigger
		    }else if (i == 1)
		    {
		    	column.setPreferredWidth(200);
		    }
		    else if (i==8 ||i==9 || i==10)
		    {
		    	column.setPreferredWidth(80);
		    }
		    else {
		        column.setPreferredWidth(50);
		    }
		}
	}
	
}
 