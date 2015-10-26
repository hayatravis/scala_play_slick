package dao

import slick.driver.H2Driver.api._
import models.SendLogRow
import java.sql.Date

class SendLogTable(tag: Tag) extends BaseTable[SendLogRow] (tag, "SENDLOG") {
	def campaign_id = column[Long]("CAMPAIGN_ID")
	def ip = column[String]("IP")
	def ua = column[String]("UA")
	def send_date = column[Date]("SEND_DATE")

	def * = (id.?, campaign_id, ip, ua, send_date, created.?, deleted.?) <> (SendLogRow.tupled, SendLogRow.unapply)
}

object SendLogDAO extends BaseDao[SendLogTable, SendLogRow] {
	val tableQuery = TableQuery[SendLogTable]
	val campaignsTableQuery = TableQuery[CampaignsTable]

	def getAllLogWithCampaignName() = {
		val q = for {
			s <- tableQuery
			c <- campaignsTableQuery if s.campaign_id === c.id
		} yield (s, c.name)
		db.run(q.result)
	}

	def getLogByCampaignIdWithCampaignName(id: Long) = {
		val q = for {
			s <- tableQuery
			c <- campaignsTableQuery if s.campaign_id === c.id && s.campaign_id === id
		} yield (s, c.name)
		db.run(q.result)
	}

	def getDailyReport(id: Long) = {
		val q = (for {
			s <- tableQuery
			c <- campaignsTableQuery if s.campaign_id === c.id && s.campaign_id === id
		} yield (s, c.name)).groupBy(
			_._1.send_date
		).map({
			// count id
			case (send_date, id) => (send_date, id.length)
		})
		db.run(q.result)
	}
}