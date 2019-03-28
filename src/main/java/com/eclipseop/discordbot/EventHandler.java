package com.eclipseop.discordbot;

import com.eclipseop.discordbot.command.Command;
import com.eclipseop.discordbot.command.impl.MerchCommand;
import com.eclipseop.discordbot.command.impl.MusicCommand;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Eclipseop.
 * Date: 2/11/2019.
 */
public class EventHandler extends ListenerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

	private Bot bot;
	private List<Command> commands = new ArrayList<>();

	public EventHandler(Bot bot) {
		this.bot = bot;

		commands.add(new MusicCommand(bot));
		commands.add(new MerchCommand(bot));
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getMember().getUser().isBot()) return;

		String stringContent = event.getMessage().getContentRaw();
		logger.info("Received message from: " + event.getMember().getNickname() +
				", content: " + stringContent);

		if (!stringContent.startsWith("#")) return;
		stringContent = stringContent.substring(1);

		final String[] args = stringContent.split(" ");
		logger.info("Processing args: " + Arrays.toString(args));

		final TextChannel replyChannel = event.getChannel();
		commands.stream().filter(p -> p.getPrefixArg().test(args[0])).findFirst().ifPresentOrElse(command -> {
			if (args.length > 1 && args[1].equals("help")) {
				bot.sendMessage(command.getHelpText(), replyChannel);
			} else {
				command.execute(event.getMessage());
			}
		}, () -> {
			logger.debug("Received unknown arg[0] of " + args[0]);
			bot.sendMessage(String.format("**Unknown command:** `%s`", args[0]), replyChannel);
		});
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		if (event.getMember().getUser().isBot()) return;
		if (bot.getGuild().getAudioManager().getConnectedChannel().getMembers().size() == 1) {
			bot.leaveVoice();
		}
	}

	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		if (event.getMember().getUser().isBot()) return;
		if (bot.getGuild().getAudioManager().getConnectedChannel().getMembers().size() == 1) {
			bot.leaveVoice();
		}
	}
}
