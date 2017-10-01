Event::CardPlaced {
	if(player not actualPlayer) {
		push(pop(playedCards), player)
		println("The card you played was returned.")
		penalize "Out of turn."
	}
}

Event::CardPulled {
	if(player not actualPlayer) {
		penalize "Out of turn."
	}
}