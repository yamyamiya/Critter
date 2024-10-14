package ai.youmatch.critter.endpoints

import ai.youmatch.critter.entity.Critter
import ai.youmatch.critter.entity.Size
import ai.youmatch.critter.service.CritterService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.collections.List

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
        return ResponseEntity.ok(critterService.add(name, size, age))
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
        return ResponseEntity.ok(critterService.updateById(id, name, size, age))
    }

    @PutMapping("/{name}")
    fun updateByName(
            @PathVariable name: String,
            @RequestParam size: Size,
            @RequestParam age: Int
    ): ResponseEntity<Critter> {
        return ResponseEntity.ok(critterService.updateByName(name, size, age))
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: UUID): ResponseEntity<Result<String>> {
        return ResponseEntity.ok(critterService.deleteById(id))
    }

    @DeleteMapping("/{name}")
    fun deleteByName(@PathVariable name: String): ResponseEntity<Result<String>> {
        return ResponseEntity.ok(critterService.deleteByName(name))
    }
}