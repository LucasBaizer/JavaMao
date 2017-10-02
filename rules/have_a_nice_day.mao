Event::CardPlaced {
	if(face(card) == SEVEN) {
		var toSay = "Have a "
		
		for(var i = 1 -> 4) {
			if(face(below(i)) != SEVEN) {
				break
			}
			toSay = concat(toSay, "very ")
		}
		
		toSay = concat(toSay, "nice day.")
		say toSay
	}
}