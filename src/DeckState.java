import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;



public class DeckState implements Serializable {
	//int[] deck;
	int[] usedCards;
	int[] playersCards; //the cards of each player in order.  Do mod to find player num
	int[] playerUpdate;
	int numPlayers;
	int phase;
	
	
	
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
	
	public void getHand(hand, playerNum) {
		//do some mod stuff in order to assign numbers in corresponding players hand to hand
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
	public int[] getPlayerUpdate() {
		return playerUpdate; 
	}
}
