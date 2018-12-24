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

object ListSubscriptionServiceTest : Spek({
    describe("a service") {
        val fakeSubscription = Subscription("42", "5")
        val mockRepository by memoized { mockk<SubscriptionsRepository>() }
        val service by memoized { ListSubscriptionService(mockRepository) }

        afterEach {
            clearMocks(mockRepository)
        }

        context("with subscriptions in Repository") {
            beforeEach {
                coEvery { mockRepository.all() } returns listOf(fakeSubscription)
            }

            it("should return subscriptions") {
                val response = runBlocking { service.execute() }
                assertEquals(response.subscriptions, listOf(fakeSubscription))
            }
        }
    }
})
