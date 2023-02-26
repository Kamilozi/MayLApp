package MyLittleSmt;

import java.util.ArrayList;
import java.util.HashMap;


import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class FrameTemplateAlterModel implements TableModelListener {
	private DefaultTableModel Model, newModel;
	private HashMap<String, ArrayList<String>>   newSysMapAll, mapSysAll;
	private HashMap<String,String>   newMapBase, newSysMap;
	private HashMap<Integer, ArrayList<Integer>> keyModel;
	private HashMap<Integer, DefaultTableModel> mapModel;
	private ArrayList<Integer> KeyList;
 
	
	private boolean isListenerEnabled;
	
	/**
	 * Generacja podstawowywch s³owników Modelowych
	 * @param mapSys
	 * @param mapBase
	 * @param sysMapAll
	 * @param Column - Lista column bazowych dla nowego Modelu
	 */
	public void genModelHashMaps(HashMap<String, String> mapSys, HashMap<String, String> mapBase,
								HashMap<String, ArrayList<String>> sysMapAll, ArrayList<String> Column)
	{
	newSysMap = new HashMap<String, String>();
	newMapBase = new HashMap<String, String>();
	newSysMapAll = new HashMap<String, ArrayList<String>>();
	
		
		for (String i: mapSys.keySet())
		{
			if(Column.contains(i))
			{
				newSysMap.put(i, mapSys.get(i));
				newMapBase.put(i, mapBase.get(i));
				newSysMapAll.put(mapSys.get(i), sysMapAll.get(mapSys.get(i)));
			}
		}
		
	}
	/**
	 * Modyfikator do s³ownika z nazwami kolumn
	 * @return <Bazowa, TableaWBazie>
	 */
	public HashMap<String, String> getNewMapBase()
	{
		return newMapBase;
	}
	/**
	 * Modyfikatro dosêpu do s³ownika z nazwami kolumn
	 * @return <Bazowa, Tableowa> nazwa kolumn
	 */
	public HashMap<String, String > getNewMapSys()
	{
		return newSysMap;
	}
	/**
	 * Modyfikator dostêpu do s³ownika z w³aœciwoœciaimi kolumny
	 * @return <Tabelowa <Bazowa, Czy Null, Typ zmiennej, Dane dot zmiennej, czy s³ownik>>
	 */
	public HashMap<String, ArrayList<String>> getNewSysMapAll()
	{
		return newSysMapAll;
	}
	

	
	/**
	 * Generacja nowego Modelu bazuj¹cego na innym Modelu. 
	 * @param Model
	 * @param columns
	 * @param newSysMap
	 * @param ModType - okreœla metodê dostêpnoœci modelu. 0-pe³na edycja, 1-pe³ne zablokowanie, 2-zablokowane komórki (potrzeba uzupe³nienia DisableColumn)
	 * @return nowy model 
	 */
	public DefaultTableModel genModelFromModel(int  ModType, ArrayList<Integer> DisableColumn,DefaultTableModel Model, ArrayList<String> columns, 
												HashMap<String, String> newSysMap)
	{
		DefaultTableModel resultModel= new DefaultTableModel(){
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				if (ModType==1)
				{
					return false;
				}else if (ModType==2)
				{
					if (Model.getValueAt(row, 2).equals("EUR"))
					{
						return false;
					}else
					{
						return true;
					}
				}else if (ModType==3)
				{
					if (column==1)
					{
						return false;
					}else 
					{
						return true;
					}
				
				}
				else
				{
					return true;
				}
	        }};
		
		for (int i=0; i<columns.size();i++)
		{
			for (int j=0; j<Model.getColumnCount(); j++)
			{
				if (Model.getColumnName(j).equals(newSysMap.get(columns.get(i))))
				{
					resultModel.addColumn(newSysMap.get(columns.get(i)));
					 
				}
			}
			
		}
		return resultModel;
	}
	/**
	 * Dodaje dane do modelu tabeli z uwzglêdnieniem pomijania powtórek
	 * @param Model - model g³ówny
	 * @param newModel - model pochodny 
	 * @param TableKey - numery kolumn zapewniaj¹cych unikalnoœæ w tabeli newModel
	 * @return
	 */
	
	
public DefaultTableModel genDataToModelDistinctKey(	DefaultTableModel Model, 
													DefaultTableModel newModel, 
													ArrayList<Integer> TableKey)
{
	DefaultTableModel UniqModel = new DefaultTableModel();
	ArrayList<Integer> ColNumMod = new ArrayList<Integer>();
	for (int i=0; i<newModel.getColumnCount();i++)
	{
		for (int j=0; j<Model.getColumnCount();j++)
		{
			if (Model.getColumnName(j).equals(newModel.getColumnName(i)))
			{
				ColNumMod.add(j);
			}
			
		}
	}
	for (int i=0;i<Model.getColumnCount();i++)
	{
		UniqModel.addColumn(Model.getColumnName(i));
	}
	
	ArrayList<ArrayList<String>> uniqList = new ArrayList<ArrayList<String>>();
	for (int i=0; i<Model.getRowCount();i++)
	{
		ArrayList<String> tempKey = new ArrayList<String>();
		for (int j=0; j<ColNumMod.size();j++)
    {
			tempKey.add(Model.getValueAt(i, ColNumMod.get(j)).toString());
    }

		if (!uniqList.contains(tempKey))
		{
			Object[] tempObj = new Object[Model.getColumnCount()];
			for (int q=0; q<Model.getColumnCount();q++)
			{
				tempObj[q] = Model.getValueAt(i, q);
			}
			UniqModel.addRow(tempObj);
			uniqList.add(tempKey);
		}
	}
	
	
	return genDataToModel(UniqModel,newModel, TableKey);

}
	
	
	
	public DefaultTableModel genDataToModel(DefaultTableModel Model, DefaultTableModel newModel, ArrayList<Integer> TableKey)
	{
		
		isListenerEnabled(false);
		ArrayList<ArrayList<String>> keyList = null;
		if (!TableKey.isEmpty())
		{
			//Tworzenie TableKey dla modelu pochodnego na podstawie TableKey Modelu g³ownego
			ArrayList<Integer> ColNumTabKey = new ArrayList<Integer>();
			for(int i=0;i<TableKey.size();i++)
			{
				for (int k=0;k<newModel.getColumnCount();k++)
				{
					if (newModel.getColumnName(k).equals(Model.getColumnName(TableKey.get(i))))
						{
						ColNumTabKey.add(k);
						}
				}
			}
			//Wywo³ywanie ³adowanie listy unikalnych kluczy dla ModeluPochodnego
			keyList = isUniqList(newModel, TableKey);
		}
		
		for (int i=0; i<Model.getRowCount(); i++)
		{
			boolean isReapit = false;
			
			if (!keyList.isEmpty())
			{
				ArrayList<String> checkList = new ArrayList<String>();
				for (int j=0; j<TableKey.size();j++)
				{
					for (int k=0; k<Model.getColumnCount();k++)
					{
						if (Model.getColumnName(k).equals(newModel.getColumnName(TableKey.get(j))))
						{
							checkList.add(Model.getValueAt(i, k).toString());
						}
					}
					
				}
				isReapit = keyList.contains(checkList);
			}
			
			
			if (isReapit==false)
			{
				Object[] tempList =new Object[newModel.getColumnCount()];
				for (int j=0;j<newModel.getColumnCount();j++)
				{
					for (int k=0;k<Model.getColumnCount();k++)
					{
						if (Model.getColumnName(k).equals(newModel.getColumnName(j)))
						{
							tempList[j] = Model.getValueAt(i, k);
						}
					}
				}
				newModel.addRow(tempList);
			}
		}
		
		
		isListenerEnabled(true);
		return newModel;
	}

	/**
	 * Zwraca listê kluczy w Modelu pochodnym
	 * @param newModel 
	 * @param TableKey
	 * @return unikalna lista kluczy
	 */
	private ArrayList<ArrayList<String>> isUniqList(DefaultTableModel newModel, ArrayList<Integer> TableKey)
	{
		ArrayList<ArrayList<String>> uniqList = new ArrayList<ArrayList<String>>();
		for (int j=0; j<newModel.getRowCount(); j++)
		{
			ArrayList<String> temp = new ArrayList<String>();
			for (int i=0; i<TableKey.size();i++)
			{									
				temp.add(newModel.getValueAt(j, TableKey.get(i)).toString());
			}
			uniqList.add(temp);
		}
		return uniqList;
		
	}
	
	public void addMyModelListener(DefaultTableModel Model, DefaultTableModel newModel, 
									HashMap<Integer, DefaultTableModel> mapModel, 
									HashMap<Integer, ArrayList<Integer>> keyModel,
									HashMap<String, ArrayList<String>> mapSysAll,
									ArrayList<Integer> KeyList)
	{
		this.Model = Model;
		this.newModel = newModel;
		this.mapModel= mapModel;
		this.keyModel = keyModel;	
		this.mapSysAll = mapSysAll;
		this.KeyList = KeyList;

	}

	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		if (isListenerEnabled!=false)
		{
			if (e.getType()==1)
			{
				addRowToModel(e.getLastRow());
				
			}else if (e.getType()==0)
			{
				modifiModel(e.getFirstRow(), e.getColumn());
			}else if (e.getType()==-1)
			{
				deleteFromModel(e.getFirstRow());
			}
		}
	}
	
	/**
	 * Wy³¹cznik monitorowania zmian. Konieczny podczas ponownego za³adowania Modeli pochodnych danymi z modelu g³ownego.
	 * @param isEnabled
	 */
	public void isListenerEnabled(boolean isEnabled)
	{
		this.isListenerEnabled = isEnabled;
	}
	
	public void addRowToModel(int AddRowNum)
	{
		new FrameTemplateListener(null, null, null, null, null, null).addEmtpyRow(AddRowNum, Model, mapSysAll,null);
	}
	/**
	 * Pobieram usuwany klucz i sprawdzam go z Modelem Bazowym
	 * @param DelRowNum - numer usuwanego wiersza z modelu bazowego
	 */
	public void deleteFromModel(int DelRowNum)
	{
		ArrayList<ArrayList<Object>> delItem =  FrameTemplateDataWarehouse.getOneDeleteItem();
		ArrayList<String> delKey = genKeyListFromObject(delItem);
		ArrayList<String> delCol = getKeyListColumnFromObject(delItem);
		ArrayList<Integer> numModelCol = new ArrayList<Integer>();	
		for (int j=0;j<delCol.size();j++)
		{
			for(int i=0; i<Model.getColumnCount();i++)
			{
				if (Model.getColumnName(i).equals(delCol.get(j)))
				{
					numModelCol.add(i);
				}
			}
		}
		for (int i=0;i<Model.getRowCount();i++)
		{
			ArrayList<String> temp = new ArrayList<String>();
			for(int j=0;j<numModelCol.size();j++)
			{
			  temp.add(Model.getValueAt(i, numModelCol.get(j)).toString());
			}
			if (temp.equals(delKey))
			{
				Model.removeRow(i);
				break;
			}
		}
	}
	
	/**
	 * Metoda pomocnicza generuj¹ca klucz z podanego itemu na podstawie danych (ListKey i newModel) modelu pochodnego
	 * @param item - usuwany wiersz
	 * @return klucz
	 */
	private ArrayList<String> genKeyListFromObject(ArrayList<ArrayList<Object>> item)
	{
		ArrayList<String> RowKey = new ArrayList<String>();
		for (int i=0;i<KeyList.size();i++)
		{
			for (int j=1; j<item.size();j++)
			{
				for (int k=0;k<item.get(j).size(); k++)
				{
					if (item.get(0).get(k).equals(newModel.getColumnName(KeyList.get(i))))
					{
						RowKey.add(item.get(j).get(k).toString());
					}
						
				}
			}
		}
		return RowKey;
	}
	private ArrayList<String> getKeyListColumnFromObject(ArrayList<ArrayList<Object>> item)
	{
		ArrayList<String> ColumnKey = new ArrayList<String>();
		for (int i=0;i<KeyList.size();i++)
		{
			for (int j=0; j<item.get(0).size();j++)
			{
				if (item.get(0).get(j).equals(newModel.getColumnName(KeyList.get(i))))
				{
					ColumnKey.add(item.get(0).get(j).toString());
				}
			}
		}
		return ColumnKey;
	}	
	private void modifiModel(int editRow,int editCol)
	{

		for (int i=0; i<Model.getColumnCount();i++)
		{
			if (Model.getColumnName(i).equals(newModel.getColumnName(editCol)))	
			{
				Object value;
				if (mapSysAll.get(Model.getColumnName(i)).get(2).equals("numeric"))
				{
					value = FrameTemplate.setDigits(newModel.getValueAt(editRow, editCol).toString());	
				}else
				{
					value = newModel.getValueAt(editRow, editCol);
				}
				Model.setValueAt(value, editRow, i);
			}
		}
		
 
	 

		for (Integer i: keyModel.keySet())
		{
		//je¿eli edytowana kolumna jest jedn¹ ze s³ownikowych
			if (keyModel.get(i).contains(editCol))
			{
			//tworzenie klucza edytowanej linii
			ArrayList<String> EditKey = new ArrayList<String>();
				for (int j=0; j<keyModel.get(i).size();j++)
				{
						EditKey.add(newModel.getValueAt(editRow, keyModel.get(i).get(j)).toString());
				}
			//dynamiczne tworzenie s³onika kluczy w modelu s³ownikowym
				ArrayList<Integer> KeyListDict = new ArrayList<Integer>(); 
				for (int k=0; k<keyModel.get(i).size();k++)
				{
					for (int j=0;j <mapModel.get(i).getColumnCount();j++)
					{
						if(mapModel.get(i).getColumnName(j).equals(newModel.getColumnName(keyModel.get(i).get(k))))
						{
							KeyListDict.add(j);
						}
					}
				}
			//pêtla po modelu s³ownikowym
				for (int j=0; j<mapModel.get(i).getRowCount();j++)
				{
					//tworzenie klucza badanego wiersza modelu s³ownikowego
					ArrayList<String> RowKey = new ArrayList<String>();

					for (int k=0;k<KeyListDict.size();k++)
					{
						RowKey.add(mapModel.get(i).getValueAt(j, KeyListDict.get(k)).toString());
					}
					
					///
					if (RowKey.equals(EditKey))
					{
						for (int q=0; q<mapModel.get(i).getColumnCount();q++)
						{
							for(int k=0; k<Model.getColumnCount();k++)
							{
								if (Model.getColumnName(k).equals(mapModel.get(i).getColumnName(q)))
								{
									Model.setValueAt(mapModel.get(i).getValueAt(j, q), editRow, k);
								}
							}
						}
					}
				}
			}
		}
		
	}

}

