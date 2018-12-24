package application

import domain.Subscription
import domain.SubscriptionsRepository


data class ListSubscriptionResponse(val subscriptions: List<Subscription>)

class ListSubscriptionService(private val subscriptionsRepository: SubscriptionsRepository) {
    suspend fun execute(): ListSubscriptionResponse = ListSubscriptionResponse(subscriptionsRepository.all())
}
