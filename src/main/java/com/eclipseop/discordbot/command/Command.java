package com.eclipseop.discordbot.command;

import com.eclipseop.discordbot.Bot;
import net.dv8tion.jda.core.entities.Message;

import java.util.function.Predicate;

/**
 * Created by Eclipseop.
 * Date: 3/19/2019.
 */
public abstract class Command {

	private Bot bot;

	public Command(Bot bot) {
		this.bot = bot;
	}

	public Bot getBot() {
		return bot;
	}

	public abstract Predicate<String> getPrefixArg();
	public abstract String getHelpText();
	public abstract void execute(Message trigger);
}
