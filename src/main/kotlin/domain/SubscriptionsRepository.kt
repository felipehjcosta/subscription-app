package domain

interface SubscriptionsRepository {
    suspend fun findSubscriptions(userId: String, productId: String): Subscription?
    suspend fun all(): List<Subscription>
}
