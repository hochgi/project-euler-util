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
	else if(grouped.exists(_._2.length == 3) && grouped.exists(_._2.length == 2)) new FullHouse(sorted, grouped)
	else if(suited) new Flush(sorted)
	else if(straight) new Straight(sorted)
	else if(grouped.exists(_._2.length == 3)) new ThreeOfAKind(sorted, grouped)
	else if(grouped.filter(_._2.length == 2).size == 2) new TwoPairs(sorted, grouped)
	else if(grouped.filter(_._2.length == 2).size == 1) new OnePair(sorted, grouped.filter(_._2.length == 2).head._1)
	else HighCard(sorted)
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

case class OnePair(sorted: List[Card], pairRank: Int) extends Hand(sorted) {
	override def compare(that: Hand) = that match {
	  case OnePair(s,p) => {
	    if(p != pairRank) pairRank - p
	    else sorted zip s map{case(a,b) => a.rank - b.rank} filter(_ != 0) last
	  }
	  case _: HighCard => 1
	  case _ => -1
	}
}

case class TwoPairs(sorted: List[Card], grouped: Map[Int,List[Card]]) extends Hand(sorted) {
	override def compare(that: Hand) = that match {
	  case TwoPairs(_,g) => {
	    val p1 = grouped.filter(_._2.length == 2).keySet
	    val p2 = g.filter(_._2.length == 2).keySet
	    if(p1.max != p2.max) p1.max - p2.max
	    else if(p1.min != p2.min) p1.min - p2.min
	    else grouped.filter(_._2.length == 1).head._1 - g.filter(_._2.length == 1).head._1 
	  }
	  case _: OnePair => 1
	  case _: HighCard => 1
	  case _  => -1
	}
}

case class ThreeOfAKind(sorted: List[Card], grouped: Map[Int,List[Card]]) extends Hand(sorted) {
	override def compare(that: Hand) = that match {
	  case ThreeOfAKind(_,g) => grouped.filter(_._2.length == 3).keys.head -  g.filter(_._2.length == 3).keys.head
	  case _: TwoPairs => 1
	  case _: OnePair => 1
	  case _: HighCard => 1
	  case _  => -1
	}
}

case class Straight(sorted: List[Card]) extends Hand(sorted) {
	override def compare(that: Hand) = that match {
	  case Straight(s) => sorted.head.rank - s.head.rank //A,2,3,4,5 is sorted to have 2 at the start and not A
	  case _: ThreeOfAKind => 1
	  case _: TwoPairs => 1
	  case _: OnePair => 1
	  case _: HighCard => 1
	  case _ => -1
	}
}

case class Flush(sorted: List[Card]) extends Hand(sorted) {
	override def compare(that: Hand) = that match {
	  case Flush(s) => sorted zip s map{case (a,b) => a.rank - b.rank} filter(_ == 0) last  
	  case _: FullHouse => -1
	  case _: FourOfAKind => -1
	  case _: StraightFlush => -1
	  case _ => 1
	}
}

case class FullHouse(sorted: List[Card], grouped: Map[Int,List[Card]]) extends Hand(sorted) {
	override def compare(that: Hand) = that match {
	  case FullHouse(_,g) => grouped.filter(_._2.length == 3).keys.head -  g.filter(_._2.length == 3).keys.head
	  case _: FourOfAKind => -1
	  case _: StraightFlush => -1
	  case _ => 1
	}
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
