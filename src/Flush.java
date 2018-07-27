/**
 * 
 * A subclass of Hand Class and is used to model a hand of a Flush cards
 * 
 * @author Anushka
 *
 */
public class Flush extends Hand {
	
	private static final long serialVersionUID = -7526200469561401561L;

	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player the player that want to be assigned to this Hand.
	 * @param cards the cardList that want to be assigned to this Hand.
	 */
	public Flush(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * a method for checking if this is a valid Flush.
	 */
	public boolean isValid()
	{
		if(size()!=5)	//check whether the size is 5
			return false;
		for(int i=0;i<4;i++)	//to check whether all the cards have the same suit
			if(getCard(i).getSuit()!=getCard(i+1).getSuit())	
				return false;
		return true;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 */
	public String getType()
	{
		return "Flush";
	}
	
	/**
	 *  a method for checking if this Flush beats a specified hand.
	 * @param hand
	 * 			a specific hand that wants to compared to
	 * @return
	 * 			a boolean value of if this hand beats a specific hand.
	 */
	public boolean beats(Hand hand)
	{
		if(isValid() && hand.isValid())
		{
			if(hand.getType().equals("Straight"))	//if the specific card type is straight, then flush win
				return true;
			if(getType().equals(hand.getType()))	//if it is the same type (i.e., Flush)
			{
				if(getTopCard().getSuit()>hand.getTopCard().getSuit())	//if this suit > specific flush suit
					return true;
				if(getTopCard().getSuit()<hand.getTopCard().getSuit())	//if this suit < specific flush suit
					return false;
				if(getTopCard().compareTo(hand.getTopCard())==1)	//if the suit is the same, compare the top card
					return true;
			}
		}
		return false; //otherwise, it loses
	}
}
