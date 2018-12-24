package com.github.felipehjcosta.adapters.infrastructure

import com.github.felipehjcosta.domain.Subscription
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

object DatabaseSubscriptionsRepositoryTest : Spek({
    describe("DatabaseSubscriptionsRepository") {
        val databaseSubscriptionsRepository by memoized { DatabaseSubscriptionsRepository() }

        context("when find subscription") {
            it("should return non null") {
                val subscriptions = runBlocking {
                    databaseSubscriptionsRepository.findSubscriptions("42", "5")
                }
                assertNotNull(subscriptions)
            }

            it("should return null") {
                val subscriptions = runBlocking {
                    databaseSubscriptionsRepository.findSubscriptions("42", "6")
                }
                assertNull(subscriptions)
            }
        }

        context("when get all subscriptions") {
            it("should return all") {
                val subscriptions = runBlocking {
                    databaseSubscriptionsRepository.all()
                }
                assertEquals(listOf(Subscription("42", "5")), subscriptions)
            }
        }
    }
})