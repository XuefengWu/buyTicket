package util

import java.util.UUID

import org.scalatest._


class JsonConvertSpec extends FlatSpec with Matchers  with JsonConvert {

  "to UUIDs" should "ok" in {
    val ids = """["26079e20-1680-4873-a684-9f62ede3822f","26079e20-1680-4873-a684-9f62ede3823e"]"""
    toUUIDs(ids) should be (List(UUID.fromString("26079e20-1680-4873-a684-9f62ede3822f"), UUID.fromString("26079e20-1680-4873-a684-9f62ede3823e")))
  }

}
