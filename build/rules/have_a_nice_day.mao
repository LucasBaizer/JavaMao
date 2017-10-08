#register have a nice day
#register have a very nice day
#register have a very very nice day
#register have a very very very nice day

Event::CardPlaced {
	if(face(card) == SEVEN) {
		var toSay = "Have a "
		
		for(var i = 1 -> Math::min(List::size(playedCards), 4)) {
			if(face(below(i)) != SEVEN) {
				break
			}
			toSay = String::concat(toSay, "very ")
		}
		
		toSay = String::concat(toSay, "nice day.")
		say toSay
	}
}