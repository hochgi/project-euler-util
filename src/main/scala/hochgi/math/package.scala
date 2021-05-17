package hochgi

import scala.annotation._
import scala.collection.mutable.{Map => MMap}

package object math {
  //IMPLICITS:

//  /**
//   * Int to Number
//   */
//  def Int2Number(i: Int): Number = new Number(i)

//  def Number2BigInt(n: Number): BigInt = n.getBigInt

//  def BigInt2Rational(n: BigInt) = new Rational(n)

  def bigInt2Number(n: BigInt) = Number(n)

  /**
   * converts a number to it's binary representation
   * (non-negative only!)
   */
  def toBinary(n: BigInt): List[Int] = n.toInt match {
    case 0 => List(0)
    case _ => {
      def toBinaryHelper(n: BigInt): List[Int] = {
        if (n == 1) List(1)
        else (n % 2).toInt :: toBinaryHelper(n / 2)
      }
      toBinaryHelper(n).reverse
    }
  }

  def digits(n: BigInt): List[Int] = {
    require(n >= 0)
    def digitsHelper(i: BigInt): List[Int] = {
      if (i < 10) List(i.toInt)
      else {
        val remainder = (i % 10).toInt
        remainder :: digitsHelper((i - remainder) / 10)
      }
    }

    digitsHelper(n).reverse
  }

  /**
   * is the given digits list represents a palindromic number?
   */
  def isPalindrom(li: List[Int]): Boolean = li match {
    case Nil => true
    case List(_) => true
    case x :: xs => (x == xs.last) && isPalindrom(xs.reverse.tail)
  }

  /**
   * Greatest Common Devisor
   */
  def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
  def gcd(a: BigInt, b: BigInt): BigInt = if (b == 0) a else gcd(b, a % b)
  def gcd(a: Number, b: Number): Number = if (b.getBigInt == 0) a else gcd(b, a % b)

  /**
   * List(1,2,3,4) => BigInt(1*10^3^ + 2*10^2^ + 3*10^1^ + 4*10^0^)
   */
  def digits2BigInt(l: List[Int]): BigInt = {

    @tailrec
    def digits2BigIntHelper(accu: List[BigInt], remainingDigits: List[Int]): List[BigInt] = remainingDigits match {
      case Nil => accu
      case x :: xs => digits2BigIntHelper(x :: (accu map (_ * 10)), xs)
    }

    if (l == Nil) BigInt(0)
    else digits2BigIntHelper(BigInt(l.head) :: Nil, l.tail).sum
  }

  /**
   * List(1,2,3,4) => BigInt(1*10^3^ + 2*10^2^ + 3*10^1^ + 4*10^0^)
   */
  def digits2Int(l: Seq[Int]): Int = l.foldRight(0 -> 1) { case (dgt, (sum, mul)) => (sum + dgt * mul, mul * 10) }._1

  def int2digits(i: Int, acc: List[Int] = Nil): List[Int] = {
    if(i == 0) {
      if(acc.nonEmpty) acc
      else List(0)
    }
    else int2digits(i / 10, (i % 10) :: acc)
  }

  /**
   * n!
   */
  //def factorial(n: Number): Number = new Number(factorial(n.getBigInt))
  def factorial(n: BigInt): BigInt = {

    @tailrec
    def fHelper(n: BigInt, acc: BigInt): BigInt = {
      if (n <= 1) acc
      else fHelper(n - 1, acc * n)
    }

    fHelper(n, 1)
  }




  val binom10: Map[(Int,Int), Int] = {

    def binom(n: Int, k: Int, acc: Map[(Int, Int), Int]): Map[(Int, Int), Int] =
      acc.updated(n -> k, {
        if (n == k || k == 0) 1
        else acc(n - 1, k) + acc(n - 1, k - 1)
      })

    (0 to 10).foldLeft(Map.empty[(Int, Int), Int]) { case (accn, n) =>
      (0 to n).foldLeft(accn) { case (acck, k) =>
        binom(n, k, acck)
      }
    }
  }

  private[this] val binomap: MMap[(Int,Int), Int] = MMap.from(binom10)

  def binom(n: Int, k: Int): Int = {
    if(n == k || k == 0) 1
    else binomap.applyOrElse(n -> k, (_: (Int, Int)) => {
      val r = binom(n - 1, k - 1) + binom(n - 1, k)
      binomap.update(n -> k, r)
      r
    })
  }

  def digitMask(n: Int, k: Int): Iterator[List[Boolean]] = {
    if(n == k)      Iterator.single(List.fill(n)(true))
    else if(k == 0) Iterator.single(List.fill(n)(false))
    else new Iterator[List[Boolean]] {

      private[this] val hs = digitMask(n - 1, k).map(false :: _)
      private[this] val ts = digitMask(n - 1, k - 1).map(true :: _)

      override def hasNext: Boolean = ts.hasNext

      override def next(): List[Boolean] = hs.nextOption().getOrElse(ts.next())
    }
  }


  /**
   * returns rotations of the number.
   * 1324 => List(1324,3241,2413,4132)
   */
  def circularNumbers(n: Number): List[BigInt] = {

    @tailrec
    def circularNumbersHelper(accu: List[BigInt], hd: List[Int], tl: List[Int]): List[BigInt] = tl match {
      case Nil => accu
      case x :: xs => circularNumbersHelper(digits2BigInt(tl ::: hd) :: accu, hd ::: List(x), xs)
    }

    circularNumbersHelper(Nil, Nil, n.digits)
  }

  /**
   * a^b
   */
  def numberPow(a: BigInt, b: BigInt): BigInt = {

    @tailrec
    def numberPowHelper(accu: BigInt, base: BigInt, togo: BigInt): BigInt = togo match {
      case i if (i < 1) => accu
      case _ => numberPowHelper(accu * base, base, togo - 1)
    }

    numberPowHelper(1, a, b)
  }

  /**
   * returns a stream of all prime numbers
   */
  val primes: LazyList[BigInt] = {

    def primeStreamGenerator(current: BigInt, streamSoFar: LazyList[BigInt]): LazyList[BigInt] = {
      if (streamSoFar.forall(i => current % i != 0)) current #:: primeStreamGenerator(current + 2, current #:: streamSoFar)
      else primeStreamGenerator(current + 2, streamSoFar)
    }

    2 #:: primeStreamGenerator(3, LazyList.empty)
  }

  def devisors(n: BigInt): IndexedSeq[BigInt] = (BigInt(1) to (n / 2)).filter(n % _ == 0)

  def isPandigital(number: Number): Boolean = isPandigital(number.digits)
  def isPandigital(digits: List[Int]): Boolean = {
    val i = digits.length
    require(i > 0 && i < 10)
    (1 to i).forall(digits.contains(_)) && digits.forall(j => digits.count(_ == j) == 1)
  }

  def isKPandigital(number: Number, k: Int): Boolean = number.digits.length == k && isPandigital(number.digits)
  def isKPandigital(digits: List[Int], k: Int): Boolean = digits.length == k && isPandigital(digits)

  def isPandigitalProduct(n: BigInt): Boolean = devisors(n).exists { d =>
    val dl = Number(d).digits ::: Number(n / d).digits ::: Number(n).digits
    isPandigital(dl)
  }

  def isKPandigitalProduct(n: BigInt, k: Int): Boolean = devisors(n).exists { d =>
    val dl = Number(d).digits ::: Number(n / d).digits ::: Number(n).digits
    dl.length == k && isPandigital(dl)
  }

  def pandigitalProductStream: LazyList[Int] = {

    def panParoductStreamGenerator(n: Int): LazyList[Int] = {
      if (isKPandigitalProduct(n, 9)) n #:: panParoductStreamGenerator(n + 1)
      else if (n > 10000) LazyList.empty
      else panParoductStreamGenerator(n + 1)
    }

    panParoductStreamGenerator(2)
  }

  def permutationStream[T](l: List[T], f: List[T] => List[T] = (x: List[T]) => x): LazyList[List[T]] = l match {
    case x :: Nil => LazyList(f(List(x)))
    case _ => mergeListOfStreams(l map { x =>
      {
        val index = l.indexOf(x)
        val ls = ((a: (List[T], List[T])) => a._1 ::: a._2.tail)(l.splitAt(index))
        val g = (someList: List[T]) => x :: someList
        permutationStream(ls, g andThen f)
      }
    })
  }

  def mergeListOfStreams[T](listOfStreams: List[LazyList[T]]): LazyList[T] = listOfStreams match {
    case Nil => LazyList.empty
    case x :: Nil => x
    case x :: xs => {
      def recHelper(s: LazyList[T], los: List[LazyList[T]]): LazyList[T] = (s, los) match {
        case (lls, Nil) if lls.isEmpty => lls
        case (lls, _) if lls.isEmpty => recHelper(los.head, los.tail)
        case (_, _) => s.head #:: recHelper(s.tail, los)
      }
      recHelper(x, xs)
    }
  }

  def bigSqrtApprox(n: BigInt): BigInt = (BigInt(1) to n).find(i => i * i >= n) match {
    case None => throw new RuntimeException("could not find sqrt - did you entered a negative value?")
    case Some(x) => x
  }
  def bigSqrtExact(n: BigInt): Option[BigInt] = {
    val sqrCandid = (BigInt(1) to n).dropWhile(i => i * i < n).head
    Option.when(sqrCandid * sqrCandid == n)(sqrCandid)
  }

  def permutations(n: Int): LazyList[List[Int]] = {
    def permHelper(availableDigits: List[Int]): LazyList[List[Int]] = availableDigits match {
      case Nil => LazyList(Nil)
      case l: List[Int] => {

        val y = for {
          i <- l.indices
          x = l(i)
          xs = l.dropRight(l.length - i) ::: l.drop(i + 1)
        } yield permHelper(xs).map(ys => x :: ys)

        var acc: LazyList[List[Int]] = LazyList.empty
        val it = y.iterator
        while (it.hasNext) {
          acc = acc ++ it.next()
        }

        acc
      }
    }
    require((n < 10) && (n > 0))
    permHelper((1 to n).toList)
  }

  def pentagonals: LazyList[Int] = {
    def recHelper(n: Int): LazyList[Int] = {
      ((n*(3*n - 1))/2) #:: recHelper(n+1)
    }
    recHelper(1)
  }
  
  def triangles: LazyList[Int] = {
	  def recHelper(n: Int): LazyList[Int] = {
	 	  (n*(n+1)/2) #:: recHelper(n+1)
	  }
	  recHelper(1)
  }
}