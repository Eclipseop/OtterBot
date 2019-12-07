package com.eclipseop.discordbot.command.impl;

import com.eclipseop.discordbot.Bot;
import com.eclipseop.discordbot.command.Command;
import com.eclipseop.discordbot.music.SongSelection;
import com.eclipseop.discordbot.music.YoutubeSearcher;
import com.eclipseop.discordbot.music.player.AudioHandler;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Eclipseop.
 * Date: 3/19/2019.
 */
public class MusicCommand extends Command {

	private YoutubeSearcher youtubeSearcher;

	private static final Logger logger = LoggerFactory.getLogger(MusicCommand.class);

	public MusicCommand(Bot bot) {
		super(bot);
		try {
			this.youtubeSearcher = new YoutubeSearcher();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] getPrefixArgs() {
		return new String[]{"play", "stop", "skip", "volume", "earrape", "playing"};
	}

	@Override
	public String getHelpText() {
		return "`#play {url}`: Plays a specific video.\n" +
				"`#play {text}`: Searches for a video with the given query.\n" +
				"`#play {1-5}`: Plays a specific song from song selection.\n" +
				"`#volume {num}`: Sets the volume of the player. Will Reset after every song.\n" +
				"`#playing`: Shows the current song.";
	}

	@Override
	public void execute(Message trigger) {
		final VoiceChannel voiceChannel = trigger.getMember().getVoiceState().getChannel();
		final TextChannel textChannel = trigger.getTextChannel();
		final AudioHandler audioHandler = getBot().getAudioHandler(trigger.getGuild());

		if (voiceChannel == null) {
			getBot().sendMessage("You are not currently in a voice channel!", textChannel);
			return;
		}
		String command = trigger.getContentRaw().substring(1);

		switch (command.split(" ")[0]) {
			case "play":
				if (!getBot().joinVoiceChannel(voiceChannel)) {
					getBot().sendMessage("Couldn't join voice channel!", textChannel);
					logger.error("Couldn't join voice channel " + voiceChannel.getName());
					return;
				}
				command = command.substring(5);

				final SongSelection selectionByUser = audioHandler.getCacheSelector().get(trigger.getAuthor());

				if (command.length() == 1 && Pattern.matches("\\d", command)) {
					if (selectionByUser == null) {
						getBot().sendMessage("No Song Selector!", textChannel);
						return;
					}

					audioHandler.loadSong(
							"https://www.youtube.com/watch?v=" +
									selectionByUser
											.getResults()[Integer.parseInt(command.substring(0, 1)) - 1]
											.getId()
											.getVideoId(),
							textChannel
					);
					audioHandler.getCacheSelector().remove(trigger.getAuthor());
					return;
				} else if (command.startsWith("http")) {
					audioHandler.loadSong(command, textChannel);
					return;
				}

				final List<SearchResult> searchResults = youtubeSearcher.search(command);
				final SongSelection songSelection = new SongSelection(searchResults.toArray(new SearchResult[5]));

				audioHandler.getCacheSelector().put(trigger.getAuthor(), songSelection);
				final MessageEmbed message = songSelection.buildMessage();
				getBot().sendMessage(message, textChannel);
				break;
			case "stop":
				audioHandler.getGuildMusicManager().getScheduler().clearQueueAndStop();
				getBot().sendMessage("Stopping playback.", textChannel);
				break;
			case "skip":
				final String songName = audioHandler.getGuildMusicManager().getPlayer().getPlayingTrack().getInfo().title;
				logger.info("Skipping: " + songName);
				getBot().sendMessage("Skipping " + songName + ".", textChannel);

				audioHandler.getGuildMusicManager().getScheduler().nextTrack();
				break;
			case "volume":
				final int volume = Integer.parseInt(command.split(" ")[1]);
				logger.info("Setting volume to" + volume);
				audioHandler.getGuildMusicManager().getPlayer().setVolume(volume);
				getBot().sendMessage("Setting volume to " + volume + ".", textChannel);
				break;
			case "earrape":
				getBot().sendMessage("gottcha fam :ok_hand:", textChannel);
				audioHandler.getGuildMusicManager().getPlayer().setVolume(250);
				break;
			case "playing":
				final AudioTrack current = audioHandler.getGuildMusicManager().getPlayer().getPlayingTrack();
				if (current == null) {
					getBot().sendMessage("Currently not playing!", textChannel);
				} else {
					getBot().sendMessage("Playing: " + current.getInfo().title, textChannel);
				}
				break;
		}
	}
}
