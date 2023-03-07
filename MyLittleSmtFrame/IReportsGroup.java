package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateTableNew;
import MyLittleSmt.FrameTemplateWindow;

public class IReportsGroup extends FrameTemplateWindow implements TableModelListener, MouseListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private mainGroup mainGro;
	private subGroup subGro;
	private JTable mainTable, subTable;
	private DefaultTableModel mainModel, subModel; 
	private HashMap<String, ArrayList<String>> mainSysAll;
	private HashMap<String, Integer> mainColM, subColM; 
	private JTextField jtMainGroup;
 
	public IReportsGroup(int x, int y, String title) {
		super(x, y, title);
		
		mainGro = new mainGroup("IDomainValueFull_sys.yml", "getDomainValueFull", new ArrayList<String>(Arrays.asList("ReportsGroups.MainReport")), 2, false); 
		mainTable = mainGro.getTable();
		mainSysAll = mainGro.getSysAll();
		mainModel = mainGro.getModel();
		mainColM = mainGro.getColM();
 
		jtMainGroup = new JTextField();
		jtMainGroup.setEnabled(false);
		jtMainGroup.setPreferredSize(new Dimension(125,20));
		
		subGro = new subGroup("IReportsGroup_SUB_sys.yml", "getSubGroup", new ArrayList<String>(Arrays.asList("X")),3,false);
		subTable = subGro.getTable();
		subModel = subGro.getModel();
		subColM = subGro.getColM();
		
		JPanel upPanel = new JPanel(new BorderLayout());
			upPanel.add(new FrameTemplate().GetUpMenu(false), BorderLayout.PAGE_START);
		add(upPanel, BorderLayout.PAGE_START);
			JPanel centPanel = new JPanel(new GridLayout(1,2));
				JPanel mainGroup = new JPanel(new BorderLayout());
				mainGroup.setBorder(new TitledBorder(""));
					mainGroup.add(mainGro.getSmallButtons(), BorderLayout.PAGE_START);
					mainGroup.add(new JScrollPane(mainTable), BorderLayout.CENTER);
				centPanel.add(mainGroup);
				JPanel subGroup = new JPanel(new BorderLayout());
				subGroup.setBorder(new TitledBorder(""));
					JPanel upSubGroup = new JPanel(new BorderLayout());
						upSubGroup.setBorder(new TitledBorder(""));
						upSubGroup.add(subGro.getSmallButtons(), BorderLayout.PAGE_START);
						JPanel jtG = new JPanel(new FlowLayout(FlowLayout.LEFT));
							jtG.add(new JLabel("Grupa:"));
							jtG.add(jtMainGroup, BorderLayout.PAGE_END);
						upSubGroup.add(jtG);
					subGroup.add(upSubGroup, BorderLayout.PAGE_START);
					subGroup.add(new JScrollPane(subTable), BorderLayout.CENTER);
				centPanel.add(subGroup);
		add(centPanel, BorderLayout.CENTER);
		
		mainModel.addTableModelListener(this);
		mainGro.addListeners(new ArrayList<Integer>(Arrays.asList(0,1)));
		mainGro.addSmallButtonsListener();
		mainTable.addMouseListener(this);
		
		

 
	}

	
	
	
	
	class mainGroup extends FrameTemplateTableNew
	{
		private JPanel smallButtons;
		public mainGroup(String yamlSys, String modelProcedure, ArrayList<String> modelParameters, int enableType,
				boolean isCheck) {
			super(yamlSys, modelProcedure, modelParameters, enableType, isCheck);
			smallButtons = frame.getSmallButtons();
			
		}
		
		private JPanel getSmallButtons()
		{
			return smallButtons;
		}
		
		private void addSmallButtonsListener()
		{
			frame.addListenerSmallButtons();
		}
		

		
	}
	
	
	class subGroup extends FrameTemplateTableNew
	{
		private JPanel smallButtons;
		public subGroup(String yamlSys, String modelProcedure, ArrayList<String> modelParameters, int enableType,
				boolean isCheck) {
			super(yamlSys, modelProcedure, modelParameters, enableType, isCheck);
			smallButtons = frame.getSmallButtons();
			addAllListeners();
		}
		private JPanel getSmallButtons()
		{
			return smallButtons;
		}
		
		private void addSmallButtonsListener()
		{
			frame.addListenerSmallButtons();
		}		
		private void addAllListeners()
		{
			 addListeners(new ArrayList<Integer>(Arrays.asList(1,2)));
			 addSmallButtonsListener();
			 frame.setGgenKey(0,8,0);
		}
	}


	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==mainModel&&e.getType()==1)
		{
			mainModel.setValueAt("ReportsGroups.MainReport", e.getFirstRow(), mainColM.get("DomainName"));
		}else if (e.getSource()==subModel&&e.getType()==1)
		{
			if (jtMainGroup.getText().trim().length()>0)
			{
				subModel.setValueAt(jtMainGroup.getText(), e.getFirstRow(), subColM.get("MainGroup"));
			}else
			{
				subModel.removeRow(e.getFirstRow());
			}
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
		HashMap<String, Integer > mainColT = new FrameTemplate().getColumnNumbers(mainTable, mainSysAll);
		if (e.getSource()==mainTable&&mainTable.getSelectedRow()>-1&&mainTable.getSelectedColumn()>-1
				&&mainTable.getValueAt(mainTable.getSelectedRow(), mainColT.get("DomainValue")).toString().trim().length()>0)
		{
			String mainG = (String) mainTable.getValueAt(mainTable.getSelectedRow(), new FrameTemplate().getColumnNumbers(mainTable, mainSysAll).get("DomainValue"));
			jtMainGroup.setText(mainG);
			subGro.newModel(new ArrayList<String>(Arrays.asList(mainG)));
			subModel = subGro.getModel();
			subModel.addTableModelListener(this);
		//	subGro.addAllListeners();
			
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
