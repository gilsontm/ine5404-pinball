package main;

public enum Collision {

	NONE(0), FLIPPER(1), BACKGROUND(2), FOREGROUND(3);
	
	private final int value;
	
	Collision(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
