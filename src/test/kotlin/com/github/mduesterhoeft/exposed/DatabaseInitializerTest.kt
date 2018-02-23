package com.github.mduesterhoeft.exposed

import org.amshove.kluent.`should be`
import org.amshove.kluent.`should not be`
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [com.github.mduesterhoeft.exposed.Application::class, com.github.mduesterhoeft.exposed.TestConfiguration::class], properties = ["spring.exposed.generate-ddl=true"])
class DatabaseInitializerTest {

	@Autowired(required = false)
	var databaseInitializer: com.github.mduesterhoeft.exposed.SpringTransactionDatabaseInitializer? = null

	@Test @Transactional fun `should autoconfigure database`() {
		databaseInitializer `should not be` null
		com.github.mduesterhoeft.exposed.Things.selectAll().count() `should be` 0
	}
}

@Configuration
class TestConfiguration {
	@Bean fun exposedTables() : com.github.mduesterhoeft.exposed.ExposedTables {
		return com.github.mduesterhoeft.exposed.ExposedTables(listOf(com.github.mduesterhoeft.exposed.Things))
	}
}

object Things: Table() {
	var id = long("id").primaryKey().autoIncrement()
	var name = varchar("name", 100)
}
