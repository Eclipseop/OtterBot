package com.eclipseop.discordbot;

import com.eclipseop.discordbot.music.player.AudioHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by Eclipseop.
 * Date: 2/11/2019.
 */
public class Bot {

	private static final Logger logger = LoggerFactory.getLogger(Bot.class);
	private final Random random = new Random();

	private AudioHandler audioHandler;
	private JDA jda;

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
				.setToken(Bootstrap.getKEYS().DISCORD)
				.setAutoReconnect(true)
				.build();

		this.audioHandler = new AudioHandler(this);
		jda.addEventListener(new EventHandler(this));
	}

	public Guild getGuild() {
		// TODO: 3/28/2019 make dynamic, no longer requiring guild id
		return jda.getGuildById(Bootstrap.getKEYS().GUILD);
	}

	public void sendMessage(String message, TextChannel textChannel) {
		textChannel.sendMessage(message).queue();
	}

	public void sendMessage(MessageEmbed messageEmbed, TextChannel textChannel) {
		textChannel.sendMessage(messageEmbed).queue();
	}

	public AudioHandler getAudioHandler() {
		return audioHandler;
	}

	public VoiceChannel getVoiceChannelFromUser(Predicate<User> user) {
		for (VoiceChannel voiceChannel : getGuild().getVoiceChannels()) {
			if (voiceChannel.getMembers().stream().map(Member::getUser).anyMatch(user)) {
				return voiceChannel;
			}
		}

		return null;
	}

	public VoiceChannel getVoiceChannelFromUser(User user) {
		return getVoiceChannelFromUser(user::equals);
	}

	public boolean joinVoiceChannel(final VoiceChannel voiceChannel) {
		final AudioManager audioManager = getGuild().getAudioManager();
		audioManager.setSendingHandler(audioHandler.getGuildMusicManager().getSendHandler());
		audioManager.openAudioConnection(voiceChannel);

		try {
			Thread.sleep(750);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return audioManager.isAttemptingToConnect() || audioManager.isConnected();
	}

	public boolean leaveVoice() {
		final AudioManager audioManager = getGuild().getAudioManager();
		audioManager.closeAudioConnection();

		return !audioManager.isConnected();
	}

	public void setGame(Game game) {
		jda.getPresence().setGame(game);
	}

	public void setRandomGame() {
		setGame(games[random.nextInt(games.length)]);
	}
}
