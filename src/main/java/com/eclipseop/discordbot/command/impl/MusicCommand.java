package com.eclipseop.discordbot.command.impl;

import com.eclipseop.discordbot.Bot;
import com.eclipseop.discordbot.command.Command;
import com.eclipseop.discordbot.music.SongSelection;
import com.eclipseop.discordbot.music.YoutubeSearcher;
import com.eclipseop.discordbot.music.player.AudioHandler;
import com.eclipseop.discordbot.util.MessageBuilder;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
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
	public String getPrefix() {
		return "m";
	}

	@Override
	public MessageEmbed getHelpText() {
		MessageBuilder commands = new MessageBuilder("Music Commands");
		commands.setColor(Color.GREEN);
		commands.addField("**#m play {url}**: Plays a specific video.");
		commands.addField("**#m play {text}**: Searches for a video with the given query.");
		commands.addField("**#m play {1-5}**: Plays a specific song from song selection.");
		commands.addField("**#m volume {num}**: Sets the volume of the player. Will Reset after every song.");
		commands.addField("**#m playing**: Shows the current song.");
		commands.addField("**#m skip**: Skips the current song.");
		commands.addField("**#m stop**: Stops all playback and clears the queue.");
		commands.addField("**#m queue**: Shows the top 5 songs in the queue.");
		commands.addField("**#m remove {1-5}**: Removes the nth song from the queue.");
		commands.addField("**#m playing**: Shows the current song playing.");
		commands.addField("**#m earrape**: Turns on *earrape* for the current song.");

		return commands.build();
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
		String command = trigger.getContentRaw().substring(3);

		LinkedList<AudioTrack> queue = audioHandler.getGuildMusicManager().getScheduler().getQueue();
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
			case "queue":
				if (queue.size() == 0) {
					getBot().sendMessage("No songs are currently in the queue.", textChannel);
					return;
				}

				EmbedBuilder embedBuilder = new EmbedBuilder();

				String songs = "";
				for (int i = 0; i < 5; i++) {
					try {
						AudioTrack audioTrack = queue.get(i);
						songs += (i + 1) + " - " + audioTrack.getInfo().title + "\n";
					} catch (IndexOutOfBoundsException e) {
						break;
					}
				}
				embedBuilder.addField("Queue (showing top 5)", songs, false);
				getBot().sendMessage(embedBuilder.build(), textChannel);
				break;
			case "remove":
				int index = Integer.parseInt(command.split(" ")[1]);
				if (queue.size() < index) {
					getBot().sendMessage("Error removing song, index too large!", textChannel);
					return;
				}
				AudioTrack audioTrack = queue.get(index - 1);
				queue.remove(audioTrack);
				getBot().sendMessage("Removed " + audioTrack.getInfo().title + " from the queue.", textChannel);
				break;
		}
	}
}
