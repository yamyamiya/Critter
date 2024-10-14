package ai.youmatch.critter.service

import ai.youmatch.critter.entity.Critter
import ai.youmatch.critter.entity.Mood
import ai.youmatch.critter.entity.Size
import ai.youmatch.critter.repository.CritterRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class CritterService (
        private val critterRepository: CritterRepository
){
    fun getAll():List<Critter> {
        return critterRepository.findAll()
    }

    fun getById(id: UUID):Critter? {
        return critterRepository.findById(id).orElseThrow {
            NoSuchElementException("Critter with ID $id not found")
        }
    }

    fun getByName(name: String):Critter? {
        return critterRepository.findByName(name).orElseThrow {
            NoSuchElementException("Critter with name $name not found")
        }
    }

    @Transactional
    fun add(name: String, size: Size, age: Int): Optional<UUID> {
        if (name.isBlank()) {
            throw IllegalArgumentException("Name cannot be blank")
        }
        if (age < 1 || age > 100) {
            throw IllegalArgumentException("Age must be between 0 and 100")
        }
        val existingCritter = critterRepository.findByName(name)
        return if (existingCritter.isPresent) {
            Optional.of(existingCritter.get().id)
        } else {
            val newCritter = Critter(name = name, size = size, age = age)
            val savedCritter = critterRepository.save(newCritter)
            Optional.of(savedCritter.id)
        }
    }

    @Transactional
    fun updateById(id: UUID, name: String, size: Size, age: Int): Critter {
        val existingCritter = critterRepository.findById(id).orElseThrow {
            NoSuchElementException("Critter with ID $id not found")
        }
        if (name.isBlank()) {
            throw IllegalArgumentException("Name cannot be blank")
        }
        if (age < 1 || age > 100) {
            throw IllegalArgumentException("Age must be between 0 and 100")
        }
        val updatedCritter = existingCritter.copy(
                name = name,
                size = size,
                age = age,
                mood = Critter.calculateMood(existingCritter.powerLevel, age)
        )
        return critterRepository.save(updatedCritter)
    }

    @Transactional
    fun updateByName(name: String, size: Size, age: Int): Critter {
        if (name.isBlank()) {
            throw IllegalArgumentException("Name cannot be blank")
        }
        val existingCritter = critterRepository.findByName(name).orElseThrow {
            NoSuchElementException("Critter with name $name not found")
        }
        if (age < 1 || age > 100) {
            throw IllegalArgumentException("Age must be between 0 and 100")
        }
        val updatedCritter = existingCritter.copy(
                size = size,
                age = age,
                mood = Critter.calculateMood(existingCritter.powerLevel, age)
        )
        return critterRepository.save(updatedCritter)
    }

    @Transactional
    fun deleteById(id: UUID) {
        if (!critterRepository.existsById(id)) {
            throw NoSuchElementException("Critter with ID $id not found")
        }
        critterRepository.deleteById(id)
    }

    @Transactional
    fun deleteByName(name: String) {
        val critterToDelete = critterRepository.findByName(name).orElseThrow {
            NoSuchElementException("Critter with name $name not found")
        }
        critterRepository.deleteById(critterToDelete.id)
    }
}