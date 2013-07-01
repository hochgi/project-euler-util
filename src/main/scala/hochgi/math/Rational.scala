package hochgi.math

import scala.math.Numeric
import sun.reflect.generics.reflectiveObjects.NotImplementedException

class Rational(n: BigInt, d: BigInt) extends Numeric[Rational] {
	require(d != 0)

	private val g = n.gcd(d)
	val numer = n / g
	val denom = d / g

	def this(n: BigInt) = this(n, 1)
	def this() = this(0, 1)

	def plus(x: Rational, y: Rational): Rational = x + y
	def + (that: Rational): Rational =
		new Rational(
			numer * that.denom + that.numer * denom,
			denom * that.denom)
	def + (i: BigInt): Rational = new Rational(numer + i * denom, denom)

	def minus(x: Rational, y: Rational): Rational = x - y
	def - (that: Rational): Rational =
		new Rational(
			numer * that.denom - that.numer * denom,
			denom * that.denom)
	def - (i: BigInt): Rational = new Rational(numer - i * denom, denom)
	
	def times(x: Rational, y: Rational): Rational = x * y
	def * (that: Rational): Rational = new Rational(numer * that.numer, denom * that.denom)
	def * (i: BigInt): Rational = new Rational(numer * i, denom)
	
	def / (that: Rational): Rational = new Rational(numer * that.denom, denom * that.numer)
	def / (i: BigInt): Rational = new Rational(numer, denom * i)
	
	def negate(x: Rational): Rational = if(denom < 0) new Rational(numer, -denom) else new Rational(-numer, denom)
	def fromInt(x: Int): Rational = new Rational(x)
	def toInt(x: Rational): Int = (numer / denom).toInt
	def toLong(x: Rational): Long = (numer / denom).toLong
	def toFloat(x: Rational): Float = (numer.toDouble / denom.toDouble).toFloat
	def toDouble(x: Rational): Double = numer.toDouble / denom.toDouble
	def compare(r1: Rational, r2: Rational): Int = ((r1.numer * r2.denom) - (r2.numer * r1.denom)).toInt
	def equals(r: Rational): Boolean = numer == r.numer && denom == r.denom
	
	/**
	 * algorithm was taken from:
	 * http://oeis.org/A051626
	 * 
	 * A051626 := proc(n) local lpow, mpow ;
	 * 	if isA003592(n) then
	 * 		RETURN(0) ;
	 * 	else
	 * 		lpow:=1 ;
	 * 		while true do
	 * 			for mpow from lpow-1 to 0 by -1 do
	 * 				if (10^lpow-10^mpow) mod n =0 then
	 * 					RETURN(lpow-mpow) ;
	 * 				fi ;
	 * 			od ;
	 * 		lpow := lpow+1 ;
	 * 		od ;
	 * 	fi ;
	 * 	end:
	 * 
	 */
	def getDecimalRepresentationCycleLength: Int = {
		val pDevs: Set[BigInt] = devisors(denom).filter(_.isPrime).toSet
		if((Set(BigInt(2),BigInt(5)).intersect(pDevs) == pDevs) && !pDevs.isEmpty) 0
		else {
			var lpow = 1
			var keepRunning = true
			var rv = -1
			while(keepRunning){
				for {
					mpow <- (lpow-1) to 0 by -1
					if ((numberPow(10,lpow)-numberPow(10,mpow)) % denom == 0)
				}{
					keepRunning = false
					rv =  (lpow - mpow)
				}
				lpow = lpow + 1 
			}
			rv
		}
	}
	
	override def toString = numer + "/" + denom
}