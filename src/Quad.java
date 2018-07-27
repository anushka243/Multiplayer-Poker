/**
 * 
 * A subclass of Hand Class and is used to model a hand of a Quad cards
 * 
 * @author Anushka
 *
 */
public class Quad extends Hand {
	
	private static final long serialVersionUID = 3132648670197405517L;

	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player the player that want to be assigned to this Hand.
	 * @param cards the cardList that want to be assigned to this Hand.
	 */
	public Quad(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * a method for checking if this is a valid Quad.
	 */
	public boolean isValid()
	{
		if(size()!=5)	//check whether the size is five
			return false;
		int temp[]=new int[13];	//an array whose index indicating the rank and the value indicating the number of cards
		for(int i=0;i<13;i++)	//set every rank count=0
			temp[i]=0;
		for(int i=0;i<5;i++)	//count how many numbers card for every rank
			temp[getCard(i).getRank()]++;
		for(int i=0;i<13;i++)	//checking whether there is a rank with count=4
			if(temp[i]==4)
				return true;
		return false;
			
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 */
	public String getType()
	{
		return "Quad";
	}
	
	/**
	 * A method for retrieving the top card of the quadruplet. 
	 * 
	 * @return the rank of the tp[ card of quadruplet of this hand.
	 */
	public Card getTopCard()
	{
		if(isValid())
		{
			int temp[]=new int[13];	//an array whose index indicating the rank and the value indicating the number of cards
			int index=0;	// an integer indicating the rank which count=4
			
			for(int i=0;i<13;i++)	//set every rank count=0
				temp[i]=0;
			
			for(int i=0;i<5;i++)	//count how many numbers card for every rank
				temp[getCard(i).getRank()]++;
			
			for(int i=0;i<13;i++)	//find which rank has count=4
				if(temp[i]==4)
					index=i;
			
			for(int i=0;i<5;i++)	//get the top of quadruplet, that is, the spade.
				if(index==getCard(i).getRank() && getCard(i).getSuit()==3)
					return getCard(i);
		}
		return null;
	}
	
	/**
	 *  a method for checking if this Quad beats a specified hand.
	 * @param hand
	 * 			a specific hand that wants to compared to
	 * @return
	 * 			a boolean value of if this hand beats a specific hand.
	 */
	
	public boolean beats(Hand hand)
	{
		if(isValid() && hand.isValid())
		{
			if(hand.getType().equals("Straight"))	// if the specific hand is Straight, then Quad wins
				return true;
			if(hand.getType().equals("Flush"))		// if the specific hand is Flush, then Quad wins
				return true;
			if(hand.getType().equals("FullHouse"))	// if the specific hand is FullHouse, then Quad wins
				return true;
			if(getType().equals(hand.getType()))	// if the specific hand is Quad, compare the top card
				if(getTopCard().compareTo(hand.getTopCard())==1)
					return true;
		}
		//otherwise, Quad loses
		return false;
	}
}