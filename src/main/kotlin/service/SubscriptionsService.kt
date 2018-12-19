package service

import model.Subscription

interface SubscriptionsService {
    suspend fun findSubscriptions(userId: String, productId: String): Subscription?
}
