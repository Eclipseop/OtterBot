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

		MessageBuilder messageBuilder = new MessageBuilder("Commands (Example: \"#m help\" for help towards the music specific commands)");
		messageBuilder.addField("m (music)");
		messageBuilder.addField("p (poe)");
		messageBuilder.addField("r (roll)");
		messageBuilder.addField("stats");

		messageBuilder.setFooter("Need more help? Go to https://discord.gg/nZjHFJj");

		getBot().sendMessage(messageBuilder.build(), textChannel);
	}
}
