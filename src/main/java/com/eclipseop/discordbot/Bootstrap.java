package com.eclipseop.discordbot;

import com.eclipseop.discordbot.util.Key;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by Eclipseop.
 * Date: 2/11/2019.
 */
public class Bootstrap {

	private static Key KEYS;

	public static Key getKeys() {
		return KEYS;
	}

	public static void main(String[] args) {
		Configurator.setRootLevel(Level.DEBUG);

		try {
			final String path = new File("src/main/java/com/eclipseop/discordbot/util/Keys.json").getAbsolutePath();
			KEYS = new Gson().fromJson(new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(path)))), Key.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			final Bot bot = new Bot();

			new Thread(() -> {
				final Scanner scanner = new Scanner(System.in);
				while (true) {
					if (scanner.nextLine().equals("stop")) {
						System.exit(0);
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
