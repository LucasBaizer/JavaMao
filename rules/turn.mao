Event::CardPlaced {
	if(player != actualPlayer) {
		push(pop(playedCards), player)
		println("The card you played was returned.")
		penalize "Out of turn."
	}
}

Event::CardPulled {
	if(player != actualPlayer) {
		penalize "Out of turn."
	}
}