package com.eclipseop.discordbot;

import com.eclipseop.discordbot.util.Key;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

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
		Configurator.setRootLevel(Level.DEBUG);

		KEYS = new Gson().fromJson(new InputStreamReader(Bootstrap.class.getClassLoader().getResourceAsStream("Keys.json")), Key.class);

		try {
			final Bot bot = new Bot();
			Bootstrap.bot = bot;

			new Thread(() -> {
				final Scanner scanner = new Scanner(System.in);
				while (true) {
					final String line = scanner.nextLine();
					if (line.equals("stop")) {
						System.exit(0);
					} else {
						bot.getJda().getGuilds().forEach(c -> {
							bot.sendMessage("[ADMIN]: " + line, c.getDefaultChannel());
						});
					}

					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}
}
