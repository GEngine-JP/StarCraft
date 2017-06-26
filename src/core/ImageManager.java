package core;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageManager {

	public static Image getMirror(Image image) {
		return getScaledImage(image, -1, 1);
	}

	public static Image getFlippedImage(Image image) {
		return getScaledImage(image, 1, -1);
	}

	private static Image getScaledImage(Image image, float x, float y) {

 
		AffineTransform transform = new AffineTransform();
		transform.scale(x, y);
		transform.translate((x - 1) * image.getWidth(null) / 2, (y - 1)* image.getHeight(null) / 2);

		BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		 
		Graphics2D g = (Graphics2D) newImage.getGraphics();
		g.drawImage(image, transform, null);
		g.dispose();

		return newImage;
	}
}
