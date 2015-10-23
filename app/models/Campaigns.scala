package models

import java.sql.Date

case class CampaignsRow(id: Option[Long], name: String, title: String, contents_text: String, destination_url: String)