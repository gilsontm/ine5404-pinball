package main;

import java.awt.Color;
import java.awt.Dimension;

public class Ball{
	
	private Double x;
	private Double y;
	private Integer radius;
	private Color color;
	private Double speedX;
	private Double speedY;
	
	public Ball(Integer x, Integer y) {
		this.x = (double) x;
		this.y = (double) y;
		this.radius = 20;
		this.color = new Color(255, 0, 0);
		this.speedX = 0.8;
		this.speedY = 1.0;
	}
	
	public Dimension getDimension() {
		return new Dimension(this.radius, this.radius);
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

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
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
}
