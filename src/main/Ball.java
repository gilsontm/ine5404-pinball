package main;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Ball{
	
	private Double x;
	private Double y;
	private Double speedX;
	private Double speedY;
	private BufferedImage sprite;
	
	public Ball(String path, Integer x, Integer y) {
		this.x = (double) x;
		this.y = (double) y;
		this.speedX = 0.8;
		this.speedY = 1.0;
		try {
			this.sprite = ImageIO.read(new File(path));			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Dimension getDimension() {
		return new Dimension(this.sprite.getWidth(), this.sprite.getHeight());
	}
	
	public Integer getWidth() {
		return this.sprite.getWidth();
	}
	
	public Integer getHeight() {
		return this.sprite.getHeight();
	}
	
	public void move() {
		this.x += this.speedX;
		this.y += this.speedY;
	}

	public Integer getX() {
		return x.intValue();
	}

	public void setX(Integer x) {
		this.x = (double) x;
	}

	public Integer getY() {
		return y.intValue();
	}

	public void setY(Integer y) {
		this.y = (double) y;
	}

	public Double getSpeedX() {
		return speedX;
	}

	public void setSpeedX(Double speedX) {
		this.speedX = speedX;
	}

	public Double getSpeedY() {
		return speedY;
	}

	public void setSpeedY(Double speedY) {
		this.speedY = speedY;
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
	
}
