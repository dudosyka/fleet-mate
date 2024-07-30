package com.fleetmate.lib.utils.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import com.fleetmate.lib.conf.AppConf.database

class DatabaseConnector(vararg tables: Table, initializer: Transaction.() -> Unit) {

    init {
        connect()
        transactionOverride {
            tables.forEach {
                SchemaUtils.create(it)
            }
            initializer()
            commit()
            close()
        }
    }
    companion object {
        private lateinit var hikariDataSource: HikariDataSource
        private fun connect() {
            hikariDataSource = HikariDataSource(
                HikariConfig().apply {
                    driverClassName = database.driver
                    jdbcUrl = database.url
                    username = database.user
                    password = database.password
                    maximumPoolSize = 1
                    minimumIdle = 1
                    maxLifetime = 600000
                    keepaliveTime = 30000
                    isAutoCommit = false
                    leakDetectionThreshold = 10000
                    transactionIsolation = "TRANSACTION_READ_COMMITTED"
//                    validate()
                }
            )
            TransactionManager.defaultDatabase = Database.connect(
                hikariDataSource,
                databaseConfig = DatabaseConfig.invoke {
                    maxEntitiesToStoreInCachePerEntity = 0
                }
            )
        }
    }
}