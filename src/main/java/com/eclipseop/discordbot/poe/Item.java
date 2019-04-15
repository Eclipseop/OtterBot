package com.eclipseop.discordbot.poe;

/**
 * Created by Eclipseop.
 * Date: 3/25/2019.
 */
public class Item {

	private final String name;
	private final int quantity;

	public Item(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Item{" +
				"name='" + name + '\'' +
				", quantity=" + quantity +
				'}';
	}
}
