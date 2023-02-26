package MyLittleSmt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class ICalendar extends FrameTemplateWindow implements TableModelListener, ActionListener {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private FrameTemplate frameCalendar;
private PythonBase readers = new PythonBase();
private HashMap<String, String> mapsys, mapsysBase; 
private HashMap<String, ArrayList<String>> mapsysAll;
private DefaultTableModel Model;
private JTable JTCalendar;
private JButton bEdit, bSave;


	public ICalendar(int x, int y, String title) {
		super(x, y, title);
		frameCalendar = new FrameTemplate();
		frameCalendar.JTableHelperSys("ICalendar_sys.yml");
		mapsys = frameCalendar.getMapSys();
		mapsysAll = frameCalendar.getMapSysAll();
		mapsysBase = frameCalendar.getMapSysBase();
		
		
		Model = getData(1);
		Model.addTableModelListener(this);
		ArrayList<Integer> TableKey = new ArrayList<Integer>(Arrays.asList(0));
		
		JPanel upPanel = new JPanel(new BorderLayout());
		upPanel.add(frameCalendar.GetUpMenu(false), BorderLayout.PAGE_START);
		
			JTabbedPane JTPRibbon =new JTabbedPane();
				JPanel JPOpcje = new JPanel(new FlowLayout(FlowLayout.LEFT));
					JPanel CalOption = new JPanel();
					CalOption.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
						bEdit = new JButton("Edytuj", FrameTemplateImageIcon.iconJButton_Edit());
						bEdit.setVerticalTextPosition(SwingConstants.BOTTOM);
						bEdit.setHorizontalTextPosition(SwingConstants.CENTER);
						bEdit.setContentAreaFilled(false);
						bEdit.setBorderPainted(false);
						bEdit.addActionListener(this);
					CalOption.add(bEdit);
			    JPOpcje.add(CalOption);
				JPOpcje.add(frameCalendar.DefaultRibbonSim());
				frameCalendar.getbAdd().setEnabled(false);
				bSave = frameCalendar.getbSave();
				bSave.addActionListener(this);
			JTPRibbon.add("Opcje", JPOpcje);
				JPanel ImpExp = new JPanel(new FlowLayout(FlowLayout.LEFT));
				ImpExp.add(frameCalendar.DefaultRibbonExp());
				ImpExp.add(frameCalendar.DefaultRibbonImp());
			JTPRibbon.add("Dane", ImpExp);
		upPanel.add(JTPRibbon, BorderLayout.PAGE_END);
		
		
		JTCalendar = frameCalendar.getDefaultTable(Model, mapsysAll);
		JScrollPane JSPCalendar = new JScrollPane(JTCalendar);
		
		frameCalendar.addFrameTempListener(JTCalendar, Model, mapsysAll, mapsysBase, mapsys, TableKey);
		frameCalendar.addListenerContextMenu();
		frameCalendar.addListenerRibbon();
		frameCalendar.addListenerJTable(JTCalendar, Model);
		frameCalendar.AutSorterJTable(JTCalendar, 0, true);
		frameCalendar.addListenerModel(Model);
		add(upPanel, BorderLayout.PAGE_START);
		add(JSPCalendar);
	}
	
	
	private DefaultTableModel getData(int Enable)
	{
		
		try {
			DefaultTableModel Model = readers.getDataToJT(Enable, mapsys, mapsysAll, "ICalendar.yml");
			MainEntryDataWarehouse warehouse = new MainEntryDataWarehouse();
			warehouse.setCalendar(readers.getListFromBase());
			return Model;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}


	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub

		if (e.getColumn()==2 && Model.getValueAt(e.getFirstRow(), e.getColumn()).equals(true))
		{
			for (int i=e.getFirstRow(); i<Model.getRowCount();i++)
			{
				if (Model.getValueAt(i, 2).equals(false))
				{
					Model.setValueAt(true, i, 2);
				}
			}
			
		}else if (e.getColumn()==2 && Model.getValueAt(e.getFirstRow(), e.getColumn()).equals(false))
		{
	
			for (int i=e.getFirstRow(); i>= 0 ;i--)
			{
				if (Model.getValueAt(i, 2).equals(true))
				{
					Model.setValueAt(false, i, 2);
				}
			}
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
	 
		// TODO Auto-generated method stub
		if (bEdit==e.getSource())
		{
			
			
			
			Model = getData(0);
			JTCalendar.setModel(Model);
			frameCalendar.addListenerJTable(JTCalendar, Model);
			Model.addTableModelListener(this);
		} else if (bSave==e.getSource())
		{
			///frameCalendar.Button_Save();
			Model = getData(1);
			JTCalendar.setModel(Model);
		}
		
	}
	
	
	
}



