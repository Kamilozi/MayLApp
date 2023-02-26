package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.FrameTemplateWindow;
import MyLittleSmt.PythonBase;


public class ISelection extends FrameTemplateWindow implements  MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FrameTemplate ImportMain;
	private HashMap<String, String> mapSysMain, mapSysBaseMain;
	private HashMap<String, ArrayList<String>> mapSysAllMain;
	private DefaultTableModel ModelMain;
	private JTable tableMain;
	private String selection;
	private int columnMain;
	public ISelection (String yamlSys, String yamlData, String title, int getcolumnMain)
	{
		super(500,500, title);
		setIconImage(FrameTemplateImageIcon.iconSys().getImage());
		columnMain=getcolumnMain;
		
		ImportMain = new FrameTemplate();
		ImportMain.JTableHelperSys(yamlSys);
		mapSysMain =ImportMain.getMapSys();
		mapSysBaseMain = ImportMain.getMapSysBase();
		mapSysAllMain = ImportMain.getMapSysAll();
		ModelMain =  getModel(yamlData);
		tableMain = ImportMain.getDefaultTable(ModelMain, mapSysAllMain);
		tableMain.setCellSelectionEnabled(false);
		tableMain.setRowSelectionAllowed(true);		
		tableMain.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		JPanel panelMain = new JPanel(new BorderLayout(5,5));
			JTabbedPane jtp = new JTabbedPane();
				JPanel main = new JPanel(new BorderLayout(5,5));
					main.add(new JScrollPane(tableMain),BorderLayout.CENTER);
				jtp.add("G³ówna", main);
			panelMain.add(jtp);
		add(panelMain,BorderLayout.CENTER);
			
				
	
	}
	public void main ()
	{

	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

	}

	private DefaultTableModel getModel(String yamlData)
	{
		try {
			PythonBase readers = new PythonBase();
			return readers.getDataToJT(1, mapSysMain, mapSysAllMain, yamlData);
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==tableMain)
		{
	       if(e.getClickCount()==2)
	        {
	    	   selection = String.valueOf(tableMain.getValueAt(tableMain.getSelectedRow(), columnMain));
	    	
	    	   this.dispose();
	        }
	        
	     }
	}
	
	public String getSelection()
	{
		return selection;
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
