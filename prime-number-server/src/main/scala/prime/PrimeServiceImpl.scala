package prime

import akka.NotUsed
import akka.stream.scaladsl.Source

class PrimeServiceImpl extends PrimeService {
  override def streamSequence(in: PrimeRequest): Source[PrimeResponse, NotUsed] = {
    lazy val primes: LazyList[Int] = 2 #:: LazyList.from(3).takeWhile(_ <= in.target).filter(i =>
      primes.takeWhile{p => p * p <= i}.forall{ p => i % p > 0})
    Source(primes).map(value => PrimeResponse(value))
  }
}
