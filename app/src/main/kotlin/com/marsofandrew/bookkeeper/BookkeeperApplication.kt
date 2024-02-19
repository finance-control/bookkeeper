package com.marsofandrew.bookkeeper

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableAutoConfiguration
@SpringBootApplication
class BookkeeperApplication

fun main(args: Array<String>) {
    runApplication<BookkeeperApplication>(*args)
}