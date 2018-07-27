import java.util.ArrayList;
import java.awt.Color;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * The BigTwoClient class is used to model a Big Two card game that supports 4 players playing over
 * the internet.
 * 
 * @author Anushka
 *
 */
public class BigTwoClient implements CardGame, NetworkGame{

	private int numOfPlayers;	// an integer specifying the number of players.
	private Deck deck; 			// A deck of cards
	private ArrayList<CardGamePlayer> playerList=new ArrayList<CardGamePlayer>();	// A list of players.
	private ArrayList<Hand> handsOnTable=new ArrayList<Hand>();						// A list of hands played on the table.
	private int playerID; //an integer specifying the playerID (i.e., index) of the local player
	private String playerName; // a string specifying the name of the local player.
	private String serverIP;	// a string specifying the IP address of the game server.
	private int serverPort;	// an integer specifying the TCP port of the game server
	private Socket sock;	// a socket connection to the game server.
	private ObjectOutputStream oos;	// an ObjectOutputStream for sending messages to the server
	private int currentIdx;				// An integer specifying the index of the current player.
	private BigTwoTable table;			// a Big Two table which builds the GUI for the game and handles all user actions.
	private boolean isRunning=false;	// a boolean specifying the status of the game
	/**
	 * A method for creating an instance of BigTwoClient.
	 * 
	 * @param args
	 *            not being used in this application.
	 */
	public static void main(String[] args) {
		BigTwoClient bigTwo = new BigTwoClient();			//making the BigTwoClient object
	}
	
	/**
	 * A method for returning a valid hand from the specified list of cards of the player. 
	 * Returns null is no valid hand can be composed from the specified list of cards.
	 * @param player
	 * 					the player which has the turn.
	 * @param cards
	 * 					the cards that the player want to move.
	 * @return the valid card from the given list of cards.
	 */
	
	public static Hand composeHand(CardGamePlayer player, CardList cards)
	{
		if(cards!=null)
		{
			Hand hand[]=new Hand[8];					//make an object of all hands.
			hand[0]=new StraightFlush(player,cards);	//straight flush must be checked first before straight and flush since a straight flush is a straight and a flush
			hand[1]=new Quad(player,cards);
			hand[2]=new FullHouse(player,cards);
			hand[3]=new Flush(player,cards);
			hand[4]=new Straight(player,cards); 
			hand[5]=new Triple(player,cards);
			hand[6]=new Pair(player,cards);
			hand[7]=new Single(player,cards);
			for(int i=0;i<8;i++)	//check which one is valid
				if(hand[i].isValid())
					return hand[i];
		}
		return null;
	}
	
	/**
	 * 
	 * A constructor for creating a BigTwoClient.
	 */
	public BigTwoClient()
	{
		//create 4 players and add them to the player list.
		for(int i=0;i<4;i++)
		{	
			CardGamePlayer player=new CardGamePlayer();
			player.setName("");
			playerList.add(player);
		}
		numOfPlayers=4;
		currentIdx=-1;
		
		
		//create a big two console object using the this as a argument.
		table = new BigTwoTable(this);
		table.disable();
		
		//set the name of the local player
		String name=(JOptionPane.showInputDialog("Your name:"));
		while(name==null || name.isEmpty())
			name=(JOptionPane.showInputDialog("Your name:"));
		setPlayerName(name);
		//make a connection to the game server
		makeConnection();
	}
	/**
	 * A method for retrieving the status of the game, whether it's running or not.
	 * 
	 * @return the status of the game
	 */
	public boolean getIsRunning()
	{
		return isRunning;
	}
	/**
	 * A method for retrieving the number of players
	 * 
	 * @return the number of players
	 */
	public int getNumOfPlayers()
	{
		return playerList.size();
	}
	
	/**
	 * A method for retrieving the deck of cards being used.
	 * 
	 * @return the deck of cards being used.
	 */
	public Deck getDeck()
	{
		return deck;
	}
	
	/**
	 * A method for retrieving the list of players.
	 * 
	 * @return the list of players.
	 */
	public ArrayList<CardGamePlayer> getPlayerList()
	{
		return playerList;	
	}
	
	/**
	 * A method for retrieving the list of hands played on the table.
	 * 
	 * @return the list of hands played on the table
	 */
	
	public ArrayList<Hand> getHandsOnTable()
	{
		return handsOnTable;
	}
	
	/**
	 * A method for retrieving the index of the current player.
	 * 
	 * @return the index of the current player
	 */
	public int getCurrentIdx()
	{
		return currentIdx;
	}
	
	/**
	 * A method to print the cards inside the list of that the player want to play to the text area.
	 *
	 *@param cards 
	 *				the list of cards that the player want to play
	 */
	public void print(CardList cards) {
			for (int i = 0; i < cards.size(); i++) {
				String string = "";
					string = string + "[" + cards.getCard(i) + "]";
				if (i % 13 != 0) {
					string = " " + string;
				}
				table.printMsg(string);
				if (i % 13 == 12 || i == cards.size() - 1) {
					table.printMsg("");
				}
			}
	}
	
	/**
	 * A method for starting the game with a (shuffled) deck of cards supplied as the argument.
	 * @param deck
	 * 				a (shuffled) deck of cards
	 */
	public void start(Deck deck)
	{
		//making the ArrayList<Hand> object
		handsOnTable=new ArrayList<Hand>();
		
		//make sure every player has no card;	
		for(int i=0;i<numOfPlayers;i++)
			playerList.get(i).removeAllCards();
		currentIdx=0;				
		this.deck=deck;				// put the shuffled deck of cards to the this object.
		
		while(!this.deck.isEmpty())
		{
			playerList.get(currentIdx).addCard(this.deck.getCard(0));	//add the card to the player currentIdx
			this.deck.removeCard(0);									//remove the card to the player
			currentIdx=(currentIdx+1)%4;								//update the currentIdx
		}
		
		for(int i=0;i<numOfPlayers;i++)
			playerList.get(i).sortCardsInHand();	
		
		//find which players has 3 diamond
		for(int i=0;i<numOfPlayers;i++)
			if(playerList.get(i).getCardsInHand().contains(new BigTwoCard(0,2)))
				currentIdx=i;
		
		//set the active player and update the textArea
		if(currentIdx==playerID)
		{
			table.enable();
			table.printMsg("Your turn:\n");
		}
		else
			table.printMsg(playerList.get(currentIdx).getName() + "'s turn:\n");
		setIsRunning(true);
		table.repaint();
	}
	
	
	/**
	 * Makes a move by the player.
	 * 
	 * @param playerID
	 *            the playerID of the player who makes the move
	 * @param cardIdx
	 *            the list of the indices of the cards selected by the player
	 */
	public void makeMove(int playerID, int[] cardIdx) {
		sendMessage(new CardGameMessage(CardGameMessage.MOVE,-1,cardIdx));
	}

	/**
	 * Checks the move made by the player.
	 * 
	 * @param playerID
	 *            the playerID of the player who makes the move
	 * @param cardIdx
	 *            the list of the indices of the cards selected by the player
	 */
	public void checkMove(int playerID, int[] cardIdx) {
		CardList tempCardList=playerList.get(currentIdx).play(cardIdx);				// a list of cards used to model the cards that the player wants to move
		Hand tempHand=composeHand(playerList.get(currentIdx),tempCardList);			// a hand used to model the hand that the player wants to move		
		playerList.get(currentIdx).sortCardsInHand();								//sorted the cards of the player currentIdx
		
		boolean isMoved=false; // a boolean value to indicating whether the player move is valid
		//if it is the fist move
		if(handsOnTable.isEmpty())	
		{
			//if the cardIdx=null, it means the player wants to pass
			if(cardIdx==null)
				table.printMsg("{pass} <=== Not a legal move!!!\n");
			//if the player inputs some cards but it is not permissible
			else if(tempHand==null || !tempCardList.contains(new BigTwoCard(0,2)))
			{
				if(this.playerID==currentIdx)
					print(tempCardList);
				table.printMsg(" <=== Not a legal move!!!\n");
			}
			//otherwise
			else
			{
				table.printMsg("{" + tempHand.getType() + "} ");
				print(tempCardList);
				table.printMsg("\n");
				handsOnTable.add(tempHand);
				playerList.get(currentIdx).removeCards(tempCardList);
				isMoved=true;
			}
		}
			
		//otherwise, it is not the first move
		else
		{
			//if this player is the one who played the last hand of cards on the table, this player cannot pass
			if(playerList.get(currentIdx).getName()==handsOnTable.get(handsOnTable.size()-1).getPlayer().getName())
			{
				if(cardIdx==null)
				{
					table.printMsg("{pass} <== Not a legal move!!!\n");	
				}
				else if(tempHand==null)
				{
					if(this.playerID==currentIdx)
						print(tempCardList);
					table.printMsg(" <=== Not a legal move!!!\n");
				}
				else
				{
					table.printMsg("{" + tempHand.getType() + "} ");
					print(tempCardList);
					table.printMsg("\n");
					handsOnTable.add(tempHand);
					playerList.get(currentIdx).removeCards(tempCardList);
					isMoved=true;
				}
			}
			//otherwise, this player is not the one who played the last hand of cards
			else
			{
				//this player might pass
				if(cardIdx==null)
				{
					table.printMsg("{pass}\n");
					isMoved=true;
				}
				else if(tempHand==null || !tempHand.beats(handsOnTable.get(handsOnTable.size()-1)))
				{
					if(this.playerID==currentIdx)
						print(tempCardList);
					table.printMsg(" <=== Not a legal move!!!\n");
				}
				else
				{
					table.printMsg("{" + tempHand.getType() + "} ");
					print(tempCardList);
					table.printMsg("\n");
					handsOnTable.add(tempHand);
					playerList.get(currentIdx).removeCards(tempCardList);
					isMoved=true;
				}
				
			}
		}
		
		//Check whether the last player who plays hand still has card or not.
		// If this player doesn't have any more card, this player is win. 
		if(endOfGame())
			{
				table.disable();
				table.repaint();
				setIsRunning(false);
				String s="";
				String k;
				if(currentIdx==this.playerID)
					k="You win!";
				else
					k="You lose!";
				s+="The Game is Finished\n\n";
				
				for(int i=0;i<numOfPlayers;i++)
				{
					if(i!=this.playerID)
					{
						s+=playerList.get(i).getName()+" ";
						if(i==currentIdx)
							s+="wins the game.\n";
						else
							s+="has " + playerList.get(i).getNumOfCards() + " card(s).\n";
					}
					else
					{
						s+="You ";
						if(i==currentIdx)
							s+="win the game.\n";
						else
							s+="have " + playerList.get(i).getNumOfCards() + " card(s).\n";
					}
					
				}
				
				JOptionPane.showMessageDialog(new JFrame(),
					    s,
					    k,
					    JOptionPane.PLAIN_MESSAGE);
				
				sendMessage(new CardGameMessage(CardGameMessage.READY,-1,null));

				
			}
		//otherwise, change the turn of the move
		else if(isMoved)
		{
			table.disable();
			currentIdx=(currentIdx+1)%4;
			if(currentIdx!=this.playerID)
			{
				table.printMsg("\n" + playerList.get(currentIdx).getName() + "'s turn:\n");
			}
			else
			{
				table.printMsg("\nYour turn:\n");
			}
		}
		table.repaint();
		
	}

	/**
	 * a method for checking if the game ends. 
	 * The game ends if the last player moved's card's number is zero.
	 */
	public boolean endOfGame()
	{
		return playerList.get(currentIdx).getNumOfCards()==0;
	}

	
	/**
	 * Returns the playerID (index) of the local player.
	 * 
	 * @return the playerID (index) of the local player
	 */
	public int getPlayerID() {
		return playerID;
	}
	
	/**
	 * A method to set the status of the game whether it's running or not
	 * 
	 * @param isRunning
	 * 					the status of the game
	 */
	public void setIsRunning(boolean isRunning)
	{
		this.isRunning=isRunning;
	}
	
	/**
	 * Sets the playerID (index) of the local player.
	 * 
	 * @param playerID
	 *            the playerID (index) of the local player.
	 */
	public void setPlayerID(int playerID) {
		this.playerID=playerID;
	}

	/**
	 * Returns the name of the local player.
	 * 
	 * @return the name of the local player
	 */
	public String getPlayerName() {
		return playerName;
	}
	
	/**
	 * Sets the name of the local player.
	 * 
	 * @param playerName
	 *            the name of the local player
	 */
	public void setPlayerName(String playerName) {
		this.playerName=playerName;
	}
	
	/**
	 * Returns the IP address of the server.
	 * 
	 * @return the IP address of the server
	 */
	public String getServerIP() {
		return serverIP;
	}

	/**
	 * Returns the IP address of the server.
	 * 
	 * @return the IP address of the server
	 */
	public void setServerIP(String serverIP) {
		this.serverIP=serverIP;
		
	}
	
	/**
	 * Returns the TCP port of the server.
	 * 
	 * @return the TCP port of the server
	 */
	public int getServerPort() {
		return serverPort;
	}
	
	/**
	 * Sets the TCP port of the server
	 * 
	 * @param serverPort
	 *            the TCP port of the server
	 */
	public void setServerPort(int serverPort) {
		this.serverPort=serverPort;
		
	}

	/**
	 * Makes a network connection to the server.
	 */
	public void makeConnection(){
		String ipAndPort=(JOptionPane.showInputDialog("ServerIP and ServerPort:(ServerIP:ServerPort) (e.g., 12.234.1:2396)","192.168.56.1:2396"));
		while(ipAndPort==null)
			ipAndPort=(JOptionPane.showInputDialog("ServerIP and ServerPort:(ServerIP:ServerPort)","192.168.56.1:2396"));
		if(!ipAndPort.isEmpty() && ipAndPort.contains(":") && ipAndPort.charAt(0)>='0' && ipAndPort.charAt(0)<='9' && ipAndPort.charAt(ipAndPort.length()-1)>='0' && ipAndPort.charAt(ipAndPort.length()-1)<='9')
		{
			try{
			setServerIP(ipAndPort.substring(0, ipAndPort.indexOf(':')));
			setServerPort(Integer.parseInt(ipAndPort.substring(ipAndPort.indexOf(':')+1, ipAndPort.length())));
			try {
				sock=new Socket(serverIP,serverPort);
				table.printMsg("Connected to a server at /" + serverIP +':' + serverPort+ "\n");
				try {
					oos= new ObjectOutputStream(sock.getOutputStream());
					ServerHandler threadJob= new ServerHandler();
					Thread myThread = new Thread(threadJob);
					myThread.start(); 
					sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, playerName));
					sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
				} catch (Exception e) {
					sock.close();
					table.printMsg("Cannot make a new thread\n\n");
					JOptionPane.showMessageDialog(new JFrame(),
							"Cannot make a new thread",
						    "Warning!",
						    JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
				}
				
			} catch (Exception ex) {
				table.printMsg("Cannot connect to the server. Try Again\n\n");
				JOptionPane.showMessageDialog(new JFrame(),
						"Cannot connect to the server. Try Again",
					    "Warning!",
					    JOptionPane.WARNING_MESSAGE);
				ex.printStackTrace();
			}
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(new JFrame(),
						"The Format of your ServerIP:ServerPort is wrong",
					    "Warning!",
					    JOptionPane.WARNING_MESSAGE);
				table.printMsg("The Format of your ServerIP:ServerPort is wrong\n\n");
				e.printStackTrace();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(new JFrame(),
					"The Format of your ServerIP:ServerPort is wrong",
				    "Warning!",
				    JOptionPane.WARNING_MESSAGE);
			table.printMsg("The Format of your ServerIP:ServerPort is wrong\n\n");
	
		}
	}

	/**
	 * Parses the specified message received from the server.
	 * 
	 * @param message
	 *            the specified message received from the server
	 */
	public void parseMessage(GameMessage message) {
		switch (message.getType()) {
		case CardGameMessage.PLAYER_LIST:
			setPlayerID(message.getPlayerID());
			table.setActivePlayer(playerID);
			for(int i=0;i<numOfPlayers;i++)
				if(((String[])message.getData())[i]!=null)
					playerList.get(i).setName(((String[])message.getData())[i]);
			table.repaint();
			break;
		case CardGameMessage.JOIN:
			playerList.get(message.getPlayerID()).setName((String)message.getData());
			table.repaint();
			break;
		case CardGameMessage.FULL:
			table.printMsg("The server is full and you cannot join the game\n");
			break;
		case CardGameMessage.QUIT:
			table.printMsg(playerList.get(getPlayerID()).getName()+' ' + message.getData() + "has left\n");
			playerList.get(message.getPlayerID()).setName("");
			//table.printMsg();
			if(getIsRunning())
			{
				setIsRunning(false);
				table.disable();
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			}
			table.repaint();
			break;
		case CardGameMessage.READY:
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " is ready\n");
			break;
		case CardGameMessage.START:
			table.printMsg("All players are ready. Game starts.\n"
					+ "--------------------------------------------------\n");
			start((BigTwoDeck)message.getData());
			break;
		case CardGameMessage.MOVE:
			checkMove(message.getPlayerID(),(int [])message.getData());
			if(isRunning)
				if(currentIdx==playerID)
					table.enable();
			break;
		case CardGameMessage.MSG:
			table.printChat((String) message.getData() + "\n");
			break;
		default:
			table.printMsg("Wrong message type: " + message.getType()+"\n");
			break;
		}
		
	}
	
	/**
	 * Sends the specified message to the server.
	 * 
	 * @param message
	 *            the specified message to be sent the server
	 */
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * An inner class that implements the Runnable interface.	
	 * 
	 * @author Budiman
	 *
	 */
	private class ServerHandler implements Runnable
	{
		private ObjectInputStream ois;
		
		public ServerHandler()
		{
			try {
				ois = new ObjectInputStream(sock.getInputStream());
			} catch (Exception ex) {
				table.printMsg("Error at making ObjectInputStream for the server\n\n");
				ex.printStackTrace();
			}
		}
		public void run() {
			table.disableConnect();
			CardGameMessage message;
			try {
				// waits for messages from the client
				while ((message = (CardGameMessage) ois.readObject()) != null) {
					parseMessage(message);
				}
			} catch (Exception ex) {
				try {
					table.enableConnect();
					table.printMsg("Lose connection to server.\n\n");
					JOptionPane.showMessageDialog(new JFrame(),
							"Lose connection to server.",
						    "Warning!",
						    JOptionPane.WARNING_MESSAGE);
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				table.printMsg("Error at receiving message from the server\n\n");
				JOptionPane.showMessageDialog(new JFrame(),
						"Error at receiving message from the server.",
					    "Warning!",
					    JOptionPane.WARNING_MESSAGE);
				ex.printStackTrace();

			}
		}
		
	}
}
