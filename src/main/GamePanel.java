package main;

import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private Ball ball;
	private Flipper leftFlipper;
	private Flipper rightFlipper;
	

	public GamePanel(Ball ball) {
		this.ball = ball;
		this.leftFlipper = new Flipper("flipper.png");
		//this.rightFlipper = new Flipper("flipper.png");
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	
		
		g.fillOval(this.ball.getX(), this.ball.getY(), this.ball.getRadius(), this.ball.getRadius());
		//g.drawImage(img, x, y, bgcolor, observer)
		g.drawImage(this.leftFlipper.getSprite(), 10, 10, null);
	}
	
}
