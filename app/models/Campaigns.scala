package models

import java.sql.Date

case class CampaignsRow(id: Long, name: String, title: String, contents_text: String, destination_url: String,
				created: Date, modified: Date, deleted: Int, deleted_date: Date)