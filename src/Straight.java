/**
 * 
 * A subclass of Hand Class and is used to model a hand of a straight cards
 * 
 * @author Anushka
 *
 */
public class Straight extends Hand {
	
	private static final long serialVersionUID = 7667658350069608701L;

	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player the player that want to be assigned to this Hand.
	 * @param cards the cardList that want to be assigned to this Hand.
	 */
	public Straight(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 */
	public boolean isValid()
	{
		if(size()!=5)	//check whether the size is five or not
			return false;
		boolean temp[]=new boolean[15];
		for(int i=0;i<15;i++)				//initialize all the value to false
			temp[i]=false;
		for(int i=0;i<5;i++)
			temp[getCard(i).getRank()]=true;	//indicate which card are appeared
		for(int i=0;i<2;i++)
			temp[i+13]=temp[i];
		for(int i=2;i<11;i++)
		{
			boolean isStraight=true;		//to check whether this cards are consecutive
			for(int j=i;j<i+5 && isStraight;j++)
			{
				if(temp[j]==false)
					isStraight=false;
			}
			if(isStraight)					//if this hand pass the consecutive test, then it is straight
				return true;
		}
		
		//otherwise, it is not a straight
		return false;
			
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 */
	public String getType()
	{
		return "Straight";
	}

}
