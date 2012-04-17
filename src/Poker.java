import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Scanner;


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
	public static byte[] serialize(DeckState state) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);   
		out.writeObject(state);
		byte[] bytes = bos.toByteArray();
		return bytes;
		//...
		//out.close();
		//bos.close();
	}
    
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
    	ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
    	ObjectInput in = new ObjectInputStream(bis);
    	Object o = in.readObject(); 
    	return o;
    	//...
    	//bis.close();
    	//in.close();
    }
        
        
    /** 
     * This is the initial function that will start all the stuff that poker does.
     * Should interpret data and appropriately adjust the state.
     * 
     * @param buffer  The buffer that has the message passed from another user.
     */
    public static void pokerInterp(byte[] buffer, int playerNum) {
    	//begin deserialize and receive
    	DeckState newDeckState = new DeckState();
    	try {
    		newDeckState = (DeckState)deserialize(buffer);
    	} catch (IOException e) {System.out.println("IOException");} catch (ClassNotFoundException e) {System.out.println("ClassNotFoundException");}
    	//end deserialize and receive
    	
    	int[] hand = new int[5];
    	//newDeckState.getHand(hand, playerNum);
    	
    	//switch on the phase.
    	switch (newDeckState.getPhase()) {
    	case DEAL_PHASE:
    		if(newDeckState.getPlayerUpdate(playerNum) == FALSE) {
    			newDeckState.dealCards(hand, newDeckState.getUsedCards());
    			newDeckState.setHand(hand, playerNum);
    			newDeckState.setPlayerUpdate(playerNum, TRUE);
    		}
    		if(newDeckState.isUpdated()) {
    			newDeckState.setPhase(BET1_PHASE);
    		}
    		break;
    	case BET1_PHASE: break;
    	case DRAW_PHASE: break;
    	case BET2_PHASE: break;
    	case FINAL_PHASE: break;
    	default: break;
    	}
    	
    	mDeckState = newDeckState;
    }
    
    //Phases for the round
    private static final int DEAL_PHASE = 0;
    private static final int BET1_PHASE = 1;
    private static final int DRAW_PHASE = 2;
    private static final int BET2_PHASE = 3;
    private static final int FINAL_PHASE = 4;
    
    //Set truth value
    private static final int FALSE = 0;
    private static final int TRUE = 1;
    
    //Offset for player cards
    private static final int PLAYER_CARD_OFFSET = 5;
    
    
    
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
    
    
    
    
    
   
    public static void printDeckState(DeckState state) {
    	int[] playersCards = state.getPlayersCards();
    	int[] usedCards = state.getUsedCards();
    	int[] playerUpdate = new int[state.getNumPlayers()];
    	for(int i=0; i<state.getNumPlayers(); i++) {
    		playerUpdate[i] = state.getPlayerUpdate(i);
    	}
    	System.out.print("Players Cards: \n");
    		for(int j=0; j<20; ) {
    			for(int i=0; i<5; i++) {
    				System.out.print(playersCards[j] + " ");
    				j++;
    			}
    			System.out.println();
    		}
    	System.out.println();
    	System.out.print("Used Cards: \n");
	    	for(int j=0; j<52; ) {
				for(int i=0; i<10 && j<52; i++) {
					System.out.print(usedCards[j]  +" ");
					j++;
				}
				System.out.println();
			}
	    System.out.println();
    	System.out.println("Phase: " + state.getPhase());
    	System.out.println();
    	System.out.println();
    }
    
    public static void main(String[] args) {
        //The init function {
    	mDeckState = new DeckState();
    	mDeckState.setNumPlayers(2);
    	mDeckState.setPhase(DEAL_PHASE);
    	mDeckState.initUsedCards();
    	mDeckState.initPlayersCards();
    	mDeckState.initPlayerUpdate();
    	mDeckState.setPlayerUpdate(0, FALSE);
    	mDeckState.setPlayerUpdate(1, FALSE);
    	//TODO: init player }
        	
    	Player p0 = new Player(0);
    	Player p1 = new Player(1);
    	
    	Scanner scan = new Scanner(System.in);
    	System.out.println("initial state:");
    	printDeckState(mDeckState);
    	while(mDeckState.getPhase() != FINAL_PHASE) {
    		//Player 0
    	
	    	/* everything from here and below is a part of the interp function */
	    	//begin serialize and send
	    	byte[] bytes0 = new byte[1024];
	    	try {
	    		bytes0 = serialize(mDeckState);
	    	} catch (IOException e) {System.out.println("IOException");}
	    	//end serialize and send
	    	
	    	//pick up where left off in code
	    	pokerInterp(bytes0, 0/*playerNumber*/);
	    	System.out.println("Player0: ");
	    	printDeckState(mDeckState);
    	
    	
    		//Player 1
	    	
	    	/* everything from here and below is a part of the interp function */
	    	//begin serialize and send
	    	byte[] bytes1 = new byte[1024];
	    	try {
	    		bytes1 = serialize(mDeckState);
	    	} catch (IOException e) {System.out.println("IOException");}
	    	//end serialize and send
	    	
	    	//pick up where left off in code
	    	pokerInterp(bytes1, 1/*playerNumber*/);
	    	System.out.println("Player1: ");
	    	printDeckState(mDeckState);
	    	//int c = scan.nextInt();
    	}
    	
    	
    	
    	
    	
    }

}
