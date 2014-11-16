package messages

import java.util.UUID

import akka.actor.ActorRef
import akka.stream.scaladsl2.Source
import akka.util.ByteString
import org.joda.time.DateTime

case class ApiGetRequest(path: String,token: Option[UUID])

case class ApiPostRequest(path: String, dataBytes: Source[ByteString],token: Option[UUID])

//commands
case class QueryRequest(date: DateTime, requester: ActorRef)

case class AddTickets(uuids: Seq[UUID], requester: ActorRef)

case class Boom(requester: ActorRef)

case class Buy(requester: ActorRef)

case class LookSelf(requester: ActorRef)

case class InitDB(num: Int, requester: ActorRef)

//events
case class TicketAdded(uuids: Seq[UUID])