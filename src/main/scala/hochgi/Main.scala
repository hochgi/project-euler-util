package hochgi

import hochgi.math._

import scala.util.control.Breaks.{break, breakable}

object Main extends App {


  println(problem56)

  def problem56: Int = (for {
    a <- Iterator.range(1, 100)
    b <- Iterator.range(1, 100)
  } yield bigint2digits(math.numberPow(a, b)).sum).max

  def problem55: Int = {
    def reverseAndAdd(i: BigInt): BigInt = i + digits2BigInt(bigint2digits(i).reverse)

    def isLychrel(i: BigInt, iterationsLeft: Int): Boolean = (iterationsLeft == 0) || {
      val j = reverseAndAdd(i)
      if (isPalindrom(bigint2digits(j))) false
      else isLychrel(j, iterationsLeft - 1)
    }

    (10000 to 0 by -1).foldLeft(Set.empty[Int] -> 0) { case ((s0, count), i) =>
      if(s0(i)) (s0 + i, count)
      else {
        val r = isLychrel(i, 50)
        if (r) (s0, count + 1)
        else (s0 + i, count)
      }
    }
  }._2

  def problem53: Int = {
    (for {
      n <- 10 to 100
      r <- (0 to n).find(k => binom(n,k) >= 1000000)
    } yield n + 1 - (2*r)).sum
  }

  def problem52: Option[Int] = Iterator.from(1).find { i =>
    val i2 = int2digits(i * 2).sorted
    val i3 = int2digits(i * 3).sorted
    val i4 = int2digits(i * 4).sorted
    val i5 = int2digits(i * 5).sorted
    val i6 = int2digits(i * 6).sorted
    i2 == i3 &&
    i3 == i4 &&
    i4 == i5 &&
    i5 == i6
  }

  def problem51: Option[Seq[Int]] = {

    (for {
      numDigits <- Iterator.range(2, 10)
      wildCards <- numDigits to 1 by -1
      locations <- digitMask(numDigits, wildCards)
      baseCount <- scala.math.pow(10, numDigits - wildCards - 1).toInt until scala.math.pow(10, numDigits - wildCards).toInt
    } yield {
      (0 to 9).map { digit =>
        val it = int2digits(baseCount).reverseIterator
        val digits = locations.foldRight(List.empty[Int]) { case (b, digitsTail) =>
          (if (b) digit else it.next()) :: digitsTail
        }
        digits2Int(digits)
      }.filter { n =>
        new Number(n).isPrime
      }
    }).find(_.length > 7)
  }


  def problem46: Option[BigInt] = {

    def checkOddComposite(oddComposite: BigInt): Boolean = {
      val allPossibleSumsWithSmallerPrimes: Seq[Boolean] = for {
        smallerPrime <- primes.takeWhile(oddComposite.>)
        squareCandid = (oddComposite - smallerPrime) / 2
      } yield bigSqrtExact(squareCandid).isDefined
      !allPossibleSumsWithSmallerPrimes.exists(identity)
    }

    primes
      .tail         // skip 2
      .sliding(2,1) // range between 2 consecutive primes
      .flatMap(twoConsecutivePrimes => (twoConsecutivePrimes.head + 2) until twoConsecutivePrimes.last by 2)
      .find { oddComposite => checkOddComposite(oddComposite) }
  }

  // not efficient!!!
  // Some(5482660)
  def problem44(max: Int): Option[Int] = {
    val pentagonals = Array.tabulate(max)(n => (n + 1) * (3 * (n + 1) - 1) / 2)
    val pSet = Set.from(pentagonals)
    // iterate from smallest to largest
    pentagonals.find { p =>
      (for {
        i <- 0 until (max - 1)
        j <- (i + 1) until max
        if pentagonals(j) - pentagonals(i) == p
        if pSet(pentagonals(j) + pentagonals(i))
      } yield p).nonEmpty
    }
  }

  def problem43: BigInt = {
    val first7Primes = List(2, 3, 5, 7, 11, 13, 17)
    def pandigitsAreSubStringDivisible(pandigits: Seq[Int]): Boolean = pandigits
      .tail
      .sliding(3,1)
      .zip(first7Primes)
      .forall { case (threeDigits, prime) => digits2Int(threeDigits) % prime == 0 }

    (0 to 9)
      .permutations
      .collect { case pandigits if pandigitsAreSubStringDivisible(pandigits) => digits2BigInt(pandigits.toList) }
      .sum
  }

  def problem39 = (1 to 1000).maxBy { perimeter =>
    (for {
      a <- 1 until (perimeter / 2)
      b <- (a + 1) until perimeter
      c = perimeter - (a + b)
      if a * a + b * b == c * c
    } yield 1).length
  }

  def problem33 = {
    def nonTrivialCanceling(n: Int, d: Int): Boolean = {
      val nDec = n / 10
      val nOne = n % 10
      val dDec = d / 10
      val dOne = d % 10
      val cancelingTuples = List((nDec,dDec), (nDec,dOne), (nOne,dDec), (nOne,dOne))
      cancelingTuples.exists(t => t._1 == t._2) && {
        val cancelingOptions = cancelingTuples.filterNot(_._2 == 0).map { case (cn, cd) => new Rational(cn, cd) }
        val realFraction = new Rational(n, d)
        cancelingOptions.exists(realFraction.equals)
      }
    }
    val all = for {
      n <- 10 to 98
      d <- (n + 1) to 99
      if nonTrivialCanceling(n,d)
    } yield s"$n/$d"

    // fast ugly solution
    println(all.groupBy(_.last).values.map(_.mkString(",")).mkString("\n"))

    /*

24/48,49/98
12/24,16/64,42/84
13/39,46/69
15/75,19/95,26/65
16/96,64/96
10/20,10/30,10/40,10/50,10/60,10/70,10/80,10/90,20/30,20/40,20/50,20/60,20/70,20/80,20/90,30/40,30/50,30/60,30/70,30/80,30/90,40/50,40/60,40/70,40/80,40/90,50/60,50/70,50/80,50/90,60/70,60/80,60/90,70/80,70/90,80/90
21/42
31/93

    49/98, 16/64, 19/95, 26/65

    98 * 64 * 95 * 65 = 38729600
     */
    (new Rational(49,98) * new Rational(16,64) * new Rational(19,95) * new Rational(26,65)).denom
  }

  def problem50: BigInt = {
    val threshold = 1000000
    var max: (BigInt, Int) = BigInt(2) -> 1

    breakable {
      primes
        .takeWhile(BigInt(threshold).>=)
        .foldLeft(List.empty[(BigInt, Int)]) { (listOfSumsAndCounts, prime) =>

          // no point in starting a sum that cannot be both longer than current max,
          // and under the threshold.
          val dontStartSumStartingWithPrime = (prime * max._2) > threshold

          if (dontStartSumStartingWithPrime && listOfSumsAndCounts.isEmpty) break()
          else {
            val z = if (dontStartSumStartingWithPrime) Nil else List(prime -> 1)
            listOfSumsAndCounts.foldRight(z) { case ((sumOfNMinus1, count), newListOfSums) =>
              val newSum = prime + sumOfNMinus1
              if (newSum > threshold) newListOfSums
              else {
                val newCount = count + 1
                if (newCount > max._2 && Number(newSum).isPrime) {
                  max = (newSum -> newCount)
//                  println(s"sum[${newSum}]\tof[${newCount}] primes, ending with[${prime}] is prime!")
                }
                (newSum, newCount) :: newListOfSums
              }
            }
          }
        }
    }

    max._1
  }
}
