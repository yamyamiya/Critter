package ai.youmatch.critter

import ai.youmatch.critter.entity.Critter
import ai.youmatch.critter.entity.Size
import ai.youmatch.critter.repository.CritterRepository
import ai.youmatch.critter.service.CritterService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.NoSuchElementException

class CritterServiceTest {
    private lateinit var critterRepository: CritterRepository
    private lateinit var critterService: CritterService

    @BeforeEach
    fun setUp() {
        critterRepository = mockk()
        critterService = CritterService(critterRepository)
    }

    @Test
    fun `should get all critters`() {
        val critters = listOf(
                Critter(name = "Critter1", size = Size.SMALL, age = 5),
                Critter(name = "Critter2", size = Size.MEDIUM, age = 10)
        )

        every { critterRepository.findAll() } returns critters

        val result = critterService.getAll()

        assertEquals(2, result.size)
        assertEquals("Critter1", result[0].name)
        verify { critterRepository.findAll() }
    }

    @Test
    fun `should return critter by ID`() {
        val critter = Critter(name = "Critter1", size = Size.SMALL, age = 5)
        val critterId = critter.id

        every { critterRepository.findById(critterId) } returns Optional.of(critter)

        val result = critterService.getById(critterId)

        assertNotNull(result)
        assertEquals(critterId, result?.id)
        verify { critterRepository.findById(critterId) }
    }

    @Test
    fun `should throw NoSuchElementException when critter not found by ID`() {
        val critterId = UUID.randomUUID()

        every { critterRepository.findById(critterId) } returns Optional.empty()

        val exception = assertThrows<NoSuchElementException> {
            critterService.getById(critterId)
        }

        assertEquals("Critter with ID $critterId not found", exception.message)
        verify { critterRepository.findById(critterId) }
    }

    @Test
    fun `should return critter by name`() {
        val critter = Critter(name = "Critter1", size = Size.SMALL, age = 5)

        every { critterRepository.findByName("Critter1") } returns Optional.of(critter)

        val result = critterService.getByName("Critter1")

        assertNotNull(result)
        assertEquals("Critter1", result?.name)
        verify { critterRepository.findByName("Critter1") }
    }

    @Test
    fun `should throw NoSuchElementException when critter not found by name`() {
        every { critterRepository.findByName("Unknown") } returns Optional.empty()

        val exception = assertThrows<NoSuchElementException> {
            critterService.getByName("Unknown")
        }

        assertEquals("Critter with name Unknown not found", exception.message)
        verify { critterRepository.findByName("Unknown") }
    }

    @Test
    fun `should add new critter`() {
        val critter = Critter(name = "NewCritter", size = Size.SMALL, age = 5)

        every { critterRepository.findByName("NewCritter") } returns Optional.empty()
        every { critterRepository.save(any()) } returns critter

        val resultId = critterService.add(name = "NewCritter", size = Size.SMALL, age = 5)

        assertEquals(critter.id, resultId)
        verify { critterRepository.findByName("NewCritter") }
        verify { critterRepository.save(any()) }
    }

    @Test
    fun `should return existing critter ID if critter already exists by name`() {
        val existingCritter = Critter(name = "ExistingCritter", size = Size.SMALL, age = 10)

        every { critterRepository.findByName("ExistingCritter") } returns Optional.of(existingCritter)

        val resultId = critterService.add(name = "ExistingCritter", size = Size.SMALL, age = 10)

        assertEquals(existingCritter.id, resultId)
        verify { critterRepository.findByName("ExistingCritter") }
    }

    @Test
    fun `should update critter by ID`() {
        val existingCritter = Critter(name = "Critter1", size = Size.SMALL, age = 5)
        val updatedCritter = existingCritter.copy(size = Size.MEDIUM, age = 20)

        every { critterRepository.findById(existingCritter.id) } returns Optional.of(existingCritter)
        every { critterRepository.save(updatedCritter) } returns updatedCritter

        val result = critterService.updateById(existingCritter.id, "Critter1", Size.MEDIUM, 20)

        assertEquals(updatedCritter.size, result.size)
        assertEquals(updatedCritter.age, result.age)
        verify { critterRepository.findById(existingCritter.id) }
        verify { critterRepository.save(updatedCritter) }
    }

    @Test
    fun `should delete critter by ID`() {
        val critterId = UUID.randomUUID()

        every { critterRepository.findById(critterId) } returns Optional.of(Critter(name = "ToDelete", size = Size.SMALL, age = 5))
        every { critterRepository.deleteById(critterId) } returns Unit

        val result = critterService.deleteById(critterId)

        assertEquals("User successfully deleted", result.getOrNull())
        verify { critterRepository.findById(critterId) }
        verify { critterRepository.deleteById(critterId) }
    }

    @Test
    fun `should return message if trying to delete non-existing critter by ID`() {
        val critterId = UUID.randomUUID()

        every { critterRepository.findById(critterId) } returns Optional.empty()

        val result = critterService.deleteById(critterId)

        assertEquals("Such user does not exist", result.getOrNull())
        verify { critterRepository.findById(critterId) }
    }
}