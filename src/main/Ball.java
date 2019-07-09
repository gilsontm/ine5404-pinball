package main;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Ball {
	
	private Double x;
	private Double y;
	private Double speedX;
	private Double speedY;
	private BufferedImage sprite;
	private Double gravity = 0.003;
	
	public Ball(String path, Dimension d, Double speedX, Double speedY) {
		this.x = (double) d.getWidth();
		this.y = (double) d.getHeight();
		this.speedX = speedX;
		this.speedY = speedY;
		try {
			this.sprite = ImageIO.read(getClass().getResource("/resources/"+path));			
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
	
	public void move(boolean useGravity) {
		this.x += this.speedX;
		this.y += this.speedY;
		if (useGravity) {
			this.speedY += this.gravity;
		}
	}
	
	public void setPosition(Dimension d) {
		this.x = d.getWidth();
		this.y = d.getHeight();
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
	
	public Rectangle getRectangle() {
		return new Rectangle(this.getX(), this.getY(), 
				this.getWidth(), this.getHeight());
	}
}
