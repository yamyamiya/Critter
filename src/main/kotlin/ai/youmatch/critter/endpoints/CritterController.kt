package ai.youmatch.critter.endpoints

import ai.youmatch.critter.dto.CritterDTO
import ai.youmatch.critter.entity.Critter
import ai.youmatch.critter.entity.Size
import ai.youmatch.critter.log
import ai.youmatch.critter.service.CritterService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/critter")
class CritterController(
        private val critterService: CritterService
) {

    @PostMapping
    fun add(
            @RequestBody critterDTO: CritterDTO
    ): ResponseEntity<UUID> {
        val response = ResponseEntity.ok(critterService.add(critterDTO.name, critterDTO.size, critterDTO.age))
        response.log.info("Critter with name ${critterDTO.name} is created")
        return response
    }

    @GetMapping("/id/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<Critter> {
        return ResponseEntity.ok(critterService.getById(id))
    }

    @GetMapping("/name/{name}")
    fun getByName(@PathVariable name: String): ResponseEntity<Critter> {
        return ResponseEntity.ok(critterService.getByName(name))
    }

    @GetMapping("/all")
    fun getAll(): ResponseEntity<List<Critter>> {
        return ResponseEntity.ok(critterService.getAll())
    }

    @PutMapping("/id/{id}")
    fun updateById(
            @PathVariable id: UUID,
            @RequestBody critterDTO: CritterDTO
    ): ResponseEntity<Critter> {
        val response = ResponseEntity.ok(critterService.updateById(id, critterDTO.name, critterDTO.size, critterDTO.age))
        response.log.info("Critter id: $id is updated")
        return response
    }

    @PutMapping("/name")
    fun updateByName(
            @RequestBody critterDTO: CritterDTO
    ): ResponseEntity<Critter> {
        val response = ResponseEntity.ok(critterService.updateByName(critterDTO.name, critterDTO.size, critterDTO.age))
        response.log.info("Critter name: ${critterDTO.name} is updated")
        return response
    }

    @DeleteMapping("/id/{id}")
    fun deleteById(@PathVariable id: UUID): ResponseEntity<Result<String>> {
        val response = ResponseEntity.ok(critterService.deleteById(id))
        response.log.info("Critter id: $id is deleted")
        return response
    }

    @DeleteMapping("/name/{name}")
    fun deleteByName(@PathVariable name: String): ResponseEntity<Result<String>> {
        val response = ResponseEntity.ok(critterService.deleteByName(name))
        response.log.info("Critter name: $name is deleted")
        return response
    }
}