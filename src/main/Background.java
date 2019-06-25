package main;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Background {

	private BufferedImage sprite;
	private Integer offset;
	
	public Background(Integer offset) {
		this.offset = offset;
	}
	
	public Background(String path, Integer offset) {
		try {
			this.sprite = ImageIO.read(new File(getClass().getResource(path).getPath()));	
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.offset = offset;
	}

	public Integer getWidth() {
		return sprite.getWidth() - offset;
 	}
	
	public Integer getHeight() {
		return sprite.getHeight();
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
	
}
