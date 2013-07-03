package hochgi

import scala.annotation._

package object math {
  //IMPLICITS:

  /**
   * Int to Number
   */
  implicit def Int2Number(i: Int): Number = new Number(i)

  implicit def Number2BigInt(n: Number): BigInt = n.getBigInt

  implicit def BigInt2Rational(n: BigInt) = new Rational(n)

  implicit def BigInt2Number(n: BigInt) = Number(n)

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
   * List(1,2,3,4) => BigInt(1*10^3 + 2*10^2 + 3*10^1 + 4*10^0)
   */
  implicit def digits2BigInt(l: List[Int]): BigInt = {

    @tailrec
    def digits2BigIntHelper(accu: List[BigInt], remainingDigits: List[Int]): List[BigInt] = remainingDigits match {
      case Nil => accu
      case x :: xs => digits2BigIntHelper(x :: (accu map (_ * 10)), xs)
    }

    if (l == Nil) BigInt(0)
    else digits2BigIntHelper(BigInt(l.head) :: Nil, l.tail) sum
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

  /**
   * returns rotations of the number.
   * 1324 => List(1324,3241,2413,4132)
   */
  def circularNumbers(n: Number): List[BigInt] = {

    @tailrec
    def circularNumbersHelper(accu: List[BigInt], hd: List[Int], tl: List[Int]): List[BigInt] = tl match {
      case Nil => accu
      case x :: xs => circularNumbersHelper((tl ::: hd) :: accu, hd ::: List(x), xs)
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
  def primes: Stream[BigInt] = {

    def primeStreamGenerator(current: BigInt, streamSoFar: Stream[BigInt]): Stream[BigInt] = {
      if (streamSoFar.forall(i => current % i != 0)) current #:: primeStreamGenerator(current + 2, current #:: streamSoFar)
      else primeStreamGenerator(current + 2, streamSoFar)
    }

    2 #:: primeStreamGenerator(3, Stream.Empty)
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

  def pandigitalProductStream: Stream[Int] = {

    def panParoductStreamGenerator(n: Int): Stream[Int] = {
      if (isKPandigitalProduct(n, 9)) n #:: panParoductStreamGenerator(n + 1)
      else if (n > 10000) Stream.Empty
      else panParoductStreamGenerator(n + 1)
    }

    panParoductStreamGenerator(2)
  }

  def permutationStream[T](l: List[T], f: List[T] => List[T] = (x: List[T]) => x): Stream[List[T]] = l match {
    case x :: Nil => Stream(f(List(x)))
    case _ => mergeListOfStreams(l map { x =>
      {
        val index = l.indexOf(x)
        val ls = ((a: (List[T], List[T])) => a._1 ::: a._2.tail)(l.splitAt(index))
        val g = (someList: List[T]) => x :: someList
        permutationStream(ls, g andThen f)
      }
    })
  }

  def mergeListOfStreams[T](listOfStreams: List[Stream[T]]): Stream[T] = listOfStreams match {
    case Nil => Stream.Empty
    case x :: Nil => x
    case x :: xs => {
      def recHelper(s: Stream[T], los: List[Stream[T]]): Stream[T] = (s, los) match {
        case (Stream.Empty, Nil) => Stream.Empty
        case (Stream.Empty, _) => recHelper(los.head, los.tail)
        case (_, _) => s.head #:: recHelper(s.tail, los)
      }
      recHelper(x, xs)
    }
  }

  def bigSqrtApprox(n: BigInt): BigInt = (BigInt(1) to n).find(i => i * i >= n) match {
    case None => throw new RuntimeException("could not find sqrt - did you entered a negative value?")
    case Some(x) => x
  }
  def bigSqrtExact(n: BigInt): Option[BigInt] = (BigInt(1) to n).takeWhile(i => i * i <= n).find(i => i * i == n)

  def permutations(n: Int): Stream[List[Int]] = {
    def permHelper(availableDigits: List[Int]): Stream[List[Int]] = availableDigits match {
      case Nil => Stream(Nil)
      case l: List[Int] => {

        val y = for {
          i <- 0 until l.length
          x = l(i)
          xs = l.dropRight(l.length - i) ::: l.drop(i + 1)
        } yield permHelper(xs).map(ys => x :: ys)

        var acc: Stream[List[Int]] = Stream.Empty
        val it = y.iterator
        while (it.hasNext) {
          acc = acc ++ it.next
        }

        acc
      }
    }
    require((n < 10) && (n > 0))
    permHelper((1 to n).toList)
  }

  def pentagonals: Stream[Int] = {
    def recHelper(n: Int): Stream[Int] = {
      ((n*(3*n - 1))/2) #:: recHelper(n+1)
    }
    recHelper(1)
  }
}