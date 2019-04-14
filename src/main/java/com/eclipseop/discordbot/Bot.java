package com.eclipseop.discordbot;

import com.eclipseop.discordbot.music.player.AudioHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.core.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Eclipseop.
 * Date: 2/11/2019.
 */
public class Bot {

	private static final Logger logger = LoggerFactory.getLogger(Bot.class);
	private final Random random = new Random();

	private JDA jda;
	private Map<Guild, AudioHandler> audioHandlers = new HashMap<>();

	private final Game[] games = {
			Game.of(Game.GameType.WATCHING, "SmurfyValVal @ Chaturbate.com"),
			Game.of(Game.GameType.STREAMING, "Call with Tyler"),
			Game.of(Game.GameType.WATCHING, "Hyink grow up"),
			Game.of(Game.GameType.WATCHING, "Free movies on 123freemovies.com"),
			Game.of(Game.GameType.WATCHING, "♂ Boy ♂ Band ♂ Catalina ♂"),
			Game.of(Game.GameType.LISTENING, "Weed-Smoking ASMR"),
			Game.of(Game.GameType.STREAMING, "You've been gnomed")
	};

	public Bot() throws LoginException {
		this.jda = new JDABuilder(AccountType.BOT)
				.setToken(Bootstrap.getKeys().DISCORD)
				.setAutoReconnect(true)
				.build();

		jda.addEventListener(new EventHandler(this));
		setRandomGame();
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

		return audioManager.isAttemptingToConnect() || audioManager.isConnected();
	}

	public void handleLeaving(GuildVoiceUpdateEvent event) {
		final Guild guild = event.getGuild();

		if (event.getMember().getUser().isBot() || !guild.getAudioManager().isConnected()) return;
		if (guild.getAudioManager().getConnectedChannel().getMembers().size() == 1) {
			final AudioManager audioManager = guild.getAudioManager();
			setRandomActivity();
			audioManager.closeAudioConnection();
		}
	}

	public void setGame(Game game) {
		jda.getPresence().setGame(game);
	}

	public void setRandomGame() {
		setGame(games[random.nextInt(games.length)]);
	}
}
