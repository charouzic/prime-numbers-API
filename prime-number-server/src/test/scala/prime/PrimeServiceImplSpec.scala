package prime

import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import org.scalatest.matchers.should.Matchers._
import org.scalatest.prop.TableDrivenPropertyChecks._

import scala.concurrent.duration._
import scala.concurrent.Await

class PrimeServiceImplSpec extends AnyFlatSpec {
  "streamSequence" should "work as expected" in {
    implicit val sys: ActorSystem = ActorSystem("Test")
    implicit val mat: ActorMaterializer = ActorMaterializer()
    val fibonacciServiceImpl = new PrimeServiceImpl()
    val cases =
      Table(
        ("input", "expectedList"),
        (2, List(2)),
        (3, List(2, 3)),
        (5, List(2, 3, 5)),
        (7, List(2, 3, 5, 7)),
        (11, List(2, 3, 5, 7, 11)),
        (13, List(2, 3, 5, 7, 11, 13)),
        (17, List(2, 3, 5, 7, 11, 13, 17)))
    forAll(cases) { (input: Int, expected: Seq[Int]) =>
      val primeCalc = fibonacciServiceImpl.streamSequence(new PrimeRequest(input)).map(value => value.message).runWith(Sink.seq)
      val result = Await.result(primeCalc, 3.seconds)
      result should equal(expected)
    }
  }
}