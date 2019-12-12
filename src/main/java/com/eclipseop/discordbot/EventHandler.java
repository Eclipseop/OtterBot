package com.eclipseop.discordbot;

import com.eclipseop.discordbot.command.Command;
import com.eclipseop.discordbot.command.impl.HelpCommand;
import com.eclipseop.discordbot.command.impl.MerchCommand;
import com.eclipseop.discordbot.command.impl.MusicCommand;
import com.eclipseop.discordbot.command.impl.StatCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Eclipseop.
 * Date: 2/11/2019.
 */
public class EventHandler extends ListenerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

	private final Bot bot;
	private final List<Command> commands = new ArrayList<>();

	public EventHandler(Bot bot) {
		this.bot = bot;

		commands.add(new MusicCommand(bot));
		commands.add(new MerchCommand(bot));
		commands.add(new HelpCommand(bot));
		commands.add(new StatCommand(bot));
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		Member member = event.getMember();
		if (member == null) return;
		User user = member.getUser();

		String stringContent = event.getMessage().getContentRaw();
		if (stringContent.equals("bad bot")) {
			bot.sendMessage("sry <={", event.getMessage().getTextChannel());
		}

		final TextChannel replyChannel = event.getChannel();

		logger.info("Received message from: " + user.getAsTag() +
				", in: " + event.getGuild().getName() + "#" + replyChannel.getName() +
				", content: " + stringContent);

		if (!stringContent.startsWith("#")) return;
		stringContent = stringContent.substring(1);

		final String[] args = stringContent.split(" ");
		logger.info("Processing args: " + Arrays.toString(args));

		Command command = commands.stream().filter(p -> p.getPrefix().equalsIgnoreCase(args[0])).findFirst().orElse(null);
		if (command != null) {
			if (args.length > 1 && args[1].equals("help")) {
				bot.sendMessage(command.getHelpText(), replyChannel);
			} else {
				command.execute(event.getMessage());
			}
		}
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		bot.handleLeaving(event);
	}

	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		bot.handleLeaving(event);
	}

	@Override
	public void onReady(@Nonnull ReadyEvent event) {
		bot.sendMessage("OtterBot is back online!", event.getJDA().getTextChannelById("405168846283079692"));
	}
}
