package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
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
	private Background background;
	private BufferedImage backup;
	private JLabel imageLabel;
	private Dimension startPosition = new Dimension(450,600);
	private Integer flipperOffset = 79;
	private Integer backgroundOffset = 100;
	private boolean inGame = false;
	private boolean isLaunching = false;
	private Double initialSpeedX = 0.6, initialSpeedY = 1.0;
	private Polygon leftBase, rightBase;
	private Collision lastCollision = Collision.NONE; 
	
	public GamePanel() {
		try {
			this.backup = ImageIO.read(new File(getClass().getResource("background.png").getPath()));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.background = new Background(backgroundOffset);
		this.background.setSprite(copyImage(this.backup));

		this.imageLabel = new JLabel();
		this.imageLabel.setIcon(new ImageIcon(this.background.getSprite()));
		this.add(imageLabel);
		
		this.ball = new Ball("ball.png", this.startPosition);
		this.leftFlipper = new Flipper("flipperLeft.png", flipperOffset, 550);
		this.rightFlipper = new Flipper("flipperRight.png", this.background.getWidth() - 
				leftFlipper.getWidth() - flipperOffset, 550);
		
		this.leftBase = new Polygon(new int[] {0, flipperOffset, flipperOffset,
				flipperOffset + leftFlipper.getWidth() - 20, flipperOffset + 
				leftFlipper.getWidth() - 20, 0}, new int[] {460, 540, 590, 610, 
				background.getHeight() - 1, background.getHeight() - 1}, 6);
	
		this.rightBase = new Polygon(new int[] {400, 400 - flipperOffset,
				400 - flipperOffset, 400 + 20 - flipperOffset - leftFlipper.getWidth(),
				400 + 20 - flipperOffset - leftFlipper.getWidth(), 400}, new int[] {460, 540, 590, 610, 
				background.getHeight() - 1, background.getHeight() - 1}, 6);
	}
	
	public static BufferedImage copyImage(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(),
	    		source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}
	
	public void update() {
		
		if (this.isLaunching && this.isBallInGame()) {
			this.startGame();
		}
		
		this.background.setSprite(copyImage(this.backup));
		
		if (inGame) {
			this.sideCollision();
		}
		
		Graphics2D g2d = (Graphics2D) this.background.getSprite().createGraphics();
		
		AffineTransform transform = g2d.getTransform();
		
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawLine(0, 0, 500 - 1, 0);
		g2d.drawLine(0, 0, 0, 650 - 1);
		g2d.drawLine(0, 650 - 1, 500 - 1, 650 - 1);
		g2d.drawLine(400, 0, 400, 650 - 1);
		g2d.drawLine(500 - 1, 0, 500 - 1, 650 - 1);
		
		g2d.fillPolygon(this.leftBase);
		g2d.fillPolygon(this.rightBase);
		
		g2d.setTransform(AffineTransform.getRotateInstance(Math.toRadians(leftRotation), 
				this.leftFlipper.getCenterX()-20, this.leftFlipper.getCenterY()-10));
		g2d.drawImage(this.leftFlipper.getSprite(), this.leftFlipper.getX(), this.leftFlipper.getY(), null);
		
		g2d.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rightRotation), 
				this.rightFlipper.getCenterX()+20, this.rightFlipper.getCenterY()-10));
		g2d.drawImage(this.rightFlipper.getSprite(), this.rightFlipper.getX(), this.rightFlipper.getY(), null);

		g2d.setTransform(transform);
		
		if (inGame) {
			this.pixelCollision();
		}
		
		this.moveBall();
		
		g2d.drawImage(this.ball.getSprite(), this.ball.getX(), this.ball.getY(), null);
		
		this.imageLabel.setIcon(new ImageIcon(this.background.getSprite()));
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
			this.resetBall();
		}
	}
	
	public void resetBall() {
		ball.setSpeedX(0.0);
		ball.setSpeedY(0.0);
		ball.setPosition(this.startPosition);
		this.inGame = false;
		this.isLaunching = false;
	}
	
	public void launchBall() {
		this.inGame = false;
		this.isLaunching = true;
		ball.setSpeedX(-0.2);
		ball.setSpeedY(-1.0);
	}
	
	public void startGame() {
		this.inGame = true;
		this.isLaunching = false;
		ball.setSpeedX(this.initialSpeedX);
		ball.setSpeedY(this.initialSpeedY);
	}
	
	public void setLastCollision(Integer RGB) {
		if (RGB == null) {
			lastCollision = Collision.NONE;
		} else {
			Color color = new Color(RGB);
			
			// detectar cor baseado no RGB e decidir sobre objetos
			
			/*if (color == Color.BLACK) {
				lastCollision = Collision.FLIPPER;
			} else if (color == Color.DARK_GRAY) {
			 	lastCollision = Collision.BACKGROUND;
			} else {
				lastCollision = Collision.NONE;
			}*/
		}


		System.out.println(lastCollision);
	}
	
	public boolean isBallInGame() {
		if (ball.getX() < 0 || ball.getX() + ball.getWidth() > background.getWidth()) {
			return false;
		}
		if (ball.getY() < 0 || ball.getY() + ball.getHeight() > background.getHeight()) {
			return false;
		}
		return true;
	}
	
	public void pixelCollision() {
		Integer RGB = null;
		outerLoop:
		for (int x = 0; x < ball.getWidth(); x++) {
			for (int y = 0; y < ball.getHeight(); y++) {
				if ((ball.getSprite().getRGB(x, y) >> 24) != 0x00) {
					try {
						RGB = this.background.getSprite().getRGB(x + ball.getX(), y + ball.getY());
						if ((RGB >> 24) != 0x00) {
							this.updateSpeed(x, y, RGB);
							break outerLoop;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						//e.printStackTrace();
						continue;
					}
				}
				
			}
		}
	}
	
	public void updateSpeed(Integer x, Integer y, Integer RGB) {
		
		this.setLastCollision(RGB);
		
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
		
		switch (lastCollision) {
		case FLIPPER:
			if (leftRising || rightRising) {
				speedX = this.initialSpeedX * speedX/Math.abs(speedX);
				speedY = this.initialSpeedY * speedY/Math.abs(speedY);
			} else {
				speedX = Math.abs(Math.abs(speedX) - 0.01) * speedX/Math.abs(speedX);
				speedY = Math.abs(Math.abs(speedY) - 0.01) * speedY/Math.abs(speedY);
			}
			break;
		case BACKGROUND:
			speedX = Math.abs(Math.abs(speedX) - 0.01) * speedX/Math.abs(speedX);
			speedY = Math.abs(Math.abs(speedY) - 0.01) * speedY/Math.abs(speedY);
			break;
		case NONE:
			break;
		}
		
		ball.setSpeedX(speedX);
		ball.setSpeedY(speedY);
	}
	
	public void moveBall() {
		if (this.isLaunching) {
			ball.move(false);
		} else if (this.inGame) {
			ball.move(true);
		}
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

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public boolean isLaunching() {
		return isLaunching;
	}

	public void setLauching(boolean isLaunching) {
		this.isLaunching = isLaunching;
	}
}
