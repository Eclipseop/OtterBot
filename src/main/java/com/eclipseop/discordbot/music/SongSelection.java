package com.eclipseop.discordbot.music;

import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;

/**
 * Created by Eclipseop.
 * Date: 8/6/2018.
 */
public class SongSelection {

	private SearchResult[] results;

	public SongSelection(SearchResult[] results) {
		this.results = results;
	}

	public SearchResult[] getResults() {
		return results;
	}

	public MessageEmbed buildMessage() {
		final EmbedBuilder message = new EmbedBuilder();

		message.setTitle("Search Results");
		for (int i = 0; i < results.length; i++) {
			SearchResult result = results[i];
			message.addField(i + 1 + "", result.getSnippet().getTitle(), true);
		}
		message.setThumbnail(results[0].getSnippet().getThumbnails().getDefault().getUrl());
		message.setColor(Color.YELLOW);

		return message.build();
	}
}
