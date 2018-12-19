package service

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import model.Subscription
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

class DatabaseSubscriptionsService : SubscriptionsService {

    override suspend fun findSubscriptions(userId: String, productId: String): Subscription? = coroutineScope {
        val response = async {
            DatabaseFacade.dbQuery {
                service.SubscriptionTable
                    .select { SubscriptionTable.userId eq userId and (SubscriptionTable.projectId eq productId) }
                    .limit(1)
                    .toList()
                    .map { Subscription(it[SubscriptionTable.userId], it[SubscriptionTable.projectId]) }
                    .firstOrNull()
            }
        }

        response.await()
    }
}
