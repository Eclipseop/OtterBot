package com.eclipseop.discordbot.command.impl;

import com.eclipseop.discordbot.Bot;
import com.eclipseop.discordbot.command.Command;
import com.eclipseop.discordbot.util.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
	public MessageEmbed getHelpText() {
		return null;
	}

	@Override
	public void execute(Message trigger) {
		TextChannel textChannel = trigger.getTextChannel();

		MessageBuilder messageBuilder = new MessageBuilder("Commands ('#{command} help' for options)");
		messageBuilder.addField("m (music)");
		messageBuilder.addField("p (poe)");
		messageBuilder.addField("stats");

		messageBuilder.setFooter("Need more help? Go to https://discord.gg/nZjHFJj");

		getBot().sendMessage(messageBuilder.build(), textChannel);
	}
}
