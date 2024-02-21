package com.example.simplespringbatch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableBatchProcessing
@SpringBootApplication
class SimpleSpringBatchApplication

fun main(args: Array<String>) {
	runApplication<SimpleSpringBatchApplication>(*args)
}
