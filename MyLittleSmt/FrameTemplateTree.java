package MyLittleSmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class FrameTemplateTree {
	
	public JTree reportTree(String title, String procedure, ArrayList<String> parameters)
	{
		ArrayList<ArrayList<String>> groups = new StoredProcedures().genUniversalArray(procedure, parameters);
		DefaultMutableTreeNode report = new DefaultMutableTreeNode(procedure);
		JTree tree=new JTree(report);
 
		HashMap<String, ArrayList<DefaultMutableTreeNode>> groupMap = new HashMap<String, ArrayList<DefaultMutableTreeNode>>();
	    for (int i=1;i<groups.size();i++)
	    {
	    		 if (groupMap.containsKey(groups.get(i).get(0)))
	    		 {
	    			 groupMap.get(groups.get(i).get(0)).add(new DefaultMutableTreeNode(groups.get(i).get(1) ));
	    		 }else
	    		 {
	    			 groupMap.put(groups.get(i).get(0), new ArrayList<DefaultMutableTreeNode>(Arrays.asList(new DefaultMutableTreeNode(groups.get(i).get(1)))));
	    		 }
	    }
	    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
	    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
	   
		for (String key:groupMap.keySet())
		{
			DefaultMutableTreeNode main=new DefaultMutableTreeNode(key);  
			for (int i=0;i<groupMap.get(key).size();i++)
			{
				main.add(new DefaultMutableTreeNode(groupMap.get(key).get(i)));
			}
			model.insertNodeInto(main, root, root.getChildCount()); 
		}
		
		return tree;
	}
}
