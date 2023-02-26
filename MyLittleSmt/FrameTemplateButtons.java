package MyLittleSmt;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class FrameTemplateButtons {
	public JButton RibbonJButton(String name, ImageIcon icon)
	{
		JButton button = new JButton(name, icon);
		button.setVerticalTextPosition(SwingConstants.BOTTOM);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		return button;	
	}
	public JButton smallButton(String toolTips, ImageIcon icon)
	{
		JButton smallButton = new JButton();
		smallButton.setIcon(icon);
		smallButton.setContentAreaFilled(false);
		smallButton.setBorderPainted(false);
		smallButton.setToolTipText(toolTips);
		return smallButton;
	}
	public JTextField getTextField()
	{
		JTextField tx = new JTextField();
		tx.setPreferredSize(new Dimension(125,20));
		tx.setEnabled(false);
		return tx;
	}
}
