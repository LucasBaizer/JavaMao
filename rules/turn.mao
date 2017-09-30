Event::CardPlaced => {
	if(Player not ActualPlayer) {
		//return the card to the player
		push card -> Player
		
		//pop the stack of played cards so that the card has been given to the player
		pop PlayedCards
		
		//additionally penalize the player
		penalize "Out of turn."
	}
}