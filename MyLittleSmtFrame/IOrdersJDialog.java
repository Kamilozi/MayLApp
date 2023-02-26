package MyLittleSmtFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import MyLittleSmt.AutoAccounting;
import MyLittleSmt.FrameTemplate;
import MyLittleSmt.FrameTemplateButtons;
import MyLittleSmt.FrameTemplateImageIcon;
import MyLittleSmt.KeyAdder;
import MyLittleSmt.StoredProcedures;

public class IOrdersJDialog extends ISimpleChange implements MouseListener, TableModelListener, ActionListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JTextField grossOrd;
		private JTextField nettoOrd;
		private JTextField postingOrd;
		private String firm, instr, regDate, conterparty;
		private JButton bPos, bSave, bAdd;

	
		
		public IOrdersJDialog(String yamlSys, String procedure, ArrayList<String> procParameters, int accesType,
				String title, ArrayList<Integer> key, String firm, String instr, String date, String conterparty ) {
			super(false, yamlSys, procedure, procParameters, accesType, title, key);
			this.firm = firm;
			this.instr = instr;
			this.regDate= date;
			this.conterparty = conterparty;
		 
			grossOrd = new JTextField();
			grossOrd.setPreferredSize(new Dimension(125,20));
			grossOrd.setEnabled(false);
			nettoOrd = new JTextField();
			nettoOrd.setPreferredSize(new Dimension(125,20));
			nettoOrd.setEnabled(false);
			postingOrd = new JTextField();
			postingOrd.setPreferredSize(new Dimension(125,20));
			postingOrd.setEnabled(false);
			bPos = new FrameTemplateButtons().RibbonJButton("Ksiegowania", FrameTemplateImageIcon.iconJButton_sbigPostring());
			bPos.addActionListener(this);
			JPanel downDownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				downDownPanel.add(new JLabel("Netto:"));
				downDownPanel.add(nettoOrd);
				downDownPanel.add(new JLabel("Brutto:"));
				downDownPanel.add(grossOrd);
				downDownPanel.add(new JLabel("ID:"));
				downDownPanel.add(postingOrd);
				
			downPanel.add(downDownPanel, BorderLayout.PAGE_END);
			rOption.add(frame.DefaultRibbonData());
			rOption.add(bPos);
		//	rExport.add(frame.DefaultRibbonImp());
			
			addTablListe(key);
			
			// TODO Auto-generated constructor stub
		}
		
		public void addTablListe(ArrayList<Integer>key)
		{
			model.addTableModelListener(this);
			setListener(key);
			table.addMouseListener(this);
			frame.setMultipleMenuRun(new ArrayList<Integer>(Arrays.asList(1,3,14,16)));
			getDict("IOrders_sys_dict.yml");
			modelColumnMap();
			
			
			addRibbonListener();
			calPositionValue();
			calPostingID();
			bSave = frame.getbSave();
			frame.remListbSave();
			bSave.addActionListener(this);
			
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
			if (e.getSource()==table)
			{
				if (table.getColumnName(table.getSelectedColumn()).equals(sysMap.get("Instrument_ID"))&&e.getClickCount()==2)
				{
					frame.dictInstrumentsbyFirm(table,  firm);
				}else if (table.getColumnName(table.getSelectedColumn()).equals(sysMap.get("Descriptions"))&&e.getClickCount()==2)
				{
					frame.dictDescriptionALL(table);
				}else if (table.getColumnName(table.getSelectedColumn()).equals(sysMap.get("SellType"))&&e.getClickCount()==2)
				{
					frame.getSelectionRunWithParameters(table, "getDomainValue",new ArrayList<String>(Arrays.asList("'Dimension_2.Type'")), "Dict_DomainValue.yml", "Typ", 0);
				}else if (table.getColumnName(table.getSelectedColumn()).equals(sysMap.get("OwnerOrd"))&&e.getClickCount()==2)
				{
					frame.getSelectionRunWithParameters(table, "getDomainValue",new ArrayList<String>(Arrays.asList("'Dimension_3.InternalContractor'")), "Dict_DomainValue.yml", "Kontrahent wewnêtrzny", 0);
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

		@Override
		public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource()==model)
			{
				if( e.getType()==0&&
					(model.getColumnName(e.getColumn()).equals(sysMap.get("Qunatity"))
					||model.getColumnName(e.getColumn()).equals(sysMap.get("UnitPrice"))
					||model.getColumnName(e.getColumn()).equals(sysMap.get("Discount"))
					||model.getColumnName(e.getColumn()).equals(sysMap.get("VAT"))))
				{
					modRow(e.getFirstRow());
					calPositionValue();
					 
				}else if (e.getType()==1)
				{
					newRow(e.getFirstRow());
				}
			}
		}
		private void modRow(int row)
		{
			double Discount =Double.valueOf(model.getValueAt(row, 8).toString().replace(",", ".")) ;
			double quantity = Double.valueOf(model.getValueAt(row, 5).toString().replace(",", ".")) ;  
			double Unit = Double.valueOf(model.getValueAt(row, 7).toString().replace(",", ".")) ;
			double Vat = Double.valueOf(model.getValueAt(row, 11).toString().replace(",", ".")) ;
			
			double perUnit = FrameTemplate.round(((1-Discount/100)*Unit),2);
			double netValue = FrameTemplate.round(perUnit* (double)quantity,2);
			double VatValue = FrameTemplate.round((netValue/100)* Vat,2);
			double grosValue = FrameTemplate.round(((1+Vat/100)*netValue),2);
			//double grosSec= FrameTemplate.round(netValue+VatValue,2);
			model.setValueAt(FrameTemplate.setDigits(String.valueOf(perUnit)), row, modelColumnInt.get("NetUnitPrice"));
			model.setValueAt(FrameTemplate.setDigits(String.valueOf(netValue)), row, modelColumnInt.get("NetAmount"));
			model.setValueAt(FrameTemplate.setDigits(String.valueOf(VatValue)), row, modelColumnInt.get("VATAmount"));
			model.setValueAt(FrameTemplate.setDigits(String.valueOf(grosValue)), row, modelColumnInt.get("GrossAmount"));
 
		 
		}
		private void newRow(int row)
		{
			if (model.getValueAt(row, modelColumnInt.get("FIRM")).toString().trim().length()==0)
			{
				model.setValueAt(firm, row, modelColumnInt.get("FIRM"));
			}
			
			if (model.getValueAt(row, modelColumnInt.get("Instrument_ID")).toString().trim().length()==0)
			{
				model.setValueAt(instr, row, modelColumnInt.get("Instrument_ID"));
			}

			if (model.getValueAt(row, modelColumnInt.get("Lp")).toString().trim().length()==0)
			{
				model.setValueAt(model.getRowCount(), row, modelColumnInt.get("Lp"));
			}
			
			if (model.getValueAt(row, modelColumnInt.get("VAT")).toString().trim().length()==0)
			{
				double vat = 23.00;
				model.setValueAt(FrameTemplate.setDigits(String.valueOf(vat)), row, modelColumnInt.get("VAT"));
			}
			if (model.getValueAt(row, modelColumnInt.get("Status")).toString().trim().length()==0)
			{
				model.setValueAt((int) 0, row, modelColumnInt.get("Status"));
			}
			 
			//model.setValueAt(postingOrd.getText() , row, modelColumnInt.get("POSTINGID"));
		}
		private void calPositionValue()
		{
		 
				double nettoOrdV = 0;
				double grossOrdV = 0;
					for (int i=0;i<model.getRowCount();i++)
					{
						nettoOrdV += FrameTemplate.round(Double.valueOf(model.getValueAt(i, modelColumnInt.get("NetAmount")).toString().replace(",", ".")),2);
						grossOrdV += FrameTemplate.round(Double.valueOf(model.getValueAt(i, modelColumnInt.get("GrossAmount")).toString().replace(",", ".")),2);
					}
					nettoOrd.setText(String.valueOf(FrameTemplate.round(nettoOrdV,2)));
					grossOrd.setText(String.valueOf(FrameTemplate.round(grossOrdV,2)));
					
			 
		}
		private void calPostingID()
		{ 
			String postingid="";
			for (int i=0; i<model.getRowCount();i++)
			{
				if (model.getValueAt(i, modelColumnInt.get("POSTINGID")).toString().length()==10)
				{
					postingid =(String) model.getValueAt(i, modelColumnInt.get("POSTINGID"));
					break;
				}
			}
			postingOrd.setText(postingid);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource()==bPos)
			{	//String postingId = new KeyAdder().PostingID(, String firm)	
				seePostings();
			}else if (e.getSource()==bSave)
			{
				//addPosting();
				multiplePosting();
				frame.Button_Save();
			}
		}
		
		private void multiplePosting()
		{	//modelColumnInt
			ArrayList<ArrayList<String>> insPosMap = new ArrayList<ArrayList<String>>();
			for (int i=0; i<model.getRowCount();i++)
			{
				String firmM=(String)model.getValueAt(i, modelColumnInt.get("FIRM"));
				String instrM =(String) model.getValueAt(i, modelColumnInt.get("Instrument_ID"));
				String postM = (String) model.getValueAt(i, modelColumnInt.get("POSTINGID"));
				boolean isFind = false;
				for (int j = 0; j<insPosMap.size();j++)
				{	
					//jezeli firma=, instrument=, ale post<> i post napotkany<>""
					if (insPosMap.get(j).get(0).equals(firmM)&&insPosMap.get(j).get(1).equals(instrM)&&!insPosMap.get(j).get(1).equals(postM)&&!insPosMap.get(j).get(2).equals(""))
					{
						insPosMap.get(j).set(2, postM);
						isFind = true;
					}else if (insPosMap.get(j).get(0).equals(firmM)&&insPosMap.get(j).get(1).equals(instrM))
					{
						isFind = true;
					}
				}
				if (isFind==false)
				{
					insPosMap.add(new ArrayList<String>(Arrays.asList(firmM,instrM,postM==null? "" : postM)));
				}
			}
			
			
			if (insPosMap.size()>0)
			{
				ArrayList<ArrayList<String>> instrList = new StoredProcedures().genUniversalArray("getInstruments", new ArrayList<String>());
				HashMap<String, Integer> arrayColIns = frame.getColumnNumbers(instrList.get(0));
				ArrayList<ArrayList<String>> ordersList = new StoredProcedures().genUniversalArray("getOrderPostingListALL", new ArrayList<String>());
				HashMap<String, Integer> arrayColOrd = frame.getColumnNumbers(ordersList.get(0));
				for (int i=0; i<insPosMap.size();i++)
				{
					for (int j=1; j<instrList.size();j++)
					{
						if( instrList.get(j).get(arrayColIns.get("FIRM")).equals(insPosMap.get(i).get(0))&&instrList.get(j).get(arrayColIns.get("ID")).equals(insPosMap.get(i).get(1)))
						{
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
							insPosMap.get(i).add(LocalDate.parse((CharSequence) instrList.get(j).get(arrayColIns.get("Registration")), formatter).toString());
							insPosMap.get(i).add( instrList.get(j).get(arrayColIns.get("Counterparty")).toString());
							break;
						}
					}
					if (insPosMap.get(i).get(2).equals(""))
						for (int j=1; j<ordersList.size();j++)
						{	
							if (ordersList.get(j).get(arrayColOrd.get("FIRM")).equals(insPosMap.get(i).get(0))&&ordersList.get(j).get(arrayColOrd.get("Instrument_ID")).equals(insPosMap.get(i).get(1))) 
							{
								insPosMap.get(i).set(2, ordersList.get(j).get(arrayColOrd.get("POSTINGID")));
								break;
							}
						}
				multiplePostingS(insPosMap.get(i).get(2), insPosMap.get(i).get(1), insPosMap.get(i).get(3), insPosMap.get(i).get(0), insPosMap.get(i).get(4));	
				}
				
				
			}
			
		}
		private void multiplePostingS(String postingID, String instrumentID, String regDate, String firm, String counterparty)
		{
			//stworzenie nowego posting id i wprowadzenie do modelu
			 if (postingID.length()!=10)
			 {
				  try 
				  {
					postingID = new KeyAdder().PostingID(regDate, firm);
					postingOrd.setText(postingID);
					for (int i = 0; i<model.getRowCount();i++)
					{
						if (model.getValueAt(i, modelColumnInt.get("FIRM")).equals(firm)&&model.getValueAt(i, modelColumnInt.get("Instrument_ID")).equals(instrumentID))
						{
						model.setValueAt(postingID, i, modelColumnInt.get("POSTINGID"));
						}
					}
				  } 
				  catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 if (postingID.length()==10&&model.getRowCount()>0)
			 {
					for (int i = 0; i<model.getRowCount();i++)
					{
						if (model.getValueAt(i, modelColumnInt.get("FIRM")).equals(firm)&&model.getValueAt(i, modelColumnInt.get("Instrument_ID")).equals(instrumentID)) 
						{
						model.setValueAt(postingID, i, modelColumnInt.get("POSTINGID"));
						}
					}
					//tworzê pauto accounting i dwie listy -cody instrumentów i columny
					AutoAccounting posting = new AutoAccounting(firm, postingID, instrumentID, "NEWORD");
					Object[] argList = posting.getPostingList(true);
					HashMap<String, String> codeMap = posting.getCodeMap();
					//tworzê ju¿ konkretne ksiêgowania
					ArrayList<Object[]> postingList = new ArrayList<Object[]>();
					postingList.add(argList);
					
					//[1, 2, Lp, Data dokumentu, Data alternatywna, Data rozliczenia, Opis, Konto, Iloœæ, Ct, Jednostka, Cena jedn., Projekt, Wymiar 2, Wymiar 3, Kontrahent, SUBID, Typ, Zaksiêgowane, Komentarz]
					for (int i =0;i< model.getRowCount();i++)
					{
					if (model.getValueAt(i, modelColumnInt.get("FIRM")).equals(firm)&&model.getValueAt(i, modelColumnInt.get("Instrument_ID")).equals(instrumentID)) 
						{
						for (String code : codeMap.keySet())
						{
							if (code.equals("goodsForPurchase")||code.equals("goodsForSale"))
							{
							Object[] tempList = {postingID, firm, postingList.size(), regDate,regDate,regDate,
									instr,codeMap.get(code),  model.getValueAt(i, modelColumnInt.get("Qunatity")), code.equals("goodsForPurchase") ? false : true,
									model.getValueAt(i, modelColumnInt.get("Unit")), model.getValueAt(i, modelColumnInt.get("UnitPrice")), instrumentID,  model.getValueAt(i, modelColumnInt.get("SellType")),
									model.getValueAt(i, modelColumnInt.get("OwnerOrd")), counterparty ,postingID,  "NEWORD", true, model.getValueAt(i, modelColumnInt.get("Descriptions"))};
							postingList.add(tempList);
							}
						}
						}
					}
					posting.addAutoRowsToModel(postingList, true);
					
			 }	
			 int size_ =0;
			for (int i = 0; i<model.getRowCount();i++)
				{
					if (model.getValueAt(i, modelColumnInt.get("FIRM")).equals(firm)&&model.getValueAt(i, modelColumnInt.get("Instrument_ID")).equals(instrumentID)) 
					{
						size_++;
					}
				}
			 
			 if (postingID.length()==10 && size_<=0)
			 {
				 AutoAccounting posting = new AutoAccounting(firm, postingID, instr, "NEWORD");
				 posting.postingDispose();
			 }
		}
		
	 
		
		private void seePostings()
		{
			IPostings postings = new IPostings(500, 500, "Ksiêgowania projektu: " + instr , 
					postingOrd.getText(), firm,"NEWORD",false);
			setModal(false);
			postings.setVisible(true);
			dispose();
		} 
		private void addPosting()
		{
			String postingID = postingOrd.getText();
			 if (postingOrd.getText().length()!=10&&model.getRowCount()>0)
			 {
				  try 
				  {
					postingID = new KeyAdder().PostingID(regDate, firm);
					postingOrd.setText(postingID);
					for (int i = 0; i<model.getRowCount();i++)
					{
						model.setValueAt(postingID, i, modelColumnInt.get("POSTINGID"));
					}
				  } 
				  catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 if (postingOrd.getText().length()==10&&model.getRowCount()>0)
			 {
					for (int i = 0; i<model.getRowCount();i++)
					{
						model.setValueAt(postingID, i, modelColumnInt.get("POSTINGID"));
					}
					//tworzê pauto accounting i dwie listy -cody instrumentów i columny
					AutoAccounting posting = new AutoAccounting(firm, postingID, instr, "NEWORD");
					Object[] argList = posting.getPostingList(true);
					HashMap<String, String> codeMap = posting.getCodeMap();
					//tworzê ju¿ konkretne ksiêgowania
					ArrayList<Object[]> postingList = new ArrayList<Object[]>();
					postingList.add(argList);
					
					//[1, 2, Lp, Data dokumentu, Data alternatywna, Data rozliczenia, Opis, Konto, Iloœæ, Ct, Jednostka, Cena jedn., Projekt, Wymiar 2, Wymiar 3, Kontrahent, SUBID, Typ, Zaksiêgowane, Komentarz]
					for (int i =0;i< model.getRowCount();i++)
					{
						for (String code : codeMap.keySet())
						{
							if (code.equals("goodsForPurchase")||code.equals("goodsForSale"))
							{
							Object[] tempList = {postingID, firm, postingList.size(), regDate,regDate,regDate,
									instr,codeMap.get(code),  model.getValueAt(i, modelColumnInt.get("Qunatity")), code.equals("goodsForPurchase") ? false : true,
									model.getValueAt(i, modelColumnInt.get("Unit")), model.getValueAt(i, modelColumnInt.get("UnitPrice")), instr,  model.getValueAt(i, modelColumnInt.get("SellType")),
									model.getValueAt(i, modelColumnInt.get("OwnerOrd")), conterparty,postingID,  "NEWORD", true,model.getValueAt(i, modelColumnInt.get("Descriptions"))};
							postingList.add(tempList);
							}
						}
					}
					posting.addAutoRowsToModel(postingList, true);
					
			 }	
			 if (postingOrd.getText().length()==10 && model.getRowCount()<=0)
			 {
				 AutoAccounting posting = new AutoAccounting(firm, postingID, instr, "NEWORD");
				 posting.postingDispose();
			 }
		}

}
