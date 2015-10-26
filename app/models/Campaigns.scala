package models

import java.sql.Timestamp

case class CampaignsRow(id: Option[Long], name: String, title: String, contents_text: String, destination_url: String,
                        created: Option[Timestamp], modified: Option[Timestamp], deleted: Option[Int], deleted_date: Option[Timestamp])