package com.example.simplespringbatch

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class HelloJobConfiguration(
        private val jobRepository: JobRepository,
        private val transactionManager: PlatformTransactionManager
) {

    @Bean
    fun helloJob(): Job {
        return JobBuilder("helloJob", jobRepository)
                .start(helloStep1())
                .next(helloStep2())
                .build()
    }

    @Bean
    fun helloStep1(): Step {
        return StepBuilder("helloStep1", jobRepository)
                .tasklet(helloTasklet1(), transactionManager)
                .build()
    }

    @Bean
    fun helloStep2(): Step {
        return StepBuilder("helloStep2", jobRepository)
                .tasklet(helloTasklet2(), transactionManager)
                .build()
    }

    @Bean
    fun helloTasklet1(): Tasklet {
        return Tasklet { contribution, chunkContext ->
            println("======================")
            println(">> Hello, Spring Batch!")
            println("======================")
            RepeatStatus.FINISHED
        }
    }

    @Bean
    fun helloTasklet2(): Tasklet {
        return Tasklet { contribution, chunkContext ->
            println("====================")
            println(">> step2 was executed")
            println("====================")
            RepeatStatus.FINISHED
        }
    }
}