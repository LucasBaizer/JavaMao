Event::CardPlaced {
	if(player != actualPlayer) {
		List::push(List::pop(playedCards), player)
		println("The card you played was returned.")
		penalize "Out of turn."
	}
}

Event::CardPulled {
	if(player != actualPlayer) {
		penalize "Out of turn."
	}
}