package com.eclipseop.discordbot.pad;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Card {

	private static LoadingCache<Object, JsonObject> cache = CacheBuilder.newBuilder()
			.expireAfterWrite(1, TimeUnit.DAYS)
			.build(new CacheLoader<Object, JsonObject>() {
				@Override
				public JsonObject load(Object object) throws Exception {
					JsonParser jsonParser = new JsonParser();
					List<String> strings = Files.readAllLines(Paths.get("C:\\Users\\guest_0arb8vt\\Dropbox\\programming\\BestDerEverWasyBot\\data.json"));
					JsonObject parse = jsonParser.parse(String.join("", strings)).getAsJsonObject();
					// TODO: 2/5/2020 change to pull from actual url

					return parse;
				}
			});

	private int id;
	private String name;
	private List<Awakening> awakenings;

	private Card(int id, String name, List<Awakening> awakenings) {
		this.id = id;
		this.name = name;
		this.awakenings = awakenings;
	}

	public static Card get(int id) {
		try {
			JsonObject parse = cache.get(new Object());

			JsonArray cards = parse.getAsJsonArray("card");
			JsonArray card = cards.get(id).getAsJsonArray();

			int startIndex = 0;
			int endIndex = 0;
			for (int i = 0; i < card.size(); i++) {
				String param = card.get(i).getAsString();
				if (param.equals("0")) startIndex = i;
				if (param.equals("") || param.contains(",") && !param.contains(" ")) {
					endIndex = i;
					break;
				}
			}
			List<Awakening> awakenings = new ArrayList<>();
			for (int i = startIndex + 2; i < endIndex; i++) {
				int awakenId = card.get(i).getAsInt();
				Awakening a = Awakening.get(awakenId);
				if (a == null) {
					System.out.println("shit dawg lmao, couldn't id " + awakenId);
					continue;
				}
				awakenings.add(a);
			}

			return new Card(id, card.get(1).getAsString(), awakenings);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Awakening> getAwakenings() {
		return awakenings;
	}

	@Override
	public String toString() {
		return "Card{" +
				"id=" + id +
				", name='" + name + '\'' +
				", awakenings=" + awakenings +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Card card = (Card) o;
		return id == card.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
