package MyLittleSmtFrame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JTable;

import MyLittleSmt.MainEntryDataWarehouse;
import MyLittleSmt.PythonBase;
import MyLittleSmt.txt;

public class IBankSettings {
	public IBankSettings ()
	{  ArrayList<String> newList = new ArrayList<String>(); 
		var window = new Window(false, "BankSettings_sys.yml", "getBankSettings",new ArrayList<String>(), 21, "Ustawienia", new ArrayList<Integer>(Arrays.asList(0)));
		window.setVisible(true);
	}
	
	class Window extends ISimpleChange implements MouseListener
	{

		public Window(boolean isCheck, String yamlSys, String procedure, ArrayList<String> procParameters,
				int accesType, String title, ArrayList<Integer> Key) {
			super(isCheck, yamlSys, procedure, procParameters, accesType, title, Key);
			setListener(Key);
			addRibbonListener();
			frame.setMenuRun(6);
			frame.setMenuRun(7);
			frame.setMenuRun(8);
			frame.setMenuRun(9);
			frame.setMenuRun(10);
			frame.setGgenKey(0,7,0);
			getDict(sysAll, table);
			table.addMouseListener(this);
		
			// TODO Auto-generated constructor stub
		}
		private void getDict (HashMap<String, ArrayList<String>> mapsysall, JTable table) 
		{
			
			try {
				PythonBase readers = new PythonBase();
				ArrayList<ArrayList<String>> templist;
				templist = readers.FromBase(false, "IBankSettings_dict.yml");
				txt txtlist = new txt();
				Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
				frame.CommboBoxDefault(dictdata, mapsysall, table);	
			
			
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
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			if (e.getClickCount()==2&&e.getSource()==table)
			{
				//do zmiany FIRM musze dodaæ do DomainValues IMPORTTYPE i Obok planAccount group
				String firm = "THMG";
				HashMap<String, Integer> columnMap = frame.getColumnNumbers(table, sysAll);
				if (table.getSelectedColumn()==columnMap.get("ACCOUNTNUM"))
				{
					frame.getSelectionRunWithParameters(table,"getAccountToPostingsALL",new ArrayList<String>(Arrays.asList("1")), "Dict_PlanAccount_sys.yml", "Konto", 0);
				}else if (table.getSelectedColumn()==columnMap.get("DIMENSION"))
				{
					frame.dictInstrumentsbyFirm(table, firm);
				}else if (table.getSelectedColumn()==columnMap.get("DIMENSION_2"))
				{
					 
					frame.getSelectionRunWithParameters(table, "getDomainValue",new ArrayList<String>(Arrays.asList("'Dimension_2.Type'")), "Dict_DomainValue.yml", "Typ", 0);
				}else if (table.getSelectedColumn()==columnMap.get("DIMENSION_3"))
				{
					frame.getSelectionRunWithParameters(table, "getDomainValue",new ArrayList<String>(Arrays.asList("'Dimension_3.InternalContractor'")), "Dict_DomainValue.yml", "Kontrahent wewnêtrzny", 0);
				
				}else if (table.getSelectedColumn()==columnMap.get("DIMENSION_4"))
				{
					frame.dictCounterpartyNameFirm(table);
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
		
	}
}

