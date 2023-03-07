package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import MyLittleSmt.FrameTemplate;

public class IReckoning 
{
	private boolean isLed;
	private ArrayList<Object[]> postingList;
	public IReckoning(boolean isLed, String firm, String posId,String altDate,String docDate,String setDate,String oType, String currency)
	{
		this.isLed=isLed;
		String procedure = isLed==true ? "getLedgerReckoning" : "getQTYReckoning";
		String yamlSys = isLed==true ? "LedgerReckoning_sys.yml" : "QTYReckoning_sys.yml";
		openFrame window = new openFrame(true, yamlSys, procedure,isLed==true ? new ArrayList<String>(Arrays.asList(firm,currency)) : new ArrayList<String>(Arrays.asList(firm)),16,
				"Rozliczenie", new ArrayList<Integer>(Arrays.asList(0)));
		window.setVisible(true);
		if (window.getCancel()==false)
		{
			ArrayList<ArrayList<Object>> resultList = window.getResultList();
			HashMap<String, Integer> columnResult = window.getColumnResult();
			postingList = new ArrayList<Object[]>();
		 
			
			for (int i = 1;i<resultList.size();i++)
			{
			if (isLed==true)
			{
		 
				Object[] data = {posId,firm, 0, docDate, altDate,setDate, "", resultList.get(i).get(columnResult.get("Account")), resultList.get(i).get(columnResult.get("AMOUNTCUR")),
								((boolean)resultList.get(i).get(columnResult.get("CREDITING"))==true ? false : true), 
								resultList.get(i).get(columnResult.get("CURRENCYCODE")), 
								0, 
								 resultList.get(i).get(columnResult.get("AMOUNTMST")),resultList.get(i).get(columnResult.get("DIMENSION")),resultList.get(i).get(columnResult.get("DIMENSION_2")),
								 resultList.get(i).get(columnResult.get("DIMENSION_3")), resultList.get(i).get(columnResult.get("DIMENSION_4")),"", resultList.get(i).get(columnResult.get("SUBACCOUNT")), oType, false};
				postingList.add(data);
			}else if (isLed==false)
			{
				Object[] data = {posId,firm, 0, docDate, altDate,setDate, "", resultList.get(i).get(columnResult.get("Account")), resultList.get(i).get(columnResult.get("QTYAMOUNT")),
						((boolean)resultList.get(i).get(columnResult.get("CREDITING"))==true ? false : true), resultList.get(i).get(columnResult.get("UNIT")), resultList.get(i).get(columnResult.get("UNITPRICE")), 
						 resultList.get(i).get(columnResult.get("DIMENSION")),resultList.get(i).get(columnResult.get("DIMENSION_2")),
						 resultList.get(i).get(columnResult.get("DIMENSION_3")), resultList.get(i).get(columnResult.get("DIMENSION_4")), resultList.get(i).get(columnResult.get("SUBACCOUNT")), oType, false,""};
				postingList.add(data);
			}
			
			}
		}
	}
	
	
	public ArrayList<Object[]> getPostingList()
	{
		return postingList;
	}
	class openFrame extends ISimpleChange  implements ActionListener, WindowListener, MouseListener, TableModelListener
	{
		private JButton bOk;
		private boolean cancel=false; 
		private HashMap<String, Integer> columnTable, columnResult; 
		private JTextField tfSumB, tfSumA;
		private ArrayList<ArrayList<Object>> resultList;
		private ArrayList<ArrayList<String>> insDictList;
		public openFrame(boolean isCheck, String yamlSys, String procedure, ArrayList<String> procParameters, int accesType,
				String title, ArrayList<Integer> Key) {
			super(isCheck, yamlSys, procedure, procParameters, accesType, title, Key);
			// TODO Auto-generated constructor stub
			frame.getbSave().setEnabled(false);
			frame.getbAdd().setEnabled(false);
			table.addMouseListener(this);
			bOk = new JButton("Rozlicz");
			bOk.setPreferredSize(new Dimension(125,20));
			bOk.addActionListener(this);
			tfSumA = new JTextField();
			tfSumA.setEnabled(false);
			tfSumA.setText("0,00");
			tfSumA.setPreferredSize(new Dimension(125,20));
			JPanel downDown = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			if (isLed==true)
			{
				tfSumB = new JTextField();
				tfSumB.setEnabled(false);
				tfSumB.setText("0,00");
				tfSumB.setPreferredSize(new Dimension(125,20));	
				downDown.add(new JLabel("Suma w walucie: "));
				downDown.add(tfSumA);
				downDown.add(new JLabel("Suma w bazowej: "));
				downDown.add(tfSumB);
			} else if (isLed==false)
			{
				downDown.add(new JLabel("Iloœæ: "));
				downDown.add(tfSumA);
			}
				
				downDown.add(bOk);
			downPanel.add(downDown, BorderLayout.PAGE_END);
			columnTable = frame.getColumnNumbers(table, sysAll);
			resultList = new ArrayList<ArrayList<Object>>();
			HashMap<Integer, String> dictColumnAndYaml = new HashMap<Integer, String>();
			dictColumnAndYaml.put((isLed==true ? 12 : 9), "IInstruments_Dict_Model.yml");
			frame.addDictToModel(dictColumnAndYaml, model);
			insDictList = frame.getOryginalDictList();
			model.addTableModelListener(this);
		}

		
		boolean getCancel()
		{
			return cancel;
		}
		
		ArrayList<ArrayList<Object>> getResultList()
		{
			return resultList;
		}
		HashMap<String, Integer> getColumnResult()
		{
			return columnResult;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource()==bOk)
			{
				 setResultList();
			}
		}

		@Override
		public void windowOpened(WindowEvent e) {}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			cancel=true;
		}

		@Override
		public void windowClosed(WindowEvent e) {}

		@Override
		public void windowIconified(WindowEvent e) {}

		@Override
		public void windowDeiconified(WindowEvent e) {}

		@Override
		public void windowActivated(WindowEvent e) {}

		@Override
		public void windowDeactivated(WindowEvent e) {}


		@Override
		public void mouseClicked(MouseEvent e) {}


		@Override
		public void mousePressed(MouseEvent e) {}


		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource()==table)
			{
				columnTable = frame.getColumnNumbers(table, sysAll);
				if (table.getSelectedColumn()==columnTable.get("Check"))
				{
					if (isLed == true) { tfCalculateLedger();} else{tfCalculateQTY();}
				}
			}
		}


		@Override
		public void mouseEntered(MouseEvent e) {}


		@Override
		public void mouseExited(MouseEvent e) {}
		
		private void tfCalculateLedger()
		{
			double baseValue = 0.00;
			double currValue = 0.00;
			columnTable = frame.getColumnNumbers(table, sysAll);
			for (int i=0;i<table.getRowCount();i++)
			{
				if ((boolean) table.getValueAt(i,columnTable.get("Check"))==true)
				{
					baseValue += Double.valueOf(table.getValueAt(i,columnTable.get("AMOUNTMST")).toString().replace(",", "."))* ((boolean)table.getValueAt(i,columnTable.get("CREDITING"))==true ? -1:1);
					currValue += Double.valueOf(table.getValueAt(i,columnTable.get("AMOUNTCUR")).toString().replace(",", "."))* ((boolean)table.getValueAt(i,columnTable.get("CREDITING"))==true ? -1:1);
				}
			}
			tfSumA.setText(String.valueOf(FrameTemplate.round(currValue, 2)).replace(".", ","));
			tfSumB.setText(String.valueOf(FrameTemplate.round(baseValue, 2)).replace(".", ","));	 
		}
		private void tfCalculateQTY()
		{
			double qtyValue = 0.00;
			columnTable = frame.getColumnNumbers(table, sysAll);
			for (int i=0;i<table.getRowCount();i++)
			{
				if ((boolean) table.getValueAt(i,columnTable.get("Check"))==true)
				{
					 
					qtyValue += Double.valueOf(table.getValueAt(i,columnTable.get("QTYAMOUNT")).toString().replace(",", ".")) * ((boolean)table.getValueAt(i,columnTable.get("CREDITING"))==true ? -1:1);
				}
			}
			tfSumA.setText(String.valueOf(FrameTemplate.round(qtyValue, 4)).replace(".", ",")); 
		}
		
		private void setResultList()
		{
			columnTable = frame.getColumnNumbers(table, sysAll);
			ArrayList<Object> header = new ArrayList<Object>();
			ArrayList<String> headerStr = new ArrayList<String>();
			for (int i=0; i<model.getColumnCount();i++)
			{
				header.add(model.getColumnName(i));
			 
			}
			resultList.add(header);
			columnResult = frame.getColumnNumbers(model, sysAll);
			
			for(int j=0;j<model.getRowCount();j++)
			{
				if ((boolean)model.getValueAt(j, columnResult.get("Check"))==true)
				{
					ArrayList<Object> data = new ArrayList<Object>();
					for (int i=0; i<model.getColumnCount();i++)
					{
						if (columnResult.get("DIMENSION_4")==i&&model.getValueAt(j, i).toString().trim().length()>0)
						{
							for (int k=0;k<insDictList.size();k++)
							{
								if (model.getValueAt(j, i).equals(insDictList.get(k).get(1)))
								{
									data.add(insDictList.get(k).get(0));
									break;
								}
							}
						}else
						{
							data.add(model.getValueAt(j, i));
						}
					}	
					resultList.add(data);
				}
			}
			dispose();
		}


		@Override
		public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			if (e.getType()==0)
			{
				columnTable = frame.getColumnNumbers(table, sysAll);
				if (isLed == true) 
				{
					if (e.getColumn()==columnTable.get("AMOUNTCUR")||e.getColumn()==columnTable.get("CREDITING"));
					{
						tfCalculateLedger();
					}
				} 
				else if (isLed == false) 
				{
					if (e.getColumn()==columnTable.get("QTYAMOUNT")||e.getColumn()==columnTable.get("CREDITING"));
					{
						tfCalculateQTY();
					}
				
				}
			}
		}
		
	 
	}
}


