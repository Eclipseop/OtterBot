package com.eclipseop.discordbot.music.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Eclipseop.
 * Date: 8/6/2018.
 */
public class TrackScheduler extends AudioEventAdapter {

	private boolean repeating;
	private final LinkedList<AudioTrack> queue;
	private final AudioPlayer player;
	private final AudioHandler audioHandler;

	private static final Logger logger = LoggerFactory.getLogger(TrackScheduler.class);

	public TrackScheduler(AudioHandler audioHandler, AudioPlayer player) {
		this.queue = new LinkedList<>();
		this.audioHandler = audioHandler;

		this.player = player;
	}

	public void queue(AudioTrack track) {
		if (!queue.add(track)) {
			logger.warn("Failed to add: " + track + "!");
		}
		if (player.getPlayingTrack() == null) {
			nextTrack();
		}
	}

	public void nextTrack() {
		final AudioTrack next = queue.poll();
		player.setVolume(100);
		if (next == null) {
			player.destroy();
			return;
		}

		logger.info(String.format("Now Playing: %s", next.getInfo().title));
		player.startTrack(next, false);

		new Thread(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (player.getPlayingTrack() == null) {
				audioHandler.loadSong(next.getInfo().uri);
			}
		}).start();
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			if (repeating) {
				player.startTrack(track.makeClone(), false);
			} else if (queue.size() > 0) {
				nextTrack();
			} else {
				logger.info("Queue Empty");
			}
		}

		if (endReason.equals(AudioTrackEndReason.LOAD_FAILED)) {
			logger.warn("Failed to load %s", track.getInfo().title);
		}

	}

	public boolean isRepeating() {
		return repeating;
	}

	public void setRepeating(boolean repeating) {
		this.repeating = repeating;
	}

	public void shuffle() {
		Collections.shuffle((List<?>) queue);
	}

	public void clearQueueAndStop() {
		queue.clear();
		nextTrack();
	}

	public LinkedList<AudioTrack> getQueue() {
		return queue;
	}
}