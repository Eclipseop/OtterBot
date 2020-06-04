package com.eclipseop.discordbot;

import com.eclipseop.discordbot.music.player.AudioHandler;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eclipseop.
 * Date: 2/11/2019.
 */
public class Bot {

	private static final Logger logger = LoggerFactory.getLogger(Bot.class);

	private final JDA jda;
	private final Map<Guild, AudioHandler> audioHandlers = new HashMap<>();

	public Bot() throws LoginException {
		this.jda = JDABuilder.createDefault(Bootstrap.getKeys().getDiscordKey())
				.setAutoReconnect(true)
				.setActivity(Activity.playing("`help for commands!"))
				.addEventListeners(new EventHandler(this))
				.build();
	}

	public void sendMessage(String message, TextChannel textChannel) {
		textChannel.sendMessage(message).queue();
	}

	public void sendMessage(MessageEmbed messageEmbed, TextChannel textChannel) {
		textChannel.sendMessage(messageEmbed).queue();
	}

	public AudioHandler getAudioHandler(Guild guild) {
		AudioHandler audioHandler;
		if ((audioHandler = audioHandlers.get(guild)) == null) {
			audioHandlers.put(guild, (audioHandler = new AudioHandler(this)));
		}

		return audioHandler;
	}

	public boolean joinVoiceChannel(final VoiceChannel voiceChannel) {
		final AudioManager audioManager = voiceChannel.getGuild().getAudioManager();

		audioManager.setSendingHandler(getAudioHandler(voiceChannel.getGuild()).getGuildMusicManager().getSendHandler());
		audioManager.openAudioConnection(voiceChannel);

		try {
			Thread.sleep(750);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return audioManager.isConnected();
	}

	public void handleLeaving(GuildVoiceUpdateEvent event) {
		final Guild guild = event.getChannelLeft().getGuild();

		if (event.getEntity().getUser().isBot() || !guild.getAudioManager().isConnected()) return;
		if (guild.getAudioManager().getConnectedChannel().getMembers().size() == 1) {
			final AudioManager audioManager = guild.getAudioManager();
			audioManager.closeAudioConnection();
		}
	}

	public JDA getJda() {
		return jda;
	}
}
