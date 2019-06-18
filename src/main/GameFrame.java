package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	
	private GamePanel gamePanel;
	private JPanel rightPanel;
	
	public GameFrame() {
		gamePanel = new GamePanel(new Ball(100, 200));
		gamePanel.setMaximumSize(new Dimension(400, 650));
		
		rightPanel = new JPanel();
		rightPanel.setMinimumSize(new Dimension(500, 500));
		rightPanel.setBackground(Color.YELLOW);
		
		this.setBackground(Color.pink);
		//gamePanel.setBackground(Color.BLUE);
		this.setLayout(new BorderLayout());
		this.add(gamePanel, BorderLayout.CENTER);
		this.add(rightPanel, BorderLayout.EAST);
	}
	
	public void setup() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 650);
		this.setTitle("Pinball");
		this.setVisible(true);
	}
	
	public static void main(String[] args) throws InterruptedException {
		GameFrame game = new GameFrame();
		game.setup();
		
		while (true) {
			Thread.sleep(1);
			game.gamePanel.getBall().move();
			game.gamePanel.repaint();
		}
	}

}
