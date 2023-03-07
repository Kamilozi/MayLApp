package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateButtons;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.FrameTemplateTableNew;
import MyLittleSmt.FrameTemplateTree;
import MyLittleSmt.FrameTemplateWindow;
import MyLittleSmt.StoredProcedures;

public class IDomainValues extends FrameTemplateWindow implements TreeSelectionListener, ActionListener{
private JTree tree;
private dvClass dv;
private JTextField domain;
private JButton bNew;
	public IDomainValues(int x, int y, String title) {
		super(x, y, title);
		tree = new FrameTemplateTree().reportTree("Domain Values", "getDomainValueTree", new ArrayList<String>());
		dv = new dvClass("IDomainValueFull_sys.yml", "getDomainValueFull", new ArrayList<String>(Arrays.asList("X")),2, false);
		domain = new FrameTemplateButtons().getTextField();
		bNew = new FrameTemplateButtons().RibbonJButton("Nowy DV", FrameTemplateImageIcon.iconJButton_PackAdd());
		bNew.addActionListener(this);
		JPanel up = new JPanel(new BorderLayout());
			up.add(new FrameTemplate().GetUpMenu(false), BorderLayout.PAGE_START);
			up.add(dv.getRibbon(), BorderLayout.PAGE_END);
		add(up, BorderLayout.PAGE_START);
		JPanel down = new JPanel(new BorderLayout());
			JPanel treeJP = new JPanel(new BorderLayout());
				treeJP.add(new JScrollPane(tree), BorderLayout.CENTER);
				treeJP.setBorder(new TitledBorder(""));
			down.add(treeJP, BorderLayout.LINE_START);
			down.add(dv.getJPanelTable(), BorderLayout.CENTER);
		add(down, BorderLayout.CENTER);	
			for (int i = 0; i < tree.getRowCount(); i++) {
			    tree.expandRow(i);
			}
			
			tree.addTreeSelectionListener(this);
	
	}
	
	class dvClass extends FrameTemplateTableNew implements TableModelListener
	{

		public dvClass(String yamlSys, String modelProcedure, ArrayList<String> modelParameters, int enableType,
				boolean isCheck) {
			super(yamlSys, modelProcedure, modelParameters, enableType, isCheck);
			// TODO Auto-generated constructor stub
 
	 		
		}

		protected JTabbedPane getRibbon()
		{
				JTabbedPane jtp = new JTabbedPane();
				JPanel sim = new JPanel(new FlowLayout(FlowLayout.LEFT));
					sim.add(frame.DefaultRibbonSim());
					JPanel bn = new JPanel(new FlowLayout(FlowLayout.LEFT));
					sim.add(bn.add(bNew));
				jtp.add("Opcje", sim);
				JPanel exp = new JPanel(new FlowLayout(FlowLayout.LEFT));
					exp.add(frame.DefaultRibbonExp());
				jtp.add("Eksport", exp);
			return jtp;
		}
		protected JPanel getJPanelTable()
		{
			JPanel panel = new JPanel(new BorderLayout());
				JPanel up = new JPanel(new FlowLayout(FlowLayout.LEFT));
					up.add(new JLabel("DomainName:"));
					up.add(domain);
				panel.add(up, BorderLayout.PAGE_START);
				panel.add(new JScrollPane(table), BorderLayout.CENTER);
			return panel;
		}
		
		protected void addListenersDV()
		{
			model.addTableModelListener(this);
			addListeners(new ArrayList<Integer>(Arrays.asList(0,1,2)));
			frame.addListenerRibbon();
		}
		
		@Override
		public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			if (e.getType()==1)
			{
				model.setValueAt(domain.getText(), e.getFirstRow(), colM.get("DomainName"));
			}
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		 DefaultMutableTreeNode node = (DefaultMutableTreeNode)
		 tree.getLastSelectedPathComponent();
		 Object nodeInfo = node.getUserObject();
		 TreePath nodes = e.getNewLeadSelectionPath();
		 if (node == null) return;
		 if (node.getChildCount()>0)
		{
		String lastNode= String.valueOf(e.getNewLeadSelectionPath().getLastPathComponent());
					 /// System.out.print(idNode);
					  domain.setText(lastNode);
					  dv.newModel(new ArrayList<String>(Arrays.asList(lastNode)));
					
					  dv.addListenersDV();
					 
					  
				  }
				   
			
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==bNew)
		{
			String strfiltr =JOptionPane.showInputDialog(null,"WprowadŸ szukan¹ frazê", "");
			if (strfiltr.trim().length()>0)
			{
				 dv.newModel(new ArrayList<String>(Arrays.asList(strfiltr)));
				 dv.addListenersDV();
				 domain.setText(strfiltr);
				 
			}
		}
	}
	


}
