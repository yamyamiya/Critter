package ai.youmatch.critter

import ai.youmatch.critter.dto.CritterDTO
import ai.youmatch.critter.endpoints.CritterController
import ai.youmatch.critter.entity.Critter
import ai.youmatch.critter.entity.Size
import ai.youmatch.critter.service.CritterService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.MockKAnnotations
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*
@WebFluxTest(CritterController::class)
class CritterControllerTest {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Mock
    private lateinit var critterService: CritterService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var critterDTO: CritterDTO
    private lateinit var critter: Critter

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        critterDTO = CritterDTO(name = "Critter1", size = Size.MEDIUM, age = 10)
        critter = Critter(name = "Critter1", size = Size.MEDIUM, age = 10)
    }

    @Test
    fun `should add new critter`() {
        val critterId = UUID.randomUUID()

        every { critterService.add(critterDTO.name, critterDTO.size, critterDTO.age) } returns critterId

        webTestClient.post()
                .uri("/critter")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(critterDTO)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isEqualTo(critterId.toString())
    }

    @Test
    fun `should return critter by id`() {
        val critterId = critter.id

        every { critterService.getById(critterId) } returns critter

        webTestClient.get()
                .uri("/critter/id/$critterId")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json(objectMapper.writeValueAsString(critter))
    }

    @Test
    fun `should return 404 when critter not found by id`() {
        val critterId = UUID.randomUUID()

        every { critterService.getById(critterId) } throws NoSuchElementException("Critter with ID $critterId not found")

        webTestClient.get()
                .uri("/critter/id/$critterId")
                .exchange()
                .expectStatus().isNotFound
                .expectBody(String::class.java)
                .isEqualTo("Critter with ID $critterId not found")
    }

    @Test
    fun `should return critter by name`() {
        every { critterService.getByName("Critter1") } returns critter

        webTestClient.get()
                .uri("/critter/name/Critter1")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json(objectMapper.writeValueAsString(critter))
    }

    @Test
    fun `should return 404 when critter not found by name`() {
        every { critterService.getByName("Critter1") } throws NoSuchElementException("Critter with name Critter1 not found")

        webTestClient.get()
                .uri("/critter/id/Critter1")
                .exchange()
                .expectStatus().isNotFound
                .expectBody(String::class.java)
                .isEqualTo("Critter with name Critter1 not found")
    }

    @Test
    fun `should return all critters`() {
        val critters = listOf(critter)

        every { critterService.getAll() } returns critters

        webTestClient.get()
                .uri("/critter/all")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json(objectMapper.writeValueAsString(critters))
    }

    @Test
    fun `should update critter by id`() {
        val critterId = critter.id
        val updatedCritter = critter.copy(age = 20)

        every { critterService.updateById(critterId, critterDTO.name, critterDTO.size, 20) } returns updatedCritter

        webTestClient.put()
                .uri("/critter/id/$critterId")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(critterDTO.copy(age = 20))
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json(objectMapper.writeValueAsString(updatedCritter))
    }

    @Test
    fun `should delete critter by id`() {
        val critterId = critter.id

        every { critterService.deleteById(critterId) } returns Result.success("User successfully deleted")

        webTestClient.delete()
                .uri("/critter/id/$critterId")
                .exchange()
                .expectStatus().isOk
                .expectBody(String::class.java)
                .isEqualTo("User successfully deleted")
    }

    @Test
    fun `should return 404 when deleting non-existing critter by id`() {
        val critterId = UUID.randomUUID()

        every { critterService.deleteById(critterId) } returns Result.success("Such user does not exist")

        webTestClient.delete()
                .uri("/critter/id/$critterId")
                .exchange()
                .expectStatus().isOk
                .expectBody(String::class.java)
                .isEqualTo("Such user does not exist")
    }
}