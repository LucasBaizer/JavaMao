[CardPlaced] => {
	//if the face of the card is the same as the card below it, exit
	if(face(card) is face(below(1)) {
		exit
	}
	//if the suit of the card is the same as the card below it, exit
	if(suit(card) is suit(below(1)) {
		exit
	}
	
	//return the card to the player
	push card -> Player
	
	//pop the stack of played cards so that the card has been given to the player
	pop PlayedCards
	
	//additionally penalize the player
	penalize "Wrong placement."
}