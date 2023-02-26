package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateButtons;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.FrameTemplateTableNew;
import MyLittleSmt.FrameTemplateWindow;

public class IReportsGroupsFirms extends FrameTemplateWindow implements ActionListener, MouseListener {
private outGroupClass outC;
private inGroupClass inC;
private mainGroupClass mainC;
private JTable mainTable, inTable, outTable;
private FrameTemplate mainFrame, inFrame, outFrame;
private JButton bAdd, bDel;
private JTextField jtfGroup;
private DefaultTableModel outModel, inModel;
	public IReportsGroupsFirms(int x, int y, String title) {
		super(x, y, title);
		// TODO Auto-generated constructor stub
		//bAdd = new FrameTemplateButtons().smallButton("Dodaj", FrameTemplateImageIcon.iconJButton_smallPlus());
		bDel = new FrameTemplateButtons().smallButton("Usuñ", FrameTemplateImageIcon.iconJButton_smallMinus());
		bAdd = new JButton("<<");
		
		bDel = new JButton(">>");
		bAdd.setPreferredSize(new Dimension(55,55));
		bDel.setPreferredSize(new Dimension(55,55));
		bAdd.addActionListener(this);
		bDel.addActionListener(this);
		
		jtfGroup = new FrameTemplateButtons().getTextField();
		outC = new outGroupClass("ICompanyShort_sys.yml", "getCompanyShort", new ArrayList<String>(), 13, true);
		mainC = new mainGroupClass("IDomainValueFull_sys.yml", "getDomainValueFull", new ArrayList<String>(Arrays.asList("ReportsGroupsFirms.MainFirms")), 2, false);
		inC = new inGroupClass("IGroupOfCompanies_sys.yml", "getGroupOfCompanies", new ArrayList<String>(Arrays.asList("Reports", "X")), 13, true);
		
		mainTable = mainC.getTable();
		mainTable.addMouseListener(this);
		inModel = inC.getModel();
		outModel = outC.getModel();
		
		JPanel up = new JPanel(new BorderLayout());
			up.add(new FrameTemplate().GetUpMenu(false), BorderLayout.PAGE_START);
		add(up, BorderLayout.PAGE_START);
		add(mainC.getFullPanel(), BorderLayout.LINE_START);
			JPanel rightM = new JPanel(new BorderLayout());
			JPanel rightUp = new JPanel(new FlowLayout(FlowLayout.LEFT));
				rightUp.add(inC.getFrame().getsmallButtonSave());
				rightUp.add(new JLabel("Grupa:"));
				rightUp.add(jtfGroup);
			rightM.add(rightUp, BorderLayout.PAGE_START);
			JPanel right = new JPanel(new GridLayout(1,2,1,1));
				right.add(inC.getJPanel());
				right.add(outC.getJPanel());
			rightM.add(right, BorderLayout.CENTER);
		add(rightM, BorderLayout.CENTER);
				
	}
	
	class mainGroupClass extends FrameTemplateTableNew implements TableModelListener
	{
		
		public mainGroupClass(String yamlSys, String modelProcedure, ArrayList<String> modelParameters, int enableType,
				boolean isCheck) {
			super(yamlSys, modelProcedure, modelParameters, enableType, isCheck);
			// TODO Auto-generated constructor stub
			frame.getSmallButtons();
			model.addTableModelListener(this);
			addListeners(new ArrayList<Integer>(Arrays.asList(0,1)));
			frame.addListenerSmallButtons();
	
		}
		private JPanel getFullPanel()
		{
			
			JPanel main = new JPanel(new BorderLayout());
				JPanel up = new JPanel(new FlowLayout(FlowLayout.LEFT));
				up.add(frame.getsmallButtonSave());
				up.add(frame.getsmallButtonAdd());
			main.add(up, BorderLayout.PAGE_START);
			main.add(new JScrollPane(table), BorderLayout.CENTER);
	 
		
			return main;
			
		}
		 
		@Override
		public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			if (e.getType()==1) {model.setValueAt("ReportsGroupsFirms.MainFirms", e.getFirstRow(), colM.get("DomainName"));}
		}
	}
	class inGroupClass extends FrameTemplateTableNew implements TableModelListener  
	{

		public inGroupClass(String yamlSys, String modelProcedure, ArrayList<String> modelParameters, int enableType,
				boolean isCheck) {
			super(yamlSys, modelProcedure, modelParameters, enableType, isCheck);
			// TODO Auto-generated constructor stub
			frame.getSmallButtons();
		}
 
		protected JPanel getJPanel()
		{
			
			JPanel main = new JPanel(new BorderLayout());
			//	JPanel up = new JPanel(new FlowLayout(FlowLayout.LEFT));
			//		up.add(frame.getsmallButtonSave());
			//	main.add(up, BorderLayout.PAGE_START);
				main.add(new JScrollPane(table), BorderLayout.CENTER);
			return main;	
		}
		
		private void deleteRow()
		{
		int size_=model.getRowCount() -1;
			for (int i=size_;i>=0;i--)
			{
			 
			 	if ((boolean)model.getValueAt(i, inC.getColM().get("Check"))==true)
				{
					frame.removeRowInModel(model, table, i);
				}
			}
			
			
		}
		private void listeners()
		{
			//model.addTableModelListener(this);
			addListeners(new ArrayList<Integer>(Arrays.asList(1,2,3)));
			frame.addListenerSmallButtons();
			frame.addSkipList(new ArrayList<Integer>(Arrays.asList(0)));
		}

		@Override
		public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			//if (e.getType()==1)
			//{
			//	model.setValueAt("Reports", e.getFirstRow(), colM.get("GroupType"));
			//}
		}
	}
	
	class outGroupClass extends FrameTemplateTableNew  
	{
		
		public outGroupClass(String yamlSys, String modelProcedure, ArrayList<String> modelParameters, int enableType,
				boolean isCheck) {
			super(yamlSys, modelProcedure, modelParameters, enableType, isCheck);
			// TODO Auto-generated constructor stub
			addListeners(new ArrayList<Integer>(Arrays.asList(0,1,2)));
		}
		protected JPanel getJPanel()
		{
			
			JPanel main = new JPanel(new BorderLayout());
				JPanel up = new JPanel(new BorderLayout());
					JPanel button = new JPanel();
					button.add(bAdd);
					button.add(bDel);
					button.setLayout(new BoxLayout(button, BoxLayout.Y_AXIS));
					up.add(button, BorderLayout.CENTER);
				main.add(up, BorderLayout.LINE_START);
				main.add(new JScrollPane(table), BorderLayout.CENTER);

			return main;
			
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==bAdd&&jtfGroup.getText().trim().length()>0)
		{
			ArrayList<Object[]> list = new ArrayList<Object[]>();
			for (int i=0; i<outModel.getRowCount();i++)
			{
				if ((boolean)outModel.getValueAt( i, outC.getColM().get("Check"))==true)
				{
					Object[] newRow = {false, "Reports", jtfGroup.getText(), outModel.getValueAt(i, outC.getColM().get("CID"))};
					list.add(newRow);
				}
			}
			inC.addRow(list);
		}else if (e.getSource()==bDel)
		{
			inC.deleteRow();
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
		HashMap<String, Integer> mColT = mainC.getColT();
		if (e.getSource()==mainTable&&mainTable.getSelectedColumn()>-1&&mainTable.getSelectedRow()>-1&&mainTable.getValueAt(mainTable.getSelectedRow(), mColT.get("DomainValue")).toString().trim().length()>0)
		{
			jtfGroup.setText(mainTable.getValueAt(mainTable.getSelectedRow(), mColT.get("DomainValue")).toString());
			inC.newModel(new ArrayList<String>(Arrays.asList("Reports", mainTable.getValueAt(mainTable.getSelectedRow(), mColT.get("DomainValue")).toString())));
			inC.listeners();
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
