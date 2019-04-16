package com.eclipseop.discordbot;

import com.eclipseop.discordbot.music.player.AudioHandler;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.managers.AudioManager;
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

	private final JDA jda;
	private final Map<Guild, AudioHandler> audioHandlers = new HashMap<>();

	private final Activity[] activities = {
			Activity.of(Activity.ActivityType.WATCHING, "SmurfyValVal @ Chaturbate.com"),
			Activity.of(Activity.ActivityType.STREAMING, "Call with Tyler"),
			Activity.of(Activity.ActivityType.WATCHING, "Hyink grow up"),
			Activity.of(Activity.ActivityType.WATCHING, "Free movies on 123freemovies.com"),
			Activity.of(Activity.ActivityType.WATCHING, "♂ Boy ♂ Band ♂ Catalina ♂"),
			Activity.of(Activity.ActivityType.LISTENING, "Weed-Smoking ASMR"),
			Activity.of(Activity.ActivityType.STREAMING, "You've been gnomed")
	};

	public Bot() throws LoginException {
		this.jda = new JDABuilder(AccountType.BOT)
				.setToken(Bootstrap.getKeys().DISCORD)
				.setAutoReconnect(true)
				.build();

		jda.addEventListener(new EventHandler(this));
		setRandomActivity();
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
		final Guild guild = event.getChannelLeft().getGuild();

		if (event.getEntity().getUser().isBot() || !guild.getAudioManager().isConnected()) return;
		if (guild.getAudioManager().getConnectedChannel().getMembers().size() == 1) {
			final AudioManager audioManager = guild.getAudioManager();
			setRandomActivity();
			audioManager.closeAudioConnection();
		}
	}

	public void setActivity(Activity activity) {
		jda.getPresence().setActivity(activity);
	}

	public void setRandomActivity() {
		setActivity(activities[random.nextInt(activities.length)]);
	}

	public JDA getJda() {
		return jda;
	}
}
