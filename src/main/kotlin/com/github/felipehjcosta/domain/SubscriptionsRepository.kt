package com.github.felipehjcosta.domain

interface SubscriptionsRepository {
    suspend fun findSubscriptions(userId: String, productId: String): Subscription?
    suspend fun all(): List<Subscription>
}
