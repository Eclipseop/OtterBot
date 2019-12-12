package com.eclipseop.discordbot.command.impl;

import com.eclipseop.discordbot.Bot;
import com.eclipseop.discordbot.command.Command;
import com.eclipseop.discordbot.util.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class StatCommand extends Command {

	private final long startTime;

	public StatCommand(Bot bot) {
		super(bot);
		startTime = System.currentTimeMillis();
	}

	@Override
	public String getPrefix() {
		return "stats";
	}

	@Override
	public MessageEmbed getHelpText() {
		MessageBuilder commands = new MessageBuilder("Stats Command");
		commands.setColor(Color.GREEN);
		return commands.addField("Shows information regarding the bot.").build();
	}

	@Override
	public void execute(Message trigger) {
		MessageBuilder bot_stats = new MessageBuilder("Bot Stats");
		bot_stats.addField("Servers -> " + getBot().getJda().getGuilds().size());
		bot_stats.addField("RAM Usage -> " + Math.round(getRamUsage() / 1000000L) + "MB | " + Math.round((getRamUsage() * 1.0 / Runtime.getRuntime().totalMemory()) * 100) + "%");
		bot_stats.addField("Ping -> " + getBot().getJda().getGatewayPing());
		bot_stats.addField("Uptime -> " + formatInterval(System.currentTimeMillis() - startTime));
		getBot().sendMessage(bot_stats.build(), trigger.getTextChannel());
	}

	private static String formatInterval(final long l) {
		final long hr = TimeUnit.MILLISECONDS.toHours(l);
		final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
		final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		return String.format("%02d:%02d:%02d", hr, min, sec);
	}

	private static long getRamUsage() {
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}
}
