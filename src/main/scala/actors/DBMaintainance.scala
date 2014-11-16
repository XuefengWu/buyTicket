package actors

import java.util.UUID

import akka.actor.{Actor, Props}
import db.MyDao
import messages.InitDB
import models.Ticket
import org.joda.time.DateTime
import util.JsonConvert


object DBMaintainance {
  def props = Props(new DBMaintainance())
}

class DBMaintainance extends Actor with JsonConvert {

  override def receive: Receive = {
    case InitDB(sum, requester) =>
      (0 to sum).foreach(i => MyDao.put(Ticket(UUID.randomUUID(), new DateTime(), s"train${i / 100}", s"site$i")))
      requester ! sum
  }

}
