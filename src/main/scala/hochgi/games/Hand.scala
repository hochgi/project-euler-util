package hochgi.games

import java.lang.IllegalArgumentException

object Hand {
	def apply(hand: List[Card]): Hand = {
	require(hand.length == 5)
	
	val sorted = hand.sortWith((c1, c2) => c1 < c2)
	val suited: Boolean = sorted.tail.forall { _.suit == sorted(0).suit }
	val straight: Boolean = sorted.sliding(2).forall {
		case List(a, b) => b.rank - a.rank == 1 || (b.rank == 14 && a.rank == 5)
		case _ => throw new IllegalArgumentException("sorted must be of length 5 (which is >= 2)")
	}
	
	val grouped: Map[Int,List[Card]] = sorted.groupBy(_.rank)
	
	if(suited && straight) new StraightFlush(sorted)
	else if(grouped.exists(_._2.length == 4)) new FourOfAKind(sorted, grouped)
	else	???
	}

	def stringToCards(hand: String): List[Card] = {
		require(hand.matches("""([2-9TJQKA][CSHD]\s){4}[2-9JQKA][CSHD]"""))
		hand.split(" ").map { new Card(_) }.toList
	}

	def apply(hand: String): Hand = apply(hand.split(" ").map { new Card(_) }.toList)
}

abstract class Hand(sorted: List[Card]) extends Ordered[Hand] {
	override def toString: String = sorted.mkString("[", ",", "]")
}

case class HighCard(sorted: List[Card]) extends Hand(sorted) {
	override def compare(that: Hand): Int = that match {
		case HighCard(sorted2) => ((a: (Card,Card)) => a._1.rank - a._2.rank)((sorted zip sorted2).filter(x => x._1.rank != x._2.rank).last)  
		case _ => -1
	}
}

case class OnePair(sorted: List[Card]) extends Hand(sorted) {
	override def compare(that: Hand) = ???
}
case class TwoPairs(sorted: List[Card]) extends Hand(sorted) {
	override def compare(that: Hand) = ???
}
case class ThreeOfAKind(sorted: List[Card]) extends Hand(sorted) {
	override def compare(that: Hand) = ???
}
case class Straight(sorted: List[Card]) extends Hand(sorted) {
	override def compare(that: Hand) = ???
}
case class Flush(sorted: List[Card]) extends Hand(sorted) {
	override def compare(that: Hand) = ???
}
case class FullHouse(sorted: List[Card]) extends Hand(sorted) {
	override def compare(that: Hand) = ???
}

case class FourOfAKind(sorted: List[Card], grouped: Map[Int,List[Card]]) extends Hand(sorted) {
	override def compare(that: Hand) = that match {
		case FourOfAKind(_, g) => {
			val f1 = grouped.filter(_._2.length == 4).keys.head
			val f2 = g.filter(_._2.length == 4).keys.head
			if(f1 != f2) f1 - f2
			else grouped.filter(_._2.length == 1).keys.head - g.filter(_._2.length == 1).keys.head 
		}
		case StraightFlush(_) => -1
		case _ => 1
	}
}

case class StraightFlush(sorted: List[Card]) extends Hand(sorted) {
	override def compare(that: Hand) = that match {
		case StraightFlush(sorted2) => sorted.head.rank - sorted2.head.rank
		case _ => 1
	}
}
