package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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
		
	private ArrayList<Collideable> objects = new ArrayList<Collideable>();
	
	public GamePanel() {
		try {
			this.backup = ImageIO.read(new File(getClass().getResource("background.png").getPath()));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.background = copyImage(this.backup);
		
		this.imageLabel = new JLabel();
		this.imageLabel.setIcon(new ImageIcon(this.background));
		this.add(imageLabel);
		
		this.ball = new Ball("ball.png", 100, 200);
		this.leftFlipper = new Flipper("flipperLeft.png", 80, 550);
		this.rightFlipper = new Flipper("flipperRight.png", 220, 550);
		this.objects.add(this.rightFlipper);
		this.objects.add(this.leftFlipper);
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
		this.pixelCollision();
		
		Graphics2D g2d = (Graphics2D) this.background.createGraphics();

		g2d.drawImage(this.ball.getSprite(), this.ball.getX(), this.ball.getY(), null);
		
		g2d.setTransform(AffineTransform.getRotateInstance(Math.toRadians(leftRotation), 
				this.leftFlipper.getCenterX()-20, this.leftFlipper.getCenterY()-10));
		g2d.drawImage(this.leftFlipper.getSprite(), this.leftFlipper.getX(), this.leftFlipper.getY(), null);
		
		g2d.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rightRotation), 
				this.rightFlipper.getCenterX()+20, this.rightFlipper.getCenterY()-10));
		
		g2d.drawImage(this.rightFlipper.getSprite(), this.rightFlipper.getX(), this.rightFlipper.getY(), null);
		
		this.imageLabel.setIcon(new ImageIcon(this.background));
		this.repaint();
	}
		
	public void sideCollision() {
		if (this.ball.getX() < 2 || this.ball.getX() + this.ball.getWidth() > this.getWidth() - 2) {
			this.ball.setSpeedX(this.ball.getSpeedX() * (-1));
		}
		
		if (this.ball.getY() < 2 || this.ball.getY() + this.ball.getHeight() > this.getHeight() - 2) {
			this.ball.setSpeedY(this.ball.getSpeedY() * (-1));
		}
	}
	
	public void pixelCollision() {
		for (Collideable c : this.objects) {
			Rectangle r1 = c.getRectangle();
			Rectangle r2 = this.ball.getRectangle();
			if (r1.intersects(r2)) {
				Rectangle intersection = r1.intersection(r2);

				outerLoop:
				for (int x = (int) intersection.getMinX(); x < intersection.getMaxX(); x++) {
					for (int y = (int) intersection.getMinY(); y < intersection.getMaxY(); y++) {
						try {
							if ((c.getSprite().getRGB(x - c.getX(), y - c.getY()) >> 24) != 0x00) {
								// c pixel is not transparent
								if ((ball.getSprite().getRGB(x - ball.getX(), y - ball.getY()) >> 24) != 0x00) {
									// ball pixel is also not transparent
									this.updateBallSpeed(c.getSprite(), x - c.getX(), y - c.getY());
									break outerLoop;
								}
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							continue;
						}
					}
				}
			}
		}
	}
	
	public HashMap<String, Integer> getPixelCountMap(BufferedImage sprite, Integer x, Integer y) {
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		map.put("up", 0);
		map.put("down", 0);
		map.put("left", 0);
		map.put("right", 0);
		
		for (int localX = 0; localX < sprite.getWidth(); localX++) {
			if ((sprite.getRGB(localX, y) >> 24) != 0x00){
				if (localX < x) {
					map.put("left", map.get("left") + 1);
				}
				if (localX > x) {
					map.put("right", map.get("right") + 1);
				}
			}
		}
		
		for (int localY = 0; localY < sprite.getHeight(); localY++) {
			if ((sprite.getRGB(x, localY) >> 24) != 0x00){
				if (localY < y) {
					map.put("up", map.get("up") + 1);
				}
				if (localY > y) {
					map.put("down", map.get("down") + 1);
				}
			}
		}

		return map;
	}
	
	public void updateBallSpeed(BufferedImage sprite, Integer x, Integer y) {
		HashMap<String, Integer> map = this.getPixelCountMap(sprite, x, y);
		
		Double speedX = this.ball.getSpeedX();
		Double speedY = this.ball.getSpeedY();
		
		if (map.get("up") > map.get("down")) { 
			// colisão embaixo
			speedY = Math.abs(speedY);
		} else { 
			// colisão em cima
			speedY = -1 * Math.abs(speedY);
		}
		
		if (map.get("left") > map.get("right")) {
			// colisão à direita
			speedX = Math.abs(speedX);
		} else {
			// colisão à esquerda
			speedX = -1 * Math.abs(speedX);
		}
		
		this.ball.setSpeedX(speedX);
		this.ball.setSpeedY(speedY);
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
