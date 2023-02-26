package Outlook;

import Outlook.impTXTtoArray;
import Outlook.OneDrive;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Desktop;
import javax.swing.*;
import javax.swing.table.*;


public class TabelaProjekty extends JFrame implements ActionListener
{

private static final long serialVersionUID = 1L;
JTable tabela;
TableColumn col;
JButton SendMail;
Object[] rowdata;
static String OneDrivePath =  OneDrive.Enviroment("OneDrive");
static String filename = OneDrivePath + "\\THM_GROUP\\Projekty\\Techniczny\\tabela_projekty_podst.txt";

public TabelaProjekty() throws IOException 
{
	
	   try {
		   	UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); 
	    	} 
	   catch(Exception ignored){}
	     
	setTitle("Wybierz projekt");
	setSize(1000,500);
	setLayout(null);

     
    impTXTtoArray dane = new impTXTtoArray(filename);
    ArrayList<String> result = dane.impfrofiles();
	ArrayList<ArrayList<String>> array = dane.getSplit();
	ArrayList<ArrayList<String>> column = new ArrayList<ArrayList<String>>();

	Object[] header  = ArrayStringToObject(array.get(0));
	DefaultTableModel model = new DefaultTableModel();
	for (Object e : header)
		{
			model.addColumn(e);
		}
	 for (int i = 1; i+1  <= array.size(); i++)
	 {
		 Object[] temp_arr = ArrayStringToObject(array.get(i));
		 Object[] elements = new Object[temp_arr.length];
		 for (int v = 0; v+1<=temp_arr.length; v++ )
		 	{
			 	if (String.valueOf(temp_arr[v]).equals(String.valueOf(false)))
			 	{
			 		elements[v] = false;
			 	}
			 	else
			 	{
			 		elements[v] = temp_arr[v];
			 	}
		 	} 

		 model.addRow(elements); 
	 }	
	
	tabela = new JTable(model)	
	{
		
		public boolean editCellAt(int row, int column, java.util.EventObject e) 
		{
            return false;
        }   
        private static final long serialVersionUID = 1L;


        public  Class getColumnClass(int header) {
            switch (header) {
                case 0:
                    return Boolean.class;
                default:
                    return String.class;
            }
        }
    };
///
    tabela.addMouseListener(new MouseAdapter() 
    {
        public void mouseClicked(MouseEvent me) 
        {
  
      	  	JTable target = (JTable)me.getSource();
      	  	int row = target.getSelectedRow();  
      	  	int column = target.getSelectedColumn();
      	  
      	  	if (column== 0 ) 
      	  	{    
      	  		String val = tabela.getValueAt(row, column).toString();
      	  		if (val=="false")
      	  			tabela.setValueAt(true, row, column);
      	  		else if (val=="true")
      	  			{tabela.setValueAt(false, row, column);}
             }
           else if (me.getClickCount() == 2)
           	{	 
        	   if (column==1)
        		 
        	   {
        		   String FilePath = OneDrivePath +  "/THM_GROUP/Projekty/Aktywne/" + tabela.getValueAt(row, column);	;
        	   try {
        		   	OpenPath(FilePath);
        	   		} 
        	   catch  (Exception e)
        	   {	
        		   JOptionPane.showMessageDialog(null, "Nie znaleziono projektu");
        	   }
        	   
        	   }  
        	   else
        	   {
  	  			JOptionPane.showMessageDialog(null, tabela.getValueAt(row, column));
        	   }
         	   
           	}
        }
     });    
    
    col = tabela.getColumnModel().getColumn(1);
	col.setCellRenderer(new MyRenderer(Color.lightGray, Color.blue));

	
	JScrollPane sp =new JScrollPane(tabela);   
	sp.setBounds(5,20,900,380);
	add(sp);
	
	try {
		  tabela.setAutoCreateRowSorter(true);
		} catch(Exception continuewithNoSort) {}
	SendMail = new JButton("Wyœlij");
	SendMail.setBounds(850, 410, 80,30);
	add(SendMail);
	SendMail.addActionListener(this);
	
	

	
	
	//umieszczanie obrazu 
	
	//String arg;
    //arg = "C:/Users/kamil/Desktop/Przechwytywanie.PNG";
	//ImageIcon icon = new ImageIcon(arg);
	//JLabel label = new JLabel(); 
	//label.setIcon(icon);
	//label.setBounds(20,150,700,600);
	//(label);
	
	
	
	
}	


 
	public static void main(String[] args) throws IOException
	{ 
	TabelaProjekty app = new TabelaProjekty();
	app.setDefaultCloseOperation(HIDE_ON_CLOSE);
	app.setVisible(true);
	

	}
	
	public Object[] ArrayStringToObject(ArrayList<String> a)
	{
		Object[] str = new Object[a.size()];
		for (int i = 0; i < a.size(); i++)
		{
			str[i] = a.get(i);
		}
		return str;
	}
	
	public void OpenPath (String s) throws IOException
	{
		Desktop.getDesktop().open(new File(s));
	}



	/**
	 *
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object z = e.getSource();
		if (z==SendMail)
		{
			ArrayList<Map<String, ArrayList<String>>> sendList = new ArrayList<Map<String, ArrayList<String>>>();		
			Map<String, ArrayList<String>>map = new HashMap<String, ArrayList<String>>();
			for (int row = 0; row < tabela.getRowCount(); row++)
			{
				String val = tabela.getValueAt(row, 0).toString();
				
				if (val=="true")
				{
					ArrayList<String> projlist = new ArrayList<String>();
					projlist.add("\"" + (tabela.getValueAt(row, 1).toString().replace('"',' ')) + "\"");
					projlist.add("\"" + (tabela.getValueAt(row, 2).toString().replace('"',' ')) + "\"");
					//projlist.add("\"" + (tabela.getValueAt(row, 3).toString()) + "\"");
					//projlist.add("\"" + (tabela.getValueAt(row, 4).toString()) + "\"");
					//projlist.add(tabela.getValueAt(row, 6).toString().replace(',','.'));
					//projlist.add(tabela.getValueAt(row, 7).toString().replace(',','.'));
					map.put(tabela.getValueAt(row, 1).toString(), projlist);
					sendList.add(map);
					
					
					//int column = tabela.getSelectedColumn();
					//JOptionPane.showMessageDialog(null, tabela.getValueAt(row,2));
					//

					
					
					//
				}

			}
			if (sendList.size()>0)
			{
				SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
				Date date = new Date(System.currentTimeMillis());
				String sendSubject = "Wystawianie_faktur_" + formatter.format(date).toString() + " (nie zmieniaj tematu)";
				String FilePath = OneDrivePath + "\\THM_GROUP\\Projekty\\Techniczny\\YAML\\" + sendSubject + ".yml";
				generateTXT file = new generateTXT(FilePath);
				file.generate();
				StringBuffer sb = new StringBuffer();
				sb.append(map);
				String str = sb.toString();
				try 
				{
				FileWriter myWrite = new FileWriter(FilePath);
				
				myWrite.write("SUBJECT : " + sendSubject +"\n");
				myWrite.write("INVOICE : " + str.replace("=[", ":["));
				myWrite.close();
				
				//
				String command = "py \"" + OneDrivePath + "\\THM_GROUP\\Customowe\\Python\\CreateMail.py\"  \"" + FilePath + "\"";
				System.out.print(command);
				try 
				{
					Process p = Runtime.getRuntime().exec(command);
					dispose();
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//
				} catch (IOException e1) 
				{
				      e1.printStackTrace();
				      JOptionPane.showMessageDialog(null, "B³¹d 102. Skontaktuj siê z administratorem");
				}
				
			}
			else
			{JOptionPane.showMessageDialog(null, "Brak wybranych projektów");
			}
			
			
			

		}
		
	}
	
	
}


class MyRenderer extends DefaultTableCellRenderer 
{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Color bg, fg;
   public MyRenderer(Color bg, Color fg) {
      super();
      this.bg = bg;
      this.fg = fg;
   }
   public Component getTableCellRendererComponent(JTable table, Object 
   value, boolean isSelected, boolean hasFocus, int row, int column) 
   {
      Component cell = super.getTableCellRendererComponent(table, value, 
      isSelected, hasFocus, row, column);
      cell.setBackground(bg);
      cell.setForeground(fg);
      return cell;
   }
}

class generateTXT 
{
	private String path;
	public generateTXT(String s)
	{
		 this.path= s;
	}
	
	public void generate ()
	{
		try {
		      File myObj = new File(path);
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		    } 
		catch (IOException e1) 
			{ 
		      JOptionPane.showMessageDialog(null, "B³¹d 101. Skontaktuj siê z administratorem");
		      System.out.println("An error occurred.");
		      e1.printStackTrace();
		    }
	}
}

