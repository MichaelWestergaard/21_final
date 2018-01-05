package Game;

import Controller.GUI_Controller;

public class Chance extends Field {
	protected Card drawncard;
	private Card[] cardList;

	public Chance(int fieldNo, String name) {
		super(fieldNo, name, "Chancekort");
	}

	public Card getCard() {
		int random = (int) (Math.random() * (15) + (0));
		return cardList[random];
	}

	public Card[] getCardList() {
		return cardList;
	}

	public void createCardList() {

		Card[] cardList = new Card[31];

		cardList[0] 	= new MoneyCard("Money", "Du har solgt din gamle klude. Modtag 20,-", 20);
		cardList[1] 	= new MoneyCard("Money", "Du har vasket din bil. Betal 10,-", -10);
		cardList[2] 	= new MoneyCard("Money", "Du har været i udlandet og blev taget i tolden og skal betale en bøde. Betal 100,-", -100);
		cardList[3] 	= new MoneyCard("Money", "Du har modtaget en parkeringsbøde. Betal 10,-", -10);
		cardList[4] 	= new MoneyCard("Money", "Værdien af din egen avl fra nyttehaven udgør 200,-, som du modtager af banken.", 200);
		cardList[5] 	= new MoneyCard("Money", "Modtag udbytte af Deres aktier. Modtag 100,-", 100);
		cardList[6] 	= new MoneyCard("Money", "Du har anskaffet et nyt dæk til din bil. Betal 10,-", -10);
		cardList[7] 	= new MoneyCard("Money", "Du har kørt frem for et Fuld Stop skilt. Betal 100 i bøde", -100);
		//cardlist[]		= new MoneyCard("Money", "Kul- og kokspriserne er steget og de skal betale: kr. 250,00 pr. hus og 1250,00 pr. hotel",)
		//cardlist[]		= new MoneyCard("Money", "Ejendomsskattene er steget og ekstaudgifterne er: kr. 500,00 pr. hus og kr. 1250,00 pr hotel.",)
		cardlist[]		=new MoneyCard("Money","Betal for vognvask og smøring. betal kr. 100,00",-100)
		cardlist[]		=new MoneyCard("Money","De har måttet vedtage en parkeringsbøde. Betal kr. 200,00 til banken",-200)
		cardlist[]		=new MoneyCard("Money","Grundet på dyrtiden har de fået gageforhøjelse. Modtag kr 250,00",250)
		cardlist[]		=new MoneyCard("Money","Manufakturvarerne er blevt billigere og bedre, herved spare du kr. 500 som du modtager af banken",500)
		cardlist[]		=new MoneyCard("Money","Efter auktionen på Assistenshuset, hvor de havde pantsat deres tøj, modtager de ekstra kr. 1200",1200)
		cardlist[]		=new MoneyCard("Money","Deres præmieobligation er kommet ud. De modtager kr.1000,00 af banken",1000)
		
		
		cardList[9] 	= new MoveCard("Move", "Du kørte for hurtigt, og derfor røg du i fængsel", 6); //Skift 19 til 18 når fængsel er implementeret
		cardList[10] 	= new MoveCard("Move", "Ryk til start", 0);
		cardList[11] 	= new MoveCard("Move", "Ryk til Boardwalk", 23);
		cardList[12] 	= new MoveCard("Move",	"Du var heldig med at finde en parkeringsplads. Ryk til gratis parkering", 12);
		cardList[13] 	= new MoveCard("Move", "Ryk til Tivoli", 7);
		cardList[14] 	= new MoveCard("Move", "Ryk til The Zoo", 20);
		//cardlist[]		= new MoveCard("Move","Ryk brikke frem til det nærmeste dampskibsselskab og betal ejeren to gange den leje, han ellers er berettiget til. Hvis selskabet ikke ejes af nogen, kan de købe det af banken")
		
		
		this.cardList = cardList;
	}

	@Override
	public void landOnField(Player player) {
		createCardList();
		drawncard = getCard();
		if(drawncard instanceof MoneyCard) {
			player.addPoints(((MoneyCard) drawncard).getAmount());
	
		} else if(drawncard instanceof MoveCard) {
			player.setFieldNo(((MoveCard) drawncard).getField());
			if(((MoveCard) drawncard).getField() == 6) {
				player.setJailed(true);
			}			
		}
	}

	public String getCardDescription() {
		return drawncard.description;
	}

	public void setDrawncard(Card drawncard) {
		this.drawncard = drawncard;
	}
}
