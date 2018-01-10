package Game;

public class Chance extends Field {
	protected Card drawncard;
	private Card[] cardList;

	public Chance(int fieldNo, String name) {
		super(fieldNo, name);
	}

	public Card getCard() {
		int random = (int) (Math.random() * 26);
		return cardList[random];
	}

	public Card[] getCardList() {
		return cardList;
	}

	public void createCardList() {

		Card[] cardList = new Card[25];

		cardList[0] 	= new MoneyCard("Money", "Du har solgt dit gamle udstyr i garagen. Modtag kr. 500,-", 500);
		cardList[1] 	= new MoneyCard("Money", "Du har vasket din bil. Betal kr. 150,-", -150);
		cardList[2] 	= new MoneyCard("Money", "Du har været i udlandet og blev taget i tolden og skal betale en bøde. Betal kr. 1500,-", -1500);
		cardList[3] 	= new MoneyCard("Money", "Du har modtaget en parkeringsbøde. Betal kr. 800,-", -800);
		cardList[4] 	= new MoneyCard("Money", "Værdien af din egen avl fra nyttehaven udgør kr. 2000,-, som du modtager af banken.", 2000);
		cardList[5] 	= new MoneyCard("Money", "Modtag udbytte af Deres aktier. Modtag kr. 1500,-", 1500);
		cardList[6] 	= new MoneyCard("Money", "Du har anskaffet et nyt dæk til din bil. Betal kr. 2000,-", -2000);
		cardList[7] 	= new MoneyCard("Money", "Du har kørt frem for et Fuld Stop skilt. Betal 1000 i bøde", -1000);
		cardList[8]		= new MoneyCard("Money","Betal for vognvask og smøring. betal kr. 100,00",-100);
		cardList[9]		= new MoneyCard("Money","De har måttet vedtage en parkeringsbøde. Betal kr. 200,00 til banken",-200);
		cardList[10]	= new MoneyCard("Money","Grundet på dyrtiden har de fået gageforhøjelse. Modtag kr 250,00",250);
		cardList[11]	= new MoneyCard("Money","Manufakturvarerne er blevt billigere og bedre, herved spare du kr. 500 som du modtager af banken",500);
		cardList[12]	= new MoneyCard("Money","Efter auktionen på Assistenshuset, hvor de havde pantsat deres tøj, modtager de ekstra kr. 1200",1200);
		cardList[13]	= new MoneyCard("Money","Deres præmieobligation er kommet ud. De modtager kr.1000,00 af banken",1000);
		cardList[14]	= new MoneyCard("Money", "De vandt i lotto. Modtag 250 kr. af banken", 250);
		cardList[15]	= new MoveCard("Move", "Tag ind på Rådhuspladsen", 39);
		cardList[16]	= new MoveCard("Move", "Ryk frem til Grønningen. Hvis De passerer >>Start<<, indkasser da 4000 kr.", 24);
		cardList[17]	= new MoveCard("Move", "Tag med øresundsbåden --- Flyt brikken frem, og hvis De passerer >>Start<<, indkasser 4000 kr.", 5);
		cardList[18]	= new MoveCard("Move", "Gå i fængsel. Ryk direkte til fængslet. Selv om De passerer >>Start<<, indkasserer De ikke 4000 kr.", 10);
		cardList[18]	= new MoveCard("Move", "Gå i fængsel. Ryk direkte til fængslet. Selv om De passerer >>Start<<, indkasserer De ikke 4000 kr.", 10);
		cardList[18]	= new MoveCard("Move", "Gå i fængsel. Ryk direkte til fængslet. Selv om De passerer >>Start<<, indkasserer De ikke 4000 kr.", 10);
		cardList[19]	= new MoveCard("Move", "Ryk tre felter tilbage.", -3);
		cardList[20]	= new MoveCard("Move", "Ryk tre felter tilbage.", -3);
		cardList[21]	= new MoveCard("Move", "Ryk frem til >>Start<<.", 0);
		cardList[22]	= new JailCard("Move", "Fængselskort: Du kan bruge dette kort til at købe dig fri fra fængslet!");
		cardList[23]	= new JailCard("Move", "Fængselskort: Du kan bruge dette kort til at købe dig fri fra fængslet!");
		cardList[24]	= new JailCard("Move", "Fængselskort: Du kan bruge dette kort til at købe dig fri fra fængslet!");
		
		this.cardList = cardList;
	}

	@Override
	public void landOnField(Player player) {
		createCardList();
		drawncard = getCard();
		if(drawncard instanceof MoneyCard) {
			player.addPoints(((MoneyCard) drawncard).getAmount());
	
		} else if(drawncard instanceof MoveCard) {
			if (((MoveCard) drawncard).getField() < 0) {
				if (player.getFieldNo() < 3) {
					player.setFieldNo(player.getFieldNo() + 40 + ((MoveCard) drawncard).getField());
				} else {
					player.setFieldNo(player.getFieldNo() + ((MoveCard) drawncard).getField());
				}
			} else {
				player.setFieldNo(((MoveCard) drawncard).getField());
			}
			
		} else if(drawncard instanceof JailCard) {
			player.setJailCard(1);
		}
	}

	public String getCardDescription() {
		return drawncard.description;
	}

	public void setDrawncard(Card drawncard) {
		this.drawncard = drawncard;
	}
}
