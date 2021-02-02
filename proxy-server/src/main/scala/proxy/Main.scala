package proxy

import prime.PrimeService

object Main extends App{

  object ProxyServerImpl extends ProxyServer {
    override protected def client: PrimeService = GrpcModule.client
  }

  ProxyServerImpl.startServer("localhost", 9090)

}