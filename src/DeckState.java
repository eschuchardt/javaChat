import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;



public class DeckState implements Serializable {
	private static final int FOLD = 0;
	private static final int STAY = 1;
	
	private static final int FALSE = 0;
	private static final int TRUE = 1;
	
	//int[] deck;
	int[] playerState;
	int[] playersBids;
	int[] usedCards;
	int[] playersCards; //the cards of each player in order.  Do mod to find player num
	int[] playersMoney;
	int[] playerUpdate;
	int numPlayers;
	int phase;
	int pot;
	int currentBid;
	Random ranGen = new Random();
	
	
	
	/** 
	 * Constructor
	 */
	DeckState() {
		//deck = new int[52];
		pot = 0;
		currentBid = 0;
		playerState = new int[4];
		initPlayerState();
		playersBids = new int[4];
		initPlayersBids();
		playersCards = new int[20];
		usedCards = new int[52];
		playerUpdate = new int[4];
		playersMoney = new int[4];
		for(int i=0; i<playersMoney.length; i++) {
			playersMoney[i]= 200;
		}
		//for(int i=0; i<52; i++) {
		//	deck[i] = i;
		//}
	}
	
	public void initPlayersCards() {
		for(int i=0; i<playersCards.length; i++) {
			playersCards[i] = 52;
		}
	}
	
	public void initPlayerState() {
		for(int i=0; i<playerState.length; i++) {
			playerState[i]= STAY;
		}
	}
	
	public void initPlayersBids() {
		for(int i=0; i<playersBids.length; i++) {
			playersBids[i]= 0;
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
	
	public int getRandomCard(int[] uCards, int[] hand) {
		boolean validCard = false;
		int ranNumber = 52;
		while (!validCard) {
    		ranNumber = ranGen.nextInt(52);
    		if(uCards != null) {
    			//see if the number is in someone else's hand.
    			validCard = checkLegalCard(uCards, ranNumber);
    		}
    		else {
    			//TODO: throw exception or something
    		}
    		
    		if(validCard) {
    			validCard = checkLegalCard(hand, ranNumber);
			} //end if
			
			//reset accept true to true
    	} //end while
		return ranNumber;
	}
	
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
		for(int i=offset; i<offset+5; i++) {
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
	
	public boolean checkGoodCheck(int bid) {
		return bid == currentBid;
	}
	public boolean checkGoodRaise(int bid) {
		return bid > currentBid;
	}
	
	/**
	 * checks to see if more than one player is still going in the game.  
	 * @return  If there is only one player that has not folded, return false. 
	 * 			If there are more than one player in game, return true.
	 */
	public boolean checkPlayerState() {
		int n = 0;
		for(int i=0; i<numPlayers; i++) {
			if(playerState[i] == STAY)
				n++;
		}
		if(n>1)
			return true;
		return false;
	}
	
	
	/**
	 * like setUsedCards but only inserts one card.
	 * This won't be as efficient as doing setUsedCards() for multiple cards, but could be tons easier.
	 * @param cards
	 */
	public void setUsedCard(int card) {
		int i = 0;
		for(; i<usedCards.length; i++) {
			if(usedCards[i] == 52) {
				break;
			}
		}
		usedCards[i] = card;
	}
	/**
	 * sets used cards by going to the first instance of 52 and inserting cards starting from there on.
	 * @param cards
	 */
	public void setUsedCards(int[] cards) {
		for(int i=0; i<usedCards.length; i++) {
			if(usedCards[i] != 52) {
				continue;
			}
			for(int j=0; j<cards.length; j++) {
				usedCards[i] = cards[j];
				i++;
			}
			break;
		}
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
	
	/**
	 * updates playersCards 
	 * @param cards  To be copied
	 * @param playerNum 
	 */
	public void setPlayersCards(int[] cards, int playerNum) {
		for(int i=0; i<cards.length; i++) {
			playersCards[playerNum*5 + i] = cards[i];
		}
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
	public void setPlayerUpdateAndClear(int player, int value) { //sets update to value and sets all others that are still in to false
		for(int i=0; i<numPlayers; i++) {
			if(playerState[i] != FOLD) {
				playerUpdate[i] = FALSE;
			}
		}
		playerUpdate[player] = value;
	}
	
	public int getCurrentBid() {
		return currentBid;
	}
	public void setCurrentBid(int bid) {
		currentBid = bid;
	}
	
	public int getPot() {
		return pot;
	}
	public void setPot(int bid) {
		pot = bid;
	}
	
	public void setPlayersBids(int bid, int playerNum) {
		playersBids[playerNum] = bid;
	}
	public int getPlayersBids(int playerNum) {
		return playersBids[playerNum];
	}
	
	public void setPlayersMoney(int bid, int playerNum) {
		playersMoney[playerNum] = bid;
	}
	public int getPlayersMoney(int playerNum) {
		return playersMoney[playerNum];
	}
	
	public void setPlayerState(int value, int playerNum) {
		playerState[playerNum] = value;
	}
	public int getPlayerState(int playerNum) {
		return playerState[playerNum];
	}
	
	
	
	
}
