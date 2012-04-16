

public class DeckState {
	int[] deck;
	int[] usedCards;
	int[] playersCards; //the cards of each player in order.  Do mod to find player num
	int numPlayers;
	int phase;
	
	
	
	/** 
	 * Constructor
	 */
	DeckState() {
		deck = new int[52];
		playersCards = new int[20];
		usedCards = new int[52];
		for(int i=0; i<52; i++) {
			deck[i] = i;
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
}
