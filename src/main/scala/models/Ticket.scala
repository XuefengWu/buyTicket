package models

import java.util.UUID

import org.joda.time.DateTime


case class Ticket(id: UUID, date: DateTime, train: String, siteNo: String)
