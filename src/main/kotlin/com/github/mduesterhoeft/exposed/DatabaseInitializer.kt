package com.github.mduesterhoeft.exposed

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.Ordered
import org.springframework.transaction.annotation.Transactional


data class ExposedTables(val tables: List<Table>)

interface DatabaseInitializer: ApplicationRunner, Ordered {
    override fun getOrder(): Int = DATABASE_INITIALIZER_ORDER

    companion object {
        const val DATABASE_INITIALIZER_ORDER = 0
    }
}

open class SimpleTransactionDatabaseInitializer(private val exposedTables: ExposedTables) : DatabaseInitializer {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(args: ApplicationArguments?) {
        log.info("creating Schema for tables '{}'", exposedTables.tables.map { it.tableName })
        transaction {
            log.info("ddl {}", exposedTables.tables.map { it.ddl }.joinToString())
            SchemaUtils.create(*exposedTables.tables.toTypedArray())
        }
    }
}

@Transactional
open class SpringTransactionDatabaseInitializer(private val exposedTables: ExposedTables): DatabaseInitializer {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(args: ApplicationArguments?) {
        log.info("creating Schema for tables '{}'", exposedTables.tables.map { it.tableName })

        log.info("ddl {}", exposedTables.tables.map { it.ddl }.joinToString())
        SchemaUtils.create(*exposedTables.tables.toTypedArray())
    }
}

