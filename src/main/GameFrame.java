package main;

import java.awt.BorderLayout;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	
	private GamePanel gamePanel;
	
	public GameFrame() {
		gamePanel = new GamePanel(new Ball(100, 200));
		this.setLayout(new BorderLayout());
		this.add(gamePanel, BorderLayout.CENTER);
	}
	
	public void setup() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 400);
		this.setTitle("Pinball");
		this.setVisible(true);
	}
	
	
	
	public static void main(String[] args) {
		GameFrame game = new GameFrame();
		game.setup();
	}

}
