import akka.actor.ActorSystem
import akka.http.Http
import akka.http.model.HttpMethods._
import akka.http.model.{HttpRequest, HttpResponse}
import akka.io.IO
import akka.pattern.ask
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.Flow
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

object BuyTicketServiceApp extends App {

  implicit val system = ActorSystem("BuyTicketServiceApp")
  implicit val materializer = FlowMaterializer()
  implicit val askTimeout: Timeout = 1500.millis

  val bindingFuture: Future[Any] = IO(Http) ? Http.Bind(interface = "localhost", port = 9000)
  bindingFuture foreach {
    case Http.ServerBinding(localAddress, connectionStream) ⇒
      Flow(connectionStream).foreach({
        case Http.IncomingConnection(remoteAddress, requestProducer, responseConsumer) ⇒
          // handle connection here
          Flow(requestProducer).mapFuture(BuyTicketServiceRouter()).produceTo(responseConsumer)
      })
  }

  def handler():HttpRequest ⇒ Future[HttpResponse] = {
    case HttpRequest(GET, _, _, _, _)  ⇒ Future(HttpResponse())
  }

  system.awaitTermination()

}
