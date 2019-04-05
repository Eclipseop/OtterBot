package com.eclipseop.discordbot.command.impl;

import com.eclipseop.discordbot.Bot;
import com.eclipseop.discordbot.command.Command;
import com.eclipseop.discordbot.music.SongSelection;
import com.eclipseop.discordbot.music.YoutubeSearcher;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
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
	public Predicate<String> getPrefixArg() {
		return s -> s.equals("play") || s.equals("stop") || s.equals("skip");
	}

	@Override
	public String getHelpText() {
		return "`#play {url}`: Plays a specific video.\n" +
				"`#play {text}`: Searches for a video with the given query.\n" +
				"`#play {1-5}`: Plays a specific song from song selection.";
	}

	@Override
	public void execute(Message trigger) {
		final VoiceChannel voiceChannel = trigger.getMember().getVoiceState().getChannel();
		if (voiceChannel == null) {
			getBot().sendMessage("You are not currently in a voice channel!", trigger.getTextChannel());
			return;
		}
		String command = trigger.getContentRaw().substring(1);

		switch (command.split(" ")[0]) {
			case "play":
				if (!getBot().joinVoiceChannel(voiceChannel)) {
					getBot().sendMessage("Couldn't join voice channel!", trigger.getTextChannel());
					logger.error("Couldn't join voice channel " + voiceChannel.getName());
					return;
				}
				command = command.substring(5);

				if (Pattern.matches("\\d", command.substring(0, 1))) {
					getBot().getAudioHandler().loadSong(
							"https://www.youtube.com/watch?v=" +
									getBot()
											.getAudioHandler()
											.getCacheSelector()
											.get(trigger.getAuthor())
											.getResults()[Integer.parseInt(command.substring(0, 1)) - 1]
											.getId()
											.getVideoId(),
							trigger.getTextChannel()
					);
					return;
				} else if (command.startsWith("http")) {
					getBot().getAudioHandler().loadSong(command);
					return;
				}

				final List<SearchResult> searchResults = youtubeSearcher.search(command);
				final SongSelection songSelection = new SongSelection(searchResults.toArray(new SearchResult[5]));

				getBot().getAudioHandler().getCacheSelector().put(trigger.getAuthor(), songSelection);
				final MessageEmbed message = songSelection.buildMessage();
				getBot().sendMessage(message, trigger.getTextChannel());
				break;
			case "stop":
				getBot().getAudioHandler().getGuildMusicManager().getScheduler().clearQueueAndStop();
				getBot().sendMessage("Stopping playback.", trigger.getTextChannel());
				break;
			case "skip":
				final String songName = getBot().getAudioHandler().getGuildMusicManager().getPlayer().getPlayingTrack().getInfo().title;
				logger.info("Skipping: " + songName);
				getBot().sendMessage("Skipping " + songName + ".", trigger.getTextChannel());

				getBot().getAudioHandler().getGuildMusicManager().getScheduler().nextTrack();
				break;
		}
	}
}
