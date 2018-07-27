/**
 * 
 * The BigTwoCard class is a subclass of the Card class, 
 * and is used to model a card used in a Big Two card  game.
 * 
 * @author Anushka
 *
 */
public class BigTwoCard extends Card{

	private static final long serialVersionUID = 1425002729938411916L;
	
	/**
	 * A constructor for building a card with the specified suit and rank.
	 * 
	 * @param suit
	 *            an int value between 0 and 3 representing the suit of a card:
	 *            <p>
	 *            0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
	 * @param rank
	 *            an int value between 0 and 12 representing the rank of a card:
	 *            <p>
	 *            0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '0', 10 = 'J', 11
	 *            = 'Q', 12 = 'K'
	 */
	BigTwoCard(int suit, int rank)
	{
		super(suit,rank);
	}
	
	/**
	 *  A method for comparing this card with the specified card for order.
	 *  
	 * @param card
	 *         the card to be compared
	 * @return a negative integer, zero, or a positive integer as this card is
	 *         less than, equal to, or greater than the specified card
	 */
	public int compareTo(Card card)
	{
		int thisRank=this.rank, cardRank=card.rank;
		
		/*
		 * To make the order of ranks from high to low of this card to
		 *  	2, A, K, Q, J, 10, 9, 8, 7, 6, 5, 4, 3.
		 */
		if(this.rank==0 || this.rank==1)
		{
			thisRank+=13;
		}
		
		/*
		 * To make the order of ranks from high to low of the specified card to
		 *  	2, A, K, Q, J, 10, 9, 8, 7, 6, 5, 4, 3.
		 */
		if(card.rank==0 || card.rank==1)
		{
			cardRank+=13;
		}

		
		if (thisRank > cardRank) {
			return 1;
		} else if (thisRank < cardRank) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
}
