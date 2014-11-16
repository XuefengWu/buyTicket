package actors

import java.util.UUID

import akka.actor.{Props, Actor}
import akka.persistence.PersistentView
import db.MyDao
import messages.{TicketAdded, LookSelf}
import models.Ticket
import util.JsonConvert


object My {
  def props(id: UUID) = Props(new My(id))
}

class My(id: UUID) extends PersistentView with JsonConvert {

  override def persistenceId: String = s"shopping-${id.toString}"
  override def viewId: String = s"$persistenceId-view"

  private val tickets = scala.collection.mutable.ListBuffer[Ticket]()

  def receive: Actor.Receive = {
    case TicketAdded(ids) if isPersistent => tickets ++= ids.map(id => MyDao.get(id).get)
    case payload if isPersistent =>
      println("=======payload========")
      println(payload)
      println("=======payload========")
    // handle message from journal...
    case LookSelf(requester) => requester ! toJson(tickets)
    case payload => println(payload)
    // handle message from user-land...
  }

}