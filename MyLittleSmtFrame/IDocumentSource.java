package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelListener;

import MyLittleSmt.DocumentsSource;
import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateTableNew;
import MyLittleSmt.FrameTemplateWindow;

public class IDocumentSource  extends FrameTemplateWindow{
	private tableClass tab;
	public IDocumentSource(int x, int y, String title, String firm) {
		super(x, y, title);
		// TODO Auto-generated constructor stub
		tab = new tableClass("IDocumentSourceFrame_sys.yml", "getDocumentSourceFrame", new ArrayList<String>(Arrays.asList(firm)), 1, false);
		add(new FrameTemplate().GetUpMenu(false), BorderLayout.PAGE_START);
		add(tab.getPanel(), BorderLayout.CENTER);
	}
	
	class tableClass extends FrameTemplateTableNew  implements ActionListener
	{
		private JMenuItem addSour, delSour;
		public tableClass(String yamlSys, String modelProcedure, ArrayList<String> modelParameters, int enableType,
				boolean isCheck) {
			super(yamlSys, modelProcedure, modelParameters, enableType, isCheck);
			// TODO Auto-generated constructor stub
			frame.sourceContextMenu();
			addListeners(new ArrayList<Integer>(Arrays.asList(0)));
			frame.removeListenersAddSour();
			addSour = frame.getAddSour();
			delSour = frame.getDelSour();
	 		addSour.addActionListener(this);
	 		delSour.addActionListener(this);
	 		frame.getaddRowM().setEnabled(false);
	 		frame.getdeleteM().setEnabled(false);
	 		frame.setkeyCol(0);
		}
		
		public JPanel getPanel()
		{
			JPanel panel = new JPanel(new BorderLayout());
				panel.add(new JScrollPane(table));
			return panel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (table.getSelectedColumn()>-1&&table.getSelectedRow()>-1)
			{
				colT = getColT();

				if (e.getSource()==addSour)
				{	
					String id = table.getValueAt(table.getSelectedRow(), colT.get("ID")).toString();
					String firm = table.getValueAt(table.getSelectedRow(), colT.get("FIRM")).toString();
					DocumentsSource ds = new DocumentsSource();
					ds.addSource(id, firm);
					table.setValueAt(ds.getFileName(), table.getSelectedRow(), colT.get("dPATH"));
				}else if (e.getSource()==delSour)
				{
					table.setValueAt("", table.getSelectedRow(), colT.get("dPATH"));
				}
			}
		}
	}
	  
}
