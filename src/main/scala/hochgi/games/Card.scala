package hochgi.games

class Card(val rank: Int, val suit: Char) extends Ordered[Card] {

  def this(card: String) = this(Card.cardRank(card(0)), card(1))
  override def toString: String = Card.rankCard(rank) + " of " + {
    suit match {
      case 'C' => "Clubs"
      case 'S' => "Spades"
      case 'D' => "Diamonds"
      case 'H' => "Hearts"
    }
  }
  
  override def compare(that: Card): Int = this.rank - that.rank
}

object Card {
  private val cardRank = Map[Char, Int]('2' -> 2,
    '3' -> 3, '4' -> 4, '5' -> 5, '6' -> 6, '7' -> 7,
    '8' -> 8, '9' -> 9, 'T' -> 10, 'J' -> 11,
    'Q' -> 12, 'K' -> 13, 'A' -> 14)

  private val rankCard = Map[Int, String](2 -> "2",
    3 -> "3", 4 -> "4", 5 -> "5", 6 -> "6", 7 -> "7",
    8 -> "8", 9 -> "9", 10 -> "10", 11 -> "Jack",
    12 -> "Queen", 13 -> "King", 14 -> "As")
}