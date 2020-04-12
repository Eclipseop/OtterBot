package com.eclipseop.discordbot.pad;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PadUtil {

	//https://f000.backblazeb2.com/file/ilmina/extract/api/download_card_data.json

	private static final int[] possible_assists = {
			5813, 4914, 5192, 5098, 4968, 4928, 5600, 5492, 5443, 4972, 5413, 5803, 5791, 5417, 5412, 4970, 4897, 4715, 4154, 5086
	};

	public static void main(String[] args) {

		List<Card> possible = Arrays.stream(possible_assists).mapToObj(Card::get).collect(Collectors.toList());
		System.out.println(possible.size());

		Random random = new Random();
		int max_score = 0;

		Card[] team = new Card[5];
		for (int i = 0; i < 100000; i++) {
			int total_sbr = 2;
			int total_posion = 0;
			int total_jammer = 0;
			int total_blind = 5;

			for (int j = 0; j < team.length; j++) {
				team[j] = possible.get(random.nextInt(possible.size()));
			}
			if (hasDup(team)) {
				continue;
			}
			total_sbr += count(team, Awakening.SBR);
			total_posion += count(team, Awakening.POISON);
			total_jammer += count(team, Awakening.JAMMER);
			total_blind += count(team, Awakening.BLIND);

			int score = 0;
			if (has(team, Awakening.CLOUD)) score += 1000;
			score += Math.min(total_sbr, 5) * 300;
			score += Math.min(total_jammer, 5) * 100;
			score += Math.min(total_blind, 5) * 200;
			score += Math.min(total_posion, 5) * 130;
			score += count(team, Awakening.DARK_ROW) * 50;
			score += count(team, Awakening.DARK_PLUS) * 30;
			score += count(team, Awakening.MOVETIME) * 30;

			if (score > max_score) {
				max_score = score;
				System.out.println("New max score of " + score + " with " + total_sbr + "/5 sbr, " + total_jammer + "/5 jammer, " + total_blind + "/5 blind, " + total_posion + "/5 poison with " + count(team, Awakening.DARK_ROW) + " rows!");
				System.out.println(Arrays.toString(team));
			}
		}


	}

	private static boolean hasDup(Card[] arr) {
		List<Card> cards = Arrays.asList(arr);

		for (Card card : cards) {
			if (cards.stream().filter(p -> p.equals(card)).count() > 1) {
				return true;
			}
		}

		return false;
	}

	private static int count(Card[] team, Awakening awakens) {
		int count = 0;
		for (Card card : team) {
			for (Awakening awakening : card.getAwakenings()) {
				if (awakening.equals(awakens)) {
					count++;
				}
			}
		}
		return count;
	}

	private static boolean has(Card[] team, Awakening awakening) {
		return count(team, awakening) >= 1;
	}
}
