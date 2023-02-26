package MyLittleSmt;

import javax.sql.rowset.Joinable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import MyLittleSmt.FrameTemplate;

public class JpkVsBsFrame extends JFrame
{
Joinable BS;
	public JpkVsBsFrame() 
	{	
		 ImageIcon img = FrameTemplate.GetImg();
		 setIconImage(img.getImage());
		 setSize(1300,650);
		 setTitle("JPK VS Bank Statement");
		 setLayout(null);
		 
		 
	}
	
	 public static void main(String [] args)
	 {
		 JpkVsBsFrame okienko = new JpkVsBsFrame();
		 okienko.setVisible(true);
		 okienko.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		 
	 }
	
}
