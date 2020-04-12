package com.eclipseop.discordbot.pad;

import java.util.Arrays;
import java.util.List;

public class Assist {

	private String name;
	private List<Awakening> awakenings;

	public Assist(String name, Awakening... awakenings) {
		this.name = name;
		this.awakenings = Arrays.asList(awakenings);
	}

	public List<Awakening> getAwakenings() {
		return awakenings;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Assist{" +
				"name='" + name + '\'' +
				", awakens=" + awakenings +
				'}';
	}
}
