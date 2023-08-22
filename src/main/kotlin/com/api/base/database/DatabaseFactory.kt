package com.api.base.database

import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val driverClassName = config.property("ktor.storage.driverClassName").getString()
        val jdbcURL = config.property("ktor.storage.jdbcURL").getString()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        val database = Database.connect(
            jdbcURL,
            driverClassName,
            databaseConfig = DatabaseConfig {
                sqlLogger = Slf4jSqlDebugLogger
            }
        )
        transaction(database) {
            SchemaUtils.create(Articles)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}


@Serializable
data class Article(val id: Int, val title: String, val body: String)

object Articles : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val body = varchar("body", 1024)
    override val primaryKey = PrimaryKey(id)
}