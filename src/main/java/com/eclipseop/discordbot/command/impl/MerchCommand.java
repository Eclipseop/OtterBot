package com.eclipseop.discordbot.command.impl;

import com.eclipseop.discordbot.Bot;
import com.eclipseop.discordbot.command.Command;
import com.eclipseop.discordbot.poe.Deal;
import com.eclipseop.discordbot.poe.FindDeals;
import com.eclipseop.discordbot.poe.Item;
import com.eclipseop.discordbot.poe.ItemLookup;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Created by Eclipseop.
 * Date: 3/20/2019.
 */
public class MerchCommand extends Command {

	public MerchCommand(Bot bot) {
		super(bot);
	}

	@Override
	public Predicate<String> getPrefixArg() {
		return s -> s.equals("deal") || s.equals("price");
	}

	@Override
	public String getHelpText() {
		return "`#price {item name}`: Returns the item price.\n" +
				"`#deal {chaos}`: Returns all possible deals within X chaos.\n" +
				"`#deal {item name}`: Returns information regarding a specific deal. This is the item itself, not the div card.";
	}

	@Override
	public void execute(Message trigger) {
		String command = trigger.getContentRaw().substring(1);

		EmbedBuilder message = new EmbedBuilder();
		message.setColor(Color.ORANGE);
		switch (command.split(" ")[0]) {
			case "deal":
				command = command.substring(5);

				if (command.matches("-?\\d+(\\.\\d+)?")) {
					message.setTitle("Deals");

					FindDeals.getDeals(Integer.parseInt(command.replace("deal ", ""))).stream()
							.filter(Deal::isProfitable)
							.sorted(Comparator.comparingDouble(Deal::getScore).reversed())
							.limit(5)
							.forEach(c -> {
								String messageString = "Made with ";

								for (Item regent : c.getRegents()) {
									messageString += regent.getName() + "(" + regent.getQuantity() + ") ";
								}

								messageString += ", Expected Profit of " + (c.getSellback() - c.getCost());
								message.addField(c.getProduct().getName(), messageString, false);
							});

				} else {
					final Deal deal = FindDeals.getDeal(command);
					message.setTitle(deal.getProduct().getName());

					message.addField("Estimated Cost", deal.getCost() + "", false);
					message.addField("Expected Profit", (deal.getSellback() - deal.getCost()) + "", false);
					message.setThumbnail(FindDeals.getItem(deal.getProduct().getName()).getIcon());

					if (deal.getType().equals("div")) {
						message.addField("Max Divcard cost", deal.getMaxDivcardCost(deal.getRegents()[0].getQuantity()) + "", false);
					}
				}
				break;
			case "price":
				ItemLookup item = FindDeals.getItem(command.replace("price ", ""));

				if (item == null) return;
				message.setTitle(item.getName());
				message.setThumbnail(item.getIcon());
				message.addField("Value", item.getPrice() + "", true);
				break;

		}
		getBot().sendMessage(message.build(), trigger.getTextChannel());
	}
}
