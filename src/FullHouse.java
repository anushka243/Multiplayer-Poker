/**
 * 
 * A subclass of Hand Class and is used to model a hand of a FullHouse cards
 * 
 * @author Anushka
 *
 */
public class FullHouse extends Hand {
	
	private static final long serialVersionUID = -1925353212846462471L;

	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player the player that want to be assigned to this Hand.
	 * @param cards the cardList that want to be assigned to this Hand.
	 */
	public FullHouse(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * a method for checking if this is a valid FullHouse.
	 */
	public boolean isValid()
	{
		if(size()!=5)
			return false;
		int temp[]=new int[13];	//an array whose index indicating the rank and the value indicating the number of cards
		
		for(int i=0;i<13;i++)	//set every rank count=0
			temp[i]=0;
		
		for(int i=0;i<5;i++)	//count how many numbers card for every rank
			temp[getCard(i).getRank()]++;	
		
		for(int i=0;i<13;i++)	//checking whether there are (3,2) combiniation
			for(int j=0;j<13;j++)
				if(i!=j)
					if(temp[i]==3 && temp[j]==2)
						return true;
		return false;
	}
	
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 */
	public String getType()
	{
		return "FullHouse";
	}
	
	/**
	 * A method for retrieving the top card of the triplet. 
	 * 
	 * @return the top card of the triplet of this hand.
	 */
	public Card getTopCard()
	{
		if(isValid())
		{
			int temp[]=new int[13]; //an array whose index indicating the rank and the value indicating the number of cards
			int index=0;	// an index to indicate which rank has three cards
			
			for(int i=0;i<13;i++)	//set every rank count=0
				temp[i]=0;
			
			for(int i=0;i<5;i++)	//count how many numbers card for every rank
				temp[getCard(i).getRank()]++;	
			
			for(int i=0;i<13;i++)	//find which rank has 3 cards
				if(temp[i]==3)
					index=i;
			for(int i=0;i<5;i++)	//find which card has suit spade and rank index(the top card)
				if(index==getCard(i).getRank() && getCard(i).getSuit()==3)
					return getCard(i);
			
			for(int i=0;i<5;i++)	//otherwise, the top car has suit heart
				if(index==getCard(i).getRank() && getCard(i).getSuit()==2)
					return getCard(i);
		}
		return null;
	}
	
	/**
	 *  a method for checking if this hand beats a specified hand.
	 * @param hand
	 * 			a specific hand that wants to compared to
	 * @return
	 * 			a boolean value of if this hand beats a specific hand.
	 */
	
	public boolean beats(Hand hand)
	{
		if(isValid() && hand.isValid())
		{
			if(hand.getType().equals("Straight")) 	// if the specific hand is Straight, then FullHouse wins
				return true;
			if(hand.getType().equals("Flush"))		//if the specific hand is Flush, then FullHouse wins
				return true;
			if(getType().equals(hand.getType()))	//if both are fullhouse, compare the top card
				if(getTopCard().compareTo(hand.getTopCard())==1)
					return true;
		}
		return false; // otherwise, it loses
	}
}