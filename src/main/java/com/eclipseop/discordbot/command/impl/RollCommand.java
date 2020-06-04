package com.eclipseop.discordbot.command.impl;

import com.eclipseop.discordbot.Bot;
import com.eclipseop.discordbot.command.Command;
import com.eclipseop.discordbot.util.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Random;


public class RollCommand extends Command {

	private final Random random;

	public RollCommand(Bot bot) {
		super(bot);
		random = new Random();
	}

	@Override
	public String getPrefix() {
		return "r";
	}

	@Override
	public MessageEmbed getHelpText() {
		MessageBuilder commands = new MessageBuilder("Roll Command");
		commands.addField("**`r**: Rolls a random number 1-10.");
		commands.addField("**`r {max}**: Rolls a number 1-max.");
		commands.addField("**`r {min, max}**: Rolls a number min-max.");

		return commands.build();
	}

	@Override
	public void execute(Message trigger) {
		TextChannel textChannel = trigger.getTextChannel();
		String message = trigger.getContentRaw().trim();

		MessageBuilder messageBuilder = new MessageBuilder("");
		if (message.length() == 2) {
			messageBuilder.setTitle("Random number from 1-10");
			messageBuilder.addField("" + (random.nextInt(10) + 1));
		} else {
			try {
				message = trigger.getContentRaw().substring(3);
				String[] split = message.split(" ");
				if (split.length == 1) {
					int max = Integer.parseInt(split[0]);
					messageBuilder.setTitle("Random number from 1-" + max);
					messageBuilder.addField("" + (random.nextInt(max + 1) + 1));
				} else {
					int min = Integer.parseInt(split[0]);
					int max = Integer.parseInt(split[1]);
					messageBuilder.setTitle("Random number from " + min + "-" + max);
					messageBuilder.addField("" + (random.nextInt(max + 1 - min) + min));
				}
			} catch (NumberFormatException e) {
				getBot().sendMessage("Recieved a String, expected numbers!", textChannel);
				return;
			}
		}

		getBot().sendMessage(messageBuilder.build(), textChannel);
	}
}
