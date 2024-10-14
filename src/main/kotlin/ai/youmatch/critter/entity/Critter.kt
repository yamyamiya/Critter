package ai.youmatch.critter.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.util.*
import kotlin.random.Random

@Entity
data class Critter (
        @Id
        val id: UUID =UUID.randomUUID(),
        @field:Column(unique = true)
        @field:NotBlank
        val name: String,
        val type: Type= Type.entries.toTypedArray().random(),
        @field:Min(1)
        @field:Max(100)
        val powerLevel: Int = Random.nextInt(1,101),
        val size: Size,
        @field:Min(1)
        @field:Max(100)
        val age: Int,
        val mood: Mood=calculateMood(powerLevel, age)
){
    companion object {
        fun calculateMood(powerLevel: Int, age: Int): Mood {
            // If age is 0, avoid division by zero by treating mood as just random
            val ageFactor = if (age > 0) (powerLevel.toDouble() / age) else 0.0
            val moodValue = Random.nextDouble(0.0, 0.5) + ageFactor

            return when {
                moodValue >= 0.75 -> Mood.HAPPY
                moodValue >= 0.5 -> Mood.ANGRY
                else -> Mood.SAD
            }
        }
    }
}

