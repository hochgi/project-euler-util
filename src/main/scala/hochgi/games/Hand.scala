package hochgi.games

import java.lang.IllegalArgumentException

case class Hand(hand: List[Card]) {
	require(hand.length == 5)
	val sorted = hand.sortWith((c1, c2) => c1 < c2)

	lazy val suited: Boolean = sorted.tail.forall { _.suit == sorted(0).suit }

	lazy val straight: Boolean = sorted.sliding(2).forall {
		case List(a, b) => b.rank - a.rank == 1 || (b.rank == 14 && a.rank == 5)
		case _ => throw new IllegalArgumentException("sorted must be of length 5 (which is >= 2)")
	}

	def stringToCards(hand: String): List[Card] = {
		require(hand.matches("""([2-9TJQKA][CSHD]\s){4}[2-9JQKA][CSHD]"""))
		hand.split(" ").map { new Card(_) }.toList
	}

	def this(hand: String) = this(hand.split(" ").map { new Card(_) }.toList)

	override def toString: String = sorted.mkString("[", ",", "]")
}