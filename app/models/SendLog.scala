package models

import java.sql.Date

case class SendLogRow(id: Option[Long], campaign_id: Long, ip: Long, ua: String)
