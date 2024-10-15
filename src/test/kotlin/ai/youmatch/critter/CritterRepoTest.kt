package ai.youmatch.critter

import ai.youmatch.critter.entity.Critter
import ai.youmatch.critter.entity.Size
import ai.youmatch.critter.repository.CritterRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import kotlin.test.assertFailsWith
@SpringBootTest
class CritterRepoTest {
    @Autowired
    private lateinit var critterRepository: CritterRepository

    @Test
    fun `should save valid critter`() {
        val critter = Critter(
                name = "Drago",
                size = Size.LARGE,
                age = 25
        )

        val savedCritter = critterRepository.save(critter)
        val foundCritter = critterRepository.findById(savedCritter.id)
        assertTrue(foundCritter.isPresent)
        assertEquals("Drago", foundCritter.get().name)
    }

    @Test
    fun `should throw exception when name is not unique`() {
        val critter1 = Critter(
                name = "Phoenix",
                size = Size.MEDIUM,
                age = 20
        )
        val critter2 = Critter(
                name = "Phoenix",
                size = Size.SMALL,
                age = 15
        )

        critterRepository.save(critter1)
        assertFailsWith<DataIntegrityViolationException> {
            critterRepository.save(critter2)
        }
    }
}