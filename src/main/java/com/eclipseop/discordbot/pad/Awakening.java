package com.eclipseop.discordbot.pad;

public enum Awakening {
	SBR(28),
	CLOUD(54),
	DARK_ROW(26),
	RED_ROW(22),
	BLUE_ROW(23),
	LIGHT_ROW(25),
	DARK_PLUS(-1),
	RED_PLUS(14),
	BLUE_PLUS(15),
	LIGHT_PLUS(17),
	BLIND(11),
	POISON(13),
	JAMMER(12),
	EQUIP(49),
	L_ATTACK(60),
	ENH_HP(1),
	ENH_ATT(2),
	ENH_RCV(3),
	TEAM_HP(46),
	TEAM_RCV(47),
	HEALER_KILLER(38),
	BALANCE_KILLER(35),
	EVO_KILLER(39),
	VENDOR_KILLER(42),
	TPA(27),
	LESS_HP(58),
	MORE_HP(57),
	MOVETIME(19),
	SBA(50),
	AUTO_RCV(9),
	BIND_PLUS(52),
	HEAL_PLUS(29), //ench heal?
	;

	private int id;

	Awakening(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static Awakening get(int id) {
		for (Awakening value : Awakening.values()) {
			if (value.id == id) {
				return value;
			}
		}

		return null;
	}
}
