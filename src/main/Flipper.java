package main;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Flipper {
	
	private BufferedImage sprite;
	
	public Flipper(String path) {
		try {
			this.sprite = ImageIO.read(new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	
	

}
