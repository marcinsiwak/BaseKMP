package pl.msiwak.database

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(url: String, user: String, password: String) {

        Flyway.configure().baselineOnMigrate(true).dataSource(url, user, password).load().also {
            it.migrate()
        }

        val database = Database.connect(
            url = url,
            user = user,
            password = password
        )

        transaction(database) {
            // insert tables
            SchemaUtils.createMissingTablesAndColumns()
        }
    }
}