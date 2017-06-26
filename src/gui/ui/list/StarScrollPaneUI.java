package gui.ui.list;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

public class StarScrollPaneUI extends BasicScrollPaneUI {

	protected void installDefaults(JScrollPane scrollpane) {
//		LookAndFeel.installBorder(scrollpane, "ScrollPane.border");
//		LookAndFeel.installColorsAndFont(scrollpane, "ScrollPane.background",
//				"ScrollPane.foreground", "ScrollPane.font");
//
//		Border vpBorder = scrollpane.getViewportBorder();
//		if ((vpBorder == null) || (vpBorder instanceof UIResource)) {
//			vpBorder = UIManager.getBorder("ScrollPane.viewportBorder");
//			scrollpane.setViewportBorder(vpBorder);
//		}
		LookAndFeel.installProperty(scrollpane, "opaque", Boolean.FALSE);
		//scrollpane.setBackground(Color.);
		
	}
	
	public static ComponentUI createUI(JComponent c) {
		return new StarScrollPaneUI();
	}
}
