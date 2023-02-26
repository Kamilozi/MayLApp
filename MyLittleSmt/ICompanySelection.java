package MyLittleSmt;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class ICompanySelection extends JFrame  implements MouseListener, WindowListener {
private DefaultTableModel model; 	
private JTable table;
String[] columns;
private String FirmCode ="Firma";
private JButton Firm;
private boolean setFirmB;

	public void setF(boolean setFirm)
	{
		this.setFirmB= setFirm;
	}
	public ICompanySelection ()
	{
		
		setSize(500,650);
		setTitle("Wybór firmy");
		setLayout(new BorderLayout(0,0));
		setIconImage(FrameTemplateImageIcon.iconSys().getImage());
		try {
			FromBase();
			table = new JTable(model){
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) {                
	                return false;               
		        }};
			
			ColumnWidth();
			JScrollPane jsp = new JScrollPane(table);
			
			add(jsp, BorderLayout.CENTER);
			table.addMouseListener(this);
			addWindowListener(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setFirm(JButton Firm)
	{
		this.Firm =Firm;
		 
		
	}
	
	public static void main (String[] args) throws IOException
	{
		ICompanySelection okno = new ICompanySelection();
		okno.setVisible(true);
		okno.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	
	private void FromBase() throws IOException
	{
		var reader = new PythonBase();
		var txt = new txt();
		ArrayList<ArrayList<String>> templist = reader.FromBase(false, "ICompanySelection.yml");
		columns = txt.ArrayListToStringOne(templist.get(0));
		Object[][] data = txt.ArrayListToObject(templist);
		model = new DefaultTableModel();
		for (String str: columns)
		{
			model.addColumn(str);
		}
		for (int i = 1; i<data.length; i++)
		{
			model.addRow(data[i]);
		}
		

	}
	private void ColumnWidth()
	{
		TableColumn column;
		for (int i = 0; i<columns.length; i++)
		{
			column = table.getColumnModel().getColumn(i);
			if (i==1)
			{
				column.setPreferredWidth(100);
				
			}	else {
		        column.setPreferredWidth(50);
		    }
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount()==2)
		{
			//System.out.print(table.getValueAt(table.getSelectedRow(), 0));
			//System.out.print(FrameTemplate.getFirmCode(table.getValueAt(table.getSelectedRow(), 0).toString()));
			//FrameTemplate asd = new FrameTemplate();
			//asd.setFirmCode(table.getValueAt(table.getSelectedRow(), 0).toString());
			FirmCode = table.getValueAt(table.getSelectedRow(), 0).toString();
			Firm.setText(FirmCode);
			if (setFirmB==true)
			{
				FrameTemplate.getFirmCode(FirmCode);
			}
			//FrameTemplate.getFirmCode(FirmCode);
			//  FrameTemplate.FirmCodes = FirmCode;
		//	System.out.print(asd.getFirmCode());
		//  FrameTemplate.firm.getText()
		    setVisible(false);
		//  dispose();
		}

		
	}
	 

	
	public String getFirmCode()
	{
		return FirmCode;
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
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		getFirmCode();
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
