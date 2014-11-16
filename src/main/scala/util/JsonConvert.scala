package util

import java.util.UUID

import akka.stream.scaladsl2.{FlowMaterializer, Source}
import akka.util.ByteString
import models.Ticket
import play.api.libs.json._

import scala.concurrent.Await
import scala.concurrent.duration._

trait JsonConvert {


  implicit val ticketFormat = Json.format[Ticket]

  def toJson(tickets:  Seq[Ticket]): String = {
    Json.toJson(tickets).toString()
  }

  def uuidToJson(ids: Seq[UUID]): String = {
    Json.toJson(ids).toString()
  }

  def toUUIDs(ids: String): Seq[UUID] = {
    Json.parse(ids).as[Seq[UUID]]
  }

  def toUUIDs(dataBytes: Source[ByteString])(implicit materializer: FlowMaterializer): Seq[UUID] = toUUIDs(Await.result(dataBytes.fold("")((acc, el) => acc + el.utf8String),10.millis))

}
