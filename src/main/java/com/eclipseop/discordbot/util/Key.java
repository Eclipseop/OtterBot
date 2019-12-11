package com.eclipseop.discordbot.util;

/**
 * Created by Eclipseop.
 * Date: 3/28/2019.
 */
public class Key {
	private final String googleKey;
	private final String discordKey;

	public Key(String googleKey, String discordKey) {
		this.googleKey = googleKey;
		this.discordKey = discordKey;
	}

	public String getDiscordKey() {
		return discordKey;
	}

	public String getGoogleKey() {
		return googleKey;
	}
}