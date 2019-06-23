package main;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public interface Collideable {
	
	public Integer getX();
	public Integer getY();
	public Integer getWidth();
	public Integer getHeight();
	public Rectangle getRectangle();
	public BufferedImage getSprite();

	// Marca o objeto como um objeto colidível.
}
