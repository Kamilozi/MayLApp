package MyLittleSmtFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTable;

import MyLittleSmt.MainEntryDataWarehouse;
import MyLittleSmt.PythonBase;
import MyLittleSmt.txt;

public class IBankAccount {
	private String firm;
	public IBankAccount(String firm)
	{
		this.firm = firm;
		JDialog window = new window(false, "BankAccount_sys.yml", "getBankAccountByFirm", new ArrayList<String>(Arrays.asList(firm)), 17, "Konta bankowe", new ArrayList<Integer>(Arrays.asList(1,2,6)) );
		window.setVisible(true);
		
	}
	
	
	class window extends ISimpleChange implements MouseListener, ActionListener
	{
		private HashMap<String, Integer> columnTable;
		private HashMap<String, ArrayList<String>> firmsMap;
		private HashMap<Integer, String> dictColumnAndYaml;
		private JButton bSave;
		public window(boolean isCheck, String yamlSys, String procedure, ArrayList<String> procParameters,
				int accesType, String title, ArrayList<Integer> Key) {
			super(isCheck, yamlSys, procedure, procParameters, accesType, title, Key);
			// TODO Auto-generated constructor stub
			setListener(Key);
			addRibbonListener();
			frame.setMenuRun(6);
			frame.setMenuRun(7);
			table.addMouseListener(this);
			firmsMap = MainEntryDataWarehouse.getFirmsMap();
			dictColumnAndYaml = new HashMap<Integer, String>();
			dictColumnAndYaml.put(7, "IInstruments_Dict_Model.yml");
			frame.addDictToModel(dictColumnAndYaml, model);
			getDict(sysAll, table);
			frame.setGgenKey(0,4,0);
			
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
			columnTable = frame.getColumnNumbers(table, sysAll);
			if (e.getClickCount()==2&&table.getSelectedColumn()==columnTable.get("ACCOUNTNUM"))
			{
				
				frame.getSelectionRunWithParameters(table,"getAccountToBank",new ArrayList<String>(Arrays.asList(firmsMap.get(firm).get(1))), "Dict_PlanAccount_sys.yml", "Konto", 0);
				
			}else if (e.getClickCount()==2&&table.getSelectedColumn()==columnTable.get("DIMENSION_4"))
			{
				frame.dictCounterpartyNameFirm(table);
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

		private void getDict (HashMap<String, ArrayList<String>> mapsysall, JTable table) 
		{
			
			try {
				PythonBase readers = new PythonBase();
				ArrayList<ArrayList<String>> templist;
				templist = readers.FromBase(false, "IBankAccount_dict.yml");
				txt txtlist = new txt();
				Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
				frame.CommboBoxDefault(dictdata, mapsysall, table);	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}

 
		
	}
}
