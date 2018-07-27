/**
 * 
 * A subclass of Hand Class and is used to model a hand of a Single card
 * 
 * @author Anushka
 *
 */
public class Single extends Hand {
	
	private static final long serialVersionUID = -5586876227643135562L;

	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player the player that want to be assigned to this Hand.
	 * @param cards the cardList that want to be assigned to this Hand.
	 */
	public Single(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * a method for checking if this is a valid single.
	 */
	public boolean isValid()
	{
		if(size()==1) //check whether the size is 1 or not
			return true;
		return false;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand (Single). 
	 */
	public String getType()
	{
		return "Single";
	}
}
