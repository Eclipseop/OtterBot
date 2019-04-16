package com.eclipseop.discordbot.poe;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eclipseop.
 * Date: 3/25/2019.
 */
public class ItemLookup {

	@SerializedName(value = "name", alternate = "currencyTypeName")
	private String name;
	@SerializedName(value = "chaosValue", alternate = "mode")
	private double price;
	private int links;
	private boolean corrupted;
	private int gemLevel;
	private Receive receive;
	private String icon;

	public double getPrice() {
		return price != 0 ? price : receive.value;
	}

	public String getName() {
		return name;
	}

	public int getLinks() {
		return links;
	}

	public boolean isCorrupted() {
		return corrupted;
	}

	public int getGemLevel() {
		return gemLevel;
	}

	private static class Receive {
		private double value;

		double getValue() {
			return value;
		}
	}

	public String getIcon() {
		return icon;
	}

	@Override
	public String toString() {
		return "ItemLookup{" +
				"name='" + name + '\'' +
				", price=" + price +
				", links=" + links +
				", corrupted=" + corrupted +
				", gemLevel=" + gemLevel +
				", receive=" + receive +
				'}';
	}
}
