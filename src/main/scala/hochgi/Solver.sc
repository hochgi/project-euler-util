import hochgi.games._
import hochgi.math._
import scala.annotation._

object Solver {

primes.take(50).toList                            //> res0: List[BigInt] = List(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43
                                                  //| , 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 
                                                  //| 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 2
                                                  //| 11, 223, 227, 229)
/*
	----------
	problem 93
	----------
val ss = Set[Double](0,1,2,3,4,5,6,7,8,9).subsets(4)

def longestConsecutives(s: Set[Double]): Int = {

	def reachableValues(xs: Set[Double]): Set[Double] = {
		if(xs.size == 1) xs
		else {
			val it = xs.subsets.filterNot(x => x.isEmpty || x == xs).map(subset => (xs.filterNot(subset.contains(_)), subset)) //every set partition will appear twice - but i won't handle this...
			val sb = new scala.collection.mutable.SetBuilder[Double, Set[Double]](Set())
			while(it.hasNext){
				val (s1,s2) = it.next
				val r1 = reachableValues(s1)
				val r2 = reachableValues(s2)
				r1.foreach{v1 =>
					r2.foreach{v2 =>
						sb ++= Set(v1 - v2, v1 + v2, v1 * v2, v1 / v2)
					}
				}
			}
			val res = sb.result
			res
		}
	}
	
	def longestStrickFromList(l: List[Double]): Int = l match {
		case Nil => 0
		case x :: Nil => 1
		case _ => {
			val i = 1 + l.sliding(2).takeWhile{case List(a,b) => (a+1) == b}.toList.length
			scala.math.max(i,longestStrickFromList(l.slice(i, l.length)))
		}
	}
	
	longestStrickFromList(reachableValues(s).filter(v => v > 0 && v.isWhole).toList.sortWith(_ < _))
}

var max = 28
var set = Set[Double](1,2,3,4)

ss.foreach{
s => {
		val x = longestConsecutives(s)
		if(x > max) {
			max = x
			set = s
		}
	}
}

max
set
*/
/*
	----------
	problem 74
	----------
val f = (0 to 9) map (i => (i -> factorial(i).toInt)) toMap
                                               
//(1 to 1000000).filter(n => n == toFactSum(n)).map((_,1)) // == ((1,1), (2,1), (145,1), (40585,1))
val m = scala.collection.mutable.Map[Int,Int](1 -> 1, 2 -> 1, 145 -> 1, 40585 -> 1, 169 -> 3, 363601 -> 3, 1454 -> 3, 871 -> 2, 872 -> 2, 45361 -> 2, 45362 -> 2)

def toFactSum(n: Int): Int = (n.digits map (f(_))).sum
def updateMap(hist: List[Int], iter: Int) = {
	val ma = hist zip ((iter - hist.length + 1) to iter)
	m ++= ma
}
                                                  
def countLoopLength(curr: Int, hist: List[Int], iter: Int, frst: Int, seen: Boolean): Unit = {
	if(m.contains(curr)) updateMap(hist, iter + m(curr))
	else countLoopLength(toFactSum(curr), curr :: hist, iter+1, frst, seen)
}
updateMap(11 :: Nil, 2)
(3 to 1000000).filterNot(m.contains(_)).foreach(i => countLoopLength(toFactSum(i), i :: Nil,1, 0, false))

m.count{case (_,v) => v ==60}
*/

/*
	def gamesStream: Stream[(Hand, Hand)] = {
		val it = scala.io.Source.fromFile("/home/gilad/poker.txt").getLines()
		def recHelper: Stream[(Hand, Hand)] = {
			if (it.hasNext) ((a: (String, String)) => (Hand(a._1), Hand(a._2.tail)))((it.next).splitAt(14)) #:: recHelper
			else Stream.Empty
		}
		recHelper
	}
	
    Hand("5S 5H 5C 5D 3H") < Hand("AH 5H 2H 4H 3H")
   Hand("AH 5H 2H 4H 3H") == Hand("AS 5S 2S 4S 3S")
                 */
                 
  //val y = Hand("2H 5S 7H 4H 3H")

	//x.suited
	//x.straight

//gamesStream.take(4).toList

//pandigitalStream(List(1,2,3,4)).foreach(x => println(x.mkString(",")))
/*
//better go from 999 to 1 and filtering primes...

val ts = {
	def getTupleStream(i: Int): Stream[(Int,Int)] = {
		if(i >= 1000) Stream.Empty
		else (i,new Rational(1,i).getDecimalRepresentationCycleLength) #:: getTupleStream(i+1)
	}
	
	getTupleStream(1)
}

ts.foldLeft((0,0)){case (a,b) => if(a._2 > b._2) a else {println(b);b}}
*/

/*
val tri = scala.collection.mutable.Set[Int]()
val words = scala.io.Source.fromFile("/home/gilad/words.txt", "UTF-8").getLines().next
val w = words.split(',').map(s => s.tail.dropRight(1))

val ab = 'A' to 'Z' zip (1 to 26) toMap
val triangles = scala.collection.mutable.Set[Int](1)
var j = 2

def isTriangle(i: Int): Boolean = {
	if(triangles.contains(i)) true
	else if(triangles.max > i) false
	else {
		while(triangles.max < i) {
			triangles.add(j*(j+1)/2)
			j += 1
		}
		triangles.contains(i)
	}
}


(w filter (s => isTriangle((s map (c => ab(c))) sum))).length
*/
                                                  
/*
val m = scala.collection.mutable.Map[(Int,Int),Int]() withDefaultValue(0)

var notFound = true
var firstPrimeInChain = 2


while(notFound){
val ps = primes.iterator
var current = ps.next
while(current < firstPrimeInChain) {
current = ps.next
}

def accumulateUpToMillion() {
}

}*/

/*
def goodNumbers(n: BigInt): Stream[BigInt] = (n + 10) #:: (n + 30) #:: (n + 70) #:: (n + 90) #:: goodNumbers(n + 100)

def isWellFormed(n: BigInt) = {
val square = (n * n).digits
if(square.length == 19 &&
   square(0) == 1 &&
square(2) == 2 &&
square(4) == 3 &&
square(6) == 4 &&
square(8) == 5 &&
square(10) == 6 &&
square(12) == 7 &&
square(14) == 8 &&
square(16) == 9 &&
square(18) == 0) true
else false
}
*/
/*
val validPerms = permutations(7).filter(n => digits2BigInt(n).isPrime)
validPerms.take(3).toList
validPerms.maxBy(digits2BigInt)
*/

/*
def odds: Stream[BigInt] = {
def next(n: BigInt): Stream[BigInt] = {n #:: next(n+2)}
next(1)
}

def isGoldbachy(n: BigInt): Boolean = {
!n.isPrime && {
val ps = primes.takeWhile(_ < n)
val sqrs = ps map (p => bigSqrtExact((n-p)/2) match {
case None => BigInt(0)
case Some(x) => x
}) filter(_ != 0)
!sqrs.isEmpty
}
}

isGoldbachy(13)
*/

/*
def countPrimes(a: Int, b: Int): Int = {
@tailrec
def primeOrDone(n: Int): Int = {
val p = n*n + a*n + b
if(p.isPrime) primeOrDone(n + 1)
else n
}
primeOrDone(0)
}

val ab = for{
a <- (-999 to 999)
b <- (-999 to 999)
c = countPrimes(a,b)
if(c >= 40)
}yield (a,b,c)

ab.maxBy(s => s._3)

val x = -61 * 971
*/

/*
val ps = primes

def isTruncatablePrime(p: BigInt): Boolean = {
def isLeftTruncatablePrime(p: BigInt): Boolean = {
if(p < 10) p.isPrime
else p.isPrime && isLeftTruncatablePrime(p.digits.tail)
}
def isRightTruncatablePrime(p: BigInt): Boolean = {
if(p < 10) p.isPrime
else p.isPrime && isRightTruncatablePrime(p.digits.dropRight(1))
}
isLeftTruncatablePrime(p) && isRightTruncatablePrime(p)
}
ps.filter(isTruncatablePrime(_)).take(15).toList.sortWith(_ < _).drop(4).sum
	*//*
	----------------------
	distinct prime factors
	----------------------
		
	def has4DistinctPrimeFactors(n: BigInt): Boolean = devisors(n).filter(_.isPrime).length == 4

	def consecutive4DistinctPrimeFactorsStream(n: BigInt, counter: Int): Stream[(BigInt,BigInt,BigInt,BigInt)] = {
		if(counter == 3 && has4DistinctPrimeFactors(n)) (n,n-1,n-2,n-3) #:: consecutive4DistinctPrimeFactorsStream(n + 1, 3)
		else if(has4DistinctPrimeFactors(n)) consecutive4DistinctPrimeFactorsStream(n + 1, counter + 1)
		else consecutive4DistinctPrimeFactorsStream(n + 1, 0)
	}
	
	//val s = consecutive4DistinctPrimeFactorsStream(2,0)//(134046,134045,134044,134043)
	
  */
  
  
	/*
	----------------
	digit factorials
	----------------

	val m = (0 to 9).map(i => factorial(i))
	def digitFactorials(n: BigInt): Stream[BigInt] = {
		if (n >= m(9)*7) {
			Stream.Empty
		} else if (n == n.digits.map { m(_) }.foldLeft(BigInt(0))((a, b) => a + b)) {
			n #:: digitFactorials(n + 1)
		} else {
			digitFactorials(n + 1)
		}
	}
	
	val s = digitFactorials(3)
	s.sum
	*/

	/*
	------------------
	Square Digit Chain
	------------------
	
	val m = collection.mutable.Map[Int, Boolean](89 -> true, 1 -> false)

	9 * 9 * 6

	def markInMapSquareDigitChainIteration(n: Int): Boolean = {
		if (m.contains(n)) m(n)
		else {
			val b = markInMapSquareDigitChainIteration(n.digits.map(i => i * i).sum)
			m.update(n, b)
			b
		}
	}

	def squareDigitChainIteration(n: Int): Boolean = {
	if(n > 486) markInMapSquareDigitChainIteration(n.digits.map(i => i * i).sum)
	else markInMapSquareDigitChainIteration(n)
	}
	
  (1 until 10000000).foldLeft(0)((a,b) => {
  if(squareDigitChainIteration(b)) a + 1
  else a
  })
  */
  
	/*
	----------------
	Counting Sundays
	----------------
	
	abstract class Month(onYear: Int) {
		def nextFirstOfTheMonth: Int
		def nextMonth: Month
	}
	abstract class ThirtyDaysMonth(firstOfTheMonthWeekDay: Int, onYear: Int) extends Month(onYear) {
		def nextFirstOfTheMonth = ((firstOfTheMonthWeekDay + 30) % 7) + 1
	}
	abstract class ThirtyOneDaysMonth(firstOfTheMonthWeekDay: Int, onYear: Int) extends Month(onYear) {
		def nextFirstOfTheMonth = ((firstOfTheMonthWeekDay + 31) % 7) + 1
	}
	case class January(firstOfTheMonthWeekDay: Int, onYear: Int) extends ThirtyOneDaysMonth(firstOfTheMonthWeekDay, onYear) {
		def nextMonth = new February(nextFirstOfTheMonth, onYear)
	}
	case class February(firstOfTheMonthWeekDay: Int, onYear: Int) extends Month(onYear) {
		def nextFirstOfTheMonth = {
			val numOfDays =
				if (onYear % 4 == 0) {
					if (onYear % 100 == 0) {
						if (onYear % 400 == 0) 29
						else 28
					} else 29
				} else 28
			((firstOfTheMonthWeekDay + numOfDays) % 7) + 1
		}

		def nextMonth = new Mars(nextFirstOfTheMonth, onYear)
	}
	case class Mars(firstOfTheMonthWeekDay: Int, onYear: Int) extends ThirtyOneDaysMonth(firstOfTheMonthWeekDay, onYear) {
		def nextMonth = new April(nextFirstOfTheMonth, onYear)
	}
	case class April(firstOfTheMonthWeekDay: Int, onYear: Int) extends ThirtyDaysMonth(firstOfTheMonthWeekDay, onYear) {
		def nextMonth = new May(nextFirstOfTheMonth, onYear)
	}
	case class May(firstOfTheMonthWeekDay: Int, onYear: Int) extends ThirtyOneDaysMonth(firstOfTheMonthWeekDay, onYear) {
		def nextMonth = new June(nextFirstOfTheMonth, onYear)
	}
	case class June(firstOfTheMonthWeekDay: Int, onYear: Int) extends ThirtyDaysMonth(firstOfTheMonthWeekDay, onYear) {
		def nextMonth = new July(nextFirstOfTheMonth, onYear)
	}
	case class July(firstOfTheMonthWeekDay: Int, onYear: Int) extends ThirtyOneDaysMonth(firstOfTheMonthWeekDay, onYear) {
		def nextMonth = new August(nextFirstOfTheMonth, onYear)
	}
	case class August(firstOfTheMonthWeekDay: Int, onYear: Int) extends ThirtyOneDaysMonth(firstOfTheMonthWeekDay, onYear) {
		def nextMonth = new September(nextFirstOfTheMonth, onYear)
	}
	case class September(firstOfTheMonthWeekDay: Int, onYear: Int) extends ThirtyDaysMonth(firstOfTheMonthWeekDay, onYear) {
		def nextMonth = new October(nextFirstOfTheMonth, onYear)
	}
	case class October(firstOfTheMonthWeekDay: Int, onYear: Int) extends ThirtyOneDaysMonth(firstOfTheMonthWeekDay, onYear) {
		def nextMonth = new November(nextFirstOfTheMonth, onYear)
	}
	case class November(firstOfTheMonthWeekDay: Int, onYear: Int) extends ThirtyDaysMonth(firstOfTheMonthWeekDay, onYear) {
		def nextMonth = new December(nextFirstOfTheMonth, onYear)
	}
	case class December(firstOfTheMonthWeekDay: Int, onYear: Int) extends ThirtyOneDaysMonth(firstOfTheMonthWeekDay, onYear) {
		def nextMonth = new January(nextFirstOfTheMonth, onYear + 1)
	}

	def countSundays(month: Month, count: Int): Int = month match {
		case January(_, 2001) => count
		case _ => {
			if (month.nextFirstOfTheMonth == 1) countSundays(month.nextMonth, count + 1)
			else countSundays(month.nextMonth, count)
		}
	}
	val j = new January(2, 1900)

	def getJanuary(month: Month): Month = month match {
		case January(_, 1901) => month
		case _ => getJanuary(month.nextMonth)
	}
	val days = countSundays(getJanuary(j.nextMonth), 0)
                                                  getJanuary(j.nextMonth)

	println(days)
	
	*/

	/*
	----------------------
	Goldbach Decomposition
	----------------------

	def goldbachDecomposition(n: BigInt): Option[(BigInt, BigInt)] = {
		(BigInt(1) to bigSqrtApprox(n)).find(i => {
			val remainder = n - (2 * i * i)
			remainder > 0 && Number(remainder).isPrime
		}) match {
			case None => None
			case Some(x) => Some((n - (x * x), x))
		}
	}

	def oddsStream: Stream[BigInt] = {
			def oddsStreamGenerator(n: BigInt): Stream[BigInt] = goldbachDecomposition(n) match {
				case None => Stream(n)
				case Some(x) => oddsStreamGenerator(n + 2)
			}
		BigInt(3) #:: oddsStreamGenerator(5)
	}

	val odd = 100
	primes.takeWhile(_ < odd).toList.map(p => (p, (odd - p) / 2)).filter {
		case (p, s) => (s == 1) || Number(p).isPrime || (bigSqrtExact(s) match {
			case None => false //0
			case Some(x) => true //x
		})
	}
	
	*/
}