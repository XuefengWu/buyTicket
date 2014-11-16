package actors

import java.util.UUID

import akka.actor.Props
import akka.persistence.PersistentActor
import db.MyDao
import messages.{Boom, TicketAdded, AddTickets, Buy}
import models.Ticket
import util.JsonConvert


object ShoppingCar {
  def props(id: UUID) = Props(new ShoppingCar(id))
}

class ShoppingCar(id: UUID) extends PersistentActor with JsonConvert {

  override def persistenceId: String = s"shopping-${id.toString}"

  private val car = scala.collection.mutable.HashSet[Ticket]()

  private def update(event: TicketAdded): Unit = {
    val tickets = event.uuids.map(id => MyDao.get(id)).filter(_.isDefined).map(_.get)
    car ++= tickets
  }

  override def receiveRecover: Receive = {
    case added: TicketAdded  => update(added)
  }

  override def receiveCommand: Receive = {
    case AddTickets(ids: Seq[UUID], requester) =>
      ///oooklkl
      persistAsync(TicketAdded(ids)) { event =>
        update(event)
        requester ! car.size
      }
    case Boom(requester) =>
      requester ! "you, killer!"
      throw new Exception("I am die")
    case Buy(requester) =>
      persistAsync(s"bought") { event =>
        val size = car.map(_.id).map(id => MyDao.delete(id)).filter(deleted => deleted).size
        car.clear()
        requester ! size
      }
  }


}