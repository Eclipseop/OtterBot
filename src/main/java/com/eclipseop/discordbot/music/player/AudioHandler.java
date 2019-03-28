package com.eclipseop.discordbot.music.player;

import com.eclipseop.discordbot.Bot;
import com.eclipseop.discordbot.music.SongSelection;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Eclipseop.
 * Date: 8/28/2018.
 */
public class AudioHandler {

	private static final Logger logger = LoggerFactory.getLogger(AudioHandler.class);

	private final GuildMusicManager guildMusicManager;
	private final DefaultAudioPlayerManager playerManager;
	private final HashMap<User, SongSelection> cacheSelector;

	private final Bot bot;

	public AudioHandler(Bot bot) {
		this.bot = bot;
		this.cacheSelector = new HashMap<>();

		this.playerManager = new DefaultAudioPlayerManager();
		this.guildMusicManager = new GuildMusicManager(this);
		AudioSourceManagers.registerRemoteSources(playerManager);
	}

	public Bot getBot() {
		return bot;
	}

	public GuildMusicManager getGuildMusicManager() {
		return guildMusicManager;
	}

	public DefaultAudioPlayerManager getPlayerManager() {
		return playerManager;
	}

	public HashMap<User, SongSelection> getCacheSelector() {
		return cacheSelector;
	}

	public void loadSong(final String trackUrl, TextChannel logChannel) {
		loadSong(trackUrl, true, logChannel);
	}

	public void loadSong(final String trackUrl) {
		loadSong(trackUrl, false, null);
	}

	public void loadSong(final String trackUrl, boolean log, TextChannel logChannel) {
		playerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
			private final TrackScheduler scheduler = guildMusicManager.getScheduler();

			@Override
			public void trackLoaded(AudioTrack track) {
				if (log) {
					final String message = String.format("Adding song to queue: %s. Position: %d.", track.getInfo().title, scheduler.getQueue().size() + 1);
					bot.sendMessage(message, logChannel);
				}
				scheduler.queue(track);
				logger.info("Loading " + track.getInfo().title);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				bot.sendMessage("Adding songs to queue: " + Arrays.toString(playlist.getTracks().toArray()), logChannel);
				playlist.getTracks().forEach(scheduler::queue);
			}

			@Override
			public void noMatches() {
				bot.sendMessage("Nothing found by " + trackUrl, logChannel);
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				bot.sendMessage("Could not play: " + exception.getMessage(), logChannel);
			}
		});
	}
}
