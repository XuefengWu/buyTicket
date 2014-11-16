package actors

import akka.actor.{Actor, Props}
import db.MyDao
import messages.QueryRequest
import util.JsonConvert

import scala.util.Random


object QueryActor {
  def props = Props(new QueryActor())
}

class QueryActor extends Actor with JsonConvert {

  override def receive: Receive = {
    case QueryRequest(date, requester) => requester ! toJson(MyDao.query(date, Random.nextInt(100000)))
  }

}
