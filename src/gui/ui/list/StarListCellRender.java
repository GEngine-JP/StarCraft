package gui.ui.list;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;

public class StarListCellRender extends DefaultListCellRenderer {
	Color text  = new Color(76,196,40);
	Color heightTx  = new Color(175,250,110);
	
	  public Component getListCellRendererComponent(
		        JList list,
			Object value,
		        int index,
		        boolean isSelected,
		        boolean cellHasFocus)
		    {
		        setComponentOrientation(list.getComponentOrientation());

//		        Color bg = null;
//		        Color fg = null;

		        setBackground(list.getBackground());
		        
			if (isSelected) {
		        //setBackground(bg == null ? list.getSelectionBackground() : bg);
			    setForeground(heightTx);
			}
			else {
			    //setBackground(list.getBackground());
			    setForeground(text);
			}
		        
			if (value instanceof Icon) {
			    setIcon((Icon)value);
			    setText("");
			}
			else {
			    setIcon(null);
			    setText((value == null) ? "" : value.toString());
			}

			setEnabled(list.isEnabled());
			setFont(list.getFont());
		        
//		        Border border = null;
//		        if (cellHasFocus) {
//		            if (isSelected) {
//		                border = DefaultLookup.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
//		            }
//		            if (border == null) {
//		                border = DefaultLookup.getBorder(this, ui, "List.focusCellHighlightBorder");
//		            }
//		        } else {
//		            border = getNoFocusBorder();
//		        }
//			setBorder(border);

			return this;
		    }
}
