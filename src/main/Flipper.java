package main;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Flipper {
	
	private BufferedImage sprite;
	private Integer x, y;
	
	public Flipper(String path, Integer x, Integer y) {
		this.x = x;
		this.y = y;
		try {
			this.sprite = ImageIO.read(getClass().getResource("/resources/"+path));			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getWidth() {
		return this.sprite.getWidth();
	}

	public Integer getHeight() {
		return this.sprite.getHeight();
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(this.getX(), this.getY(),
				this.getWidth(), this.getHeight());
	}
	
	public Integer getCenterX() {
		return x + getWidth()/2;
	}

	public Integer getCenterY() {
		return y + getHeight()/2;
	}

}
