package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateButtons;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.FrameTemplateTable;
import MyLittleSmt.FrameTemplateWindow;

public class IPlanAccountN extends  FrameTemplateWindow implements ChangeListener {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private FrameTemplate mainFrame = new FrameTemplate();
private PlanAccountTable planAccount;
private PlanAccountCodeTable planAccountCode;
private JTabbedPane jtpDown;
	public IPlanAccountN(int x, int y, String title) {
		super(x, y, title);
		planAccount = new PlanAccountTable("IPlanAccount_sys.yml",  "getFullPlanAccount",new ArrayList<String>(), 0);
		planAccountCode = new PlanAccountCodeTable("PlanAcountCodeForFirm_sys.yml",  "getPlanAccountCodesForGroup",new ArrayList<String>(Arrays.asList("XX")), 1);
		JPanel up = getUpMenu(mainFrame);
			JTabbedPane	jtpRibbon = new JTabbedPane();
					JPanel jpOption = new JPanel(new FlowLayout(FlowLayout.LEFT));
						jpOption.add(planAccount.ribbonOptionSimp());
						jpOption.add(planAccountCode.ribbonOptionSimp());
					JPanel jpExport = new JPanel(new FlowLayout(FlowLayout.LEFT));
						jpExport.add(planAccount.ribbonExport());
						jpExport.add(planAccountCode.ribbonExport());
				jtpRibbon.add("Opcje", jpOption);
				jtpRibbon.add("Export" , jpExport);
			up.add(jtpRibbon, BorderLayout.PAGE_END);
		JPanel down = new JPanel(new BorderLayout());
			jtpDown = new JTabbedPane();
				jtpDown.add("Plan kont", planAccount.getTableJP());
				jtpDown.add("Kody planu kont", planAccountCode.getTableJP());
			down.add(jtpDown, BorderLayout.CENTER);
		add(up, BorderLayout.PAGE_START);
		add(down, BorderLayout.CENTER);		 
		listeners();
		planAccount.setRibbonVisible(true);
		planAccountCode.setRibbonVisible(false);
		jtpDown.addChangeListener(this);
	 
	}
	
	private void listeners()
	{
		planAccount.fullListener(new ArrayList<Integer>(Arrays.asList(0)));
		planAccountCode.fullListener(new ArrayList<Integer>(Arrays.asList(0,1)));
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==jtpDown&&jtpDown.getSelectedIndex()==0)
		{
			planAccount.setRibbonVisible(true);
			planAccountCode.setRibbonVisible(false);
		}else if (e.getSource()==jtpDown&&jtpDown.getSelectedIndex()==1)
		{
			planAccount.setRibbonVisible(false);
			planAccountCode.setRibbonVisible(true);		
			planAccountCode.defaultModel();
			planAccountCode.fullListener(new ArrayList<Integer>(Arrays.asList(0,1)));
			planAccountCode.setMenuRun();
		}
	}

	 
		
}

class PlanAccountTable extends FrameTemplateTable
{

	public PlanAccountTable(String sysYaml, String procedure, ArrayList<String> procParameters, int enableType) {
		super(sysYaml, procedure, procParameters, enableType);
		RibbonCreate();
	 

		
		try {
			getDict("IPlanAccount_dict.yml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}
	
	public JPanel ribbonOptionSimp()
	{
		//JPanel rOption = new JPanel(new FlowLayout(FlowLayout.LEFT));
			rOption.add(frame.DefaultRibbonSim());
		return rOption;
	}
	
	public JPanel ribbonExport()
	{
		//JPanel rExport = new JPanel(new FlowLayout(FlowLayout.LEFT));
			rExport.add(frame.DefaultRibbonExp());
			rExport.add(frame.DefaultRibbonImp());
		return rExport;
	}
}

class PlanAccountCodeTable extends FrameTemplateTable  implements ActionListener, MouseListener
{
	private JComboBox cbCode; 
	private JComboBox cbPlanAccount;
	private JButton smallAdd;
	
	public PlanAccountCodeTable(String sysYaml, String procedure, ArrayList<String> procParameters, int enableType) {
		super(sysYaml, procedure, procParameters, enableType);
		
		
		cbPlanAccount = frame.getSimplComboBox("ComboBox_PlanAccountGroup.yml", false);
		cbPlanAccount.setPreferredSize(new Dimension(125,20));
		cbPlanAccount.addActionListener(this);
		
		smallAdd = new FrameTemplateButtons().smallButton("Dodaj kod", FrameTemplateImageIcon.iconJButton_SmallAdd());
		smallAdd.addActionListener(this);
		cbCode = frame.getSimplComboBox("ComboBox_PlanAccountCodes.yml", false);
		cbCode.setPreferredSize(new Dimension(125,20));
		JPanel upPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			upPanel.add(new JLabel("Grupa:"));
			upPanel.add(cbPlanAccount);
			upPanel.add(cbCode);
			upPanel.add(smallAdd);
		
		panel.add(upPanel, BorderLayout.PAGE_START);
		RibbonCreate();
		
		table.addMouseListener(this);
		// TODO Auto-generated constructor stub
	}
	public JPanel ribbonOptionSimp()
	{
		rOption.add(frame.DefaultRibbonSim());
		frame.getbAdd().setEnabled(false);
		return rOption;
	}
	
	public void setMenuRun()
	{
		frame.setMenuRun(2);
	}
	
	public JPanel ribbonExport()
	{
		rExport.add(frame.DefaultRibbonExp());
		//rExport.add(frame.DefaultRibbonImp());
		return rExport;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==cbPlanAccount)
		{
			if (!cbPlanAccount.getSelectedItem().equals(""))
			{
				setNewModel(new ArrayList<String>(Arrays.asList((String) cbPlanAccount.getSelectedItem())));
			}
		}else if (e.getSource()==smallAdd && !cbCode.getSelectedItem().equals("") && !cbPlanAccount.getSelectedItem().equals(""))
		{
			if (annyAreYouOk((String)cbCode.getSelectedItem() )==true)
			{
				 Object[] newRow = {cbPlanAccount.getSelectedItem(),cbCode.getSelectedItem(), "" };
				 model.insertRow(0, newRow);
			}
		}
	}	
	
	public void defaultModel()
	{
		if (!cbPlanAccount.getSelectedItem().equals(""))
		{
			setNewModel(new ArrayList<String>(Arrays.asList((String) cbPlanAccount.getSelectedItem())));
		}
	}
	
	private boolean annyAreYouOk(String value)
	{
		HashMap<String, Integer> columnMap = columnMapModel();
		boolean result = true;
			for (int i=0; i< model.getRowCount();i++)
			{
				if (model.getValueAt(i, columnMap.get("Code")).equals(value))
				{
					result = false;
					break;
				}
			}
		return result;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==table)
		{
		if (e.getClickCount()==2)
		{	
			//HashMap<String, Integer> columnMap = columnMapTable();
			if (table.getColumnName(table.getSelectedColumn()).equals(sysMap.get("CodeValue")))
			{ 
				frame.getSelectionRunWithParameters(table, "getPlanAccountForGroup",new ArrayList<String>(Arrays.asList((String)cbPlanAccount.getSelectedItem())) , "IPlanAccount_sys.yml", table.getColumnName(table.getSelectedColumn()),0);
			}
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
