import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;



public class DeckState implements Serializable {
	//int[] deck;
	int[] usedCards;
	int[] playersCards; //the cards of each player in order.  Do mod to find player num
	int[] playerUpdate;
	int numPlayers;
	int phase;
	Random ranGen = new Random(52);
	
	
	
	/** 
	 * Constructor
	 */
	DeckState() {
		//deck = new int[52];
		playersCards = new int[20];
		usedCards = new int[52];
		playerUpdate = new int[4];
		//for(int i=0; i<52; i++) {
		//	deck[i] = i;
		//}
	}
	
	public void initPlayersCards() {
		for(int i=0; i<playersCards.length; i++) {
			playersCards[i] = 52;
		}
	}
	
	public void initUsedCards() {
		for(int i=0; i<usedCards.length; i++) {
			usedCards[i] = 52;
		}
	}
	
	public void initPlayerUpdate() {
		for(int i=0; i<playerUpdate.length; i++) {
			playerUpdate[i] = 0;
		}
	}
	
	public boolean isUpdated() {
		boolean updated = true;
		for(int i=0; i<numPlayers; i++) {
			if(playerUpdate[i] == 0) {
				updated = false;
				break;
			}
		}
		return updated;
	}
	
	/**
     * 
     * @param hand  a passed in hand of a player or used cards
     * @param card  an int that represents a given card
     * @return  true if card is legal.
     */
    public boolean checkLegalCard(int[] hand, int card) {
    	boolean acceptCard = true;
    	for(int i=0; i<hand.length; i++) {
    		if(card == hand[i]) {
    			acceptCard = false;
    			break;
    		}
    	}
    	return acceptCard;
    }
	
	/**
     * Deals the cards based upon cards that have already been dealt to other players.
     * @param hand  the hand of a player to be passed in.
     * @param usedCards  used cards from all players.
     * @return
     */
	public void dealCards(int[] hand, int[] usedCards) {
    	int cardCount = 0;
    	int ranNumber;
    	boolean acceptCard = true;
    	
    	//init cards to 0
    	for(int i=0; i<5; i++) {
    		hand[i] = 52; //52 is not a number in the deck.
    	}
    	
    	//While all the cards in the hand have not been changed...
    	while (cardCount < 5) {
    		ranNumber = ranGen.nextInt(52);
    		if(usedCards != null) {
    			//see if the number is in someone else's hand.
    			acceptCard = checkLegalCard(usedCards, ranNumber);
    		}
    		
			//check to see that the card is not already in the hand.
			if(acceptCard) {
				acceptCard = checkLegalCard(hand, ranNumber);
				if(acceptCard) {
					hand[cardCount] = ranNumber;
    				cardCount++;
				} //end if
			} //end if
			
			//reset accept true to true
    		acceptCard = true;
    	} //end while
    } //end dealCards()
	
	/**
	 * Copies cards from the cards of all players and assigns only the individual
	 * hand of the player specified into hand.
	 * 
	 * @param hand  5 cards to put assigned cards into depending on playerNum
	 * @param playerNum  The number of the player
	 */
	public void getHand(int[] hand, int playerNum) {
		if(playerNum > numPlayers) {
			//TODO: throw error
		}
		int offset = (playerNum*5);
		int j = 0;
		for(int i=offset; i<offset; i++) {
			hand[j]=playersCards[i];
			j++;
		}
	}
	
	public void setHand(int[] hand, int playerNum) {
		if(playerNum > numPlayers) {
			//TODO: throw error
		}
		int offset = (playerNum*5);
		int j = 0;
		for(int i=offset; i<offset+5; i++) {
			playersCards[i]=hand[j];
			j++;
		}
	}
	
	public void copy(DeckState oldState, DeckState newState) { //TODO: may need to do a soft copy because of pointers.
		oldState.setUsedCards(newState.getUsedCards());
		oldState.setPhase(newState.getPhase());
		oldState.setPlayersCards(newState.getPlayersCards(), numPlayers);
	}
	
	public void setUsedCards(int[] cards) {
		usedCards = cards;
	}
	public int[] getUsedCards() {
		return usedCards;
	}
	
	public void setNumPlayers(int players) {
		numPlayers = players;
	}
	public int getNumPlayers() {
		return numPlayers;
	}
	
	public void setPhase(int p) {
		phase = p;
	}
	public int getPhase() {
		return phase;
	}
	
	public void setPlayersCards(int[] cards, int playerNum) {
		
	}
	public int[] getPlayersCards() {
		return playersCards;
	}
	
	public void setPlayerUpdate(int player, int value) { //update whether a player had taken his turn
		playerUpdate[player] = value;
	}
	public int getPlayerUpdate(int playerNum) {
		return playerUpdate[playerNum]; 
	}
}
