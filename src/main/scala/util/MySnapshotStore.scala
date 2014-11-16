package util

import akka.persistence.{SelectedSnapshot, SnapshotMetadata, SnapshotSelectionCriteria}
import akka.persistence.snapshot.SnapshotStore

import scala.concurrent.Future


class MySnapshotStore extends SnapshotStore {
  override def loadAsync(persistenceId: String, criteria: SnapshotSelectionCriteria): Future[Option[SelectedSnapshot]] = ???

  override def saveAsync(metadata: SnapshotMetadata, snapshot: Any): Future[Unit] = ???

  override def saved(metadata: SnapshotMetadata): Unit = ???

  override def delete(metadata: SnapshotMetadata): Unit = ???

  override def delete(persistenceId: String, criteria: SnapshotSelectionCriteria): Unit = ???
}
