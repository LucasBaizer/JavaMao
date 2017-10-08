#register one card remaining

Event::CardPlaced {
	if(Player::cardCount(player) == 1) {
		say "One card remaining."
	}
}
