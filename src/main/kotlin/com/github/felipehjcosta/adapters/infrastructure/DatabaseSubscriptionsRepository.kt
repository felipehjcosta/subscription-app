package com.github.felipehjcosta.adapters.infrastructure

import com.github.felipehjcosta.domain.SubscriptionsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import com.github.felipehjcosta.domain.Subscription
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class DatabaseSubscriptionsRepository : SubscriptionsRepository {

    override suspend fun findSubscriptions(userId: String, productId: String): Subscription? = coroutineScope {
        val response = async {
            DatabaseFacade.dbQuery {
                com.github.felipehjcosta.adapters.infrastructure.SubscriptionTable
                    .select { SubscriptionTable.userId eq userId and (SubscriptionTable.productId eq productId) }
                    .limit(1)
                    .toList()
                    .map {
                        Subscription(
                            it[SubscriptionTable.userId],
                            it[SubscriptionTable.productId]
                        )
                    }
                    .firstOrNull()
            }
        }

        response.await()
    }

    override suspend fun all(): List<Subscription> = coroutineScope {
        val response = async {
            DatabaseFacade.dbQuery {
                com.github.felipehjcosta.adapters.infrastructure.SubscriptionTable
                    .selectAll()
                    .map {
                        Subscription(
                            it[SubscriptionTable.userId],
                            it[SubscriptionTable.productId]
                        )
                    }
            }
        }

        response.await()
    }
}
