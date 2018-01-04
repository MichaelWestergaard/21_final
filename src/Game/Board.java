package Game;

import java.util.*;

public class Board {
	
	private Field[] fields = new Field[40];

	public void createFields() {

		Start start = new Start(0, 4000, "Start");
		fields[0] = start;
		Street street1 = new Street(1, "Burger Joint", null, 1, Group.BROWN);
		fields[1] = street1;
		Street street2 = new Street(2, "Pizza House", null, 1, Group.BROWN);
		fields[2] = street2;

		Chance chance1 = new Chance(3, "Træk et Chancekort");
		fields[3] = chance1;

		Street street3 = new Street(4, "Candy Shop", null, 1, Group.LIGHTBLUE);
		fields[4] = street3;
		Street street4 = new Street(5, "Ice Cream Palace", null, 1, Group.LIGHTBLUE);
		fields[5] = street4;

		Jail jail1 = new Jail(6, "Fængsel", "Jail");
		fields[6] = jail1;

		Street street5 = new Street(7, "Tivoli", null, 2, Group.PURPLE);
		fields[7] = street5;
		Street street6 = new Street(8, "Carnival", null, 2, Group.PURPLE);
		fields[8] = street6;

		Chance chance3 = new Chance(9, "Træk et Chancekort");
		fields[9] = chance3;

		Street street7 = new Street(10, "Waterpark", null, 2, Group.ORANGE);
		fields[10] = street7;
		Street street8 = new Street(11, "Skaterpark", null, 2, Group.ORANGE);
		fields[11] = street8;

		Parking parking = new Parking(12, "Gratis parkering");
		fields[12] = parking;

		Street street9 = new Street(13, "Lazer Tag Arena", null, 3, Group.RED);
		fields[13] = street9;
		Street street10 = new Street(14, "Game Arcade", null, 3, Group.RED);
		fields[14] = street10;

		Chance chance5 = new Chance(15, "Træk et Chancekort");
		fields[15] = chance5;

		Street street11 = new Street(16, "Toy store", null, 3, Group.YELLOW);
		fields[16] = street11;
		Street street12 = new Street(17, "Pet store", null, 3, Group.YELLOW);
		fields[17] = street12;

		// Skal ændres til et gå i fængsel felt
		Jail jail2 = new Jail(18, "Gå i Fængsel", "Jail");
		fields[18] = jail2;

		Street street13 = new Street(19, "Bowling Alley", null, 4, Group.GREEN);
		fields[19] = street13;
		Street street14 = new Street(20, "The Zoo", null, 4, Group.GREEN);
		fields[20] = street14;

		Chance chance6 = new Chance(21, "Træk et Chancekort");
		fields[21] = chance6;

		Street street15 = new Street(22, "Park Place", null, 5, Group.DARKBLUE);
		fields[22] = street15;
		Street street16 = new Street(23, "Boardwalk", null, 5, Group.DARKBLUE);
		fields[23] = street16;
		
		Parking parking = new Parking(21, "Parkering");
		fields[21] = parking;
		
		
		
		
		
		Street street12 = new Street(22, "Trianglen", null, 5, Group.RED);
		fields[22] = street12;
		
		Chance chance4 = new Chance(23, "Prøv lykken");
		fields[23] = chance4;
		
		Street street13 = new Street(24, "Østerbrogade", null, 5, Group.RED);
		fields[24] = street13;
		Street street14 = new Street(25, "Grønningen", null, 5, Group.RED);
		fields[25] = street14;
		
		Scandlines scandlines3 = new Scandlines(26, "Scandlines", "????");
		fields[26] = scandlines3;
		
		Street street15 = new Street(27, "Bredgade", null, 6, Group.WHITE);
		fields[27] = street15;
		Street street16 = new Street(28, "Kgs. Nytorv", null, 6, Group.WHITE);
		fields[28] = street16;
		
		Beverages Beverages2 = new Beverages(29, "Coca Cola");
		fields[29] = Beverages2;
		
		Street street17 = new Street(30, "Østergade", null, 6, Group.WHITE);
		fields[30] = street17;
		
		Jail jail2 = new Jail(31, "De fængsles", "Fængsel");
		fields[31] = jail2;
		
		Street street18 = new Street(32, "Amagertorv", null, 7, Group.YELLOW);
		fields[32] = street18;
		Street street19 = new Street(33, "Vimmelskaffet", null, 7, Group.YELLOW);
		fields[33] = street19;
		
		Chance chance5 = new Chance(34, "Prøv lykken");
		fields[34] = chance5;
		
		Street street20 = new Street(35, "Nygade", null, 7, Group.YELLOW);
		fields[35] = street20;
		
		Scandlines scandlines4 = new Scandlines(36, "Scandlines", "????");
		fields[36] = scandlines4;
		
		Chance chance6 = new Chance(37, "Prøv lykken");
		fields[37] = chance6;
		
		Street street21 = new Street(38, "Frederiksberggade", null, 8, Group.PURPLE);
		fields[38] = street21;
		
		Beskatning beskatning1 = new Beskatning(39, "Ekstraordinær statsskat", "Betal Kr. 2.000");
		fields[39] = beskatning1;
		
		Street street22 = new Street(40, "Rådhuspladsen", null, 8, Group.PURPLE);
		fields[40] = street22;
	}

	public Field getField(int i) {
		return fields[i];
	}

	public Field[] getFields() {
		return fields;
	}

}