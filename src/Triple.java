/**
 * 
 * A subclass of Hand Class and is used to model a hand of a Triple cards
 * 
 * @author Anushka
 *
 */
public class Triple extends Hand {
	
	private static final long serialVersionUID = 2777316522602839614L;

	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player the player that want to be assigned to this Hand.
	 * @param cards the cardList that want to be assigned to this Hand.
	 */
	public Triple(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * a method for checking if this is a valid tripple.
	 */
	public boolean isValid()
	{
		if(size()!=3) //check whether the size is three.
			return false;
		if(getCard(0).getRank()==getCard(1).getRank() && getCard(0).getRank()==getCard(2).getRank()) //check whether all the cards have the same rank
			return true;
		return false;
			
	}
	
	/**
	 * a method for returning a string specifying the type of this hand (Triple).
	 */
	public String getType()
	{
		return "Triple";
	}
}
