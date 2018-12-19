package service

import Subscription

interface SubscriptionsService {
    suspend fun findSubscriptions(userId: String, productId: String): Subscription?
}
