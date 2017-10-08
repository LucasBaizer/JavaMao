Event::CardPlaced {
	if(face(card) == face(below(1))) {
		exit
	}
	if(suit(card) == suit(below(1))) {
		exit
	}
	List::push(List::pop(playedCards), player)
	println("The card you played was returned.")
	penalize "Wrong placement."
}