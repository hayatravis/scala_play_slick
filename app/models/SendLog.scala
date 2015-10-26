package models

import java.sql.Timestamp

case class SendLogRow(id: Option[Long], campaign_id: Long, ip: String, ua: String,
		created: Option[Timestamp], deleted: Option[Int])
