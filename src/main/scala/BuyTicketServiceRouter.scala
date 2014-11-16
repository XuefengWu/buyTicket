import java.util.UUID

import actors.ApiRouter
import akka.actor.ActorSystem
import akka.http.model.HttpMethods._
import akka.http.model._
import akka.http.model.headers.RawHeader
import akka.pattern.ask
import akka.util.Timeout
import messages.{ApiPostRequest, ApiGetRequest}

import scala.collection.immutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.Source
import scala.util.{Failure, Success, Try}

object BuyTicketServiceRouter {

  val system = ActorSystem("BuyTicketServiceRouter")
  val apiRouter = system.actorOf(ApiRouter.props)
  implicit val askTimeout: Timeout = 1500.millis

  def apply():HttpRequest ⇒ Future[HttpResponse] = {
    case HttpRequest(GET, Uri.Path("/"), rHeaders, _, _) ⇒
      Future(HttpResponse(StatusCodes.PermanentRedirect,
        headers = List(headers.Location(s"http://${rHeaders.find(_.name() == "Host").map(_.value()).get}/index.html"))))
    case HttpRequest(method, uri, headers, entity, _) ⇒ routDynamic(method,uri, headers, entity)
  }

  private def routDynamic(method: HttpMethod, uri: Uri, headers: immutable.Seq[HttpHeader], entity: RequestEntity): Future[HttpResponse] = {
    uri.path.toString() match {
      case apiPath if apiPath.startsWith("/api") => handleApi(method, apiPath.drop(5), entity, headers.find(_.name() == "token").map(_.value()))
      case path => handleStatic(path)
    }
  }

  private def handleApi(method: HttpMethod, api: String, entity: RequestEntity, token: Option[String]): Future[HttpResponse] = {
    val _token: Option[UUID] = token.map(UUID.fromString).orElse(Some(UUID.randomUUID()))
    val responseF = method match {
      case GET => (apiRouter ? ApiGetRequest(api, _token))
      case POST => (apiRouter ? ApiPostRequest(api, entity.dataBytes, _token))
    }
    responseF.map(response => HttpResponse(entity = HttpEntity(MediaTypes.`application/json`, response.toString),
                  headers = List(RawHeader("token", _token.map(_.toString).get))))
  }

  private def handleStatic(path: String) : Future[HttpResponse]= {
    getFromResourceDirectory(path).map {
      case Success(resource) => HttpResponse(entity = HttpEntity(MediaTypes.`text/html`, resource))
      case Failure(e) => HttpResponse(StatusCodes.NotFound, entity = HttpEntity(MediaTypes.`text/html`, e.toString))
    }
  }

  private def getFromResourceDirectory(path: String): Future[Try[String]] = {
    Future(Try(getClass.getClassLoader.getResourceAsStream(s"web/${path}")).map(resource => Source.fromInputStream(resource).mkString))
  }

}
