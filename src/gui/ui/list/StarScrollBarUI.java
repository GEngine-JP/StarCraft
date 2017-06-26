package gui.ui.list;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class StarScrollBarUI extends BasicScrollBarUI {

	// public void paint(Graphics g, JComponent c) {
	//
	// }

	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {

		if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
			return;
		}

		int w = thumbBounds.width;
		int h = thumbBounds.height;

		g.translate(thumbBounds.x, thumbBounds.y);

		g.setColor(thumbDarkShadowColor);
		g.drawRect(0, 0, w - 1, h - 1);
		g.setColor(thumbColor);
		g.fillRect(0, 0, w - 1, h - 1);

		g.setColor(thumbHighlightColor);
		g.drawLine(1, 1, 1, h - 2);
		g.drawLine(2, 1, w - 3, 1);

		g.setColor(thumbLightShadowColor);
		g.drawLine(2, h - 2, w - 2, h - 2);
		g.drawLine(w - 2, 1, w - 2, h - 3);

		g.translate(-thumbBounds.x, -thumbBounds.y);
	}

	protected void configureScrollBarColors() {
		LookAndFeel.installColors(scrollbar, "ScrollBar.background",
				"ScrollBar.foreground");
		 scrollbar.setBackground(Color.black);
		 scrollbar.setForeground(Color.red);
		thumbHighlightColor = Color.black;//UIManager.getColor("ScrollBar.thumbHighlight");
		thumbLightShadowColor =Color.black; //UIManager.getColor("ScrollBar.thumbShadow");
		thumbDarkShadowColor = Color.black;//UIManager.getColor("ScrollBar.thumbDarkShadow");
		thumbColor = Color.red;
		trackColor = Color.black;//UIManager.getColor("ScrollBar.track");
		trackHighlightColor =Color.black;// UIManager.getColor("ScrollBar.trackHighlight");
	}

	public static ComponentUI createUI(JComponent c) {
		return new StarScrollBarUI();
	}

	protected JButton createDecreaseButton(int orientation) {
		return new MyArrowButton(orientation);
	}

	protected JButton createIncreaseButton(int orientation) {
		return new MyArrowButton(orientation);
	}

	private class MyArrowButton extends BasicArrowButton {

		public MyArrowButton(int direction) {
			super(direction);

		}

		public void paint(Graphics g) {
			g.setColor(Color.red);
			if (direction == NORTH) {

				Polygon filledPolygon = new Polygon();
				filledPolygon.addPoint(8,0);
				filledPolygon.addPoint(0, 14);
				filledPolygon.addPoint(15, 14);
				 
				g.drawPolygon(filledPolygon);

			} else {

				Polygon filledPolygon = new Polygon();
				filledPolygon.addPoint(0, 0);
				filledPolygon.addPoint(15, 0);
				filledPolygon.addPoint(8, 14);
				g.drawPolygon(filledPolygon);
			}

		}
		// public void paintTriangle(Graphics g, int x, int y, int size,
		// int direction, boolean isEnabled) {
		//			
		// Polygon filledPolygon = new Polygon();
		// filledPolygon.addPoint(5, 5);
		// filledPolygon.addPoint(5 + 12, 5 + 0);
		// filledPolygon.addPoint(5 + 6, 5 + 8);
		// g.fillPolygon(filledPolygon);
		// }
	}
}
