package MyLittleSmt;

import java.util.ArrayList;


public class FrameTemplateDataWarehouse {
private static ArrayList<ArrayList<Object>> oneDeleteItem;

public static void setOneDeleteItem(ArrayList<ArrayList<Object>> item)
{
	oneDeleteItem = item;
}
	
public static ArrayList<ArrayList<Object>> getOneDeleteItem()
{
	return oneDeleteItem;
}
}
