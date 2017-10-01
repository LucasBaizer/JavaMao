Event::CardPlaced {
	if(face(card) is face(below(1))) {
		exit
	}
	if(suit(card) is suit(below(1))) {
		exit
	}
	push(pop(playedCards), player)
	println("The card you played was returned.")
	penalize "Wrong placement."
}