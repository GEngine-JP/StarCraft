package gui.ui;

import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboPopup;

/**
 * 扩展弹出的popup，为popup加上红色边框
 * @author jiangyp
 *
 */
public class StarComboPopup extends BasicComboPopup{

	public StarComboPopup(JComboBox combo) {
		super(combo);
	}

	protected void configurePopup() {
		super.configurePopup();
		setBorder(new RoundBorder(Color.red));
    }

}
