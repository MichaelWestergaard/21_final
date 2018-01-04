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

		Card[] cardList = new Card[15];

		cardList[0] 	= new MoneyCard("Money", "Du har spist for meget slik. Betal 2,- til banken", -2);
		cardList[1] 	= new MoneyCard("Money", "Du vandt en dansekonkurrencer og modtog 3,- som præmiepenge", 3);
		cardList[2] 	= new MoneyCard("Money", "Du gik overfor rødt, og betalte en bøde på 5,-", -5);
		cardList[3] 	= new MoneyCard("Money", "Du vandt på et skrabelod og modtog 10,-", 10);
		cardList[4] 	= new MoneyCard("Money", "Du stoppede en bandit, og modtog 2,- for din gode gerning", 2);
		cardList[5] 	= new MoneyCard("Money", "Din bil løb tør for benzin og betalte 3,- for genopfyldning", -3);
		cardList[6] 	= new MoneyCard("Money", "Du vandt et bilrace og modtog 4,- som præmiepenge", 4);
		cardList[7] 	= new MoneyCard("Money", "Du blev sulten og stoppede for at handle. Betal 1,-", -1);
		cardList[8] 	= new MoneyCard("Money", "Du vandt en skøndhedskonkurrence og modtog 5,- som præmiepenge", 5);
		cardList[9] 	= new MoveCard("Move", "Du kørte for hurtigt, og derfor røg du i fængsel", 6); //Skift 19 til 18 når fængsel er implementeret
		cardList[10] 	= new MoveCard("Move", "Ryk til start", 0);
		cardList[11] 	= new MoveCard("Move", "Ryk til Boardwalk", 23);
		cardList[12] 	= new MoveCard("Move",	"Du var heldig med at finde en parkeringsplads. Ryk til gratis parkering", 12);
		cardList[13] 	= new MoveCard("Move", "Ryk til Tivoli", 7);
		cardList[14] 	= new MoveCard("Move", "Ryk til The Zoo", 20);

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
