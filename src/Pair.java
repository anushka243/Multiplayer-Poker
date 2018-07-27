/**
 * 
 * A subclass of Hand Class and is used to model a hand of a Pair cards
 * 
 * @author Anushka
 *
 */
public class Pair extends Hand {
	
	private static final long serialVersionUID = 6734656789664472980L;

	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player the player that want to be assigned to this Hand.
	 * @param cards the cardList that want to be assigned to this Hand.
	 */
	public Pair(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * a method for checking if this is a valid pair.
	 */
	public boolean isValid()
	{
		if(size()!=2)	//check whether the size is two.
			return false;
		if(getCard(0).getRank()==getCard(1).getRank()) //check whether all the cards have the same rank
			return true;
		return false;
			
	}
	
	/**
	 * a method for returning a string specifying the type of this hand (Pair).
	 */
	public String getType()
	{
			return "Pair";		
	}
}
