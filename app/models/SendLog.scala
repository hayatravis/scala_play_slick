package models

import java.sql.Date

case class SendLogRow(id: Option[Long], campaign_id: Long, ip: String, ua: String,
		created: Option[Date], deleted: Option[Int])
