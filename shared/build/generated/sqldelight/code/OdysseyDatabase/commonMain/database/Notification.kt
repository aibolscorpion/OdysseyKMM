package database

import kotlin.Long
import kotlin.String

public data class Notification(
  public val notification_id: String,
  public val notification_type: String,
  public val notifiable_type: String,
  public val created_at: String,
  public val updated_at: String,
  public val read_at: String?,
  public val id: Long,
  public val title: String,
  public val content: String,
  public val is_important: Long,
  public val type: String,
  public val application_id: Long,
  public val segment_id: Long?,
)
