package Game;

import java.util.*;

public class Board {
	
	private Field[] fields = new Field[40];

	public void createFields() {

		Start start = new Start(0, 4000, "Start");
		fields[0] = start;
		
		Street street1 = new Street(1, "Rødovrevej", null, 1, Group.LIGHTBLUE);
		fields[1] = street1;
		
		Chance chance1 = new Chance(2, "Prøv lykken");
		fields[2] = chance1;
		
		Street street2 = new Street(3, "Hvidovrevej", null, 1, Group.LIGHTBLUE);
		fields[3] = street2;
		
		Taxation taxation1 = new Taxation(4, "Betal inkomstskat", "10% eller Kr. 4.000");
		fields[4] = taxation1;

		Ferry ferries1 = new Ferry(5, "Scandlines", "Helsingør-helsinborg");
		fields[5] = ferries1;
		
		Street street3 = new Street(6, "Roskildevej", null, 2, Group.ORANGE);
		fields[6] = street3;
		
		Chance chance2 = new Chance(7, "Prøv lykken");
		fields[7] = chance2;
		
		Street street4 = new Street(8, "Valby Langgade", null, 2, Group.ORANGE);
		fields[8] = street4;
		Street street5 = new Street(9, "Allégade", null, 2, Group.ORANGE);
		fields[9] = street5;
		
		Jail jail1 = new Jail(10, "På besøg", "I fængsel");
		fields[10] = jail1;
		
		Street street6 = new Street(11, "Frederiksberg Allé", null, 3, Group.LIGHTGREEN);
		fields[11] = street6;
		
		Beverage beverages1 = new Beverage(12, "Squash");
		fields[12] = beverages1;
		
		Street street7 = new Street(13, "Bülowsvej", null, 3, Group.LIGHTGREEN);
		fields[13] = street7;
		Street street8 = new Street(14, "Gl. Kongevej", null, 3, Group.LIGHTGREEN);
		fields[14] = street8;

		Ferry ferries2 = new Ferry(15, "Scandlines", "Mols linjen");
		fields[15] = ferries2;
		
		Street street9 = new Street(16, "Bernstorffsvej", null, 4, Group.LIGHTGREY);
		fields[16] = street9;
		
		Chance chance3 = new Chance(17, "Prøv lykken");
		fields[17] = chance3;
		
		Street street10 = new Street(18, "Hellerupvej", null, 4, Group.LIGHTGREY);
		fields[18] = street10;
		Street street11 = new Street(19, "Strandvejen", null, 4, Group.LIGHTGREY);
		fields[19] = street11;	
		
		Parking parking = new Parking(20, "Parkering");
		fields[20] = parking;
		
		Street street12 = new Street(21, "Trianglen", null, 5, Group.RED);
		fields[21] = street12;
		
		Chance chance4 = new Chance(22, "Prøv lykken");
		fields[22] = chance4;
		
		Street street13 = new Street(23, "Østerbrogade", null, 5, Group.RED);
		fields[23] = street13;
		Street street14 = new Street(24, "Grønningen", null, 5, Group.RED);
		fields[24] = street14;

		Ferry ferries3 = new Ferry(25, "Scandlines", "Gedser-Rostock");
		fields[25] = ferries3;
		
		Street street15 = new Street(26, "Bredgade", null, 6, Group.WHITE);
		fields[26] = street15;
		Street street16 = new Street(27, "Kgs. Nytorv", null, 6, Group.WHITE);
		fields[27] = street16;
		
		Beverage beverages2 = new Beverage(28, "Coca Cola");
		fields[28] = beverages2;
		
		Street street17 = new Street(29, "Østergade", null, 6, Group.WHITE);
		fields[29] = street17;
		
		Jail jail2 = new Jail(30, "De fængsles", "Fængsel");
		fields[30] = jail2;
		
		Street street18 = new Street(31, "Amagertorv", null, 7, Group.YELLOW);
		fields[31] = street18;
		Street street19 = new Street(32, "Vimmelskaffet", null, 7, Group.YELLOW);
		fields[32] = street19;
		
		Chance chance5 = new Chance(33, "Prøv lykken");
		fields[33] = chance5;
		
		Street street20 = new Street(34, "Nygade", null, 7, Group.YELLOW);
		fields[34] = street20;

		Ferry ferries4 = new Ferry(35, "Scandlines", "Rødby Puttgarden");
		fields[35] = ferries4;
		
		Chance chance6 = new Chance(36, "Prøv lykken");
		fields[36] = chance6;
		
		Street street21 = new Street(37, "Frederiksberggade", null, 8, Group.PURPLE);
		fields[37] = street21;
		
		Taxation taxation2 = new Taxation(38, "Ekstraordinær statsskat", "Betal Kr. 2.000");
		fields[38] = taxation2;
		
		Street street22 = new Street(39, "Rådhuspladsen", null, 8, Group.PURPLE);
		fields[39] = street22;
	}

	public Field getField(int i) {
		return fields[i];
	}

	public Field[] getFields() {
		return fields;
	}

}