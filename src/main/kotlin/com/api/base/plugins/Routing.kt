package com.api.base.plugins

import com.api.base.database.Article
import com.api.base.database.Articles
import com.api.base.database.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.swagger.codegen.v3.generators.html.StaticHtml2Codegen
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

fun Application.configureRouting() {
    routing {
        openAPI(path = "openapi") {
            codegen = StaticHtml2Codegen()
        }
        swaggerUI(path = "swagger")

        get("/") {
            call.respondText("Hello World!")
        }
        get("/articles") {
            val result = DatabaseFactory.dbQuery {
                Articles
                    .selectAll()
                    .map { row ->
                        Article(
                            id = row[Articles.id],
                            title = row[Articles.title],
                            body = row[Articles.body],
                        )
                    }
            }
            call.respond(result)
        }
        post("/article") {
            DatabaseFactory.dbQuery {
                val insert = Articles.insert {
                    it[Articles.title] = "My first Article"
                    it[Articles.body] = "With a really nice content"
                }

                val result = insert.resultedValues?.map { row ->
                    Article(
                        id = row[Articles.id],
                        title = row[Articles.title],
                        body = row[Articles.body],
                    )
                }?.first()

                call.respond(result!!)
            }
        }

        get("/token") {
            call.respondText("abc123")
        }

        authenticate {
            get("/protected") {
                call.respondText("Accessing protected API")
            }
        }
    }
}
