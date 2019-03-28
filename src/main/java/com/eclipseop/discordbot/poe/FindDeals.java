package com.eclipseop.discordbot.poe;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
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

	//public static final String FILE_PATH = "E:\\Dropbox\\programming\\BestDerEverWasyBot\\src\\main\\java\\com\\eclipseop\\discordbot\\util\\DealData.json";
	public static String FILE_PATH;

	//public final URL FILE_PATH = this.getClass().getClassLoader().getResource("DealData.json");
	private static final String[] API_LINKS = {
			"https://poe.ninja/api/data/currencyoverview?league=Synthesis&type=Currency",
			"https://poe.ninja/api/data/itemoverview?league=Synthesis&type=DivinationCard",
			"https://poe.ninja/api/data/itemoverview?league=Synthesis&type=UniqueArmour",
			"https://poe.ninja/api/data/itemoverview?league=Synthesis&type=UniqueAccessory",
			"https://poe.ninja/api/data/itemoverview?league=Synthesis&type=Prophecy",
			"https://poe.ninja/api/data/itemoverview?league=Synthesis&type=UniqueMap",
			"https://poe.ninja/api/data/itemoverview?league=Synthesis&type=UniqueFlask",
			"https://poe.ninja/api/data/itemoverview?league=Synthesis&type=UniqueWeapon",
			"https://poe.ninja/api/data/itemoverview?league=Synthesis&type=UniqueJewel",
			"https://poe.ninja/api/data/itemoverview?league=Synthesis&type=SkillGem",
			"https://poe.ninja/api/data/currencyoverview?league=Synthesis&type=Fragment"
	};

	private static final Gson GSON = new Gson();
	private static final LoadingCache<String, ItemLookup> cache = CacheBuilder.newBuilder()
			.expireAfterWrite(30, TimeUnit.MINUTES)
			.build(new CacheLoader<>() {
				@Override
				public ItemLookup load(String key) throws Exception {
					repopulateCache();
					return cache.get(key);
				}
			});
	private static PossibleDeal[] possibleDeals;

	static {
		try {
			FILE_PATH = new File("src/main/java/com/eclipseop/discordbot/util/DealData.json").getAbsolutePath();

			possibleDeals = GSON.fromJson(new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(FILE_PATH)))), PossibleDeal[].class);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
