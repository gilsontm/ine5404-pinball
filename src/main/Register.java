package main;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Register implements Serializable, Comparable<Register> {

	private String name;
	private Integer score;
	
	public Register(String name, Integer score) {
		this.name = (name == null || name.isEmpty() ? "unknown" : name);
		this.score = (score == null ? 0 : score);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public int compareTo(Register register) {
		if (this.score > register.score) {
			return 1;
		} else if (this.score < register.score) {
			return -1;
		}
		return 0;
	}
}
