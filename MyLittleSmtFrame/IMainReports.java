package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpinnerDateModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateButtons;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.FrameTemplateTableNew;
import MyLittleSmt.FrameTemplateWindow;
import MyLittleSmt.Reports;
import MyLittleSmt.StoredProcedures;

public class IMainReports extends FrameTemplateWindow implements TreeSelectionListener,ActionListener ,MouseListener
{
	private JTree tree;
	private reportsClass reports;
	private reportsCodeClass   codes;
	private JTable repTable, codeTable;
	private FrameTemplate repFrame, codFrame;
	private JButton bGen, bcodeAdd;
	private JSpinner dateFrom, dateTo;
	private JTextField jtfGroupID;
	private DefaultTableModel repModel, codeModel;
	private JComboBox<String> jcCodes;
	private JTextField jtfReportID;
	
	public IMainReports(int x, int y, String title)
	{
		super(x, y, title);
		tree=reportTree();
		reports = new reportsClass("IReports_sys.yml", "getReports", new ArrayList<String>(Arrays.asList("X")), 3, false);
		repTable = reports.getTable();
		repFrame = reports.getFrame();
		repModel = reports.getModel();
		codes = new reportsCodeClass("IReportsCodes_sys.yml", "getReportsCodes", new ArrayList<String>(Arrays.asList("X")), 3, false);
		codeTable = codes.getTable();
		codFrame = codes.getFrame();
		codeModel = codes.getModel();
		jcCodes = new JComboBox<String>();
		jcCodes.setPreferredSize(new Dimension(125,20));
		codFrame.getSmallButtons();
		bGen = new FrameTemplateButtons().RibbonJButton("Generacja", FrameTemplateImageIcon.iconJButton_Generate());
		bcodeAdd = new FrameTemplateButtons().smallButton("Dodaj kod", FrameTemplateImageIcon.iconJButton_SmallAdd());
		
		dateFrom = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH)); 
		dateFrom.setEditor(new JSpinner.DateEditor(dateFrom, "dd-MM-yyyy")); 
		dateTo = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
		dateTo.setEditor(new JSpinner.DateEditor(dateTo, "dd-MM-yyyy"));
		jtfGroupID = new JTextField();
		jtfGroupID.setEnabled(false);
		jtfGroupID.setPreferredSize(new Dimension(125,20));
		jtfReportID = new FrameTemplateButtons().getTextField();
		JSpinner.DateEditor editor1 = new JSpinner.DateEditor(dateFrom, "dd-MM-yyyy");
		JPanel up = new JPanel(new BorderLayout());
			up.add(repFrame.GetUpMenu(false),BorderLayout.PAGE_START);
			JTabbedPane jtp = new JTabbedPane();
			JPanel ribbonSim = new JPanel(new FlowLayout(FlowLayout.LEFT));
				ribbonSim.add(repFrame.DefaultRibbonSim());
				JPanel ribRep = new JPanel(new FlowLayout(FlowLayout.LEFT));
					ribRep.add(bGen);
					JPanel datePan = new JPanel(new GridLayout(2,2,1,1));
					datePan.setBorder(new TitledBorder(""));
						datePan.add(new JLabel("Data od:"));
						datePan.add(dateFrom);
						datePan.add(new JLabel("Data do:"));
						datePan.add(dateTo);
					ribRep.add(datePan);
				ribbonSim.add(ribRep);
			jtp.add("Opcje", ribbonSim);
			JPanel ribbonExp = new JPanel(new FlowLayout(FlowLayout.LEFT));
				ribbonExp.add(repFrame.DefaultRibbonExp());	
			jtp.add("Eksport",ribbonExp);
			up.add(jtp, BorderLayout.PAGE_END);
		JPanel cen = new JPanel(new BorderLayout());
			JPanel cenTree = new JPanel(new BorderLayout());
				cenTree.setBorder(new TitledBorder(""));
				cenTree.add(new JScrollPane(tree), BorderLayout.CENTER);
			cen.add(cenTree, BorderLayout.LINE_START);
			JPanel cenRep = new JPanel(new BorderLayout());
				JPanel cenRepUp = new JPanel(new FlowLayout(FlowLayout.LEFT));
					cenRepUp.add(new JLabel("Groupa:"));
					cenRepUp.add(jtfGroupID);
				cenRep.add(cenRepUp, BorderLayout.PAGE_START);
				cenRep.setBorder(new TitledBorder(""));
				cenRep.add(new JScrollPane(repTable));
			cen.add(cenRep, BorderLayout.CENTER);
			JPanel cenCod = new JPanel(new BorderLayout());
				cenCod.setBorder(new TitledBorder(""));
			 	JPanel cenCodUp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			 	 	cenCodUp.add(codFrame.getsmallButtonSave());
			 		cenCodUp.add(bcodeAdd);
			 		cenCodUp.add(jcCodes);
			 		cenCodUp.add(new JLabel("Kod raportu: "));
			 		cenCodUp.add(jtfReportID);
			 	cenCod.add(cenCodUp, BorderLayout.PAGE_START);
			 	cenCod.add(new JScrollPane(codeTable), BorderLayout.CENTER);
			 cen.add(cenCod, BorderLayout.LINE_END);
			 add(up, BorderLayout.PAGE_START);
			 add(cen, BorderLayout.CENTER);
		
		//add(tree, BorderLayout.LINE_START);	
		for (int i = 0; i < tree.getRowCount(); i++) {
		    tree.expandRow(i);
		}
		tree.addTreeSelectionListener(this);
		bGen.addActionListener(this);
		bcodeAdd.addActionListener(this);
		
	}
	

	private JTree reportTree()
	{
		ArrayList<ArrayList<String>> groups = new StoredProcedures().genUniversalArray("getReportsGroupAll", new ArrayList<String>());
		DefaultMutableTreeNode report = new DefaultMutableTreeNode("Raporty");
		JTree tree=new JTree(report);
 
		HashMap<String, ArrayList<DefaultMutableTreeNode>> groupMap = new HashMap<String, ArrayList<DefaultMutableTreeNode>>();
	    for (int i=1;i<groups.size();i++)
	    {
	    		 if (groupMap.containsKey(groups.get(i).get(0)))
	    		 {
	    			 groupMap.get(groups.get(i).get(0)).add(new DefaultMutableTreeNode(groups.get(i).get(1) + " (" + groups.get(i).get(2) + ")"));
	    		 }else
	    		 {
	    			 groupMap.put(groups.get(i).get(0), new ArrayList<DefaultMutableTreeNode>(Arrays.asList(new DefaultMutableTreeNode(groups.get(i).get(1)+ " (" + groups.get(i).get(2) + ")"))));
	    		 }
	    }
	    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
	    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
	   
		for (String key:groupMap.keySet())
		{
			DefaultMutableTreeNode main=new DefaultMutableTreeNode(key);  
			for (int i=0;i<groupMap.get(key).size();i++)
			{
				main.add(new DefaultMutableTreeNode(groupMap.get(key).get(i)));
			}
			model.insertNodeInto(main, root, root.getChildCount()); 
		}
		
		return tree;
	}



	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		 DefaultMutableTreeNode node = (DefaultMutableTreeNode)
         tree.getLastSelectedPathComponent();
		 Object nodeInfo = node.getUserObject();
		 TreePath nodes = e.getNewLeadSelectionPath();
		  if (node == null) return;
		  if (node.getChildCount()==0)
		  {
			  String lastNode= String.valueOf(e.getNewLeadSelectionPath().getLastPathComponent());
			  String idNode= lastNode.substring(lastNode.lastIndexOf("(") +1, lastNode.length()-1);
			 /// System.out.print(idNode);
			  jtfGroupID.setText(idNode);
			  reports.newModel(new ArrayList<String>(Arrays.asList(idNode)));
			  reports.addListenersR();
			  repTable.addMouseListener(this);
			  
		  }
		   
	}
	 
	class reportsClass extends FrameTemplateTableNew implements TableModelListener
	{

		public reportsClass(String yamlSys, String modelProcedure, ArrayList<String> modelParameters, int enableType,
				boolean isCheck) {
			super(yamlSys, modelProcedure, modelParameters, enableType, isCheck);
			// TODO Auto-generated constructor stub
 
	 		
		}

		public void addListenersR()
		{
		 	frame.addSimpleComboBoxToTable(table, 3, "getDomainValue",new ArrayList<String>(Arrays.asList("ReportsGroupsFirms.MainFirms")) ,false);
		 	frame.addSimpleComboBoxToTable(table, 2, "getDomainValue",new ArrayList<String>(Arrays.asList("Reports")) ,false);
			model.addTableModelListener(this);
			addListeners(new ArrayList<Integer>(Arrays.asList(0,1)));
			frame.addListenerRibbon();
			frame.setGgenKey(1,9,0);
			
		}

		@Override
		public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			if (e.getType()==1)
			{
				model.setValueAt(jtfGroupID.getText(), e.getFirstRow(), reports.getColM().get("GroupID"));
			}
		}
		
	}
	
	class reportsCodeClass extends FrameTemplateTableNew
	{

		public reportsCodeClass(String yamlSys, String modelProcedure, ArrayList<String> modelParameters,
				int enableType, boolean isCheck) {
			super(yamlSys, modelProcedure, modelParameters, enableType, isCheck);
			// TODO Auto-generated constructor stub
		}
		
		public void addListenersCode()
		{
			 
			addListeners(new ArrayList<Integer>(Arrays.asList(0,1)));
			frame.addListenerSmallButtons();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==bcodeAdd&&jcCodes.getSelectedItem().toString().trim().length()>0&&jtfReportID.getText().trim().length()>0)
		{
			String code = jcCodes.getSelectedItem().toString();
			boolean isuni = true;
			for (int i=0;i<codes.getModel().getRowCount();i++)
			{
				System.out.print(codes.getModel().getValueAt(i, codes.getColM().get("Code")).toString());
				if (codes.getModel().getValueAt(i, codes.getColM().get("Code")).toString().equals(code))
				{
					isuni=false;
					break;
				}
			}
			if (isuni==true)
			{
			 	Object[] newRow = {jtfReportID.getText(), code, ""};
			 	ArrayList<Object[]> newRowList = new ArrayList<Object[]>();
			 	newRowList.add(newRow);
			 	codes.addRow(newRowList);
			}
		}else if (bGen==e.getSource())
		{

			try {
				String NewFormat = "yyyy-MM-dd";
				String OldFormat = "EEE MMM dd HH:mm:ss Z yyyy";
				String strStartDate = dateTo.getValue().toString();
				String strEndtDate = dateFrom.getValue().toString();
				SimpleDateFormat sdf = new SimpleDateFormat(OldFormat, Locale.ENGLISH);
				SimpleDateFormat sdf2 = new SimpleDateFormat(OldFormat, Locale.ENGLISH);
				Date ds = sdf.parse(strStartDate);
				sdf.applyPattern(NewFormat);
				strStartDate = sdf.format(ds);
				Date de = sdf2.parse(strEndtDate);
				sdf2.applyPattern(NewFormat);
				strEndtDate = sdf2.format(de);
				String group = jtfGroupID.getText();
				new Reports(strEndtDate, strStartDate, group);
				
			} catch (ParseException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
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
		if (e.getSource()==repTable
				&&repTable.getSelectedColumn()>-1&&repTable.getSelectedRow()>-1
				&&!jtfReportID.getText().equals(repTable.getValueAt(repTable.getSelectedRow(), reports.getColT().get("ReportID")).toString()))
		{
			codes.newModel(new ArrayList<String>(Arrays.asList(repTable.getValueAt(repTable.getSelectedRow(), reports.getColT().get("ReportID")).toString())));
			
			codFrame.addToSimplComboBox(jcCodes, "getDomainValue",new ArrayList<String>(Arrays.asList("Reports." + repTable.getValueAt(repTable.getSelectedRow(), reports.getColT().get("Report")).toString()  )));
			jtfReportID.setText(repTable.getValueAt(repTable.getSelectedRow(), reports.getColT().get("ReportID")).toString());
			codes.addListenersCode();
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
