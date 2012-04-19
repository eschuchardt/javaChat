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
    
    /*
     * Test functions
     */
    	public static void testPair(int[] hand) {
    		hand[0] = 0;
			hand[1] = 13;
			hand[2] = 28;
			hand[3] = 29;
			hand[4] = 30;
    	}
    	public static void testTwoPair(int[] hand) {
    		hand[0] = 0;
			hand[1] = 13;
			hand[2] = 1;
			hand[3] = 14;
			hand[4] = 29;
    	}
    	public static void testThreeOfAKind(int[] hand) {
    		hand[0] = 0;
			hand[1] = 13;
			hand[2] = 26;
			hand[3] = 27;
			hand[4] = 28;
    	}
    	public static void testFourOfAKind(int[] hand) {
    		hand[0] = 0;
			hand[1] = 13;
			hand[2] = 26;
			hand[3] = 39;
			hand[4] = 40;
    	}
    	public static void testStraight(int[] hand) {
    		hand[0] = 14;
			hand[1] = 2;
			hand[2] = 3;
			hand[3] = 4;
			hand[4] = 5;
    	}
    	public static void testFlush(int[] hand) {
    		hand[0] = 0;
			hand[1] = 2;
			hand[2] = 3;
			hand[3] = 4;
			hand[4] = 5;
    	}
    	public static void testFullHouse(int[] hand) {
    		hand[0] = 0;
			hand[1] = 13;
			hand[2] = 26;
			hand[3] = 1;
			hand[4] = 14;
    	}
    	public static void testStraightFlush(int[] hand) {
    		hand[0] = 0;
			hand[1] = 1;
			hand[2] = 2;
			hand[3] = 3;
			hand[4] = 4;
    	}
	/*
     * END Test functions
     */
        
        
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
    	
    	
    	//TODO: remove this scanner
    	Scanner scan = new Scanner(System.in);
    	System.out.println("Player " + playerNum);
    	
    	int[] hand;
    	boolean whileFlag = true;
    	int bid = 0;
    	//boolean whileFlag = true;
    	//switch on the phase.
    	switch (newDeckState.getPhase()) {
    	case DEAL_PHASE:
    		hand = new int[5];
    		//newDeckState.getHand(hand, playerNum);
    		if(newDeckState.getPlayerUpdate(playerNum) == FALSE) {
    			//TODO: undo this following comment
    			//newDeckState.dealCards(hand, newDeckState.getUsedCards());
    			
    			/*
    			 * TODO: THIS IS THE TESTING PART
    			 */
    			switch(playerNum) {
    			case 0:
    				testStraight(hand);
    				break;
    			case 1:
    				testFlush(hand);
    				break;
    			case 2:
    				testFullHouse(hand);
    				break;
    			case 3:
    				testStraightFlush(hand);
    				break;
    			}
    			newDeckState.setHand(hand, playerNum);
    			//add dealt cards to usedCards var
    			newDeckState.setHand(hand, playerNum);
    			//add dealt cards to usedCards var
    			newDeckState.setUsedCards(hand);
    			newDeckState.setPlayerUpdate(playerNum, TRUE);
    		}
    		if(newDeckState.isUpdated()) {
    			newDeckState.setPhase(BET1_PHASE);
    			newDeckState.initPlayerUpdate();
    		}
    		break;
    	case BET1_PHASE: //Place bet or fold
    		
    		betPhase(newDeckState, playerNum);
    		
    		//check to see if all players involved have finished this phase
			if(newDeckState.isUpdated()) {
    			newDeckState.setPhase(DRAW_PHASE);
    			newDeckState.initPlayerUpdate();
    		}
			
    		//check to see if there are still more than one player who has not folded.
			//if there is not, set state to FINAL_PHASE
			if(!newDeckState.checkPlayerState()) {
				whileFlag = false;
				newDeckState.setPhase(FINAL_PHASE);
				break;
			}
    		break;
    	case DRAW_PHASE: 
    		//ask user which cards to replace
    		System.out.println("How many cards are you replacing?: ");
    		int replacing = scan.nextInt(); //find out how many cards they are replacing
    		if(replacing>0) {
	    		hand = new int[5];
	    		newDeckState.getHand(hand, playerNum);
//	    		int[] discard = new int[5];
//	    		for(int i=0; i<discard.length; i++) {
//	    			discard[i] = 52;
//	    		}
	    		int scanned;
	    		//TODO: check that int is in range of <5
	    		for(int i=0; i<replacing; i++) {
	    			//TODO: check within range
	    			System.out.println("Enter pos of card to replace: ");
	    			scanned = scan.nextInt(); //should scan position of card they are replacing
	    			
	    			hand[scanned] = newDeckState.getRandomCard(newDeckState.getUsedCards(), hand); //insert random number into local hand, but not into usedCards or playersCards
	    			newDeckState.setUsedCard(hand[scanned]); //insert new number into usedCards
	    		}
	    		newDeckState.setPlayersCards(hand, playerNum); //replace cards in deckstate
    		
    		}
    		//update player
    		newDeckState.setPlayerUpdate(playerNum, TRUE);
    		
    		//if everyone is updated, switch phases
    		if(newDeckState.isUpdated()) {
    			newDeckState.setPhase(BET2_PHASE);
    			newDeckState.initPlayerUpdate();
    		}
    		
    		break;
    	case BET2_PHASE: 
    		betPhase(newDeckState, playerNum);
    		
    		//check to see if all players involved have finished this phase
			if(newDeckState.isUpdated()) {
    			newDeckState.setPhase(FINAL_PHASE);
    			newDeckState.initPlayerUpdate();
    		}
			
    		//check to see if there are still more than one player who has not folded.
			//if there is not, set state to FINAL_PHASE
			if(!newDeckState.checkPlayerState()) {
				whileFlag = false;
				newDeckState.setPhase(FINAL_PHASE);
				break;
			}
    		break;
    	case FINAL_PHASE: 
    		/*
    		 * In the final phase, check to see if there is only 1 player left.
    		 * If so, that player gets all the winnings.
    		 * If not, run function to compare hand against each other.
    		 * Finally, reset things so that they can go to the deal phase again.
    		 */
    		
    		int winningPlayer = 52;
    		
    		//Check to see if there is more than 1 player left.  If not, that player gets all the winnings.
    		if(newDeckState.checkPlayerState()) { //meaning there is more than 1 player left
    			//run function to compare hands against each other and assign the winner to winning player
    			//TODO: stuff with winning hand and winning player
    			newDeckState.calcWinningHand();
    			winningPlayer = newDeckState.getWinningPlayer();
			}
    		else {
    			winningPlayer = newDeckState.getRemainingPlayer(); //there is only one person in the round
    		}
    		
    		
    		//distribute winnings to winning player
    		newDeckState.distributeWinnings(winningPlayer);
    		
    		//TODO: fix reset stuff
    		newDeckState.setPhase(DEAL_PHASE);
    		break;
    	default: break;
    	}
    	
    	mDeckState = newDeckState;
    }
    
    public static void betPhase(DeckState newDeckState, int playerNum) {
    	//TODO: remove this scanner
    	Scanner scan = new Scanner(System.in);
    	
    	int[] hand;
    	boolean whileFlag = true;
    	int bid = 0;
    	
    	while(whileFlag) {
			
    		if(newDeckState.getPlayerUpdate(playerNum) == FALSE) {
    			System.out.println("Options: \n" +
    					"0. Check\n" +
    					"1. Call\n" +
    					"2. Raise\n" +
    					"3. Fold");
    			int bidPhaseOption = scan.nextInt();
    			switch(bidPhaseOption) {
    			case BID_PHASE_CHECK:
    				//check if current bid is higher than bid
    				//if it is, replace currentBid with bid
    				//if bid is good, set flag to false
    				//else repeat and say why it didn't work
    				bid = newDeckState.getPlayersBids(playerNum);
    				if(newDeckState.checkGoodCheck(bid)) {
    					newDeckState.setCurrentBid(bid);
    					newDeckState.setPlayerUpdate(playerNum, TRUE);
    					whileFlag = false; //break the loop.
    				}
    				else {
    					System.out.println("\n Cannot Check.\n");
    				}
    				break;
    			case BID_PHASE_CALL:
    				//check if current bid is higher than bid
    				//if bid is good, set flag to false
    				//else repeat and say why it didn't work
    				bid = newDeckState.getCurrentBid();
    				if(newDeckState.getPlayersBids(playerNum) != bid) {
    					//don't need to set current bid because he just called to the current highest bid.
    					newDeckState.setPlayersBids(bid, playerNum);
    					newDeckState.setPlayerUpdate(playerNum, TRUE);
    					whileFlag = false; //break the loop.
    				}
    				else {
    					System.out.println("\n Cannot Call.\n");
    				}
    				break;
    			case BID_PHASE_RAISE:
    				//check if current bid is higher than bid
    				//if bid is good, set flag to false
    				//else repeat and say why it didn't work
    				System.out.println("You have " + newDeckState.getPlayersMoney(playerNum) + " to bid.\n Enter amount: ");
    				//TODO: check to see if they enter a valid amount.
    				bid = scan.nextInt();
    				if(newDeckState.checkGoodRaise(bid)) {
    					newDeckState.setPlayersBids(bid, playerNum);
    					newDeckState.setCurrentBid(bid);
    					newDeckState.setPlayerUpdateAndClear(playerNum, TRUE);
    					whileFlag = false; //break the loop.
    				}
    				else {
    					System.out.println("\n Did not outbid max.\n");
    				}
    				break;
    			case BID_PHASE_FOLD:
    				//if bid is good, set flag to false
    				newDeckState.setPlayerState(FOLD, playerNum);
    				newDeckState.setPlayerUpdate(playerNum, TRUE);
    				whileFlag = false;
    				break;
    			default:
    				//TODO: put in while loop for user input or something
    				System.out.println("\n Did not recognize input.\n");
    				break;
    			}
    		}
		} //end while loop
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
    
    //In or out of the game
    private static final int FOLD = 0;
	private static final int STAY = 1;
	
	private static final int BID_PHASE_CHECK = 0;
	private static final int BID_PHASE_CALL = 1;
	private static final int BID_PHASE_RAISE = 2;
	private static final int BID_PHASE_FOLD = 3;
    
    
    static DeckState mDeckState;
    
   
    public static void printDeckState(DeckState state) {
    	int[] playersCards = state.getPlayersCards();
    	int[] usedCards = state.getUsedCards();
    	int[] playerUpdate = new int[state.getNumPlayers()];
    	for(int i=0; i<state.getNumPlayers(); i++) {
    		playerUpdate[i] = state.getPlayerUpdate(i);
    	}
    	System.out.println("Phase: " + state.getPhase());
    	System.out.print("Players Cards: \n");
    		for(int j=0; j<20; ) {
    			for(int i=0; i<5; i++) {
    				System.out.print(playersCards[j] + " ");
    				j++;
    			}
    			System.out.println();
    		}
    	System.out.print("Used Cards: \n");
	    	for(int j=0; j<52; ) {
				for(int i=0; i<20 && j<52; i++) {
					System.out.print(usedCards[j]  +" ");
					j++;
				}
				System.out.println();
			}
    	
    	System.out.print("Players States: ");
    	for(int i=0; i<4; i++) {
    		System.out.print(state.getPlayerState(i) + " ");
    	}
    	System.out.println();
    	
    	System.out.print("Players Update: ");
    	for(int i=0; i<4; i++) {
    		System.out.print(state.getPlayerUpdate(i) + " ");
    	}
    	System.out.println();
    	
    	System.out.print("Players Bids: ");
    	for(int i=0; i<4; i++) {
    		System.out.print(state.getPlayersBids(i) + " ");
    	}
    	System.out.println();
    	System.out.print("Players Money: ");
    	for(int i=0; i<4; i++) {
    		System.out.print(state.getPlayersMoney(i) + " ");
    	}
    	System.out.println();
    	System.out.println("Current Bid: " + state.getCurrentBid());
    	System.out.println("Winning Player: " + state.getWinningPlayer());
    	System.out.println("Winning Hand: " + state.getWinningHand());
    	System.out.println();
    }
    
    public static void main(String[] args) {
        //The init function {
    	mDeckState = new DeckState();
    	mDeckState.setNumPlayers(4);
    	mDeckState.setPhase(DEAL_PHASE);
    	mDeckState.initUsedCards();
    	mDeckState.initPlayersCards();
    	mDeckState.initPlayerUpdate();
    	mDeckState.setPlayerUpdate(0, FALSE);
    	mDeckState.setPlayerUpdate(1, FALSE);
    	//TODO: init player }
        	
//    	Player p0 = new Player(0);
//    	Player p1 = new Player(1);
    	
    	Scanner scan = new Scanner(System.in);
    	//System.out.println("initial state:");
    	//printDeckState(mDeckState);
    	
//    	while(true /*mDeckState.getPhase() != FINAL_PHASE*/) {
//    		//Player 0
//    	
//	    	/* everything from here and below is a part of the interp function */
//	    	//begin serialize and send
//	    	byte[] bytes0 = new byte[1024];
//	    	try {
//	    		bytes0 = serialize(mDeckState);
//	    	} catch (IOException e) {System.out.println("IOException");}
//	    	//end serialize and send
//	    	
//	    	//pick up where left off in code
//	    	pokerInterp(bytes0, 0/*playerNumber*/);
//	    	//System.out.println("Player0: ");
//	    	printDeckState(mDeckState);
//	    	System.out.println();
//    	
//    	
//    		//Player 1
//	    	
//	    	/* everything from here and below is a part of the interp function */
//	    	//begin serialize and send
//	    	byte[] bytes1 = new byte[1024];
//	    	try {
//	    		bytes1 = serialize(mDeckState);
//	    	} catch (IOException e) {System.out.println("IOException");}
//	    	//end serialize and send
//	    	
//	    	//pick up where left off in code
//	    	pokerInterp(bytes1, 1/*playerNumber*/);
//	    	//System.out.println("Player1: ");
//	    	printDeckState(mDeckState);
//	    	System.out.println();
//	    	
//	    	
//	    	//Player 2
//	    	
//	    	/* everything from here and below is a part of the interp function */
//	    	//begin serialize and send
//	    	byte[] bytes2 = new byte[1024];
//	    	try {
//	    		bytes2 = serialize(mDeckState);
//	    	} catch (IOException e) {System.out.println("IOException");}
//	    	//end serialize and send
//	    	
//	    	//pick up where left off in code
//	    	pokerInterp(bytes2, 2/*playerNumber*/);
//	    	//System.out.println("Player2: ");
//	    	printDeckState(mDeckState);
//	    	System.out.println();
//	    	
//	    	
//	    	//Player 3
//	    	
//	    	/* everything from here and below is a part of the interp function */
//	    	//begin serialize and send
//	    	byte[] bytes3 = new byte[1024];
//	    	try {
//	    		bytes3 = serialize(mDeckState);
//	    	} catch (IOException e) {System.out.println("IOException");}
//	    	//end serialize and send
//	    	
//	    	//pick up where left off in code
//	    	pokerInterp(bytes3, 3/*playerNumber*/);
//	    	//System.out.println("Player3: ");
//	    	printDeckState(mDeckState);
//	    	System.out.println();
//    	}
    	
    	//check to see if pair/kind/flush system is working.
    	while(true /*mDeckState.getPhase() != FINAL_PHASE*/) {
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
	    	//System.out.println("Player0: ");
	    	printDeckState(mDeckState);
	    	System.out.println();
		
		
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
	    	//System.out.println("Player1: ");
	    	printDeckState(mDeckState);
	    	System.out.println();
	    	
	    	
	    	//Player 2
	    	
	    	/* everything from here and below is a part of the interp function */
	    	//begin serialize and send
	    	byte[] bytes2 = new byte[1024];
	    	try {
	    		bytes2 = serialize(mDeckState);
	    	} catch (IOException e) {System.out.println("IOException");}
	    	//end serialize and send
	    	
	    	//pick up where left off in code
	    	pokerInterp(bytes2, 2/*playerNumber*/);
	    	//System.out.println("Player2: ");
	    	printDeckState(mDeckState);
	    	System.out.println();
	    	
	    	
	    	//Player 3
	    	
	    	/* everything from here and below is a part of the interp function */
	    	//begin serialize and send
	    	byte[] bytes3 = new byte[1024];
	    	try {
	    		bytes3 = serialize(mDeckState);
	    	} catch (IOException e) {System.out.println("IOException");}
	    	//end serialize and send
	    	
	    	//pick up where left off in code
	    	pokerInterp(bytes3, 3/*playerNumber*/);
	    	//System.out.println("Player3: ");
	    	printDeckState(mDeckState);
	    	System.out.println();
		}
    	
    	
    	
    	
    	
    }

}
