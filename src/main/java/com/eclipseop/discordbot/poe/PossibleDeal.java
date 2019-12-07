package com.eclipseop.discordbot.poe;

import java.util.Arrays;

/**
 * Created by Eclipseop.
 * Date: 3/25/2019.
 */
public class PossibleDeal {

	private String type;
	private Item[] regents;
	private Item product;

	public Item getProduct() {
		return product;
	}

	public Item[] getRegents() {
		return regents;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Deal{" +
				"type='" + type + '\'' +
				", regents=" + Arrays.toString(regents) +
				", product=" + product +
				'}';
	}
}
