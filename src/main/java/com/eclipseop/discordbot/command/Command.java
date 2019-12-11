package com.eclipseop.discordbot.command;

import com.eclipseop.discordbot.Bot;
import net.dv8tion.jda.api.entities.Message;

/**
 * Created by Eclipseop.
 * Date: 3/19/2019.
 */
public abstract class Command {

	private final Bot bot;

	public Command(Bot bot) {
		this.bot = bot;
	}

	public Bot getBot() {
		return bot;
	}

	public abstract String getPrefix();
	public abstract String getHelpText();
	public abstract void execute(Message trigger);
}
