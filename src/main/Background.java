package main;

import java.awt.image.BufferedImage;

public class Background {

	private BufferedImage sprite;
	private Integer offset;
	
	public Background(Integer offset) {
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
