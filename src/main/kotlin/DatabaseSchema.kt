import org.jetbrains.exposed.dao.IntIdTable

object SubscriptionTable : IntIdTable() {
    val userId = varchar("user_id", length = 255)
    val projectId = varchar("project_id", length = 255)
}