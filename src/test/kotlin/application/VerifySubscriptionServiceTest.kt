package application

import domain.Subscription
import domain.SubscriptionsRepository
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals


object VerifySubscriptionServiceTest : Spek({

    describe("execute service") {
        val fakeSubscription = Subscription("42", "5")
        val mockRepository by memoized { mockk<SubscriptionsRepository>() }
        val service by memoized { VerifySubscriptionService(mockRepository) }

        afterEach {
            clearMocks(mockRepository)
        }

        context("with matching subscription in Repository") {
            beforeEach {
                coEvery { mockRepository.findSubscriptions("42", "5") } returns fakeSubscription
            }

            it("should respond that subscription exists") {
                val response = runBlocking {
                    service.execute(VerifySubscriptionServiceRequest("42", "5"))
                }
                assertEquals(response, VerifySubscriptionServiceResponse.VerifySubscriptionServiceExists)
            }
        }

        context("with not matching subscription in Repository") {
            beforeEach {
                coEvery { mockRepository.findSubscriptions("42", "5") } returns null
            }

            it("should respond that subscription doesn't exist") {
                val response = runBlocking {
                    service.execute(VerifySubscriptionServiceRequest("42", "5"))
                }
                assertEquals(response, VerifySubscriptionServiceResponse.VerifySubscriptionServiceNotExists)
            }
        }
    }

})
