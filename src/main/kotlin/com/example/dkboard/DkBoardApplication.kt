package com.example.dkboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class DkBoardApplication

fun main(args: Array<String>) {
    runApplication<DkBoardApplication>(*args)
}
