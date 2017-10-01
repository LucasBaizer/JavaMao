Event::CardPlaced {
	if(face(card) is SEVEN) {
		var toSay = "Have a "
		
		var i = 1
		for(i to 4) {
			if(face(below(i)) not SEVEN) {
				break
			}
			toSay = concat(toSay, "very ")
		}
		
		toSay = concat(toSay, "nice day.")
		say toSay
	}
}