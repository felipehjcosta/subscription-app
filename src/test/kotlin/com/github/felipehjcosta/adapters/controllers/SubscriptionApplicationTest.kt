package com.github.felipehjcosta.adapters.controllers

import com.github.felipehjcosta.application.ListSubscriptionResponse
import com.github.felipehjcosta.application.ListSubscriptionService
import com.github.felipehjcosta.application.VerifySubscriptionService
import com.github.felipehjcosta.application.VerifySubscriptionServiceRequest
import com.github.felipehjcosta.application.VerifySubscriptionServiceResponse
import com.github.felipehjcosta.domain.Subscription
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object SubscriptionApplicationTest : Spek({
    describe("Application") {
        val fakeSubscription = Subscription("42", "5")
        val mockVerifySubscriptionService by memoized { mockk<VerifySubscriptionService>() }
        val mockListSubscriptionService by memoized { mockk<ListSubscriptionService>() }

        afterEach {
            clearMocks(mockVerifySubscriptionService, mockListSubscriptionService)
        }

        it("should list all subscriptions") {
            coEvery { mockListSubscriptionService.execute() } returns ListSubscriptionResponse(
                listOf(
                    fakeSubscription
                )
            )

            withTestApplication({
                moduleWithDependencies(
                    mockVerifySubscriptionService,
                    mockListSubscriptionService
                )
            }) {
                with(handleRequest(HttpMethod.Get, "subscription")) {
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertEquals("""[{"userId":"42","productId":"5"}]""", response.content)
                }
            }
        }

        context("when verify subscription") {

            it("should return 404") {
                coEvery {
                    mockVerifySubscriptionService.execute(any())
                } answers {
                    VerifySubscriptionServiceResponse.VerifySubscriptionServiceNotExists
                }

                withTestApplication({
                    moduleWithDependencies(
                        mockVerifySubscriptionService,
                        mockListSubscriptionService
                    )
                }) {
                    with(handleRequest(HttpMethod.Post, "subscription/verify") {
                        setBody("""{"userId":"42","productId":"6"}""")
                    }) {
                        assertEquals(HttpStatusCode.NotFound, response.status())
                        assertEquals("Invalid Subscription", response.content)
                    }
                }
            }

            it("should return 200") {
                coEvery {
                    mockVerifySubscriptionService.execute(VerifySubscriptionServiceRequest("42", "5"))
                } returns VerifySubscriptionServiceResponse.VerifySubscriptionServiceExists

                withTestApplication({
                    moduleWithDependencies(
                        mockVerifySubscriptionService,
                        mockListSubscriptionService
                    )
                }) {
                    with(handleRequest(HttpMethod.Post, "subscription/verify") {
                        setBody("""{"userId":"42","productId":"5"}""")
                    }) {
                        assertEquals(HttpStatusCode.OK, response.status())
                    }
                }
            }
        }
    }
})
