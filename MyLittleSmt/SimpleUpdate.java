package MyLittleSmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SimpleUpdate {
	protected DefaultTableModel model;
	protected JTable table;
	protected HashMap<String, String> sysBase, sys;
	protected HashMap<String, ArrayList<String>> sysAll;
	protected FrameTemplate frame;
	protected ArrayList<Integer> key;
	protected HashMap<String, Integer> columnMap;
	protected ArrayList<String> parameters;
	protected String procedure;
	
	public SimpleUpdate(String yaml, String procedure, ArrayList<String> parameters, ArrayList<Integer> key)
	{
		frame = new FrameTemplate();
		frame.JTableHelperSys(yaml);
		sys = frame.getMapSys();
		sysBase = frame.getMapSysBase();
		sysAll = frame.getMapSysAll();
		model = getModel(procedure, parameters);
		table = frame.getDefaultTable(model, sysAll);
		frame.addFrameTempListener(table, model, sysAll, sysBase, sys, key);
		frame.addListenerJTable(table, model);
		columnMap();
	}
	
	protected DefaultTableModel getModel(String procedure, ArrayList<String> parameters)
	{
		return new StoredProcedures().genUniversalModel(false, procedure,parameters, 0, sys, sysAll);
	}
	
	protected void columnMap()
	{
		columnMap = frame.getColumnNumbers(model, sysAll);
	}
}
