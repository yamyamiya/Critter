package ai.youmatch.critter

import ai.youmatch.critter.entity.Critter
import ai.youmatch.critter.entity.Mood
import ai.youmatch.critter.entity.Size
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CritterTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setUp() {
        val factory: ValidatorFactory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(ParameterMessageInterpolator())  // Use ParameterMessageInterpolator
                .buildValidatorFactory()
        validator = factory.validator
    }

    @Test
    fun `should pass validation for valid critter`() {
        val critter = Critter(
                name = "Fluffy",
                size = Size.MEDIUM,
                age = 25
        )
        val violations = validator.validate(critter)
        assertTrue(violations.isEmpty(), "No validation violations should occur for a valid Critter")
    }
    @Test
    fun `should fail validation when name is blank`() {
        val critter = Critter(
                name = "",
                size = Size.SMALL,
                age = 15
        )
        val violations = validator.validate(critter)
        assertFalse(violations.isEmpty())

        val nameViolation = violations.firstOrNull { it.propertyPath.toString() == "name" }
        assertNotNull(nameViolation)
        assertEquals("must not be blank", nameViolation?.message)
    }

    @Test
    fun `should fail validation for age less than 1`() {
        val critter = Critter(
                name = "Tiny",
                size = Size.SMALL,
                age = 0
        )

        val violations = validator.validate(critter)
        assertFalse(violations.isEmpty(), "Validation should fail for invalid age")

        val ageViolation = violations.firstOrNull { it.propertyPath.toString() == "age" }
        assertNotNull(ageViolation)
        assertEquals("must be greater than or equal to 1", ageViolation?.message)
    }

    @Test
    fun `should fail validation for age more than 100`() {
        val critter = Critter(
                name = "Tiny",
                size = Size.SMALL,
                age = 200
        )

        val violations = validator.validate(critter)
        assertFalse(violations.isEmpty(), "Validation should fail for invalid age")

        val ageViolation = violations.firstOrNull { it.propertyPath.toString() == "age" }
        assertNotNull(ageViolation)
        assertEquals("must be less than or equal to 100", ageViolation?.message)
    }

    @Test
    fun `should have valid power level between 1 and 100`() {
        val critter = Critter(
                name = "Fang",
                size = Size.LARGE,
                age = 30
        )
        assertTrue(critter.powerLevel in 1..100)
    }

    @Test
    fun `should calculate correct mood`() {
        val critter = Critter(
                name = "Sparky",
                size = Size.MEDIUM,
                age = 10,
                powerLevel = 80
        )
        assertEquals(Mood.HAPPY, critter.mood)
    }
}