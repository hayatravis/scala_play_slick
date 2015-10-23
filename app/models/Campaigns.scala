package models

import java.sql.Date
case class CampaignsRow(id: Option[Long], name: String, title: String, contents_text: String, destination_url: String,
                        created: Option[Date], modified: Option[Date], deleted: Option[Int], deleted_date: Option[Date])