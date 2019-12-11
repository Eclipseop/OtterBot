package com.eclipseop.discordbot.command.impl;

import com.eclipseop.discordbot.Bot;
import com.eclipseop.discordbot.command.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class HelpCommand extends Command {

	public HelpCommand(Bot bot) {
		super(bot);
	}

	@Override
	public String[] getPrefixArgs() {
		return new String[]{"help"};
	}

	@Override
	public String getHelpText() {
		return null;
	}

	@Override
	public void execute(Message trigger) {
		TextChannel textChannel = trigger.getTextChannel();
		String message = trigger.getContentRaw();
		String[] split = message.split(" ");
		if (split.length == 1) {
			getBot().sendMessage("Available Help Commands `#help {command}`:\n" +
					"`music`\n" +
					"`poe`\n" +
					"`stats`", textChannel);
		} else {
			// TODO: 11/29/2019 lmao what
			switch (split[1]) {
				case "music":
					getBot().sendMessage(new MusicCommand(null).getHelpText(), textChannel);
					break;
				case "poe":
					getBot().sendMessage(new MerchCommand(null).getHelpText(), textChannel);
					break;
				case "stats":
					getBot().sendMessage(new StatCommand(null).getHelpText(), textChannel);
					break;
				default:
					getBot().sendMessage("Invalid help command.", textChannel);
					break;
			}
		}
	}
}
