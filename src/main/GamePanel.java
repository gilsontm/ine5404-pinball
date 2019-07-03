package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

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
	private Dimension startPosition = new Dimension(405, 615);
	private Integer flipperOffset = 80;
	private Integer backgroundOffset = 100;
	private boolean inGame = false;
	private boolean isLaunching = false;
	private boolean settingLaunch = false;
	private boolean risingWall = false;
	private boolean gameOver = false;
	private boolean inPause = false;
	private Integer wallOffset;
	private Integer initialWallOffset = 100;
	private Double initialSpeedX = 0.7, initialSpeedY = 1.0;
	private Polygon leftBase, rightBase, outerBorder;
	private Collision lastCollision = Collision.NONE;
	private Color backgroundColor = new Color(0, 50, 40);
	private Integer numberLives = 3;
	private Color foregroundColor = new Color(50, 0, 0);
	
	public GamePanel() {
		this.backup = new BufferedImage(500, 650, BufferedImage.TYPE_INT_ARGB);

		this.background = new Background(backgroundOffset);
		this.background.setSprite(copyImage(this.backup));
		
		this.imageLabel = new JLabel();
		this.imageLabel.setIcon(new ImageIcon(this.background.getSprite()));
		this.add(imageLabel);
		
		this.ball = new Ball("ball.png", this.startPosition, this.initialSpeedX, this.initialSpeedY);
		this.leftFlipper = new Flipper("flipperLeft.png", flipperOffset, 550);
		this.rightFlipper = new Flipper("flipperRight.png", this.background.getWidth() - 
				leftFlipper.getWidth() - flipperOffset, 550);
		
		this.leftBase = new Polygon(
			new int[] {0, flipperOffset + 15, flipperOffset, flipperOffset, 0}, 
			new int[] {480, 540, 560, background.getHeight() - 1, background.getHeight() - 1},
			5
		);
	
		this.rightBase = new Polygon(
			new int[] {400, 400 - flipperOffset - 15,400 - flipperOffset, 400 - flipperOffset, 400},
			new int[] {480, 540, 560, background.getHeight() - 1, background.getHeight() - 1},
			5
		);
		
		this.outerBorder = new Polygon(
			new int[] {0, 500 - 1, 500 - 1, 0},
			new int[] {0, 0, 650 - 1, 650 - 1},
			4
		);
	
		this.updateBackup(backgroundColor);
	}
	
	
	public void updateBackup(Color color) {
		Graphics2D g2d = (Graphics2D) this.backup.getGraphics();
		g2d.setColor(color);
		
		g2d.drawPolygon(this.outerBorder);
		g2d.fillPolygon(this.leftBase);
		g2d.fillPolygon(this.rightBase);
		
		g2d.setColor(foregroundColor);
		g2d.fillOval(100, 200, 40, 40);
		g2d.fillOval(200, 100, 40, 40);
		g2d.fillOval(300, 300, 40, 40);
		g2d.setColor(Color.WHITE);
		g2d.drawOval(105, 205, 30, 30);
		g2d.drawOval(110, 210, 20, 20);
		g2d.fillOval(115, 215, 10, 10);
		g2d.drawOval(205, 105, 30, 30);
		g2d.drawOval(210, 110, 20, 20);
		g2d.fillOval(215, 115, 10, 10);
		g2d.drawOval(305, 305, 30, 30);
		g2d.drawOval(310, 310, 20, 20);
		g2d.fillOval(315, 315, 10, 10);
		
		this.background.setSprite(copyImage(this.backup));
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
			this.enterGame();
		}
				
		if (inGame) {
			this.sideCollision();
		} else if (isLaunching) {
			this.lauchingSideCollision();
		} 
		
		this.background.setSprite(copyImage(this.backup));
		
		Graphics2D g2d = (Graphics2D) this.background.getSprite().createGraphics();
		
		this.drawRemainingLives(g2d);
		
		AffineTransform transform = g2d.getTransform();
		
		g2d.setColor(backgroundColor);
		
		if (inGame) {
			g2d.drawLine(400, 0, 400, 650 - 1);
		} else if (settingLaunch) {
			g2d.drawLine(400, wallOffset, 400, 650 - 1);
			if (wallOffset >= 450) {
				this.risingWall = true;
			}
			if (wallOffset <= initialWallOffset) {
				this.risingWall = false;
			}
			
			this.wallOffset += (risingWall? -2 : 2);
	    } else if (isLaunching) {
			g2d.drawLine(400, wallOffset, 400, 650 - 1);
		} else {
			g2d.drawLine(400, initialWallOffset, 400, 650 - 1);
		}
				
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
		
		if (!gameOver) {
			
		}
		this.moveBall();	
		g2d.drawImage(this.ball.getSprite(), this.ball.getX(), this.ball.getY(), null);
		
		if (this.gameOver) {
			this.setToGameOver();
		}
		
		if (this.inPause) {
			this.setToPause();
		}
		
		this.imageLabel.setIcon(new ImageIcon(this.background.getSprite()));
		this.repaint();
	}
	
	public void setToGameOver() {
		Graphics2D g2d = (Graphics2D) this.background.getSprite().getGraphics();
		g2d.setColor(Color.RED);
		
		g2d.setFont(new Font("Arial", Font.BOLD, 50));
		g2d.drawString("GAME OVER", 30, 320);
		
		String source = "press enter to restart";
		g2d.setFont(new Font("Arial", Font.BOLD, 20));
		g2d.drawString(source, 75, 345);
	}
	
	public void setToPause() {
		Graphics2D g2d = (Graphics2D) this.background.getSprite().getGraphics();
		g2d.setColor(Color.BLACK);
		
		g2d.setFont(new Font("Arial", Font.BOLD, 80));
		g2d.drawString("II", 180, 350);
	}
	
	public void setStartPosition() {
		if (numberLives == 1) {
			this.startPosition = new Dimension(405, 615);
		}
		if (numberLives == 2) {
			this.startPosition = new Dimension(405, 575);
		} 
		if (numberLives == 3) {
			this.startPosition = new Dimension(405, 535);
		}
	}
	
	public void drawRemainingLives(Graphics2D g2d) {
		if (numberLives >= 1) {
			g2d.drawImage(ball.getSprite(), 405, 615, null);
		}
		if (numberLives >= 2) {
			g2d.drawImage(ball.getSprite(), 405, 575, null);
		}
		if (numberLives >= 3) {
			g2d.drawImage(ball.getSprite(), 405, 535, null);
		}
	}
		
	public void sideCollision() {
		if (ball.getX() <= 1) {
			ball.setSpeedX(Math.abs(ball.getSpeedX()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getX() + ball.getWidth() >= background.getWidth()) {
			ball.setSpeedX((-1) * Math.abs(ball.getSpeedX()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getY() <= 1) {
			ball.setSpeedY(Math.abs(ball.getSpeedY()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getY() + ball.getHeight()>= background.getHeight()) {	
			if (numberLives > 0) {
				this.resetBall();
				this.lastCollision = Collision.BACKGROUND;
			} else {
				this.gameOver = true;
				this.inGame = false;
			}
		}
	}
	
	public void lauchingSideCollision() {
		if (ball.getX() <= background.getWidth()) {
			if (ball.getY() + ball.getHeight() > wallOffset) {
				ball.setSpeedX(Math.abs(ball.getSpeedX()));
				this.lastCollision = Collision.BACKGROUND;
			}
		}
		if (ball.getX() + ball.getWidth() >= background.getWidth() + backgroundOffset - 1) {
			ball.setSpeedX((-1) * Math.abs(ball.getSpeedX()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getY() <= 1) {
			ball.setSpeedY(Math.abs(ball.getSpeedY()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getY() + ball.getHeight() >= background.getHeight()) {	
			ball.setSpeedY(-1 * Math.abs(ball.getSpeedY()));
			this.lastCollision = Collision.BACKGROUND;
		}
	}
	
	public void resetBall() {
		ball.setSpeedX(0.0);
		ball.setSpeedY(0.0);
		ball.setPosition(this.startPosition);
		this.inGame = false;
		this.isLaunching = false;
		this.settingLaunch = false;
	}
	
	public void setLaunch() {
		if (!settingLaunch) {
			this.settingLaunch = true;
			this.wallOffset = this.initialWallOffset; 
		}
	}
	
	public void launchBall() {
		this.inGame = false;
		this.isLaunching = true;
		this.settingLaunch = false;
		ball.setSpeedX(3.0);
		ball.setSpeedY(-2.0);
		setStartPosition();
		ball.setPosition(this.startPosition);
		this.numberLives -= 1;
		setStartPosition();
	}
	
	public void enterGame() {
		this.inGame = true;
		this.isLaunching = false;
		this.settingLaunch = false;
		ball.setSpeedX(this.initialSpeedX);
		ball.setSpeedY(this.initialSpeedY);
	}
	
	public void restartGame() {
		this.numberLives = 3;
		this.gameOver = false;
		this.resetBall();
	}
	
	public void setLastCollision(Integer RGB) {
		if (RGB == null) {
			lastCollision = Collision.NONE;
		} else {
			if (RGB == backgroundColor.getRGB()) {
			 	lastCollision = Collision.BACKGROUND;
			} else if (RGB == foregroundColor.getRGB()) {
				lastCollision = Collision.FOREGROUND;
			} else {
				lastCollision = Collision.FLIPPER;
			}
		}
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
			if ((leftRising && x + this.ball.getX() <= background.getWidth()/2) ||
				(rightRising && x + this.ball.getX() > background.getWidth()/2)) {
				speedX = (this.initialSpeedX + 0.5) * speedX/Math.abs(speedX);
				speedY = (this.initialSpeedY + 0.5) * speedY/Math.abs(speedY);
			} else {
				speedX = Math.abs(Math.abs(speedX) - 0.2) * speedX/Math.abs(speedX);
				speedY = Math.abs(Math.abs(speedY) - 0.07)/1.5 * speedY/Math.abs(speedY);
			}
			break;
		case BACKGROUND:
			speedX = Math.abs(Math.abs(speedX) - 0.2) * speedX/Math.abs(speedX);
			speedY = Math.abs(Math.abs(speedY) - 0.07)/1.5 * speedY/Math.abs(speedY);
			break;
		case FOREGROUND:
			speedX = (this.initialSpeedX + (new Random()).nextDouble()) * speedX/Math.abs(speedX);
			speedY = (this.initialSpeedY + (new Random()).nextDouble()) * speedY/Math.abs(speedY);
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

	public Integer getNumberLives() {
		return numberLives;
	}

	public void setNumberLives(Integer numberLives) {
		this.numberLives = numberLives;
	}


	public boolean isGameOver() {
		return gameOver;
	}


	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}


	public boolean isInPause() {
		return inPause;
	}


	public void setInPause(boolean inPause) {
		this.inPause = inPause;
	}	
}
