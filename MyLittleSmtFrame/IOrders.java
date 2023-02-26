package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateWindow;
import MyLittleSmt.PythonBase;
import MyLittleSmt.StoredProcedures;
import MyLittleSmt.txt;

public class IOrders  extends FrameTemplateWindow implements MouseListener, TableModelListener {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private FrameTemplate orderFrame;
private HashMap<String, ArrayList<String>> orderSysAll;
private HashMap<String, String> orderSysBase, orderSys;
private DefaultTableModel Model; 
private JTable orderTable;
private JPanel rOption, rExport,ribbonOption, ribbonExport, ribbonImport, ribbonData;
private String Firm, InsId;
private HashMap<Integer, String> dictColumnAndYaml;
private ArrayList<Integer> orderKey;

private boolean canI = true;
	public IOrders(int x, int y, String title,String InsId, String Firm) {
		super(x, y, title);
		orderFrame = new FrameTemplate();
		this.Firm=Firm;
		this.InsId=InsId;
		JPanel upPanel = new JPanel(new BorderLayout());
		upPanel.add(orderFrame.GetUpMenu(false), BorderLayout.PAGE_START);
			JTabbedPane jtp = new JTabbedPane();
				rOption = new JPanel(new FlowLayout(FlowLayout.LEFT));
				rExport = new JPanel(new FlowLayout(FlowLayout.LEFT));
					orderRibbon();
				jtp.add("Opcje" , rOption);
				jtp.add("Eksport", rExport);
			upPanel.add(jtp, BorderLayout.PAGE_END);
		add(upPanel,BorderLayout.PAGE_START);	
		if (Firm=="")
		{
		Firm = orderFrame.getBFirm().getText();
		}
		orderFrame.JTableHelperSys("IOrders_sys.yml");
		orderSys = orderFrame.getMapSys();
		orderSysBase = orderFrame.getMapSysBase();
		orderSysAll = orderFrame.getMapSysAll();
		Model = getOrderModel(InsId,Firm);
		orderTable= orderFrame.getDefaultTable(Model, orderSysAll);
		dictColumnAndYaml = new HashMap<Integer, String>();
		dictColumnAndYaml.put(1, "Orders_Dict_Model.yml");
		orderKey = new ArrayList<Integer>(Arrays.asList(0,1,2));
		
		getDict(orderSysAll, orderTable);
		JPanel downPanel = new JPanel(new BorderLayout(5,5));
			downPanel.add(new JScrollPane(orderTable),BorderLayout.CENTER);
		add(downPanel);
			
		setOrdersListeners();
		 
		
		
	}
	
	private void setOrdersListeners()
	{
		orderFrame.addFrameTempListener(orderTable, Model, orderSysAll, orderSysBase, orderSys, orderKey);
		orderFrame.addListenerJTable(orderTable, Model);
		//orderFrame.setMenuRun(1);
		//orderFrame.setMenuRun(3);
		orderFrame.addListenerRibbon();
		orderFrame.getbAdd().setEnabled(false); //blokada
		orderFrame.getbSave().setEnabled(false);//nn
		orderFrame.getbDup().setEnabled(false);//nn
		orderFrame.addListenerContextMenu();
		orderFrame.isEditContextMenu(false);//n
		orderFrame.getpasteM().setEnabled(false);//n
		orderTable.addMouseListener(this);
		orderFrame.addFirm(0, orderFrame.getBFirm());
		Model.addTableModelListener(this);
	}
	
	private void orderRibbon()
	{
		ribbonOption = orderFrame.DefaultRibbonSim();
		ribbonExport = orderFrame.DefaultRibbonExp(); 
		//ribbonImport = orderFrame.DefaultRibbonImp();
		ribbonData = orderFrame.DefaultRibbonData();
	 
	
	rExport.add(ribbonExport);
	//rExport.add(ribbonImport);
	rOption.add(ribbonOption);
	rOption.add(ribbonData);
//	rOption.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));BorderFactory.createLineBorder(Color.gray);
	}
	private DefaultTableModel getOrderModel(String ID, String Firm)
	{
		StoredProcedures sp = new StoredProcedures();
		if (ID=="")
		{
			return sp.genModelOrdersAll(Firm, 1, orderSys, orderSysAll);//5
		}else
		{
			return sp.genModelOrders(ID ,Firm, 5, orderSys, orderSysAll);
		}
		
		
	}
	private void getDict (HashMap<String, ArrayList<String>> mapsysall, JTable table) 
	{
		
		try {
			PythonBase readers = new PythonBase();
			ArrayList<ArrayList<String>> templist;
			templist = readers.FromBase(false, "IOrders_sys_dict.yml");
			txt txtlist = new txt();
			Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
			orderFrame.CommboBoxDefault(dictdata, mapsysall, table);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==orderTable)
		{
			if(e.getClickCount()==2 )
			{
				if (orderTable.getSelectedColumn()==1)
				{
					ISelectionRun jdialog = new ISelectionRun("Orders_Dict_Model_Sys.yml", "Orders_Dict_Model.yml", "Projekt", 1);
					orderTable.setValueAt(jdialog.getSelection(), orderTable.getSelectedRow(), orderTable.getSelectedColumn());

				}else if (orderTable.getSelectedColumn()==3)
				{
					ISelectionRun jdialogTXT = new ISelectionRun("OrdersTXT_Dict_Model_Sys.yml", "Orders_TXT_Dict_Model.yml", "Opis", 1);
					orderTable.getCellEditor().stopCellEditing();
					orderTable.setValueAt(jdialogTXT.getSelection(), orderTable.getSelectedRow(), 3);
				}
				
				
			}
			canI=true; 
		}
		
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
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getType()==1)
		{
			canI=false;
			newLine(e.getFirstRow());
			 
		}else if(e.getType()==0 && canI==true)
		{
		 	ModLine(e.getFirstRow(), e.getColumn());
		}
	}
	
	private void newLine(int newRow)
	{
		
		if (Firm!="firma")
		{
		
			Model.setValueAt(Firm, newRow, 0);
			
		}
		if (InsId!="")
		{
			Model.setValueAt(InsId, newRow, 1);
			Model.setValueAt(Model.getRowCount(), newRow, 2);
		}
		Model.setValueAt(0, newRow, 15);
		Model.setValueAt(FrameTemplate.setDigits("23.00"), newRow, 11);
	} 
	private void ModLine(int row, int column)
	{
	
		double Discount =Double.valueOf(Model.getValueAt(row, 8).toString().replace(",", ".")) ;
		double quantity = Double.valueOf(Model.getValueAt(row, 5).toString().replace(",", ".")) ;  
		double Unit = Double.valueOf(Model.getValueAt(row, 7).toString().replace(",", ".")) ;
		double Vat = Double.valueOf(Model.getValueAt(row, 11).toString().replace(",", ".")) ;
		
		if (column==5||column==8||column==7||column==11)
		{	//=ZAOKR((1-H776/100)*G776;2)
		//	String Value =  FrameTemplate.setDigits(String.valueOf((1-Discount/100)* Unit)*quantity);
			double perUnit = FrameTemplate.round(((1-Discount/100)*Unit),2);
			//=ZAOKR(I772*E772;2)
			double netValue = FrameTemplate.round(perUnit* (double)quantity,2);
			//=ZAOKR((K772/100)*J772;2)
			double VatValue = FrameTemplate.round((netValue/100)* Vat,2);
			//=ZAOKR((1+K772/100)*J772;2)
			double grosValue = FrameTemplate.round(((1+Vat/100)*netValue),2);
			double grosSec= FrameTemplate.round(netValue+VatValue,2);
			Model.setValueAt(FrameTemplate.setDigits(String.valueOf(perUnit)), row, 9);
			Model.setValueAt(FrameTemplate.setDigits(String.valueOf(netValue)), row, 10);
			Model.setValueAt(FrameTemplate.setDigits(String.valueOf(VatValue)), row, 12);
			Model.setValueAt(FrameTemplate.setDigits(String.valueOf(grosValue)), row, 13);
			//System.out.print(netValue+VatValue);
			if (grosSec!=grosValue)
			{
				JOptionPane.showMessageDialog(null, "VAT+Netto!=brutto","Informacja", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}
