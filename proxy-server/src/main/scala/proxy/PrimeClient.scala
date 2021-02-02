package proxy

import prime.PrimeService

trait PrimeClient {
  protected def client: PrimeService
}