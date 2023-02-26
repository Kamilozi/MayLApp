package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.DataImport;
import MyLittleSmt.FlatFile;
import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateButtons;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.FrameTemplateWindow;
import MyLittleSmt.PythonBase;
import MyLittleSmt.StoredProcedures;
import MyLittleSmt.txt;

public class IBank extends FrameTemplateWindow implements ActionListener, TableModelListener, MouseListener
{
	private FrameTemplate stFrame;
	private HashMap<String, String> stSys, stBase;
	private HashMap<String, ArrayList<String>> stSysAll;
	private HashMap<String, Integer> columnModel, columnTable;
	private ArrayList<Integer> stKey;
	private DefaultTableModel stModel;
	private JTable stTable;
	private String firm;
	private JPanel rOption, rExport;
	private JButton bBank, bStat, bSettings, bStaImp;
	private ArrayList<ArrayList<String>> accounts;
	private ArrayList<Object[]> impRows;
	
	public IBank(int x, int y, String title, String firm) {
		super(x, y, title);
		this.firm=firm;
		stFrame = new FrameTemplate();
		stFrame.JTableHelperSys("Bank_sys.yml");
		stSys = stFrame.getMapSys();
		stBase = stFrame.getMapSysBase();
		stSysAll = stFrame.getMapSysAll();
		stModel = getStModel(firm);
		stTable = stFrame.getDefaultTable(stModel, stSysAll);
		stKey = new ArrayList<Integer>(Arrays.asList(0,1));
		
		bBank =new FrameTemplateButtons().RibbonJButton("Konta bankowe", FrameTemplateImageIcon.iconJButton_BankAccount());
		bBank.addActionListener(this);
		bStat = new FrameTemplateButtons().RibbonJButton("Wyci¹gi", FrameTemplateImageIcon.iconJButton_BankStatement());
		bStat.addActionListener(this);
		bSettings = new FrameTemplateButtons().RibbonJButton("Ustawienia", FrameTemplateImageIcon.iconJButton_BankStatementSettings());
		bSettings.addActionListener(this);
		bStaImp = new FrameTemplateButtons().RibbonJButton("Import", FrameTemplateImageIcon.iconJButton_ImportStatement());
		bStaImp.addActionListener(this);
		JPanel upMenu = new JPanel(new BorderLayout());
			upMenu.add(stFrame.GetUpMenu(false), BorderLayout.PAGE_START);
			JTabbedPane jtp = new JTabbedPane();//new FlowLayout(FlowLayout.LEFT)
				rOption = new JPanel(new FlowLayout(FlowLayout.LEFT));
				rExport = new JPanel(new FlowLayout(FlowLayout.LEFT));
				 	rOption.add(stFrame.DefaultRibbonSim());
					rOption.add(bBank);
					rOption.add(bStat);
					rOption.add(bSettings);
					rOption.add(bStaImp);
				 	rExport.add(stFrame.DefaultRibbonExp());
					rExport.add(stFrame.DefaultRibbonImp());			
				jtp.add("Opcje", rOption);
				jtp.add("Eksport", rExport);
			upMenu.add(jtp, BorderLayout.PAGE_END);
		JPanel downMenu = new JPanel(new BorderLayout());
			downMenu.add(new JScrollPane(stTable), BorderLayout.CENTER);
		add(upMenu, BorderLayout.PAGE_START);
		add(downMenu, BorderLayout.CENTER);
		addListeners();
		columnModel = stFrame.getColumnNumbers(stModel, stSysAll);
		getDict (stSysAll, stTable);
		stFrame.setGgenKey(0,5,0);
		
	}
 	private DefaultTableModel getStModel(String firm)
	{
		return new StoredProcedures().genUniversalModel("getBankByFirm", new  ArrayList<String>(Arrays.asList(firm)), 18, stSys, stSysAll);
	}
 	
 	private void addListeners()
 	{
 		
 		stModel.addTableModelListener(this);
 		stFrame.addFrameTempListener(stTable, stModel, stSysAll, stBase, stSys, stKey);
 		stFrame.addListenerJTable(stTable, stModel);
 		stFrame.addListenerRibbon();
 		stFrame.addContextMenu(stTable);
 		stFrame.setMenuRun(3);
 		stTable.addMouseListener(this);
 		
 	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		columnTable = stFrame.getColumnNumbers(stTable, stSysAll);
		if (e.getSource()==bBank)
		{
			new IBankAccount(firm);
			stModel = getStModel(firm);
			stFrame.setModel(stModel);
		}else if (bStat==e.getSource())
		{
			if (stTable.getSelectedRow()>=0&&stTable.getSelectedColumn()>=0)
			{
			new IBankStatement(	(String) stTable.getValueAt(stTable.getSelectedRow(), columnTable.get("FIRM")),
								(String) stTable.getValueAt(stTable.getSelectedRow(), columnTable.get("ID")),
								String.valueOf(stTable.getValueAt(stTable.getSelectedRow(), columnTable.get("DOCUMENTDATE"))),
								(String) stTable.getValueAt(stTable.getSelectedRow(), columnTable.get("AccountNumber")));
			}	
			
		}else if (bSettings==e.getSource())
		{
			IBankSettings widow =new IBankSettings();
		}else if (bStaImp==e.getSource())
		{
			ImportBank();
		}
	}
	
	private void ImportBank()
	{
		DataImport imp = new DataImport();
		String fileName = imp.setFileName();
		ArrayList<ArrayList<String>> templist = new FlatFile().ImportFlatFile(false, fileName, ";");
		String bankNum = "";
		ArrayList<ArrayList<String>> bankAccountSetting;
		impRows = new ArrayList<Object[]>();
		for (int i = 0; i<templist.size(); i++)
		{
			
			//mBank
			if (templist.get(i).get(0).trim().equals("#Numer rachunku"))
			{
				bankNum = templist.get(i+1).get(0).trim();
				Pattern pat = Pattern.compile("[\\w]{2}[\\d\\s]*");
				Matcher mat = pat.matcher(bankNum);			
				if (mat.find())
				{
					bankAccountSetting= new StoredProcedures().genUniversalArray("getBankAccountSettingToImport", new ArrayList<String>(Arrays.asList(mat.group().trim().replace(" ", ""), String.valueOf(mat.group().trim().replace(" ", "").length()))));
					if (bankAccountSetting.size()<=1)
					{
						JOptionPane.showMessageDialog(null, "Nie znaleziony, lub nie obs³u¿ony wyciag: " + mat.group(), "Informacja", JOptionPane.INFORMATION_MESSAGE);
					}else
					{
						HashMap<String, Integer> column =  new FrameTemplate().getColumnNumbers(bankAccountSetting.get(0));
						if (bankAccountSetting.get(1).get(column.get("IMPORTTYPE")).equals("mBank1"))
						{
							Object[] mBank1Bank = mBank1Bank(templist, bankAccountSetting);
							stModel.addRow(mBank1Bank);
							stFrame.Button_Save();
							impRows = mBank1(templist, bankAccountSetting,(String) stModel.getValueAt(stModel.getRowCount()-1, 0));
							HashMap<String, Integer> stModelColumn = stFrame.getColumnNumbers(stModel, stSysAll);
							
							var bankStatement = new IBankStatement(true,
									(String) mBank1Bank[stModelColumn.get("FIRM")],
									(String) stModel.getValueAt(stModel.getRowCount()-1, 0),
									(String) mBank1Bank[stModelColumn.get("DOCUMENTDATE")],
									(String) mBank1Bank[stModelColumn.get("AccountNumber")]);
							bankStatement.addAndSaveSsModel(impRows, true);
							break;
						}else 
						{
							JOptionPane.showMessageDialog(null, "Nie znaleziony schemate wyci¹gu dla: " + bankAccountSetting.get(1).get(column.get("IMPORTTYPE")), "Informacja", JOptionPane.INFORMATION_MESSAGE);
						}
					}		
				
					 
				}
			}
			
		}
		//ArrayList<ArrayList<String>> clearStatement = FlatFile(true, "")
	}
	private Object[] mBank1Bank (ArrayList<ArrayList<String>> templist, ArrayList<ArrayList<String>> settings)
	{
		HashMap<String, Integer> column =  new FrameTemplate().getColumnNumbers(settings.get(0));
		
		for (int i=0;i<templist.size();i++)
		{
			if (templist.get(i).get(0).trim().equals("#Za okres:"))
			{
				try {
					Date date =new SimpleDateFormat("dd.MM.yyyy").parse(templist.get(i+1).get(1));
					String sdf = new SimpleDateFormat("yyyy-MM-dd").format(date);
					
					Object[] head = {"",settings.get(1).get(column.get("FIRM")), sdf, settings.get(1).get(column.get("AccountNumber")),
							settings.get(1).get(column.get("AccountName")), settings.get(1).get(column.get("CURRENCYCODE")),settings.get(1).get(column.get("BTYPE")), false};
					return head;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				return null;
			}
		}
		 
		
		 return null;
	}
	
	private ArrayList<Object[]> mBank1(ArrayList<ArrayList<String>> templist, ArrayList<ArrayList<String>> settings, String accountID)
	{
		ArrayList<Object[]> result = new ArrayList<Object[]>();
		HashMap<String, Integer> column =  new FrameTemplate().getColumnNumbers(settings.get(0));
		for (int i = 0; i<templist.size();i++)
		{
			if (templist.get(i).size()==8&&!String.valueOf(templist.get(i).get(6)).equals("#Saldo koñcowe"))
			{
				Object[] temp = {false, "", accountID, settings.get(1).get(column.get("FIRM")),
						templist.get(i).get(0), templist.get(i).get(1), templist.get(i).get(2), templist.get(i).get(3).replace("'", "").replace("\"", ""), templist.get(i).get(4).replace("'", "").replace("\"", ""), 
						templist.get(i).get(5).replace("'", "").replace("\"", ""),(String) templist.get(i).get(6).replace(" ", ""),(String) templist.get(i).get(7).trim().replace(" ", "")};
				result.add(temp);

			}
		}
		return result;
	}
	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		if (e.getType()==1)
		{
			newRowDate(e.getFirstRow());
		}else if (e.getType()==0&&e.getColumn()==columnModel.get("AccountNumber"))
		{
			newRow(e.getFirstRow());
		}
	}
	
	private void newRowDate(int row)
	{
		columnTable = stFrame.getColumnNumbers(stTable, stSysAll);
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		stModel.setValueAt(formatter.format(date),row, columnTable.get("DOCUMENTDATE"));
	}
	
	private void newRow(int row)
	{
		columnTable = stFrame.getColumnNumbers(stTable, stSysAll);
		ArrayList<ArrayList<String>> accountList = new StoredProcedures().genUniversalArray("getBankAccountToDict",new ArrayList<String>(Arrays.asList(firm)));
		HashMap<String, Integer> columnArray = stFrame.getColumnNumbers(accountList.get(0));
		int rowList = -1;
		for (int i=0;i<accountList.size();i++)
		{
			if (stTable.getValueAt(row, columnTable.get("AccountNumber")).equals(accountList.get(i).get(columnArray.get("AccountNumber")))) 
			{
				rowList= i;
				break;
			}
		}
		if (rowList>=0)
		{
			for (int i = 0; i< stTable.getColumnCount();i++)
			{
				 if (i==columnTable.get("AccountName"))
				 {
					 stTable.setValueAt(accountList.get(rowList).get(columnArray.get("AccountName")), row, i);
				 } else if (i==columnTable.get("CURRENCYCODE"))
				 {
					 stTable.setValueAt(accountList.get(rowList).get(columnArray.get("CURRENCYCODE")), row, i);
				 }
				 else if (i==columnTable.get("BTYPE"))
				 {
					 stTable.setValueAt(accountList.get(rowList).get(columnArray.get("BTYPE")), row, i);
				 }
			}
		}
		
	}
	
	private void getDict (HashMap<String, ArrayList<String>> mapsysall, JTable table) 
	{
		
		try {
			PythonBase readers = new PythonBase();
			ArrayList<ArrayList<String>> templist;
			templist = readers.FromBase(false, "IBank_dict.yml");
			txt txtlist = new txt();
			Object[][] dictdata = Arrays.copyOfRange(txtlist.ArrayListToObject(templist), 1, templist.size());
			stFrame.CommboBoxDefault(dictdata, mapsysall, table);	
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
		columnTable = stFrame.getColumnNumbers(stTable, stSysAll);
		if (e.getClickCount()==2&&stTable.getSelectedColumn()==columnTable.get("AccountNumber"))
		{
			stFrame.getSelectionRunWithParameters(stTable,"getBankAccountToDict",new ArrayList<String>(Arrays.asList(firm)), "Dict_BankAccount_sys.yml", "Konta bankowe", 0);
			newRow(stTable.getSelectedRow());
		}else if (e.getClickCount()==2&&e.getSource()==stTable)
		{
			if (stTable.getSelectedRow()>=0&&stTable.getSelectedColumn()>=0)
			{
			new IBankStatement(	(String) stTable.getValueAt(stTable.getSelectedRow(), columnTable.get("FIRM")),
								(String) stTable.getValueAt(stTable.getSelectedRow(), columnTable.get("ID")),
								String.valueOf(stTable.getValueAt(stTable.getSelectedRow(), columnTable.get("DOCUMENTDATE"))),
								(String) stTable.getValueAt(stTable.getSelectedRow(), columnTable.get("AccountNumber")));
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
