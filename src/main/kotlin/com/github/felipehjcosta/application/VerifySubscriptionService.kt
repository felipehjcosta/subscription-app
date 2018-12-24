package com.github.felipehjcosta.application

import com.github.felipehjcosta.domain.Subscription
import com.github.felipehjcosta.domain.SubscriptionsRepository

data class VerifySubscriptionServiceRequest(val userId: String, val productId: String)

sealed class VerifySubscriptionServiceResponse {
    object VerifySubscriptionServiceExists : VerifySubscriptionServiceResponse()
    object VerifySubscriptionServiceNotExists : VerifySubscriptionServiceResponse()
}

class VerifySubscriptionService(private val repository: SubscriptionsRepository) {

    suspend fun execute(request: VerifySubscriptionServiceRequest): VerifySubscriptionServiceResponse {
        return when (repository.findSubscriptions(request.userId, request.productId)) {
            is Subscription -> VerifySubscriptionServiceResponse.VerifySubscriptionServiceExists
            else -> VerifySubscriptionServiceResponse.VerifySubscriptionServiceNotExists
        }
    }
}
