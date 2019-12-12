package com.eclipseop.discordbot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {

	private final String title;
	private final List<String> fields;
	private Color color;

	public MessageBuilder(String title) {
		this.title = title;
		color = Color.CYAN;
		fields = new ArrayList<>();
	}

	public MessageBuilder addField(String field) {
		fields.add(field);
		return this;
	}

	public MessageBuilder setColor(Color color) {
		this.color = color;
		return this;
	}

	public MessageEmbed build() {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setColor(color);
		String field = "";
		for (String s : fields) {
			field += "੦ " + s + "\n";
		}
		embedBuilder.addField(title, field, false);
		return embedBuilder.build();
	}
}
