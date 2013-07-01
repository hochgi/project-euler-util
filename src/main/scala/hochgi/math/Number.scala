package hochgi.math

import scala.math.pow
import scala.math.Numeric

class Number(private val n: BigInt) extends Numeric[Number] {

	def this(n: Int) = this(BigInt(n))

	def getBigInt = n

	def isPrime: Boolean = {
		if(Number.pMap.contains(n)) Number.pMap(n)
		else{
			val rv = (n == 2) ||
				{
					(n > 2) &&
					(n % 2 != 0) &&
					(primes.takeWhile(a => ((a * a) <= n)).forall(i => n % i != 0))
				}
			Number.pMap.update(n, rv)
			rv
		}
	}
	
	def digits: List[Int] = {
			def digitsHelper(i: BigInt): List[Int] = {
				if (i < 10) List(i.toInt)
				else {
					val remainder = (i % 10).toInt
					remainder :: digitsHelper((i - remainder)/10)
				}
			}
		digitsHelper(n).reverse
	}

	def devisors: IndexedSeq[Number] = (BigInt(1) to (n/2)).filter(n % _ == 0).map(Number(_))

	def %(i: Number) = Number(n % i.getBigInt)
	
	//Numeric methods:
	def plus(x: Number, y: Number): Number = Number(x.n + y.n)
	def minus(x: Number, y: Number): Number = Number(x.n - y.n)
	def times(x: Number, y: Number): Number = Number(x.n * y.n)
	def negate(x: Number): Number = Number(-x.n)
	def fromInt(x: Int): Number = Number(x)
	def toInt(x: Number): Int = x.n.toInt
	def toLong(x: Number): Long = x.n.toLong
	def toFloat(x: Number): Float = x.n.toFloat
	def toDouble(x: Number): Double = x.n.toDouble
	def compare(x: Number, y: Number): Int = scala.math.Ordering.BigInt.compare(x.n, y.n)
	
	//overrides:
	override def toString: String = n.toString
}

object Number {
	private lazy val pMap = scala.collection.mutable.Map[BigInt,Boolean]()

	def apply(n: BigInt) = new Number(n) 
	
//	def primes: Stream[Number] = {
//		
//		def primeStreamGenerator(current: Number, streamSoFar: Stream[Number]): Stream[Number] = {
//			if(streamSoFar.forall(i => current % i != 0)) current #:: primeStreamGenerator(Number(current.n + 1), current #:: streamSoFar)
//			else primeStreamGenerator(Number(current.n + 1), streamSoFar)
//		}
//		
//		primeStreamGenerator(2, Stream.Empty)
//	}
	
}