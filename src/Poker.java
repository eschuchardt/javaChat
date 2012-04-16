import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;


public class Poker {
    /**
     * Needs to init state and stuff
     */
//    public void initPoker() {
//    	mDeckState = new DeckState();
//    	mDeckState.setNumPlayers(2);
//    	mDeckState.setPhase(DEAL_PHASE);
//    	//TODO: init player
//    	
//    }
    
    //TODO: Need to serialize each individual thing...http://arstechnica.com/civis/viewtopic.php?f=20&t=311940
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }
    
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }
        
        
    /** 
     * This is the initial function that will start all the stuff that poker does.
     * Should interpret data and appropriately adjust the state.
     * 
     * @param buffer  The buffer that has the message passed from another user.
     */
    public void pokerInterp(byte[] buffer) {
    	//mDeckState.copy(mDeckState, (DeckState)buffer);
    	try {
    		deserialize(buffer);
    	} catch (IOException e) {
    		//TODO:
    	} catch (ClassNotFoundException e) {
    		//TODO:
    	}
    	
    	
    	switch (mDeckState.getPhase()) {
    	case DEAL_PHASE: break;
    	case BET1_PHASE: break;
    	case DRAW_PHASE: break;
    	case BET2_PHASE: break;
    	case FINAL_PHASE: break;
    	default: break;
    	}
    }
    
    //Phases for the round
    private static final int DEAL_PHASE = 0;
    private static final int BET1_PHASE = 1;
    private static final int DRAW_PHASE = 2;
    private static final int BET2_PHASE = 3;
    private static final int FINAL_PHASE = 4;
    
    Random ranGen = new Random(52);
    
    static DeckState mDeckState;
    
//    public class Player {
//    	int playerId;
//    	int[] hand = new int[5];
//    	
//    	Player(int pid) {
//    		playerId = pid;
//    	}
//    }
    
//    public class DeckState {
//    	int[] deck;
//    	int[] usedCards;
//    	int[] playersCards; //the cards of each player in order.  Do mod to find player num
//    	int numPlayers;
//    	int phase;
//    	
//    	
//    	
//    	/** 
//    	 * Constructor
//    	 */
//    	DeckState() {
//    		deck = new int[52];
//    		playersCards = new int[20];
//    		usedCards = new int[52];
//    		for(int i=0; i<52; i++) {
//    			deck[i] = i;
//    		}
//    	}
//    	
//	    public void copy(DeckState oldState, DeckState newState) { //TODO: may need to do a soft copy because of pointers.
//			oldState.setUsedCards(newState.getUsedCards());
//			oldState.setPhase(newState.getPhase());
//			oldState.setPlayersCards(newState.getPlayersCards(), numPlayers);
//		}
//    	
//    	public void setUsedCards(int[] cards) {
//    		usedCards = cards;
//    	}
//    	public int[] getUsedCards() {
//    		return usedCards;
//    	}
//    	
//    	public void setNumPlayers(int players) {
//    		numPlayers = players;
//    	}
//    	public int getNumPlayers() {
//    		return numPlayers;
//    	}
//    	
//    	public void setPhase(int p) {
//    		phase = p;
//    	}
//    	public int getPhase() {
//    		return phase;
//    	}
//    	
//    	public void setPlayersCards(int[] cards, int playerNum) {
//    		
//    	}
//    	public int[] getPlayersCards() {
//    		return playersCards;
//    	}
//    }
    
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
    		ranNumber = ranGen.nextInt();
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
    
    public static void main(String[] args) {
        //The init function {
    	mDeckState = new DeckState();
    	mDeckState.setNumPlayers(2);
    	mDeckState.setPhase(DEAL_PHASE);
    	//TODO: init player }
        	
    	Player p1 = new Player(1);
    	Player p2 = new Player(2);
    	
    }

}
