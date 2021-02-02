package proxy

import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.scaladsl.Source
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import prime.{PrimeResponse, PrimeService}

class ProxyServerSpec extends AnyFlatSpec with Matchers with ScalatestRouteTest with MockFactory{

  val clientStub = stub[PrimeService]
  val primeSeq: List[PrimeResponse] = Seq(2, 3, 5, 7, 11, 13, 17).map(value => new PrimeResponse(value)).toList

  // defining stub
  (clientStub.streamSequence _).when(*).returns(Source(primeSeq))

  trait TestPrimeClient extends PrimeClient{
    override protected def client: PrimeService = clientStub
  }

  object ProxyServerUT extends ProxyServer with TestPrimeClient {
  }

  "WebServer" should "work" in {
    Get() ~> ProxyServerUT.routes ~> check {
      responseAs[String] shouldEqual ProxyServerUT.startResponse
    }

    Get("/prime/17") ~> ProxyServerUT.routes ~> check {
      responseAs[String] shouldEqual "2,3,5,7,11,13,17."
    }

    Get("/prime/dixa") ~> ProxyServerUT.routes ~> check {
      responseAs[String] shouldEqual ProxyServerUT.wrongInputResponse
    }

    Get("/prime/0") ~> ProxyServerUT.routes ~> check {
      responseAs[String] shouldEqual ProxyServerUT.wrongInputResponse
    }

    Get("/ThisGuyIsGoodForJuniorDev") ~> ProxyServerUT.routes ~> check {
      responseAs[String] shouldEqual ProxyServerUT.basicResponse
    }
  }
}