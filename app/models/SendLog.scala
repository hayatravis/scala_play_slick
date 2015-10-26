package models

import java.sql.{Date, Timestamp}

case class SendLogRow(id: Option[Long], campaign_id: Long, ip: String, ua: String, send_date: Date,
		created: Option[Timestamp], deleted: Option[Int])
