package com.eclipseop.discordbot.poe;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Eclipseop.
 * Date: 3/25/2019.
 */
public class FindDeals {

	private static final String[] API_LINKS = {
			"https://poe.ninja/api/data/currencyoverview?league=Metamorph&type=Currency",
			"https://api.poe.watch/get?league=Metamorph&category=card",
			"https://poe.ninja/api/data/itemoverview?league=Metamorph&type=UniqueArmour",
			"https://poe.ninja/api/data/itemoverview?league=Metamorph&type=UniqueAccessory",
			"https://poe.ninja/api/data/itemoverview?league=Metamorph&type=Prophecy",
			"https://poe.ninja/api/data/itemoverview?league=Metamorph&type=UniqueMap",
			"https://poe.ninja/api/data/itemoverview?league=Metamorph&type=UniqueFlask",
			"https://poe.ninja/api/data/itemoverview?league=Metamorph&type=UniqueWeapon",
			"https://poe.ninja/api/data/itemoverview?league=Metamorph&type=UniqueJewel",
			"https://poe.ninja/api/data/itemoverview?league=Metamorph&type=SkillGem",
			"https://poe.ninja/api/data/currencyoverview?league=Metamorph&type=Fragment"
	};

	private static final Gson GSON = new Gson();
	private static final LoadingCache<String, ItemLookup> cache = CacheBuilder.newBuilder()
			.expireAfterWrite(30, TimeUnit.MINUTES)
			.build(new CacheLoader<String, ItemLookup>() {
				@Override
				public ItemLookup load(String key) throws Exception {
					repopulateCache();
					return cache.get(key);
				}
			});
	private static final PossibleDeal[] possibleDeals = null;

	static {
		//possibleDeals = GSON.fromJson(new InputStreamReader(Bootstrap.class.getClassLoader().getResourceAsStream("DealData.json")), PossibleDeal[].class);
	}

	public static ItemLookup getItem(String itemName) {
		try {
			return cache.get(itemName);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Deal> getDeals() {
		List<Deal> temp = new ArrayList<>();

		try {
			for (PossibleDeal deal : possibleDeals) {
				double totalCost = 0;
				for (Item regent : deal.getRegents()) {
					totalCost += cache.get(regent.getName()).getPrice() * regent.getQuantity();
				}

				final Item product = deal.getProduct();
				double sellbackPrice = cache.get(product.getName()).getPrice() * product.getQuantity();

				temp.add(new Deal(deal, totalCost, sellbackPrice));
			}
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return temp;
	}

	public static List<Deal> getDeals(int chaos) {
		return getDeals().stream().filter(p -> p.getCost() <= chaos).collect(Collectors.toList());
	}

	public static Deal getDeal(String productName) {
		return getDeals().stream().filter(p -> p.getProduct().getName().equals(productName)).findFirst().orElse(null);
	}

	private static void repopulateCache() throws IOException {
		for (String apiLink : API_LINKS) {
			final URLConnection urlConnection = new URL(apiLink).openConnection();
			urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

			final JsonElement parse = new JsonParser().parse(new BufferedReader(new InputStreamReader(urlConnection.getInputStream())));

			if (apiLink.contains("poe.watch")) {
				for (JsonElement jsonElement : parse.getAsJsonArray()) {
					final ItemLookup itemLookup = GSON.fromJson(jsonElement, ItemLookup.class);
					cache.put(itemLookup.getName(), itemLookup);
				}
				continue;
			}

			for (JsonElement line : parse.getAsJsonObject().getAsJsonArray("lines")) {
				final ItemLookup item = GSON.fromJson(line, ItemLookup.class);
				if (item.getName() == null) continue;

				String name;
				if (item.getName().equals("Tabula Rasa")) {
					name = item.getName();
				} else {
					name = item.getName() + (item.getLinks() == 6 ? " (6)" : "");
				}

				if (item.getGemLevel() > 1) {
					name += " (lvl " + item.getGemLevel() + ")";
				}
				if (item.isCorrupted()) {
					name += " (corrupted)";
				}
				cache.put(name, item);
			}
		}
	}

	public static LoadingCache<String, ItemLookup> getCache() {
		return cache;
	}
}
