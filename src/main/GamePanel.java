package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
	private boolean risingWall = false;
	private Background visibleBackground;
	private Background unvisibleBackground;
	//private BufferedImage visibleBackup;
	//private BufferedImage unvisibleBackup;
	private JLabel imageLabel;
	private Dimension startPosition = new Dimension(405, 615);
	private Integer flipperOffset = 80;
	private Integer backgroundOffset = 100;
	private Integer wallOffset;
	private Integer initialWallOffset = 100;
	private Double initialSpeedX = 0.7, initialSpeedY = 1.0;
	private Polygon leftBase, rightBase, outerBorder;
	private Collision lastCollision = Collision.NONE;
	private Color backgroundColor = new Color(0, 50, 40);
	private Color foregroundColor = new Color(50, 0, 0);
	private Integer numberLives = 3;
	private Integer totalPoints = 0;
	private GameState state = GameState.WAITING_LAUNCH;
	private boolean isPaused = false;
	
	public GamePanel() {
		this.visibleBackground = new Background(backgroundOffset);
		this.unvisibleBackground = new Background(backgroundOffset);
		
		this.imageLabel = new JLabel();
		this.add(imageLabel);
		
		this.ball = new Ball("ball.png", this.startPosition, this.initialSpeedX, this.initialSpeedY);
		this.leftFlipper = new Flipper("flipperLeft.png", flipperOffset, 550);
		this.rightFlipper = new Flipper("flipperRight.png", 400 - leftFlipper.getWidth() - flipperOffset, 550);
		
		this.leftBase = new Polygon(
			new int[] {0, flipperOffset + 15, flipperOffset, flipperOffset, 0}, 
			new int[] {480, 540, 560, 650 - 1, 650 - 1},
			5
		);
	
		this.rightBase = new Polygon(
			new int[] {400, 400 - flipperOffset - 15,400 - flipperOffset, 400 - flipperOffset, 400},
			new int[] {480, 540, 560, 650 - 1, 650 - 1},
			5
		);
		
		this.outerBorder = new Polygon(
			new int[] {0, 500 - 1, 500 - 1, 0},
			new int[] {0, 0, 650 - 1, 650 - 1},
			4
		);
		
		//this.visibleBackup = new BufferedImage(500, 650, BufferedImage.TYPE_INT_ARGB);
		//this.unvisibleBackup = new BufferedImage(500, 650, BufferedImage.TYPE_INT_ARGB);
		
		//this.makeBackup(visibleBackup, visibleBackground, Color.BLACK, Color.BLACK);
		//this.makeBackup(unvisibleBackup, unvisibleBackground, backgroundColor, foregroundColor);
	}
	
	/*public void makeBackup(BufferedImage backupSprite, Background visibleBackground,
			Color backgroundColor, Color foregroundColor) {
		
		Graphics2D g2d = (Graphics2D) backupSprite.getGraphics();
		g2d.setColor(backgroundColor);
		
		g2d.drawPolygon(this.outerBorder);
		g2d.fillPolygon(this.leftBase);
		g2d.fillPolygon(this.rightBase);
		
		g2d.setColor(foregroundColor);
		g2d.fillOval(100, 200, 30, 30);
		g2d.fillOval(200, 100, 30, 30);
		g2d.fillOval(300, 300, 30, 30);
		g2d.setColor(Color.WHITE);
		g2d.drawOval(105, 205, 22, 22);
		g2d.drawOval(110, 210, 15, 15);
		g2d.fillOval(115, 215, 10, 10);
		g2d.drawOval(205, 105, 22, 22);
		g2d.drawOval(210, 110, 15, 15);
		g2d.fillOval(215, 115, 10, 10);
		g2d.drawOval(305, 305, 22, 22);
		g2d.drawOval(310, 310, 15, 15);
		g2d.fillOval(315, 315, 10, 10);
		
		visibleBackground.setSprite(copyImage(backupSprite));	
	}*/
	
	public BufferedImage makeBackground(Color backgroundColor, Color foregroundColor) {
		BufferedImage image = new BufferedImage(500, 650, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setColor(backgroundColor);
		
		g2d.drawPolygon(this.outerBorder);
		g2d.fillPolygon(this.leftBase);
		g2d.fillPolygon(this.rightBase);
		
		g2d.setColor(foregroundColor);
		g2d.fillOval(100, 200, 30, 30);
		g2d.fillOval(200, 100, 30, 30);
		g2d.fillOval(300, 300, 30, 30);
		g2d.setColor(Color.WHITE);
		g2d.drawOval(105, 205, 22, 22);
		g2d.drawOval(110, 210, 15, 15);
		g2d.fillOval(115, 215, 10, 10);
		g2d.drawOval(205, 105, 22, 22);
		g2d.drawOval(210, 110, 15, 15);
		g2d.fillOval(215, 115, 10, 10);
		g2d.drawOval(305, 305, 22, 22);
		g2d.drawOval(310, 310, 15, 15);
		g2d.fillOval(315, 315, 10, 10);
		
		return image;
	}
	
	public void update() {
		if (state == GameState.LAUNCHING && this.isBallInGame()) {
			this.enterGame();
		}
		
		if (state == GameState.PLAYING) {
			this.sideCollision();
		} else if (state == GameState.LAUNCHING) {
			this.lauchingSideCollision();
		} 
		
		//this.visibleBackground.setSprite(copyImage(this.visibleBackup));
		//this.unvisibleBackground.setSprite(copyImage(this.unvisibleBackup));
		
		this.visibleBackground.setSprite(makeBackground(Color.BLACK, Color.BLACK));
		this.unvisibleBackground.setSprite(makeBackground(backgroundColor, foregroundColor));
		
		Graphics2D g2d = (Graphics2D) visibleBackground.getSprite().getGraphics();
		
		this.drawBackground(g2d, Color.BLACK);
		this.drawBackground((Graphics2D) unvisibleBackground.getSprite().getGraphics(), backgroundColor);
		
		
		this.drawRemainingLives(g2d);
		
		if (state == GameState.PLAYING) {
			this.pixelCollision();
		}
		
		if (lastCollision == Collision.FOREGROUND) {
			totalPoints++;
		}

		lastCollision = Collision.NONE;
		
		this.moveBall();
		
		g2d.drawImage(this.ball.getSprite(), this.ball.getX(), this.ball.getY(), null);
		
		this.imageLabel.setIcon(new ImageIcon(visibleBackground.getSprite()));
		this.repaint();
	}
	
	public void drawBackground(Graphics2D g2d, Color backgroundColor) {
		AffineTransform transform = g2d.getTransform();
		
		g2d.setColor(backgroundColor);
		
		if (state == GameState.PLAYING) {
			g2d.drawLine(400, 0, 400, 650 - 1);
		} else if (state == GameState.PREPARING_LAUNCH) {
			g2d.drawLine(400, wallOffset, 400, 650 - 1);
			if (wallOffset >= 450) {
				this.risingWall = true;
			}
			if (wallOffset <= initialWallOffset) {
				this.risingWall = false;
			}
			
			this.wallOffset += (risingWall? -2 : 2);
	    } else if (state == GameState.LAUNCHING) {
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
	}
	
	public void drawGameOver() {
		Graphics2D g2d = (Graphics2D) visibleBackground.getSprite().getGraphics();
		g2d.setColor(Color.RED);
		
		g2d.setFont(new Font("Arial", Font.BOLD, 50));
		g2d.drawString("GAME OVER", 30, 420);
		
		String source = "press enter to restart";
		g2d.setFont(new Font("Arial", Font.BOLD, 20));
		g2d.drawString(source, 75, 445);
		
		this.imageLabel.setIcon(new ImageIcon(visibleBackground.getSprite()));
		this.repaint();
	}
	
	public void drawPaused() {
		Graphics2D g2d = (Graphics2D) visibleBackground.getSprite().getGraphics();
		g2d.setColor(Color.BLACK);
		
		g2d.setFont(new Font("Arial", Font.BOLD, 80));
		g2d.drawString("II", 180, 350);
		
		this.imageLabel.setIcon(new ImageIcon(visibleBackground.getSprite()));
		this.repaint();
	}
		
	public void updateStartPosition() {
		switch (numberLives) {
		case 1:
			this.startPosition = new Dimension(405, 615);
			break;
		case 2:
			this.startPosition = new Dimension(405, 575);
			break;
		case 3:
			this.startPosition = new Dimension(405, 535);
			break;
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
		if (ball.getX() <= 2) {
			ball.setSpeedX(Math.abs(ball.getSpeedX()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getX() + ball.getWidth() >= visibleBackground.getWidth() - 2) {
			ball.setSpeedX((-1) * Math.abs(ball.getSpeedX()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getY() <= 2) {
			ball.setSpeedY(Math.abs(ball.getSpeedY()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getY() + ball.getHeight()>= visibleBackground.getHeight() - 2) {	
			if (numberLives > 0) {
				this.resetBall();
			} else {
				this.state = GameState.OVER;
			}
		}
	}
	
	public void lauchingSideCollision() {
		if (ball.getX() <= visibleBackground.getWidth()) {
			if (ball.getY() + ball.getHeight() > wallOffset) {
				ball.setSpeedX(Math.abs(ball.getSpeedX()));
				this.lastCollision = Collision.BACKGROUND;
			}
		}
		if (ball.getX() + ball.getWidth() >= visibleBackground.getWidth() + backgroundOffset - 1) {
			ball.setSpeedX((-1) * Math.abs(ball.getSpeedX()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getY() <= 1) {
			ball.setSpeedY(Math.abs(ball.getSpeedY()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getY() + ball.getHeight() >= visibleBackground.getHeight()) {	
			ball.setSpeedY(-1 * Math.abs(ball.getSpeedY()));
			this.lastCollision = Collision.BACKGROUND;
		}
	}
	
	public void resetBall() {
		ball.setSpeedX(0.0);
		ball.setSpeedY(0.0);
		ball.setPosition(this.startPosition);
		this.state = GameState.WAITING_LAUNCH;
	}
	
	public void setLaunch() {
		if (state != GameState.PREPARING_LAUNCH) {
			this.state = GameState.PREPARING_LAUNCH;
			this.wallOffset = this.initialWallOffset; 
		}
	}
	
	public void launchBall() {
		this.state = GameState.LAUNCHING;
		ball.setSpeedX(3.0);
		ball.setSpeedY(-2.0);
		updateStartPosition();
		ball.setPosition(this.startPosition);
		this.numberLives -= 1;
		updateStartPosition();
	}
	
	public void enterGame() {
		this.state = GameState.PLAYING;
		ball.setSpeedX(this.initialSpeedX);
		ball.setSpeedY(this.initialSpeedY);
	}
	
	public void restartGame() {
		this.totalPoints = 0;
		this.numberLives = 3;
		this.state = GameState.WAITING_LAUNCH;
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
		if (ball.getX() < 0 || ball.getX() + ball.getWidth() > visibleBackground.getWidth()) {
			return false;
		}
		if (ball.getY() < 0 || ball.getY() + ball.getHeight() > visibleBackground.getHeight()) {
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
						RGB = unvisibleBackground.getSprite().getRGB(x + ball.getX(), y + ball.getY());
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
	
	public void moveBall() {
		if (state == GameState.LAUNCHING) {
			ball.move(false);
		} else if (state == GameState.PLAYING) {
			ball.move(true);
		}
	}
	
	public void updateSpeed(Integer x, Integer y, Integer RGB) {
		
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
		
		this.setLastCollision(RGB);
		
		switch (lastCollision) {
		case FLIPPER:
			if ((leftRising  && x + this.ball.getX() <= visibleBackground.getWidth()/2) ||
				(rightRising && x + this.ball.getX() > visibleBackground.getWidth()/2)) {
				speedX = (initialSpeedX + 0.7) * speedX/Math.abs(speedX);
				speedY = (initialSpeedY + 0.7) * speedY/Math.abs(speedY);
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
	
	public boolean isPlaying() {
		return state == GameState.PLAYING;
	}
	
	public boolean isPaused() {
		return this.isPaused;
	}
	
	public void togglePaused() {
		this.isPaused = !isPaused;
		if (isPaused) {
			this.drawPaused();
		}
	}
	
	public boolean isLaunching() {
		return state == GameState.LAUNCHING;
	}
	
	public boolean isOver() {
		return state == GameState.OVER;
	}
	
	public boolean isWaitingLaunch() {
		return state == GameState.WAITING_LAUNCH;
	}
	
	public boolean isPreparingLaunch() {
		return state == GameState.PREPARING_LAUNCH;
	}

	public Integer getNumberLives() {
		return numberLives;
	}

	public void setNumberLives(Integer numberLives) {
		this.numberLives = numberLives;
	}

	public Integer getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}

	public void setState(GameState state) {
		this.state = state;
	}
}
