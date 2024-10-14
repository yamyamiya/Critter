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
        try {
            val сritterId = critterService.add(name, size, age)
            return if (сritterId.isPresent) {
                ResponseEntity.status(HttpStatus.CONFLICT).body(сritterId.get())
            } else {
                ResponseEntity.status(HttpStatus.CREATED).body(сritterId.get())
            }
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<Critter> {
        return try{
            val critter = critterService.getById(id)
            ResponseEntity.ok(critter)
        } catch (e: NoSuchElementException){
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/{name}")
    fun getByName(@PathVariable name: String): ResponseEntity<Critter> {
        return try{
            val critter = critterService.getByName(name)
            ResponseEntity.ok(critter)
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/all")
    fun getAll(): ResponseEntity<List<Critter>> {
        return try {
            val list = critterService.getAll()
            ResponseEntity.ok(list)
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PutMapping("/{id}")
    fun updateById(
            @PathVariable id: UUID,
            @RequestParam name: String,
            @RequestParam size: Size,
            @RequestParam age: Int
    ): ResponseEntity<Critter> {
        return try {
            val updatedCritter = critterService.updateById(id, name, size, age)
            return ResponseEntity.ok(updatedCritter)
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PutMapping("/{name}")
    fun updateByName(
            @PathVariable name: String,
            @RequestParam size: Size,
            @RequestParam age: Int
    ): ResponseEntity<Critter> {
        return try {
            val updatedCritter = critterService.updateByName(name, size, age)
            return ResponseEntity.ok(updatedCritter)
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: UUID): ResponseEntity<Void> {
        return try {
            critterService.deleteById(id)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @DeleteMapping("/{name}")
    fun deleteByName(@PathVariable name:String): ResponseEntity<Void> {
        return try {
            critterService.deleteByName(name)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}