package com.eclipseop.discordbot.music;

import com.eclipseop.discordbot.Bootstrap;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by Eclipseop.
 * Date: 8/5/2018.
 */
public class YoutubeSearcher {

	private final YouTube.Search.List search;

	private static final Logger logger = LoggerFactory.getLogger(YoutubeSearcher.class);

	public YoutubeSearcher() throws IOException {
		final YouTube builder = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), httpRequest -> {
		}).build();

		search = builder.search().list("id,snippet");
	}

	public List<SearchResult> search(String query) {
		try {
			search.setKey(Bootstrap.getKeys().getGoogleKey());
			search.setQ(query);
			search.setType("video");
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(5L);

			return search.execute().getItems();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
