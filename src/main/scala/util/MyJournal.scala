package util

import akka.persistence.journal.SyncWriteJournal
import akka.persistence.{PersistentConfirmation, PersistentId, PersistentRepr}

import scala.collection.immutable.Seq
import scala.concurrent.Future


class MyJournal extends SyncWriteJournal {

  override def writeMessages(messages: Seq[PersistentRepr]): Unit = {
    println("=======writeMessages=========")
    messages.foreach(println)
    println("=======writeMessages=========")
  }

  override def deleteMessagesTo(persistenceId: String, toSequenceNr: Long, permanent: Boolean): Unit = {
    println("=======deleteMessagesTo=========")
  }

  @deprecated("deleteMessages will be removed.")
  override def deleteMessages(messageIds: Seq[PersistentId], permanent: Boolean): Unit = println("=======deleteMessages=========")

  @deprecated("writeConfirmations will be removed, since Channels will be removed.")
  override def writeConfirmations(confirmations: Seq[PersistentConfirmation]): Unit = println("=======writeConfirmations=========")

  override def asyncReadHighestSequenceNr(persistenceId: String, fromSequenceNr: Long): Future[Long] = ???

  override def asyncReplayMessages(persistenceId: String, fromSequenceNr: Long, toSequenceNr: Long, max: Long)(replayCallback: (PersistentRepr) => Unit): Future[Unit] = ???

}
