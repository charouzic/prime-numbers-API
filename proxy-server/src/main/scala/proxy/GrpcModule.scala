package proxy


import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import prime.{PrimeService, PrimeServiceClient}

import scala.concurrent.ExecutionContextExecutor

object GrpcModule {
  implicit val sys: ActorSystem = ActorSystem("PrimeClient")
  implicit val ec: ExecutionContextExecutor = sys.dispatcher

  lazy val clientSettings: GrpcClientSettings = GrpcClientSettings
    .connectToServiceAt("localhost", 8080)
    .withTls(false)
  lazy val client: PrimeService = PrimeServiceClient(clientSettings)
}
