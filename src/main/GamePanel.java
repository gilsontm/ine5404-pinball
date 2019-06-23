package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private Ball ball;
	private Flipper leftFlipper, rightFlipper;
	private Integer leftRotation = 0, rightRotation = 0;
	private boolean leftRising = false, rightRising = false;
	private BufferedImage background, backup;
	private JLabel imageLabel;
		
	//private ArrayList<Collideable> objects = new ArrayList<Collideable>();
	
	public GamePanel() {
		try {
			this.backup = ImageIO.read(new File(getClass().getResource("background.png").getPath()));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.background = copyImage(this.backup);
		
		this.setBackground(Color.PINK);
		this.imageLabel = new JLabel();
		this.imageLabel.setIcon(new ImageIcon(this.background));
		this.add(imageLabel);
		
		this.ball = new Ball("ball.png", 100, 200);
		this.leftFlipper = new Flipper("flipperLeft.png", 70, 550);
		this.rightFlipper = new Flipper("flipperRight.png", 230, 550);
		//this.objects.add(this.rightFlipper);
		//this.objects.add(this.leftFlipper);
	}
	
	public static BufferedImage copyImage(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}
	
	public void update() {
		this.background = copyImage(this.backup);
		
		this.sideCollision();
		
		Graphics2D g2d = (Graphics2D) this.background.createGraphics();
		
		AffineTransform transform = g2d.getTransform();
		
		g2d.setTransform(AffineTransform.getRotateInstance(Math.toRadians(leftRotation), 
				this.leftFlipper.getCenterX()-20, this.leftFlipper.getCenterY()-10));
		g2d.drawImage(this.leftFlipper.getSprite(), this.leftFlipper.getX(), this.leftFlipper.getY(), null);
		
		g2d.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rightRotation), 
				this.rightFlipper.getCenterX()+20, this.rightFlipper.getCenterY()-10));
		g2d.drawImage(this.rightFlipper.getSprite(), this.rightFlipper.getX(), this.rightFlipper.getY(), null);
		
		g2d.setTransform(transform);

		this.pixelCollision();
		g2d.drawImage(this.ball.getSprite(), this.ball.getX(), this.ball.getY(), null);
		
		this.imageLabel.setIcon(new ImageIcon(this.background));
		this.repaint();
	}
		
	public void sideCollision() {
		if (ball.getX() <= 0) {
			ball.setSpeedX(Math.abs(ball.getSpeedX()));
		}
		if (ball.getX() + ball.getWidth() >= background.getWidth()) {
			ball.setSpeedX((-1) * Math.abs(ball.getSpeedX()));
		}
		if (ball.getY() <= 0) {
			ball.setSpeedY(Math.abs(ball.getSpeedY()));
		}
		if (ball.getY() + ball.getHeight() >= background.getHeight()) {
			ball.setSpeedY((-1) * Math.abs(ball.getSpeedY()));
		}
	}
	
	public void pixelCollision() {
		
		outerLoop:
		for (int x = 0; x < ball.getWidth(); x++) {
			for (int y = 0; y < ball.getHeight(); y++) {
				if ((ball.getSprite().getRGB(x, y) >> 24) != 0x00) {
					try {
						if ((this.background.getRGB(x + ball.getX(), y + ball.getY()) >> 24) != 0x00) {
							this.updateSpeed(x, y);
							break outerLoop;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
						continue;
					}
				}
				
			}
		}
	}
	
	public void updateSpeed(Integer x, Integer y) {
		Double speedX = ball.getSpeedX();
		Double speedY = ball.getSpeedY();
		
		if (x < ball.getWidth()/2) {
			// left collision, must go right
			speedX = Math.abs(speedX);
		} else {
			// right collision, must go left
			speedX = -1 * Math.abs(speedX);
		}
		
		if (y < ball.getHeight()/2) {
			// upper collision, must go down
			speedY = Math.abs(speedY);
		} else {
			// lower collision, must go up
			speedY = -1 * Math.abs(speedY);
		}
		
		ball.setSpeedX(speedX);
		ball.setSpeedY(speedY);
	}
		
	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public Integer getLeftRotation() {
		return leftRotation;
	}

	public void setLeftRotation(Integer leftRotation) {
		this.leftRotation = leftRotation;
	}

	public Integer getRightRotation() {
		return rightRotation;
	}

	public void setRightRotation(Integer rightRotation) {
		this.rightRotation = rightRotation;
	}

	public boolean isLeftRising() {
		return leftRising;
	}

	public void setLeftRising(boolean leftRising) {
		this.leftRising = leftRising;
	}

	public boolean isRightRising() {
		return rightRising;
	}

	public void setRightRising(boolean rightRising) {
		this.rightRising = rightRising;
	}
}
