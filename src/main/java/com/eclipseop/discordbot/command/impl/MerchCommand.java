package com.eclipseop.discordbot.command.impl;

import com.eclipseop.discordbot.Bot;
import com.eclipseop.discordbot.command.Command;
import com.eclipseop.discordbot.poe.FindDeals;
import com.eclipseop.discordbot.poe.ItemLookup;
import com.eclipseop.discordbot.util.MessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

/**
 * Created by Eclipseop.
 * Date: 3/20/2019.
 */
public class MerchCommand extends Command {

	public MerchCommand(Bot bot) {
		super(bot);
	}

	@Override
	public String getPrefix() {
		return "p";
	}

	@Override
	public MessageEmbed getHelpText() {
		MessageBuilder commands = new MessageBuilder("Path of Exile Commands");
		commands.setColor(Color.GREEN);
		commands.addField("*#p price {item name}*: Returns the item price.");
		return commands.build();
	}

	@Override
	public void execute(Message trigger) {
		String command = trigger.getContentRaw().substring(3);
		EmbedBuilder message = new EmbedBuilder();
		message.setColor(Color.ORANGE);
		switch (command.split(" ")[0]) {
			/*
			case "deal":
				command = command.substring(5);

				if (command.matches("-?\\d+(\\.\\d+)?")) {
					message.setTitle("Deals");

					FindDeals.getDeals(Integer.parseInt(command.replace("deal ", ""))).stream()
							.filter(Deal::isProfitable)
							.sorted(Comparator.comparingDouble(Deal::getScore).reversed())
							.limit(5)
							.forEach(c -> {
								StringBuilder messageString = new StringBuilder("Made with ");

								for (Item regent : c.getRegents()) {
									messageString.append(regent.getName()).append("(").append(regent.getQuantity()).append(") ");
								}

								messageString.append(", Expected Profit of ").append(c.getSellback() - c.getCost());
								message.addField(c.getProduct().getName(), messageString.toString(), false);
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

			 */
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
