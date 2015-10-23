package dao

import slick.driver.H2Driver.api._
import models.SendLogRow

class SendLogTable(tag: Tag) extends BaseTable[SendLogRow] (tag, "SENDLOG") {
	def campaign_id = column[Long]("CAMPAIGN_ID")
	def ip = column[Long]("IP")
	def ua = column[String]("UA")

	def * = (id.?, campaign_id, ip, ua) <> (SendLogRow.tupled, SendLogRow.unapply)
}

object SendLogDAO extends BaseDao[SendLogTable, SendLogRow] {
	val tableQuery = TableQuery[SendLogTable]
}