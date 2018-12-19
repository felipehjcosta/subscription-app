interface SubscriptionsApi {
    suspend fun findSubscriptions(userId: String, productId: String): Subscription?
}