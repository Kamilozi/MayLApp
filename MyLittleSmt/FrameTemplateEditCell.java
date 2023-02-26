package MyLittleSmt;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class FrameTemplateEditCell extends DefaultCellEditor {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Object value;
	 public FrameTemplateEditCell() 
	 {
	        super( new JTextField() );
	        ((JTextField)getComponent()).setHorizontalAlignment(JTextField.RIGHT);
	}
	 


 
	    @Override
	    public Object getCellEditorValue()
	    {
	        return value;
	    }

	    @Override
	    public boolean stopCellEditing()
	    {
	        try
	        {
	            String editingValue = (String)super.getCellEditorValue();

	            //  Don't allow user to enter "."

	            if (editingValue.contains("."))
	            {
	                JTextField textField = (JTextField)getComponent();
	                textField.setBorder(new LineBorder(Color.red));
	                return false;
	            }

	            // Replace local specific character

	            int offset = editingValue.lastIndexOf(",");

	            if (offset != -1)
	            {
	                StringBuilder sb = new StringBuilder(editingValue);
	                sb.setCharAt(offset, '.');
	                editingValue = sb.toString();
	            }
	            int numeric =2;
				if (String.valueOf(Double.parseDouble( editingValue.toString())).length()-String.valueOf(Double.parseDouble( editingValue.toString())).lastIndexOf(".")-1>2)
				{
					numeric=String.valueOf(Double.parseDouble( editingValue.toString())).length()-String.valueOf(Double.parseDouble( editingValue.toString())).lastIndexOf(".");
				}else 
				{
					numeric =2;
				}
				String newVal = String.format(" %."+ numeric +"f",  Double.parseDouble( editingValue.toString() ));
				String NewEditingValue = newVal.replace(",", ".");
	            
	            value =  NewEditingValue;
	            		///Double.parseDouble( NewEditingValue );
	        }
	        catch(NumberFormatException exception)
	        {
	            JTextField textField = (JTextField)getComponent();
	            textField.setBorder(new LineBorder(Color.red));
	            return false;
	        }

	        return super.stopCellEditing();
	    }

	    @Override
	    public Component getTableCellEditorComponent(
	        JTable table, Object value, boolean isSelected, int row, int column)
	    {
	        Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
	       
	        JTextField textField = (JTextField)c;
	        textField.setBorder( new LineBorder(Color.BLACK) );

	        String text = textField.getText();
	        int offset = text.lastIndexOf(".");

	        // Display local specific character

	        if (offset != -1)
	        {
	            StringBuilder sb = new StringBuilder(text);
	            sb.setCharAt(offset, ',');
	            textField.setText( sb.toString() );
	        }
	       // table.clearSelection();
	      //  table.setCellSelectionEnabled(true);
	     //   table.setColumnSelectionAllowed(false);
	      //  table.setRowSelectionAllowed(true);
	        return c;
	    }


}
