/**
 * 
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards.
 * 
 * @author Anushka
 *
 */
public abstract class Hand extends CardList{

	private static final long serialVersionUID = 3708415018664975012L;
	private CardGamePlayer player=new CardGamePlayer(); // The player who plays this hand.
	
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player the player that want to be assigned to this Hand.
	 * @param cards the cardList that want to be assigned to this Hand.
	 */
	public Hand(CardGamePlayer player, CardList cards)
	{
		this.player.setName(player.getName());
		if(cards!=null)
			for(int i=0;i<cards.size();i++)
			{	
				Card card=cards.getCard(i);
				this.addCard(card);
			}
	}
	
	/**
	 * A method for retrieving the player of this hand.
	 * 
	 * @return the player of this hand.
	 */
	public CardGamePlayer getPlayer()
	{
		return player;
	}
	
	/**
	 * A method for retrieving the top card of this hand. 
	 * 
	 * @return the top card of this hand.
	 */
	public Card getTopCard()
	{
		if(size()>0)
		{
			Card card= getCard(0);
			for(int i=1;i<size();i++)
			{
				Card comparedFor = getCard(i);
				if(card.compareTo(comparedFor)==-1)
					card=comparedFor;
			}
			return card;
		}
		return null;
	}
	
	/**
	 * A method for checking if this hand beats a specified hand.
	 * @param hand
	 * 			a specific hand that wants to compared to
	 * @return
	 * 			a boolean value of if this hand beats a specific hand.
	 */
	public boolean beats(Hand hand)
	{
		if(isValid() && hand.isValid())
			if(getType().equals(hand.getType()))
				if(getTopCard().compareTo(hand.getTopCard())==1)
					return true;
		return false;
	}
	
	/**
	 * A method for checking if this is a valid hand.
	 * @return whether this is a valid hand.
	 */
	public abstract boolean isValid();
	
	/**
	 * A method for returning a string specifying the type of this hand.
	 * @return a string specifying the type of this hand.
	 */
	public abstract String getType();
	
}
