package gui.ui.combobox;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;

import core.ResourceManager;

/**
 * 扩展默认的ComboBox的UI外观 更改ArrowButton的样式，创建自定义的StarComboPopup
 * 
 * @author jiangyp
 * 
 */
public class StarComboBoxUI extends BasicComboBoxUI {
	
	private static Image arrow = ResourceManager.loadImage("arrow.png");

	private static Color bg = new Color(0, 0, 50);

	private static Color text = new Color(76,196,40);

	static{
		UIManager.put("ComboBox.disabledForeground", Color.gray);
		UIManager.put("ComboBox.disabledBackground", bg);
	}
	
	@Override
	protected JButton createArrowButton() {

		JButton button = new MyArrowButton();
		comboBox.setBackground(bg);
		comboBox.setForeground(text);

		button.setIgnoreRepaint(true);
		button.setFocusable(false);
		button.setContentAreaFilled(false);
		return button;
	}

	protected ComboPopup createPopup() {
		return new StarComboPopup(comboBox);
	}

	public static ComponentUI createUI(JComponent c) {
		return new StarComboBoxUI();
	}

	@SuppressWarnings("serial")
	private class MyArrowButton extends JButton {

		public void paint(Graphics g) {
			g.setColor(bg);
			g.fillRoundRect(-1, 0, getWidth(), getHeight(), 6, 6);
			g.drawImage(arrow, -2, 0, null);
			 
		}
	}

}