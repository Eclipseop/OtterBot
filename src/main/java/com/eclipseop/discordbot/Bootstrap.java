package com.eclipseop.discordbot;

import com.eclipseop.discordbot.util.Key;
import com.google.gson.Gson;

import javax.security.auth.login.LoginException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by Eclipseop.
 * Date: 2/11/2019.
 */
public class Bootstrap {

	private static Key KEYS;
	private static Bot bot;

	public static Bot getBot() {
		return bot;
	}

	public static Key getKeys() {
		return KEYS;
	}

	public static void main(String... args) {
		if (args.length != 2) {
			System.out.println("Please pass google and discord key, in that order.");
			return;
		}
		KEYS = new Key(args[0], args[1]);
		System.out.println("Reading keys as " + KEYS);

		try {
			Bootstrap.bot = new Bot();
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}
}
