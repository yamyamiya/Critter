package ai.youmatch.critter.endpoints

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
            @RequestParam name: String,
            @RequestParam size: Size,
            @RequestParam age: Int
    ): ResponseEntity<UUID> {
        val response = ResponseEntity.ok(critterService.add(name, size, age))
        response.log.info("Critter with name $name is created")
        return response
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<Critter> {
        return ResponseEntity.ok(critterService.getById(id))
    }

    @GetMapping("/{name}")
    fun getByName(@PathVariable name: String): ResponseEntity<Critter> {
        return ResponseEntity.ok(critterService.getByName(name))
    }

    @GetMapping("/all")
    fun getAll(): ResponseEntity<List<Critter>> {
        return ResponseEntity.ok(critterService.getAll())
    }

    @PutMapping("/{id}")
    fun updateById(
            @PathVariable id: UUID,
            @RequestParam name: String,
            @RequestParam size: Size,
            @RequestParam age: Int
    ): ResponseEntity<Critter> {
        val response = ResponseEntity.ok(critterService.updateById(id, name, size, age))
        response.log.info("Critter id: $id is updated")
        return response
    }

    @PutMapping("/{name}")
    fun updateByName(
            @PathVariable name: String,
            @RequestParam size: Size,
            @RequestParam age: Int
    ): ResponseEntity<Critter> {
        val response = ResponseEntity.ok(critterService.updateByName(name, size, age))
        response.log.info("Critter name: $name is updated")
        return response
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: UUID): ResponseEntity<Result<String>> {
        val response = ResponseEntity.ok(critterService.deleteById(id))
        response.log.info("Critter id: $id is deleted")
        return response
    }

    @DeleteMapping("/{name}")
    fun deleteByName(@PathVariable name: String): ResponseEntity<Result<String>> {
        val response = ResponseEntity.ok(critterService.deleteByName(name))
        response.log.info("Critter name: $name is deleted")
        return response
    }
}