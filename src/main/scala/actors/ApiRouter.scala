package actors

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.RoundRobinPool
import messages._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import util.JsonConvert

object ApiRouter {
  def props = Props(new ApiRouter())
}

class ApiRouter extends Actor with JsonConvert {

  private val queryActor = context.actorOf(RoundRobinPool(600).props(QueryActor.props))
  private val userCars = scala.collection.mutable.HashMap[UUID, ActorRef]()
  private val userViews = scala.collection.mutable.HashMap[UUID, ActorRef]()
  //private val queryActor = context.actorOf(QueryActor.props)
  private val dbMaintainance = context.actorOf(DBMaintainance.props)
  private val dateformat =  DateTimeFormat.forPattern("yyyy-MM-dd")
  private implicit val materializer = akka.stream.scaladsl2.FlowMaterializer()

  override def receive: Receive = {
    case ApiGetRequest(path, token) if path.startsWith("query") => queryActor ! QueryRequest(DateTime.parse(path.drop(6), dateformat), sender())
    case ApiPostRequest(path, dataBytes, token) if  path == "add" => getUserCar(token.get) ! AddTickets(toUUIDs(dataBytes)(materializer), sender())
    case ApiPostRequest(path, _, token) if  path == "buy" => getUserCar(token.get) ! Buy(sender())
    case ApiPostRequest(path, _, token) if  path == "boom" => getUserCar(token.get) ! Boom(sender())
    case ApiGetRequest(path, token) if  path == "my" => getUserView(token.get) ! LookSelf(sender())
    case ApiGetRequest(path, token) if path.startsWith("init") => dbMaintainance ! InitDB(100000, sender())
    case _ => sender() ! """{"name":"xuefeng"}"""
  }

  private def getUserCar(uid: UUID)= userCars.getOrElseUpdate(uid, context.actorOf(ShoppingCar.props(uid)))
  private def getUserView(uid: UUID)= userViews.getOrElseUpdate(uid, context.actorOf(My.props(uid)))

}
