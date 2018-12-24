package com.github.felipehjcosta.application

import com.github.felipehjcosta.domain.Subscription
import com.github.felipehjcosta.domain.SubscriptionsRepository


data class ListSubscriptionResponse(val subscriptions: List<Subscription>)

class ListSubscriptionService(private val subscriptionsRepository: SubscriptionsRepository) {
    suspend fun execute(): ListSubscriptionResponse =
        ListSubscriptionResponse(subscriptionsRepository.all())
}
