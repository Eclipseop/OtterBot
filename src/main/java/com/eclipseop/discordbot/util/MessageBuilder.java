package com.eclipseop.discordbot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {

	private String title;
	private final List<String> fields;
	private Color color;
	private String footer;

	public MessageBuilder(String title) {
		this.title = title;
		color = Color.CYAN;
		fields = new ArrayList<>();
	}

	public MessageBuilder() {
		this("");
	}

	public MessageBuilder addField(String field) {
		fields.add(field);
		return this;
	}

	public MessageBuilder setColor(Color color) {
		this.color = color;
		return this;
	}

	public MessageBuilder setFooter(String footer) {
		this.footer = footer;
		return this;
	}

	public MessageBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public MessageEmbed build() {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setColor(color);
		embedBuilder.setFooter(footer);
		String field = "";
		for (String s : fields) {
			field += "੦ " + s + "\n";
		}
		embedBuilder.addField(title, field, false);
		return embedBuilder.build();
	}
}
