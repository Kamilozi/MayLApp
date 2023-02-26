package MyLittleSmt;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FrameTemplateTableNew
	{
		protected FrameTemplate frame;
		private HashMap<String, String> sys, sysBase;
		private HashMap<String, ArrayList<String>> sysAll;
		public DefaultTableModel model;
		protected JTable table;
		private String modelProcedure;
		private int  enableType;
		private boolean isCheck;
		ArrayList<ArrayList<String>> cleanData; 
		protected HashMap<String, Integer> colM, colT;
		
		
		public FrameTemplateTableNew(String yamlSys, String modelProcedure, ArrayList<String> modelParameters, int enableType,boolean isCheck)
		{
			this.enableType= enableType;
			this.modelProcedure = modelProcedure;
			this.isCheck = isCheck;
			frame = new FrameTemplate();
			if (isCheck==true)
			{
				frame.JTableHelperSys(yamlSys, true);
			}else
			{
				frame.JTableHelperSys(yamlSys);
			}
			sys = frame.getMapSys();
			sysBase = frame.getMapSysBase();
			sysAll = frame.getMapSysAll();
			model =  isCheck==true ?  getModel(modelParameters, isCheck): getModel(modelParameters);
			table =frame.getDefaultTable(model, sysAll);
			colM = frame.getColumnNumbers(model, sysAll);
		}
		public FrameTemplateTableNew(String yamlSys, ArrayList<ArrayList<String>> list, int enableType,boolean isCheck)
		{
			this.enableType= enableType;
			 
			this.isCheck = isCheck;
			frame = new FrameTemplate();
			if (isCheck==true)
			{
				frame.JTableHelperSys(yamlSys, true);
			}else
			{
				frame.JTableHelperSys(yamlSys);
			}
			sys = frame.getMapSys();
			sysBase = frame.getMapSysBase();
			sysAll = frame.getMapSysAll();
			model =  getModel(list, enableType, sysAll, sys);
			table =frame.getDefaultTable(model, sysAll);
			colM = frame.getColumnNumbers(model, sysAll);
		}		
	    public HashMap<String, Integer> getColM()
	    {
	    	return colM;
	    }
	    
	    public HashMap<String, Integer> getColT()
	    {
	    	return frame.getColumnNumbers(table, sysAll);
	    }
	    public FrameTemplate getFrame()
		{
			return frame;
		}
		
		 public JTable getTable()
		{
			return table;
		}
		
		 public DefaultTableModel getModel()
		{
			return model;
		}
		 public HashMap<String, ArrayList<String>> getSysAll()
		{
			return sysAll;
		}
		private DefaultTableModel getModel(ArrayList<String> modelParameters)
		{
			var sp = new StoredProcedures();
			DefaultTableModel model = sp.genUniversalModel(modelProcedure, modelParameters, enableType, sys, sysAll);
			cleanData = sp.getListFromBase();
			return model;

		}
		
		private DefaultTableModel getModel(ArrayList<String> modelParameters, boolean isCheck)
		{
			var sp = new StoredProcedures();
			DefaultTableModel model = sp.genUniversalModel(isCheck, modelProcedure, modelParameters, enableType, sys, sysAll);
			cleanData = sp.getListFromBase();
			return model;

		}
		
		
		public void newModel(ArrayList<String> modelParameters)
		{
			this.model =  isCheck==true ?  getModel(modelParameters, isCheck): getModel(modelParameters);
			
			frame.setModel(model);
		}
		
		public void addRow(ArrayList<Object[]> list)
		{
			for (int i = 0; i<list.size();i++)
			{
				model.addRow(list.get(i));
			}
		}
		
		public void addListeners(ArrayList<Integer> keys)
		{
			frame.addFrameTempListener(table, model, sysAll, sysBase, sys, keys);
			frame.addListenerJTable(table, model);
			frame.addListenerContextMenu();
		}
		
		private DefaultTableModel getModel(ArrayList<ArrayList<String>> list, int enable,HashMap<String, ArrayList<String>> sysAll, HashMap<String, String> sys)
		{
			if (isCheck==true)
			{
				list.get(0).add(0, "Check");
				for (int i =1; i<list.size();i++)
				{
					list.get(i).add(0, "0");
				}
			}
			
			return new StoredProcedures().genModel(list, enable, sys, sysAll);
		}
		
	 
	}
