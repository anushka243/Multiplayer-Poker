/**
 * 
 * A subclass of Hand Class and is used to model a hand of a StraightFlush cards
 * 
 * @author Anushka
 *
 */
public class StraightFlush extends Hand {

	private static final long serialVersionUID = 7124986224640665043L;

	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player the player that want to be assigned to this Hand.
	 * @param cards the cardList that want to be assigned to this Hand.
	 */
	public StraightFlush(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * a method for checking if this is a valid StraightFlush.
	 */
	public boolean isValid()
	{
		if(size()!=5)	//checking whether the size is five or not
			return false;
		
		//checking whether it's a Flush or not
		for(int i=0;i<4;i++)
			if(getCard(i).getSuit()!=getCard(i+1).getSuit())
				return false;
		
		//checking whether it's a Straight or not
		boolean temp[]=new boolean[15];
		for(int i=0;i<15;i++)
			temp[i]=false;
		for(int i=0;i<5;i++)
			temp[getCard(i).getRank()]=true;
		for(int i=0;i<2;i++)
			temp[i+13]=temp[i];
		for(int i=2;i<11;i++)
		{
			boolean isStraight=true;
			for(int j=i;j<i+5 && isStraight;j++)
			{
				if(temp[j]==false)
					isStraight=false;
			}
			//if it is straight, then it is straight flush
			if(isStraight)
				return true;
		}
		
		return false;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 */
	public String getType()
	{
		return "StraightFlush";
	}
	
	
	/**
	 *  a method for checking if this StraightFlush beats a specified hand.
	 * @param hand
	 * 			a specific hand that wants to compared to
	 * @return
	 * 			a boolean value of if this hand beats a specific hand.
	 */
	
	public boolean beats(Hand hand)
	{
		if(isValid() && hand.isValid())
		{
			if(hand.getType().equals("Straight"))	// if the specific hand is Straight, then StraightFlush wins
				return true;
			if(hand.getType().equals("Flush"))		// if the specific hand is Flush, then StraightFlush wins
				return true;
			if(hand.getType().equals("FullHouse")) 	// if the specific hand is FullHouse, then StraightFlush wins
				return true;
			if(hand.getType().equals("Quad"))		// if the specific hand is Quad, then StraightFlush wins
				return true;
			if(getType().equals(hand.getType()))	// if the specific hand is also Straight flush, then check the top cards.
				if(getTopCard().compareTo(hand.getTopCard())==1)
					return true;
		}
		return false;
	}
}
