package com.eclipseop.discordbot.music.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

/**
 * Created by Eclipseop.
 * Date: 8/6/2018.
 */
public class GuildMusicManager {

	private final AudioPlayer player;
	private final TrackScheduler scheduler;
	private final AudioPlayerSendHandler sendHandler;

	public GuildMusicManager(AudioHandler audioHandler) {
		player = audioHandler.getPlayerManager().createPlayer();
		scheduler = new TrackScheduler(audioHandler, player);
		sendHandler = new AudioPlayerSendHandler(player);
		player.addListener(scheduler);
	}

	public AudioPlayer getPlayer() {
		return player;
	}

	public AudioPlayerSendHandler getSendHandler() {
		return sendHandler;
	}

	public TrackScheduler getScheduler() {
		return scheduler;
	}
}
