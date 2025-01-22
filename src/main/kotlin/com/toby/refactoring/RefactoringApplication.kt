package com.toby.refactoring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RefactoringApplication

fun main(args: Array<String>) {
	println("Hello, Refactoring!")
	runApplication<RefactoringApplication>(*args)
}
