package MyLittleSmt;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;




public class FrameTemplateWindow extends JFrame {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JPanel upPanel;
 
	
	public FrameTemplateWindow(int x, int y, String title)
	{
		setIconImage(FrameTemplateImageIcon.iconSys().getImage());
		setSize(x,y);
		setTitle(title);
		setLayout(new BorderLayout(0,0));
	}
	
public void main ()
{

setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
}



protected JPanel getUpMenu(FrameTemplate inFrame)
{
	JPanel up = new JPanel(new BorderLayout());
		up.add(inFrame.GetUpMenu(false), BorderLayout.PAGE_START);
	return up;
}

}
