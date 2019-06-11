package main;

import java.awt.Color;
import java.awt.Dimension;

public class Ball {
	
	private Integer x;
	private Integer y;
	private Integer radius;
	private Color color;
	
	public Ball(Integer x, Integer y) {
		this.x = x;
		this.y = y;
		this.radius = 20;
		this.color = new Color(255, 0, 0);
	}
	
	public Dimension getDimension() {
		return new Dimension(this.radius, this.radius);
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
}
