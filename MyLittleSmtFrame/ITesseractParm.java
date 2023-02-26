package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelListener;

import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateTableNew;
import MyLittleSmt.FrameTemplateWindow;

public class ITesseractParm extends FrameTemplateWindow {
	private tableClass tab;
	private parmClass parm;
	public ITesseractParm(int x, int y, String title) {
		super(x, y, title);
		tab = new tableClass("ITesseract_tableInvoice_sys.yml", "getTesseractTable", new ArrayList<String>(), 0, false);
		parm = new parmClass("ITesseract_ParmInvoice_sys.yml", "getTesseractPar", new ArrayList<String>(), 0, false);
		
		JPanel up = new JPanel(new BorderLayout());
			up.add(new FrameTemplate().GetUpMenu(false), BorderLayout.PAGE_START);
		add(up, BorderLayout.PAGE_START);
		JPanel down = new JPanel(new GridLayout(1,2,1,1));
			down.add(tab.getPanel());
			down.add(parm.getPanel());
		add(down, BorderLayout.CENTER);
		
	}
	class tableClass extends FrameTemplateTableNew  
	{

		public tableClass(String yamlSys, String modelProcedure, ArrayList<String> modelParameters, int enableType,
				boolean isCheck) {
			super(yamlSys, modelProcedure, modelParameters, enableType, isCheck);
			 
		}
		protected JPanel getPanel()
		{
			
			 JPanel result = new JPanel(new BorderLayout());
			 	result.add(frame.getSmallButtons(), BorderLayout.PAGE_START);
			 	result.add(new JScrollPane(table), BorderLayout.CENTER);
			 	addListeners(new ArrayList<Integer>(Arrays.asList(0,1)));
			 	frame.addListenerSmallButtons();
			 return result;

		}
		
	}
	class parmClass extends FrameTemplateTableNew  
	{

		public parmClass(String yamlSys, String modelProcedure, ArrayList<String> modelParameters, int enableType,
				boolean isCheck) {
			super(yamlSys, modelProcedure, modelParameters, enableType, isCheck);
			 
		}
		protected JPanel getPanel()
		{
			
			 JPanel result = new JPanel(new BorderLayout());
			 	result.add(frame.getSmallButtons(), BorderLayout.PAGE_START);
			 	result.add(new JScrollPane(table), BorderLayout.CENTER);
			 addListeners(new ArrayList<Integer>(Arrays.asList(0,1,2,3)));
			 frame.addListenerSmallButtons();
			 	
			 return result;
			 

		}
	}
}
