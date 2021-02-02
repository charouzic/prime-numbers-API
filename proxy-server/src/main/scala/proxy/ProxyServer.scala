package proxy

import akka.NotUsed
import akka.http.scaladsl.marshalling.{Marshaller, Marshalling}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.stream.scaladsl.Source
import akka.util.ByteString
import prime.{PrimeRequest, PrimeResponse}

trait ProxyServer extends HttpApp with PrimeClient {

  val startResponse: String = "Hello and welcome to prime number sequence API! Please use /prime/{number} to get the sequence"
  val wrongInputResponse: String = "Seems like you are trying to input wrong number (< 1) or a string. No prime number sequence for you!"
  val basicResponse: String = "You can get a prime number sequence by going to /prime/{number}"

  implicit val numberMarshaller: Marshaller[PrimeResponse, ByteString] = Marshaller.strict[PrimeResponse, ByteString] { t =>
    Marshalling.WithFixedContentType(ContentTypes.`text/html(UTF-8)`, () => ByteString.fromString(t.message.toString))
  }

  implicit val CommaSeparatedEntityStreamingSupport: CommaSeparatedEntityStreamingSupport = new CommaSeparatedEntityStreamingSupport(1024)

  override def routes: Route =
    concat(
      path("") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, startResponse))
        }
      },
      path("prime") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, basicResponse))
        }
      },
      path("prime" / IntNumber) { i =>
        get {
          if (i > 1) {
            val responseStream: Source[PrimeResponse, NotUsed] = client.streamSequence(PrimeRequest(i))
            complete(responseStream)
          }
          else {
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, wrongInputResponse))
          }
        }
      },
      path("prime" / Remaining) { _ =>
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, wrongInputResponse))
        }
      },
      path(Remaining) { _ =>
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, basicResponse))
        }
      }
    )
}
