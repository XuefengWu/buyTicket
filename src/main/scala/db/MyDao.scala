package db

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

//import scala.collection.JavaConversions._
import models.Ticket
import org.joda.time.DateTime

object MyDao {

  private val store = new ConcurrentHashMap[UUID, Ticket]()

  def put(ticket: Ticket): Unit = store.put(ticket.id, ticket)

  def delete(id: UUID): Boolean = store.remove(id) != null

  def get(id: UUID): Option[Ticket] = Option(store.get(id))

  def query(date: DateTime, offset: Int): Seq[Ticket] = {
    val buffer = scala.collection.mutable.ListBuffer[Ticket]()
    val it = store.values().iterator()
    while(buffer.length < (100 + offset) && it.hasNext){
      val value = it.next()
      if(value.date.dayOfYear() == date.dayOfYear()) {
        buffer += value
      }
    }
    buffer.drop(offset)
    //store.values().filter(_.date == date).drop(offset).take(100).toSeq
  }

}
