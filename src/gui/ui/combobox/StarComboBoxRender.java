package gui.ui.combobox;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * 扩展swing默认的BasicComboBoxRenderer
 * 主要是为了改变默认选择每个下拉项的外观
 * @author jiangyp
 */
public class StarComboBoxRender extends BasicComboBoxRenderer {

	Color bg  = new Color(0,0,50);
	Color text  = new Color(76,196,40);
	Color heightTx  = new Color(175,250,110);
	
	public StarComboBoxRender() {
		super();
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
	 
		setBackground(bg);
        if (isSelected) {
        	 setForeground(heightTx);
        }
        else {
        	 setForeground(text);
        } 
        
        setText((value == null) ? "" : value.toString());
		return this;
	}

}