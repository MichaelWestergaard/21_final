package entities;

public class Chance extends Field {
	
	protected Card drawncard;
	private Card[] cardList;

	public Chance(int fieldNo, String name) {
		super(fieldNo, name);
	}

	public Card getCard() {
		int random = (int) (Math.random() * 25);
		setDrawncard(cardList[random]);
		return cardList[random];
	}

	public Card[] getCardList() {
		return cardList;
	}

	public void createCardList() {

		Card[] cardList = new Card[25];

		cardList[0] 	= new MoneyCard("Money", "Du har solgt dit gamle udstyr i garagen. Modtag kr. 500,-", 500);
		cardList[1] 	= new MoneyCard("Money", "Du har vasket din bil. Betal kr. 150,-", -150);
		cardList[2] 	= new MoneyCard("Money", "Du har v�ret i udlandet og blev taget i tolden og skal betale en b�de. Betal kr. 1500,-", -1500);
		cardList[3] 	= new MoneyCard("Money", "Du har modtaget en parkeringsb�de. Betal kr. 800,-", -800);
		cardList[4] 	= new MoneyCard("Money", "V�rdien af din egen avl fra nyttehaven udg�r kr. 2000,-, som du modtager af banken.", 2000);
		cardList[5] 	= new MoneyCard("Money", "Modtag udbytte af Deres aktier. Modtag kr. 1500,-", 1500);
		cardList[6] 	= new MoneyCard("Money", "Du har anskaffet et nyt d�k til din bil. Betal kr. 2000,-", -2000);
		cardList[7] 	= new MoneyCard("Money", "Du har k�rt frem for et Fuld Stop skilt. Betal 1000 i b�de", -1000);
		cardList[8]		= new MoneyCard("Money","Betal for vognvask og sm�ring. betal kr. 100,00",-100);
		cardList[9]		= new MoneyCard("Money","De har m�ttet vedtage en parkeringsb�de. Betal kr. 200,00 til banken",-200);
		cardList[10]	= new MoneyCard("Money","Grundet p� dyrtiden har de f�et gageforh�jelse. Modtag kr 250,00",250);
		cardList[11]	= new MoneyCard("Money","Manufakturvarerne er blevt billigere og bedre, herved spare du kr. 500 som du modtager af banken",500);
		cardList[12]	= new MoneyCard("Money","Efter auktionen p� Assistenshuset, hvor de havde pantsat deres t�j, modtager de ekstra kr. 1200",1200);
		cardList[13]	= new MoneyCard("Money","Deres pr�mieobligation er kommet ud. De modtager kr.1000,00 af banken",1000);
		cardList[14]	= new MoneyCard("Money", "De vandt i lotto. Modtag 250 kr. af banken", 250);
		cardList[15]	= new MoveCard("Move", "Tag ind p� R�dhuspladsen", 39);
		cardList[16]	= new MoveCard("Move", "Ryk frem til Gr�nningen. Hvis De passerer >>Start<<, indkasser da 4000 kr.", 24);
		cardList[17]	= new MoveCard("Move", "Tag med �resundsb�den --- Flyt brikken frem, og hvis De passerer >>Start<<, indkasser 4000 kr.", 5);
		cardList[18]	= new MoveCard("Move", "G� i f�ngsel. Ryk direkte til f�ngslet. Selv om De passerer >>Start<<, indkasserer De ikke 4000 kr.", 10);
		cardList[18]	= new MoveCard("Move", "G� i f�ngsel. Ryk direkte til f�ngslet. Selv om De passerer >>Start<<, indkasserer De ikke 4000 kr.", 10);
		cardList[18]	= new MoveCard("Move", "G� i f�ngsel. Ryk direkte til f�ngslet. Selv om De passerer >>Start<<, indkasserer De ikke 4000 kr.", 10);
		cardList[19]	= new MoveCard("Move", "Ryk tre felter tilbage.", -3);
		cardList[20]	= new MoveCard("Move", "Ryk tre felter tilbage.", -3);
		cardList[21]	= new MoveCard("Move", "Ryk frem til >>Start<<.", 0);
		cardList[22]	= new JailCard("Move", "F�ngselskort: Du kan bruge dette kort til at k�be dig fri fra f�ngslet!");
		cardList[23]	= new JailCard("Move", "F�ngselskort: Du kan bruge dette kort til at k�be dig fri fra f�ngslet!");
		cardList[24]	= new JailCard("Move", "F�ngselskort: Du kan bruge dette kort til at k�be dig fri fra f�ngslet!");
		
		this.cardList = cardList;
	}

	public String getCardDescription() {
		return drawncard.description;
	}

	public void setDrawncard(Card drawncard) {
		this.drawncard = drawncard;
	}

}