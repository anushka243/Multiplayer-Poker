import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.awt.Color;

/**
 * The BigTwoTable class implements the CardGameTable interface. 
 * It is used to build a GUI for the Big Two card game and handle all user actions.
 * @author Anushka
 *
 */

public class BigTwoTable implements CardGameTable{
	
	private BigTwoClient game;		//a card game associates with this table.
	private boolean[] selected=new boolean[13];	//a boolean array indicating which cards are being selected.
	private int activePlayer; 	//an integer specifying the index of the active player.
	private JFrame frame;		//the main window of the application.
	private JPanel bigTwoPanel;	//a panel for showing the cards of each player and the cards played on the table.
	private JButton playButton;	//a “Play” button for the active player to play the selected cards.
	private JButton passButton; //a “Pass” button for the active player to pass his/her turn to the next player.
	private JLabel msgLabel; 	//a msgLabel
	private JTextArea msgArea;	//a text area for showing the current game status as well as end of game messages.
	private JTextArea chatArea;	//a text area for showing the chat area of the table
	private JTextField outgoing; 	//for sending message
	private Image[][] cardImages=new Image[4][];	//a 2D array storing the images for the faces of the cards.
	private Image cardBackImage;	//an image for the backs of the cards.
	private Image[] avatars;		//an array storing the images for the avatars.
	private JMenuItem menuConnect; 	//a menu to connect to the server
	
	/**
	 * a constructor for creating a BigTwoTable. 
	 * @param game is a reference to a card game associates with this table.
	 */
	public BigTwoTable(BigTwoClient game)
	{
		//connect this bigTwoTable object with game object
		this.game=game;
		
		//initialize all the selected to false
		for(int i=0;i<13;i++)
			selected[i]=false;
		
		//seting the image of the back card
		cardBackImage=new ImageIcon("b.gif").getImage();
		
		//setting the player avatar
		Image[] av={new ImageIcon("batman_72.png").getImage(),new ImageIcon("flash_72.png").getImage(),
				new ImageIcon("wonder_woman_72.png").getImage(),new ImageIcon("superman_72.png").getImage()};
		avatars=av;
		
		//setting the front image of the cards
		Image[] r0={new ImageIcon("ad.gif").getImage(),
					new ImageIcon("2d.gif").getImage(),
					new ImageIcon("3d.gif").getImage(),
					new ImageIcon("4d.gif").getImage(),					
					new ImageIcon("5d.gif").getImage(),
					new ImageIcon("6d.gif").getImage(),			
					new ImageIcon("7d.gif").getImage(),
					new ImageIcon("8d.gif").getImage(),					
					new ImageIcon("9d.gif").getImage(),
					new ImageIcon("td.gif").getImage(),					
					new ImageIcon("jd.gif").getImage(),
					new ImageIcon("qd.gif").getImage(),					
					new ImageIcon("kd.gif").getImage()};
		
		Image[] r1={new ImageIcon("ac.gif").getImage(),
					new ImageIcon("2c.gif").getImage(),
					new ImageIcon("3c.gif").getImage(),
					new ImageIcon("4c.gif").getImage(),					
					new ImageIcon("5c.gif").getImage(),
					new ImageIcon("6c.gif").getImage(),			
					new ImageIcon("7c.gif").getImage(),
					new ImageIcon("8c.gif").getImage(),					
					new ImageIcon("9c.gif").getImage(),
					new ImageIcon("tc.gif").getImage(),					
					new ImageIcon("jc.gif").getImage(),
					new ImageIcon("qc.gif").getImage(),					
					new ImageIcon("kc.gif").getImage()};
	
		Image[] r2={new ImageIcon("ah.gif").getImage(),
					new ImageIcon("2h.gif").getImage(),
					new ImageIcon("3h.gif").getImage(),
					new ImageIcon("4h.gif").getImage(),					
					new ImageIcon("5h.gif").getImage(),
					new ImageIcon("6h.gif").getImage(),			
					new ImageIcon("7h.gif").getImage(),
					new ImageIcon("8h.gif").getImage(),					
					new ImageIcon("9h.gif").getImage(),
					new ImageIcon("th.gif").getImage(),					
					new ImageIcon("jh.gif").getImage(),
					new ImageIcon("qh.gif").getImage(),					
					new ImageIcon("kh.gif").getImage()};
			
		Image[] r3={new ImageIcon("as.gif").getImage(),
					new ImageIcon("2s.gif").getImage(),
					new ImageIcon("3s.gif").getImage(),
					new ImageIcon("4s.gif").getImage(),					
					new ImageIcon("5s.gif").getImage(),
					new ImageIcon("6s.gif").getImage(),			
					new ImageIcon("7s.gif").getImage(),
					new ImageIcon("8s.gif").getImage(),					
					new ImageIcon("9s.gif").getImage(),
					new ImageIcon("ts.gif").getImage(),					
					new ImageIcon("js.gif").getImage(),
					new ImageIcon("qs.gif").getImage(),					
					new ImageIcon("ks.gif").getImage()};
			
			cardImages[0]=r0;
			cardImages[1]=r1;
			cardImages[2]=r2;
			cardImages[3]=r3;
			

			//making a JFrame object
			frame=new JFrame();
			frame.setTitle("Big Two");
			frame.setMinimumSize(new Dimension(1200,720));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1200,720);
			
			//making the menu bar and all the components
			JMenuBar menuBar = new JMenuBar();
			JMenu menuGame = new JMenu("Game");
			JMenuItem menuQuit= new JMenuItem("Quit");
			menuConnect= new JMenuItem("Connect");	
			menuGame.add(menuQuit);
			menuGame.add(menuConnect);
			menuConnect.addActionListener(new ConnectMenuItemListener());
			menuQuit.addActionListener(new QuitMenuItemListener());
			
			JMenu menuMessage=new JMenu("Message");
			JMenuItem menuClearChat= new JMenuItem("Clear Chat");
			JMenuItem menuClearMessage= new JMenuItem("Clear Message");	
			menuMessage.add(menuClearChat);
			menuMessage.add(menuClearMessage);
			menuClearChat.addActionListener(new ClearChatArea());
			menuClearMessage.addActionListener(new ClearMsgArea());
			menuBar.add(menuGame);
			menuBar.add(menuMessage);
			
			

			//making the button frame and its components
			JPanel buttonPlaced=new JPanel();
			buttonPlaced.setBackground(Color.GRAY);
			playButton=new JButton("Play");
			playButton.addActionListener(new PlayButtonListener());
			passButton=new JButton("Pass");
			passButton.addActionListener(new PassButtonListener());
			buttonPlaced.add(playButton);
			buttonPlaced.add(passButton);
			
			//make a text area to show the current game status as well as end of game messages
			msgArea = new JTextArea(18,40);
			msgArea.setLineWrap(true);
			msgArea.setEditable(false);
			JScrollPane scroller = new JScrollPane(msgArea);
			scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			
			//make a text area to show the chat
			chatArea = new JTextArea(18,40);
			chatArea.setLineWrap(true);
			chatArea.setEditable(false);
			JScrollPane chatScroller = new JScrollPane(chatArea);
			chatScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			chatScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			
			
			
			//make a bigTwoPanel object
			bigTwoPanel=new BigTwoPanel();
			
			JPanel gamePosition= new JPanel();
			gamePosition.setLayout(new BorderLayout());
			gamePosition.add(buttonPlaced,BorderLayout.SOUTH);
			gamePosition.add(bigTwoPanel,BorderLayout.CENTER);

			
			JPanel msgPanel=new JPanel();
			msgPanel.setBackground(Color.WHITE);
			msgPanel.setLayout(new BorderLayout());
			outgoing=new JTextField(26);
			msgLabel=new JLabel("  Message:  ");
			msgPanel.add(msgLabel,BorderLayout.WEST);
			msgPanel.add(outgoing,BorderLayout.CENTER);

			
			JPanel textArea= new JPanel();
			textArea.setLayout(new BorderLayout());
			textArea.add(scroller,BorderLayout.NORTH);
			textArea.add(msgPanel,BorderLayout.SOUTH);
			textArea.add(chatScroller,BorderLayout.CENTER);
			
			outgoing.addKeyListener(new EnterKeyListener());

			
			//put all the sub-containers into the main frame
			frame.add(textArea,BorderLayout.EAST);
			frame.add(gamePosition,BorderLayout.CENTER);
			frame.setJMenuBar(menuBar);
			frame.setVisible(true);
			
			disable();
			
		
	}
	

	/**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer
	 *            an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer)
	{
		this.activePlayer=activePlayer;
	}

	/**
	 * Returns an array of indices of the cards selected.
	 * 
	 * @return an array of indices of the cards selected
	 */
	public int[] getSelected()
	{
		int[] cardIdx = null;
		int count = 0;
		for (int j = 0; j < selected.length; j++) {
			if (selected[j]) {
				count++;
			}
		}

		if (count != 0) {
			cardIdx = new int[count];
			count = 0;
			for (int j = 0; j < selected.length; j++) {
				if (selected[j]) {
					cardIdx[count] = j;
					count++;
				}
			}
		}
		return cardIdx;
	}


	/**
	 * Resets the list of selected cards to an empty list.
	 */
	public void resetSelected()
	{
		for(int i=0;i<13;i++)
			selected[i]=false;
	}

	/**
	 * Repaints the GUI.
	 */
	public void repaint()
	{
		frame.setTitle("Big Two (" + game.getPlayerName() + ")");
		frame.repaint();
	}

	/**
	 * Prints the specified string to the message area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the message area of the card game
	 *            table
	 */
	public void printMsg(String msg)
	{
		msgArea.append(msg);
	}
	
	/**
	 * Prints the specified string to the chat area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the chat area of the card game
	 *            table
	 */
	public void printChat(String msg)
	{
		chatArea.append(msg);
	}

	/**
	 * Clears the message area of the card game table.
	 */
	public void clearMsgArea()
	{
		msgArea.setText("");
	}
	
	/**
	 * Clears the chat area of the card game table.
	 */
	public void clearChatArea()
	{
		chatArea.setText("");
	}

	/**
	 * Resets the GUI.
	 */
	public void reset()
	{
		clearMsgArea();
		BigTwoDeck deck=new BigTwoDeck();
		deck.initialize();					//initializing the BigTwoDeck
		deck.shuffle();						//shuffling the BigTwoDeck
		game.start(deck);
	}
	
	/**
	 * to connect to the server
	 */
	public void connect()
	{
		game.makeConnection();
	}
	
	/**
	 * to enable the connect menu
	 */
	public void enableConnect()
	{
		menuConnect.setEnabled(true);
	}
	
	/**
	 * to disable the connect menu
	 */
	public void disableConnect()
	{
		menuConnect.setEnabled(false);
	}

	/**
	 * Enables user interactions.
	 */
	public void enable()
	{
		passButton.setEnabled(true);
		playButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}

	/**
	 * Disables user interactions.
	 */
	public void disable()
	{
		passButton.setEnabled(false);
		playButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}
	
	/**
	 * An inner class that extends the JPanel class and implements the MouseListener interface.
	 * Overrides the paintComponent() method inherited from the JPanel class to draw the card game table.
	 * Implements the mouseClicked() method from the MouseListener interface to handle mouse click events.
	 * @author Budiman
	 *
	 */
	public class BigTwoPanel extends JPanel implements MouseListener
	{
		
		
		private static final long serialVersionUID = -2679386016864585827L;
		
		/**
		 * A constructor of BigTwoPanel so that it add a mouseListener to the BigTwoPanel object.
		 */
		BigTwoPanel()
		{
			addMouseListener(this);
		}
		
		/**
		 * A method to paint the GUI
		 */
		public void paintComponent(Graphics g)
		{
			g.setFont(new Font("serif",Font.BOLD,14));  //set the font type
			g.setColor(new Color(180,200,200));			//set the color
			g.fillRect(0, 0, getWidth(), getHeight());	//to set the background
			g.setColor(Color.black);					//set the color to black
			
			//to make a line
			for(int i=1;i<5;i++)
			{
				g.fillRect(0, i*getHeight()/5, getWidth(), 1);
			}
			//to make a name of the player. If the player is the current player, the color of the font will be blue
			//and also to draw the image
			for(int i=0;i<4;i++)
			{
				if(!game.getPlayerList().get(i).getName().isEmpty())
				{	
					if(i==game.getCurrentIdx())
						g.setColor(Color.blue);
					else
						g.setColor(Color.black);
					if(i==activePlayer)
					{
						g.drawString("You", 20, 20+i*getHeight()/5);
						g.drawImage(avatars[i], 10, 30+i*getHeight()/5,this);
					}
					else
					{
						g.drawString(game.getPlayerList().get(i).getName(), 20, 20+i*getHeight()/5);
						g.drawImage(avatars[i], 10, 30+i*getHeight()/5,this);
					}
				}
					
			}
			
			//to draw the cards of the players
			if(game.getIsRunning())
			{
				for(int i=0;i<4;i++)
				{
					if(i!=activePlayer)
					{
						for(int j=0;j<game.getPlayerList().get(i).getNumOfCards();j++)
							g.drawImage(cardBackImage, 100+20*j,10+i*getHeight()/5,this);
					}
					else
					{
						if(game.getCurrentIdx()!=activePlayer)
						{
							for(int j=0;j<game.getPlayerList().get(i).getNumOfCards();j++)
							{
								Card card=game.getPlayerList().get(i).getCardsInHand().getCard(j);
								g.drawImage(cardImages[card.suit][card.rank], 100+20*j,10+i*getHeight()/5,this);
							}
						}
						else
						{
							for(int j=0;j<game.getPlayerList().get(i).getNumOfCards();j++)
							{
								Card card=game.getPlayerList().get(i).getCardsInHand().getCard(j);
								if(selected[j])
									g.drawImage(cardImages[card.suit][card.rank], 100+20*j,2+i*getHeight()/5,this);
								else
									g.drawImage(cardImages[card.suit][card.rank], 100+20*j,10+i*getHeight()/5,this);
							}
						}
					}
				}
			
			//to draw the most bottom panel to show the last played card and the last player played
			g.setColor(Color.black);
				if(game.getHandsOnTable().size()==0)
					g.drawString("First Move", 10, 20+4*getHeight()/5);
				else
				{
					g.drawString("Played by:", 10, 20+4*getHeight()/5);
					g.drawString(game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName(), 10, 40+4*getHeight()/5);
					Hand hand=game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
					for(int i=0;i<hand.size();i++)
					{
						Card card=hand.getCard(i);
						g.drawImage(cardImages[card.suit][card.rank], 100+20*i,10+4*getHeight()/5,this);
					}
				}
			}
			
			
		}
		
		/**
		 * a method which used to handle the mouse click event
		 */
		public void mouseClicked(MouseEvent e) {
			//image size is 97x73
			double xm = e.getX();	//the x-coordinate of the mouse click
			double ym = e.getY();	//the y-coordinate of the mouse click
			
			boolean isSelected=false; // to indicate that whether any card has selected
			int lastCard=game.getPlayerList().get(activePlayer).getNumOfCards()-1;
			
			//check whether the coordinate is inside the last card
			if( !isSelected && !selected[lastCard])
			{
				if(ym>=10+activePlayer*getHeight()/5 && ym<=97+10+activePlayer*getHeight()/5)
					if(xm>=100+20*lastCard && xm<=100+73+20*lastCard)
						{
							selected[lastCard]=true;
							isSelected=true;
						}
			}
			else
			{
				if(ym>=2+activePlayer*getHeight()/5 && ym<=97+2+activePlayer*getHeight()/5)
					if(xm>=100+20*lastCard && xm<=100+73+20*lastCard)
						{
							selected[lastCard]=false;
							isSelected=true;
						}
			}
			
			/*	if it is not inside the last card, check whether it contains in other cards.
				Once it is contained, no need to check in other card.
			*/
			for(int i=lastCard-1;i>=0 && !isSelected;i--)
			{
				if(!selected[i] && !selected[i+1])
				{
					if(xm>=100+20*i && xm<100+20*(i+1) && ym>=10+activePlayer*getHeight()/5 && ym <= 97 + 10 + activePlayer*getHeight()/5)
					{
						selected[i]=true;
						isSelected=true;
					}
				}
				else if(selected[i] && selected[i+1])
				{
					if(xm>=100+20*i && xm<100+20*(i+1) && ym>=2+activePlayer*getHeight()/5 && ym <= 97 + 2 + activePlayer*getHeight()/5)
					{
						selected[i]=false;
						isSelected=true;
					}
				}
				else if(selected[i] && !selected[i+1])
				{
					if(xm>=100+20*i && xm<=100+20*(i+1) && ym>=2+activePlayer*getHeight()/5 && ym <= 97 + 2 + activePlayer*getHeight()/5)
					{
						selected[i]=false;
						isSelected=true;
					}
					if(xm>=100+20*i && xm<100+73+20*i && ym>=2+activePlayer*getHeight()/5 && ym <10 + activePlayer*getHeight()/5)
					{
						selected[i]=false;
						isSelected=true;
					}
				}
				else 
				{
					if(xm>=100+20*i && xm<100+20*(i+1) && ym>=10+activePlayer*getHeight()/5 && ym <= 97 + 10 + activePlayer*getHeight()/5)
					{
						selected[i]=true;
						isSelected=true;
					}
					if(xm>=100+20*i && xm<100+73+20*i && ym>=97+2+activePlayer*getHeight()/5 && ym <= 97 + 10 + activePlayer*getHeight()/5)
					{
						selected[i]=true;
						isSelected=true;
					}
				}
			}
			
			
			repaint();
		}

		public void mousePressed(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		
	}
	
	/**
	 * An inner class that implements the ActionListener interface.
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle button-click events for the “Play” button
	 * 
	 * @author Budiman
	 *
	 */
	public class PlayButtonListener implements ActionListener
	{
		
		/**
		 * To handle if the playButton pressed
		 */
		public void actionPerformed(ActionEvent e) 
		{
			int selected[]=getSelected();
			if(selected!=null)
			{
				game.makeMove(game.getCurrentIdx(),selected);
				resetSelected();
			}
		}
		
	}
	
	/**
	 * An inner class that implements the ActionListener interface.
	 * Implements the actionPerformed() method from the ActionListener interface to 
	 * handle button-click events for the “Pass” button.
	 * @author Budiman
	 *
	 */
	public class PassButtonListener implements ActionListener
	{

		/**
		 * To handle if the passButton pressed
		 */
		public void actionPerformed(ActionEvent arg0) {
			resetSelected();
			game.makeMove(game.getCurrentIdx(),null);
		}
		
	}
	
	/**
	 * An inner class that implements the KeyListener interface.
	 * Will handle the enter-button pressed on the message area
	 * 
	 * @author Budiman
	 *
	 */
	public class EnterKeyListener implements KeyListener{

        public void keyPressed(KeyEvent e){

            if(e.getKeyChar() == KeyEvent.VK_ENTER){
            	if(outgoing.getText()!=null && outgoing.getText()!="")
            	{
                	game.sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, outgoing.getText()));
                
                	outgoing.setText("");
                	outgoing.requestFocus();
            	}
            }      
        }

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
    }

	
	/**
	 * An inner class that implements the ActionListener interface.
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle menu-item-click events for the “Connect” menu item.
	 * 
	 * @author Budiman
	 *
	 */
	public class ConnectMenuItemListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e) {
				connect();
		}
		
	}
	
	
	/**
	 *An inner class that implements the ActionListener interface.
	 *Implements the actionPerformed() method from the ActionListener interface
	 *to handle menu-item-click events for the “Quit” menu item.
	 * @author Budiman
	 *
	 */
	public class QuitMenuItemListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e) {
			System.exit(0);
			
		}
		
	}
	
	/**
	 *An inner class that implements the ActionListener interface.
	 *Implements the actionPerformed() method from the ActionListener interface
	 *to handle menu-item-click events for the “Clear Chat” menu item.
	 * @author Budiman
	 *
	 */
	public class ClearChatArea implements ActionListener
	{

		public void actionPerformed(ActionEvent e) {
				clearChatArea();
			
		}
		
	}
	
	/**
	 *An inner class that implements the ActionListener interface.
	 *Implements the actionPerformed() method from the ActionListener interface
	 *to handle menu-item-click events for the “Clear Message” menu item.
	 * @author Budiman
	 *
	 */
	public class ClearMsgArea implements ActionListener
	{

		public void actionPerformed(ActionEvent e) {
				clearMsgArea();
			
		}
		
	}
	
}
