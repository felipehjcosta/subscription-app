package service

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFacade {

    private const val MAXIMUM_POOL_SIZE = 3

    private val database: Database by lazy {
        Database.connect(hikari()).apply {
            transaction(this) {
                SchemaUtils.create(SubscriptionTable)

                SubscriptionTable.insert { table ->
                    table[userId] = "42"
                    table[projectId] = "5"
                }
            }
        }
    }

    private fun hikari(): HikariDataSource {
        return with(HikariConfig()) {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:test"
            maximumPoolSize = MAXIMUM_POOL_SIZE
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
            HikariDataSource(this)
        }
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction(database) {
                block()
            }
        }
}
