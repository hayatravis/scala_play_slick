package models

import java.sql.Date

case class SendLogRow(id: Long, campaign_id: Long, ip: Long, ua: String, created: Date)
