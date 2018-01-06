package Game;

import java.util.*;

public class Board {
	
	private Field[] fields = new Field[40];

	public void createFields() {

		
		Start start = new Start(0, 4000, "Start");
		fields[0] = start;
		
		Street[] streets = new Street[] {
			new Street(1, "Rødovrevej", null, Group.LIGHTBLUE, 1200, new int[] {50, 250, 750, 2250, 4000, 6000}, 1000),
			new Street(3, "Hvidovrevej", null, Group.LIGHTBLUE, 1200, new int[] {50, 250, 750, 2250, 4000, 6000}, 1000),
			new Street(6, "Roskildevej", null, Group.ORANGE, 2000, new int[] {100, 600, 1800, 5400, 8000, 11000}, 1000),
			new Street(8, "Valby Langgade", null, Group.ORANGE, 2000, new int[] {100, 600, 1800, 5400, 8000, 11000}, 1000),
			new Street(9, "Allégade", null, Group.ORANGE, 2400, new int[] {150, 800, 2000, 6000, 9000, 12000}, 1000),
			new Street(11, "Frederiksberg Allé", null, Group.LIGHTGREEN, 2800, new int[] {200, 1000, 3000, 9000, 12500, 15000}, 2000),
			new Street(11, "Frederiksberg Allé", null, Group.LIGHTGREEN, 2800, new int[] {200, 1000, 3000, 9000, 12500, 15000}, 2000),
			new Street(13, "Bülowsvej", null, Group.LIGHTGREEN, 2800, new int[] {200, 1000, 3000, 9000, 12500, 15000}, 2000),
			new Street(14, "Gl. Kongevej", null, Group.LIGHTGREEN, 3200, new int[] {250, 1250, 3750, 10000, 14000, 18000}, 2000),
			new Street(16, "Bernstorffsvej", null, Group.LIGHTGREY, 3600, new int[] {300, 1400, 4000, 11000, 15000, 19000}, 2000),
			new Street(18, "Hellerupvej", null, Group.LIGHTGREY, 3600, new int[] {300, 1400, 4000, 11000, 15000, 19000}, 2000),
			new Street(19, "Strandvejen", null, Group.LIGHTGREY, 4000, new int[] {350, 1600, 4400, 12000, 16000, 20000}, 2000),
			new Street(21, "Trianglen", null, Group.RED, 4400, new int[] {350, 1800, 5000, 14000, 17500, 21000}, 3000),
			new Street(23, "Østerbrogade", null, Group.RED, 4400, new int[] {350, 1800, 5000, 14000, 17500, 21000}, 3000),
			new Street(24, "Grønningen", null, Group.RED, 4800, new int[] {400, 2000, 6000, 15000, 18500, 22000}, 3000),
			new Street(26, "Bredgade", null, Group.WHITE, 5200, new int[] {450, 2200, 6600, 16000, 19500, 23000}, 3000),
			new Street(27, "Kgs. Nytorv", null, Group.WHITE, 5200, new int[] {450, 2200, 6600, 16000, 19500, 23000}, 3000),
			new Street(29, "Østergade", null, Group.WHITE, 5600, new int[] {500, 2400, 7200, 17000, 20500, 24000}, 3000),
			new Street(31, "Amagertorv", null, Group.YELLOW, 6000, new int[] {550, 2600, 7800, 18000, 22000, 25000}, 4000),
			new Street(32, "Vimmelskaffet", null, Group.YELLOW, 6000, new int[] {550, 2600, 7800, 18000, 22000, 25000}, 4000),
			new Street(34, "Nygade", null, Group.YELLOW, 6400, new int[] {600, 3000, 9000, 20000, 24000, 28000}, 4000),
			new Street(37, "Frederiksberggade", null, Group.PURPLE, 7000, new int[] {800, 3500, 10000, 22000, 26000, 30000}, 4000),
			new Street(39, "Rådhuspladsen", null, Group.PURPLE, 8000, new int[] {1000, 4000, 12000, 28000, 34000, 40000}, 4000)
		};
		
		//Sætter alle ejendomme ind i fields[]
		for (int i = 0; i < streets.length; i++) {
			fields[streets[i].getFieldNo()] = streets[i];
		}
		
		Chance[] chances = new Chance[] {
			new Chance(2, "Prøv lykken"),
			new Chance(7, "Prøv lykken"),
			new Chance(17, "Prøv lykken"),
			new Chance(22, "Prøv lykken"),
			new Chance(33, "Prøv lykken"),
			new Chance(36, "Prøv lykken")
		};
		
		//Sætter alle prøv lykken felter ind i fields[]
		for (int i = 0; i < chances.length; i++) {
			fields[chances[i].getFieldNo()] = chances[i];
		}
		
		Ferry[] ferries = new Ferry[] {
			//new Ferry(fieldNo, name, null, Group.BLACK, 4000, 500)
			new Ferry(5, "Helsingør - helsinborg", null, Group.BLACK, 4000, 500),
			new Ferry(15, "Scandlines - Mols linjen", null, Group.BLACK, 4000, 500),
			new Ferry(25, "Scandlines - Gedser-Rostock", null, Group.BLACK, 4000, 500),
			new Ferry(35, "Scandlines - Rødby Puttgarden", null, Group.BLACK, 4000, 500)
		};
		
		//Sætter alle prøv lykken felter ind i fields[]
		for (int i = 0; i < ferries.length; i++) {
			fields[ferries[i].getFieldNo()] = ferries[i];
		}
		
		Taxation[] taxations = new Taxation[] {
			new IncomeTax(4, "Betal inkomstskat", 4000),
			new GovernmentTax(38, "Betal inkomstskat", 2000)
		};
		
		//Sætter alle prøv lykken felter ind i fields[]
		for (int i = 0; i < taxations.length; i++) {
			fields[taxations[i].getFieldNo()] = taxations[i];
		}
		
		Jail jail1 = new Jail(10, "På besøg", "I fængsel");
		fields[10] = jail1;
		Jail jail2 = new Jail(30, "De fængsles", "Fængsel");
		fields[30] = jail2;	
		
		Beverage beverage1 = new Beverage(12, "Squash", null, Group.BROWN, 3000, 100);
		Beverage beverage2 = new Beverage(28, "Coca Cola", null, Group.BROWN, 3000, 100);
		fields[12] = beverage1;
		fields[28] = beverage2;	
		
		Parking parking = new Parking(20, "Parkering");
		fields[20] = parking;	
		
	}

	public Field getField(int i) {
		return fields[i];
	}

	public Field[] getFields() {
		return fields;
	}

}