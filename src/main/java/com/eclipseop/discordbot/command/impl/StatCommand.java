package com.eclipseop.discordbot.command.impl;

import com.eclipseop.discordbot.Bot;
import com.eclipseop.discordbot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.concurrent.TimeUnit;

public class StatCommand extends Command {

	private final long startTime;

	public StatCommand(Bot bot) {
		super(bot);
		startTime = System.currentTimeMillis();
	}

	@Override
	public String[] getPrefixArgs() {
		return new String[]{"stats"};
	}

	@Override
	public String getHelpText() {
		return "Run to see misc stats about the bot.";
	}

	@Override
	public void execute(Message trigger) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.addField("Bot Stats", buildMessage(), false);

		getBot().sendMessage(embedBuilder.build(), trigger.getTextChannel());
	}

	private String buildMessage() {
		String temp = "";

		temp += "੦ Servers -> " + getBot().getJda().getGuilds().size() + "\n";
		temp += "੦ RAM Usage -> " + Math.round(getRamUsage() / 1000000L) + "MB | " + Math.round((getRamUsage() * 1.0 / Runtime.getRuntime().totalMemory()) * 100) + "%\n";
		temp += "੦ Ping -> " + getBot().getJda().getGatewayPing() + "ms\n";
		temp += "੦ Uptime -> " + formatInterval(System.currentTimeMillis() - startTime);

		return temp;
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
