Event::CardPlaced {
	//if the card is a seven
	if(face(card) is SEVEN) {
		//is the last three cards were sevens
		if(face(below(3)) is SEVEN and face(below(2)) is SEVEN and face(below(1)) is SEVEN) {
			say "Have a very, very, very nice day."
		}
		if(face(below(2)) is SEVEN and face(below(1)) is SEVEN) {
			say "Have a very, very nice day."
		}
		if(face(below(1)) is SEVEN) {
			say "Have a very nice day."
		}
		say "Have a nice day."
	}
}