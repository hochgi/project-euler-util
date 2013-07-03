package hochgi.games

case class Hand(hand: List[Card]) {
	require(hand.length == 5)
	val sorted = hand.sortWith((c1, c2) => c1 < c2)
	
	lazy val suited: Boolean = sorted.tail.forall{_.suit == sorted(0).suit}
	lazy val straight: Boolean = {
	  def recHelper(l: List[Card]): Stream[Boolean] = l match {
	    case Nil => Stream.Empty
	    case x :: Nil => Stream(x.rank > 5)
	    case x :: y :: Nil if(x.rank == 5 && y.rank == 14) => Stream(true)
	    case x :: y :: xs => (y.rank - x.rank == 1) #:: recHelper(y :: xs)
	  }
	  recHelper(sorted).forall(_ == true)
	} 
	
	def stringToCards(hand: String): List[Card] = {
		require(hand.matches("""([2-9TJQKA][CSHD]\s){4}[2-9JQKA][CSHD]"""))
		hand.split(" ").map {new Card(_)}.toList
	}
	
	def this(hand: String) = this(hand.split(" ").map {new Card(_)}.toList)
	
	override def toString: String = sorted.mkString("[",",","]")
}