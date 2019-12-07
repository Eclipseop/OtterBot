package com.eclipseop.discordbot.poe;

import java.util.Arrays;

/**
 * Created by Eclipseop.
 * Date: 3/26/2019.
 */
public class Deal {

	private final Item[] regents;
	private final Item product;
	private final double cost;
	private final double sellback;
	private final String type;

	public Deal(Item[] regents, Item product, double cost, double sellback) {
		this.regents = regents;
		this.product = product;
		this.cost = cost;
		this.sellback = sellback;
		this.type = null;
	}

	public Deal(PossibleDeal possibleDeal, double cost, double sellback) {
		this.regents = possibleDeal.getRegents();
		this.product = possibleDeal.getProduct();
		this.cost = cost;
		this.sellback = sellback;
		this.type = possibleDeal.getType();
	}

	public Item[] getRegents() {
		return regents;
	}

	public Item getProduct() {
		return product;
	}

	public double getCost() {
		return cost;
	}

	public double getSellback() {
		return sellback;
	}

	public double getROI() {
		return ((sellback - cost) / cost) * 100;
	}

	public boolean isProfitable() {
		return sellback > cost;
	}

	public double getScore() {
		return getROI() * getCost();
	}

	public String getType() {
		return type;
	}

	public double getMaxDivcardCost(int stackSize) {
		double maxSpend = sellback / 1.1;
		return maxSpend / stackSize;
	}

	@Override
	public String toString() {
		return "Deal{" +
				"regents=" + Arrays.toString(regents) +
				", product=" + product +
				", cost=" + cost +
				", sellback=" + sellback +
				'}';
	}
}
