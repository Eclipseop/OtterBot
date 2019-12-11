package com.eclipseop.discordbot.command.impl;

import com.eclipseop.discordbot.Bot;
import com.eclipseop.discordbot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand extends Command {

	public HelpCommand(Bot bot) {
		super(bot);
	}

	@Override
	public String getPrefix() {
		return "help";
	}

	@Override
	public String getHelpText() {
		return null;
	}

	@Override
	public void execute(Message trigger) {
		TextChannel textChannel = trigger.getTextChannel();
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.addField(
				"Commands ('#{command} help' for options):",
				"੦ m (music)\n" +
						"੦ p (poe)\n" +
						"੦ stats\n",
				false);
		getBot().sendMessage(embedBuilder.build(), textChannel);
	}
}
