package ai.youmatch.critter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CritterProjectApplication

fun main(args: Array<String>) {
	runApplication<CritterProjectApplication>(*args)
}
