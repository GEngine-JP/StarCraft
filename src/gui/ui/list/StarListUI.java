package gui.ui.list;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;

public class StarListUI extends BasicListUI {

	public static ComponentUI createUI(JComponent c) {
		return new StarListUI();
	}
}
