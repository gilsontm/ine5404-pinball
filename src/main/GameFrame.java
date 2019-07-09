package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	
	private GamePanel gamePanel;
	private JPanel rightPanel;
	private JLabel currentPoints, scoreBoard;
	private boolean isPressedLeft = false;
	private boolean isPressedRight = false;
	private boolean rotateLeft = false, rotateRight = false;
	
	public GameFrame() {
		gamePanel = new GamePanel();
		gamePanel.setMaximumSize(new Dimension(500, 650));
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
	
		currentPoints = new JLabel();
		currentPoints.setFont(new Font("Arial", Font.BOLD, 23));
		rightPanel.add(currentPoints, BorderLayout.NORTH);
		
		scoreBoard = new JLabel();
		this.updateScoreBoard();
		rightPanel.add(scoreBoard, BorderLayout.CENTER);
		
		JLabel instructionLabel = new JLabel(
				"<html><p>press P to pause</p>"
				+ "<p>arrows control flippers</p>"
				+ "<p>hold space bar to launch</p></html>");
		
		rightPanel.add(instructionLabel, BorderLayout.SOUTH);
		
		this.setLayout(new BorderLayout());
		this.add(gamePanel, BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.CENTER);
		
		this.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				if (gamePanel.isWaitingBegin()) {
					if (Character.isLetter(e.getKeyChar()) ||
						Character.isDigit(e.getKeyChar())) {
						gamePanel.addToPlayerName(e.getKeyChar());
					}
					if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						gamePanel.trimPlayerName();
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					isPressedLeft = true;
					rotateLeft = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					isPressedRight = true;
					rotateRight = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (gamePanel.isWaitingLaunch()) {
						gamePanel.prepareLaunch();
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_P) {
					if (!gamePanel.isWaitingBegin() &&
						!gamePanel.isOver() &&
						!gamePanel.isWaitingLaunch()) {
						gamePanel.togglePaused();
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (gamePanel.isWaitingBegin()) {
						gamePanel.setState(GameState.WAITING_LAUNCH);
					}
					if (gamePanel.isOver()) {
						gamePanel.restartGame();
						updateScoreBoard();
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					isPressedLeft = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					isPressedRight = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (gamePanel.isPreparingLaunch()) {
						gamePanel.launchBall();
					}
				}
			}
		});
	}
	
	public void setup() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(700, 685);
		this.setTitle("Pinball");
		this.setVisible(true);
	}
	
	public void updateScoreBoard() {
		ArrayList<Register> scores = gamePanel.getScores();
		String text;
		if (scores.size() == 0) {
			text = "";
		} else {
			text = "<html><table><tr><td>#</td><td>NAME</td><td>SCORE</td></tr>";
			for (int i = 0; i < scores.size(); i++) {
				text += "<tr><td>" + (i+1) + "</td>"
						+ "<td> " + scores.get(i).getName() + "</td>"
						+ "<td style='color:red;'> "
						+ String.format("%06d", scores.get(i).getScore())
						+ "</td></tr>";
			}
			text += "</table></html>";
		}
		this.scoreBoard.setText(text);
	}
	
	public void computeRotations() {
		Integer rotation;
		if (this.rotateLeft) {
			rotation = this.gamePanel.getLeftRotation();
			if (rotation >= 0) {
				this.gamePanel.setLeftRising(true);
			}
			if (rotation <= -90) {
				this.gamePanel.setLeftRising(false);
			}
			if (rotation >= -2 && !this.gamePanel.isLeftRising()) {
				this.rotateLeft = false;
			}
			if (this.gamePanel.isLeftRising()) {
				rotation -= 2;
			} else if (!isPressedLeft || rotation != -90) {
				rotation += 2;
			}
			this.gamePanel.setLeftRotation(rotation);
		}
		
		if (this.rotateRight) {
			rotation = this.gamePanel.getRightRotation();
			if (rotation <= 0) {
				this.gamePanel.setRightRising(true);
			}
			if (rotation >= 90) {
				this.gamePanel.setRightRising(false);
			}
			if (rotation <= 2 && !this.gamePanel.isRightRising()) {
				this.rotateRight = false;
			}
			if (this.gamePanel.isRightRising()) {
				rotation += 2;
			} else if (!isPressedRight || rotation != 90) {
				rotation -= 2;
			}
			this.gamePanel.setRightRotation(rotation);
		}
	}
	
	public void run() throws InterruptedException {
		this.setup();
		
		while (true) {
			if (!gamePanel.isPaused() && !gamePanel.isOver()) {
				this.computeRotations();
				this.gamePanel.update();
				String points = String.format("%06d", gamePanel.getTotalPoints());
				this.currentPoints.setText("<html>SCORE<span style='color:red;'>" +
											points + "</span></html>");
			} else if (gamePanel.isOver()) {
				gamePanel.drawGameOver();
			} else if (gamePanel.isPaused()) {
				gamePanel.drawPaused();
			}
			//Thread.sleep(1);
			Thread.sleep(0, 50);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		GameFrame game = new GameFrame();
		game.run();
	}

}
